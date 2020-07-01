/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.deployMng.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.projectJOA.com.util.ResultMap;
import kr.co.projectJOA.com.zdeployJOA.deployMng.service.DeployMngService;

/**
 * @author chcho
 *
 */
@Service
public class DeployMngServiceImpl implements DeployMngService{

	private static final Logger logger = LoggerFactory.getLogger(DeployMngServiceImpl.class);
	
	@Autowired
	private DeployMngDAO deployMngDAO;

	@Override
	@Transactional(rollbackFor={Exception.class})	
	public void insertDeployInfo(HashMap<String, Object> param)
			throws Exception {
		// TODO Auto-generated method stub
		
		int bNum = deployMngDAO.getMaxBnum(param);
		param.put("bNum", bNum);
		
		deployMngDAO.insertDeployInfo(param);
		
		List<Map<String, Object>> versArr = (List<Map<String, Object>>)param.get("versArr");
		
		if(versArr.size()>0){
			for(int i=0;i<versArr.size();i++){
				HashMap<String, Object> verMap = (HashMap<String, Object>)versArr.get(i);
				verMap.put("sNum", param.get("sNum"));
				verMap.put("bNum", bNum);
				
				deployMngDAO.insertDeployVers(verMap);
			}
		}
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public int updateDeployInfo(HashMap<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		
		int cnt = deployMngDAO.updateDeployInfo(param);
		
		List<Map<String, Object>> versArr = (List<Map<String, Object>>)param.get("versArr");
		
		if(versArr.size()>0){
			deployMngDAO.deleteDeployVers(param);
			
			for(int i=0;i<versArr.size();i++){
				HashMap<String, Object> verMap = (HashMap<String, Object>)versArr.get(i);
				verMap.put("sNum", param.get("sNum"));
				verMap.put("bNum", param.get("bNum"));
				
				deployMngDAO.insertDeployVers(verMap);
				
				cnt++;
			}
		}
		
		return cnt;
	}

	@Override
	public List<ResultMap> selectDeployList(HashMap<String, Object> param) throws Exception{
		return deployMngDAO.selectDeployList(param);
	}
	
	@Override
	public int selectDeployListCount(HashMap<String, Object> param) throws Exception {
		return deployMngDAO.selectDeployListCount(param);
	}
	
	@Override
	public ResultMap selectDeployInfo(HashMap<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return deployMngDAO.selectDeployInfo(param);
	}

	@Override
	public List<ResultMap> selectDeployVers(HashMap<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return deployMngDAO.selectDeployVers(param);
	}
	
	@Override
	public int updateDeployDepoState(HashMap<String, Object> param) throws Exception{
		return deployMngDAO.updateDeployDepoState(param);
	}

	@Override
	public ResultMap selectLastBNumByRollback(HashMap<String, Object> param)
			throws Exception {
		// TODO Auto-generated method stub
		return deployMngDAO.selectLastBNumByRollback(param);
	}

	@Override
	public int updateDoneRollback(HashMap<String, Object> param)
			throws Exception {
		// TODO Auto-generated method stub
		return deployMngDAO.updateDoneRollback(param);
	}
	
	
}
