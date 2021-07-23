package com.autot.main.controller;

import com.autot.main.service.MainService;
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
 * @Class			: MainController
 * @since			: 2021. 7. 14.
 * @author			: 김은상
 * @Description		: 메인, 이용내역  관리 프로그램을 관리하는 Controller
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
public class MainController {
	
	// slf4j 로깅 파사드를 통해 logback 로깅 모듈을 지원
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
    private MainService mainService;
    
    public MainController(MainService mainService){
        this.mainService = mainService;
    }

	/**
	 * @MethodName		: getMain
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: regseq [지역코드], usrseq [사용자번호]
	 * @return			: asnfInfo[배차가능여부정보], usrTrmchkInfo[사용자약관정보], result[SUCCESS]
	 * @Description		: 메인패이지	
	 * @throws DefaultException
	 */
    @PostMapping("/main")
    public @ResponseBody Map<String, Object> getMain(@RequestBody Map<String, Object> m) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	if("100".equals(m.get("regseq").toString())) {
        	resultMap.put("asnfInfo", mainService.getAsnSeoul(m));
    	}else {
        	resultMap.put("asnfInfo", mainService.getAsnSeoulNon(m));
    	}
    	resultMap.put("result", "SUCCESS");
    	
        return resultMap;
    }

	/**
	 * @MethodName		: getUsehis
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: usrseq [사용자번호]
	 * @return			: usehisrsvyInfo[예약내역],usehisrsvnInfo[즉시내역], result[SUCCESS]
	 * @Description		: 사용자이용내역	
	 * @throws DefaultException
	 */
    @PostMapping("/usehis")
    public @ResponseBody Map<String, Object> getUsehis(@RequestBody Map<String, Object> m) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();

		m.put("rsvyn", "Y");
    	resultMap.put("usehisrsvyInfo", mainService.getCal(m));
		m.put("rsvyn", "N");
    	resultMap.put("usehisrsvnInfo", mainService.getCal(m));
    	resultMap.put("result", "SUCCESS");

        return resultMap;
    }

	/**
	 * @MethodName		: getUsehisDetail
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: calseq [호출순번]
	 * @return			: usehisrInfo[이용내역상세], result[SUCCESS]
	 * @Description		: 사용자이용내역상세	
	 * @throws DefaultException
	 */
    @PostMapping("/usehisdetail")
    public @ResponseBody Map<String, Object> getUsehisDetail(@RequestBody Map<String, Object> m) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();

    	resultMap.put("usehisrInfo", mainService.getCalDetail(m));
    	resultMap.put("result", "SUCCESS");

        return resultMap;
    }

	/**
	 * @MethodName		: getCalCancel
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: calseq [호출순번], canrsn [취소사유], canrsndes [취소사유상세]
	 * @return			: usehisrInfo[이용내역상세], result[SUCCESS/FAILURE], resultMsg[결과메시지]
	 * @Description		: 호출취소	
	 * @throws DefaultException
	 */
    @PostMapping("/calcancel")
    public @ResponseBody Map<String, Object> getCalCancel(@RequestBody Map<String, Object> m) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();

    	int resultCnt = mainService.updateCalCancel(m);

    	if(resultCnt == 1) {	    		
        	mainService.updateCarinfCalCancel(m);
    		resultMap.put("resultMsg", "정상적으로 호출취소가 완료되었습니다.");
        	resultMap.put("result", "SUCCESS");
    	}else {
    		resultMap.put("resultMsg", "호출취소중 오류가 발생하였습니다.");
        	resultMap.put("result", "FAILURE");
    	}

        return resultMap;
    }
}
