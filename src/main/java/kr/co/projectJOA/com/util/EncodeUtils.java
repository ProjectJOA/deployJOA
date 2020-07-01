/**
 * 
 */
package kr.co.projectJOA.com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.net.util.Base64;

/**
 * @author chcho
 * @since 2016. 6. 9.
 * <pre>
 * Name : EncodeUtils.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 6. 9.     chcho    최초생성
 * </pre>
 */
public class EncodeUtils {

	private static final Logger logger = LoggerFactory.getLogger(EncodeUtils.class);
	
	/**
	 * Encode base64.
	 *
	 * @param str
	 *            the str
	 * @return the string
	 */
	public static String encodeBase64(String str) {
		if (logger.isInfoEnabled()) {
			logger.info("encodeBase64(String) - str={}", str); //$NON-NLS-1$
		}

		return str != null ? new String(Base64.encodeBase64((str).getBytes())) : str;
	}	
}
