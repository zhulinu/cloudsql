<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>cloud security operation manage sql</title>
<meta name="description" content="cloud security operation manage.">
<meta name="keywords" content="cloud sql ssh security operation manage">
<link rel="stylesheet" type="text/css" href="static/css/base.css">
<link rel="stylesheet" type="text/css" href="static/css/cloudsql.css">
<link rel="stylesheet" type="text/css" href="static/css/codemirror.css">

<script src="static/js/jquery.min.js"></script>
<script src="static/js/codemirror.js"></script>
<script src="static/js/plsql.js"></script>

<script src="static/js/handlebars-1.0.0.beta.6.js"></script>

<script src="static/js/underscore-1.3.1.js"></script>
<script src="static/js/backbone.js"></script>
	
<script src="static/js/jquery.tools.min.js"></script>
<script src="static/js/cloudsom.js" charset="UTF-8"></script>
<script>
	$(function() {	
		$('#accordion h2').click(function(){
			$(this).next('div').toggle();
		});
	});
</script>
<script id="query-output-template" type="text/x-handlebars-template">
	{{#if id}}
		{{#each sets}}
		<div class="set">
				<table class="results table table-bordered table-striped">
					<tr>
					{{#each this.results.columns}}
					<th>{{this}}</th>
					{{/each}}
					</tr>
					{{#each this.results.data}}
					<tr>
						{{#each this}}
						<td>{{this}}</td>
						{{/each}}
					</tr>
					{{/each}}
				</table>
			{{#if this.succeeded}}
			<div id="messages" class="alert alert-success">
				<i class="icon-ok"></i>
				Record Count: {{this.results.data.length}}; Execution Time: {{this.executiontime}}ms
			</div>
			{{/if}}
		</div>
		{{/each}}
	{{/if}}
</script>	
	
</head>
<body>
	<div id="cs-wrapper">
		<div id="cs-header">
			<div id="cs-header-title-bar">
				<h1>Cloud Sql -- <c:out value='${dbInfo.user}@${dbInfo.dbip}:${dbInfo.dbport}/${dbInfo.dbsid}'/></h1>
			</div>
			<div id="cs-header-tools-bar">
			    <div>
					<button id="cs-tools-login" title="login"></button>
				</div>
				<div>
					<button id="cs-tools-execute" title="execute"></button>
				</div>
			</div>
			<div id="cs-header-help-bar">
				<h1>Welcome to Cloud Sql</h1>
			</div>

		</div>
		<hr>
		<div id="cs-content-wrapper">
			<div id="cs-splitter">
				<div id="cs-splitter-left">
					<!-- panes -->
					<div class="cs-nav-panes">
						<div style="display: block;">
							选择：<select id="tabUserCbx">
							</select> 
							<br />
							<div id="accordion">
								<div id="cs-sql-tabls">
									<h2 id="cs-all-table">all tables</h2>
									<div id="sql-sql-tables-detail">
									</div>
								</div>
								<div id="cs-sql-views">
									<h2 id="cs-all-view">all views</h2>
									<div id="sql-sql-views-detail">
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="cs-splitter-right">
					<div id="cs-query-panel">
						<div id="cs-sql-editor">
						  <form><textarea id="cs-sql-code" ></textarea></form>
						</div>
					</div>
					<div id="cs-sql-result"></div>
				</div>
			</div>
		</div>
		<hr>
		<div id="cs-footer">The footer</div>
	</div>
</body>
</html>