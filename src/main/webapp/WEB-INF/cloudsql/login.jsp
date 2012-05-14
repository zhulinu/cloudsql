<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>cloudsql login</title>
    <link rel="stylesheet" type="text/css" href="static/css/loginstyle.css">
</head>
<body style="margin: 20px">
  <div id="cs-sql-login">
    <form:form id="login" action="${ctx}/login" method="post" >
		<table class="login" >
            <tbody><tr class="login">
                <th class="login">Login</th>
                <th class="login"></th>
            </tr>
            <tr><td class="login" colspan="2"></td></tr>
            <tr class="login">
                <td class="login">Database Type:</td>
                <td class="login">
                    <select name="setting" size="1" >
                    <option value="Generic Oracle">Generic Oracle</option>
                    </select>
                </td>
            </tr>
            <tr class="login">
                <td class="login" colspan="2">
                    <hr>
                </td>
            </tr>
            <tr class="login">
                <td class="login">Database IP:</td>
                <td class="login"><input type="text" name="dbip" value="192.168.17.129"></td>
            </tr>
            <tr class="login">
                <td class="login">Database Port:</td>
                <td class="login"><input type="text" name="dbport"  value="1521"></td>
            </tr>
			<tr class="login">
                <td class="login">Database SID:</td>
                <td class="login"><input type="text" name="dbsid"  value="XE"></td>
            </tr>
            <tr class="login">
                <td class="login">User Name:</td>
                <td class="login"><input type="text" name="user" value="bwdasic"></td>
            </tr>
            <tr class="login">
                <td class="login">Password:</td>
                <td class="login"><input type="password" name="password" value="bwdasic"></td>
            </tr>
            <tr class="login">
                <td class="login"></td>
                <td class="login">
                    <input type="submit" class="button" value="Connect">
                    &nbsp;
                    <input type="button" class="button" value="Test Connection">
                    <br>
                    <br>
                </td>
            </tr>
        </tbody></table>
        <br>
        <p class="error"></p>
    </form:form>
  </div>
</body></html>