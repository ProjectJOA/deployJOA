/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.targetServMng.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.projectJOA.com.util.ResultMap;
import kr.co.projectJOA.com.zdeployJOA.targetServMng.service.TargetServMngService;

/**
 * @author chcho
 *
 */
@Service
public class TargetServMngServiceImpl implements TargetServMngService{

	@Autowired
	private TargetServMngDAO targetServMngDAO;
	
	/**
	 * 서버 목록
	 */
	@Override
	public List<ResultMap> selectServerMngList(HashMap<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return targetServMngDAO.selectServerMngList(param);
	}

	
	@Override
	public int selectServerMngListCount(HashMap<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return targetServMngDAO.selectServerMngListCount(param);
	}

	@Override
	public ResultMap selectServerInfo(HashMap<String, Object> param) throws Exception{
		return targetServMngDAO.selectServerInfo(param);
	}
	
	@Override
	@Transactional(rollbackFor={Exception.class})
	public void insertServerMngInfo(HashMap<String, Object> param) throws Exception{
		
		int maxSeq = targetServMngDAO.getSeverSeq(param);
		param.put("sNum", maxSeq);
		
		targetServMngDAO.insertServerMngInfo(param); //server info
		targetServMngDAO.insertReversionInfo(param); //svn info
	}


	@Override
	@Transactional(rollbackFor={Exception.class})
	public int updateServerMngInfo(HashMap<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		int updateCnt = targetServMngDAO.updateServerInfo(param);
		if(updateCnt > 0){
			targetServMngDAO.updateReversionInfo(param);
		}
		
		return updateCnt;
	}
	
	/**
	 * update project version
	 */
	@Override
	public int updateProjectVersion(HashMap<String, Object> param) throws Exception{
		return targetServMngDAO.updateProjectVersion(param);
	}
	
	@Override
	public int updateLastBNum(HashMap<String, Object> param) throws Exception{
		return targetServMngDAO.updateLastBNum(param);
	}

	@Override
	public int updateCISeverInit(HashMap<String, Object> param) throws Exception{
		return targetServMngDAO.updateCISeverInit(param);
	}
	
}
