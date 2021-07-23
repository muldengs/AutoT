package com.autot.main.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Mapper
@Repository
public interface MainMapper {
	
    List<Map<String, Object>> selectCal(Map<String, Object> m);
    
    Map<String, Object> selectCalDetail(Map<String, Object> m);
    
    Map<String, Object> selectAsnSeoul(Map<String, Object> m);
    
    Map<String, Object> selectAsnSeoulNon(Map<String, Object> m);
	
	int updateCalCancel(Map<String, Object> m);
	
	int updateCarinfCalCancel(Map<String, Object> m);
}