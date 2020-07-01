/**
 * 
 */
package kr.co.projectJOA.svn.conn.svn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

/**
 * @author chcho
 *
 */
public class SVNConnectHandler {

	private static final Logger logger = LoggerFactory.getLogger(SVNConnectHandler.class);
	
	private static SVNRepository repository = null;
	private static String SVNROOT = "";
	
	public static SVNRepository getRepository(String serverId) throws Exception{
//		if(repository == null){
//			repository = ConnectRepository.createConnectionRepository();
//		}
		return repository;
	}
	
	public static void setRepository(String svnUrl, String id, String pwd) throws Exception{
		repository = ConnectRepository.createConnectionRepository(svnUrl,id,pwd);
	}	

	public static void setInitRepository(String serverId) throws Exception{
		repository = null;
	}

	public static String getSVNROOT() {
		return SVNROOT;
	}

	public static void setSVNROOT(String sVNROOT) {
		SVNROOT = sVNROOT;
	}
	
	
}
