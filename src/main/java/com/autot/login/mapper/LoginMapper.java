package com.autot.login.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Mapper
@Repository
public interface LoginMapper {
	
    List<Map<String, Object>> selectTrminf();
    
    List<Map<String, Object>> selectUsrTrmchk(Map<String, Object> m);
    
    List<Map<String, Object>> selectReg();
    
    String selectUsrCer(Map<String, Object> m);
    
    int insetUsrCer(Map<String, Object> m);
    
    int deleteUsrCer(Map<String, Object> m);
    
    int insetUsrTrmchk(Map<String, Object> m);
    
    int deleteUsrTrmchk(Map<String, Object> m);
}