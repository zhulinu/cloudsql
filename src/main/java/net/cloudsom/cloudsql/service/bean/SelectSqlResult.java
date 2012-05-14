package net.cloudsom.cloudsql.service.bean;

import java.util.List;
/**
 * DatabaseInfo为数据库查询内容信息，包括列和值。
 * @author zhulin
 */
public class SelectSqlResult {
	private List<String> columns;
	private List<List<Object>> data;
	
	public List<List<Object>> getData() {
		return data;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public void setData(List<List<Object>> data) {
		this.data = data;
	}
}
