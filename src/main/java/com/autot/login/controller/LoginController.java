package com.autot.login.controller;

import com.autot.login.service.LoginService;
import com.autot.log.service.LogService;
import com.autot.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.nurigo.java_sdk.Coolsms;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @prjectName		: SWM 21 자율주행 서비스 플랫폼 autot   
 * @Class			: LoginController
 * @since			: 2021. 7. 14.
 * @author			: 김은상
 * @Description		: 어풀 초기 접근, 로그인, 로그아웃, 중복확인(이메일), 휴대폰인증 (발송/확인), 회원가입, 자동로그인  관리 프로그램을 관리하는 Controller
 * @Modification Information
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2021. 7. 14.		김은상       신규개발
 * 
 * @Copyright (C) 휴코어(주) All right reserved.
 * <pre>
 */
@RestController
public class LoginController {
	
	// slf4j 로깅 파사드를 통해 logback 로깅 모듈을 지원
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
    private LogService logService;
    
    private LoginService loginService;

    private UserService userService;
    
    public LoginController(LogService logService, LoginService loginService, UserService userService){
        this.logService = logService;
        this.loginService = loginService;
        this.userService = userService;
    }

	/**
	 * @MethodName		: getInit
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: usrmobtel [사용자모바일전화번호]
	 * @return			: trminfInfo[약관정보], usrTrmchkInfo[사용자약관정보]
	 * @Description		: 어플 시작시 단말기 정보로 사용자 등록여부	
	 * @throws DefaultException
	 */
    @PostMapping("/init")
    public @ResponseBody Map<String, Object> getInit(@RequestBody Map<String, Object> m) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("trminfInfo", loginService.getTrminf());
    	resultMap.put("usrTrmchkInfo", loginService.getUsrTrmchk(m));
    	resultMap.put("regInfo", loginService.getReg());
    	
