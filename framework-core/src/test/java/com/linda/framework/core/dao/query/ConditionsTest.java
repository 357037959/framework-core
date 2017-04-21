package com.linda.framework.core.dao.query;

import org.junit.Test;

import com.ctvit.framework.core.dao.query.Conditions;

public class ConditionsTest {

	@Test
	public final void testGetFlatConditions1() {
		Conditions cond = Conditions
				.and()
				.equalTo("F1", "a")
				.like("F1", "b")
				.greaterThan("F2", 2)
				.addConditions(
						Conditions
								.or()
								.equalTo("F3", 4)
								.equalTo("F3", 5)
								.addConditions(
										Conditions.and().equalTo("F6", 1)
												.equalTo("F7", 4)))
				.lessThan("F4", "abc");
		System.out.println(cond);
	}

	@Test
	public final void testGetFlatConditions2() {
		Conditions cond = Conditions
				.and()
				.addConditions(
						Conditions
								.or()
								.equalTo("F3", 4)
								.equalTo("F3", 5)
								.addConditions(
										Conditions.and().equalTo("F6", 1)
												.equalTo("F7", 4)))
				.lessThan("F4", "abc").equalTo("F1", "a").greaterThan("F2", 2);
		System.out.println(cond);
	}
	@Test
	public final void testGetFlatConditions3() {
		Conditions cond = Conditions
				.and()
				.lessThan("F4", "abc");
		System.out.println(cond);
	}
	@Test
	public final void testGetFlatConditions4() {
		Conditions cond = Conditions
				.and();
		System.out.println(cond);
	}
}
