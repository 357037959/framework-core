package com.ctvit.framework.core.dao;

import static org.springframework.util.Assert.isTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiMap;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;

import com.ctvit.framework.core.dao.mybatis3.DefaultMultiValueMapResultHandler;
import com.ctvit.framework.core.dao.query.Conditions;
import com.ctvit.framework.core.dao.query.Query;

/**
 * 通用的Mybatis访问类
 * 
 * @author WCH
 * 
 */
public class GenericDao extends DaoSupport {
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
	 * 根据指定的Mybatis语句id，插入数据
	 * 
	 * @param statement
	 *            包含命名空间的mybatis insert语句id
	 * @param parameter
	 *            insert语句的参数对象
	 * @return 插入的记录数
	 */
	public int insert(String statement, Object parameter) {
		if (parameter != null) {
			return getSqlSession().insert(statement, parameter);
		} else {
			return getSqlSession().insert(statement);
		}
	}

	/**
	 * 将bean对象插入数据库，按照命名规范将bean对象对应到mybatis语句id
	 * 
	 * @param bean
	 * @return 插入的记录数
	 */
	public <T> int insert(T bean) {
		String statement = getStatement(bean.getClass(), "insert");
		return getSqlSession().insert(statement, bean);
	}

	/**
	 * 将bean对象插入数据库，按照命名规范将bean对象对应到mybatis语句id
	 * 
	 * @param bean
	 * @return 插入的记录数
	 */
	public <T> int insertSelective(T bean) {
		String statement = getStatement(bean.getClass(), "insertSelective");
		return getSqlSession().insert(statement, bean);
	}

	/**
	 * 批量插入bean对象
	 * 
	 * @param beans
	 */
	public <T> void batchInsert(List<T> beans) {
		SqlSession session = null;
		try {
			session = getSqlSessionFactory().openSession(ExecutorType.BATCH);
			for (T item : beans) {
				String statement = getStatement(item.getClass(), "insert");
				session.insert(statement, item);
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	/**
	 * 批量更新bean对象
	 * 
	 * @param beans
	 * @param includeBlob
	 *            是否更新blob字段
	 */
	public <T> void batchUpdate(List<T> beans, boolean includeBlob) {
		if (beans == null || beans.size() == 0) {
			return;
		}
		String statement;
		if (includeBlob) {
			statement = getStatement(beans.get(0).getClass(), "updateByPrimaryKeyWithBLOBs");
		} else {
			statement = getStatement(beans.get(0).getClass(), "updateByPrimaryKey");
		}
		SqlSession session = null;
		try {
			session = getSqlSessionFactory().openSession(ExecutorType.BATCH);
			for (T item : beans) {
				session.update(statement, item);
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	/**
	 * 批量更新bean对象，仅更新bean对象中非空属性
	 * 
	 * @param beans
	 */
	public <T> void batchUpdateSelective(List<T> beans) {
		if (beans == null || beans.size() == 0) {
			return;
		}
		String statement;
		statement = getStatement(beans.get(0).getClass(), "updateByExampleSelective");
		SqlSession session = null;
		try {
			session = getSqlSessionFactory().openSession(ExecutorType.BATCH);
			for (T item : beans) {
				session.update(statement, item);
			}
			session.commit();
		} finally {
			session.close();
		}
	}

	/**
	 * 根据指定的Mybatis语句id，更新数据
	 * 
	 * @param statement
	 *            包含命名空间的mybatis insert语句id
	 * @param parameter
	 *            update语句的参数对象
	 * @return 更新的记录数
	 */
	public int update(String statement, Object parameter) {
		if (parameter != null) {
			return getSqlSession().update(statement, parameter);
		} else {
			return getSqlSession().update(statement);
		}
	}

	/**
	 * 根据主键更新bean对象，仅更新bean中非空属性
	 * 
	 * @param bean
	 * @return 更新的记录数
	 */
	public <T> int updateByPrimaryKeySelective(T bean) {
		String statement;

		statement = getStatement(bean.getClass(), "updateByPrimaryKeySelective");
		return getSqlSession().insert(statement, bean);
	}

	/**
	 * 根据主键更新bean对象
	 * 
	 * @param bean
	 * @param includeBlob
	 *            是否更新blob属性
	 * @return 更新的记录数
	 */
	public <T> int updateByPrimaryKey(T bean, boolean includeBlob) {
		String statement;
		if (includeBlob) {
			statement = getStatement(bean.getClass(), "updateByPrimaryKeyWithBLOBs");
		} else {
			statement = getStatement(bean.getClass(), "updateByPrimaryKey");
		}
		return getSqlSession().update(statement, bean);
	}

	/**
	 * 根据查询条件更新bean对象，仅更新bean中非空属性
	 * 
	 * @param bean
	 * @param conditions
	 *            查询条件
	 * @return 更新的记录数
	 */
	public <T> int updateByExampleSelective(T bean, Conditions conditions) {
		String statement;
		statement = getStatement(bean.getClass(), "updateByExampleSelective");
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("record", bean);
		parameter.put("conditions", conditions);
		return getSqlSession().update(statement, parameter);
	}

	/**
	 * 根据查询条件更新bean对象
	 * 
	 * @param bean
	 * @param conditions
	 * @param includeBlob
	 *            是否更新blob属性
	 * @return 更新的记录数
	 */
	public <T> int updateByExample(T bean, Conditions conditions, boolean includeBlob) {
		String statement;
		if (includeBlob) {
			statement = getStatement(bean.getClass(), "updateByExample");
		} else {
			statement = getStatement(bean.getClass(), "updateByExampleWithBLOBs");
		}
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("record", bean);
		parameter.put("conditions", conditions);
		return getSqlSession().update(statement, parameter);
	}

	/**
	 * 根据指定的Mybatis语句id，删除数据
	 * 
	 * @param statement
	 *            包含命名空间的mybatis delete语句id
	 * @param parameter
	 *            delete语句的参数对象
	 */
	public int delete(String statement, Object parameter) {
		if (parameter != null) {
			return getSqlSession().delete(statement, parameter);
		} else {
			return getSqlSession().delete(statement);
		}
	}

	/**
	 * 根据pk删除数据记录
	 * 
	 * @param clazz
	 * @param pk
	 * @return 删除的记录数
	 */
	public <T> int deleteByPrimaryKey(Class<T> clazz, Object pk) {
		String statement = getStatement(clazz, "deleteByPrimaryKey");
		return getSqlSession().delete(statement, pk);
	}

	/**
	 * 根据查询条件删除数据记录
	 * 
	 * @param clazz
	 * @param conditions
	 * @return 删除的记录数
	 */
	public <T> int deleteByExample(Class<T> clazz, Conditions conditions) {
		String statement = getStatement(clazz, "deleteByExample");
		HashMap<String, Object> parameter = null;
		if (conditions != null) {
			parameter = new HashMap<String, Object>();
			parameter.put("conditions", conditions);
		}
		return getSqlSession().delete(statement, parameter);
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
