$(function(){
var CloudSOM = {
  CloudSql:{},
  CloudSsh:{},
  initialize: function() {  
    window.query = new CloudSOM.CloudSql.QueryModel();
    window.queryView = new CloudSOM.CloudSql.QueryView({
      id: "cs-sql-code",
      model: window.query,
      outputTemplate: $("#query-output-template"),
      output_el: $("#cs-sql-result"),
      el:$("#cs-sql-editor")
    });
    window.buttonView = new CloudSOM.CloudSql.ButtonView();
	window.resultView = new CloudSOM.CloudSql.ResultView();
  } 
};

CloudSOM.CloudSql.QueryModel = Backbone.Model.extend({
  defaults: {
    "id": 1,
    "sql": "",
    "sets":[]
    /*"sets": [{"RESULTS":
	          {"COLUMNS":["id","type","details"],
			      "DATA":[["1","MAIL","details"],["2","OLD","details"]]
              },
			  "EXECUTIONTIME":3,
			  "SUCCEEDED":"SUCCEEDED"
             }
			]*/
  },
  execute: function () {
    var thisModel = this;
    $.ajax({
      type: "POST",
      url:"/cloudsql/query/sql.html",
      dataType: "json",
      contentType: "application/x-www-form-urlencoded; charset=utf-8",
	  data: "sql="+this.get("sql"),
    beforeSend: function(XMLHttpRequest){
    	   XMLHttpRequest.setRequestHeader("Accept", "text/plain");
    	  },
      success: function (resp, textStatus, jqXHR) {
    	  thisModel.set({
				"sets": resp
			});
			thisModel.trigger("executed");
      }
    })
	
  }
});

//
CloudSOM.CloudSql.ButtonView = Backbone.View.extend({
  el:$("#cs-header-tools-bar"),
  events: {
	"click #cs-tools-login"      : "login",
    "click #cs-tools-execute"    : "execute"
  },
  login:function(){},
  execute:function(){
	    var thisView = window.queryView; // kludge to handle the context limitations on CodeMirror change events
	    var querySql = thisView.editor.getSelection();
	    if ( querySql.length == 0 ){
	    	querySql = thisView.editor.getValue();
	    }
	    thisView.model.set({
	      "sql": querySql
	    });
	    window.query.execute();
  },
  save:function(){},
  saveAs:function(){},
  cut:function(){},
  paste:function(){},
  beautifer:function(){},
  break:function(){},
  commit:function(){},
  rollback:function(){},
  explain:function(){}
});

//
CloudSOM.CloudSql.ObjectView = Backbone.View.extend({
  el:$("#cs-splitter-left"),
  events: {
    "change #tabUserCbx" : "changeUsers",
    "click #cs-all-table": "changeTable",
    "click #cs-all-view" : "changeView",
    "click #accordion p" : "clickObject"
  },
  changeUsers:function(){
	  //alert($('#tabUserCbx').val()+$('#tabUserCbx option:selected').text());
	  $('#sql-sql-tables-detail').load('/cloudsql/query/allTables.html',{"user":$('#tabUserCbx option:selected').text()});
	  $('#sql-sql-views-detail').load('/cloudsql/query/allViews.html',{"user":$('#tabUserCbx option:selected').text()});
  },
  changeTable:function(){
	  
  },
  changeView:function(){
	  
  },
  clickObject:function(e){
	  //window.queryView.editor.setValue('select * from '+$(this).text().trim()+';');
	  window.queryView.editor.setValue('select * from '+$(e.target).text()+';');
  },
  initialize:function(){
	  $('#tabUserCbx').load('/cloudsql/query/sqlusers.html');
  }
});

//
CloudSOM.CloudSql.QueryView = Backbone.View.extend({
  initialize: function () {
    this.editor = CodeMirror.fromTextArea(document.getElementById(this.id), {
      lineNumbers: true,
      matchBrackets: true,
      indentUnit: 4,
      mode: "text/x-plsql",
	  onChange: this.handleQueryChange,
	  onKeyEvent: this.keyEvent
    });
	this.compiledOutputTemplate = Handlebars.compile(this.options.outputTemplate.html()); 
  },
  events: {
     //"keypress #new-todo":  "createOnEnter"
  },
  handleQueryChange: function () {			

  },
  keyEvent:function(editor,event){
    event = $.event.fix(event);
    if (event.keyCode == 119) {	// F8
      window.buttonView.execute();
    }
  },
  
  renderOutput:function(){
    var sql = this.model.get("sql");
    var result = this.compiledOutputTemplate(this.model.toJSON());
    this.options.output_el.html(result);
    $("#cs-footer").html("Record Count: "+this.model.toJSON().sets[0].results.data.length+
    	"; Execution Time: "+this.model.toJSON().sets[0].executiontime+"ms");
  }
});

//
CloudSOM.CloudSql.ResultView = Backbone.View.extend({
  el:$("#cs-sql-result"),
  
});
//CloudSOM.initialize();

    window.query = new CloudSOM.CloudSql.QueryModel();

    window.buttonView = new CloudSOM.CloudSql.ButtonView();

    window.objectView = new CloudSOM.CloudSql.ObjectView();
    
	window.queryView = new CloudSOM.CloudSql.QueryView({
      id: "cs-sql-code",
      model: window.query,
      outputTemplate: $("#query-output-template"),
      output_el: $("#cs-sql-result"),
      el:$("#cs-sql-editor")
    });
	
    window.resultView = new CloudSOM.CloudSql.ResultView();

  //bind events
  window.query.on("executed", function () {
    window.queryView.renderOutput();
  });
});