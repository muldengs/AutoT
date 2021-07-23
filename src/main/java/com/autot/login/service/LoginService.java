package com.autot.login.service;

import org.springframework.stereotype.Service;

import com.autot.login.mapper.LoginMapper;

import java.util.Map;
import java.util.List;

@Service
public class LoginService {
    private LoginMapper loginMapper;

    public LoginService(LoginMapper loginMapper){
        this.loginMapper = loginMapper;
    }

    public List<Map<String, Object>> getTrminf() {
        return loginMapper.selectTrminf();
    }

    public List<Map<String, Object>> getUsrTrmchk(Map<String, Object> m) {
        return loginMapper.selectUsrTrmchk(m);
    }
    
    public List<Map<String, Object>> getReg() {
        return loginMapper.selectReg();
    }

    public String getUsrCer(Map<String, Object> m) {
        return loginMapper.selectUsrCer(m);
    }

    public int insetUsrCer(Map<String, Object> m) {
        return loginMapper.insetUsrCer(m);
    }

    public int deleteUsrCer(Map<String, Object> m) {
        return loginMapper.deleteUsrCer(m);
    }

    public int insetUsrTrmchk(Map<String, Object> m) {
        return loginMapper.insetUsrTrmchk(m);
    }

    public int deleteUsrTrmchk(Map<String, Object> m) {
        return loginMapper.deleteUsrTrmchk(m);
    }
}