package net.cloudsom.cloudsql.web;

import net.cloudsom.cloudsql.service.DbConnectPools;
import net.cloudsom.cloudsql.service.bean.DatabaseInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * @author zhulin
 */
@Controller
@SessionAttributes("dbInfo")
public class LoginController {
	@Autowired
	private DbConnectPools dbConnectPools;

	@ModelAttribute("dbInfo")
	public DatabaseInfo getDbInfo(){
		return new DatabaseInfo();
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("dbInfo") DatabaseInfo dbInfo) {
		dbConnectPools.addDbConnect(dbInfo);
		return "redirect:/home.html";
	}
}
