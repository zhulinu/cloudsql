<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>cloud security operation manage sql</title>
<meta name="description" content="cloud security operation manage.">
<meta name="keywords" content="cloud sql ssh security operation manage">

<link rel="stylesheet" type="text/css" href="static/css/jquery.ui.all.css">
<LINK rel="stylesheet" type="text/css" href="static/css/layout-default-latest.css">
<link rel="stylesheet" type="text/css" href="static/css/demos.css">
<link rel="stylesheet" type="text/css" href="static/css/cloudsql.css">
<link rel="stylesheet" type="text/css" href="static/css/codemirror.css">
<STYLE type="text/css"> 
//---------------tabs
	#tabs { margin-top: 1em; }
	#tabs li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }
	#add_tab { cursor: pointer; }
	
	#tabs{height:300px;margin:0;padding:0;border:0;}
	#tabs-1{height:200px;margin:0;padding:0;border:0;}
	
	//------------layout
	.container {
		border-width:	0;
		overflow:		hidden;
		padding:		0;
		display:		none; /* hide panes until layout initializes */
	}
	.block {
		border:			1px dashed #BBB;
		padding:		1px 1px;
		font-weight:	bold;
	}
	.border	{ border-width:	1px; }
	.ui-layout-pane { padding:		0;}
	.lite	{ background: #EEE; color: #000; }
	.dark	{ background: #777; color: #FFF; }
	.grey	{ background: #CCC; color: #000; }
	.yellow	{ background: #FFA; color: #777; }
	.white	{ background: #FFF; color: #000; }
</STYLE> 

<SCRIPT src="static/js/jquery-latest.js"></SCRIPT> 
<SCRIPT src="static/js/jquery-ui-latest.js"></SCRIPT> 
<SCRIPT src="static/js/jquery.layout-latest.js"></SCRIPT> 
<script src="static/js/codemirror.js"></script>
<script src="static/js/plsql.js"></script>

<script src="static/js/handlebars-1.0.0.beta.6.js"></script>

<script src="static/js/underscore-1.3.1.js"></script>
<script src="static/js/backbone.js"></script>

<script src="static/js/cloudsom.js"></script>
<SCRIPT type="text/javascript" language="javascript"> 
//--------------tabs config begin--------------------
	$(function() {
		var tab_counter = 2;

		// tabs init with a custom tab template and an "add" callback filling in the content
		var $tabs = $( "#tabs").tabs({
			tabTemplate: "<li><a href='#\{href\}'>#\{label\}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
			add: function( event, ui ) {
				$( ui.panel ).append( "<p>" + "Tab " + tab_counter + " content." + "</p>" );
			}
		});

		// actual addTab function: adds new tab using the title input from the form above
		function addTab() {
			var tab_title =  "Tab " + tab_counter;
			$tabs.tabs( "add", "#tabs-" + tab_counter, tab_title );
			tab_counter++;
		}

		// addTab button: just opens the dialog
		$( "#add_tab" )
			.button()
			.click(function() {
				addTab();
			});

		// close icon: removing the tab on click
		// note: closable tabs gonna be an option in the future - see http://dev.jqueryui.com/ticket/3924
		$( "#tabs span.ui-icon-close" ).live( "click", function() {
			var index = $( "li", $tabs ).index( $( this ).parent() );
			$tabs.tabs( "remove", index );
		});
	});
//--------------tabs config end--------------------
//--------------layout config begin--------------------
	var
		outerLayout = null,
		outerLayoutOptions = {
			north__paneSelector   : ".out-north"  ,
			west__paneSelector    : ".out-west"   ,
			center__paneSelector  : ".out-center" ,
			south__paneSelector   : ".out-south"  ,
			north__closable       : false ,
		    north__resizable      : false ,
			south__closable       : false ,
		    south__resizable      : false ,
			north__size           :	50  ,
			south__size           :	30  ,
			west__size            : 220 ,
			west__minSize         :	220 ,
			west__maxSize         :	500 ,
			north__size           :	66  ,
			north__spacing_open   :	0   ,
			south__spacing_open   :	0   ,
			spacing_open          :	8   ,
		    spacing_closed        :	10  ,
			center__onresize      : "innerLayout.resizeAll",
			center__onresize_end  :function () {$("#tabs-1").height(document.body.clientHeight-131);
												 $("#cs-left-pane-detail").height(document.body.clientHeight-122);
												 $("#cs-left-pane-detail").width($(".out-west").width()-17);}
		},
		innerLayout = null,
	    innerLayoutOptions = {
			center__paneSelector:	".inner-center" ,
			south__paneSelector:	".inner-south"  ,
			spacing_open:			8  ,// ALL panes  ,
			spacing_closed:			10 ,
			south__size:			"70%" ,
			south__minSize:			80    ,
			south__maxSize:			"80%" ,
			south__closable:		false ,
			south__onresize_end  :function () {$(".CodeMirror-scroll").height($("#tabs-1").height()-$(".inner-south").height()-20);
			                                    $("#cs-sql-result").height($(".inner-south").height()-20);}
		}; 
//--------------layout config end--------------------
	$(function() {
		outerLayout = $('body').layout( outerLayoutOptions ); 
		innerLayout = $('#tabs-1').layout( innerLayoutOptions ); 
		//设置中心的高度，防止多个滚动条出现
		$("#tabs-1").height(document.body.clientHeight-131);
		$("#cs-left-pane-detail").height(document.body.clientHeight-122);
		$("#cs-left-pane-detail").width($(".out-west").width()-17);
	}); 
	
	$(function() {	
		$('#cs-left-pane-detail h2').click(function(){
			$(this).next('div').toggle();
		});
	});

//---------------dialog begin
	$(function() {
		var $dialog = $( "#dialog" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				Connect: function() {
					 var form = document.getElementById("login");
					 form.submit();
					$( this ).dialog( "close" );
				},
				Test: function() {
					$( this ).dialog( "close" );
				}
			},
			open: function() {
				
			},
			close: function() {
				$form[ 0 ].reset();
			}
		});

		if ( $('#cs-header-title-bar p').text().length < 20){
			$dialog.dialog( "open" );
		}
		
		$( "#cs-login" )
		.button()
		.click(function() {
			$dialog.dialog( "open" );
		});
	});
//---------------dialog end
</SCRIPT>
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

</HEAD>
<BODY>
<DIV class="out-north container border">
  <div id="cs-header-title-bar">
    <p>Cloud Sql -- <c:out value='${dbInfo.user}@${dbInfo.dbip}:${dbInfo.dbport}/${dbInfo.dbsid}'/></p>
  </div>
  <div id="cs-header-tools-bar">
    <div><button id="cs-login">login</button></div>
	<div><button id="cs-tools-execute" title="execute"></button></div>
  </div>
</DIV>
<DIV class="out-west container border">
  <div id="cs-left-pane" style="display: block;">
    请选择：<select id="tabUserCbx"></select> 
	<br />
	<div id="cs-left-pane-detail">
	  <div id="cs-sql-tabls">
	    <h2 id="cs-all-table">all tables</h2>
	    <div id="sql-sql-tables-detail"></div>
      </div>
	  <div id="cs-sql-views">
	    <h2 id="cs-all-view">all views</h2>
		<div id="sql-sql-views-detail"></div>
	  </div>
    </div>
  </div>
</DIV>
<DIV class="out-center container">
<div class="demo container">
	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">home</a></li>
		</ul>
		<div id="tabs-1">
	<DIV class="inner-center container">
		<div id="cs-sql-editor">
		  <form><textarea id="cs-sql-code" ></textarea></form>
		</div>
	</DIV> 
	<DIV class="inner-south container">
		<DIV class="block lite">Info</DIV>
		<div id="cs-sql-result"></div>
	</DIV>
		</div>
	</div>
</div><!-- End demo -->
 
</DIV>
<DIV id="cs-footer" class="out-south container border">Info</DIV>

<div id="dialog" title="Login">
	<form:form id="login" action="${ctx}/login" method="post" >
		<table class="login" >
            <tbody>
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
        </tbody></table>
        <br>
        <p class="error"></p>
	</form:form>
	</div>
</BODY>
</HTML>