package com.ctvit.framework.core.dao.query;

public class Query {
	protected OrderPart orderBy;
	protected boolean distinct;
	protected Conditions conditions = null;

	public static Query create() {
		return new Query();
	}

	public OrderPart getOrderBy() {
		return orderBy;
	}

	public Query setOrderBy(OrderPart orderByClause) {
		this.orderBy = orderByClause;
		return this;
	}

	public String getOrderByClause() {
		if (this.orderBy != null) {
			return this.orderBy.getOrderByClause();
		} else {
			return null;
		}
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public Query setConditions(Conditions value) {
		this.conditions = value;
		return this;
	}

	public Conditions getConditions() {
		return conditions;
	}
}
