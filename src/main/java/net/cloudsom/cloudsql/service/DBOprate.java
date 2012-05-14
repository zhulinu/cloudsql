package net.cloudsom.cloudsql.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface DBOprate {
	//获取数据库用户名称
	public List<String> getUsers();
	
	//获取数据库schema，在很多情况下和等同于获取用户。
	public List<String> getSchema();
	
	//获取用户下的表
	public List<String> getTablesbyUser(String userName);
	
	//获取用户下的视图
	public List<String> getViewsByUser(String userName);
}