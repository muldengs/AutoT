package com.autot.user.service;

import org.springframework.stereotype.Service;

import com.autot.user.mapper.UserMapper;

import java.util.Map;
import java.util.List;

@Service
public class UserService {
    private UserMapper userMapper;

    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public List<Map<String, Object>> getUsers(Map<String, Object> m) {
        return userMapper.selectUsers(m);
    }

    public Map<String, Object> getUserTel(Map<String, Object> m) {
        return userMapper.selectUserTel(m);
    }

    public Map<String, Object> getUserEml(Map<String, Object> m) {
        return userMapper.selectUserEml(m);
    }

    public Map<String, Object> getUserInfo(Map<String, Object> m) {
        return userMapper.selectUserInfo(m);
    }

    public Map<String, Object> getLogin(Map<String, Object> m) {
        return userMapper.selectLogin(m);
    }

    public int getDuplicate(Map<String, Object> m) {
        return userMapper.selectDuplicate(m);
    }

    public int getInsertUser(Map<String, Object> m) {
        return userMapper.insertUser(m);
    }

    public int getUpdateUser(Map<String, Object> m) {
        return userMapper.updateUser(m);
    }

    public int getUpdateUserPwd(Map<String, Object> m) {
        return userMapper.updateUserPwd(m);
    }

    public int getDeleteUser(Map<String, Object> m) {
        return userMapper.deleteUser(m);
    }

    public int getInsetUserHis(Map<String, Object> m) {
        return userMapper.insetUserHis(m);
    }
}