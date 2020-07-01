/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.sysconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.projectJOA.com.util.ResultMap;

import org.springframework.stereotype.Service;

/**
 * @author Administrator
 *
 */
@Service
public interface SystemConfigService {

	public int getPatchVerCount(Map param) throws Exception;
	
	public int isCheckPatchProcess(Map param) throws Exception;
	
	public void createPatchProcess(Map param) throws Exception;
	
	public void createPatch(Map param) throws Exception;
	
	public List<ResultMap> getPatchHist(HashMap<String, Object> param) throws Exception;
	
	public int getPatchHistCount(HashMap<String, Object> param) throws Exception;
	
	public int updatePatchedYN(HashMap<String, Object> param) throws Exception;
	
	public void doPatched(HashMap<String, Object> param) throws Exception;
}
