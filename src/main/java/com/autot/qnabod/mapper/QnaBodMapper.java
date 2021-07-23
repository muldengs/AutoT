package com.autot.qnabod.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QnaBodMapper {
	
	List<Map<String, Object>> selectQnaBod(Map<String, Object> m);
	
	int insertQnaBod(Map<String, Object> m);
}
