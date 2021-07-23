package com.autot.log.controller;

import com.autot.log.service.LogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LogController {
	
	// slf4j 로깅 파사드를 통해 logback 로깅 모듈을 지원
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
    private LogService logService;
    
    public LogController(LogService logService){
        this.logService = logService;
    }

    @PostMapping("/logs")
    public @ResponseBody List<Map<String, Object>> getLogs(@RequestBody Map<String, Object> m) {
        return logService.getLogs(m);
    }

    @PostMapping("/insetlog")
    public @ResponseBody Map<String, Object> getInsetUser(@RequestBody Map<String, Object> m) {
    	/*
    	 * 사용자 등록
    	 * */
    	int resultCnt = logService.insetLog(m);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	resultMap.put("result", resultCnt);
    	
        return resultMap;
    }
}
