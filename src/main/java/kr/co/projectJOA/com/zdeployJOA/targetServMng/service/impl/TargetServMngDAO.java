/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.targetServMng.service.impl;

import java.util.HashMap;
import java.util.List;

import kr.co.projectJOA.com.cmmn.service.impl.CommonAbstractDAO;
import kr.co.projectJOA.com.util.ResultMap;

import org.springframework.stereotype.Repository;

/**
 * @author chcho
 *
 */
@Repository
public class TargetServMngDAO extends CommonAbstractDAO{

	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ResultMap> selectServerMngList(HashMap<String, Object> param) throws Exception {
		return (List<ResultMap>)list("targetServMngDAO.selectServerMngList", param);
	}	
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int selectServerMngListCount(HashMap<String, Object> param) throws Exception {
		return (int)select("targetServMngDAO.selectServerMngListCount", param);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int getSeverSeq(HashMap<String, Object> param) throws Exception {
		return (int)select("targetServMngDAO.getSeverSeq", param);
	}
	
	/**
	 * 
	 * @param param
	 * @throws Exception
	 */
	public void insertServerMngInfo(HashMap<String, Object> param) throws Exception {
		insert("targetServMngDAO.insertServerMngInfo", param);
	}

	/**
	 * 
	 * @param param
	 * @throws Exception
	 */
	public void insertReversionInfo(HashMap<String, Object> param) throws Exception {
		insert("targetServMngDAO.insertReversionInfo", param);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public ResultMap selectServerInfo(HashMap<String, Object> param) throws Exception {
		return (ResultMap)select("targetServMngDAO.selectServerInfo", param);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateServerInfo(HashMap<String, Object> param) throws Exception {
		return update("targetServMngDAO.updateServerInfo", param);
	}
	
	/**
	 * project version update
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateProjectVersion(HashMap<String, Object> param) throws Exception {
		return update("targetServMngDAO.updateProjectVersion", param);
	}
	
	/**
	 * 최신배포번호 UPDATE
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateLastBNum(HashMap<String, Object> param) throws Exception {
		return update("targetServMngDAO.updateLastBNum", param);
	}
	
	
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateReversionInfo(HashMap<String, Object> param) throws Exception {
		return update("targetServMngDAO.updateReversionInfo", param);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateCISeverInit(HashMap<String, Object> param) throws Exception {
		return update("targetServMngDAO.updateCISeverInit", param);
	}
	
}
