/**
 * 
 */
package kr.co.projectJOA.com.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author chcho
 * @since 2016. 3. 25.
 * <pre>
 * Name : ConvertUtil.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 3. 25.     chcho    최초생성
 * </pre>
 */
@SuppressWarnings("unchecked")
public class ConvertUtil {

	/**
	 * Convert.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @param clasz
	 *            the clasz
	 * @return the t
	 */
	public static <T> T convert(Object value, Class<T> clasz) {
		return convert(value, clasz, null);
	}

	/**
	 * Convert.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @param clasz
	 *            the clasz
	 * @param defaultValue
	 *            the default value
	 * @return the t
	 */
	public static <T> T convert(Object value, Class<T> clasz, T defaultValue) {
		return value != null ? (T) ConvertUtils.convert(value, clasz) : defaultValue;
	}

	/**
	 * Convert json to list.
	 *
	 * @param json
	 *            the json
	 * @return the list
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<HashMap<String, Object>> convertJsonToList(String json) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(json, new TypeReference<List<HashMap<String, Object>>>() {
		});
	}

	/**
	 * Convert json to list.
	 *
	 * @param <T>
	 *            the generic type
	 * @param json
	 *            the json
	 * @param typeReference
	 *            the type reference
	 * @return the t
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static <T> T convertJsonToList(String json, TypeReference<T> typeReference) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(json, typeReference);
	}

	/**
	 * Convert json to map.
	 *
	 * @param json
	 *            the json
	 * @return the hash map
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static HashMap<String, Object> convertJsonToMap(String json) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(json, new TypeReference<HashMap<String, Object>>() {
		});
	}
	
}
