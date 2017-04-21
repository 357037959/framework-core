package com.ctvit.framework.core.domain;

public class ReturnVO extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3436558275114875264L;
	public final static int SUCCESS=0;
	public final static int FAILURE=1;
	private int result;
	private String desc;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
