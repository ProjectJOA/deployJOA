/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.sysconfig.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.projectJOA.com.util.ResultMap;
import kr.co.projectJOA.com.zdeployJOA.sysconfig.service.SystemConfigService;

/**
 * @author Administrator
 *
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService{

	@Autowired
	private SystemConfigDAO SystemConfigDAO;
	
	
	//PATCH VERSION 존재여부 파악
	public int getPatchVerCount(Map param) throws Exception {
		return SystemConfigDAO.getPatchVerCount(param);
	}
	
	public int isCheckPatchProcess(Map param) throws Exception{
		return SystemConfigDAO.isCheckPatchProcess(param);
	}
	
	public void createPatchProcess(Map param) throws Exception{
		SystemConfigDAO.createPatchProcess(param);
	}
	
	//patch insert
	public void createPatch(Map param) throws Exception{
		SystemConfigDAO.createPatch(param);
	}
	
	@Override
	public List<ResultMap> getPatchHist(HashMap<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return SystemConfigDAO.getPatchHist(param);
	}
	
	
	public int getPatchHistCount(HashMap<String, Object> param) throws Exception {
		return SystemConfigDAO.getPatchHistCount(param);
	}
	
	public int updatePatchedYN(HashMap<String, Object> param) throws Exception {
		return SystemConfigDAO.updatePatchedYN(param);
	}
	
	public void doPatched(HashMap<String, Object> param) throws Exception {
		SystemConfigDAO.doPatched(param);
	}
}
