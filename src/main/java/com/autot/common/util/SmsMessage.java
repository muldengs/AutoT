package com.autot.common.util;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;


public class SmsMessage {

	public static Map<String, Object> sendSms(Map<String, Object> m) throws Exception {
	    
		final Logger logger = LoggerFactory.getLogger(SmsMessage.class);
		
		String api_key = "API_KEY";  //api키
        String api_secret = "Secret_api_key"; // 비밀키
        Message coolsms = new Message(api_key, api_secret);
        logger.debug("로스 테스트");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", m.get("to").toString()); //수신번호
        params.put("from", m.get("from").toString()); //발신번호(coolsms에서 발신번호인증 필요)
        params.put("type", m.get("type").toString()); // 유형 SMS , LMS
        params.put("text", m.get("text").toString()); // Text 입력
        //params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            
            if ((boolean)obj.get("status") == true) {
            	 resultMap.put("result", "SUCCESS");
            }else {
            	 resultMap.put("result", "FAILURE");
            }
            
            logger.debug("SUCCESS :" , obj.toString());
        } catch (CoolsmsException e) {
        	logger.debug("Message : " ,e.getMessage());
        	logger.debug("CODE : ", e.getCode());
            resultMap.put("result", "FAILURE");
        }
        return resultMap;
    }
}
