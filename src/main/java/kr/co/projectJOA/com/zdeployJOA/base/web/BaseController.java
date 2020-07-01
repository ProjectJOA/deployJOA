/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.base.web;

import java.util.HashMap;

import javax.annotation.Resource;

import kr.co.projectJOA.com.util.ExNumberUtils;
import kr.co.projectJOA.com.util.ExStringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @author chcho
 *
 */
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;
	
	/**
	 * 페이징 초기화를 위한 처리
	 * @param param
	 * @param totalCnt
	 * @param model
	 * @throws Exception
	 */
	public void initPaginationInfo(PaginationInfo paginationInfo, HashMap<String,Object> param, ModelMap model) throws Exception{
		if( !param.containsKey("pitem")){
			param.put("pitem", propertyService.getInt("pageUnit"));
		}
		param.put("psize", propertyService.getInt("pageSize"));
		
		paginationInfo.setCurrentPageNo( ExNumberUtils.toInt( ExStringUtils.defaultIfNull(param.get("page"), "1")));
		paginationInfo.setRecordCountPerPage(ExNumberUtils.toInt( ExStringUtils.defaultIfNull(param.get("pitem"), "1")));
		paginationInfo.setPageSize(ExNumberUtils.toInt( ExStringUtils.defaultIfNull(param.get("psize"), "1")));
		
		param.put("findex", paginationInfo.getFirstRecordIndex());
		param.put("lindex", paginationInfo.getLastRecordIndex());
		param.put("ppage", paginationInfo.getRecordCountPerPage());
		
/*		logger.debug("=======paginationInfo=getCurrentPageNo==="+paginationInfo.getCurrentPageNo());
		logger.debug("=======paginationInfo=getRecordCountPerPage==="+paginationInfo.getRecordCountPerPage());
		logger.debug("=======paginationInfo=getPageSize==="+paginationInfo.getPageSize());
		
		logger.debug("=======param===="+param.toString());*/
		
	}	
	
	public int getTotalPage(long lastestVersion){
		
		int totalPageCnt = (int)(lastestVersion/10)+ ( lastestVersion%10 > 0 ? 1 : 0);
		return totalPageCnt;
	}	
}
