package com.autot.safmng.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SafMngMapper {

	List<Map<String, Object>> selectSafMngReg();

	List<Map<String, Object>> selectCar(Map<String, Object> m);
	
	Map<String, Object> selectSafMngLogin(Map<String, Object> m);
	
	Map<String, Object> selectSafMngtel(Map<String, Object> m);
	
	int updateSafMngPwd(Map<String, Object> m);
	
	int insertSafMngInf(Map<String, Object> m);
	
	int updateSafMngInf(Map<String, Object> m);
	
	int deleteSafMngInf(Map<String, Object> m);
	
	int insetSafMngInfHis(Map<String, Object> m);

	List<Map<String, Object>> selectSafMngCal(Map<String, Object> m);
	
	Map<String, Object> selectSafMngCalDetail(Map<String, Object> m);

	List<Map<String, Object>> selectSafMngAsn(Map<String, Object> m);
	
	Map<String, Object> selectSafMngAsnDetail(Map<String, Object> m);
	
	int selectSafMngAsnCnt(Map<String, Object> m);
	
	int insertSafMngAsn(Map<String, Object> m);
	
	int updateRide(Map<String, Object> m);
	
	int updateCarinf(Map<String, Object> m);
	
	int updateQuit(Map<String, Object> m);
	
	int updateAsnCancel(Map<String, Object> m);
}
