package com.aj.evaidya.patreg.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;

import jxl.Workbook;

public class PatRegThreadBean {
	private Connection dbConn;
	private PreparedStatement pStat;
	private Workbook workbook;
	private int rowsToInsert;
	
	
	public int getRowsToInsert() {
		return rowsToInsert;
	}
	public void setRowsToInsert(int rowsToInsert) {
		this.rowsToInsert = rowsToInsert;
	}
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
				+ (workbook != null ? "workbook=" + workbook + ", " : "")
				+ "rowsToInsert=" + rowsToInsert + "]";
	}
	
	
}
