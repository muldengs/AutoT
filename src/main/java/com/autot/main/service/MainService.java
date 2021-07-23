package com.autot.main.service;

import org.springframework.stereotype.Service;

import com.autot.main.mapper.MainMapper;

import java.util.Map;
import java.util.List;

@Service
public class MainService {
    private MainMapper mainMapper;

    public MainService(MainMapper mainMapper){
        this.mainMapper = mainMapper;
    }

    public List<Map<String, Object>> getCal(Map<String, Object> m) {
        return mainMapper.selectCal(m);
    }
    
    public Map<String, Object> getCalDetail(Map<String, Object> m) {
        return mainMapper.selectCalDetail(m);
    }
    
    public Map<String, Object> getAsnSeoul(Map<String, Object> m) {
        return mainMapper.selectAsnSeoul(m);
    }
    
    public Map<String, Object> getAsnSeoulNon(Map<String, Object> m) {
        return mainMapper.selectAsnSeoulNon(m);
    }
    
    public int updateCalCancel(Map<String, Object> m) {
        return mainMapper.updateCalCancel(m);
    }
    
    public int updateCarinfCalCancel(Map<String, Object> m) {
        return mainMapper.updateCarinfCalCancel(m);
    }

}