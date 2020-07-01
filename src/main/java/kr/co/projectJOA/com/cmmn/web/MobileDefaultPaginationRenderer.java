/**
 * 
 */
package kr.co.projectJOA.com.cmmn.web;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

import kr.co.projectJOA.com.cmmn.paging.MobileAbstractPaginationRenderer;

/**
 * @author chcho
 * @since 2016. 3. 31.
 * <pre>
 * Name : CmsDefaultPaginationRenderer.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 3. 31.     chcho    최초생성
 * </pre>
 */
public class MobileDefaultPaginationRenderer extends MobileAbstractPaginationRenderer implements ServletContextAware{

	private ServletContext servletContext;
	
	public MobileDefaultPaginationRenderer(){
		 
	}
	
	public void initVariables() {
		
		firstPageLabel = "";
		previousPageLabel = "<div class=\"prev\"><button onclick=\"{0}({1}); return false;\">이전페이지</button></div>";
		currentPageLabel = "<div class=\"page\"><span class=\"current-page\">{1}</span>";
		totalPageLabel = "<span class=\"total-page\">{2}</span></div>";
		nextPageLabel = "<div class=\"next\"><button onclick=\"{0}({1}); return false;\">다음페이지</button></div>";
		lastPageLabel = "";
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		initVariables();
	}	
}
