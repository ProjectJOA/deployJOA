/**
 * 
 */
package kr.co.projectJOA.svn.export;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;

/**
 * @author chcho
 *
 */
public class ExportReporterBaton implements ISVNReporterBaton {

	private long exportRevision;

	public ExportReporterBaton( long revision ){
		exportRevision = revision;
	}
	
	public void report( ISVNReporter reporter ) throws SVNException {
		try {
			
			System.out.println("=====ExportReporterBaton.exportRevision===="+exportRevision ); 
//			reporter.setPath( "" , null , exportRevision , SVNDepth.FILES, false);
			reporter.setPath( "" , null , exportRevision , SVNDepth.INFINITY, true);
			reporter.finishReport( );
			
		}catch(SVNException svne ) {
			reporter.abortReport( );
			 System.out.println( "Report failed" );
		}
	}
}