/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.targetServMng.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.projectJOA.com.constant.GlobalConstant;
import kr.co.projectJOA.com.exception.ApplicationAlertException;
import kr.co.projectJOA.com.util.DomXMLParser;
import kr.co.projectJOA.com.util.ExStringUtils;
import kr.co.projectJOA.com.util.FileUtil;
import kr.co.projectJOA.com.util.ResultMap;
import kr.co.projectJOA.com.view.JsonView;
import kr.co.projectJOA.com.zdeployJOA.base.web.BaseController;
import kr.co.projectJOA.com.zdeployJOA.cmm.cmUtils;
import kr.co.projectJOA.com.zdeployJOA.sysconfig.service.SystemConfigService;
import kr.co.projectJOA.com.zdeployJOA.targetServMng.service.TargetServMngService;
import kr.co.projectJOA.svn.conn.svn.SVNConnectHandler;
import kr.co.projectJOA.svn.export.ExSVNFiles;

import org.apache.maven.cli.MavenCli;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;
import org.tmatesoft.svn.core.io.SVNRepository;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @author chcho
 *
 */
@Controller
public class TargetServMngController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(TargetServMngController.class);

	@Autowired
	private TargetServMngService targetServMngService;
	
	@Autowired
	private SystemConfigService systemConfigService;
	
	private String DEPLOY_INIT_PATH = "/project_init";
	
	@RequestMapping("/servmng/targetser_list.do")
	public String getTargetServMngList( @RequestParam HashMap<String,Object> param
             , ModelMap model
             , HttpServletRequest request
             , HttpServletResponse response) throws Exception {
		
		
//		targetServMngService.insertServerMngInfo(param);

		PaginationInfo paginationInfo = new PaginationInfo();
		super.initPaginationInfo(paginationInfo, param, model);
		
		List<ResultMap> list = targetServMngService.selectServerMngList(param);
		int totalCnt = targetServMngService.selectServerMngListCount(param);
		
		paginationInfo.setTotalRecordCount(totalCnt);
		
		int patchCount = 0;
		if(GlobalConstant.IS_ABLE_PATCHPROCESS){
			patchCount = systemConfigService.getPatchHistCount(param);
		}
		
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("param", param);
		model.addAttribute("servLst", list);
		
		model.addAttribute("patchCount", patchCount);
		
		return "deploy/servermng/server_list";
		
	}
	
	@RequestMapping("/servmng/targetser_editor.do")
	public String registerForm(@RequestParam HashMap<String,Object> param
             , ModelMap model
             , HttpServletRequest request
             , HttpServletResponse response) throws Exception {
		
		
		ResultMap serverInfo = targetServMngService.selectServerInfo(param);
		
		model.addAttribute("serverInfo", serverInfo);
		model.addAttribute("param", param);
		model.addAttribute("deployInitPath", DEPLOY_INIT_PATH);
		
		return "deploy/servermng/server_register";
	}
	
	/**
	 * build sample file download
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/servmng/down_samplefile.do")
	public void getSamplefile(HttpServletRequest request
							, HttpServletResponse response) throws Exception {
		
			HashMap<String,Object> param = new HashMap<String,Object>();
	        File file = null;
	        FileInputStream fis = null;
	        OutputStream os = null;
	        
	        try{
	        	String fileNm = ""+getBuildFilePath(request,"sample");
	        	
	        	file = new File(fileNm);
	        	if(file.exists()){
	        	
		            response.setContentType("application/octet-stream");
		            response.setContentLength((int) file.length());
		            
		            response.setHeader("Content-Disposition", "attachment; filename=\"" + "deployJOA_sample_property.xml" + "\"");
		            
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
	        	
	        } finally {
	            if (fis != null) {
	                fis.close();
	            }
	            if (os != null) {
	                os.close();
	            }
	        }		
	}
	
	@RequestMapping("/servmng/targetser_save.do")
	public String saveServerMngInfo(@RequestParam HashMap<String,Object> param
            , ModelMap model
            , MultipartHttpServletRequest request 
            , HttpServletResponse response) throws Exception {
		//tarSrcRtpath
		//tarBldRtpath
		//deployPath
		
		String tarSrcRtpath = param.get("tarSrcRtpath").toString()+DEPLOY_INIT_PATH;
//		String tarBldRtpath = param.get("tarBldRtpath").toString();
		String deployPath = param.get("deployPath").toString();
		String mavenRepo = param.get("tarSrcRtpath").toString()+"/maven/repository";
		
		FileUtil.makeDirs(tarSrcRtpath);
//		FileUtil.makeDirs(tarBldRtpath);
		FileUtil.makeDirs(deployPath);
		FileUtil.makeDirs(mavenRepo);
		
		String uploadFileNm = "";
		List<MultipartFile> uploadFiles = request.getFiles("attach_file");
		if(uploadFiles.size() > 0){
			uploadFileNm = uploadRefFile(param.get("tarSrcRtpath").toString()+DEPLOY_INIT_PATH ,uploadFiles);
			if(!"".equals(uploadFileNm)){
				param.put("buildRefFile1", uploadFileNm);
			}
		}
		
		String sNum = ExStringUtils.defaultIfNull( param.get("sNum"), "");
		if("".equals(sNum)){
			targetServMngService.insertServerMngInfo(param);	
		}else{
			targetServMngService.updateServerMngInfo(param);
		}
		
		return "redirect:/servmng/targetser_list.do";
		
	}
	
	/**
	 * system activate.
	 * @param param
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servmng/ci_init.do")
	public View doDeploy(@RequestParam HashMap<String,Object> param
						, ModelMap model
						, HttpServletRequest request
            			, HttpServletResponse response) throws Exception{
		
		SVNRepository repository = null;
		String projectId = "";
		String projectVersion = "1.0.0";
		String lastBNum = "";
		String buildType = "M";
		String antCommandResult = "";
		boolean isCompiled = false;
		try{
			
			SVNConnectHandler.setInitRepository("");
			
			ResultMap serverInfo = targetServMngService.selectServerInfo(param);	
			projectId = serverInfo.getString("projectId");
			lastBNum = serverInfo.getString("lastBNum");
			buildType = serverInfo.getString("buildType");
			projectVersion = serverInfo.getString("projectVersion")+"."+lastBNum;
			
			SVNConnectHandler.setRepository((String)serverInfo.get("reposUrl"), (String)serverInfo.get("verContUserId"), (String)serverInfo.get("verContPwd"));
			
			repository = SVNConnectHandler.getRepository("");
			String tarSrcRtpath = serverInfo.get("tarSrcRtpath").toString();
			String targetRtPath = tarSrcRtpath+DEPLOY_INIT_PATH;
		
			new ExSVNFiles(repository).getLastestVersionAllFiles(targetRtPath);
			
			model.addAttribute("result","001");
			
			int cnt = targetServMngService.updateCISeverInit(param);
			if(cnt == 0 ){
				model.addAttribute("result","801");
			}
			
			/**********************************/
			try{
				
				if("M".equals(buildType)){
					
					logger.info("+++++++++++++++++++ maven clean install jar start +++++++++++++++++++");
					
					String settingXmlPath = getGlobalMavenSettingFile(request);
					String outDir = tarSrcRtpath+"/project_init/";
					String localMavenRepo = tarSrcRtpath+"/maven/repository";
					
					String[] args = new String[]{ "-Dbuilddir="+tarSrcRtpath+"/project_init/target"
													,"-Dsrcdir="+tarSrcRtpath+"/project_init/src"
													,"-DprojectId="+projectId
													,"-DprojectVersion="+projectVersion
													,"-f="+tarSrcRtpath+"/project_init/pom.xml"
													,"-s="+settingXmlPath
													,"clean"
													,"install"};
					
					if(!mavenInitBuild( args, outDir, targetRtPath, localMavenRepo)){
						
						model.addAttribute("result","901");
					}else{
//						antInitDeploy(serverInfo, request); //2017-10-13 수정 local repository에 등록하는 걸로 수정
						
						if(antInitDeploy(serverInfo, request)){ //jar 파일 만들고, jar를 local repository에 등록
							args = new String[]{ "install:install-file"
									,"-Dfile="+tarSrcRtpath+"/project_init/"+projectId+"-"+serverInfo.getString("projectVersion")+".jar"
									,"-DgroupId="+projectId
									,"-DartifactId="+projectId
									,"-DpomFile="+tarSrcRtpath+"/project_init/pom.xml"
									,"-Dversion="+serverInfo.getString("projectVersion")
									,"-Dpackaging=jar"};		
							
							if(!mavenInitBuild( args, outDir, targetRtPath, localMavenRepo)){
								model.addAttribute("result","901");
							}
						}else{
							model.addAttribute("result","901");
						}
					}
					
					logger.info("+++++++++++++++++++ maven clean install jar end +++++++++++++++++++");
					
				}else{ //ant build
					
					isCompiled = antTotalSrcBuildDeploy(serverInfo,request);
					if(isCompiled)
						antInitDeploy(serverInfo, request);
					
				}

				
			}catch(Exception e){
				e.printStackTrace();
				logger.error("make jar : "+e.getMessage());
			}
			/**********************************/
			
		}catch(Exception e){
			model.addAttribute("result","900");
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return new JsonView();
	}
	
	/**
	 * ant all sources build
	 * @param serverInfo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//init build
	private boolean antTotalSrcBuildDeploy(ResultMap serverInfo, HttpServletRequest request) throws Exception{
		boolean isComplete = false;
		
        DefaultLogger consoleLogger = getConsoleLogger();		
        Project project = new Project();
        
        try{
			String projectId = serverInfo.getString("projectId");
			String tarSrcRtpath = serverInfo.get("tarSrcRtpath").toString();
			String buildRefFilepath = serverInfo.get("buildRefFile1").toString();
			if("".equals(buildRefFilepath)){
				logger.error("Build Ref File not exists!!!!");
				return isComplete;
			}
			
			String webrootRealPath = getBuildFilePath(request,"build");
			
	        File buildFile = new File(""+webrootRealPath);			
	        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
	        project.setProperty("deploy.buildRefFilePath", tarSrcRtpath+"/project_init/"+buildRefFilepath);
	        project.addBuildListener(consoleLogger);
	        
            project.fireBuildStarted();
            project.init();
            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            project.addReference("ant.projectHelper", projectHelper);
            project.setProperty("deploy.basedir", tarSrcRtpath);
            
            projectHelper.parse(project, buildFile);
            
            String targetToExecute = "projectInit"; //projectInit
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
	 * make init jar file
	 * @param serverInfo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean antInitDeploy(ResultMap serverInfo, HttpServletRequest request) throws Exception{
		boolean isComplete = false;
		
        DefaultLogger consoleLogger = getConsoleLogger();		
        Project project = new Project();
        
        try{
			String projectId = serverInfo.getString("projectId");
			String lastBNum = serverInfo.getString("lastBNum");
			String projectVersion = serverInfo.getString("projectVersion")+"."+lastBNum;
			String tarSrcRtpath = serverInfo.get("tarSrcRtpath").toString();
//			String targetRtPath = tarSrcRtpath+DEPLOY_INIT_PATH;			
			String buildType = serverInfo.getString("buildType");			
			
			String buildInitPath = getBuildFilePath(request,"init");
			
	        File buildFile = new File(""+buildInitPath);			
	        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
	        project.addBuildListener(consoleLogger);
	        
            project.fireBuildStarted();
            project.init();
            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            project.addReference("ant.projectHelper", projectHelper);
            project.setProperty("deploy.basedir", tarSrcRtpath);
            if("M".equals(buildType)){
            	project.setProperty("deploy.init_jarfile", projectId+"-"+serverInfo.getString("projectVersion"));
            }else{
            	project.setProperty("deploy.init_jarfile", projectId+"-"+projectVersion);	
            }
            
            
            projectHelper.parse(project, buildFile);
            
            String targetToExecute = "makeInitJar";
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
	 * maven init build
	 * @param args
	 * @param outDir
	 * @param targetRtPath
	 * @param localMavenRepo
	 * @return
	 */
	private boolean mavenInitBuild(String[] args, String outDir, String targetRtPath, String localMavenRepo) throws Exception{
		
		boolean resultBoolean = false;
		int resultPointer = 1;
		
		MavenCli maven = new MavenCli();
		System.setProperty(maven.MULTIMODULE_PROJECT_DIRECTORY, targetRtPath);
		System.setProperty(maven.LOCAL_REPO_PROPERTY, localMavenRepo);
		
		resultPointer = maven.doMain(args, outDir, System.out, System.out);
		if(resultPointer == 0) resultBoolean = true;
		return resultBoolean;
	}
	
	private String uploadRefFile(String saveFilePath, List<MultipartFile> uploadFiles) throws Exception{
//System.out.println("uploadRefFile====");		
		String uploadFileNm = "";;
		try{
			for(int i=0;i<uploadFiles.size();i++){
				MultipartFile mf = (MultipartFile)uploadFiles.get(i);
//System.out.println("mf.getOriginalFilename()===="+mf.getOriginalFilename());				
				if(!"".equals(mf.getOriginalFilename())){
					
					makeFile(saveFilePath, mf);
					uploadFileNm = mf.getOriginalFilename();
				}
			}

		}catch(Exception e){
			uploadFileNm = "";
			logger.error(""+e.getMessage());
		}
		
		return uploadFileNm;
        
	}
	
	private boolean makeFile(String saveFilePath, MultipartFile mf) throws Exception {
		
		boolean isMkfile = true; 
		
		try{
	        String saveFileName = mf.getOriginalFilename();
//System.out.println("saveFileName==="+saveFileName);	
	        int fileSize = (int) mf.getSize();
	        
	        File filepathDir = new File(saveFilePath);
	        if(!filepathDir.exists()){
	        	filepathDir.mkdirs();
	        }
//System.out.println("saveFilePath==="+saveFilePath);	        
	        File file = new File(String.format("%s/%s", saveFilePath, saveFileName));
	        
	        if(!file.exists()){
	            file.mkdirs();
	        }else{
	        	file.deleteOnExit();
	        	file.mkdirs();
	        }
	        
	        mf.transferTo(file);

		}catch(Exception e){
			isMkfile = false;
			logger.error(""+e.getMessage());
		}
        
		return isMkfile;
	}
	
	
	private String getGlobalMavenSettingFile(HttpServletRequest request) throws Exception{
		String webrootRealPath = request.getSession().getServletContext().getRealPath("/");
		String settingFilePath = webrootRealPath.replaceAll("\\\\", "/");
		settingFilePath = settingFilePath+"WEB-INF/classes/"+"settings.xml";
		return settingFilePath;
	}
	
	/**
	 * build file path
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private String getBuildFilePath(HttpServletRequest request, String gbn) throws Exception{
		String webrootRealPath = request.getSession().getServletContext().getRealPath("/");
			   webrootRealPath = webrootRealPath.replaceAll("\\\\", "/");
		if(gbn.equals("build")){
			webrootRealPath = webrootRealPath+"WEB-INF/classes/"+"deployJOA_build.xml";
		}else if(gbn.equals("sample")){
			webrootRealPath = webrootRealPath+"WEB-INF/classes/"+"deployJOA_sample_custom_build.xml";
		}else{
			webrootRealPath = webrootRealPath+"WEB-INF/classes/"+"deployJOA_INIT_build.xml";
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
	
    private DefaultLogger getConsoleLogger() {
        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
        
        return consoleLogger;
    }	
    
    /*
	@RequestMapping("/servmng/ci_init.do")
	public View doDeploy(@RequestParam HashMap<String,Object> param
						, ModelMap model
						, HttpServletRequest request
            			, HttpServletResponse response) throws Exception{
		
		SVNRepository repository = null;
		String projectId = "";
		String projectVersion = "1.0.0";
		String lastBNum = "";
		String buildType = "M";
		String antCommandResult = "";
		boolean isCompiled = false;
		try{
			
			SVNConnectHandler.setInitRepository("");
			
			ResultMap serverInfo = targetServMngService.selectServerInfo(param);	
			projectId = serverInfo.getString("projectId");
			lastBNum = serverInfo.getString("lastBNum");
			buildType = serverInfo.getString("buildType");
			projectVersion = serverInfo.getString("projectVersion")+"."+lastBNum;
			
			SVNConnectHandler.setRepository((String)serverInfo.get("reposUrl"), (String)serverInfo.get("verContUserId"), (String)serverInfo.get("verContPwd"));
			
			repository = SVNConnectHandler.getRepository("");
			String tarSrcRtpath = serverInfo.get("tarSrcRtpath").toString();
			String targetRtPath = tarSrcRtpath+DEPLOY_INIT_PATH;
		
			new ExSVNFiles(repository).getLastestVersionAllFiles(targetRtPath);
			
			model.addAttribute("result","001");
			
			int cnt = targetServMngService.updateCISeverInit(param);
			if(cnt == 0 ){
				model.addAttribute("result","801");
			}
			
			try{
				
				if("M".equals(buildType)){
				
					List<String> cmd1 = new ArrayList<String>();
					if(cmUtils.isWindows()){
						cmd1.add("cmd");
						cmd1.add("/c");
					}
					cmd1.add("mvn");
					cmd1.add("-Dbuilddir="+tarSrcRtpath+"/project_init/target");
					cmd1.add("-Dsrcdir="+tarSrcRtpath+"/project_init/src");
	//				cmd1.add("-DprojectId="+projectId);
	//				cmd1.add("-DprojectVersion="+projectVersion);
					cmd1.add("-f");
					cmd1.add(""+tarSrcRtpath+"/project_init/pom.xml");
					cmd1.add("-s");
					cmd1.add(""+tarSrcRtpath+"/project_init/settings.xml");
					cmd1.add("clean");
	logger.debug("ci_init cmd1 = "+cmd1.toString());				
					cmUtils.mavenBuilder(cmd1);
						
					List<String> cmd2 = new ArrayList<String>();
					if(cmUtils.isWindows()){
						cmd2.add("cmd");
						cmd2.add("/c");
					}
					cmd2.add("mvn");
					cmd2.add("-Dbuilddir="+tarSrcRtpath+"/project_init/target");
					cmd2.add("-Dsrcdir="+tarSrcRtpath+"/project_init/src");
					cmd2.add("-f");
					cmd2.add(""+tarSrcRtpath+"/project_init/pom.xml");
					cmd2.add("-s");
					cmd2.add(""+tarSrcRtpath+"/project_init/settings.xml");
					cmd2.add("install");
	logger.debug("ci_init cmd2 = "+cmd2.toString());				
					cmUtils.mavenBuilder(cmd2);
				
					String jarfileNm = projectId+"-"+projectVersion+".jar";
					List<String> cmd3 = new ArrayList<String>();
					if(cmUtils.isWindows()){
						cmd3.add("cmd");
						cmd3.add("/c");
					}
					cmd3.add("jar");				
					cmd3.add("cvf");
					cmd3.add(""+tarSrcRtpath+"/project_init/"+jarfileNm);
					cmd3.add("*.class");
					cmd3.add("-C");
					cmd3.add(""+tarSrcRtpath+"/project_init/target/classes/");
					cmd3.add(".");
	logger.debug("ci_init cmd3 = "+cmd3.toString());				
					cmUtils.mavenBuilder(cmd3);
					
				}else{
					
					List<String> antcmd = new ArrayList<String>();
					if(cmUtils.isWindows()){
						antcmd.add("cmd");
						antcmd.add("/c");
					}
					antcmd.add("ant");
					antcmd.add("-buildfile");
					antcmd.add(tarSrcRtpath+"/project_init/"+projectId+"_build.xml");
					antcmd.add("-Dproject.basedir="+tarSrcRtpath+"/project_init");
					antCommandResult = cmUtils.antCommand(antcmd, logger);
					
System.out.println("ant -buildfile "+tarSrcRtpath+"/project_init/"+projectId+"_build.xml"+" -Dproject.basedir="+tarSrcRtpath+"/project_init");					
System.out.println("antcmd antCommandResult=="+antCommandResult);
					

					List<String> antcmd2 = new ArrayList<String>();
					if(cmUtils.isWindows()){
						antcmd2.add("cmd");
						antcmd2.add("/c");
					}
					antcmd2.add("ant");
					antcmd2.add("-buildfile");
					antcmd2.add(tarSrcRtpath+"/project_init/"+projectId+"_INIT_build.xml");
					antcmd2.add("-Ddeploy.basedir="+tarSrcRtpath);
					antcmd2.add("-Ddeploy.init_jarfile="+projectId+"-"+projectVersion);
					antCommandResult = cmUtils.antCommand(antcmd2, logger);
					
System.out.println("antcmd2 antCommandResult=="+antCommandResult);					
				}

				
			}catch(Exception e){
				e.printStackTrace();
				logger.error("make jar : "+e.getMessage());
			}

			
		}catch(Exception e){
			model.addAttribute("result","900");
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return new JsonView();
	}
	*/    
}
