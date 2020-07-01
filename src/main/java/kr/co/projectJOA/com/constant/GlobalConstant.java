/**
 * 
 */
package kr.co.projectJOA.com.constant;

import kr.co.projectJOA.com.util.EgovProperties;
import kr.co.projectJOA.com.util.ExNumberUtils;

/**
 * @author chcho
 * @since 2016. 4. 5.
 * <pre>
 * Name : GlobalConstant.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 4. 5.     chcho    최초생성
 * </pre>
 */
public class GlobalConstant {

	public static final String UPLOAD_PATH = EgovProperties.getProperty("Globals.fileStorePath");
	
	/** The Constant BUFFER_SIZE. */
	public static final int BUFFER_SIZE = 1024 * 1024 * 20;
	
	/** The Constant File_SIZE.  */
	public static final int FILE_SIZE = 1024 * 1024 * ExNumberUtils.toInt(EgovProperties.getProperty("Globals.upload_filesize"), 5);	

	/** The Constant Manage File_SIZE.  */
	public static final int FILE_SIZE_MNG = 1024 * 1024 * ExNumberUtils.toInt(EgovProperties.getProperty("Globals.upload_mngfilesize"), 20);
	
	/** site domain http */
	public static String DOMAIN_HTTP = EgovProperties.getProperty("Globals.domain_http");
	
	/** site domain https */
	public static String DOMAIN_HTTPS = EgovProperties.getProperty("Globals.domain_https");
	
	/** 대표 서버 설정 */
	public static String SERVER_NO = EgovProperties.getProperty("Globals.server_no");	
	
	public static boolean IS_ABLE_PATCHPROCESS = true;

}
