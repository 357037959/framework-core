package com.ctvit.framework.core.dao.mybatis3.plugin;

import java.sql.Connection;
import java.util.Properties;



import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;

import com.ctvit.framework.core.dao.mybatis3.dialect.Dialect;

/**
 * MyBatis���dialect��ҳ(��Ҫhibernate֧��):��ݿ��ҳ
 * 
 * @author
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationInterceptor implements Interceptor {

	private final static Log log = LogFactory
			.getLog(PaginationInterceptor.class);

	private Dialect dialect;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql();
		MetaObject metaStatementHandler = MetaObject.forObject(
				statementHandler, new DefaultObjectFactory(),
				new DefaultObjectWrapperFactory());
		RowBounds rowBounds = (RowBounds) metaStatementHandler
				.getValue("delegate.rowBounds");
		if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
			return invocation.proceed();
		} else if (rowBounds.getLimit() > 0
				&& rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT) {

			String originalSql = (String) metaStatementHandler
					.getValue("delegate.boundSql.sql");

			String desSql = dialect.getLimitString(originalSql,
					rowBounds.getOffset(), rowBounds.getLimit());

			metaStatementHandler.setValue("delegate.boundSql.sql", desSql);

//			String addSql = desSql.replace(originalSql, "");
//
//			Pattern pattern = Pattern.compile("[?]");
//			Matcher matcher = pattern.matcher(originalSql);
//			int size = 0;
//			while (matcher.find()) {
//				size++;
//			}
//			Configuration configuration = (Configuration)metaStatementHandler.getValue("delegate.configuration");
//			if (size == 1) {
//				ParameterMapping.Builder builder = new ParameterMapping.Builder(
//						configuration, "limit", Integer.class);
//				boundSql.getParameterMappings().add(builder.build());
//				boundSql.setAdditionalParameter("limit", rowBounds.getLimit());
//			}
//			if (size == 2) {
//
//				ParameterMapping.Builder builder = new ParameterMapping.Builder(
//						configuration, "offset", Integer.class);
//				boundSql.getParameterMappings().add(builder.build());
//				boundSql.setAdditionalParameter("offset", rowBounds.getOffset());
//
//				builder = new ParameterMapping.Builder(configuration, "limit",
//						Integer.class);
//				boundSql.getParameterMappings().add(builder.build());
//				boundSql.setAdditionalParameter("limit", rowBounds.getLimit());
//			}

			metaStatementHandler.setValue("delegate.rowBounds.offset",
					RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit",
					RowBounds.NO_ROW_LIMIT);

			if (log.isDebugEnabled()) {
				log.debug("��ɷ�ҳSQL : " + boundSql.getSql());
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);

	}

	@Override
	public void setProperties(Properties properties) {
		String dialectClass = properties.getProperty("dialectClass");
		try {
			dialect = (Dialect) Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(
					"cannot create dialect instance by dialectClass:"
							+ dialectClass, e);
		}
		log.debug(PaginationInterceptor.class.getSimpleName() + ".dialect="
				+ dialectClass);
	}
}
