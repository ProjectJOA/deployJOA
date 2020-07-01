/**
 * 
 */
package kr.co.projectJOA.svn.conn.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.projectJOA.com.util.DateUtil;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.io.SVNRepository;

/**
 * @author chcho
 *
 */
public class VersionControlFactory {

	private static Logger logger = LoggerFactory.getLogger(VersionControlFactory.class);
	
	public List getLoglist(SVNRepository repository,List versions) throws Exception{
		
		Collection logEntries = null;
		List retArr = new ArrayList();
		
		if(versions.size()>0){
			for(int i=0;i<versions.size();i++){
				int ver = (Integer)versions.get(i);

				System.out.println("=====ver=="+ver);
				
				logEntries = repository.log( new String[] { "" } , null , ver , ver , true , true );	
				for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
					SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
					
					StringBuffer result = new StringBuffer();
					
					Map myChangedPaths = logEntry.getChangedPaths();
					
					for (Iterator paths = myChangedPaths.values().iterator(); paths.hasNext();) {
					    result.append('\n');
					    SVNLogEntryPath path = (SVNLogEntryPath) paths.next();
					    
					    System.out.println("path==="+path.getPath());
					    
					    retArr.add(  (path.toString()).replace("/trunk/", "")  );
					}
					
				}
			}
		}
		
		
		return retArr;
	}	
	
	/**
	 * 구간 배포를 위한 시작, 끝 버전 정보 추출
	 * @param repository
	 * @param startRevision
	 * @param endRevision
	 * @return
	 * @throws Exception
	 */
	public List getSectionLoglist(SVNRepository repository,long startRevision, long endRevision) throws Exception{
		
		List versions = new ArrayList();
		versions.add(startRevision);
		versions.add(endRevision);
		
		List logLst = new ArrayList();
		HashMap<String,String> logMap = new HashMap<String,String>();
		
		Collection logEntries = null;

//System.out.println("===versions.size=="+versions.size());		
		if(versions.size()>0){
			
			for(int i=0;i<versions.size();i++ ) 
			{
		
				long tVersion = (long)versions.get(i);
				
				logEntries = repository.log( new String[] { "" } , null , tVersion , tVersion , true , true );				
				
				for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
					SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );

					logMap = new HashMap<String,String>();
					logMap.put("version", ""+logEntry.getRevision());
					logMap.put("comment", logEntry.getMessage());
					logMap.put("filecnt", ""+logEntry.getChangedPaths().size());
					logMap.put("author", logEntry.getAuthor());
					logMap.put("reg_date", DateUtil.convertDateToDateStr(logEntry.getDate()));
			
					logLst.add(logMap);					
				}
			}
		}
		
		return logLst;
	}	
	
	public List getLoglist(SVNRepository repository,long startRevision, long endRevision) throws Exception{
		
		Collection logEntries = null;
		Collection fileList = null;
		
		logEntries = repository.log( new String[] { "" } , null , startRevision , endRevision , true , true );

		List logLst = new ArrayList();
		HashMap<String,String> logMap = new HashMap<String,String>();
		
		int i=0;
		for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
			
			SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
			
			logMap = new HashMap<String,String>();
			logMap.put("version", ""+logEntry.getRevision());
			logMap.put("comment", logEntry.getMessage());
			logMap.put("filecnt", ""+logEntry.getChangedPaths().size());
			logMap.put("author", logEntry.getAuthor());
			logMap.put("reg_date", DateUtil.convertDateToDateStr(logEntry.getDate()));
//System.out.println("===logMap=="+logMap.toString());			
			logLst.add(logMap);
			i++;
			if(i > 24){
				break;
			}
		}
		
