package com.aj.evaidya.patreg.dao.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.common.Logger;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.h2.jdbcx.JdbcConnectionPool;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;
import com.aj.evaidya.patreg.beans.PatRegThreadBean;
import com.aj.evaidya.patreg.dao.PatRegDao;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PatRegDaoImpl implements PatRegDao {
	
	private static final ThreadLocal<PatRegThreadBean> threadLocal= new ThreadLocal<PatRegThreadBean>();
	
	private static final Logger logger = Logger.getLogger(PatRegDaoImpl.class);
	
	private JdbcConnectionPool dbConnPool;

	@Inject
	public void setDbConnPool(@Named("dbConnPool") JdbcConnectionPool dbConnPool) {
		this.dbConnPool = dbConnPool;
	}

	@Override
	public PatRegResponseBean savePatDtls(PatRegRequestBean patRegRequestBean) throws Exception {
		
 		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
 		
 		try(Connection dbConn = dbConnPool.getConnection()){
 					 			
 			dbConn.setAutoCommit(false);
 			
 			QueryRunner qRunner = new QueryRunner();
 			
 			int patNameExistsRowcount = qRunner.query(dbConn , "select count(*) from EV_PAT where EV_PAT_NAME='" + patRegRequestBean.getNameText() + "' and EV_PAT_ADDR1 ='" + patRegRequestBean.getAddress1Text() + "' and EV_PAT_FAT_NAME = '"+  patRegRequestBean.getFatNameText() +"'", 
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
 					"insert into EV_PAT(EV_PAT_NAME,EV_PAT_DOB,EV_PAT_ADDR1,EV_PAT_ADDR2,EV_PAT_STATE,EV_PAT_PIN_CODE,EV_PAT_SEX,EV_PAT_TEL1,EV_PAT_TEL2,EV_PAT_FAT_NAME) values ( ?,?,?,?,?,?,?,?,?,? ) "  , 
 					new Object[]{patRegRequestBean.getNameText() , patRegRequestBean.getYearText()+"-"+patRegRequestBean.getMonthText()+"-"+patRegRequestBean.getDateText() , patRegRequestBean.getAddress1Text() , patRegRequestBean.getAddress2Text() , patRegRequestBean.getStateText() , patRegRequestBean.getPincode() ,patRegRequestBean.getSex() ,patRegRequestBean.getTel1Text() , patRegRequestBean.getTel2Text() ,patRegRequestBean.getFatNameText()  });
 			
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
		
		try(Connection dbConn = dbConnPool.getConnection()){
						
			QueryRunner qRunner = new QueryRunner();
 			
 			patNameListMap = qRunner.query(dbConn , "select EV_PAT_ID , EV_PAT_NAME from EV_PAT where upper(EV_PAT_NAME) like upper('"+patRegRequestBean.getNameText()+"%') order by EV_PAT_NAME" , 
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
		
		try(Connection dbConn = dbConnPool.getConnection()){
			
			QueryRunner qRunner = new QueryRunner();
 			
			String patNameId = patRegRequestBean.getNameId();
			
			patRegResponseBean = qRunner.query(dbConn , "select EV_PAT_ID , EV_PAT_NAME , DAY_OF_MONTH(EV_PAT_DOB) as PAT_DAY , FORMATDATETIME(EV_PAT_DOB,'MMM') as PAT_MON , YEAR(EV_PAT_DOB) as PAT_YEAR ,EV_PAT_ADDR1, EV_PAT_ADDR2 , EV_PAT_STATE , EV_PAT_PIN_CODE , EV_PAT_SEX , EV_PAT_TEL1 , EV_PAT_TEL2 , EV_PAT_FAT_NAME from EV_PAT where EV_PAT_ID = '"+patNameId+"'" , 
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
							patRegResponseBean.setStateText(resultSet.getString("EV_PAT_STATE"));
							patRegResponseBean.setPincode(resultSet.getString("EV_PAT_PIN_CODE"));
							patRegResponseBean.setSex(resultSet.getString("EV_PAT_SEX"));
							patRegResponseBean.setTel1Text(resultSet.getString("EV_PAT_TEL1"));
							patRegResponseBean.setTel2Text(resultSet.getString("EV_PAT_TEL2"));
							patRegResponseBean.setFatNameText(resultSet.getString("EV_PAT_FAT_NAME"));
							
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
 		
		try(Connection dbConn = dbConnPool.getConnection()){
 					 			
 			dbConn.setAutoCommit(false);
 			
 			QueryRunner qRunner = new QueryRunner();
 			 			
 			qRunner.update(dbConn , 
 					"update EV_PAT set EV_PAT_NAME = ?,EV_PAT_DOB=?,EV_PAT_ADDR1=?,EV_PAT_ADDR2=?,EV_PAT_STATE=?,EV_PAT_PIN_CODE=?,EV_PAT_SEX=?,EV_PAT_TEL1=?,EV_PAT_TEL2 = ?,EV_PAT_FAT_NAME=? where EV_PAT_ID = ? "  , 
 					new Object[]{patRegRequestBean.getNameText() , patRegRequestBean.getYearText()+"-"+patRegRequestBean.getMonthText()+"-"+patRegRequestBean.getDateText() , patRegRequestBean.getAddress1Text() , patRegRequestBean.getAddress2Text() , patRegRequestBean.getStateText() , patRegRequestBean.getPincode() , patRegRequestBean.getSex() ,patRegRequestBean.getTel1Text() , patRegRequestBean.getTel2Text() ,patRegRequestBean.getFatNameText() , patRegRequestBean.getNameId() });
 			
 			patRegResponseBean.setStatus("success");
 			patRegResponseBean.setMessage("Saved ...");
 			
 			dbConn.commit();
 			
 		}  catch(Exception e) {
 			
 			throw e;
 			 			
 		} 
 		
    	return patRegResponseBean;
	}

	@Override
	public PatRegResponseBean uploadPatDtlsToDb(PatRegRequestBean patRegRequestBean , boolean isLastRow) throws Exception {
	
		// logger.debug("inside uploadPatDtlsToDb "+patRegRequestBean);
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		patRegResponseBean.setStatus("success");
		patRegResponseBean.setMessage("Saved ...");
		patRegResponseBean.setRowCnt( 0 );
		
		try {
			
			if( threadLocal.get() == null ){
				// Initialize
			
				PatRegThreadBean patRegThreadBean = new PatRegThreadBean();
				
				Connection dbConn = dbConnPool.getConnection();
				dbConn.setAutoCommit(false);
				
				patRegThreadBean.setDbConn(dbConn);
				
				PreparedStatement pStat = dbConn.prepareStatement("insert into EV_PAT(EV_PAT_NAME,EV_PAT_DOB,EV_PAT_ADDR1,EV_PAT_ADDR2,EV_PAT_STATE,EV_PAT_PIN_CODE,EV_PAT_SEX,EV_PAT_TEL1,EV_PAT_TEL2,EV_PAT_FAT_NAME) values( ?,?,?,?,?,?,?,?,?,? ) ");
				patRegThreadBean.setpStat(pStat);
				
				patRegThreadBean.setNameAddr1FatNameSet(new HashSet<String>());
				
				threadLocal.set(patRegThreadBean);
				
				addToPreparedStatement( patRegRequestBean );
				
			} else if( threadLocal.get().getRowsToInsert() % 5 == 0 ) {
				
				addToPreparedStatement( patRegRequestBean );
				
				PatRegThreadBean patRegThreadBean = threadLocal.get();
				int insertedRowCnt = patRegThreadBean.getpStat().executeBatch().length;
				
				patRegResponseBean.setRowCnt( insertedRowCnt );
					
				patRegThreadBean.getDbConn().commit();
				
				patRegThreadBean.getDbConn().setAutoCommit(false);
				
				patRegThreadBean.getpStat().clearBatch();
				patRegThreadBean.getpStat().clearParameters();	
				
				patRegThreadBean.getNameAddr1FatNameSet().clear();
			
			} else if ( isLastRow ) {
				
				if(threadLocal.get() == null){
					
					patRegResponseBean.setStatus("success");
					patRegResponseBean.setMessage("No Data To Save ...");
					patRegResponseBean.setRowCnt( 0 );
					return patRegResponseBean;
					
				}
				
				PatRegThreadBean patRegThreadBean = threadLocal.get();
				
				int[] insertedRowCnt = patRegThreadBean.getpStat().executeBatch();
				patRegResponseBean.setRowCnt( insertedRowCnt.length );
				
				logger.debug("rows inserted => "+insertedRowCnt.length);
				
				// All resources cleaned up
				
				patRegThreadBean.getNameAddr1FatNameSet().clear();
				
				patRegThreadBean.getDbConn().commit();
				
				DbUtils.closeQuietly( patRegThreadBean.getDbConn() , patRegThreadBean.getpStat(), null);
								
				threadLocal.remove();
					
			}  else {
				
				addToPreparedStatement( patRegRequestBean );
			
			}
		
		} catch (Exception e) {
		
			if(isLastRow){
				if(threadLocal.get() != null){
					
					PatRegThreadBean patRegThreadBean = threadLocal.get();
					
					DbUtils.closeQuietly( patRegThreadBean.getDbConn() , patRegThreadBean.getpStat(), null);
					
					threadLocal.remove();
					
				}
				
			}
			
			throw e;
		}
		
		return patRegResponseBean;
		
	}

	private void addToPreparedStatement( PatRegRequestBean patRegRequestBean ) throws Exception {
		
		if( isExistsInDb(patRegRequestBean) ){
			return;
		}
		
		if( !isExistsMoreInExcel(patRegRequestBean) ){
			return;
		}
		
		PatRegThreadBean patRegThreadBean = threadLocal.get();

		PreparedStatement pStat = patRegThreadBean.getpStat();
		
		pStat.setString( 1, patRegRequestBean.getNameText() );
		pStat.setString( 2, patRegRequestBean.getDateOfBirth() );
		pStat.setString( 3, patRegRequestBean.getAddress1Text() );
		pStat.setString( 4, patRegRequestBean.getAddress2Text() );
		pStat.setString( 5, patRegRequestBean.getStateText());
		pStat.setString( 6, patRegRequestBean.getPincode() );
		pStat.setString( 7, patRegRequestBean.getSex() );
		pStat.setString( 8, patRegRequestBean.getTel1Text());
		pStat.setString( 9, patRegRequestBean.getTel2Text());
		pStat.setString( 10, patRegRequestBean.getFatNameText() );
				
		pStat.addBatch();

		// Increment rows to insert
		patRegThreadBean.setRowsToInsert( patRegThreadBean.getRowsToInsert() + 1 );

	}

	private boolean isExistsInDb(PatRegRequestBean patRegRequestBean) throws Exception {
		
		PatRegThreadBean patRegThreadBean = threadLocal.get();
		
		QueryRunner qRunner = new QueryRunner();
			
		int rowcount = qRunner.query( patRegThreadBean.getDbConn() , "select count(*) from EV_PAT group by EV_PAT_NAME , EV_PAT_ADDR1 , EV_PAT_FAT_NAME having lower(EV_PAT_NAME) = ? and lower(EV_PAT_ADDR1) = ? and lower(EV_PAT_FAT_NAME) = ?", 
			new ResultSetHandler<Integer>(){

				public Integer handle(ResultSet resultSet) throws SQLException {
				
					if ( !resultSet.next() ){
						return Integer.valueOf( 0 );
					}
					
					return Integer.valueOf( resultSet.getInt(1) );
				}
		},patRegRequestBean.getNameText().toLowerCase(),patRegRequestBean.getAddress1Text().toLowerCase(),patRegRequestBean.getFatNameText().toLowerCase());
					
		return rowcount == 0 ? false : true ;
	}
	
	private boolean isExistsMoreInExcel(PatRegRequestBean patRegRequestBean) throws Exception {
		
		Set<String> nameAddr1FatNameSet = threadLocal.get().getNameAddr1FatNameSet();
		
		// set already contains entry , then returns false
					
		return nameAddr1FatNameSet.add( patRegRequestBean.getNameText().concat( patRegRequestBean.getAddress1Text() ).concat( patRegRequestBean.getFatNameText() ).toLowerCase() );
	}
	
	@Override
	public PatRegResponseBean getExcelRowsOnUpload(PatRegRequestBean patRegRequestBean) throws Exception {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		Workbook workbook = null;	
		
		try {
			
			workbook = Workbook.getWorkbook(new File( patRegRequestBean.getXlFilePath() ));
			
			Sheet sheet = workbook.getSheet(1);
			
			patRegResponseBean.setExcelRowNum( String.valueOf(sheet.getRows()) );
			
 		}  catch(Exception e) {
 			
 			throw e;
 			 			
 		} finally {
 			workbook.close();
 		}
		
		return patRegResponseBean;
	}

	@Override
	public PatRegResponseBean getExcelCellDtls(PatRegRequestBean patRegRequestBean, int rowNum) throws Exception {
		
		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
		
		Workbook workbook = null;	
		
		try {
			
			workbook = Workbook.getWorkbook(new File( patRegRequestBean.getXlFilePath() ));
			
			Sheet sheet = workbook.getSheet(1);
			
			Cell[] cells = sheet.getRow(rowNum);
			
			patRegResponseBean.setNameText(cells[0].getContents().trim());
			patRegResponseBean.setDateOfBirth(cells[1].getContents().trim());
			patRegResponseBean.setAddress1Text(cells[2].getContents().trim());
			patRegResponseBean.setAddress2Text(cells[3].getContents().trim());
			patRegResponseBean.setStateText(cells[4].getContents().trim());
			patRegResponseBean.setPincode(cells[5].getContents().trim());
			patRegResponseBean.setSex(cells[6].getContents().trim());
			patRegResponseBean.setTel1Text(cells[7].getContents().trim());
			patRegResponseBean.setTel2Text(cells[8].getContents().trim());
			patRegResponseBean.setFatNameText(cells[9].getContents().trim());
			
 		}  catch(Exception e) {
 			
 			throw e;
 			 			
 		} finally {
 			workbook.close();
 		}
		
		return patRegResponseBean;
	}
	
}

