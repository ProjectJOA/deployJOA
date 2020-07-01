/**
 * 
 */
package kr.co.projectJOA.com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;
/**
 * @author chcho
 * @since 2016. 3. 25.
 * <pre>
 * Name : XSSFilter.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 3. 25.     chcho    최초생성
 * </pre>
 */
public class XSSFilter implements Filter{

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
	throws IOException, ServletException {
	
		if(excludeUrl((HttpServletRequest)request)) {
			chain.doFilter(request, response);
		} else {
			 chain.doFilter( XSSRequestWrapper.newInstance((HttpServletRequest) request), response);
		}
	}
	
	private boolean excludeUrl(final HttpServletRequest request) {
		final String uri = request.getRequestURI().toString().trim();
		
		final String xssExclude = "/admin";
		final String[] xss_Exclude_Patten = {"/{siteId}/brd/p_{seq}/**/register.do"};
	
		if(xssExclude.indexOf(uri) > -1){
			return true;
		}else{
		
			AntPathMatcher antPathMatcher = new AntPathMatcher();
			for (final String exclude : xss_Exclude_Patten) {
				if (antPathMatcher.match(exclude, uri)) { // 요청 패턴1에 대하여 일치하면 siteId를 가져온다.
logger.debug("========return======uri ==="+uri+"/exclude="+exclude);					
					return true;	
				}
			}
		}		
		return false;
	}
}
