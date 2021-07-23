package com.autot.log.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Mapper
@Repository
public interface LogMapper {
	
    List<Map<String, Object>> selectLogs(Map<String, Object> m);
    
    int insetLog(Map<String, Object> m);
}