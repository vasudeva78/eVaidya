package com.aj.evaidya.docreg.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;
import com.aj.evaidya.docreg.dao.DocRegDao;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DocRegDaoImpl implements DocRegDao{
	
	private static final Logger logger = Logger.getLogger(DocRegDaoImpl.class);
	
	private JdbcConnectionPool dbConnPool;

	@Inject
	public void setDbConnPool(@Named("dbConnPool") JdbcConnectionPool dbConnPool) {
		this.dbConnPool = dbConnPool;
	}

	@Override
	public DocRegResponseBean saveDocDtls(DocRegRequestBean docRegRequestBean) throws Exception {
		
		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
		
		docRegResponseBean.setStatus("success");
		docRegResponseBean.setMessage("Saved ...");
		
		try(Connection dbConn = dbConnPool.getConnection()){
			
			dbConn.setAutoCommit(false);
			
			QueryRunner qRunner = new QueryRunner();
 			
 			int docNameExistsRowcount = qRunner.query(dbConn , "select count(*) from EV_DOC where EV_DOC_NAME='" + docRegRequestBean.getNameText() + "'" , 
					new ResultSetHandler<Integer>(){

						public Integer handle(ResultSet resultSet) throws SQLException {
						
							resultSet.next();
							
							return Integer.valueOf( resultSet.getInt(1) );
						}
				});
 			
 			logger.debug("docNameExistsRowcount => "+docNameExistsRowcount);
 			
 			if(docNameExistsRowcount > 0){
 				
 				docRegResponseBean.setStatus("errorNameExists");
     			docRegResponseBean.setMessage("Name already Exists ...");
     			
     			return docRegResponseBean;
 			}
 			
 			qRunner.update(dbConn , 
 					"insert into EV_DOC(EV_DOC_NAME,EV_DOC_QUALI,EV_DOC_DESIG,EV_DOC_ADDR1,EV_DOC_ADDR2,EV_DOC_CNSULT,EV_DOC_STATE,EV_DOC_PIN_CODE,EV_DOC_TEL1,EV_DOC_TEL2,EV_DOC_EMAIL,EV_DOC_HOSP) values ( ?,?,?,?,?,?,?,?,?,?,?,? ) "  , 
 					new Object[]{docRegRequestBean.getNameText() , docRegRequestBean.getQualiText() , docRegRequestBean.getDesigText() , docRegRequestBean.getAddress1Text() , docRegRequestBean.getAddress2Text() , docRegRequestBean.getConsultText() , docRegRequestBean.getStateId() , docRegRequestBean.getPincode() , docRegRequestBean.getTel1Text() , docRegRequestBean.getTel2Text() ,docRegRequestBean.getEmail() , docRegRequestBean.getHospText() });
 			 			
 			dbConn.commit();
		
		} catch(Exception e ){
 			
			throw e;
		}
		
		return docRegResponseBean;
	}

	@Override
	public Map<String, String> getDocNameDtls() throws Exception {
		
		Map<String,String> docNameListMap = new LinkedHashMap<String,String>();
		
		try(Connection dbConn = dbConnPool.getConnection() ){
						
			QueryRunner qRunner = new QueryRunner();
 			
 			docNameListMap = qRunner.query(dbConn , "select EV_DOC_ID , EV_DOC_NAME from EV_DOC order by EV_DOC_NAME" , 
					new ResultSetHandler<Map<String, String>>(){

						public Map<String, String> handle(ResultSet resultSet) throws SQLException {
							
							Map<String, String> nameListMap = new LinkedHashMap<String, String>() ;
							
							nameListMap.put("--","-- Select --");
							
							while( resultSet.next() ){
								nameListMap.put( resultSet.getString("EV_DOC_ID") , resultSet.getString("EV_DOC_NAME") );
							}
							
							return nameListMap;
						}
				});
		
		} catch(Exception e ){
 			
			throw e;
		}
		
		return docNameListMap;
	}

	@Override
	public DocRegResponseBean getDocDtls(DocRegRequestBean docRegRequestBean) throws Exception {
 		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
 		
 		String nameId = docRegRequestBean.getNameId();
 		if (nameId.equalsIgnoreCase("--")){
 			return docRegResponseBean;
 		}
 		
 		try(Connection dbConn = dbConnPool.getConnection() ){
 									
 			QueryRunner qRunner = new QueryRunner();
 			
 			docRegResponseBean = qRunner.query(dbConn , "select EV_DOC_ID , EV_DOC_NAME , EV_DOC_QUALI , EV_DOC_DESIG , EV_DOC_ADDR1, EV_DOC_ADDR2 , EV_DOC_CNSULT , EV_DOC_STATE , EV_DOC_PIN_CODE , EV_DOC_TEL1 , EV_DOC_TEL2 , EV_DOC_EMAIL , EV_DOC_HOSP from EV_DOC where EV_DOC_ID = '"+nameId+"'" , 
					new ResultSetHandler<DocRegResponseBean>(){

						public DocRegResponseBean handle(ResultSet resultSet) throws SQLException {
							
							DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
							
							resultSet.next();
							docRegResponseBean.setNameText(resultSet.getString("EV_DOC_NAME"));
							docRegResponseBean.setQualiText(resultSet.getString("EV_DOC_QUALI"));
							docRegResponseBean.setDesigText(resultSet.getString("EV_DOC_DESIG"));
							docRegResponseBean.setAddress1Text(resultSet.getString("EV_DOC_ADDR1"));
							docRegResponseBean.setAddress2Text(resultSet.getString("EV_DOC_ADDR2"));
							docRegResponseBean.setConsultText(resultSet.getString("EV_DOC_CNSULT"));
							docRegResponseBean.setStateId(resultSet.getString("EV_DOC_STATE"));
							docRegResponseBean.setPincode(resultSet.getString("EV_DOC_PIN_CODE"));
							docRegResponseBean.setTel1Text(resultSet.getString("EV_DOC_TEL1"));
							docRegResponseBean.setTel2Text(resultSet.getString("EV_DOC_TEL2"));
							docRegResponseBean.setEmail(resultSet.getString("EV_DOC_EMAIL"));
							docRegResponseBean.setHospText(resultSet.getString("EV_DOC_HOSP"));
							
							return docRegResponseBean;
						}
				});
 			
 		}  catch(Exception e) {
 			
 			throw e;
 			
 		} 
    	 
    	return docRegResponseBean;
	}

	@Override
	public DocRegResponseBean updateDocDtls(DocRegRequestBean docRegRequestBean) throws Exception {

		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
		
		docRegResponseBean.setStatus("success");
		docRegResponseBean.setMessage("Saved ...");
			
		try(Connection dbConn = dbConnPool.getConnection() ){
 			
 			dbConn.setAutoCommit(false);
 			
 			QueryRunner qRunner = new QueryRunner();
 			     			
 			qRunner.update(dbConn , 
 					"update EV_DOC set EV_DOC_NAME = ? , EV_DOC_QUALI = ? , EV_DOC_DESIG = ?, EV_DOC_ADDR1 = ? ,EV_DOC_ADDR2 = ? , EV_DOC_CNSULT = ? , EV_DOC_STATE = ? , EV_DOC_PIN_CODE = ? ,EV_DOC_TEL1 = ? ,EV_DOC_TEL2 = ? , EV_DOC_EMAIL = ? , EV_DOC_HOSP = ? , EV_ENTRY_TIME = ( select now() ) where EV_DOC_ID = ? "  , 
 					new Object[]{docRegRequestBean.getNameText() ,  docRegRequestBean.getQualiText(),docRegRequestBean.getDesigText(), docRegRequestBean.getAddress1Text() , docRegRequestBean.getAddress2Text() ,docRegRequestBean.getConsultText() , docRegRequestBean.getStateId() , docRegRequestBean.getPincode() ,docRegRequestBean.getTel1Text() , docRegRequestBean.getTel2Text() , docRegRequestBean.getEmail() ,docRegRequestBean.getHospText() , docRegRequestBean.getNameId() });
 			
 			docRegResponseBean.setStatus("success");
 			docRegResponseBean.setMessage("Saved ...");
 			
 			dbConn.commit();
 			
 		}  catch(Exception e) {
 			
 			throw e;
 			
 		}     	 
    	
		return docRegResponseBean;
     }

}
