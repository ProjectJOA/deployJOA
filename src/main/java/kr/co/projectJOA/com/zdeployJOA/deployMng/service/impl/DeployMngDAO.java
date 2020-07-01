/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.deployMng.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.projectJOA.com.cmmn.service.impl.CommonAbstractDAO;
import kr.co.projectJOA.com.util.ResultMap;

/**
 * @author chcho
 *
 */
@Repository
public class DeployMngDAO extends CommonAbstractDAO{

	public int getMaxBnum(HashMap<String, Object> param) throws Exception {
		return (int)select("deployMngDAO.getMaxBnum", param);
	}

	public int updateDeployInfo(HashMap<String, Object> param) throws Exception {
		return update("deployMngDAO.updateDeployInfo", param);
	}
	
	public void insertDeployInfo(HashMap<String, Object> param) throws Exception {
		insert("deployMngDAO.insertDeployInfo", param);
	}
	
	public void insertDeployVers(HashMap<String, Object> param) throws Exception {
		insert("deployMngDAO.insertDeployVers", param);
	}
	
	public void deleteDeployVers(HashMap<String, Object> param) throws Exception {
		delete("deployMngDAO.deleteDeployVers", param);
	}
	
	public int updateDeployAppState(HashMap<String, Object> param) throws Exception {
		return update("deployMngDAO.updateDeployAppState", param);
	}	
	
	public int updateDeployDepoState(HashMap<String, Object> param) throws Exception {
		return update("deployMngDAO.updateDeployDepoState", param);
	}		
	
	public List<ResultMap> selectDeployList(HashMap<String, Object> param) throws Exception {
		return (List<ResultMap>)list("deployMngDAO.selectDeployList", param);
	}
	
	public int selectDeployListCount(HashMap<String, Object> param) throws Exception {
		return (int)select("deployMngDAO.selectDeployListCount", param);
	}
	
	public ResultMap selectDeployInfo(HashMap<String, Object> param) throws Exception {
		return (ResultMap)select("deployMngDAO.selectDeployInfo", param);
	}
	
	public List<ResultMap> selectDeployVers(HashMap<String, Object> param) throws Exception {
		return (List<ResultMap>)list("deployMngDAO.selectDeployVers", param);
	}
	
	public ResultMap selectLastBNumByRollback(HashMap<String, Object> param) throws Exception {
		return (ResultMap)select("deployMngDAO.selectLastBNumByRollback", param);
	}
	
	public int updateDoneRollback(HashMap<String, Object> param) throws Exception {
		return update("deployMngDAO.updateDoneRollback", param);
	}	
}
