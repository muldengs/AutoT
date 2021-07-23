package com.autot.call.controller;

import com.autot.call.service.CallService;
import com.autot.common.util.PushMessage;
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
 * @Class			: CallController
 * @since			: 2021. 7. 14.
 * @author			: 김은상
 * @Description		: 즉시호출, 예약호출  관리 프로그램을 관리하는 Controller
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
public class CallController {
	
	// slf4j 로깅 파사드를 통해 logback 로깅 모듈을 지원
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
    private CallService callService;
    
    public CallController(CallService callService){
        this.callService = callService;
    }

	/**
	 * @MethodName		: getCalstep1
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: rsvyn [예약구분], wchyn [휠체어여부], regseq [지역코드]
	 * @return			: rsvdtInfo[예약가능일자], stnInfo[정류장], stnpolInfo[정류장요금], stacnt[탑승인원]
	 * @Description		: 메인패이지	
	 * @throws DefaultException
	 */
    @PostMapping("/callstep1")
    public @ResponseBody Map<String, Object> getCalstep1(@RequestBody Map<String, Object> m) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	if("Y".equals(m.get("rsvyn").toString())) {    	
	    	if("100".equals(m.get("regseq").toString())) {
	        	m.put("cod", "10000401");
	    	}else if("200".equals(m.get("regseq").toString())) {
	        	m.put("cod", "10000403");
	    	}else if("300".equals(m.get("regseq").toString())) {
	        	m.put("cod", "10000404");
	    	}else{
	        	m.put("cod", "10000402");
	    	}
	       	resultMap.put("rsvdtInfo", callService.selectRsvdt(m));
    	}
    	
	    resultMap.put("stnInfo", callService.selectStnInfo(m));
	   	resultMap.put("stnpolInfo", callService.selectStnPol(m));
	   	resultMap.put("satcnt", callService.selectSatCnt(m));
    	resultMap.put("result", "SUCCESS");
    	
        return resultMap;
    }
    
	/**
	 * @MethodName		: getCalstep2
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: rsvdt [예약일자], wchyn [휠체어여부], regseq [지역코드]
	 * @return			: rsvdttmInfo[예약가능일자스케즐]
	 * @Description		: 예약가능일자스케즐 조회	
	 * @throws DefaultException
	 */
    @PostMapping("/callstep2")
    public @ResponseBody Map<String, Object> getCalstep2(@RequestBody Map<String, Object> m) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();
      	
    	if("100".equals(m.get("regseq").toString())) {
           	resultMap.put("rsvdttmInfo", callService.selectRsvtmcarSeoul(m));
    	}else if("400".equals(m.get("regseq").toString())) {
           	resultMap.put("rsvdttmInfo", callService.selectRsvtmcarPangyo(m));
    	}else{
           	resultMap.put("rsvdttmInfo", callService.selectRsvtmcarDs(m));
    	}
       	
    	resultMap.put("result", "SUCCESS");
    	
        return resultMap;
    }
    
	/**
	 * @MethodName		: getCalstep3
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: regseq [지역코드], adrnm [주소] 
	 * @return			: resultMsg[처리결과메시지], result[SUCCESS/FAILURE]
	 * @Description		: 서울지역 서비스지역 유무체크
	 * @throws DefaultException
	 */
    @PostMapping("/callstep3")
    public @ResponseBody Map<String, Object> getCalstep3(@RequestBody Map<String, Object> m) {

    	Map<String, Object> resultMap = new HashMap<String, Object>();
      	int resultCnt = callService.selectServiceArea(m);
    	if(resultCnt > 0) {   		
    		resultMap.put("resultMsg", "서비스 지역입니다.");
        	resultMap.put("result", "SUCCESS");
    	}else {
    		resultMap.put("resultMsg", "서비스 불가 지역입니다.");
        	resultMap.put("result", "FAILURE");
    	}
    	
        return resultMap;
    }
    
	/**
	 * @MethodName		: getCal
	 * @Date			: 2021. 7. 14. 
	 * @Author			: 김은상
	 * @param 			: rsvyn [예약구분], regseq [지역코드], usrseq [사용자번호], deplon [출발지경도], deplat [출발지위도],
	 * 					  depadr [출발지주소], depstnseq [출발지정류장순번], dstlon [목적지경도], dstlat [목적지위도], dstadr [목적지주소],
	 * 					  dststnseq [목적지정류장순번], wchyn [휠체어여부], satcnt [탑승인원], calydt[호출일시]
	 * @return			: calInfo[호출정보], fcmInfo[FCM정보]
	 * @throws Exception 
	 * @Description		: 메인패이지	
	 * @throws DefaultException
	 */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @PostMapping("/call")
    public @ResponseBody Map<String, Object> getCal(@RequestBody Map<String, Object> m) throws Exception {

    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	Map<String, Object> calInfo = new HashMap<String, Object>();  
    	Map<String, Object> carInfo = new HashMap<String, Object>();  
    	Map<String, Object> fcmInfo = new HashMap<String, Object>();  	
      	
    	int resultCnt = 0;
    	if("Y".equals(m.get("rsvyn").toString())) {
    		resultCnt = callService.insertCalRsvY(m); 	

        	if(resultCnt == 1) {
            	int resultACnt = 0;
        		calInfo = callService.selectCalInfo(m);
    			Map<String, Object> data1 = new HashMap<String, Object>();  
    			data1.put("calseq", calInfo.get("calseq"));  
    			data1.put("regseq", calInfo.get("regseq"));
    			data1.put("wchyn", calInfo.get("wchyn"));
    			data1.put("calydt", calInfo.get("calydt"));
    			resultACnt = callService.insertAsn(data1);
		    	resultMap.put("calInfo", calInfo);
    			if(resultACnt == 1) {
        			Map<String, Object> data2 = new HashMap<String, Object>();   
        			data2.put("calseq", calInfo.get("calseq"));   
        			data2.put("regseq", calInfo.get("regseq"));
        			carInfo = callService.selectCarInfo(data2);
        			if(carInfo.size() > 0 || carInfo != null) {
            			Map<String, Object> data3 = new HashMap<String, Object>();
            			data3.put("data", carInfo);   
            			data3.put("token", carInfo.get("fcmtoken"));   
            			fcmInfo = PushMessage.sendFcmDataPush(data3);
        		    	resultMap.put("fcmInfo", fcmInfo);
        			}
    			}
        	}
    	}else {
    		resultCnt = callService.insertCalRsvN(m);

        	if(resultCnt == 1) {
        		calInfo = callService.selectCalInfo(m);
		    	resultMap.put("calInfo", calInfo);
		    	List<Map<String, Object>> asnCarInfo = callService.selectAsnCarInfo(m);
		    	for(Map<String, Object> data3 : asnCarInfo ) {
        			Map<String, Object> data4 = new HashMap<String, Object>();
        			data4.put("data", data3);   
        			data4.put("token", data3.get("fcmtoken"));   
        			fcmInfo = PushMessage.sendFcmDataPush(data4);	
    		    	resultMap.put("fcmInfo", fcmInfo);	    		
		    	}
        		
        	}
    	}
       	
    	resultMap.put("result", "SUCCESS");
    	
        return resultMap;
    }
}
