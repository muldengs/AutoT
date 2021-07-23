package com.autot.qnabod.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.autot.qnabod.mapper.QnaBodMapper;

@Service
public class QnaBodService {

	private QnaBodMapper qnaBodMapper;
	
	public QnaBodService(QnaBodMapper qnaBodMapper){
        this.qnaBodMapper = qnaBodMapper;
    }
	
	
    public List<Map<String, Object>> getQnaBodList(Map<String, Object> m) {
        return qnaBodMapper.selectQnaBod(m);
    }
    
    public int getInsertQna(Map<String, Object> m) {
        return qnaBodMapper.insertQnaBod(m);
    }
}
