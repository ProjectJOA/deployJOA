/**
 * 
 */
package kr.co.projectJOA.svn.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.diff.SVNDeltaProcessor;
import org.tmatesoft.svn.core.io.diff.SVNDiffWindow;

/**
 * @author chcho
 *
 */
public class ExportDeployPartialEditor implements ISVNEditor{

	
	private File myRootDirectory;
	private SVNDeltaProcessor myDeltaProcessor;
    private boolean isSelectedDirVersion = false;
    private boolean isSelectedFileVersion = false;
    private List myVersionList;
	private String targetVersion;
	private int ord = 0;
	
    public ExportDeployPartialEditor(File root) {
        myRootDirectory = root;
        myDeltaProcessor = new SVNDeltaProcessor();
        
    }
    
	@Override
	public void applyTextDelta(String path, String baseChecksum) throws SVNException {
		// TODO Auto-generated method stub
//		if(isSelectedFileVersion){
			myDeltaProcessor.applyTextDelta((File) null, new File(myRootDirectory, path), false);
//		}
		
System.out.println("==applyTextDelta==ord==="+ord++);		
	}

	@Override
	public OutputStream textDeltaChunk(String path, SVNDiffWindow diffWindow) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==textDeltaChunk==ord==="+ord++);
//		if(isSelectedFileVersion){
//			return null;
//		}
		
		return myDeltaProcessor.textDeltaChunk(diffWindow);
	}

	@Override
	public void textDeltaEnd(String arg0) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==textDeltaEnd==ord==="+ord++);		
//		if(isSelectedFileVersion){
			myDeltaProcessor.textDeltaEnd();
//		}
	}

	@Override
	public void abortEdit() throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==abortEdit==ord==="+ord++);		
	}

	@Override
	public void absentDir(String arg0) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==absentDir==ord==="+ord++);		
	}

	@Override
	public void absentFile(String arg0) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==absentFile==ord==="+ord++);		
	}

	@Override
	public void addDir(String path, String copyFromPath, long copyFromRevision) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==addDir==ord==="+(ord++)+"/path="+path+"/copyFromPath="+copyFromPath+"/copyFromRevision="+copyFromRevision);
//		if(isSelectedDirVersion){
			File newDir = new File(myRootDirectory, path);
			if (!newDir.exists()) {
			    if (!newDir.mkdirs()) {
			        SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "error: failed to add the directory ''{0}''.", newDir);
			        throw new SVNException(err);
			    }
			}
//		}
//      System.out.println("dir added: " + path);		
	}

	@Override
	public void addFile(String path, String copyFromPath, long copyFromRevision) throws SVNException {
System.out.println("==addFile==ord==="+(ord++)+"/path="+path+"/copyFromPath="+copyFromPath+"/copyFromRevision="+copyFromRevision);		
//System.out.println("======addFile===copyFromRevision====="+copyFromRevision);
//		if(isSelectedFileVersion){
			String dirPath = getDirFromFilename(path);
			if(!"".equals(dirPath)){
				File newDir = new File(myRootDirectory, dirPath);
				if (!newDir.exists()) {
					newDir.mkdirs();
				}
			}
			
	        File file = new File(myRootDirectory, path);
	        if (file.exists()) {
	//            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "error: exported file ''{0}'' already exists!", file);
	//            throw new SVNException(err);
	        	file.deleteOnExit();
	        }
	        
	        try {
	        	if (!file.exists()) {
	        		file.createNewFile();
	        	}
	        } catch (IOException e) {
	            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "error: cannot create new  file ''{0}''", file);
	            throw new SVNException(err);
	        }
//		}
//        System.out.println("file added: " + path);
	}

	@Override
	public void changeDirProperty(String name, SVNPropertyValue property) throws SVNException {
		ord = 0;
		// TODO Auto-generated method stub
//System.out.println("changeDirProperty===name="+name);		
System.out.println("==changeDirProperty==ord==="+(ord++)+"/path="+name+"/property.getString()="+property.getString());		
/*    	if("svn:entry:committed-rev".equals(name)){
//System.out.println("==changeDirProperty==ord==="+(ord++)+"/path="+name+"/property.getString()="+property.getString());    		
			if(targetVersion.equals(property.getString())){	
    			isSelectedDirVersion = true;
    		}else{
    			isSelectedDirVersion = false;
    			addDir("", null, -1);
    		}
    	}	*/	
	}

	@Override
	public void changeFileProperty(String path, String name, SVNPropertyValue property) throws SVNException {
		// TODO Auto-generated method stub
//		System.out.println("changeDirProperty===name="+name+"/property="+property);
		
System.out.println("==changeFileProperty==ord==="+(ord++)+"/path="+name+"/property.getString()="+property.getString());		
		if("svn:entry:committed-rev".equals(name)){
//System.out.println("==changeFileProperty==ord==="+(ord++)+"/path="+name+"/property.getString()="+property.getString());					
			if(targetVersion.equals(property.getString())){	
				isSelectedFileVersion = true;
			}else{
				isSelectedFileVersion = false;
			}
		}		
	}

	@Override
	public void closeDir() throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==closeDir==ord==="+ord++);		
	}

	@Override
	public SVNCommitInfo closeEdit() throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==closeEdit==ord==="+ord++);		
		return null;
	}

	@Override
	public void closeFile(String path, String textChecksum) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==closeFile==ord==="+ord++);
	}

	@Override
	public void deleteEntry(String arg0, long arg1) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==deleteEntry==ord==="+ord++);
	}

	@Override
	public void openDir(String path, long revision) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==openDir==ord==="+ord++);
	}

	@Override
	public void openFile(String path, long revision) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==openFile==ord==="+ord++);
	}

	@Override
	public void openRoot(long arg0) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==openRoot==ord==="+ord++);
	}

	@Override
	public void targetRevision(long revision) throws SVNException {
		// TODO Auto-generated method stub
System.out.println("==targetRevision==ord==="+ord++);		
		targetVersion = ""+revision;
	}
	
	public void targetFileList(List logList){
		myVersionList = logList;
	}


    private String getDirFromFilename(String filename){
    	String dirPath = "";
    	if(filename.length()>1){
    		String[] arr = filename.split("[/]");
    		if(arr.length>1){
    			for(int i=0;i<arr.length-1;i++){
    				dirPath += "/"+arr[i];
    			}
    		}
    		
    	}
    	return dirPath;
    }		
}
