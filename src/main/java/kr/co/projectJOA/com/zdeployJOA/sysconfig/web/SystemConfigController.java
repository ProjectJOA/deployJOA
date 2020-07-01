/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.sysconfig.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.projectJOA.com.constant.GlobalConstant;
import kr.co.projectJOA.com.util.ResultMap;
import kr.co.projectJOA.com.zdeployJOA.base.web.BaseController;
import kr.co.projectJOA.com.zdeployJOA.deployMng.web.DeployMngController;
import kr.co.projectJOA.com.zdeployJOA.sysconfig.service.SystemConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Administrator
 *
 */
@Controller
public class SystemConfigController extends BaseController{

	
	private static Logger logger = LoggerFactory.getLogger(SystemConfigController.class);
	
	@Autowired
	private SystemConfigService systemConfigService;
	
	/**
	 * 패치 history
	 * @param param
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sysconfig/patch_his.do")
	public String goDeployMng( @RequestParam HashMap<String,Object> param
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {
		
		List<ResultMap> patchHist = new ArrayList<ResultMap>();
		
		try{
			if(GlobalConstant.IS_ABLE_PATCHPROCESS){
				patchHist = systemConfigService.getPatchHist(param);
			}
		
			model.addAttribute("patchHist", patchHist);
		}catch(Exception e){
			model.addAttribute("patchHist", patchHist);
		}
		
		return "deploy/sysconfig/sys_update_list";
	}
	
	/**
	 * 패치 수행
	 * @param param
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sysconfig/do_patch.do")
	public String doPatch( @RequestParam HashMap<String,Object> param
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {
		
		systemConfigService.doPatched(param);
		
		return "redirect:/sysconfig/patch_his.do";
	}
}
