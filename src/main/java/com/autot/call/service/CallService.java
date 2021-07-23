package com.autot.call.service;

import org.springframework.stereotype.Service;

import com.autot.call.mapper.CallMapper;

import java.util.Map;
import java.util.List;

@Service
public class CallService {
    private CallMapper callMapper;

    public CallService(CallMapper callMapper){
        this.callMapper = callMapper;
    }
    
    public List<Map<String, Object>> selectRsvdt(Map<String, Object> m) {
        return callMapper.selectRsvdt(m);
    }
    
    public List<Map<String, Object>> selectRsvtmcarSeoul(Map<String, Object> m) {
        return callMapper.selectRsvtmcarSeoul(m);
    }
    
    public List<Map<String, Object>> selectRsvtmcarPangyo(Map<String, Object> m) {
        return callMapper.selectRsvtmcarPangyo(m);
    }
    
    public List<Map<String, Object>> selectRsvtmcarDs(Map<String, Object> m) {
        return callMapper.selectRsvtmcarDs(m);
    }
    
    public List<Map<String, Object>> selectStnInfo(Map<String, Object> m) {
        return callMapper.selectStnInfo(m);
    }
    
    public List<Map<String, Object>> selectStnPol(Map<String, Object> m) {
        return callMapper.selectStnPol(m);
    }
    
    public int selectSatCnt(Map<String, Object> m) {
        return callMapper.selectSatCnt(m);
    }
    
    public int insertCalRsvN(Map<String, Object> m) {
        return callMapper.insertCalRsvN(m);
    }
    
    public int insertCalRsvY(Map<String, Object> m) {
        return callMapper.insertCalRsvY(m);
    }
    
    public int insertAsn(Map<String, Object> m) {
        return callMapper.insertAsn(m);
    }
    
    public Map<String, Object> selectCalInfo(Map<String, Object> m) {
        return callMapper.selectCalInfo(m);
    }
    
    public Map<String, Object> selectCarInfo(Map<String, Object> m) {
        return callMapper.selectCarInfo(m);
    }
    
    public List<Map<String, Object>> selectAsnCarInfo(Map<String, Object> m) {
        return callMapper.selectAsnCarInfo(m);
    }
    
    public int selectServiceArea(Map<String, Object> m) {
        return callMapper.selectServiceArea(m);
    }

}