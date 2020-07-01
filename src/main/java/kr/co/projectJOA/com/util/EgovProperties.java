/**
 * 
 */
package kr.co.projectJOA.com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chcho
 * @since 2016. 4. 5.
 * <pre>
 * Name : EgovProperties.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 4. 5.     chcho    최초생성
 * </pre>
 */
public class EgovProperties {

	private static final Logger logger = LoggerFactory.getLogger(EgovProperties.class);
			
	//프로퍼티값 로드시 에러발생하면 반환되는 에러문자열
	public static final String ERR_CODE =" EXCEPTION OCCURRED";
	public static final String ERR_CODE_FNFE =" EXCEPTION(FNFE) OCCURRED";
	public static final String ERR_CODE_IOE =" EXCEPTION(IOE) OCCURRED";
	
	//파일구분자
    static final char FILE_SEPARATOR     = File.separatorChar;

    public static final String RELATIVE_PATH_PREFIX = EgovProperties.class.getResource("").getPath().substring(0, EgovProperties.class.getResource("").getPath().lastIndexOf("com"));

    public static final String GLOBALS_PROPERTIES_FILE = RELATIVE_PATH_PREFIX + "props" + FILE_SEPARATOR + "globals.properties";
    
    public static final String GLOBALS_PROPERTIES_DIR = RELATIVE_PATH_PREFIX + "props" + FILE_SEPARATOR;
    
    /**
     * 인자로 주어진 문자열을 Key값으로 하는 상대경로 프로퍼티 값을 절대경로로 반환한다(Globals.java 전용)
     * @param keyName
     * @return
     */
	public static String getProperty(String keyName){
		String value = ERR_CODE;
		value="99";
		logger.debug(GLOBALS_PROPERTIES_FILE + " : " + keyName);
		FileInputStream fis = null;
		try{
			Properties props = new Properties();
			fis = new FileInputStream( filePathReplace(GLOBALS_PROPERTIES_FILE) );
			props.load(new java.io.BufferedInputStream(fis));
			value = props.getProperty(keyName).trim();
		}catch(FileNotFoundException fne){
			logger.debug(fne.getMessage());
		}catch(IOException ioe){
			logger.debug(ioe.getMessage());
		}catch(Exception e){
			logger.debug(e.getMessage());
		}finally{
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {
				//ex.printStackTrace();
				//System.out.println("IGNORE: " + ex);	// 2011.10.10 보안점검 후속조치
				logger.debug("IGNORED: " + ex.getMessage());
			}

		}
		return value;
	}    
	
	/**
	 * 주어진 파일에서 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 값을 반환한다
	 * @param fileName String
	 * @param key String
	 * @return String
	 */
	public static String getProperty(String fileName, String key){
		FileInputStream fis = null;
		try{
			java.util.Properties props = new java.util.Properties();
			fis = new FileInputStream( filePathReplace(fileName) );
			props.load(new java.io.BufferedInputStream(fis));
			fis.close();

			String value = props.getProperty(key);
			return value;
		}catch(java.io.FileNotFoundException fne){
			return ERR_CODE_FNFE;
		}catch(java.io.IOException ioe){
			return ERR_CODE_IOE;
		}finally{
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {
				//ex.printStackTrace();
				//System.out.println("IGNORE: " + ex);	// 2011.10.10 보안점검 후속조치
				logger.debug("IGNORED: " + ex.getMessage());
			}
		}
	}	
	
	public static String getPathProperty(String keyName){
		String value = ERR_CODE;
		value="99";
		logger.debug(GLOBALS_PROPERTIES_FILE + " : " + keyName);
		FileInputStream fis = null;
		try{
			Properties props = new Properties();
			fis = new FileInputStream(  filePathReplace(GLOBALS_PROPERTIES_FILE) );
			props.load(new java.io.BufferedInputStream(fis));
			value = props.getProperty(keyName).trim();
			value = GLOBALS_PROPERTIES_DIR  + FILE_SEPARATOR + value;
		}catch(FileNotFoundException fne){
			logger.debug(fne.getMessage());
		}catch(IOException ioe){
			logger.debug(ioe.getMessage());
		}catch(Exception e){
			logger.debug(e.getMessage());
		}finally{
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {
				//ex.printStackTrace();
				//System.out.println("IGNORE: " + ex);	// 2011.10.10 보안점검 후속조치
				logger.debug("IGNORED: " + ex.getMessage());
			}

		}
		return value;
	}	
	
	public static String filePathReplace(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replaceAll("\\.\\./", ""); // ../
		returnValue = returnValue.replaceAll("\\.\\.\\\\", ""); // ..\

		return returnValue;
	}	
}
