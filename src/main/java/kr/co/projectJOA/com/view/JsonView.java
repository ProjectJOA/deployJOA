/**
 * 
 */
package kr.co.projectJOA.com.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @author chcho
 * @since 2016. 4. 29.
 * <pre>
 * Name : JsonView.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 4. 29.     chcho    최초생성
 * </pre>
 */
public class JsonView extends AbstractView {

	public static final String DEFAULT_CONTENT_TYPE = "text/html";
	private ObjectMapper objectMapper = new ObjectMapper();

	private JsonEncoding encoding = JsonEncoding.UTF8;

	private boolean prefixJson = false;
	private Set<String> renderedAttributes;
	private boolean disableCaching = true;

	private boolean pretty = true;

	public JsonView() {
		setContentType("text/html");
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = objectMapper;
	}

	public void setEncoding(JsonEncoding encoding) {
		Assert.notNull(encoding, "'encoding' must not be null");
		this.encoding = encoding;
	}

	public void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
	}

	public void setRenderedAttributes(Set<String> renderedAttributes) {
		this.renderedAttributes = renderedAttributes;
	}

	public void setDisableCaching(boolean disableCaching) {
		this.disableCaching = disableCaching;
	}

	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(getContentType());
		response.setCharacterEncoding(this.encoding.getJavaName());

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "86400");

		if (this.disableCaching) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
	}

	public void setPretty(boolean pretty) {
		this.pretty = pretty;
	}

	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object value = filterModel(model);
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(), this.encoding);

		if (this.pretty) {
			generator.setPrettyPrinter(new DefaultPrettyPrinter());
		}

		if (this.prefixJson) {
			generator.writeRaw("{} && ");
		}
		this.objectMapper.writeValue(generator, value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object filterModel(Map<String, Object> model) {
		Map result = new HashMap(model.size());
		Set renderedAttributes = !CollectionUtils.isEmpty(this.renderedAttributes) ? this.renderedAttributes :
			model.keySet();
		for (Map.Entry entry : model.entrySet()) {
			if ((!(entry.getValue() instanceof BindingResult)) && (renderedAttributes.contains(entry.getKey()))) {
				result.put((String)entry.getKey(), entry.getValue());
			}
		}

		if (!(result instanceof Map)) {
			return result;
		}
		Map map = result;
		if (map.size() == 1) {
			return map.values().toArray()[0];
		}
		return map;
	}
}
