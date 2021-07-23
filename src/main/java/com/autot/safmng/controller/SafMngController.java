package com.autot.safmng.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.autot.log.service.LogService;
import com.autot.safmng.service.SafMngService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @prjectName		: SWM 21 자율주행 서비스 플랫폼 autot   
 * @Class			: SafMngController
 * @since			: 2021. 7. 16.
 * @author			: 이원재
 * @Description		: 어풀 초기 접근, 로그인, 로그아웃, 자동로그인  관리 프로그램을 관리하는 Controller
 * @Modification Information
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2021. 7. 16.		이원재       신규개발
 * 
 * @Copyright (C) 휴코어(주) All right reserved.
 * <pre>
 */
@RestController
public class SafMngController {
	
	// slf4j 로깅 파사드를 통해 logback 로깅 모듈을 지원
		private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
		
	    private LogService logService;
	    
	    private SafMngService safMngService;	
	    
	    public SafMngController(LogService logService, SafMngService safMngService){
	        this.logService = logService;
	        this.safMngService = safMngService;
	    }

		/**
		 * @MethodName		: getSafMngInit
		 * @Date			: 2021. 7. 16. 
		 * @Author			: 이원재
		 * @param 			: 
		 * @return			: regInfo[지역정보]
		 * @Description		: 어플 시작시 지역정보 가져오기	
		 * @throws DefaultException
		 */
	    
	    @PostMapping("/safMnginit")
	    public @ResponseBody Map<String, Object> getSafMngInit(@RequestBody Map<String, Object> m) {

	    	Map<String, Object> resultMap = new HashMap<String, Object>();
	    	resultMap.put("regInfo", safMngService.getSafMngReg());
	    	
	        return resultMap;
	    }
	    
	    /**
		 * @MethodName		: getSafMngLogin
		 * @Date			: 2021. 7. 16. 
		 * @Author			: 이원재
		 * @param 			: usrid [safmngmobtel(사용자모바일전화번호)]
		 * 					  pwd [safmngpwd(비밀번호)]
		 * @return			: safMngInfo[안전관리자정보]
		 * @Description		: 로그인	
		 * @throws DefaultException
		 */
	    @PostMapping("/safMngLogin")
	    public @ResponseBody Map<String, Object> getSafMngLogin(@RequestBody Map<String, Object> m) {
	    	
	    	Map<String, Object> resultMap = new HashMap<String, Object>();
	    	Map<String, Object> safMngInfo = new HashMap<String, Object>();
	    	safMngInfo = safMngService.getSafMngLogin(m);
	    	
	    	if(safMngInfo.size() == 0 || safMngInfo == null ) {
	    		resultMap.put("resultMsg", "안전관리자정보가 없습니다.");
	        	resultMap.put("result", "FAILURE");
	    	}else {
	    		if(!m.get("pwd").toString().equals(safMngInfo.get("safmngpwd"))){
	        		resultMap.put("resultMsg", "비밀번호가 일치하지 않습니다.");
	            	resultMap.put("result", "FAILURE");    			
	    		}else {
	        		resultMap.put("resultMsg", "정상처리되었습니다.");
	    	    	resultMap.put("result", "SUCCESS");
	    	    	resultMap.put("safMngInfo", safMngInfo);
	    	    	Map<String, Object> dataLog = new HashMap<String, Object>();
	    	    	dataLog.put("usrseq", safMngInfo.get("safmngseq"));
	    	    	dataLog.put("reqnam", "safMngLogin");
	    	    	logService.insetLog(dataLog);
	    		}
	    	}

	        return resultMap;
	    }
	    
