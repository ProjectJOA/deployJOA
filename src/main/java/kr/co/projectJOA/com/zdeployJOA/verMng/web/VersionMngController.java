/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.verMng.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.projectJOA.com.util.ExNumberUtils;
import kr.co.projectJOA.com.util.ExStringUtils;
import kr.co.projectJOA.com.util.ResultMap;
import kr.co.projectJOA.com.view.JsonView;
import kr.co.projectJOA.com.zdeployJOA.base.web.BaseController;
import kr.co.projectJOA.com.zdeployJOA.cmm.cmUtils;
import kr.co.projectJOA.com.zdeployJOA.targetServMng.service.TargetServMngService;
import kr.co.projectJOA.svn.conn.svn.SVNConnectHandler;
import kr.co.projectJOA.svn.conn.svn.VersionControlFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.tmatesoft.svn.core.io.SVNRepository;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @author chcho
 *
 */
@Controller
public class VersionMngController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(VersionMngController.class);
			
	@Autowired
	private TargetServMngService targetServMngService;
	
	@RequestMapping("/vermng/version_move.do")
	public String goVersionMng( @RequestParam HashMap<String,Object> param
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {

		try{
			SVNConnectHandler.setInitRepository("");
		
			ResultMap serverInfo = targetServMngService.selectServerInfo(param);	
			SVNConnectHandler.setRepository((String)serverInfo.get("reposUrl"), (String)serverInfo.get("verContUserId"), (String)serverInfo.get("verContPwd"));
	
			cmUtils.setSessionSNum(request, (String)param.get("sNum"));
			
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return "redirect:/vermng/version_list.do";
	}	
	
	@RequestMapping("/vermng/version_list.do")
	public String getDeployMngList( @RequestParam HashMap<String,Object> param
             , ModelMap model
             , HttpServletRequest request
             , HttpServletResponse response) throws Exception {

		
		SVNRepository repository = null;
		
		try{
			repository = SVNConnectHandler.getRepository("");

			int srchStartVersion = ExNumberUtils.toInt(ExStringUtils.defaultIfNull(param.get("srchStartVersion"), ""+repository.getLatestRevision() )) ;
			int srchEndVersion = ExNumberUtils.toInt(ExStringUtils.defaultIfNull(param.get("srchEndVersion"), "0" )) ;
			
			
			long startRevision = srchStartVersion;
			long endRevision = srchEndVersion;

			List logLst = new VersionControlFactory().getLoglist(repository, startRevision, endRevision);
			model.addAttribute("logLst", logLst);
			
			HashMap<String,String> logMap = new HashMap<String,String>();
			if(logLst.size()>0){
				logMap = (HashMap<String,String>)logLst.get(logLst.size()-1);
				model.addAttribute("endRevision", logMap.get("version"));
			}
			
			model.addAttribute("srchStartVersion", srchStartVersion);
			model.addAttribute("srchEndVersion", srchEndVersion);

			String sNum = cmUtils.getSessionSNum(request);
			
			param.put("sNum",sNum);

			ResultMap serverInfo = targetServMngService.selectServerInfo(param);
			model.addAttribute("serverInfo", serverInfo);
			
		}catch(Exception e){
			logger.error("VersionMngController error : "+e.getMessage());
		}

		return "deploy/vermng/version_list";
	}
	
	/**
	 * 구간 배포시 시작버전과 끝 버전의 정보를 가져온다.
	 * @param param
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vermng/get_sec_verInfos.do")	
	public View deploySave( @RequestParam HashMap<String,Object> param, Model model 
								, HttpServletRequest request , HttpServletResponse response) throws Exception {
		
		SVNRepository repository = null;
		String resultCd = "000"; //성공 000, 실패 999
		try{
			repository = SVNConnectHandler.getRepository("");

			VersionControlFactory verControlFactory = new VersionControlFactory();
			
			int srchStartVersion = ExNumberUtils.toInt(ExStringUtils.defaultIfNull(param.get("dpT_startVer"), ""+repository.getLatestRevision() )) ;
			int srchEndVersion = ExNumberUtils.toInt(ExStringUtils.defaultIfNull(param.get("dpT_endVer"), "0" )) ;
			
			long startRevision = srchStartVersion;
			long endRevision = srchEndVersion;

			HashMap<String,String> logMap = new HashMap<String,String>();
			
			List logLst = verControlFactory.getSectionLoglist(repository, startRevision, endRevision);
			
			if(logLst.size() < 2){
				resultCd = "999"; //성공 000, 실패 999
			}
			
			model.addAttribute("resultCd", resultCd);
			model.addAttribute("logLst", logLst);

		}catch(Exception e){
			logger.error("VersionMngController error : "+e.getMessage());
			e.printStackTrace();
		}	

		
		return new JsonView();		
	}
	
	
	
	@RequestMapping("/vermng/version_listmore.do")
	public View getDeployMngListMore( @RequestParam HashMap<String,Object> param
            , ModelMap model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {
	
		SVNRepository repository = null;
		
		try{
			repository = SVNConnectHandler.getRepository("");

			int srchStartVersion = ExNumberUtils.toInt(ExStringUtils.defaultIfNull(param.get("prevEndVersion"), ""+repository.getLatestRevision() )) ;
			int srchEndVersion = ExNumberUtils.toInt(ExStringUtils.defaultIfNull(param.get("srchEndVersion"), "0" )) ;
			
			if(srchStartVersion > 0){
				srchStartVersion = srchStartVersion - 1;
			}
			
			long startRevision = srchStartVersion;
			long endRevision = srchEndVersion;

			List logLst = new VersionControlFactory().getLoglist(repository, startRevision, endRevision);
			model.addAttribute("logLst", logLst);

		}catch(Exception e){
			logger.error("VersionMngController error : "+e.getMessage());
		}	
		
		return new JsonView();
	}
	
	@RequestMapping("/vermng/popfile_list.do")
	public String popFileList( @RequestParam HashMap<String,Object> param
            , ModelMap model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {
		
		SVNRepository repository = null;
		
		try{
			repository = SVNConnectHandler.getRepository("");
		
			long targetRevision = Long.parseLong( ExStringUtils.defaultIfNull(param.get("ver"), ""+repository.getLatestRevision())) ;
			
			List fileLst = new VersionControlFactory().getVersionFileList(repository, targetRevision);
			model.addAttribute("fileLst", fileLst);
			model.addAttribute("targetRevision", targetRevision);
			
		}catch(Exception e){
			logger.error("VersionMngController error : "+e.getMessage());
		}		
		
		return "pop/deploy/vermng/popfile_list";
	}
	
	
	@RequestMapping("/vermng/popver_list.do")
	public String getVersionListForDeploy( @RequestParam HashMap<String,Object> param
             , ModelMap model
             , HttpServletRequest request
             , HttpServletResponse response) throws Exception {

		
		SVNRepository repository = null;
		
		try{
			repository = SVNConnectHandler.getRepository("");

			int srchStartVersion = ExNumberUtils.toInt(ExStringUtils.defaultIfNull(param.get("srchStartVersion"), ""+repository.getLatestRevision() )) ;
			int srchEndVersion = ExNumberUtils.toInt(ExStringUtils.defaultIfNull(param.get("srchEndVersion"), "0" )) ;
			
			
			long startRevision = srchStartVersion;
			long endRevision = srchEndVersion;

			List logLst = new VersionControlFactory().getLoglist(repository, startRevision, endRevision);
			model.addAttribute("logLst", logLst);
			
			HashMap<String,String> logMap = new HashMap<String,String>();
			if(logLst.size()>0){
				logMap = (HashMap<String,String>)logLst.get(logLst.size()-1);
				model.addAttribute("endRevision", logMap.get("version"));
			}
			
			model.addAttribute("srchStartVersion", srchStartVersion);
			model.addAttribute("srchEndVersion", srchEndVersion);

			String sNum = cmUtils.getSessionSNum(request);
			
			param.put("sNum",sNum);

			ResultMap serverInfo = targetServMngService.selectServerInfo(param);
			model.addAttribute("serverInfo", serverInfo);
			
		}catch(Exception e){
			logger.error("VersionMngController error : "+e.getMessage());
		}

		return "pop/deploy/vermng/popver_list";
	}	
}
