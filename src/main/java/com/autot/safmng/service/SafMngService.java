package com.autot.safmng.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.autot.safmng.mapper.SafMngMapper;

@Service
public class SafMngService {
	 
	private SafMngMapper safMngMapper;

    public SafMngService(SafMngMapper safMngMapper){
        this.safMngMapper = safMngMapper;
    }
    
    public List<Map<String, Object>> getSafMngReg() {
        return safMngMapper.selectSafMngReg();
    }
    
    public List<Map<String, Object>> getCar(Map<String, Object> m) {
        return safMngMapper.selectCar(m);
    }
    
    public Map<String, Object> getSafMngLogin(Map<String, Object> m) {
        return safMngMapper.selectSafMngLogin(m);
    }
    
    public Map<String, Object> getSafMngtel(Map<String, Object> m) {
        return safMngMapper.selectSafMngtel(m);
    }
    
    public int getUpdateSafMngPwd(Map<String, Object> m) {
        return safMngMapper.updateSafMngPwd(m);
    }
    
    public int insertSafMngInf(Map<String, Object> m) {
        return safMngMapper.insertSafMngInf(m);
    }
    
    public int updateSafMngInf(Map<String, Object> m) {
        return safMngMapper.updateSafMngInf(m);
    }
    
    public int deleteSafMngInf(Map<String, Object> m) {
        return safMngMapper.deleteSafMngInf(m);
    }
    
    public int insetSafMngInfHis(Map<String, Object> m) {
        return safMngMapper.insetSafMngInfHis(m);
    }
    
    public List<Map<String, Object>> getSafMngCal(Map<String, Object> m) {
        return safMngMapper.selectSafMngCal(m);
    }
    
    public Map<String, Object> getSafMngCalDetail(Map<String, Object> m) {
        return safMngMapper.selectSafMngCalDetail(m);
    }
    
    public List<Map<String, Object>> getSafMngAsn(Map<String, Object> m) {
        return safMngMapper.selectSafMngAsn(m);
    }
    
    public Map<String, Object> getSafMngAsnDetail(Map<String, Object> m) {
        return safMngMapper.selectSafMngAsnDetail(m);
    }

    public int selectSafMngAsnCnt(Map<String, Object> m) {
        return safMngMapper.selectSafMngAsnCnt(m);
    }
    
    public int insertSafMngAsn(Map<String, Object> m) {
        return safMngMapper.insertSafMngAsn(m);
    }
    
    public int updateRide(Map<String, Object> m) {
        return safMngMapper.updateRide(m);
    }
    
    public int updateCarinf(Map<String, Object> m) {
        return safMngMapper.updateCarinf(m);
    }
    
    public int updateQuit(Map<String, Object> m) {
        return safMngMapper.updateQuit(m);
    }
    
    public int updateAsnCancel(Map<String, Object> m) {
        return safMngMapper.updateSafMngInf(m);
    }
}
