package com.aj.evaidya.docreg.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;
import com.aj.evaidya.docreg.dao.DocRegDao;

public class DocRegDaoImpl implements DocRegDao{
	
	private static final Logger logger = Logger.getLogger(DocRegDaoImpl.class);

	@Override
	public DocRegResponseBean insertDocRegDtls(DocRegRequestBean docRegRequestBean) throws Exception {
		
		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
		
		try(Connection dbConn = DriverManager.getConnection( docRegRequestBean.getDbUrl(), docRegRequestBean.getDbUsername() , docRegRequestBean.getDbPwd() )){
			
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
 					"insert into EV_DOC(EV_DOC_NAME,EV_DOC_QUALI,EV_DOC_ADDR1,EV_DOC_ADDR2,EV_DOC_STATE,EV_DOC_PIN_CODE,EV_DOC_TEL1,EV_DOC_TEL2,EV_DOC_EMAIL) values ( ?,?,?,?,?,?,?,?,? ) "  , 
 					new Object[]{docRegRequestBean.getNameText() , docRegRequestBean.getQualiText() , docRegRequestBean.getAddress1Text() , docRegRequestBean.getAddress2Text() , docRegRequestBean.getStateId() , docRegRequestBean.getPincode() ,docRegRequestBean.getTel1Text() , docRegRequestBean.getTel2Text() , docRegRequestBean.getEmail() });
 			 			
 			dbConn.commit();
		
		} catch(Exception e ){
 			
			throw e;	
		}
		
		return docRegResponseBean;
	}

}
