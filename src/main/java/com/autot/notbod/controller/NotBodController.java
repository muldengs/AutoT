package com.autot.notbod.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.autot.notbod.service.NotBodService;

/**
 * @prjectName		: SWM 21 자율주행 서비스 플랫폼 autot   
 * @Class			: NotBodController
 * @since			: 2021. 7. 14.
 * @author			: 이원재
 * @Description		: 공지사항 리스트, 상세보기 프로그램을 관리하는 Controller
 * @Modification Information
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     	 수정자           수정내용
 *  -------    		--------    ---------------------------
 *  2021. 7. 14.	이원재       신규개발
 * 
 * @Copyright (C) 휴코어(주) All right reserved.
 * <pre>
 */

@RestController
public class NotBodController {
		// slf4j 로깅 파사드를 통해 logback 로깅 모듈을 지원
		private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
		
	    private NotBodService notBodService;
	    
	    public NotBodController(NotBodService notBodService){
	        this.notBodService = notBodService;
	    }

		/**
		 * @MethodName		: getNotBodList
		 * @Date			: 2021. 7. 14. 
		 * @Author			: 이원재
		 * @param 			: 
		 * @return			: 공지사항 리스트
		 * @Description		: 공지사항 리스트 출력	
		 * @throws DefaultException
		 */
	    @PostMapping("/notBodList")
	    public @ResponseBody Map<String, Object> getNotBodList(@RequestBody Map<String, Object> m) {
	    	Map<String, Object> resultMap = new HashMap<String, Object>();
	    	
	    	resultMap.put("notBodList", notBodService.getNotBodList(m));
	    	
	        return resultMap;
	    }
	    
		
	    /**
		 * @MethodName		: getNotBodDetail
		 * @Date			: 2021. 7. 14. 
		 * @Author			: 이원재
		 * @param 			: notseq [공지사항번호]
		 * @return			: notBodDetail[공지사항상세] 
		 * @Description		: 공지사항 상세정보	
		 * @throws DefaultException
		 */	   
	    @PostMapping("/notBodDetail")
	    public @ResponseBody Map<String, Object> getNotBodDetail(@RequestBody Map<String, Object> m) {
	    	
	    	Map<String, Object> resultMap = new HashMap<String, Object>();
	    	
	    	resultMap.put("notBodDetail", notBodService.getNotBodDetail(m));
	        return resultMap;
	    }
	    
	    
	    /*
    	 * 공지사항 등록
    	 * */
	    @PostMapping("/insetNotBod")
	    public @ResponseBody Map<String, Object> getInsetUser(@RequestBody Map<String, Object> m) {
	    	
	    	int resultCnt = notBodService.getInsertNotBod(m);
	    	Map<String, Object> resultMap = new HashMap<String, Object>();
	    	
	    	resultMap.put("result", resultCnt);
	    	
	        return resultMap;
	    }
}
