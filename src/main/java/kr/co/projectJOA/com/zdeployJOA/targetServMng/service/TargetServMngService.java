/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.targetServMng.service;

import java.util.HashMap;
import java.util.List;

import kr.co.projectJOA.com.util.ResultMap;

import org.springframework.stereotype.Service;

/**
 * @author chcho
 *
 */
@Service
public interface TargetServMngService {

	public List<ResultMap> selectServerMngList(HashMap<String, Object> param) throws Exception;

	public int selectServerMngListCount(HashMap<String, Object> param) throws Exception;
	
	public ResultMap selectServerInfo(HashMap<String, Object> param) throws Exception;
	
	public void insertServerMngInfo(HashMap<String, Object> param) throws Exception;
	
	public int updateServerMngInfo(HashMap<String, Object> param) throws Exception;
	
	public int updateLastBNum(HashMap<String, Object> param) throws Exception;

	public int updateCISeverInit(HashMap<String, Object> param) throws Exception;
	
	public int updateProjectVersion(HashMap<String, Object> param) throws Exception;
}
