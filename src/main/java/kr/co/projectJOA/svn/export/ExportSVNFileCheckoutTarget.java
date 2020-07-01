/**
 * 
 */
package kr.co.projectJOA.svn.export;

import java.io.OutputStream;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.io.ISVNFileCheckoutTarget;

/**
 * @author chcho
 *
 */
public class ExportSVNFileCheckoutTarget implements ISVNFileCheckoutTarget{

	@Override
	public void filePropertyChanged(String path, String name, SVNPropertyValue value) throws SVNException {
		// TODO Auto-generated method stub
//System.out.println("=========path===="+path+"=========name===="+name+"=========value===="+value);		
	}

	@Override
	public OutputStream getOutputStream(String path) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("=========1111===="+path);		
		return null;
	}

	
	
}
