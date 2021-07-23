package com.autot.call.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Mapper
@Repository
public interface CallMapper {
	
    List<Map<String, Object>> selectRsvdt(Map<String, Object> m);
    
    List<Map<String, Object>> selectRsvtmcarSeoul(Map<String, Object> m);
    
    List<Map<String, Object>> selectRsvtmcarPangyo(Map<String, Object> m);
    
    List<Map<String, Object>> selectRsvtmcarDs(Map<String, Object> m);
    
    List<Map<String, Object>> selectStnInfo(Map<String, Object> m);
    
    List<Map<String, Object>> selectStnPol(Map<String, Object> m);
    
    int selectSatCnt(Map<String, Object> m);
    
    int insertCalRsvN(Map<String, Object> m);
    
    int insertCalRsvY(Map<String, Object> m);
    
    int insertAsn(Map<String, Object> m);
    
    Map<String, Object> selectCalInfo(Map<String, Object> m);
    
    Map<String, Object> selectCarInfo(Map<String, Object> m);
    
    List<Map<String, Object>> selectAsnCarInfo(Map<String, Object> m);
    
    int selectServiceArea(Map<String, Object> m);    
    
}