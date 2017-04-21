package com.ctvit.framework.core.dao.query;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Condition {
	private String prefix = "";// and or
	private String condition;
	private Object value;
	private Object secondValue;
	private boolean noValue = false;
	private boolean singleValue = false;
	private boolean betweenValue = false;
	private boolean listValue = false;
	private boolean conditionValue = false;

	protected Condition(String condition) {
		super();
		this.condition = condition;
		this.noValue = true;
	}

	protected Condition(String condition, Object value) {
		super();
		this.condition = condition;
		this.value = value;
		if (value instanceof List<?>) {
			this.listValue = true;
		} else {
			this.singleValue = true;
		}
	}

	protected Condition(String condition, Object value, Object secondValue) {
		super();
		this.condition = condition;
		this.value = value;
		this.secondValue = secondValue;
		this.betweenValue = true;
	}

	protected Condition(Conditions conditions) {
		this.value = conditions;
		this.conditionValue = true;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isConditionValue() {
		return conditionValue;
	}

	public String getCondition() {
		return condition;
	}

	public Object getValue() {
		return value;
	}

	public Object getSecondValue() {
		return secondValue;
	}

	public boolean isNoValue() {
		return noValue;
	}

	public boolean isSingleValue() {
		return singleValue;
	}

	public boolean isBetweenValue() {
		return betweenValue;
	}

	public boolean isListValue() {
		return listValue;
	}
	@Override
	public String toString() {
		if(noValue){
			return prefix+" "+condition;
		}
		if(singleValue){
			return prefix+" "+condition+" "+value;
		}
		if(betweenValue){
			return prefix+" "+condition+" "+value+" and "+secondValue;
		}

		if(listValue){
			return prefix+" "+condition+" "+StringUtils.join((List<?>)value, ",");
		}
		if(conditionValue){
			return prefix+" ("+value+")";
		}
		
		return super.toString();
	}
}
