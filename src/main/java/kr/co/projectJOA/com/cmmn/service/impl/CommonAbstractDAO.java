/**
 * 
 */
package kr.co.projectJOA.com.cmmn.service.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chcho
 * @since 2016. 3. 25.
 * <pre>
 * Name : CommonAbstractDAO.java
 * Description : 
 * << 개정이력(Modification Information) >>
 *     수정일               수정자                수정내용
 * ======================================
 *  2016. 3. 25.     chcho    최초생성
 * </pre>
 */
public abstract class CommonAbstractDAO {

	@Autowired
    private SqlSessionTemplate sqlSession;
	
	public Object select(String queryId, Object parameterObject) throws Exception {
//		return super.selectOne(queryId, parameterObject);
		
		return sqlSession.selectOne(queryId, parameterObject);
	}
	
	public List<?> list(String queryId, Object parameterObject) throws Exception {
		return (List<?>)sqlSession.selectList(queryId, parameterObject);
	}
	
	public List<?> list(String queryId) throws Exception {
		return (List<?>)sqlSession.selectList(queryId);
	}

	public Map<Object, Object> selectMap(String queryId, Object parameterObject, String mapKey) throws Exception {
		return (Map<Object, Object>)sqlSession.selectMap(queryId, parameterObject, mapKey);
	}
	
	public int update(String queryId, Object parameterObject) throws Exception {
		return sqlSession.update(queryId, parameterObject);
	}
	
	public int insert(String queryId, Object parameterObject) throws Exception {
		return sqlSession.insert(queryId, parameterObject);
	}
	
	public void delete(String queryId, Object parameterObject) throws Exception {
		sqlSession.delete(queryId, parameterObject);
	}	
}
