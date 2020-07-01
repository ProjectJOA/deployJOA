/**
 * 
 */
package kr.co.projectJOA.com.cmmn.paging;

import java.text.MessageFormat;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationRenderer;

/**
 * @author chcho
 * @since 2016. 3. 31.
 * <pre>
 * Name : CmsAbstractPaginationRenderer.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 3. 31.     chcho    최초생성
 * </pre>
 */
public class CmsAbstractPaginationRenderer implements PaginationRenderer{

	public String firstPageLabel;
	public String previousPageLabel;
	public String currentPageLabel;
	public String otherPageLabel;
	public String nextPageLabel;
	public String lastPageLabel;
	
	public String renderPagination(PaginationInfo paginationInfo, String jsFunction) {

		StringBuffer strBuff = new StringBuffer();

		int firstPageNo = paginationInfo.getFirstPageNo();
		int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList();
		int totalPageCount = paginationInfo.getTotalPageCount();
		int pageSize = paginationInfo.getPageSize();
		int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList();
		int currentPageNo = paginationInfo.getCurrentPageNo();
		int lastPageNo = paginationInfo.getLastPageNo();

		strBuff.append("<span class=\"num\">");
		
		if (totalPageCount > pageSize) {
			if (firstPageNoOnPageList > pageSize) {
				strBuff.append(MessageFormat.format(firstPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
				strBuff.append(MessageFormat.format(previousPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList - 1) }));
			} else {
				strBuff.append(MessageFormat.format(firstPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
				strBuff.append(MessageFormat.format(previousPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
			}
		}

		for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
			if (i == currentPageNo) {
				strBuff.append(MessageFormat.format(currentPageLabel, new Object[] { jsFunction, Integer.toString(i), Integer.toString(i) }));
			} else {
				strBuff.append(MessageFormat.format(otherPageLabel, new Object[] { jsFunction, Integer.toString(i), Integer.toString(i) }));
			}
		}

		if (totalPageCount > pageSize) {
			if (lastPageNoOnPageList < totalPageCount) {
				strBuff.append(MessageFormat.format(nextPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList + pageSize) }));
				strBuff.append(MessageFormat.format(lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
			} else {
				strBuff.append(MessageFormat.format(nextPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
				strBuff.append(MessageFormat.format(lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
			}
		}
		
		strBuff.append("</span>");
		
		return strBuff.toString();
	}
}
