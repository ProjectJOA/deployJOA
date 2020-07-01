/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.deployMng.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.projectJOA.com.exception.ApplicationAlertException;
import kr.co.projectJOA.com.util.DomXMLParser;
import kr.co.projectJOA.com.util.ExStringUtils;
import kr.co.projectJOA.com.util.JsonToObject;
import kr.co.projectJOA.com.util.ResultMap;
import kr.co.projectJOA.com.view.JsonView;
import kr.co.projectJOA.com.zdeployJOA.base.web.BaseController;
import kr.co.projectJOA.com.zdeployJOA.cmm.cmUtils;
import kr.co.projectJOA.com.zdeployJOA.deployMng.service.DeployMngService;
import kr.co.projectJOA.com.zdeployJOA.exception.UserDefineBuildException;
import kr.co.projectJOA.com.zdeployJOA.targetServMng.service.TargetServMngService;
import kr.co.projectJOA.svn.conn.svn.SVNConnectHandler;
import kr.co.projectJOA.svn.export.ExSVNFiles;
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.maven.cli.MavenCli;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.tmatesoft.svn.core.io.SVNRepository;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @author chcho
 *
 */
@Controller
public class DeployMngController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger(DeployMngController.class);
	
	@Autowired
	private TargetServMngService targetServMngService;
	
	@Autowired
	private DeployMngService deployMngService;
	
	private String DEPLOY_INIT_PATH = "/project_init";
	
	@RequestMapping("/deploymng/deploy_move.do")
	public String goDeployMng( @RequestParam HashMap<String,Object> param
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {

		SVNConnectHandler.setInitRepository("");
		
		ResultMap serverInfo = targetServMngService.selectServerInfo(param);	
		
		SVNConnectHandler.setRepository((String)serverInfo.get("reposUrl"), (String)serverInfo.get("verContUserId"), (String)serverInfo.get("verContPwd"));
		
		cmUtils.setSessionSNum(request, (String)param.get("sNum"));
		
		return "redirect:/deploymng/deploy_list.do";
	}	
	
	
	@RequestMapping("/deploymng/deploy_list.do")
	public String getDeployMngList( @RequestParam HashMap<String,Object> param
             , ModelMap model
             , HttpServletRequest request
             , HttpServletResponse response) throws Exception {

		String sNum = cmUtils.getSessionSNum(request);
		if(sNum == null || "".equals(sNum)){
			return "redirect:/servmng/targetser_list.do";
		}
		
		param.put("sNum",sNum);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		super.initPaginationInfo(paginationInfo, param, model); 
		

		ResultMap serverInfo = targetServMngService.selectServerInfo(param);
		model.addAttribute("serverInfo", serverInfo);
		
		List<ResultMap> deployList = deployMngService.selectDeployList(param);
		int totalCnt = deployMngService.selectDeployListCount(param); 

		paginationInfo.setTotalRecordCount(totalCnt);
		
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("deployList", deployList);
		model.addAttribute("params", param);
		
		return "deploy/deploymng/deploy_list";
	}
	
	@RequestMapping("/deploymng/deploy_editor.do")
	public String getDeployMngEditor( @RequestParam HashMap<String,Object> param
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {

		String sNum = cmUtils.getSessionSNum(request);
		param.put("sNum",sNum);

		ResultMap deployInfo = deployMngService.selectDeployInfo(param);
		
		List<ResultMap> deployVers = deployMngService.selectDeployVers(param);
		
		model.addAttribute("params", param);
		model.addAttribute("deployInfo", deployInfo);
		model.addAttribute("deployVers", deployVers);
		
		return "deploy/deploymng/deploy_register";
		
	}
	
	@RequestMapping("/deploymng/deploy_view.do")
	public String getDeployMngDetailView( @RequestParam HashMap<String,Object> param
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {

		String sNum = cmUtils.getSessionSNum(request);
		param.put("sNum",sNum);

		model.addAttribute("params", param);
		
		logger.debug("=======param====="+param.toString());
		
		ResultMap deployInfo = deployMngService.selectDeployInfo(param);
		
		List<ResultMap> deployVers = deployMngService.selectDeployVers(param);
		
		model.addAttribute("deployInfo", deployInfo);
		model.addAttribute("deployVers", deployVers);
		
		return "deploy/deploymng/deploy_view";
		
	}	
	
	@RequestMapping("/deploymng/deploy_register.do")
	public String goDeployRegister( @RequestParam HashMap<String,Object> param
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {
		
		String sNum = cmUtils.getSessionSNum(request);
		param.put("sNum", sNum);
//		model.addAttribute("sNum", cmUtils.getSessionSNum(request));
		model.addAttribute("params", param);
		
		return "deploy/deploymng/deploy_register";
	}
	
	@RequestMapping("/deploymng/deploy_save.do")
	public View deploySave( Model model
            , HttpServletRequest request
            , HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		Map<String,String[]> params = request.getParameterMap();
		for (Map.Entry<String,String[]> entry : params.entrySet()) {
		  String v[] = entry.getValue();
		  Object o = (v.length == 1) ? v[0] : v;
		  
		  jsonObj.put(entry.getKey(), o);
		}

		HashMap<String, Object> tMap = (HashMap<String, Object>)JsonToObject.toMap(jsonObj.getJSONObject("jsonInfo"));
		
/*		logger.debug("==============toMap.sNum=="+tMap.get("sNum"));
		logger.debug("==============toMap.wasRestartYn=="+tMap.get("wasRestartYn"));
		logger.debug("==============toMap.depoVers=="+tMap.get("depoVers"));*/
		
		List<Map<String, Object>> versArr = (List<Map<String, Object>>)tMap.get("versArr");
		
		if(versArr.size()>0){
			for(int i=0;i<versArr.size();i++){
				Map<String, Object> verMap = (Map<String, Object>)versArr.get(i);
//				logger.debug("===========verMap["+i+"]========"+verMap);
			}
		}
		
		if(!"".equals(ExStringUtils.defaultIfNull(tMap.get("bNum"), ""))){
			deployMngService.updateDeployInfo(tMap);
		}else{
			deployMngService.insertDeployInfo(tMap);
		}
		
		model.addAttribute("result", "001");		
		
		return new JsonView();
	}
	
	@RequestMapping("/deploymng/doDeploy.do")
	public View doDeploy(@RequestParam HashMap<String,Object> param
			            , Model model
			            , HttpServletRequest request
            			, HttpServletResponse response) throws Exception{
		
		SVNRepository repository = null;
		
		boolean successed = false;
		boolean antSuccessed = false;
		
		String tarSrcRtpath = "";
		String refVer = "1.0.0";
		
		
		try{
			repository = SVNConnectHandler.getRepository("");
			
			ResultMap serverInfo = targetServMngService.selectServerInfo(param);
			
			tarSrcRtpath = serverInfo.get("tarSrcRtpath").toString();
			String buildType = serverInfo.getString("buildType");
			
			String rootPath = tarSrcRtpath+"/deploy/buildsrc/"+param.get("bNum"); 

			/** log file 생성**/
			String logfileNm = tarSrcRtpath+"/logs/deploy_log_"+param.get("bNum").toString()+".log";
			System.setProperty("logFilename", logfileNm);
		    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		    ctx.reconfigure();	
		    /** log file 생성**/
			
			List verLst = deployMngService.selectDeployVers(param);
			List tVersionArr = new ArrayList();
			
			if(verLst.size()>0){
				for(int i=0;i<verLst.size();i++){
					ResultMap rMap = (ResultMap)verLst.get(i);
					tVersionArr.add(Long.parseLong(rMap.get("bSnum").toString()));
				}
			}
			if("M".equals(buildType)){ //Maven 빌드
				successed = deployProcess(request, repository, tVersionArr,tarSrcRtpath,param.get("bNum").toString(), refVer, serverInfo);
			}else{	//Ant 빌드
				successed = antDeployProcess(request,repository, tVersionArr, tarSrcRtpath, param.get("bNum").toString(), refVer, serverInfo);				
				if(successed && !"N".equals(buildType)){ //java compile 일 경우만.
					antInitDeploy(request,serverInfo,param.get("bNum").toString());
				}
			}
			
			if(successed){
				//last bNum를 배포버전번호로 update를 수행 
				param.put("lastBNum", param.get("bNum").toString());
				targetServMngService.updateLastBNum(param);
			}
			
			if(successed){
				param.put("depoState", "W9"); //배포중
				model.addAttribute("result", "001");
			}else{
				param.put("depoState", "WX"); //배포실패
				model.addAttribute("result", "900");
			}
			
			deployMngService.updateDeployDepoState(param);			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("result", "900");
			logger.error(e.getMessage());
		}
		
		return new JsonView();
	}
	
	/**
	 * maven deploy
	 * @param repository
	 * @param tVersionArr
	 * @param tarSrcRtpath
	 * @param bNum
	 * @param refVer
	 * @param serverInfo
	 * @return
	 * @throws Exception
	 */
	private boolean deployProcess(HttpServletRequest request, SVNRepository repository, List tVersionArr,String tarSrcRtpath, String bNum, String refVer, ResultMap serverInfo) throws Exception{
		boolean isComplete = false;
		
		try{

			String projectId = serverInfo.getString("projectId");
			String projectVersion = serverInfo.getString("projectVersion");
			String webRootPath = serverInfo.getString("deployPath");
			String sNum = serverInfo.getString("sNum");
			String lastBNum = serverInfo.getString("lastBNum");
			String buildType = serverInfo.getString("buildType");
			String projectPreVersion = serverInfo.getString("projectVersion")+"."+lastBNum; //lastBNum를 합해서 projectPreVersion을 만든다.
			String projectNewVersion = serverInfo.getString("projectVersion")+"."+bNum; //bNum를 합해서 projectNewVersion을 만든다.
//logger.info("lastBNum="+lastBNum);			
//logger.info("bNum="+bNum);
			String rootPath = tarSrcRtpath+"/deploy/buildsrc/"+bNum;
			
			String srcpath = tarSrcRtpath+"/deploy/buildsrc/"+bNum;
			String buildpath = tarSrcRtpath+"/deploy/build/"+bNum;
			logger.info("tarSrcRtpath : "+tarSrcRtpath);
			logger.info("getting SVN files start +++++++++");
			new ExSVNFiles(repository).getFiles(rootPath, tVersionArr);
			logger.info("getting SVN files end +++++++++");
			
			File webappDir = new File(rootPath+"/src/main/webapp");

			if(!webappDir.exists()){
				webappDir.mkdirs();
			}
			
			String jarfileNm = projectId+"-"+projectVersion+".jar"; //maven deploy 일경우에만
			
			String settingXmlPath = getGlobalMavenSettingFile(request);
			String outDir = tarSrcRtpath+"/project_init/";
			String localMavenRepo = tarSrcRtpath+"/maven/repository";
			String targetRtPath = tarSrcRtpath+DEPLOY_INIT_PATH;
			
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.debug("++ DeployJOA Start Deploy......");
			logger.debug("++ Deploy No : "+bNum);
			logger.debug("++ Maven Build.....");
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");			
			
/*			String[] args = new String[]{ "-Dbuilddir="+buildpath
											,"-Dsrcdir="+srcpath
											,"-DsystemPath_basedir="+targetRtPath
											,"-Ddeploy.projectId="+projectId
											,"-Ddeploy.projectVersion="+projectVersion
											,"-Dproject.ref.version="+projectPreVersion
											,"-Dproject.ref.jar="+tarSrcRtpath+"/project_init/"+jarfileNm //project activate시 만들어진 jar (local repository에 등록된) 
											,"-f="+tarSrcRtpath+"/deploy/pom.xml"
											,"-s="+settingXmlPath
											,"clean"
											,"compile"};
			
			if(mavenTargetVerionBuild( args, outDir, targetRtPath, localMavenRepo)){
				isComplete = true;
				isComplete = mavenInitDeploy(request, serverInfo, lastBNum);
				logger.info("[DeployJOA] Build Successs.....");
			}*/
			
			isComplete = mavenInitDeploy(request, serverInfo, bNum);
			
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			if(isComplete){
				logger.info("[DeployJOA] Build Successs.....");
			}else{
				logger.info("[DeployJOA] Build failed.....");
			}
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			
		}catch(UserDefineBuildException ue){
			isComplete = false;
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("[DeployJOA] Build failed.....");
			logger.error(ue.getMessage());
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}catch(Exception e){
			isComplete = false;
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("[DeployJOA] Build failed.....");
			logger.error(e.getMessage());
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
		
		return isComplete;
	}

	/**
	 * maven init build
	 * @param args
	 * @param outDir
	 * @param targetRtPath
	 * @param localMavenRepo
	 * @return
	 */
	private boolean mavenTargetVerionBuild(String[] args, String outDir, String targetRtPath, String localMavenRepo) throws Exception{
		
		boolean resultBoolean = false;
		int resultPointer = 1;
		
		MavenCli maven = new MavenCli();
		System.setProperty(maven.MULTIMODULE_PROJECT_DIRECTORY, targetRtPath);
		System.setProperty(maven.LOCAL_REPO_PROPERTY, localMavenRepo);
		
		resultPointer = maven.doMain(args, outDir, System.out, System.out);
		if(resultPointer == 0) resultBoolean = true;
		
		return resultBoolean;
	}	
	
	/**
	 * maven copy init, verions files, make jar, zip by ant-build
	 * @param request
	 * @param serverInfo
	 * @param bNum
	 * @return
	 * @throws Exception
	 */
	private boolean mavenInitDeploy(HttpServletRequest request,ResultMap serverInfo, String bNum) throws Exception{
		boolean isComplete = false;
		
        DefaultLogger consoleLogger = getConsoleLogger();		
        Project project = new Project();
        
        try{
			String projectId = serverInfo.getString("projectId");
			String projectVersion = serverInfo.getString("projectVersion")+"."+bNum;
			String tarSrcRtpath = serverInfo.get("tarSrcRtpath").toString();
			String webRootPath = serverInfo.getString("deployPath");
//			String targetRtPath = tarSrcRtpath+DEPLOY_INIT_PATH;
//			String buildType = serverInfo.getString("buildType");
//			String lastBNum = serverInfo.getString("lastBNum");
			String buildInitPath = getBuildFilePath(request,"maven-build"); //maven build.xml file
			String buildRefFilepath = serverInfo.get("buildRefFile1").toString();
			
	        File buildFile = new File(""+buildInitPath);
	        
	        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
	        project.addBuildListener(consoleLogger);
	        
            project.fireBuildStarted();
            project.init();
            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            project.addReference("ant.projectHelper", projectHelper);
            project.setProperty("deploy.basedir", tarSrcRtpath);
            project.setProperty("deploy.webroot", webRootPath);
            project.setProperty("deploy.deployno", bNum);
//            project.setProperty("deploy.init_jarfile", projectId+"-"+projectVersion); //2017-10-12
            project.setProperty("deploy.init_jarfile", projectId+"-"+serverInfo.getString("projectVersion")); //2017-10-12
            project.setProperty("deploy.war_path", projectId+"-"+serverInfo.getString("projectVersion")); //2017-10-23
            project.setProperty("deploy.buildRefFilePath", tarSrcRtpath+"/project_init/"+buildRefFilepath);
            
            projectHelper.parse(project, buildFile);
            
            String targetToExecute = "makeVersionDeploy";
            project.executeTarget(targetToExecute);
            project.fireBuildFinished(null);
            isComplete = true;			
        	
        } catch (BuildException buildException) {
            project.fireBuildFinished(buildException);
            throw new RuntimeException("!!! DeployJOA Build Error !!!", buildException);
        }	
        
		return isComplete;		
	}	
	/**
	 * maven setting file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private String getGlobalMavenSettingFile(HttpServletRequest request) throws Exception{
		String webrootRealPath = request.getSession().getServletContext().getRealPath("/");
		String settingFilePath = webrootRealPath.replaceAll("\\\\", "/");
		settingFilePath = settingFilePath+"WEB-INF/classes/"+"settings.xml";
		return settingFilePath;
	}
	
	/**
	 * ant deploy
	 * @param request
	 * @param repository
	 * @param tVersionArr
	 * @param tarSrcRtpath
	 * @param bNum
	 * @param refVer
	 * @param serverInfo
	 * @return
	 * @throws Exception
	 */
	private boolean antDeployProcess(HttpServletRequest request,  SVNRepository repository, List tVersionArr , String tarSrcRtpath, String bNum, String refVer, ResultMap serverInfo) throws Exception{
		boolean isComplete = false;
        DefaultLogger consoleLogger = getConsoleLogger();
        
        Project project = new Project();

        // Capture event for Ant script build start / stop / failure
        try {
        	//System.out.println("==============test====================");
			String projectId = serverInfo.getString("projectId");
			String projectVersion = serverInfo.getString("projectVersion");
			String webRootPath = serverInfo.getString("deployPath");
			String projectNewVersion = serverInfo.getString("projectVersion")+"."+bNum; //bNum를 합해서 projectNewVersion을 만든다.
			String buildRefFilepath = serverInfo.get("buildRefFile1").toString();
			String buildType = serverInfo.getString("buildType");
			if("".equals(buildRefFilepath)){
				logger.error("Build Ref File not exists!!!!");
				return isComplete;
			}
			//ant 빌드를 이용하여 파일을 복사한다. - maven 은 넘 어려워...
			String rootPath = tarSrcRtpath+"/deploy/buildsrc/"+bNum;
			String buildRefNm = tarSrcRtpath+"/project_init/"+buildRefFilepath;
			
			/* 배포정보 가져오기 start*/
			String sNum = cmUtils.getSessionSNum(request);
			HashMap<String,Object> param = new HashMap<String,Object>();
			param.put("sNum",sNum);			
			param.put("bNum",bNum);
			ResultMap deployInfo = deployMngService.selectDeployInfo(param);
			/* 배포정보 가져오기 end*/
			
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.debug("++ DeployJOA Start Deploy......");
			logger.debug("++ Deploy No : "+bNum);
			logger.debug("++ Deploy Title : "+ deployInfo.get("depoNm"));
			logger.debug("++ Deploy Type : "+ deployInfo.get("depoType"));
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			//ExStringUtils.defaultIfNull(request.getParameter("bno"), "");
//System.out.println("==deployInfo=="+deployInfo.toString());			
			if("B".equals(ExStringUtils.defaultIfNull(deployInfo.get("depoType"),""))){
				String[] vers = ExStringUtils.defaultIfNull(deployInfo.get("depoVers"),"").split("[,]");
				if(vers.length > 1){
					long startRevision = Long.parseLong(vers[0]);
					long endRevision = Long.parseLong(vers[1]);
//System.out.println("=startRevision="+startRevision);					
//System.out.println("=endRevision="+endRevision);
					new ExSVNFiles(repository).getSecFiles(rootPath, startRevision, endRevision, buildType);
				}
			}else{
				new ExSVNFiles(repository).getFiles(rootPath, tVersionArr, buildType);	
			}
			
			//구간배포일경우 로직 추가
			//getSecFiles
			logger.debug("getting SVN files end +++++++++");
			
//	        File buildFile = new File(""+tarSrcRtpath+"/deploy/"+projectId+"_DEPLOY_build.xml");
			String webrootRealPath = "";
			File buildFile = null;
//			File buildFile = new File(""+tarSrcRtpath+"/project_init/"+projectId+"_build.xml");
			
			if(!"N".equals(buildType)){ //compile 수행
				File webappDir = new File(rootPath+"/"+getWebrootPath(buildRefNm));

				System.out.println("rootPath=="+rootPath);
				if(!webappDir.exists()){
					webappDir.mkdirs();
				}	
				
				webrootRealPath = getBuildFilePath(request,"build");
				buildFile = new File(""+webrootRealPath);
				
		        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
		        project.addBuildListener(consoleLogger);
				
	            project.fireBuildStarted();
	            project.init();
	            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
	            project.addReference("ant.projectHelper", projectHelper);
	            project.setProperty("deploy.webroot", webRootPath);
	            project.setProperty("deploy.basedir", tarSrcRtpath);
	            project.setProperty("deploy.webrootnm", projectId);
	            project.setProperty("deploy.deployno", bNum);
	            project.setProperty("deploy.artifactId", projectId);
	            project.setProperty("deploy.version", projectVersion);
	            project.setProperty("deploy.init_jarfile", projectId+"-"+projectNewVersion);
	            project.setProperty("deploy.buildRefFilePath", tarSrcRtpath+"/project_init/"+buildRefFilepath);
	            projectHelper.parse(project, buildFile);
	            
	            String targetToExecute = "versionDeploy";
	            project.executeTarget(targetToExecute);
	            project.fireBuildFinished(null);
			}else{ //compile 없이 수행
				webrootRealPath = getBuildFilePath(request,"nonjava");
				buildFile = new File(""+webrootRealPath);
				
		        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
		        project.addBuildListener(consoleLogger);
		        
	            project.fireBuildStarted();
	            project.init();
	            
	            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
	            project.addReference("ant.projectHelper", projectHelper);
	            project.setProperty("deploy.webroot", webRootPath);	          
	            project.setProperty("deploy.deployno", bNum);
	            project.setProperty("deploy.basedir", tarSrcRtpath);
	            
	            project.setProperty("deploy.buildRefFilePath", tarSrcRtpath+"/project_init/"+buildRefFilepath);
	            projectHelper.parse(project, buildFile);
	            
	            String targetToExecute = "makeNonJavaZip";
	            project.executeTarget(targetToExecute);
	            project.fireBuildFinished(null);	            
			}
            isComplete = true;
            
        } catch (BuildException buildException) {
        	buildException.printStackTrace();
            project.fireBuildFinished(buildException);
            throw new RuntimeException("!!! DeployJOA Build Error !!!", buildException);
        }		
		
		return isComplete;
	}
	
	//zip jar make ant
	private boolean antInitDeploy(HttpServletRequest request,ResultMap serverInfo, String bNum) throws Exception{
		boolean isComplete = false;
		
        DefaultLogger consoleLogger = getConsoleLogger();		
        Project project = new Project();
        
        try{
			String projectId = serverInfo.getString("projectId");
			String projectVersion = serverInfo.getString("projectVersion")+"."+bNum;
			String tarSrcRtpath = serverInfo.get("tarSrcRtpath").toString();
			String webRootPath = serverInfo.getString("deployPath");
//			String targetRtPath = tarSrcRtpath+DEPLOY_INIT_PATH;
//			String buildType = serverInfo.getString("buildType");
//			String lastBNum = serverInfo.getString("lastBNum");
			String buildInitPath = getBuildFilePath(request,"init");
			
	        File buildFile = new File(""+buildInitPath);
	        
	        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
	        project.addBuildListener(consoleLogger);
	        
            project.fireBuildStarted();
            project.init();
            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            project.addReference("ant.projectHelper", projectHelper);
            project.setProperty("deploy.basedir", tarSrcRtpath);
            project.setProperty("deploy.webroot", webRootPath);
            project.setProperty("deploy.deployno", bNum);
            project.setProperty("deploy.init_jarfile", projectId+"-"+projectVersion);
            
            projectHelper.parse(project, buildFile);
            
            String targetToExecute = "makeDeployFils";
            project.executeTarget(targetToExecute);
            project.fireBuildFinished(null);
            isComplete = true;			
        	
        } catch (BuildException buildException) {
            project.fireBuildFinished(buildException);
            throw new RuntimeException("!!! DeployJOA Build Error !!!", buildException);
        }	
        
		return isComplete;		
	}
	
	/**
	 * build file path
	 * @param request
	 * @param gbn
	 * @return
	 * @throws Exception
	 */
	private String getBuildFilePath(HttpServletRequest request, String gbn) throws Exception{
		String webrootRealPath = request.getSession().getServletContext().getRealPath("/");
		if(gbn.equals("nonjava")){
			webrootRealPath = webrootRealPath+"/WEB-INF/classes/"+"deployJOA_nonjavazip_build.xml";
		}else if(gbn.equals("build")){
			webrootRealPath = webrootRealPath+"/WEB-INF/classes/"+"deployJOA_build.xml";
		}else if(gbn.equals("maven-build")){
				webrootRealPath = webrootRealPath+"/WEB-INF/classes/"+"deployJOA_maven_build.xml";			
		}else{
			webrootRealPath = webrootRealPath+"/WEB-INF/classes/"+"deployJOA_INIT_build.xml";
		}
		
		return webrootRealPath;
	}
	
	/**
	 * get webroot property from build ref file user modified 
	 * @param buildFileRealPath
	 * @return
	 * @throws Exception
	 */
	private String getWebrootPath(String buildFileRealPath) throws Exception{
		
		String webroot = "";
		DomXMLParser xmlParser = new DomXMLParser();
		HashMap<String,String> buildPropMap = xmlParser.getBuildProperties(buildFileRealPath);
		
		if(buildPropMap.containsKey("webroot")){
			webroot = buildPropMap.get("webroot"); 
		}
		
		return webroot;
	}
	
	/**
	 * ant build log print
	 * @return
	 */
    private DefaultLogger getConsoleLogger() {
        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
        
        return consoleLogger;
    }
	
    
	/**
	 * rollback 수행
	 * @param param
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	/*
	@RequestMapping("/deploymng/doRollback.do")
	public View doRollback(@RequestParam HashMap<String,Object> param
			            , Model model
			            , HttpServletRequest request
            			, HttpServletResponse response) throws Exception{
		
		boolean rollbackSuccessed = false;
		
		String tarSrcRtpath = "";
		String refVer = "1.0.0";
		
		try{
			ResultMap serverInfo = targetServMngService.selectServerInfo(param);
			
			tarSrcRtpath = serverInfo.get("tarSrcRtpath").toString();
			
			// log file 생성
			String logfileNm = tarSrcRtpath+"/logs/deploy_log_"+param.get("bNum").toString()+".log";
			System.setProperty("logFilename", logfileNm);
		    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		    ctx.reconfigure();	
		    // log file 생성

		    rollbackSuccessed = antRollBackProcess(tarSrcRtpath, param.get("bNum").toString(), refVer, serverInfo);
			
			if(rollbackSuccessed){
				//last bNum를 배포버전번호로 update를 수행
				//rollback이전 배포된 것중 가장 최근것
				ResultMap rmap = deployMngService.selectLastBNumByRollback(param);
				param.put("lastBNum", rmap.get("bNum").toString());
				targetServMngService.updateLastBNum(param);
			}
			
			if(rollbackSuccessed){
				model.addAttribute("result", "001");
				deployMngService.updateDoneRollback(param);
			}else{
				model.addAttribute("result", "900");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("result", "900");
			logger.error(e.getMessage());
		}
		
		return new JsonView();
	}
	*/    
	/**
	 * Rollback 하기
	 * @param tarSrcRtpath
	 * @param bNum
	 * @param refVer
	 * @param serverInfo
	 * @return
	 * @throws Exception
	 */
    /*
	private boolean antRollBackProcess(String tarSrcRtpath, String bNum, String refVer, ResultMap serverInfo) throws Exception{
		boolean isComplete = false;
		String antCommandResult = null;
		try{

			String projectId = serverInfo.getString("projectId");
			String webRootPath = serverInfo.getString("deployPath");

			//ant 빌드를 이용하여 파일을 복사한다. - maven 은 넘 어려워...
			
			logger.info("+++ rollback start +++++++++");
			
			List<String> antcmd = new ArrayList<String>();
			if(cmUtils.isWindows()){
				antcmd.add("cmd");
				antcmd.add("/c");
			}
			antcmd.add("ant");
			antcmd.add("-buildfile");
			antcmd.add(""+tarSrcRtpath+"/deploy/rollback.xml");
			antcmd.add("-Ddeploy.webroot="+webRootPath);
			antcmd.add("-Ddeploy.basedir="+tarSrcRtpath);
			antcmd.add("-Ddeploy.webrootnm="+projectId);
			antcmd.add("-Ddeploy.deployno="+bNum);
			antCommandResult = cmUtils.antCommand(antcmd, logger);
			
//System.out.println("ant -buildfile "+tarSrcRtpath+"/deploy/rollback.xml -Ddeploy.webroot="+webRootPath+" -Ddeploy.basedir="+tarSrcRtpath+" -Ddeploy.webrootnm="+projectId+" -Ddeploy.deployno="+bNum);			
//			logger.info(antCommandResult);
			
			if(antCommandResult.indexOf("BUILD SUCCESSFUL") >= 0){
				isComplete = true;
			}else{
				logger.error("======== rollback error =====");
			}
			
			
		}catch(UserDefineBuildException ue){
			isComplete = false;
			logger.error("========rollback err=====");
			logger.error(ue.getMessage());			
		}catch(Exception e){
			isComplete = false;
			logger.error("========rollback err=====");
			logger.error(e.getMessage());
		}
		
		return isComplete;				
	}
	*/
	
	@RequestMapping("/deploymng/down_deployfile.do")
	public void getFileDownload(HttpServletRequest request
							, HttpServletResponse response) throws Exception {
		
			HashMap<String,Object> param = new HashMap<String,Object>();
	        File file = null;
	        FileInputStream fis = null;
	        OutputStream os = null;
	        
	        try{

	    		String sNum = cmUtils.getSessionSNum(request);
	    		param.put("sNum",sNum);
	    		
	    		ResultMap serverInfo = targetServMngService.selectServerInfo(param);
	    		
	    		String bNo = ExStringUtils.defaultIfNull(request.getParameter("bno"), "");
	    		
	    		if(!"".equals(bNo)){
	    			
		        	String fileNm = serverInfo.getString("deployPath")+"/DeployJOA_files_"+bNo+".zip";
//System.out.println("fileNm=="+fileNm);
		        	file = new File(fileNm);
		        	if(file.exists()){
		        	
			            response.setContentType("application/octet-stream");
			            response.setContentLength((int) file.length());
			            
			            response.setHeader("Content-Disposition", "attachment; filename=\"" + "DeployJOA_files_"+bNo+".zip" + "\"");
			            
			            fis = new FileInputStream(file);
			            byte[] buff = new byte[fis.available()];
			            fis.read(buff);
			            
			            os = response.getOutputStream();
			            os.write(buff);
			            
			            os.flush();
		        	}else{
		        		//파일이 존재하지 않습니다.
		        		new ApplicationAlertException().ApplicationAlertException(response, "deploy file not exists!!!");
		        	}
	    		}else{
	    			new ApplicationAlertException().ApplicationAlertException(response, "request is not corrent!!!");
	    		}
	        	
	        } finally {
	            if (fis != null) {
	                fis.close();
	            }
	            if (os != null) {
	                os.close();
	            }
	        }		
	}
	

	/**
	 * 이전 commnd 로 ant 실행
	 * @param repository
	 * @param tVersionArr
	 * @param tarSrcRtpath
	 * @param bNum
	 * @param refVer
	 * @param serverInfo
	 * @return
	 * @throws Exception
	 */
	private boolean oldAntDeployProcess(SVNRepository repository, List tVersionArr , String tarSrcRtpath, String bNum, String refVer, ResultMap serverInfo) throws Exception{
		boolean isComplete = false;
		String antCommandResult = null;
		try{
//System.out.println("==============test====================");
			String projectId = serverInfo.getString("projectId");
			String projectVersion = serverInfo.getString("projectVersion");
			String webRootPath = serverInfo.getString("deployPath");
			String projectNewVersion = serverInfo.getString("projectVersion")+"."+bNum; //bNum를 합해서 projectNewVersion을 만든다.

			//ant 빌드를 이용하여 파일을 복사한다. - maven 은 넘 어려워...
			String rootPath = tarSrcRtpath+"/deploy/buildsrc/"+bNum;

			System.out.println("getting SVN files start +++++++++");
			new ExSVNFiles(repository).getFiles(rootPath, tVersionArr);
			System.out.println("getting SVN files end +++++++++");
			
			File webappDir = new File(rootPath+"/src/main/webapp");

			System.out.println("rootPath=="+rootPath);
			if(!webappDir.exists()){
				webappDir.mkdirs();
			}			
			
			logger.info("ant deploy file +++++++++");
//System.out.println("==============test1====================");			
			List<String> antcmd = new ArrayList<String>();
			if(cmUtils.isWindows()){
				antcmd.add("cmd");
				antcmd.add("/c");
			}
			antcmd.add("ant");
			antcmd.add("-buildfile");
			antcmd.add(""+tarSrcRtpath+"/deploy/"+projectId+"_DEPLOY_build.xml");
			antcmd.add("-Ddeploy.webroot="+webRootPath); //deployRealPath
			antcmd.add("-Ddeploy.basedir="+tarSrcRtpath);
			antcmd.add("-Ddeploy.webrootnm="+projectId);
			antcmd.add("-Ddeploy.deployno="+bNum);
			antcmd.add("-Ddeploy.artifactId="+projectId);
			antcmd.add("-Ddeploy.version="+projectVersion);
			antcmd.add("-Ddeploy.init_jarfile="+projectId+"-"+projectNewVersion);
			antCommandResult = cmUtils.antCommand(antcmd, logger);
			
System.out.println("ant -buildfile "+tarSrcRtpath+"/deploy/"+projectId+"_DEPLOY_build.xml "+"-Ddeploy.webroot="+webRootPath+" -Ddeploy.basedir="+tarSrcRtpath+" -Ddeploy.webrootnm="+projectId
		+" -Ddeploy.deployno="+bNum+" -Ddeploy.artifactId="+projectId+" -Ddeploy.version="+projectVersion+" -Ddeploy.init_jarfile="+projectId+"-"+projectNewVersion);
			
			logger.info(antCommandResult);
			
			if(antCommandResult.indexOf("BUILD SUCCESSFUL") >= 0){
				isComplete = true;
			}else{
				logger.error("======== ant deploy error =====");
			}
			
			
		}catch(UserDefineBuildException ue){
			isComplete = false;
			logger.error("========build err=====");
			logger.error(ue.getMessage());			
		}catch(Exception e){
			isComplete = false;
			logger.error("========build err=====");
			logger.error(e.getMessage());
		}
		
		return isComplete;		
	}	
}
