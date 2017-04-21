package com.ctvit.framework.core.dao.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Conditions {
	protected String separator = "and";
	protected List<Condition> children = new ArrayList<Condition>();

	public List<Condition> getChildren() {
		return children;
	}

	public static Conditions and() {
		Conditions item = new Conditions();
		return item;
	}

	public static Conditions or() {
		Conditions item = new Conditions();
		item.separator = "or";
		return item;
	}

	public String getSeparator() {
		return separator;
	}

	public boolean isValid() {
		return children.size() > 0;
	}

	public Conditions addCondition(String condition) {
		if (condition == null) {
			return this;
		}
		Condition cond = new Condition(condition);

		children.add(cond);
		return this;
	}

	public Conditions addCondition(String condition, Object value) {
		if (value == null) {
			return this;
		}
		Condition cond = new Condition(condition, value);
		children.add(cond);
		return this;
	}

	public Conditions addCondition(String fieldName, String op, Object value) {
		if (value == null) {
			return this;
		}
		Condition cond = new Condition(fieldName + " " + op, value);

		children.add(cond);
		return this;
	}

	public Conditions addConditions(Conditions conditions) {
		if (conditions != null && conditions.isValid()) {
			Condition cond = new Condition(conditions);

			children.add(cond);
		}
		return this;
	}

	public Conditions isNull(String fieldName) {
		addCondition(fieldName + " is null");
		return this;
	}

	public Conditions isNotNull(String fieldName) {
		addCondition(fieldName + " is not null");
		return this;
	}

	public Conditions equalTo(String fieldName, Object value) {
		addCondition(fieldName, "=", value);
		return this;
	}

	public Conditions notEqualTo(String fieldName, Object value) {
		addCondition(fieldName, "<>", value);
		return this;
	}

	public Conditions greaterThan(String fieldName, Object value) {
		addCondition(fieldName, ">", value);
		return this;
	}

	public Conditions greaterThanOrEqualTo(String fieldName, Object value) {
		addCondition(fieldName, ">=", value);
		return this;
	}

	public Conditions lessThan(String fieldName, Object value) {
		addCondition(fieldName, "<", value);
		return this;
	}

	public Conditions lessThanOrEqualTo(String fieldName, Object value) {
		addCondition(fieldName, "<=", value);
		return this;
	}

	public Conditions in(String fieldName, List<?> values) {
		addCondition(fieldName, "in", values);
		return this;
	}

	public Conditions notIn(String fieldName, List<?> values) {
		addCondition(fieldName, "not in", values);
		return this;
	}

	public Conditions between(String fieldName, Object value1, Object value2) {
		if (value1 == null || value2 == null) {
			return this;
		}
		Condition cond = new Condition(fieldName + " between", value1, value2);

		children.add(cond);
		return this;
	}

	public Conditions notBetween(String fieldName, Object value1, Object value2) {
		if (value1 == null || value2 == null) {
			return this;
		}
		Condition cond = new Condition(fieldName + " not between", value1, value2);

		children.add(cond);
		return this;
	}

	public Conditions like(String fieldName, String value) {
		addCondition(fieldName, "like", value);
		return this;
	}

	public Conditions like5(String fieldName, String value) {
		if (value == null) {
			return this;
		}
		addCondition(fieldName, "like", "%".concat(value).concat("%"));
		return this;
	}

	public Conditions like9(String fieldName, String value) {
		if (value == null) {
			return this;
		}
		addCondition(fieldName, "like", "_".concat(value).concat("_"));
		return this;
	}

	public Conditions like(String fieldName, String value, String prefix, String suffix) {
		if (value == null) {
			return this;
		}
		addCondition(fieldName, "like", StringUtils.defaultString(prefix).concat(value).concat(StringUtils.defaultString(suffix)));
		return this;
	}

	public Conditions notLike(String fieldName, String value) {
		addCondition(fieldName, "not like", value);
		return this;
	}

	public Conditions notLike5(String fieldName, String value) {
		if (value == null) {
			return this;
		}
		addCondition(fieldName, "not like", "%".concat(value).concat("%"));
		return this;
	}

	public Conditions notLike9(String fieldName, String value) {
		if (value == null) {
			return this;
		}
		addCondition(fieldName, "not like", "_".concat(value).concat("_"));
		return this;
	}

	public Conditions notLike(String fieldName, String value, String prefix, String suffix) {
		if (value == null) {
			return this;
		}
		addCondition(fieldName, "not like", StringUtils.defaultString(prefix).concat(value).concat(StringUtils.defaultString(suffix)));
		return this;
	}

	@Override
	public String toString() {
		if (isValid()) {
			// StringBuilder builder = new StringBuilder();
			// int index = 0;
			// for (Condition item : children) {
			// if (index == 0) {
			// builder.append(item);
			// } else {
			// builder.append(" " + separator + " " + item);
			// }
			// index++;
			// }
			// return builder.toString();
			List<Condition> conds = this.getFlatConditions();
			StringBuilder builder = new StringBuilder();
			for (Condition item : conds) {
				builder.append(" " + item);
			}
			return builder.toString();
		}
		return "";
	}

	/**
	 * 为了配合MyBatis 映射文件中的Example_Where_Clause语句，将嵌套的条件展开成平面结构
	 * 
	 * @return
	 */
	public List<Condition> getFlatConditions() {
		List<Condition> answer = new ArrayList<Condition>();
		int index = 0;
		for (Condition item : children) {
			if (item.isConditionValue()) {
				Condition open = new Condition("(");
				if (index > 0) {
					open.setPrefix(separator);
				}
				answer.add(open);
				//
				answer.addAll(((Conditions) item.getValue()).getFlatConditions());
				//
				Condition close = new Condition(")");
				answer.add(close);
			} else {
				if (index > 0) {
					item.setPrefix(separator);
				}
				answer.add(item);
			}

			index++;
		}
		return answer;
	}
}