        return resultMap;
    }

	/**
	 * @MethodName		: getLogin
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: usrid [USREML(사용자Email), USRMOBTEL(사용자모바일전화번호)]
	 * 					  pwd [비밀번호]
	 * @return			: userInfo[사용자정보]
	 * @Description		: 로그인	
	 * @throws DefaultException
	 */
    @PostMapping("/login")
    public @ResponseBody Map<String, Object> getLogin(@RequestBody Map<String, Object> m) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	Map<String, Object> userInfo = new HashMap<String, Object>();
    	userInfo = userService.getLogin(m);
    	
    	if(userInfo.size() == 0 || userInfo == null ) {
    		resultMap.put("resultMsg", "사용자정보가 없읍니다.");
        	resultMap.put("result", "FAILURE");
    	}else {
    		if(!m.get("pwd").toString().equals(userInfo.get("usrpwd"))){
        		resultMap.put("resultMsg", "비밀번호가 일치하지 않습니다.");
            	resultMap.put("result", "FAILURE");    			
    		}else {
        		resultMap.put("resultMsg", "정상처리되었습니다.");
    	    	resultMap.put("result", "SUCCESS");
    	    	resultMap.put("userInfo", userInfo);
    	    	Map<String, Object> dataLog = new HashMap<String, Object>();
    	    	dataLog.put("usrseq", userInfo.get("usrseq"));
    	    	dataLog.put("reqnam", "login");
    	    	logService.insetLog(dataLog);
    		}
    	}

        return resultMap;
    }


	/**
	 * @MethodName		: getLogout
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: usrseq [사용자번호]
	 * @return			: resultMsg[처리결과메시지], result[성공/실패] 
	 * @Description		: 로그아웃	
	 * @throws DefaultException
	 */
    @PostMapping("/logout")
    public @ResponseBody Map<String, Object> getLogout(@RequestBody Map<String, Object> m) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	m.put("reqnam", "logout");
    	
    	int resultCnt = logService.insetLog(m);
    	if(resultCnt == 1) {
    		resultMap.put("resultMsg", "로그아웃 되었습니다.");
        	resultMap.put("result", "SUCCESS");
    	}else {
    		resultMap.put("resultMsg", "로그아웃 처리중 애러가 발생하였습니다.");
        	resultMap.put("result", "FAILURE");
    	}
    	
        return resultMap;
    }

	/**
	 * @MethodName		: getDuplicate
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: usreml [사용자Email], usrmobtel [사용자모바일전화번호]
	 * @return			: resultMsg[처리결과메시지], result[성공/실패] 
	 * @Description		: 중복체크	(이메일/전화번호)
	 * @throws DefaultException
	 */
    @PostMapping("/duplicate")
    public @ResponseBody Map<String, Object> getDuplicate(@RequestBody Map<String, Object> m) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();    	
    	
    	int resultCnt = userService.getDuplicate(m);
    	if(resultCnt == 1) {
    		if("".equals(m.get("usrmobtel").toString())) {
        		resultMap.put("resultMsg", "이미 같은 메일주소가 있습니다.");
    		}else {
        		resultMap.put("resultMsg", "이미 가입된 전화번호 입니다.");
    		}
        	resultMap.put("result", "FAILURE");
    	}else {
    		if("".equals(m.get("usrmobtel").toString())) {
        		resultMap.put("resultMsg", "사용하실 수 있는 메일주소입니다.");
    		}else {
        		resultMap.put("resultMsg", "사용하실 수 있는 전화번호입니다.");
    		}
        	resultMap.put("result", "SUCCESS");
    	}
    	
        return resultMap;
    }

	/**
	 * @MethodName		: getCertification
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: usrmobtel [사용자모바일전화번호]
	 * @return			: cernum [인증번호]
	 * @Description		: sms 발송 
	 * @throws DefaultException
	 */
    @PostMapping("/certification")
    public @ResponseBody Map<String, Object> getCertification(@RequestBody Map<String, Object> m) {
    	
    	/*
    	 * 인증번호 생성 
    	 * */
        Random random = new Random(System.currentTimeMillis());
        
        int range = (int)Math.pow(10,6);
        int trim = (int)Math.pow(10, 6-1);
        int result = random.nextInt(range)+trim;
         
        if(result>range){
            result = result - trim;
        }
        String cernum = String.valueOf(result);
    	m.put("cernum", cernum);

    	loginService.deleteUsrCer(m);
    	
    	int resultCnt = loginService.insetUsrCer(m);
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();    	

        if (resultCnt == 1) {
          // 메시지 보내기 성공 및 전송결과 출력
	      resultMap.put("cernum", cernum);
	      resultMap.put("result", "SUCCESS");
        } else {
          // 메시지 보내기 실패
	      resultMap.put("cernum", "");
          resultMap.put("result", "FAILURE");
        }
        
    	/* sms 서버 준비중
        String api_key = "<너의키>";
        String api_secret = "<너의키>";
        Coolsms coolsms = new Coolsms(api_key, api_secret);

        HashMap<String, String> set = new HashMap<String, String>();
        set.put("to", "너의번호"); // 수신번호

        set.put("from", m.get("usrmobtel").toString()); // 발신번호
        set.put("text", cernum); // 문자내용
        set.put("type", "sms"); // 문자 타입
        
        JSONObject resultSms;
		try {
			resultSms = coolsms.sendPostRequest(api_secret, set);// 보내기&전송결과받기

	        if ((boolean)resultSms.get("status") == true) {
	          // 메시지 보내기 성공 및 전송결과 출력
			  resultMap.put("resultMsg", resultSms.get("result_message")); // 결과 메시지
		      resultMap.put("cernum", cernum);
		      resultMap.put("result", "SUCCESS");
	        } else {
	          // 메시지 보내기 실패
	    	  resultMap.put("resultMsg", resultSms.get("message"));
		      resultMap.put("cernum", "");
	          resultMap.put("result", "FAILURE");
	        }
		} catch (CoolsmsException e) {
	        // 메시지 보내기 실패
	    	resultMap.put("resultMsg", "문자 발송중 오류발생");
		    resultMap.put("cernum", "");
	        resultMap.put("result", "FAILURE");
	        
	  		return resultMap; 
		}
		*/
		return resultMap; 
    }    

	/**
	 * @MethodName		: getAuthentication
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: usrmobtel [사용자모바일전화번호], cernum [인증번호]
	 * @return			: resultMsg[처리결과메시지], result[성공/실패] 
	 * @Description		: 인증확인	
	 * @throws DefaultException
	 */
    @PostMapping("/authentication")
    public @ResponseBody Map<String, Object> getAuthentication(@RequestBody Map<String, Object> m) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();    	
    	
    	String cernum = loginService.getUsrCer(m);
    	if(cernum.equals(m.get("cernum").toString())) {
    		resultMap.put("resultMsg", "인증번호가 일치합니다.");
        	resultMap.put("result", "SUCCESS");
        	loginService.deleteUsrCer(m);
    	}else {
    		resultMap.put("resultMsg", "올바르지 않은 인증번호입니다.");
        	resultMap.put("result", "FAILURE");
    	}
    	
    	
        return resultMap;
    }   

	/**
	 * @MethodName		: getSignup
	 * @Date			: 2021. 7. 15. 
	 * @Author			: 김은상
	 * @param 			: usrmobtel [사용자모바일전화번호], cernum [인증번호]
	 * @return			: resultMsg[처리결과메시지], result[성공/실패] 
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @Description		: 회원가입	
	 * @throws DefaultException
	 */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @SuppressWarnings("unchecked")
	@PostMapping("/signup")
    public @ResponseBody Map<String, Object> getSignup(@RequestBody Map<String, Object> m) throws JsonMappingException, JsonProcessingException {

    	Map<String, Object> resultMap = new HashMap<String, Object>();  
    	Map<String, Object> userInfo = new HashMap<String, Object>();  	 
    	
    	// 구글의 json paser 라이브러리
    	Gson gson = new Gson();

    	// jsonPaserPser 클래스 객체를 만들고 해당 객체에 
    	JsonParser jparser = new JsonParser();

    	// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
    	JsonElement usrTrmchk = jparser.parseString(m.get("usrTrmchk").toString());

    	// JsonElement -> List<String>으로 파싱
    	List<Map<String, Object>> usrTrmchkInfo = gson.fromJson(usrTrmchk, (new TypeToken<List<Map<String, Object>>>() {  }).getType());
    	
    	/*
    	 * 사용자 등록
    	 * */
    	int resultCnt = userService.getInsertUser(m);
    	if(resultCnt == 1) {
    		userInfo = userService.getUserTel(m);
    		if(usrTrmchkInfo.size() > 0) {
    			Map<String, Object> data1 = new HashMap<String, Object>();  
    			data1.put("usrseq", userInfo.get("usrseq"));
    			
    			loginService.deleteUsrTrmchk(data1);
    			
    			for(Map<String, Object> data2 : usrTrmchkInfo) {
        			data2.put("usrseq", userInfo.get("usrseq"));
        			loginService.insetUsrTrmchk(data2);
        		}    			
    		}
    		
    		resultMap.put("resultMsg", "정상적으로 회원가입이 완료되었습니다.");
        	resultMap.put("userInfo", userInfo);
        	resultMap.put("result", "SUCCESS");
    	}else {
    		resultMap.put("resultMsg", "회원가입중 오류가 발생하였습니다.");
        	resultMap.put("result", "FAILURE");
    	}
    	
    	loginService.deleteUsrCer(m);
    	
        return resultMap;
    }

	/**
	 * @MethodName		: getForgotpwd
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: fcmtoken [FCM토큰], usrmobtel [사용자모바일전화번호]
	 * @return			: usrpwd [임시비밀번호]
	 * @Description		: sms 발송 
	 * @throws DefaultException
	 */
    @PostMapping("/forgotpwd")
    public @ResponseBody Map<String, Object> getForgotpwd(@RequestBody Map<String, Object> m) {
    	
    	/*
    	 * 인증번호 생성 
    	 * */
        Random random = new Random(System.currentTimeMillis());
        
        int range = (int)Math.pow(10,6);
        int trim = (int)Math.pow(10, 6-1);
        int result = random.nextInt(range)+trim;
         
        if(result>range){
            result = result - trim;
        }
        String usrpwd = String.valueOf(result);
    	m.put("usrpwd", usrpwd);
    	
    	int resultCnt = userService.getUpdateUserPwd(m);
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();    	

        if (resultCnt == 1) {
          // 메시지 보내기 성공 및 전송결과 출력
	      resultMap.put("temppwd", usrpwd);
	      resultMap.put("result", "SUCCESS");
        } else {
          // 메시지 보내기 실패
	      resultMap.put("temppwd", "");
          resultMap.put("result", "FAILURE");
        }
        
    	/* sms 서버 준비중
        String api_key = "<너의키>";
        String api_secret = "<너의키>";
        Coolsms coolsms = new Coolsms(api_key, api_secret);

        HashMap<String, String> set = new HashMap<String, String>();
        set.put("to", "너의번호"); // 수신번호

        set.put("from", m.get("usrmobtel").toString()); // 발신번호
        set.put("text", "임시비밀번호 " + usrpwd + " 입니다. 비밀번호 변경을 부탁드립니다."); // 문자내용
        set.put("type", "sms"); // 문자 타입
        
        JSONObject resultSms;
		try {
			resultSms = coolsms.sendPostRequest(api_secret, set);// 보내기&전송결과받기

	        if ((boolean)resultSms.get("status") == true) {
	          // 메시지 보내기 성공 및 전송결과 출력
			  resultMap.put("resultMsg", resultSms.get("result_message")); // 결과 메시지
		      resultMap.put("temppwd", usrpwd);
		      resultMap.put("result", "SUCCESS");
	        } else {
	          // 메시지 보내기 실패
	    	  resultMap.put("resultMsg", resultSms.get("message"));
		      resultMap.put("temppwd", "");
	          resultMap.put("result", "FAILURE");
	        }
		} catch (CoolsmsException e) {
	        // 메시지 보내기 실패
	    	resultMap.put("resultMsg", "문자 발송중 오류발생");
		    resultMap.put("temppwd", "");
	        resultMap.put("result", "FAILURE");
	        
	  		return resultMap; 
		}
		*/
		return resultMap; 
    }    

	/**
	 * @MethodName		: getUsrchange
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: usreml [사용자Email], usrmobtel [사용자모바일전화번호], usrnam [사용자명], usrpwd [사용자비밀번호],
	 * 					  tmppwdyn [임시비밀번호여부], fcmtoken [FCM토큰], xsta [상태], usrseq [사용자번호]
	 * @return			: result[SUCCESS/FAILURE], userInfo[사용자정보]
	 * @Description		: 사용자정보변경
	 * @throws DefaultException
	 */
    @PostMapping("/usrchange")
    public @ResponseBody Map<String, Object> getUsrchange(@RequestBody Map<String, Object> m) {
    	    	 
    	Map<String, Object> userInfo = new HashMap<String, Object>();  
    	
    	int resultCnt = userService.getUpdateUser(m);
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();    	

        if (resultCnt == 1) {
        	userInfo = userService.getUserTel(m);
        	resultMap.put("result", "SUCCESS");
        	resultMap.put("userInfo", userInfo);
        } else {
        	resultMap.put("result", "FAILURE");
        }
		return resultMap; 
    }    
}
