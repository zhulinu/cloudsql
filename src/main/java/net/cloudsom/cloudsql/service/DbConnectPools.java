package net.cloudsom.cloudsql.service;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.cloudsom.cloudsql.service.bean.DatabaseInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

@Service
public class DbConnectPools {
	private static Logger logger = LoggerFactory.getLogger(DbConnectPools.class);
	private Map<String,DataSource> dbConnectPools = new HashMap<String,DataSource>();

	public Map<String, DataSource> getDbConnectPools() {
		return dbConnectPools;
	}

	public void setDbConnectPools(Map<String, DataSource> dbConnectPools) {
		this.dbConnectPools = dbConnectPools;
	}
	
	public void addDbConnect( DatabaseInfo dbInfo ){
		String dbName = dbInfo.getDbip()+dbInfo.getDbport()+dbInfo.getDbsid();
		if ( dbConnectPools.get(dbName) == null ){
			String driverName = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@"+dbInfo.getDbip()+":"+dbInfo.getDbport()+":"+dbInfo.getDbsid();
			String userName = dbInfo.getUser();
			String password = dbInfo.getPassword();
			DriverManagerDataSource ds = new DriverManagerDataSource();
			ds.setDriverClassName(driverName);
			ds.setUrl(url);
			ds.setUsername(userName);
			ds.setPassword(password);
			logger.info("add DB:"+"ip:"+dbInfo.getDbip()+"port:"+dbInfo.getDbport()+"user:"+dbInfo.getUser());
			dbConnectPools.put(dbName, ds);
		}
	}
	
	public DataSource getDataSource( DatabaseInfo dbInfo ){
		String dbName = dbInfo.getDbip()+dbInfo.getDbport()+dbInfo.getDbsid();
		if ( dbConnectPools.get(dbName) == null ){
			addDbConnect( dbInfo );
		}
		return dbConnectPools.get(dbName);
	}
	
	//获取JdbcTemplate
	public JdbcTemplate getJdbcTemplate(DatabaseInfo dbInfo){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(getDataSource(dbInfo));
		return jdbcTemplate;
	}
}
