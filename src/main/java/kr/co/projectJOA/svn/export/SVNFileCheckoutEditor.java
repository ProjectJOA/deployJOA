/**
 * 
 */
package kr.co.projectJOA.svn.export;

import java.io.File;
import java.io.OutputStream;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.internal.wc.SVNFileUtil;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNFileCheckoutTarget;
import org.tmatesoft.svn.core.io.diff.SVNDeltaProcessor;
import org.tmatesoft.svn.core.io.diff.SVNDiffWindow;

/**
 * @author chcho
 *
 */
public class SVNFileCheckoutEditor implements ISVNEditor {
    
    private ISVNFileCheckoutTarget myTarget;
    private SVNDeltaProcessor myDeltaProcessor;
    private File myRootDirectory;
    private boolean isSelectedVersionDir = false;    
    private boolean isSelectedVersionFile = false;
    private long targetVersion = -1;

    public SVNFileCheckoutEditor(File root) {
        myRootDirectory = root;
        myDeltaProcessor = new SVNDeltaProcessor();
    }

    public void abortEdit() throws SVNException {
System.out.println("==abortEdit=");    	
    }

    public void absentDir(String path) throws SVNException {
System.out.println("==absentDir="+path);    	
    }

    public void absentFile(String path) throws SVNException {
System.out.println("=absentFile=="+path);    	
    }

    public void addDir(String path, String copyFromPath, long copyFromRevision) throws SVNException {
    	if(isSelectedVersionFile || isSelectedVersionFile){
    		System.out.println("==addDir="+path);    	
    	}
    }

    public void addFile(String path, String copyFromPath, long copyFromRevision) throws SVNException {
    	if(isSelectedVersionFile || isSelectedVersionFile){
    		System.out.println("=addFile=="+path);    	
    	}
    }

    public void changeDirProperty(String name, SVNPropertyValue value) throws SVNException {
		if("svn:entry:committed-rev".equals(name) && value.equals( Long.toString(targetVersion))){
			isSelectedVersionFile = true;
		}else{
			isSelectedVersionFile = false;
		}    	
    }

    public void changeFileProperty(String path, String name, SVNPropertyValue value) throws SVNException {
    	
		if("svn:entry:committed-rev".equals(name) && value.equals( Long.toString(targetVersion))){
			isSelectedVersionFile = true;
		    myTarget.filePropertyChanged(path, name, value);
		}else{
			isSelectedVersionFile = false;
		}
    }

    public void closeDir() throws SVNException {
    	if(isSelectedVersionFile || isSelectedVersionFile){
    		System.out.println("==closeDir=");    	
    	}
    }

    public SVNCommitInfo closeEdit() throws SVNException {
    	if(isSelectedVersionFile || isSelectedVersionFile){
    		System.out.println("=closeEdit==");    	
    	}
        return null;
    }

    public void closeFile(String path, String textChecksum) throws SVNException {
    	if(isSelectedVersionFile || isSelectedVersionFile){
    		System.out.println("==closeFile="+path);    	
    	}
    }

    public void deleteEntry(String path, long revision) throws SVNException {
System.out.println("==deleteEntry="+path+"/"+revision);    	
    }

    public void openDir(String path, long revision) throws SVNException {
System.out.println("=openDir=="+path+"/"+revision);
    }

    public void openFile(String path, long revision) throws SVNException {
System.out.println("==openFile="+path+"/"+revision);    	
    }

    public void openRoot(long revision) throws SVNException {
System.out.println("==openRoot="+revision);
		targetVersion = revision;
    }

    public void targetRevision(long revision) throws SVNException {
System.out.println("=targetRevision=="+revision);

		targetVersion = revision;
    }

    public void applyTextDelta(String path, String baseChecksum) throws SVNException {
    	
//        myDeltaProcessor.applyTextDelta(SVNFileUtil.DUMMY_IN, myTarget.getOutputStream(path), false);
		if(isSelectedVersionFile || isSelectedVersionFile){
			System.out.println("=applyTextDelta=="+path);
			myDeltaProcessor.applyTextDelta((File) null, new File("D:/exports/src", path), false);
		}
    }

    public OutputStream textDeltaChunk(String path, SVNDiffWindow diffWindow) throws SVNException {

		OutputStream os = null;
		if(isSelectedVersionFile || isSelectedVersionFile){
			System.out.println("=textDeltaChunk=="+path);
			os = myDeltaProcessor.textDeltaChunk(diffWindow);
		}

        return os;
    }

    public void textDeltaEnd(String path) throws SVNException {

    	if(isSelectedVersionFile || isSelectedVersionFile){
			System.out.println("==textDeltaEnd="+path);			
			myDeltaProcessor.textDeltaEnd();
		}
    }

}
