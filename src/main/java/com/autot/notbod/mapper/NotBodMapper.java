package com.autot.notbod.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NotBodMapper {

	List<Map<String, Object>> selectNotBod(Map<String, Object> m);
	
	Map<String, Object> selectNotBodDetail(Map<String, Object> m);
	
	int insertNotBod(Map<String, Object> m);
}
