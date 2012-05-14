package net.cloudsom.cloudsql.service.bean;

import org.springframework.stereotype.Service;

@Service
public class SqlColumn {
	private String columnName;
	private String columnType;
	private int length;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
