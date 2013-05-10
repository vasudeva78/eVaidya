package com.aj.evaidya.patreg.controller;

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
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public class PatRegNewController extends AbstractPatRegController {
	
	private static final Logger logger = Logger.getLogger( PatRegNewController.class );
	
	protected void populateFieldsOnIinit() {			
		super.populateStateField();
	}
	
	protected PatRegRequestBean populatePatRegRequestBean(){
		
		PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
		
		patRegRequestBean.setNameText( nameTextField.getText().trim().substring(0, Math.min(100, nameTextField.getText().trim().length() )) );
		patRegRequestBean.setDateText( dateTextField.getText().trim() );
		int monthIndx = monthChoiceBox.getSelectionModel().getSelectedIndex();
		patRegRequestBean.setMonthText( monthIndx  < 10 ? "0"+monthIndx : monthIndx+"" );
		patRegRequestBean.setYearText( yearTextField.getText().trim() );
		patRegRequestBean.setAddress1Text( address1TextField.getText().substring(0, Math.min(2000, address1TextField.getText().trim().length())) );
		patRegRequestBean.setAddress2Text( address2TextField.getText().substring(0, Math.min(2000, address2TextField.getText().trim().length() )) );
		patRegRequestBean.setStateId( stateCode );
		patRegRequestBean.setPincode( pincodeTextField.getText().substring(0, Math.min(10, pincodeTextField.getText().trim().length())) );
		patRegRequestBean.setTel1Text( tel1TextField.getText().substring(0, Math.min(100, tel1TextField.getText().trim().length() )) );
		patRegRequestBean.setTel2Text( tel2TextField.getText().substring(0, Math.min(100, tel2TextField.getText().trim().length() )) );
		
		return patRegRequestBean;
		
	}
	
	protected void saveDbTask(final PatRegRequestBean patRegRequestBean){
		
		final Task<PatRegResponseBean> saveTask = new Task<PatRegResponseBean>() { 
			
	         @Override protected PatRegResponseBean call() throws Exception {     		
	     		Connection dbConn = null;
    					
	     		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
	     		
	     		try {
	     						
	     			dbConn = DriverManager.getConnection( CONN_URL, CONN_UNAME , CONN_PWD );
	     			
	     			logger.debug("after getting db Conn => "+dbConn);
	     			
	     			dbConn.setAutoCommit(false);
	     			
	     			QueryRunner qRunner = new QueryRunner();
	     			
	     			int patNameExistsRowcount = qRunner.query(dbConn , "select count(*) from EV_PAT where EV_PAT_NAME='" + patRegRequestBean.getNameText() + "'" , 
	    					new ResultSetHandler<Integer>(){

	    						public Integer handle(ResultSet resultSet) throws SQLException {
    							
	    							resultSet.next();
	    							
	    							return Integer.valueOf( resultSet.getInt(1) );
	    						}
	    				});
	     			
	     			logger.debug("patNameExistsRowcount => "+patNameExistsRowcount);
	     			
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
	     			
	     			if ( dbConn != null) {
	     				dbConn.rollback();
	     			}
	     			
	     			logger.error("Error saving doc details lists " ,e);
	     			
	     			patRegResponseBean.setStatus("error");
	     			patRegResponseBean.setMessage("Not Saved ...");
	     			
	     		} finally {
	     			     				
	     			logger.debug("releasing connection");
	     			DbUtils.closeQuietly(dbConn);
	     		}
	        	 
	        	 return patRegResponseBean;
	         }
	         
	     };
		
	     saveTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					PatRegResponseBean patRegResponseBean = saveTask.getValue();
					
					if( "success".equals(patRegResponseBean.getStatus() ) ){
						CommonControlsBo.showFinalSuccessStatus( statusLabel , patRegResponseBean.getMessage() );
					
					} else if( "errorNameExists".equals(patRegResponseBean.getStatus() ) ){
						//CommonControlsBo.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
						CommonControlsBo.showErrorMessage(statusLabel, nameTextField, patRegResponseBean.getMessage());
					
					} else {
						CommonControlsBo.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
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
