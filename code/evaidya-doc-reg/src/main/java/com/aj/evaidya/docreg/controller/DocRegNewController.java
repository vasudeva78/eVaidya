package com.aj.evaidya.docreg.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

import com.aj.evaidya.common.bo.CommonControlsBo;
import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;

public class DocRegNewController extends AbstractDocRegController {
	
	private static final Logger logger = Logger.getLogger( DocRegNewController.class );
	
	protected void populateFieldsOnIinit() {			
		super.populateStateField();
	}
	
	protected DocRegRequestBean populateDocRegRequestBean(){
		
		DocRegRequestBean docReqBean = new DocRegRequestBean();
		
		docReqBean.setNameText( nameTextField.getText().trim().substring(0, Math.min(100, nameTextField.getText().trim().length() )) );
		docReqBean.setQualiText( qualiTextField.getText().trim().substring(0, Math.min(100, qualiTextField.getText().trim().length() )) );
		docReqBean.setAddress1Text( address1TextField.getText().substring(0, Math.min(2000, address1TextField.getText().trim().length())) );
		docReqBean.setAddress2Text( address2TextField.getText().substring(0, Math.min(2000, address2TextField.getText().trim().length() )) );
		docReqBean.setStateId( stateCode );
		docReqBean.setPincode( pincodeTextField.getText().substring(0, Math.min(10, pincodeTextField.getText().trim().length())) );
		docReqBean.setTel1Text( tel1TextField.getText().substring(0, Math.min(100, tel1TextField.getText().trim().length() )) );
		docReqBean.setTel2Text( tel2TextField.getText().substring(0, Math.min(100, tel2TextField.getText().trim().length() )) );
		docReqBean.setEmail( emailTextField.getText().substring(0, Math.min(200, emailTextField.getText().trim().length())) );
		
		return docReqBean;
		
	}
	
	protected void saveDbTask(final DocRegRequestBean docReqBean){
		
		final Task<DocRegResponseBean> saveTask = new Task<DocRegResponseBean>() { 
			
	         @Override protected DocRegResponseBean call() throws Exception {     		
	     		Connection dbConn = null;
    					
	     		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
	     		
	     		try {
	     						
	     			dbConn = DriverManager.getConnection( CONN_URL, CONN_UNAME , CONN_PWD );
	     			
	     			logger.debug("after getting db Conn => "+dbConn);
	     			
	     			dbConn.setAutoCommit(false);
	     			
	     			QueryRunner qRunner = new QueryRunner();
	     			
	     			int docNameExistsRowcount = qRunner.query(dbConn , "select count(*) from EV_DOC where EV_DOC_NAME='" + docReqBean.getNameText() + "'" , 
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
	     					new Object[]{docReqBean.getNameText() , docReqBean.getQualiText() , docReqBean.getAddress1Text() , docReqBean.getAddress2Text() , docReqBean.getStateId() , docReqBean.getPincode() ,docReqBean.getTel1Text() , docReqBean.getTel2Text() , docReqBean.getEmail() });
	     			
	     			docRegResponseBean.setStatus("success");
	     			docRegResponseBean.setMessage("Saved ...");
	     			
	     			dbConn.commit();
	     			
	     		}  catch(Exception e) {
	     			
	     			if ( dbConn != null) {
	     				dbConn.rollback();
	     			}
	     			
	     			logger.error("Error saving doc details lists " ,e);
	     			
	     			docRegResponseBean.setStatus("error");
	     			docRegResponseBean.setMessage("Not Saved ...");
	     			
	     		} finally {
	     			     				
	     			logger.debug("releasing connection");
	     			DbUtils.closeQuietly(dbConn);
	     		}
	        	 
	        	 return docRegResponseBean;
	         }
	         
	     };
		
	     saveTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					DocRegResponseBean docRegResponseBean = saveTask.getValue();
					
					if( "success".equals(docRegResponseBean.getStatus() ) ){
						CommonControlsBo.showFinalSuccessStatus( statusLabel , docRegResponseBean.getMessage() );
					
					} else if( "errorNameExists".equals(docRegResponseBean.getStatus() ) ){
						//CommonControlsBo.showFinalFailureStatus( statusLabel , docRegResponseBean.getMessage() );
						CommonControlsBo.showErrorMessage(statusLabel, nameTextField, docRegResponseBean.getMessage());
					
					} else {
						CommonControlsBo.showFinalFailureStatus( statusLabel , docRegResponseBean.getMessage() );
					}
							
				}
				
			}	 
	     });
	     
		new Thread( saveTask ).start();
	     
	}

	@Override
	protected void abstractResetFields() {
		
	}
}
