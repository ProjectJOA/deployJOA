/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.cmm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.projectJOA.com.zdeployJOA.deployMng.web.DeployMngController;
import kr.co.projectJOA.com.zdeployJOA.exception.UserDefineBuildException;

/**
 * @author chcho
 *
 */
public class cmUtils {

	private static String OS_NAME = System.getProperty("os.name").toLowerCase();
	private static Logger logger = LoggerFactory.getLogger(cmUtils.class);
	
	public static void setSessionSNum(HttpServletRequest request, String sNum){
		HttpSession session = request.getSession(false);
		session = request.getSession(true);
		session.setAttribute("sNum", sNum);		
	}
	
	public static String getSessionSNum(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		session = request.getSession(true);
		String sNum = (String)session.getAttribute("sNum");
		return sNum;
	}
	
	
	public static void mavenBuilder(List<String> command) throws Exception{
		 mavenBuilder( command, logger);
	}
	

	public static void mavenBuilder(List<String> command, Logger logger) throws Exception{
		Process p;
		BufferedReader br = null;
		
		try{
			
			p = new ProcessBuilder(command).start();

			br = new BufferedReader(new InputStreamReader(p.getInputStream(),"MS949"));
			String line = null;
			
			while ((line = br.readLine()) != null) {
//				logger.info(line);
				System.out.println(line);
			  if(line.indexOf("BUILD FAILURE") >= 0){
				  throw new UserDefineBuildException();
			  }
			  if(line.indexOf("ERROR") >= 0){
				  throw new UserDefineBuildException();
			  }			  
			}

			p.destroy();
			br.close();
		}catch( Exception e) {
			e.printStackTrace();
		}finally{
			try{br.close();}catch(Exception e){}
		}		
	}

	public static String antCommand(List<String> command, Logger logger) throws Exception{
		Process p;
		BufferedReader br = null;
		StringBuffer strb = new StringBuffer();
		try{
			
			p = new ProcessBuilder(command).start();
	
			br = new BufferedReader(new InputStreamReader(p.getInputStream(),"MS949"));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				logger.info(line);
				strb.append(line);
			}

			p.destroy();
			br.close();
			
			return strb.toString();
			
		}finally{
			try{br.close();}catch(Exception e){}
		}		
	}
	
    public static boolean isWindows() {
        return (OS_NAME.indexOf("win") >= 0);
    }	
}
