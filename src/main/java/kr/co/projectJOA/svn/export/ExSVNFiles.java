/**
 * 
 */
package kr.co.projectJOA.svn.export;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.projectJOA.com.util.ExStringUtils;
import kr.co.projectJOA.com.util.ResultMap;
import kr.co.projectJOA.com.zdeployJOA.deployMng.web.DeployMngController;
import kr.co.projectJOA.svn.conn.svn.VersionControlFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNFileCheckoutTarget;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;

/**
 * @author chcho
 *
 */
public class ExSVNFiles {

	private static Logger logger = LoggerFactory.getLogger(ExSVNFiles.class);
	
	private SVNRepository repository;
	
	public ExSVNFiles(SVNRepository repository){
		this.repository = repository;
	}
	
	public void getLastestVersionAllFiles(String rootPath) throws Exception{
		
		File exportDir = new File(rootPath);
		
    	long latestRevision = repository.getLatestRevision( );
//    	latestRevision = 164;
    	ISVNReporterBaton reporterBaton = new ExportReporterBaton( latestRevision );
    	ISVNEditor exportEditor = new ExportEditor( exportDir );

    	repository.update( latestRevision , null , true , reporterBaton , exportEditor );
//    	System.out.println( "Exported revision: " + latestRevision );		
	}
	
	public void getFiles(String rootPath, List tVersionArr) throws Exception{
		getFiles(rootPath, tVersionArr, "A");
	}
	public void getFiles(String rootPath, List tVersionArr, String buildType) throws Exception{ //buildType 추가
		try{
		
			long targetRevision = 113;

//			System.out.println("=========rootpath=="+repository.getRepositoryRoot().getPath());
			//개별파일 가져오기.
			List fileLst = new VersionControlFactory().getVersionFileList(repository, tVersionArr);
			makeSVNFiles(rootPath,fileLst,buildType);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("ExSVNFiles error : "+e.getMessage());
		}		
	}
	
	/**
	 * 구간배포일경우 시작번호, 끝 번호 기준으로 파일 조회
	 * @param rootPath
	 * @param startRevision
	 * @param endRevision
	 * @throws Exception
	 */
	public void getSecFiles(String rootPath, long startRevision, long endRevision) throws Exception{
		getSecFiles(rootPath, startRevision, endRevision, "A");
	}
	public void getSecFiles(String rootPath, long startRevision, long endRevision, String buildType) throws Exception{
		try{
			
			List fileLst = new VersionControlFactory().getVersionFileList(repository, startRevision, endRevision);
			makeSVNFiles(rootPath,fileLst,buildType);
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("ExSVNFiles error : "+e.getMessage());
		}		
	}
	
	public void getFiles(String rootPath, long version) throws Exception{
		
//		System.out.println("================111======================");
//		System.out.println("=========rootpath=="+repository.getRepositoryRoot().getPath());
//		System.out.println("=====rootPath=="+rootPath+"====version="+version);

		File exportDir = new File(rootPath);
		
		List logLst = new ArrayList();
		logLst = getLoglist( repository,version, version);
		
    	ISVNReporterBaton reporterBaton = new ExportReporterBaton( version );
    	ISVNEditor exportEditor = new ExportDeployPartialEditor( exportDir );
    	exportEditor.targetRevision(version);

    	repository.update( version , null , true , reporterBaton , exportEditor );
//    	System.out.println( "Exported revision: " + version );		
	}
	
