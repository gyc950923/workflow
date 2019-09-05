package com.online.workflow.util;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class DataGridModel {

	private int total = 0;

	private List rows = new ArrayList();

	public int getTotal() {
		return total;
	}

	public void setTotal(int i) {
		this.total = i;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

}
