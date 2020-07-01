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
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * LogViewerThread 작성이전.. 복사본
 * @author chcho
 *
 */
public class CopyOfLogHandler extends TextWebSocketHandler {
	
	private static Logger log = LoggerFactory.getLogger(CopyOfLogHandler.class);
			
    private Set<WebSocketSession> sessionSet = new HashSet<>();
    private int crunchifyRunEveryNSeconds = 2000;
    private long lastKnownPosition = 0;
    private boolean shouldIRun = true;
    private File crunchifyFile = null;
    
    public CopyOfLogHandler() {
        super();
        log.info("create SocketHandler instance!");
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        
        if(thread != null) thread.stop();
        
        sessionSet.remove(session);

        log.info("remove session!");
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionSet.add(session);
        
        log.info("add session!");
    }
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        try{
	        //String fileNm = message.getPayload().toString();
	        String fileNm = "D:/exports/logs/deploy_log_11.log";
	        
	        crunchifyFile = new File(fileNm);
	        
	        lastKnownPosition = 0;
	        log.info("receive message:" + message.toString());
	        
	        if(!thread.isAlive()) thread.start();
	        
	        log.debug("==crunchifyFile.canRead()=="+crunchifyFile.canRead());
	        
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("web socket error!", exception);
    }
    
    @Override
    public boolean supportsPartialMessages() {
        log.info("call method!");
        return super.supportsPartialMessages();
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
    
    
    Thread thread = new Thread() {
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
    };
}