/**
 * 
 */
package kr.co.projectJOA.com.util;

import java.sql.Clob;
import java.sql.Date;

/**
 * @author chcho
 * @since 2016. 3. 25.
 * <pre>
 * Name : ResultMap.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 3. 25.     chcho    최초생성
 * </pre>
 */
public class ResultMap extends ResultMapHandler {

	/** serialVersionUID */
	private static final long serialVersionUID = 2793289837053231669L;

	/**
	 * Gets the boolean.
	 *
	 * @param key
	 *            the key
	 * @return the boolean
	 */
	public Boolean getBoolean(String key) {
		return ConvertUtil.convert(get(key), Boolean.class);
	}

	/**
	 * Gets the clob.
	 *
	 * @param key
	 *            the key
	 * @return the clob
	 */
	public Clob getClob(String key) {
		return (Clob) get(key);
	}

	/**
	 * Gets the date.
	 *
	 * @param key
	 *            the key
	 * @return the date
	 */
	public Date getDate(String key) {
		return ConvertUtil.convert(get(key), Date.class);
	}

	/**
	 * Gets the double.
	 *
	 * @param key
	 *            the key
	 * @return the double
	 */
	public Double getDouble(String key) {
		return ConvertUtil.convert(get(key), Double.class);
	}

	/**
	 * Gets the float.
	 *
	 * @param key
	 *            the key
	 * @return the float
	 */
	public Float getFloat(String key) {
		return ConvertUtil.convert(get(key), Float.class);
	}

	/**
	 * Gets the int.
	 *
	 * @param key
	 *            the key
	 * @return the int
	 */
	public Integer getInt(String key) {
		return ConvertUtil.convert(get(key), Integer.class);
	}

	/**
	 * Gets the long.
	 *
	 * @param key
	 *            the key
	 * @return the long
	 */
	public Long getLong(String key) {
		return ConvertUtil.convert(get(key), Long.class);
	}

	/**
	 * Gets the short.
	 *
	 * @param key
	 *            the key
	 * @return the short
	 */
	public Short getShort(String key) {
		return ConvertUtil.convert(get(key), Short.class);
	}

	/**
	 * Gets the string.
	 *
	 * @param key
	 *            the key
	 * @return the string
	 */
	public String getString(String key) {
		return ConvertUtil.convert(get(key), String.class);
	}
}
