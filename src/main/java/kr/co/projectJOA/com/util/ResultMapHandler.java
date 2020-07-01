/**
 * 
 */
package kr.co.projectJOA.com.util;

import org.apache.commons.collections.map.ListOrderedMap;

import egovframework.rte.psl.dataaccess.util.CamelUtil;

/**
 * @author chcho
 * @since 2016. 4. 19.
 * <pre>
 * Name : ResultMapHandler.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 4. 19.     chcho    최초생성
 * </pre>
 */
public class ResultMapHandler extends ListOrderedMap{

	private static final long serialVersionUID = 6723434363565852262L;
	
	@Override
	public Object put(Object key, Object value){
		return super.put(CamelUtil.convert2CamelCase((String) key), ExStringUtils.returnDataAsDataType(value));
	}
}
