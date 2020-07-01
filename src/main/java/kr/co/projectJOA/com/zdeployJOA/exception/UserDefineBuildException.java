/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.exception;

import egovframework.rte.fdl.cmmn.exception.BaseException;

/**
 * @author chcho
 *
 */
public class UserDefineBuildException extends BaseException{

	private static final long serialVersionUID = -798040826500254005L;
	
    public UserDefineBuildException(){              // 기본 생성자
	}
    
	public UserDefineBuildException(String s){
	            super("Build Error="+s);
	}	
}
