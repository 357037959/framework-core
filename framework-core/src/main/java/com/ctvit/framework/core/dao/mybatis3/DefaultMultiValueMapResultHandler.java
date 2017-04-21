package com.ctvit.framework.core.dao.mybatis3;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class DefaultMultiValueMapResultHandler<K, V> implements ResultHandler {

	private final MultiMap mappedResults;
	private final String mapKey;
	private final ObjectFactory objectFactory;
	private final ObjectWrapperFactory objectWrapperFactory;

	public DefaultMultiValueMapResultHandler(String mapKey, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
		this.objectFactory = objectFactory;
		this.objectWrapperFactory = objectWrapperFactory;
		this.mappedResults = new MultiValueMap();
		this.mapKey = mapKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleResult(ResultContext context) {
		// TODO is that assignment always true?
		final V value = (V) context.getResultObject();
		final MetaObject mo = MetaObject.forObject(value, objectFactory, objectWrapperFactory);
		// TODO is that assignment always true?
		final K key = (K) mo.getValue(mapKey);
		mappedResults.put(key, value);
	}

	public MultiMap getMappedResults() {
		return mappedResults;
	}
}