//		Collections.reverse(logLst);
		
		return logLst;
	}	
	
	public List getVersionFileList(SVNRepository repository,List versions) throws Exception{
		
		Collection logEntries = null;
		List fileList = new ArrayList();
		
		HashMap<String,String> logMap = new HashMap<String,String>();
//System.out.println("===versions.size=="+versions.size());		
		if(versions.size()>0){
			
			for(int i=0;i<versions.size();i++ ) 
			{
		
				long tVersion = (long)versions.get(i);
				
				logEntries = repository.log( new String[] { "" } , null , tVersion , tVersion , true , true );				
				
				for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
					SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
					
					Map myChangedPaths = logEntry.getChangedPaths();
//System.out.println("====myChangedPaths==="+myChangedPaths.toString());			
					for (Iterator paths = myChangedPaths.values().iterator(); paths.hasNext();) {
		
					    SVNLogEntryPath path = (SVNLogEntryPath) paths.next();
					    
					    logMap = new HashMap<String,String>();
					    logMap.put("filePath", path.getPath());
					    logMap.put("fileKind", path.getKind().toString());
					    logMap.put("fileType", ""+path.getType());
					    logMap.put("copyPath", path.getCopyPath());
					    logMap.put("copyRevision", ""+path.getCopyRevision());
//System.out.println("get logMap="+logMap.toString());					    
					    if(!"D".equals(path.getType())){
					    	fileList.add(logMap);
					    }
					} //end 3rd for
				} //end 2nd for
			} // end 1st for
		} // end if
		
		logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		logger.debug("++ Versions Count : "+logEntries.size());
		logger.debug("++ File Count : "+fileList.size());
		logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");		
		return fileList;		
	}
	
	public List getVersionFileList(SVNRepository repository,long targetRevision) throws Exception{

		Collection logEntries = null;
		List fileList = new ArrayList();
		
		logEntries = repository.log( new String[] { "" } , null , targetRevision , targetRevision , true , true );
		
		HashMap<String,String> logMap = new HashMap<String,String>();
		
		
//		System.out.println("======root====="+repository.getRepositoryRoot().getPath());
		
		for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
			SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
			
			Map myChangedPaths = logEntry.getChangedPaths();
//System.out.println("====myChangedPaths==="+myChangedPaths.toString());			
			for (Iterator paths = myChangedPaths.values().iterator(); paths.hasNext();) {

			    SVNLogEntryPath path = (SVNLogEntryPath) paths.next();
			    
			    logMap = new HashMap<String,String>();
			    logMap.put("filePath", path.getPath());
			    logMap.put("fileKind", path.getKind().toString());
			    logMap.put("fileType", ""+path.getType());
			    logMap.put("copyPath", path.getCopyPath());
			    logMap.put("copyRevision", ""+path.getCopyRevision());
			    
			    fileList.add(logMap);
			}
			
		}
		
		return fileList;
	}
	
	/**
	 * 구간배포일 경우 시작, 끝 버전
	 * @param repository
	 * @param startRevision
	 * @param endRevision
	 * @return
	 * @throws Exception
	 */
	public List getVersionFileList(SVNRepository repository,long startRevision,long endRevision) throws Exception{
		
		Collection logEntries = null;
		List fileList = new ArrayList();
		
		HashMap<String,String> logMap = new HashMap<String,String>();
				
		logEntries = repository.log( new String[] { "" } , null , startRevision , endRevision , true , true );				
		
		for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
			SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
			
//			if("CSMKT-SHKIM".equals(logEntry.getAuthor()) || "CSMKT-CHCHO".equals(logEntry.getAuthor())  ){
			
				Map myChangedPaths = logEntry.getChangedPaths();
		
				for (Iterator paths = myChangedPaths.values().iterator(); paths.hasNext();) {
	
				    SVNLogEntryPath path = (SVNLogEntryPath) paths.next();
				    
				    logMap = new HashMap<String,String>();
				    logMap.put("filePath", path.getPath());
				    logMap.put("fileKind", path.getKind().toString());
				    logMap.put("fileType", ""+path.getType());
				    logMap.put("copyPath", path.getCopyPath());
				    logMap.put("copyRevision", ""+path.getCopyRevision());
				    
				    if(!"D".equals(path.getType())){
				    	fileList.add(logMap);
				    }
				} //end 2nd for
//			} //if
		} //end 1st for
		
		logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		logger.debug("++ Versions Count : "+logEntries.size());
		logger.debug("++ File Count : "+fileList.size());
		logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		return fileList;		
	}	
}
