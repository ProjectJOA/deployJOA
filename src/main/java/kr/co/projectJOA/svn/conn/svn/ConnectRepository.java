/**
 * Project Name : DeployJOAPro
 * File Name : ConnectRepository.java
 * Author : cho
 * Description : 
 *
 *
 * ========================================================
 * Create Date : 2012. 11. 9.
 * ========================================================
 */
package kr.co.projectJOA.svn.conn.svn;

import java.io.File;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @author cho
 *
 */
public class ConnectRepository {

	
	public static SVNRepository createConnectionRepository() throws Exception {
		
		String url  = "https://127.0.0.1:8080/svn/test/test_admin";
		String name = "test_cch";
		String password = "test";

        return createConnectionRepository(url, name, password);
	}
	
	public static SVNRepository createConnectionRepository(String svnUrl, String id, String pwd) throws Exception {
		SVNRepository repository = null;
		SVNURL url = SVNURL.parseURIDecoded( svnUrl );
		String name = id;
		String password = pwd;
					
//		SVNRepository repository = SVNRepositoryFactory.create( url, null );
		repository = SVNRepositoryFactory.create( url);
		
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( name , password );
		
		repository.setAuthenticationManager( authManager );
		
        return repository;		
	}
	
}
