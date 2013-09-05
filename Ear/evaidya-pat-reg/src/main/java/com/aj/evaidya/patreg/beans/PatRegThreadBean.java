package com.aj.evaidya.patreg.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;

import jxl.Workbook;

public class PatRegThreadBean {
	private Connection dbConn;
	private PreparedStatement pStat;
	private String[] sqlUpdateResults;
	private Workbook workbook;
	
	
	public Connection getDbConn() {
		return dbConn;
	}
	public void setDbConn(Connection dbConn) {
		this.dbConn = dbConn;
	}
	public PreparedStatement getpStat() {
		return pStat;
	}
	public void setpStat(PreparedStatement pStat) {
		this.pStat = pStat;
	}
	public String[] getSqlUpdateResults() {
		return sqlUpdateResults;
	}
	public void setSqlUpdateResults(String[] sqlUpdateResults) {
		this.sqlUpdateResults = sqlUpdateResults;
	}
	public Workbook getWorkbook() {
		return workbook;
	}
	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
	@Override
	public String toString() {
		return "PatRegThreadBean ["
				+ (dbConn != null ? "dbConn=" + dbConn + ", " : "")
				+ (pStat != null ? "pStat=" + pStat + ", " : "")
				+ (sqlUpdateResults != null ? "sqlUpdateResults="
						+ Arrays.toString(sqlUpdateResults) + ", " : "")
				+ (workbook != null ? "workbook=" + workbook : "") + "]";
	}
	
	
}
