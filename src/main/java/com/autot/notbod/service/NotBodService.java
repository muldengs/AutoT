package com.autot.notbod.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.autot.notbod.mapper.NotBodMapper;


@Service
public class NotBodService {
	private NotBodMapper notBodMapper;
	
	public NotBodService(NotBodMapper notBodMapper){
        this.notBodMapper = notBodMapper;
    }
	
	
    public List<Map<String, Object>> getNotBodList(Map<String, Object> m) {
        return notBodMapper.selectNotBod(m);
    }
 

    public Map<String, Object> getNotBodDetail(Map<String, Object> m) {
        return notBodMapper.selectNotBodDetail(m);
    }

    public int getInsertNotBod(Map<String, Object> m) {
        return notBodMapper.insertNotBod(m);
    }
}
