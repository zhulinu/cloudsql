package net.cloudsom.cloudsql.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.cloudsom.cloudsql.service.OracleDBOperate;
import net.cloudsom.cloudsql.service.bean.DatabaseInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SqlController {
	@Autowired
	private OracleDBOperate oracle;
//	@Autowired
//	private DatabaseInfo dbInfo;

	@RequestMapping(value = "/home")
	public String setDb(HttpSession session) {
		DatabaseInfo dbInfo = (DatabaseInfo)session.getAttribute("dbInfo");
		if ( dbInfo != null ){
			oracle.setDbInfo(dbInfo);
		}
		return "home";
	}
	
//	@RequestMapping(value = "/home", method = RequestMethod.POST)
//	public ModelAndView login(@RequestParam(value = "dbInfo") DatabaseInfo dbInfo) {
//		System.out.println("ip:"+dbInfo.getDbip()+"port:"+dbInfo.getDbport()+"user:"+dbInfo.getUser());
//		return new ModelAndView("home", "dbInfo", dbInfo);
//	}
	/**
	 * 
	 * @param sql 传入的sql语句
	 * @return 返回json格式的数据
	 * 例如："[{\"RESULTS\":{\"COLUMNS\":[\"aa\",\"type\",\"details\"],\"DATA\":[[\"123\",\"MAIL\",\"details\"],[\"234\",\"OLD\",\"details\"]]  },\"EXECUTIONTIME\":3,\"SUCCEEDED\":\"SUCCEEDED\"}]";
	 */
	@RequestMapping(value = "/query/sql")
	@ResponseBody
	public String helloWorld(@RequestParam(value = "sql", required = true) String sql) {
		String message = null;
		  try{
			  message = oracle.toJson(sql);
		  } catch(Exception e){
			  e.printStackTrace();
		  }
		  String retMessage = null;
		  try{
			  retMessage = new String(message.getBytes("UTF-8"), "ISO-8859-1");
		  } catch( Exception e ){
			  e.printStackTrace();
		  }
		  return retMessage;
	}
	
	@RequestMapping(value = "/query/sqlusers")
	@ResponseBody
	public String getUsers(){
		String retUsers = "";
		
		List<String> allUsers = oracle.getUsers();
		for (String user:allUsers){
			retUsers += "<option value=\""+user+"\">"+user+"</option>";
		}
		
		return retUsers;
	}
	
	@RequestMapping(value = "/query/allViews")
	@ResponseBody
	public String getView(@RequestParam(value = "user", required = true) String user){
		String retViews = "";
		List<String> allViews = oracle.getViewsByUser(user.toUpperCase());
		for (String view:allViews){
			retViews += "<p>"+view;
		}
		return retViews;
	}
	
	@RequestMapping(value = "/query/allTables")
	@ResponseBody
	public String getTables(@RequestParam(value = "user", required = true) String user){
		String retTables = "";
		List<String> allTables = oracle.getTablesbyUser(user.toUpperCase());
		for (String table:allTables){
			retTables += "<p>"+table;
		}
		return retTables;
	}
}
