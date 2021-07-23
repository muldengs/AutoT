package com.autot.common.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class PushMessage {

	/**
	 * send push message
	 * 
	 * 
	 */
	
	
	private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
	private static final String[] SCOPES = { MESSAGING_SCOPE };  
	//private static final String path = "C:/workspace2/AutoT/src/main/resources/push-test-f254f-firebase-adminsdk-g5wjx-1a08b1ab97.json";           //sdk key 
	//private static final String path = "D:/autoTDev/autoT/src/main/resources/push-test-f254f-firebase-adminsdk-g5wjx-1a08b1ab97.json";           //sdk key 
	private static final String path = "/home/hucore/AutoT/workspace/autot/src/main/resources/push-test-f254f-firebase-adminsdk-g5wjx-1a08b1ab97.json";           //sdk key 
	
	
	 
		
	 /**
	   * Retrieve a valid access token that can be use to authorize requests to the FCM REST
	   * API.
	   *
	   * @return Access token.
	   * @throws IOException
	   */
	private static String getAccessToken() throws IOException {
		  	
		  GoogleCredential googleCredential = GoogleCredential
	        .fromStream(new FileInputStream(path))
	        .createScoped(Arrays.asList(SCOPES));
		  	googleCredential.refreshToken();
		  	return googleCredential.getAccessToken();
	}
	
	public static void sendPush(Map<String, Object> m) throws Exception {
		
		 try {    
			 	
	            HttpHeaders headers = new HttpHeaders();
	            headers.add("content-type" , "application/json; UTF-8");
	            headers.add("Authorization", "Bearer " + getAccessToken());
	            
	            JSONObject notification = new JSONObject();
	            notification.put("body", "TEST");
	            notification.put("title", "TEST");
	            
	            JSONObject message = new JSONObject();
	            message.put("token", "fa_qIyte8d4:APA91bHOGnZulT059PyK3z_sb1dIkDXTiZUIuRksmS7TdK6XgXAS5kopeGIwUfyhad3X3iXMNknCUOZaF6_mgoj1ohG10CanRyJ_EW1d3xN2E-1DPiLdbMK4pdOgdhB1ztZClqB-25rC");
	            message.put("notification", notification);
	            
	            JSONObject jsonParams = new JSONObject();
	            jsonParams.put("message", message);
	            
	            HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonParams, headers);
	            RestTemplate rt = new RestTemplate();            
	            
	            ResponseEntity<String> res = rt.exchange("https://fcm.googleapis.com/v1/projects/push-test-f254f/messages:send"
	                    , HttpMethod.POST
	                    , httpEntity
	                    , String.class);
	        
				
				  if (res.getStatusCode() != HttpStatus.OK) { 
					  System.out.println("FCM-Exception");
					  System.out.println(res.getStatusCode().toString());
					  System.out.println(res.getHeaders().toString()); 
					  System.out.println(res.getBody().toString());
				  
				  } else { 
					  System.out.println(res.getStatusCode().toString());
					  System.out.println(res.getHeaders().toString());
					  System.out.println(res.getBody().toLowerCase());
				  
				  }
				 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	public static Map<String, Object> sendFcmPush(Map<String, Object> m) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type" , "application/json; UTF-8");
        headers.add("Authorization", "Bearer " + getAccessToken());
        
        JSONObject notification = new JSONObject();
        notification.put("body", m.get("body").toString());
        notification.put("title", m.get("title").toString());
        
        JSONObject message = new JSONObject();
        message.put("token", m.get("token").toString());
        message.put("notification", notification);
        
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("message", message);
        
        HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonParams, headers);
        RestTemplate rt = new RestTemplate();    
        
		 try {    
			 	        
	            
	            ResponseEntity<String> res = rt.exchange("https://fcm.googleapis.com/v1/projects/push-test-f254f/messages:send"
	                    , HttpMethod.POST
	                    , httpEntity
	                    , String.class);
	        
				
				  if (res.getStatusCode() != HttpStatus.OK) { 
					  resultMap.put("result", "FAILURE");
				  
				  } else { 
					  resultMap.put("result", "SUCCESS");
				  
				  }
				 
	        } catch (Exception e) {
				  resultMap.put("result", "FAILURE");
	        }
	   return resultMap;
	}
	public static Map<String, Object> sendFcmDataPush(Map<String, Object> m) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type" , "application/json; UTF-8");
        headers.add("Authorization", "Bearer " + getAccessToken());
                
        JSONObject message = new JSONObject();
        message.put("token", m.get("token").toString());
        message.put("data", m.get("data"));
        
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("message", message);
        
        HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonParams, headers);
        RestTemplate rt = new RestTemplate();    
        
		 try {    
			 	        
	            
	            ResponseEntity<String> res = rt.exchange("https://fcm.googleapis.com/v1/projects/push-test-f254f/messages:send"
	                    , HttpMethod.POST
	                    , httpEntity
	                    , String.class);
	        
				
				  if (res.getStatusCode() != HttpStatus.OK) { 
					  resultMap.put("result", "FAILURE");
				  
				  } else { 
					  resultMap.put("result", "SUCCESS");
				  
				  }
				 
	        } catch (Exception e) {
				  resultMap.put("result", "FAILURE");
	        }
	   return resultMap;
	}
}