	    /**
		 * @MethodName		: getSafMngLogout
		 * @Date			: 2021. 7. 16. 
		 * @Author			: 이원재
		 * @param 			: safmngseq [안전관리자순번]
		 * @return			: resultMsg[처리결과메시지], result[SUCCESS/FAILURE]
		 * @Description		: 로그아웃	
		 * @throws DefaultException
		 */
	    @PostMapping("/safMngLogout")
	    public @ResponseBody Map<String, Object> getSafMngLogout(@RequestBody Map<String, Object> m) {

	    	Map<String, Object> resultMap = new HashMap<String, Object>();

	    	m.put("usrseq", m.get("safmngseq").toString());
	    	m.put("reqnam", "safMngLogout");
	    	
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
		 * @MethodName		: getSafMngForgotpwd
		 * @Date			: 2021. 7. 14. 
		 * @Author			: 김은상
		 * @param 			: fcmtoken [FCM토큰], safmngmobtel [안전관리자모바일전화번호]
		 * @return			: temppwd [임시비밀번호], result[SUCCESS/FAILURE]
		 * @Description		: sms 발송 
		 * @throws DefaultException
		 */
	    @PostMapping("/safmngForgotpwd")
	    public @ResponseBody Map<String, Object> getSafMngForgotpwd(@RequestBody Map<String, Object> m) {
	    	
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
	        String safmngpwd = String.valueOf(result);
	    	m.put("safmngpwd", safmngpwd);
	    	
	    	int resultCnt = safMngService.getUpdateSafMngPwd(m);
	    	
	    	Map<String, Object> resultMap = new HashMap<String, Object>();    	

	        if (resultCnt == 1) {
	          // 메시지 보내기 성공 및 전송결과 출력
		      resultMap.put("temppwd", safmngpwd);
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
		 * @MethodName		: getsafMngSignup
		 * @Date			: 2021. 7. 15. 
		 * @Author			: 김은상
		 * @param 			: regseq [지역순번], carseq [차순번], safmngmobtel [안전관리자모바일전화번호],
		 * 					  safmngnam [안전관리자명], safmngpwd [안전관리자비밀번호], fcmtoken [FCM토큰]
		 * @return			: resultMsg[처리결과메시지], result[SUCCESS/FAILURE], safMngInfo [안전관리자정보] 
		 * @throws JsonProcessingException 
		 * @throws JsonMappingException 
		 * @Description		: 회원가입	
		 * @throws DefaultException
		 */
	    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
	    @SuppressWarnings("unchecked")
		@PostMapping("/safMngSignup")
	    public @ResponseBody Map<String, Object> getsafMngSignup(@RequestBody Map<String, Object> m) throws JsonMappingException, JsonProcessingException {

	    	Map<String, Object> resultMap = new HashMap<String, Object>();  
	    	Map<String, Object> safMngInfo = new HashMap<String, Object>();  
	    	
	    	/*
	    	 * 사용자 등록
	    	 * */
	    	int resultCnt = safMngService.insertSafMngInf(m);
	    	if(resultCnt == 1) {
	    		safMngInfo = safMngService.getSafMngtel(m);	    		
	    		resultMap.put("resultMsg", "정상적으로 회원가입이 완료되었습니다.");
	        	resultMap.put("safMngInfo", safMngInfo);
	        	resultMap.put("result", "SUCCESS");
	    	}else {
	    		resultMap.put("resultMsg", "회원가입중 오류가 발생하였습니다.");
	        	resultMap.put("result", "FAILURE");
	    	}
	    	
	        return resultMap;
	    }

		/**
		 * @MethodName		: getSafMngchange
		 * @Date			: 2021. 7. 14. 
		 * @Author			: 김은상
		 * @param 			: safmngseq [안전관리자순번], regseq [지역순번], carseq [차순번], safmngmobtel [안전관리자모바일전화번호],
		 * 					  safmngnam [안전관리자명], safmngpwd [안전관리자비밀번호], fcmtoken [FCM토큰], tmppwdyn [임시비밀번호여부]
		 * @return			: result[SUCCESS/FAILURE], safMngInfo [안전관리자정보]
		 * @Description		: 안전관리자정보변경
		 * @throws DefaultException
		 */
	    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
	    @PostMapping("/safMngchange")
	    public @ResponseBody Map<String, Object> getSafMngchange(@RequestBody Map<String, Object> m) {

	    	Map<String, Object> safMngInfo = new HashMap<String, Object>();  
	    	safMngService.insetSafMngInfHis(m);
	    	int resultCnt = safMngService.updateSafMngInf(m);
	    	
	    	Map<String, Object> resultMap = new HashMap<String, Object>();    	

	        if (resultCnt == 1) {
	        	safMngInfo = safMngService.getSafMngtel(m);	
	        	resultMap.put("safMngInfo", safMngInfo);
	        	resultMap.put("result", "SUCCESS");
	        } else {
	        	resultMap.put("result", "FAILURE");
	        }
			return resultMap; 
	    }    
	    
	    /**
		 * @MethodName		: getSafMngAsn
		 * @Date			: 2021. 7. 20. 
		 * @Author			: 김은상
		 * @param 			: safmngseq [안전관리자순번], regseq [지역순번]
		 * @return			: safMngAsnInfo[안전관리자 배차내역], result[SUCCESS/FAILURE]
		 * @Description		: 안전관리자 배차내역	
		 * @throws DefaultException
		 */
	    @PostMapping("/safMngAsn")
	    public @ResponseBody Map<String, Object> getSafMngAsn(@RequestBody Map<String, Object> m) {

	    	Map<String, Object> resultMap = new HashMap<String, Object>();


    		resultMap.put("safMngAsnInfo", safMngService.getSafMngAsn(m));
        	resultMap.put("result", "SUCCESS");
	    	
	        return resultMap;
	    }   
	    
	    /**
		 * @MethodName		: getSafMngAsnDetail
		 * @Date			: 2021. 7. 20. 
		 * @Author			: 김은상
		 * @param 			: safmngseq [안전관리자순번], calseq [호출번호]
		 * @return			: safMngAsn[안전관리자 배차내역 상세], result[SUCCESS/FAILURE]
		 * @Description		: 안전관리자 배차내역	 상세
		 * @throws DefaultException
		 */
	    @PostMapping("/safMngAsnDetail")
	    public @ResponseBody Map<String, Object> getSafMngAsnDetail(@RequestBody Map<String, Object> m) {

	    	Map<String, Object> resultMap = new HashMap<String, Object>();


    		resultMap.put("safMngAsn", safMngService.getSafMngAsnDetail(m));
        	resultMap.put("result", "SUCCESS");
	    	
	        return resultMap;
	    }
	    
	    /**
		 * @MethodName		: getSafMngCal
		 * @Date			: 2021. 7. 20. 
		 * @Author			: 김은상
		 * @param 			: safmngseq [안전관리자순번], regseq [지역순번]
		 * @return			: safMngCalInfo[안전관리자 Call 내역], result[SUCCESS/FAILURE]
		 * @Description		: 안전관리자 Call 내역	
		 * @throws DefaultException
		 */
	    @PostMapping("/safMngCal")
	    public @ResponseBody Map<String, Object> getSafMngCal(@RequestBody Map<String, Object> m) {

	    	Map<String, Object> resultMap = new HashMap<String, Object>();


    		resultMap.put("safMngCalInfo", safMngService.getSafMngCal(m));
        	resultMap.put("result", "SUCCESS");
	    	
	        return resultMap;
	    }   
	    
	    /**
		 * @MethodName		: getSafMngCalDetail
		 * @Date			: 2021. 7. 20. 
		 * @Author			: 김은상
		 * @param 			: safmngseq [안전관리자순번], calseq [호출번호]
		 * @return			: safMngCal[안전관리자 Call 내역 상세], result[SUCCESS/FAILURE]
		 * @Description		: 안전관리자 Call 내역 상세
		 * @throws DefaultException
		 */
	    @PostMapping("/safMngCalDetail")
	    public @ResponseBody Map<String, Object> getSafMngCalDetail(@RequestBody Map<String, Object> m) {

	    	Map<String, Object> resultMap = new HashMap<String, Object>();


    		resultMap.put("safMngCal", safMngService.getSafMngCalDetail(m));
        	resultMap.put("result", "SUCCESS");
	    	
	        return resultMap;
	    }

		/**
		 * @MethodName		: getDispatch
		 * @Date			: 2021. 7. 14. 
		 * @Author			: 김은상
		 * @param 			: calseq [호출번호], carseq [차순번]
		 * @return			: resultMsg[처리결과메시지], result[SUCCESS/FAILURE]
		 * @Description		: 배차확정
		 * @throws DefaultException
		 */
	    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
	    @PostMapping("/dispatch")
	    public @ResponseBody Map<String, Object> getDispatch(@RequestBody Map<String, Object> m) { 
	    	
	    	Map<String, Object> resultMap = new HashMap<String, Object>();  
	    	
	    	int resultAsnCnt = safMngService.selectSafMngAsnCnt(m);  	
	    		
	    	if(resultAsnCnt == 0) {
		    	int resultCnt = safMngService.insertSafMngAsn(m);  
		        if (resultCnt == 1) {
		        	resultMap.put("result", "SUCCESS");
		    		resultMap.put("resultMsg", "정상적으로 배차되었습니다.");
		        } else {
		        	resultMap.put("result", "FAILURE");
		    		resultMap.put("resultMsg", "배차중 오류가 발생되었습니다.");
		        }
	    		
	    	}else {
	        	resultMap.put("result", "FAILURE");	 
	    		resultMap.put("resultMsg", "이미 다른 차량에서 배차되었습니다.");   		
	    	}
			return resultMap; 
	    } 

		/**
		 * @MethodName		: getRide
		 * @Date			: 2021. 7. 14. 
		 * @Author			: 김은상
		 * @param 			: calseq [호출번호], carseq [차순번]
		 * @return			: result[SUCCESS/FAILURE]
		 * @Description		: 승차
		 * @throws DefaultException
		 */
	    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
	    @PostMapping("/ride")
	    public @ResponseBody Map<String, Object> getRide(@RequestBody Map<String, Object> m) { 
	    	
	    	Map<String, Object> resultMap = new HashMap<String, Object>();  
	    	
	    	int resultCnt = safMngService.updateRide(m);  	

	        if (resultCnt == 1) {
	        	resultMap.put("result", "SUCCESS");
	        	m.put("asnyn", "Y");
	        	safMngService.updateCarinf(m);
	        } else {
	        	resultMap.put("result", "FAILURE");
	        }
			return resultMap; 
	    } 

		/**
		 * @MethodName		: getQuit
		 * @Date			: 2021. 7. 14. 
		 * @Author			: 김은상
		 * @param 			: calseq [호출번호], carseq [차순번]
		 * @return			: result[SUCCESS/FAILURE]
		 * @Description		: 하차
		 * @throws DefaultException
		 */
	    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
	    @PostMapping("/quit")
	    public @ResponseBody Map<String, Object> getQuit(@RequestBody Map<String, Object> m) { 
	    	
	    	Map<String, Object> resultMap = new HashMap<String, Object>();  
	    	
	    	int resultCnt = safMngService.updateQuit(m);  	

	        if (resultCnt == 1) {
	        	resultMap.put("result", "SUCCESS");
	        	m.put("asnyn", "N");
	        	safMngService.updateCarinf(m);
	        } else {
	        	resultMap.put("result", "FAILURE");
	        }
			return resultMap; 
	    } 

		/**
		 * @MethodName		: getAsnCancel
		 * @Date			: 2021. 7. 14. 
		 * @Author			: 김은상
		 * @param 			: calseq [호출번호], carseq [차순번]
		 * @return			: result[SUCCESS/FAILURE]
		 * @Description		: 배차취소
		 * @throws DefaultException
		 */
	    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
	    @PostMapping("/asnCancel")
	    public @ResponseBody Map<String, Object> getAsnCancel(@RequestBody Map<String, Object> m) { 
	    	
	    	Map<String, Object> resultMap = new HashMap<String, Object>();  
	    	
	    	int resultCnt = safMngService.updateAsnCancel(m);  	

	        if (resultCnt == 1) {
	        	resultMap.put("result", "SUCCESS");
	        } else {
	        	resultMap.put("result", "FAILURE");
	        }
			return resultMap; 
	    } 
}
