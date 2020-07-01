/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.deployMng.service;

import java.util.HashMap;
import java.util.List;

import kr.co.projectJOA.com.util.ResultMap;

import org.springframework.stereotype.Service;

/**
 * @author chcho
 *
 */
@Service
public interface DeployMngService {

	public void insertDeployInfo(HashMap<String, Object> param) throws Exception;
	
	public int updateDeployInfo(HashMap<String, Object> param) throws Exception;
	
	public List<ResultMap> selectDeployList(HashMap<String, Object> param) throws Exception;
	
	public int selectDeployListCount(HashMap<String, Object> param) throws Exception;
	
	public ResultMap selectDeployInfo(HashMap<String, Object> param) throws Exception;
	
	public List<ResultMap> selectDeployVers(HashMap<String, Object> param) throws Exception;
	
	public int updateDeployDepoState(HashMap<String, Object> param) throws Exception;
	
	public ResultMap selectLastBNumByRollback(HashMap<String, Object> param) throws Exception;
	
	public int updateDoneRollback(HashMap<String, Object> param) throws Exception;
}
