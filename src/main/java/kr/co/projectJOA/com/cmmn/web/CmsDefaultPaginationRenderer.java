/**
 * 
 */
package kr.co.projectJOA.com.cmmn.web;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

import kr.co.projectJOA.com.cmmn.paging.CmsAbstractPaginationRenderer;

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
public class CmsDefaultPaginationRenderer extends CmsAbstractPaginationRenderer implements ServletContextAware{

	private ServletContext servletContext;
	
	public CmsDefaultPaginationRenderer(){
		
	}
	
	public void initVariables() {
		firstPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<i class='fa fa-angle-double-left fa-lg'></i></a>&#160;";
		previousPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<i class='fa fa-angle-left fa-lg'></i></a>&#160;";
		currentPageLabel = "<a href=\"#\" class=\"current-page\" onclick=\"{0}({1}); return false;\">{2}</a>&#160;";
		otherPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">{2}</a>&#160;";
		nextPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<i class='fa fa-angle-right fa-lg'></i></a>&#160;";
		lastPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<i class='fa fa-angle-double-right fa-lg'></i></a>&#160;";
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		initVariables();
	}	
}