	private void makeSVNFiles(String targetPath, List fileLst) throws Exception{
		makeSVNFiles(targetPath, fileLst, "A");
	}
	private void makeSVNFiles(String targetPath, List fileLst, String buildType) throws Exception{
		
        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		List ExFileList = new ArrayList(); //파일 오류로 인해 제외된 목록
		
        if(fileLst.size()>0){
        	for(int i=0;i<fileLst.size();i++){
        		HashMap<String,String> fMap = (HashMap<String,String>)fileLst.get(i);
        		if(!"D".equals(fMap.get("fileType"))){
        			
        			logger.debug((String)fMap.get("filePath"));
//        			System.out.println(fMap.toString());
        			
	        		FileOutputStream fos = null;
	        		baos = new ByteArrayOutputStream();
	        		
	        		try{
	        			
	        			String repoPath = fMap.get("filePath");
	        			String fileKind = fMap.get("fileKind");
	        			
	        			String dirPath = "";
	        			String filePath = "";
	        			
	        			if(!"N".equals(buildType)){ //compile 수행
		        			dirPath = getDirPathForMAVEN(fileKind, repoPath);
		        			filePath = getFileNameForMAVEN(fileKind, repoPath); //maven 파일 path는 /src/main 으로 시작하므로	
	        			}else{ //소스 파일
		        			dirPath = getDirPathForTxT(fileKind, repoPath);
		        			filePath = getFileNameForTxT(fileKind, repoPath); //maven 파일 path는 /src/main 으로 시작하므로			        			
	        			}
	        			
	        			if("file".equals(fileKind)  )
	        			{
	        				File dir = new File(targetPath+dirPath);
	        				dir.mkdirs();
	        				
	        				File outFile = new File(targetPath+filePath);
	//        				outFile.deleteOnExit();
	        				if(!outFile.exists()){
	        					outFile.createNewFile();
	        				}else{
	        					outFile.delete();
	        					outFile.createNewFile();
	        				}
	        				
		        			fos = new FileOutputStream (outFile);
		        			
		        			repository.getFile(repoPath, -1, fileProperties, baos);
		        			
		        			baos.writeTo(fos);
	        			}else{
	        				File dir = new File(targetPath+dirPath);
	        				if(!dir.exists())
	        					dir.mkdirs();        				
	        			}
	        			
	        			if(fos != null) try{ fos.close(); }catch(Exception e){};
	        			if(baos != null) try{ baos.close();  }catch(Exception e){};
	        		}catch(Exception e){
	        			//logger.error("Exception files : "+fMap.get("filePath"));
	        			ExFileList.add(fMap);
	        		}finally{
	        			if(fos != null) try{ fos.close();  }catch(Exception e){};
	        			if(baos != null) try{ baos.close();  }catch(Exception e){};
	        		}
        		}
        	}
        	
        	if(ExFileList.size() > 0){
        		logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        		logger.debug("++ Error file List");
        		logger.debug("++");
            	for(int i=0;i<ExFileList.size();i++){
            		HashMap<String,String> fMap = (HashMap<String,String>)ExFileList.get(i);
//            		logger.error("Exception files : "+fMap.get("filePath"));
            		logger.debug("++ [file Type : "+fMap.get("fileType")+"]"+fMap.get("filePath"));
            	}
            	logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        	}
        }		
	}
	
	/**
	 * Maven일경우 src/main 으로 시작하도록 filtering 함.
	 * @param fileKind
	 * @param filePath
	 * @return
	 */
	private String getDirPathForMAVEN(String fileKind, String filePath){
		
		String dirPath = "";
		boolean srcFolderStart = false;
		if("file".equals(fileKind)){
			String[] paths = filePath.split("[/]");
			if(paths.length > 0){
				for(int i=0;i<paths.length-1;i++){
					
					if("src".equals( paths[i])){
						srcFolderStart = true;
					}
					
					if(srcFolderStart){
						dirPath += "/";
						dirPath +=paths[i];
					}
				}
			}
		}else{
			dirPath = filePath;
		}
		
		return dirPath;
	}
	
	/**
	 * not java or no need java compile
	 * @param fileKind
	 * @param filePath
	 * @return
	 */
	private String getDirPathForTxT(String fileKind, String filePath){
		
		String dirPath = "";
		boolean srcFolderStart = false;
		if("file".equals(fileKind)){
			String[] paths = filePath.split("[/]");
			if(paths.length > 0){
				for(int i=0;i<paths.length-1;i++){
					dirPath += "/";
					dirPath +=paths[i];
				}
			}
		}else{
			dirPath = filePath;
		}
		
		return dirPath;
	}	
	
	/**
	 * Maven일경우 src/main 으로 시작하도록 filtering 함.
	 * @param fileKind
	 * @param filePath
	 * @return
	 */
	private String getFileNameForMAVEN(String fileKind, String filePath) {
		
		String srcKey = "/src/";
		String fileName = "";
		if(filePath.indexOf(srcKey)>0){
			fileName = filePath.substring(filePath.indexOf(srcKey));
		}else{
			fileName = filePath;
		}
		
		return fileName;
	}
	
	/**
	 * not java or no need java compile
	 * @param fileKind
	 * @param filePath
	 * @return
	 */
	private String getFileNameForTxT(String fileKind, String filePath) {
		return filePath;
	}	
	
	public List getLoglist(SVNRepository repository,long startRevision, long endRevision) throws Exception{
		
		Collection logEntries = null;
		Collection fileList = null;
		
		logEntries = repository.log( new String[] { "" } , null , startRevision , endRevision , true , true );

		List logLst = new ArrayList();
		
		for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
			SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
//System.out.println("====logEntry : "+logEntry.toString());
//System.out.println("====="+logEntry.getAuthor());
//System.out.println("====="+logEntry.getMessage());
//System.out.println("====="+logEntry.getDate());
//System.out.println("====="+logEntry.getRevision());
//System.out.println("====getChangedPaths===="+logEntry.getChangedPaths().getPath());
//SVNLogEntryPath path = logEntry.getChangedPaths();
			
			Map myChangedPaths = logEntry.getChangedPaths();
			for (Iterator paths = myChangedPaths.values().iterator(); paths.hasNext();) {
				
			    SVNLogEntryPath path = (SVNLogEntryPath) paths.next();
			    
			    String filename = path.getPath();
//System.out.println("========filename===="+filename);			    
			    logLst.add(filename);
			}
		}
		
		return logLst;
	}	
}
