package com.autot.qnabod.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.autot.qnabod.service.QnaBodService;

/**
 * @prjectName		: SWM 21 자율주행 서비스 플랫폼 autot   
 * @Class			: QnaBodController
 * @since			: 2021. 7. 14.
 * @author			: 이원재
 * @Description		: 1:1문의 리스트, 등록하기, 상세보기 프로그램을 관리하는 Controller
 * @Modification Information
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     	 수정자           수정내용
 *  -------    		--------    ---------------------------
 *  2021. 7. 15.	이원재       신규개발
 * 
 * @Copyright (C) 휴코어(주) All right reserved.
 * <pre>
 */

@RestController
public class QnaBodController {
	// slf4j 로깅 파사드를 통해 logback 로깅 모듈을 지원
			private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
			
			private QnaBodService qnaBodService;
		    
		    public QnaBodController(QnaBodService qnaBodService){
		        this.qnaBodService = qnaBodService;
		    }

		    
			/**
			 * @MethodName		: getQnaBodList
			 * @Date			: 2021. 7. 15. 
			 * @Author			: 이원재
			 * @param 			: usrseq [사용자번호]
			 * @return			: 1:1 문의 리스트
			 * @Description		: 1:1 문의 리스트 출력	
			 * @throws DefaultException
			 */
		    @PostMapping("/qnaBodList")
		    public @ResponseBody Map<String, Object> getQnaBodList(@RequestBody Map<String, Object> m) {
		    	
		    	Map<String, Object> resultMap = new HashMap<String, Object>();
		    	
	    		resultMap.put("qnaBodList", qnaBodService.getQnaBodList(m));
	    		
		        return resultMap;
		    }
		    
		    
			/**
			 * @MethodName		: getInsertQna
			 * @Date			: 2021. 7. 15. 
			 * @Author			: 이원재
			 * @param 			: usrseq [사용자번호], qstcon[문의내용]
			 * @return			: 1:1 문의 리스트
			 * @Description		: 1:1 문의 리스트 출력	
			 * @throws DefaultException
			 */
		    @PostMapping("/insertQnaBod")
		    public @ResponseBody Map<String, Object> getInsertQna(@RequestBody Map<String, Object> m) {
		    	
		    	int resultCnt = qnaBodService.getInsertQna(m);
		    	
		    	Map<String, Object> resultMap = new HashMap<String, Object>();
		    	if(resultCnt == 1) {
		    		resultMap.put("resultMsg", "1:1 문의가 등록되었습니다.");
		        	resultMap.put("result", "SUCCESS");
		    	}else {
		    		resultMap.put("resultMsg", "1:1 문의 등록에 실패하였습니다.");
		        	resultMap.put("result", "FAILURE");
		    	}
		    	
		        return resultMap;
	    }
		    
}
