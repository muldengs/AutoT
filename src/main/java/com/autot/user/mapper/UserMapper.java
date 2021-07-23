package com.autot.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {
	
    List<Map<String, Object>> selectUsers(Map<String, Object> m);
    
    Map<String, Object> selectUserTel(Map<String, Object> m);
    
    Map<String, Object> selectUserEml(Map<String, Object> m);
    
    Map<String, Object> selectUserInfo(Map<String, Object> m);
    
    Map<String, Object> selectLogin(Map<String, Object> m);
    
    int selectDuplicate(Map<String, Object> m);
    
    int insertUser(Map<String, Object> m);
    
    int updateUser(Map<String, Object> m);
    
    int updateUserPwd(Map<String, Object> m);
    
    int deleteUser(Map<String, Object> m);
    
    int insetUserHis(Map<String, Object> m);
}