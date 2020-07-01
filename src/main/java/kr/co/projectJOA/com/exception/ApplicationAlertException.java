/**
 * 
 */
package kr.co.projectJOA.com.exception;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.fdl.cmmn.exception.BaseException;

/**
 * @author chcho
 * @since 2016. 3. 25.
 * <pre>
 * Name : ApplicationAlertException.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 3. 25.     chcho    최초생성
 * </pre>
 */
public class ApplicationAlertException extends BaseException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -798040826500254001L;
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationAlertException.class);
	
	/**
	 * alert 메세지만 보여줌
	 * @param response
	 * @param message
	 */
	public void ApplicationAlertException(HttpServletResponse response, String message){
		response.setContentType("text/html;charset=utf-8"); //한글깨짐방지
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.println("<script type='text/javascript'>");
			writer.println("alert('"+message+"');");
			writer.println("return;");
			writer.println("</script>");
			writer.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}			
	}

	/**
	 * alert 메세지 후 페이지 forward
	 * @param response
	 * @param message
	 * @param forwardUrl
	 */
	public void ApplicationAlertException(HttpServletResponse response, String message, String forwardUrl){
		response.setContentType("text/html;charset=utf-8"); //한글깨짐방지
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.println("<script type='text/javascript'>");
			writer.println("alert('"+message+"');");
			writer.println("location.replace('"+forwardUrl+"') ");
			writer.println("</script>");
			writer.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}			
	}	
	
	/**
	 * alert메세지 history.go(-1)
	 * @param response
	 * @param message
	 */
	public void ApplicationAlertHistorybackException(HttpServletResponse response, String message){
		response.setContentType("text/html;charset=utf-8"); //한글깨짐방지
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.println("<script type='text/javascript'>");
			writer.println("alert('"+message+"');");
			writer.println("history.go(-1) ");
			writer.println("</script>");
			writer.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}			
	}	
		
}
