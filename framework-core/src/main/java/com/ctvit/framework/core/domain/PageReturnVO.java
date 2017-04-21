package com.ctvit.framework.core.domain;

import java.util.List;

public class PageReturnVO<T> {
	private Integer total;
	private List<T> rows;
	public PageReturnVO(Integer total,List<T> rows){
		this.total=total;
		this.rows=rows;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
