/**
 * 
 */
package kr.co.projectJOA.svn.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

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
public class ExportEditor implements ISVNEditor{

	
	private File myRootDirectory;
	private SVNDeltaProcessor myDeltaProcessor;
	
    public ExportEditor(File root) {
        myRootDirectory = root;
        myDeltaProcessor = new SVNDeltaProcessor();
    }
    
	@Override
	public void applyTextDelta(String path, String baseChecksum) throws SVNException {
		// TODO Auto-generated method stub
		myDeltaProcessor.applyTextDelta((File) null, new File(myRootDirectory, path), false);		
	}

	@Override
	public OutputStream textDeltaChunk(String path, SVNDiffWindow diffWindow) throws SVNException {
		// TODO Auto-generated method stub
		return myDeltaProcessor.textDeltaChunk(diffWindow);
	}

	@Override
	public void textDeltaEnd(String arg0) throws SVNException {
		// TODO Auto-generated method stub
		myDeltaProcessor.textDeltaEnd();
	}

	@Override
	public void abortEdit() throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void absentDir(String arg0) throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void absentFile(String arg0) throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDir(String path, String copyFromPath, long copyFromRevision) throws SVNException {
		// TODO Auto-generated method stub
      File newDir = new File(myRootDirectory, path);
      if (!newDir.exists()) {
          if (!newDir.mkdirs()) {
              SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "error: failed to add the directory ''{0}''.", newDir);
              throw new SVNException(err);
          }
      }
//      System.out.println("dir added: " + path);		
	}

	@Override
	public void addFile(String path, String copyFromPath, long copyFromRevision) throws SVNException {
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
        
//        System.out.println("file added: " + path);
	}

	@Override
	public void changeDirProperty(String arg0, SVNPropertyValue arg1)
			throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeFileProperty(String arg0, String arg1,
			SVNPropertyValue arg2) throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeDir() throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SVNCommitInfo closeEdit() throws SVNException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeFile(String arg0, String arg1) throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEntry(String arg0, long arg1) throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openDir(String arg0, long arg1) throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openFile(String arg0, long arg1) throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openRoot(long arg0) throws SVNException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void targetRevision(long arg0) throws SVNException {
		// TODO Auto-generated method stub
		
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
