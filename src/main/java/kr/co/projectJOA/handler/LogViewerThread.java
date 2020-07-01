/**
 * 
 */
package kr.co.projectJOA.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author chcho
 *
 */
public class LogViewerThread implements Runnable {
	
	private static Logger log = LoggerFactory.getLogger(LogViewerThread.class);
			
	private Set<WebSocketSession> sessionSet = new HashSet<>();
    private int crunchifyRunEveryNSeconds = 2000;
    private long lastKnownPosition = 0;
    private boolean shouldIRun = true;
    private File crunchifyFile = null;
    
    public LogViewerThread(File crunchifyFile,Set<WebSocketSession> sessionSet){
    	this.crunchifyFile = crunchifyFile;
    	this.sessionSet = sessionSet;
    }
    
    public void sendMessage(String message) {
        for (WebSocketSession session : this.sessionSet) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception ignored) {
                    log.error("fail to send message!", ignored);
                }
            }
        }
    }
    
	@Override
	public void run() {
        while (shouldIRun) {
            try {
                Thread.sleep(crunchifyRunEveryNSeconds);
                long fileLength = crunchifyFile.length();
//System.out.println("==========1====fileLength="+fileLength);                    
                if (fileLength > lastKnownPosition) {
//System.out.println("==========12====");                    	
                    RandomAccessFile readWriteFileAccess = new RandomAccessFile(crunchifyFile, "rw");
                    readWriteFileAccess.seek(lastKnownPosition);
                    String crunchifyLine = null;
                    while ((crunchifyLine = readWriteFileAccess.readLine()) != null) {
                        sendMessage(new String(crunchifyLine.getBytes("8859_1"), "KSC5601"));
                    }
                    lastKnownPosition = readWriteFileAccess.getFilePointer();
                    readWriteFileAccess.close();
//System.out.println("==========13====");                        
                }
                
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }		
	}

	
}
