package com.ctvit.framework.core.dao.query;

public class OrderPart {
	private String orderByClause="";
	public static OrderPart create() {
		return new OrderPart();
	}

	public OrderPart asc(String fieldName) {
		if(orderByClause.length()>0){
			orderByClause=","+fieldName+" ASC";
		}else{
			orderByClause=fieldName+" ASC";
		}
		return this;
	}

	public OrderPart desc(String fieldName) {
		if(orderByClause.length()>0){
			orderByClause=","+fieldName+" DESC";
		}else{
			orderByClause=fieldName+" DESC";
		}
		return this;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderStr(String orderByClause) {
		this.orderByClause = orderByClause;
	}
}
