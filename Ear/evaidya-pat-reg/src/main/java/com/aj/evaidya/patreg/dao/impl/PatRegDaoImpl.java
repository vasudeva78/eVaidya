package com.aj.evaidya.patreg.dao.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;
import com.aj.evaidya.patreg.beans.PatRegThreadBean;
import com.aj.evaidya.patreg.dao.PatRegDao;

public class PatRegDaoImpl implements PatRegDao {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); 
	
	private static final ThreadLocal<PatRegThreadBean> threadLocal= new ThreadLocal<PatRegThreadBean>();
	
	private static final Calendar cal = Calendar.getInstance();

	@Override
	public PatRegResponseBean savePatDtls(PatRegRequestBean patRegRequestBean) throws Exception {
		
 		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
 		
 		try(Connection dbConn = DriverManager.getConnection( patRegRequestBean.getDbUrl(), patRegRequestBean.getDbUsername() , patRegRequestBean.getDbPwd() )){
 					 			
 			dbConn.setAutoCommit(false);
 			
 			QueryRunner qRunner = new QueryRunner();
 			
 			int patNameExistsRowcount = qRunner.query(dbConn , "select count(*) from EV_PAT where EV_PAT_NAME='" + patRegRequestBean.getNameText() + "'" , 
					new ResultSetHandler<Integer>(){

						public Integer handle(ResultSet resultSet) throws SQLException {
						
							resultSet.next();
							
							return Integer.valueOf( resultSet.getInt(1) );
						}
				});
 			
 			
 			if(patNameExistsRowcount > 0){
 				
 				patRegResponseBean.setStatus("errorNameExists");
     			patRegResponseBean.setMessage("Name already Exists ...");
     			
     			return patRegResponseBean;
 			}
 			
 			qRunner.update(dbConn , 
 					"insert into EV_PAT(EV_PAT_NAME,EV_PAT_DOB,EV_PAT_ADDR1,EV_PAT_ADDR2,EV_PAT_STATE,EV_PAT_PIN_CODE,EV_PAT_TEL1,EV_PAT_TEL2) values ( ?,?,?,?,?,?,?,? ) "  , 
 					new Object[]{patRegRequestBean.getNameText() , patRegRequestBean.getYearText()+"-"+patRegRequestBean.getMonthText()+"-"+patRegRequestBean.getDateText() , patRegRequestBean.getAddress1Text() , patRegRequestBean.getAddress2Text() , patRegRequestBean.getStateId() , patRegRequestBean.getPincode() ,patRegRequestBean.getTel1Text() , patRegRequestBean.getTel2Text()  });
 			
 			patRegResponseBean.setStatus("success");
 			patRegResponseBean.setMessage("Saved ...");
 			
 			dbConn.commit();
 			
 		}  catch(Exception e) {
 			
 			throw e;
 			 			
 		} 
 		
    	return patRegResponseBean;
     }

	@Override
	public Map<String, String> getPatNameDtls(PatRegRequestBean patRegRequestBean) throws Exception {

		Map<String,String> patNameListMap = new LinkedHashMap<String,String>();
		
		try(Connection dbConn = DriverManager.getConnection( patRegRequestBean.getDbUrl(), patRegRequestBean.getDbUsername() , patRegRequestBean.getDbPwd() )){
						
			QueryRunner qRunner = new QueryRunner();
 			
 			patNameListMap = qRunner.query(dbConn , "select EV_PAT_ID , EV_PAT_NAME from EV_PAT where EV_PAT_NAME like '"+patRegRequestBean.getNameText()+"%' order by EV_PAT_NAME" , 
					new ResultSetHandler<Map<String, String>>(){

						public Map<String, String> handle(ResultSet resultSet) throws SQLException {
							
							Map<String, String> nameListMap = new LinkedHashMap<String, String>() ;
							
							nameListMap.put("--","-- Select --");
							
							while( resultSet.next() ){
								nameListMap.put( resultSet.getString("EV_PAT_ID") , resultSet.getString("EV_PAT_NAME") );
							}
							
							return nameListMap;
						}
				});
		
		} catch(Exception e ){
 			
			throw e;
		}
		
		return patNameListMap;
		
	}

	@Override
	public PatRegResponseBean getPatDtls(PatRegRequestBean patRegRequestBean) throws Exception {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		try(Connection dbConn = DriverManager.getConnection( patRegRequestBean.getDbUrl(), patRegRequestBean.getDbUsername() , patRegRequestBean.getDbPwd() )){
			
			QueryRunner qRunner = new QueryRunner();
 			
			String patNameId = patRegRequestBean.getNameId();
			
			patRegResponseBean = qRunner.query(dbConn , "select EV_PAT_ID , EV_PAT_NAME , DAY_OF_MONTH(EV_PAT_DOB) as PAT_DAY , FORMATDATETIME(EV_PAT_DOB,'MMM') as PAT_MON , YEAR(EV_PAT_DOB) as PAT_YEAR ,EV_PAT_ADDR1, EV_PAT_ADDR2 , EV_PAT_STATE , EV_PAT_PIN_CODE , EV_PAT_TEL1 , EV_PAT_TEL2 from EV_PAT where EV_PAT_ID = '"+patNameId+"'" , 
					new ResultSetHandler<PatRegResponseBean>(){

						public PatRegResponseBean handle(ResultSet resultSet) throws SQLException {
							
							PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
							
							resultSet.next();
							patRegResponseBean.setNameText(resultSet.getString("EV_PAT_NAME"));
							patRegResponseBean.setPatDay(resultSet.getString("PAT_DAY"));
							patRegResponseBean.setPatMon(resultSet.getString("PAT_MON").toUpperCase());
							patRegResponseBean.setPatYear(resultSet.getString("PAT_YEAR"));
							patRegResponseBean.setAddress1Text(resultSet.getString("EV_PAT_ADDR1"));
							patRegResponseBean.setAddress2Text(resultSet.getString("EV_PAT_ADDR2"));
							patRegResponseBean.setStateId(resultSet.getString("EV_PAT_STATE"));
							patRegResponseBean.setPincode(resultSet.getString("EV_PAT_PIN_CODE"));
							patRegResponseBean.setTel1Text(resultSet.getString("EV_PAT_TEL1"));
							patRegResponseBean.setTel2Text(resultSet.getString("EV_PAT_TEL2"));
							
							return patRegResponseBean;
						}
				});
		
		} catch(Exception e ){
 			
			throw e;
		}
		
		return patRegResponseBean;
		
	}

	@Override
	public PatRegResponseBean updatePatDtls(PatRegRequestBean patRegRequestBean) throws Exception {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
 		
 		try(Connection dbConn = DriverManager.getConnection( patRegRequestBean.getDbUrl(), patRegRequestBean.getDbUsername() , patRegRequestBean.getDbPwd() )){
 					 			
 			dbConn.setAutoCommit(false);
 			
 			QueryRunner qRunner = new QueryRunner();
 			 			
 			qRunner.update(dbConn , 
 					"update EV_PAT set EV_PAT_NAME = ?,EV_PAT_DOB=?,EV_PAT_ADDR1=?,EV_PAT_ADDR2=?,EV_PAT_STATE=?,EV_PAT_PIN_CODE=?,EV_PAT_TEL1=?,EV_PAT_TEL2 = ? where EV_PAT_ID = ? "  , 
 					new Object[]{patRegRequestBean.getNameText() , patRegRequestBean.getYearText()+"-"+patRegRequestBean.getMonthText()+"-"+patRegRequestBean.getDateText() , patRegRequestBean.getAddress1Text() , patRegRequestBean.getAddress2Text() , patRegRequestBean.getStateId() , patRegRequestBean.getPincode() ,patRegRequestBean.getTel1Text() , patRegRequestBean.getTel2Text() , patRegRequestBean.getNameId() });
 			
 			patRegResponseBean.setStatus("success");
 			patRegResponseBean.setMessage("Saved ...");
 			
 			dbConn.commit();
 			
 		}  catch(Exception e) {
 			
 			throw e;
 			 			
 		} 
 		
    	return patRegResponseBean;
	}

	@Override
	public PatRegResponseBean uploadPatDtls(PatRegRequestBean patRegRequestBean , int rowNum , int maxRowNum) throws Exception {
	
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		patRegResponseBean.setStatus("success");
		patRegResponseBean.setMessage("Saved ...");
		
		try {
			
			if(rowNum == 1){
				
				PatRegThreadBean patRegThreadBean = new PatRegThreadBean();
				
				Connection dbConn = DriverManager.getConnection( patRegRequestBean.getDbUrl(), patRegRequestBean.getDbUsername() , patRegRequestBean.getDbPwd() );
				dbConn.setAutoCommit(false);
				
				patRegThreadBean.setDbConn(dbConn);
				
				PreparedStatement pStat = dbConn.prepareStatement("insert into EV_PAT(EV_PAT_NAME,EV_PAT_DOB,EV_PAT_ADDR1,EV_PAT_ADDR2,EV_PAT_STATE,EV_PAT_PIN_CODE,EV_PAT_TEL1,EV_PAT_TEL2) values (?,?,?,?,?,?,?,?)" );
				patRegThreadBean.setpStat(pStat);
				
				Workbook workbook = Workbook.getWorkbook(new File( patRegRequestBean.getXlFilePath() ));
				
				patRegThreadBean.setWorkbook(workbook);
				
				threadLocal.set(patRegThreadBean);
				
			} else if (rowNum % 100 == 0){
				PatRegThreadBean patRegThreadBean = threadLocal.get();
				patRegThreadBean.getpStat().executeBatch();
					
				patRegThreadBean.getpStat().clearBatch();
				patRegThreadBean.getpStat().clearParameters();
			}
			else if(rowNum == maxRowNum){
				
				PatRegThreadBean patRegThreadBean = threadLocal.get();
				
				patRegThreadBean.getpStat().executeBatch();
				patRegThreadBean.getpStat().close();
				
				patRegThreadBean.getDbConn().commit();
				patRegThreadBean.getDbConn().close();
				
				patRegThreadBean.getWorkbook().close();
				
				threadLocal.remove();
			}
			else {
				
				PatRegThreadBean patRegThreadBean = threadLocal.get();
				Sheet sheet = patRegThreadBean.getWorkbook().getSheet(0);
				
				Cell[] cells = sheet.getRow(rowNum);
				
				PreparedStatement pStat = patRegThreadBean.getpStat();
				pStat.setString(1, cells[0].getContents().trim().substring(0, Math.min(100, cells[0].getContents().trim().length() )) );
				
				cal.setTime( sdf.parse(cells[1].getContents().trim()) );
				
				pStat.setString(2, cal.get(Calendar.YEAR) + "-" + ( cal.get(Calendar.MONTH) + 1 ) + "-" + cal.get(Calendar.DATE) );
				
				pStat.setString(3, cells[2].getContents().substring(0, Math.min(2000, cells[2].getContents().trim().length())) );
				pStat.setString(4, cells[3].getContents().substring(0, Math.min(2000, cells[3].getContents().trim().length())) );
				pStat.setString(5, cells[4].getContents().substring(0, Math.min(10, cells[4].getContents().trim().length())) );
				pStat.setString(6, cells[5].getContents().substring(0, Math.min(10, cells[5].getContents().trim().length())) );
				pStat.setString(7, cells[6].getContents().substring(0, Math.min(100, cells[6].getContents().trim().length())) );
				pStat.setString(8, cells[7].getContents().substring(0, Math.min(100, cells[7].getContents().trim().length())) );
				
				pStat.addBatch();
			}
			
		} catch (Exception e) {
			
			PatRegThreadBean patRegThreadBean = threadLocal.get();
			
			if(null != patRegThreadBean){
				
				patRegThreadBean.getpStat().close();
				patRegThreadBean.getDbConn().close();
				patRegThreadBean.getWorkbook().close();
				
			}
			
			threadLocal.remove();
			
			throw e;
		
		} 
			
		return patRegResponseBean;
	}
	
