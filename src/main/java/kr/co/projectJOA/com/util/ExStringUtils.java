/**
 * 
 */
package kr.co.projectJOA.com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chcho
 * @since 2016. 3. 31.
 * <pre>
 * Name : ExtendStringUtils.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 3. 31.     chcho    최초생성
 * </pre>
 */
public class ExStringUtils extends StringUtils{

	private static final Logger logger = LoggerFactory.getLogger(ExStringUtils.class);
	
	public static String defaultIfNull(Object obj , String defaultStr) throws Exception{
		
		String returnStr = "";
		if(obj == null || "".equals(obj.toString())){
			returnStr = defaultStr;
		}else{
			returnStr = defaultIfBlank(obj.toString(), defaultStr);
		}
		
		return returnStr;
	}
	
    public static String removeMinusChar(String str) {
        return remove(str, '-');
    }	
    
    /*
     * Clob 를 String 으로 변경
     */
    public static String clobToString(Clob clob) throws SQLException,
      IOException {

     if (clob == null) {
      return "";
     }

     StringBuffer strOut = new StringBuffer();

     String str = "";

     BufferedReader br = new BufferedReader(clob.getCharacterStream());

     while ((str = br.readLine()) != null) {
    	 strOut.append(str);
     }
     return strOut.toString();
    }    

    
    public static Object returnDataAsDataType(Object value){
    	
    	if(value  instanceof java.sql.Clob ){
    		try{
    		value = ExStringUtils.clobToString((Clob)value);
    		}catch(Exception e){
    			logger.error("returnDataAsDataType Error : "+e.getLocalizedMessage());
    		}
    	}
    
    	return value;
    }
}
