/*
 * btvpms: com.dota.framework.domain.BeanPopulator.java Create On Jun 10, 2009 2:44:30 PM By Q-Wang
 * $Revision: 1.1 $
 */
package com.ctvit.framework.core.domain;

import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

/**
 * Bean == Mapת��.
 * 
 * �÷�:��ʹ��{@link BeanPopulatorFactory#getPopulater(Class)}ȡ��ʵ��.
 * @author Q-Wang
 */
public abstract class BeanPopulator<T> {
	public abstract Map<String, Object> toMap(Object bean);
	public abstract T toBean(T bean, Map<String,Object> value);
	
	/**
	 * Ĭ������ת����.
	 * 
	 * @param <C>
	 * @param target
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <C> C convert(Class<C> target, Object value) {
		if (value==null) {
			return null;
		}
		if (value.getClass().isAssignableFrom(target)) {
			return (C) value;
		}
		return (C) ConvertUtils.convert(value, target);
	}
	
	/**
	 * To int ת��.
	 * 
	 * @param value
	 * @return int value
	 */
	protected int toI(Object value) {
		if (value==null) return 0;
		if (value instanceof Number) return ((Number)value).intValue();
		if (value instanceof String) return Integer.parseInt((String)value);
		if (value instanceof Character) return ((Character)value).charValue();
		return (Integer)ConvertUtils.convert(value, Integer.class);
	}
	
	/**
	 * To long ת��.
	 * 
	 * @param value
	 * @return
	 */
	protected long toJ(Object value) {
		if (value==null) return 0;
		if (value instanceof Number) return ((Number)value).longValue();
		if (value instanceof String) return Long.parseLong((String)value);
		if (value instanceof Character) return ((Character)value).charValue();
		return (Long)ConvertUtils.convert(value, Long.class);
	}
	
	/**
	 * To float ת��.
	 * 
	 * @param value
	 * @return
	 */
	protected float toF(Object value) {
		if (value==null) return 0;
		if (value instanceof Number) return ((Number)value).floatValue();
		if (value instanceof String) return Float.parseFloat((String)value);
		return (Float)ConvertUtils.convert(value, Float.class);
	}
	
	/**
	 * To double ת��.
	 * 
	 * @param value
	 * @return
	 */
	protected double toD(Object value) {
		if (value==null) return 0;
		if (value instanceof Number) return ((Number)value).doubleValue();
		if (value instanceof String) return Double.parseDouble((String)value);
		return (Double)ConvertUtils.convert(value, Double.class);
	}
	
	/**
	 * To char ת��.
	 * 
	 * @param value
	 * @return
	 */
	protected char toC(Object value) {
		if (value==null) return 0;
		if (value instanceof Character) return ((Character)value).charValue();
//		if (value instanceof Number) return ((Number)value).intValue();
		return (Character)ConvertUtils.convert(value, Character.class);
	}
	
	/**
	 * To short ת��.
	 * 
	 * @param value
	 * @return
	 */
	protected short toS(Object value) {
		if (value==null) return 0;
		if (value instanceof Number) return ((Number)value).shortValue();
		if (value instanceof String) return Short.parseShort((String)value);
		return (Short)ConvertUtils.convert(value, Short.class);
	}
	
	/**
	 * To boolean ת��.
	 * 
	 * @param value
	 * @return
	 */
	protected boolean toZ(Object value) {
		if (value == null) return false;
		if (value instanceof Boolean) return ((Boolean)value).booleanValue();
		if (value instanceof Number) return ((Number)value).intValue()!=0;
		if (value instanceof String) return Boolean.parseBoolean((String)value);
		return (Boolean)ConvertUtils.convert(value, Boolean.class);
	}
	
	protected Integer toInteger(Object value) {
		return null;
	}
	
	protected Long toLong(Object value) {
		return null;
	}
	
	protected Short toShort(Object value) {
		return null;
	}
	
	protected Boolean toBoolean(Object value) {
		return null;
	}
	
	protected Float toFloat(Object value) {
		return null;
	}
	
	protected Double toDouble(Object value) {
		return null;
	}
	
	protected Character toCharacter(Object value) {
		return null;
	}
	
	protected String toString(Object value) {
		return null;
	}
}