//	@Override
//	public PatRegResponseBean uploadPatDtls(PatRegRequestBean patRegRequestBean , int rowNum) throws Exception {
//		
//		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
// 		
//		Workbook workbook = null;
//		
// 		try( Connection dbConn = DriverManager.getConnection( patRegRequestBean.getDbUrl(), patRegRequestBean.getDbUsername() , patRegRequestBean.getDbPwd() ) ; 
// 				PreparedStatement pStat = dbConn.prepareStatement("insert into EV_PAT(EV_PAT_NAME,EV_PAT_DOB,EV_PAT_ADDR1,EV_PAT_ADDR2,EV_PAT_STATE,EV_PAT_PIN_CODE,EV_PAT_TEL1,EV_PAT_TEL2) values (?,?,?,?,?,?,?,?)" )) {
// 					 			
// 			dbConn.setAutoCommit(false);
// 			
// 			workbook = Workbook.getWorkbook(new File( patRegRequestBean.getXlFilePath() ));
// 			
// 			Sheet sheet = workbook.getSheet(0);
// 			
// 			int rowNum = sheet.getRows();
// 			
// 			Calendar cal = Calendar.getInstance();
// 			 			
// 			for( int i=1 ; i< rowNum ; i++ ){
// 				
// 				Cell[] cells = sheet.getRow(i);
// 				
// 				pStat.setString(1, cells[0].getContents().trim().substring(0, Math.min(100, cells[0].getContents().trim().length() )) );
// 				
// 				cal.setTime( sdf.parse(cells[1].getContents().trim()) );
// 				
// 				pStat.setString(2, cal.get(Calendar.YEAR) + "-" + ( cal.get(Calendar.MONTH) + 1 ) + "-" + cal.get(Calendar.DATE) );
// 				
// 				pStat.setString(3, cells[2].getContents().substring(0, Math.min(2000, cells[2].getContents().trim().length())) );
// 				pStat.setString(4, cells[3].getContents().substring(0, Math.min(2000, cells[3].getContents().trim().length())) );
// 				pStat.setString(5, cells[4].getContents().substring(0, Math.min(10, cells[4].getContents().trim().length())) );
// 				pStat.setString(6, cells[5].getContents().substring(0, Math.min(10, cells[5].getContents().trim().length())) );
// 				pStat.setString(7, cells[6].getContents().substring(0, Math.min(100, cells[6].getContents().trim().length())) );
// 				pStat.setString(8, cells[7].getContents().substring(0, Math.min(100, cells[7].getContents().trim().length())) );
// 				
// 				pStat.addBatch();
// 				
// 				if(i % 100 == 0){
// 					pStat.executeBatch();
// 					
// 					pStat.clearBatch();
// 					pStat.clearParameters();
// 				}
// 				
// 			}
// 			
//			pStat.executeBatch();
// 			
// 			patRegResponseBean.setStatus("success");
// 			patRegResponseBean.setMessage("Saved ...");
//	
// 			dbConn.commit();
// 			
// 		}  catch(Exception e) {
// 			
// 			throw e;
// 			 			
// 		} finally {
// 			workbook.close();
// 		}
// 		
//    	return patRegResponseBean;
//    	
//	}

	@Override
	public PatRegResponseBean getExcelRowsOnUpload(PatRegRequestBean patRegRequestBean) throws Exception {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		Workbook workbook = null;	
		
		try {
			
			workbook = Workbook.getWorkbook(new File( patRegRequestBean.getXlFilePath() ));
			
			Sheet sheet = workbook.getSheet(0);
			
			patRegResponseBean.setExcelRowNum( String.valueOf(sheet.getRows()) );
			
 		}  catch(Exception e) {
 			
 			throw e;
 			 			
 		} finally {
 			workbook.close();
 		}
		
		return patRegResponseBean;
	}
	
}

