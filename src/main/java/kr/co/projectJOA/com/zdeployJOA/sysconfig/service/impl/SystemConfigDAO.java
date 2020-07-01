/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.sysconfig.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.projectJOA.com.cmmn.service.impl.CommonAbstractDAO;
import kr.co.projectJOA.com.constant.GlobalConstant;
import kr.co.projectJOA.com.util.ExStringUtils;
import kr.co.projectJOA.com.util.ResultMap;

import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 *
 */
@Repository
public class SystemConfigDAO  extends CommonAbstractDAO{

	//PATCH VERSION 존재여부 파악
	public int getPatchVerCount(Map param) throws Exception {
		return (int)select("systemConfigDAO.getPatchVerCount", param);
	}
	
	public int isCheckPatchProcess(Map param) throws Exception {
		return (int)select("systemConfigDAO.isCheckPatchProcess", param);
	}
	
	public void createPatchProcess(Map param) throws Exception{
		update("systemConfigDAO.createPatchTable",param);
	}
	
	//patch insert
	public void createPatch(Map param) throws Exception{
		insert("systemConfigDAO.createPatch", param);
	}
	
	public List<ResultMap> getPatchHist(HashMap<String, Object> param) throws Exception {
		return (List<ResultMap>)list("systemConfigDAO.getPatchHist", param);
	}
	
	public int getPatchHistCount(HashMap<String, Object> param) {
		
		int cnt = 0;
		try{
			if(GlobalConstant.IS_ABLE_PATCHPROCESS){
				cnt =  (int)select("systemConfigDAO.getPatchHistCount", param);
			}
		}catch(Exception e){
			System.err.println("error : Patch History : "+e.getMessage());
		}
		return cnt;
	}
	
	public int updatePatchedYN(HashMap<String, Object> param) throws Exception {
		return update("systemConfigDAO.updatePatchedYN", param);
	}
	
	public void doPatched(HashMap<String, Object> param) throws Exception {
		String patchVer = ExStringUtils.defaultIfNull(param.get("patchVer"), "")  ;
		if(!"".equals(patchVer)){
			String sqlId = "systemConfigDAO.patched_ver_"+patchVer;
			
			update(sqlId, param);
			updatePatchedYN(param);
		}
	}
}
