package com.aj.evaidya.patreg.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Set;

public class PatRegThreadBean {
	private Connection dbConn;
	private PreparedStatement pStat;
	private int rowsToInsert;
	private Set<String> nameAddr1FatNameSet;
	
	
	public Set<String> getNameAddr1FatNameSet() {
		return nameAddr1FatNameSet;
	}
	public void setNameAddr1FatNameSet(Set<String> nameAddr1FatNameSet) {
		this.nameAddr1FatNameSet = nameAddr1FatNameSet;
	}
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
	
	@Override
	public String toString() {
		return "PatRegThreadBean ["
				+ (dbConn != null ? "dbConn=" + dbConn + ", " : "")
				+ (pStat != null ? "pStat=" + pStat + ", " : "")
				+ "rowsToInsert="
				+ rowsToInsert
				+ ", "
				+ (nameAddr1FatNameSet != null ? "nameAddr1FatNameSet="
						+ nameAddr1FatNameSet : "") + "]";
	}

}
