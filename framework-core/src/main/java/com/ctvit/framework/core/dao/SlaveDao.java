package com.ctvit.framework.core.dao;

import static org.springframework.util.Assert.isTrue;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiMap;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;

import com.ctvit.framework.core.dao.mybatis3.DefaultMultiValueMapResultHandler;
import com.ctvit.framework.core.dao.query.Query;

/**
 * 通用的Mybatis访问类
 * 
 * @author WCH
 * 
 */
public class SlaveDao extends DaoSupport {
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;
	private String dialect;

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSession = sqlSessionTemplate;
	}

	public SqlSession getSqlSession() {
		return this.sqlSession;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void checkDaoConfig() {
		isTrue(this.sqlSession != null && sqlSessionFactory != null && dialect != null, "Property 'sqlSession' 'sqlSessionFactory' 'dialect' are required");
	}

	/**
	 * 根据指定的Mybatis语句id，查询数据
	 * 
	 * @param statement
	 *            包含命名空间的mybatis delete语句id
	 * @param parameter
	 *            select语句的参数对象
	 */
	public <T> List<T> selectList(String statement, Object parameter) {
		return selectList(statement, parameter, RowBounds.DEFAULT);
	}

	/**
	 * 根据指定的Mybatis语句id，查询数据
	 * 
	 * @param statement
	 *            包含命名空间的mybatis delete语句id
	 * @param parameter
	 *            select语句的参数对象
	 * @param rowBounds
	 *            分页参数，用于指定取得数据记录的偏移量和记录数
	 * @return 包含java bean 对象的list
	 */
	public <T> List<T> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return getSqlSession().selectList(statement, parameter, rowBounds);
	}

	/**
	 * 根据指定的Mybatis语句id，查询一条数据记录
	 * 
	 * @param statement
	 *            包含命名空间的mybatis delete语句id
	 * @param parameter
	 *            select语句的参数对象
	 */
	public <T> T selectOne(String statement, Object parameter) {
		return getSqlSession().selectOne(statement, parameter);
	}

	/**
	 * 根据指定的Mybatis语句id，查询数据记录，并将数据结果集按照mapkey指定的属性封装成Map对象
	 * 
	 * @param statement
	 *            包含命名空间的mybatis delete语句id
	 * @param parameter
	 *            select语句的参数对象
	 * @param mapKey
	 *            返回的bean对象的属性名
	 * @return 以mapkey属性对应的内容为key，java bean为value的map对象
	 */
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return getSqlSession().selectMap(statement, parameter, mapKey, RowBounds.DEFAULT);
	}

	/**
	 * 根据指定的Mybatis语句id，查询数据记录，并将数据结果集按照mapkey指定的属性封装成Map对象
	 * 
	 * @param statement
	 *            包含命名空间的mybatis delete语句id
	 * @param parameter
	 *            select语句的参数对象
	 * @param mapKey
	 *            返回的bean对象的属性名
	 * @param rowBounds
	 *            分页参数，用于指定取得数据记录的偏移量和记录数
	 * @return 以mapkey属性对应的内容为key，java bean为value的map对象
	 */
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return getSqlSession().selectMap(statement, parameter, mapKey, rowBounds);
	}

	public MultiMap selectMultiValueMap(String statement, Object parameter, String mapKey) {
		return selectMultiValueMap(statement, parameter, mapKey, RowBounds.DEFAULT);
	}

	/**
	 * 根据查询条件查询数据记录，并将数据结果集按照mapkey指定的属性封装成Map对象
	 * 
	 * @param clazz
	 *            数据记录对应的Java bean类型
	 * @param query
	 *            查询条件
	 * @param mapKey
	 *            返回的bean对象的属性名
	 * @param includeBlob
	 *            是否返回blob属性
	 * @return 以mapkey属性对应的内容为key，java bean为value的map对象
	 */
	public <K, T> Map<K, T> selectMapByExample(Class<T> clazz, Query query, String mapKey, boolean includeBlob) {
		return selectMapByExample(clazz, query, mapKey, RowBounds.DEFAULT, includeBlob);
	}

	public MultiMap selectMultiValueMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {

		final List<?> list = selectList(statement, parameter, rowBounds);
		final DefaultMultiValueMapResultHandler<?, ?> mapResultHandler = new DefaultMultiValueMapResultHandler<Object, Object>(mapKey, getSqlSession().getConfiguration().getObjectFactory(), getSqlSession().getConfiguration().getObjectWrapperFactory());
		final DefaultResultContext context = new DefaultResultContext();
		for (Object o : list) {
			context.nextResultObject(o);
			mapResultHandler.handleResult(context);
		}
		MultiMap selectedMap = mapResultHandler.getMappedResults();
		return selectedMap;
	}

	/**
	 * 根据查询条件查询数据记录，并将数据结果集按照mapkey指定的属性封装成Map对象
	 * 
	 * @param clazz
	 *            数据记录对应的Java bean类型
	 * @param query
	 *            查询条件
	 * @param mapKey
	 *            返回的bean对象的属性名
	 * @param rowBounds
	 *            分页参数，用于指定取得数据记录的偏移量和记录数
	 * @param includeBlob
	 *            是否返回blob属性
	 * @return 以mapkey属性对应的内容为key，java bean为value的map对象
	 */
	public <K, T> Map<K, T> selectMapByExample(Class<T> clazz, Query query, String mapKey, RowBounds rowBounds, boolean includeBlob) {
		String statement;
		if (includeBlob) {
			statement = getStatement(clazz, "selectByExampleWithBLOBs");
		} else {
			statement = getStatement(clazz, "selectByExample");
		}
		return getSqlSession().selectMap(statement, query, mapKey, rowBounds);
	}

	public <K, T> Map<K, T> selectMapByExample(Class<T> clazz, Query query, String mapKey) {
		return selectMapByExample(clazz, query, mapKey, RowBounds.DEFAULT, false);
	}

	public <T> MultiMap selectMultiValueMapByExample(Class<T> clazz, Query query, String mapKey) {
		return selectMultiValueMapByExample(clazz, query, mapKey, RowBounds.DEFAULT, false);
	}

	public <T> MultiMap selectMultiValueMapByExample(Class<T> clazz, Query query, String mapKey, RowBounds rowBounds, boolean includeBlob) {
		String statement;
		if (includeBlob) {
			statement = getStatement(clazz, "selectByExampleWithBLOBs");
		} else {
			statement = getStatement(clazz, "selectByExample");
		}
		final List<?> list = selectList(statement, query, rowBounds);
		final DefaultMultiValueMapResultHandler<?, ?> mapResultHandler = new DefaultMultiValueMapResultHandler<Object, Object>(mapKey, getSqlSession().getConfiguration().getObjectFactory(), getSqlSession().getConfiguration().getObjectWrapperFactory());
		final DefaultResultContext context = new DefaultResultContext();
		for (Object o : list) {
			context.nextResultObject(o);
			mapResultHandler.handleResult(context);
		}
		MultiMap selectedMap = mapResultHandler.getMappedResults();
		return selectedMap;
	}

	/**
	 * 根据主键查询数据记录
	 * 
	 * @param clazz
	 *            数据记录对应的Java bean类型
	 * @param pk
	 *            主键
	 * @return java bean对象
	 */
	public <T> T selectByPrimaryKey(Class<T> clazz, Object pk) {
		String statement = getStatement(clazz, "selectByPrimaryKey");
		return getSqlSession().selectOne(statement, pk);
	}

	public <T> List<T> selectAll(Class<T> clazz) {
		return selectByExample(clazz, null, false);
	}

	public <T> List<T> selectByExample(Class<T> clazz, Query query, RowBounds rowBounds, boolean includeBlob) {
		String statement;
		if (includeBlob) {
			statement = getStatement(clazz, "selectByExampleWithBLOBs");
		} else {
			statement = getStatement(clazz, "selectByExample");
		}
		return getSqlSession().selectList(statement, query, rowBounds);
	}

	public <T> Integer countByExample(Class<T> clazz, Query query) {
		String statement = getStatement(clazz, "countByExample");

		return getSqlSession().selectOne(statement, query);
	}

	/**
	 * 根据查询条件查询数据记录，并将数据结果集按照mapkey指定的属性封装成Map对象
	 * 
	 * @param clazz
	 *            数据记录对应的Java bean类型
	 * @param query
	 *            查询条件
	 * @param includeBlob
	 *            是否返回blob属性
	 * @return 包含java bean的List对象
	 */
	public <T> T selectOneByExample(Class<T> clazz, Query query, boolean includeBlob) {
		List<T> result = selectByExample(clazz, query, RowBounds.DEFAULT, includeBlob);
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据查询条件查询数据记录，并将数据结果集按照mapkey指定的属性封装成Map对象
	 * 
	 * @param clazz
	 *            数据记录对应的Java bean类型
	 * @param query
	 *            查询条件
	 * @param includeBlob
	 *            是否返回blob属性
	 * @return 包含java bean的List对象
	 */
	public <T> List<T> selectByExample(Class<T> clazz, Query query, boolean includeBlob) {
		return selectByExample(clazz, query, RowBounds.DEFAULT, includeBlob);
	}

	protected String getStatement(Class<?> voclass, String methodName) {
		return getDefaultNameSpace(voclass) + "." + methodName;
	}

	protected String getDefaultNameSpace(Class<?> voclass) {
		return voclass.getName().replaceAll(".domain.", ".dao.sqlmap." + dialect + ".") + "Mapper";
	}
}
