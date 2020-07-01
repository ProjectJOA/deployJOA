/**
 * 
 */
package kr.co.projectJOA.com.util;

import java.io.File;
import java.io.IOException;

/**
 * @author chcho
 * @since 2016. 4. 5.
 * <pre>
 * Name : FileUtil.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 4. 5.     chcho    최초생성
 * </pre>
 */
public class FileUtil {

	public static boolean createThumbnail(String sourcePathName, String thumbnailPathName, int thumbnailWidth, int thumbnailHeight, String fileExt, int resizeType) throws IOException {
		
		return false;
	}	
	
	public static void makeDirs(String dirPaths) throws Exception{
		File targetDir = new File(dirPaths);
        if(!targetDir.exists()){
        	targetDir.mkdirs();
        }        
	}
}
