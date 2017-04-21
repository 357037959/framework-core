/*
 * $Header: /home/cvsroot/cri/rms/03src/java/CRIRMS/framework/core/com/dota/framework/domain/BaseBean.java,v 1.1 2011/09/14 08:53:31 wangchen Exp $
 */
package com.ctvit.framework.core.domain;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 *
 *
 */
public class BaseBean implements Serializable {

	private static final long serialVersionUID = 3663687482168313896L;

//	private BeanPopulator<BaseBean> populator;
//
//	public BaseBean() {
//		this.populator = (BeanPopulator<BaseBean>) BeanPopulatorFactory.getPopulater(getClass());
//	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**   contextRoot
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Map<String, Object> toMap() {
		return BeanPopulatorFactory.getPopulater(getClass()).toMap(this);
	}

	@SuppressWarnings("unchecked")
	public BaseBean toBean(Map<String,Object> map) {
		return ((BeanPopulator<BaseBean>)BeanPopulatorFactory.getPopulater(getClass())).toBean(this, map);
	}
}
