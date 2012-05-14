package net.cloudsom.cloudsql.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.cloudsom.cloudsql.service.bean.DatabaseInfo;
import net.cloudsom.cloudsql.service.bean.SelectSqlJson;
import net.cloudsom.cloudsql.service.bean.SelectSqlResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.JsonMapper;
/**
 * OracleDBOperate为oracle的操作类继承DBOprate接口。
 * @author zhulin
 */
@Service
public class OracleDBOperate implements DBOprate {
	private static Logger logger = LoggerFactory.getLogger(OracleDBOperate.class);
	
	private static JsonMapper mapper = JsonMapper.buildNonDefaultMapper();
//	private List<String> sqlColumns = new ArrayList<String>();
	
	@Autowired
	private DbConnectPools dbConnectPools;
	@Autowired
	private DatabaseInfo dbInfo;
	
	private JdbcTemplate getJdbcTemplate( ){
		return dbConnectPools.getJdbcTemplate(dbInfo);
	}

	public DatabaseInfo getDbInfo() {
		return dbInfo;
	}

	public void setDbInfo(DatabaseInfo dbInfo) {
		this.dbInfo = dbInfo;
	}

	public List<String> getUsers() {
		String sql = "select username from sys.all_users order by username";
		final List<String> allUser = new ArrayList<String>();
		getJdbcTemplate().query(sql,new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				allUser.add(rs.getString("username").toString());
			}
		});
		logger.info("allUser:"+allUser);
		return allUser;
	}

	public List<String> getUsers(DatabaseInfo dbInfo ) {
		// TODO Auto-generated method stub
		String sql = "select username from sys.all_users order by username";
		final List<String> allUser = new ArrayList<String>();
		getJdbcTemplate().query(sql,new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				allUser.add(rs.getString("username").toString());
			}
		});

		return allUser;
	}
	
	@Override
	public List<String> getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTablesbyUser(String userName) {
		String sql = "select table_name from sys.all_tables where owner='"+userName+"' order by table_name";
		final List<String> allTable = new ArrayList<String>();
		getJdbcTemplate().query(sql,new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				allTable.add(rs.getString("table_name").toString());
			}
		});

		logger.info("allTable:"+allTable);
		return allTable;
	}

	@Override
	public List<String> getViewsByUser(String userName) {
		String sql = "select view_name from sys.all_views where owner='"+userName+"' order by view_name";
		final List<String> allView = new ArrayList<String>();
		getJdbcTemplate().query(sql,new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				allView.add(rs.getString("view_name").toString());
			}
		});

		logger.info("allView:"+allView);
		return allView;
	}
	
	/**
	 * 根据sql语句获取查询sql结构。
	 * @param sql
	 * @return
	 */
	public List<String> querySqlRowSet( String sql ){
		List<String> sqlColumns = new ArrayList<String>();
		SqlRowSetMetaData rsmd = getJdbcTemplate().queryForRowSet(sql).getMetaData();
		for ( int i=1 ;i <= rsmd.getColumnCount(); i++ ){
			sqlColumns.add(rsmd.getColumnName(i));
		}
		return sqlColumns;
	}
	
	public List<List<Object>> querySql(String sql){
		// TODO 要对sql进行校验
		if ( sql == null || sql.length() == 0 ){
			return null;
		}
		
		final List<List<Object>> allDate = new ArrayList<List<Object>>();
		getJdbcTemplate().query(sql,new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				List<Object> oneDate = new ArrayList<Object>();
				ResultSetMetaData rsmd = rs.getMetaData();
				int iColumnCount = rsmd.getColumnCount();
				for ( int i=1 ;i <= iColumnCount; i++ ){
					oneDate.add(rs.getObject(i));
				}
				allDate.add(oneDate);
			}
		});
		return allDate;
	}
	
	/**
	 * 根据查询select的sql语句得到结果集，同时得到sql语句的列名称。
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public String toSelectJson( String sql ) {
		SelectSqlResult sqlResult = new SelectSqlResult();
		SelectSqlJson sqlJson = new SelectSqlJson();
		String beanString = null;
		List<SelectSqlJson> sqlJsons = new ArrayList<SelectSqlJson>();
		
		sqlResult.setColumns(querySqlRowSet(sql));
		sqlResult.setData(querySql(sql));	
		
		sqlJson.setRESULTS(sqlResult);
		sqlJson.setEXECUTIONTIME(10);
		sqlJson.setSUCCEEDED("SUCCEEDED");
		sqlJsons.add(sqlJson);
		try{
			beanString = mapper.toJson(sqlJsons);
			logger.info("Bean:" + beanString);
		} catch ( Exception e ){
			e.printStackTrace();
		}
		return beanString;
	}
	
	/**
	 * 根据执行非select的sql语句得到执行结果。
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public String toExecJson( String sql )  {
		SelectSqlResult sqlResult = new SelectSqlResult();
		SelectSqlJson sqlJson = new SelectSqlJson();
		
		String beanString = null;
		String sqlExec = "success";
		
		List<SelectSqlJson> sqlJsons = new ArrayList<SelectSqlJson>();
		List<Object> oneDate = new ArrayList<Object>();
		List<List<Object>> allDate = new ArrayList<List<Object>>();
		List<String> sqlColumns = new ArrayList<String>();
		
		try{
			getJdbcTemplate().execute(sql);
		} catch (DataAccessException e ){
			sqlExec = "error" + e.getMessage();
			e.printStackTrace();
		} catch ( Exception e ){
			e.printStackTrace();
		}
		sqlColumns.clear();
		sqlColumns.add("exec result");
		
		oneDate.add(sqlExec);
		allDate.add(oneDate);
		
		sqlResult.setData(allDate);
		sqlResult.setColumns(sqlColumns);
		
		sqlJson.setRESULTS(sqlResult);
		sqlJson.setEXECUTIONTIME(10);
		sqlJson.setSUCCEEDED("SUCCEEDED");
		sqlJsons.add(sqlJson);
		beanString = mapper.toJson(sqlJsons);
		logger.info("Bean:" + beanString);
		
		return beanString;
	}
	
	/**
	 * 根据sql语句得到结果集。
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public String toJson(String sql) throws Exception {
		logger.info("sql:"+sql);
		String retString = "";
		String sqlAnalyse = sql.trim();
		//如果是oracle语句中带；号，要把分号先过滤掉。
		if ( sqlAnalyse.substring(sqlAnalyse.length()-1).contains(";") ){
			sqlAnalyse = sqlAnalyse.substring(0, sqlAnalyse.length()-1 );
		}
		if ( sqlAnalyse.toUpperCase().startsWith("SELECT")){
			retString = toSelectJson( sqlAnalyse );
		} else {
			retString = toExecJson( sqlAnalyse );
		}
		return retString;
	}
	
	public static void main(String[] args) {
		OracleDBOperate dbOp = new OracleDBOperate();
		dbOp.getUsers();
	}
}
