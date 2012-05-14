package net.cloudsom.cloudsql.service.bean;

import org.springframework.stereotype.Service;
/**
 * DatabaseInfo为数据库连查询结果对象，为了生成json数据返回给view层。
 * @author zhulin
 */
@Service
public class SelectSqlJson {
	private SelectSqlResult RESULTS = null;
	private int EXECUTIONTIME = 0;
	private String SUCCEEDED = null;
	public String getSUCCEEDED() {
		return SUCCEEDED;
	}
	public void setSUCCEEDED(String sUCCEEDED) {
		SUCCEEDED = sUCCEEDED;
	}
	public int getEXECUTIONTIME() {
		return EXECUTIONTIME;
	}
	public void setEXECUTIONTIME(int eXECUTIONTIME) {
		EXECUTIONTIME = eXECUTIONTIME;
	}
	public SelectSqlResult getRESULTS() {
		return RESULTS;
	}
	public void setRESULTS(SelectSqlResult rESULTS) {
		RESULTS = rESULTS;
	}
}
