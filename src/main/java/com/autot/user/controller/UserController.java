package com.autot.user.controller;

import com.autot.user.service.UserService;

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
public class UserController {
	
	// slf4j 로깅 파사드를 통해 logback 로깅 모듈을 지원
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
    private UserService userService;
    
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    public @ResponseBody List<Map<String, Object>> getUsers(@RequestBody Map<String, Object> m) {
        return userService.getUsers(m);
    }

    @PostMapping("/userTel")
    public @ResponseBody Map<String, Object> getUserTel(@RequestBody Map<String, Object> m) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	resultMap.put("userInfo", userService.getUserTel(m));
        return resultMap;
    }

    @PostMapping("/userEml")
    public @ResponseBody Map<String, Object> getUserEml(@RequestBody Map<String, Object> m) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	resultMap.put("userInfo", userService.getUserEml(m));
        return resultMap;
    }

    @PostMapping("/userInfo")
    public @ResponseBody Map<String, Object> getUserInfo(@RequestBody Map<String, Object> m) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	resultMap.put("userInfo", userService.getUserInfo(m));
        return resultMap;
    }

    @PostMapping("/insertUser")
    public @ResponseBody Map<String, Object> getInsetUser(@RequestBody Map<String, Object> m) {
    	/*
    	logger.info(m.get("usreml").toString());
    	logger.info(m.get("usrmobtel").toString());
    	logger.info(m.get("usrnam").toString());
    	logger.info(m.get("usrpwd").toString());
    	logger.info(m.get("tmppwdyn").toString());
    	*/
    	/*
    	 * 사용자 등록
    	 * */
    	int resultCnt = userService.getInsertUser(m);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	if(resultCnt == 1) {
  	      	resultMap.put("result", "SUCCESS");
        	resultMap.put("userInfo", userService.getUserTel(m));
    	}else {
            resultMap.put("result", "FAILURE");
    	}
    	
        return resultMap;
    }
    
    @PostMapping("/updateUser")
    public @ResponseBody Map<String, Object> getUpdateUser(@RequestBody Map<String, Object> m) {
    	
    	int resultCnt = userService.getUpdateUser(m);
    	
    	resultCnt = userService.getInsetUserHis(m);
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>(); 
    	if(resultCnt == 1) {
  	      	resultMap.put("result", "SUCCESS");
        	resultMap.put("userInfo", userService.getUserTel(m));
    	}else {
            resultMap.put("result", "FAILURE");
    	}
    	
        return resultMap;
    }
    
    @PostMapping("/deleteUser")
    public @ResponseBody int getDeleteUser(@RequestBody Map<String, Object> m) {
        return userService.getDeleteUser(m);
    }
}
