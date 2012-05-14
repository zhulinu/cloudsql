package net.cloudsom.cloudsql.service.bean;

import org.springframework.stereotype.Service;
/**
 * DatabaseInfo为数据库连接信息，这里主要针对oracle，后面在扩充到其他数据库。
 * @author zhulin
 */
@Service
public class DatabaseInfo {
	private String dbip;
	private int    dbport;
	private String dbsid;
	private String user;
	private String password;
	
	public String getDbip() {
		return dbip;
	}
	public void setDbip(String dbip) {
		this.dbip = dbip;
	}
	public int getDbport() {
		return dbport;
	}
	public void setDbport(int dbport) {
		this.dbport = dbport;
	}
	public String getDbsid() {
		return dbsid;
	}
	public void setDbsid(String dbsid) {
		this.dbsid = dbsid;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
