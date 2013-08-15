package com.aj.evaidya.patreg.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

import com.aj.evaidya.common.bo.CommonControlsBo;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public class PatRegEditController extends AbstractPatRegController {
	
	private static final Logger logger = Logger.getLogger( PatRegEditController.class );
	
	@FXML
	private ChoiceBox patNameChoiceBox;
	
	private String patNameId;
	private List<String> patNameList;
	private List<String> patNameIdList;
	
	protected void populateFieldsOnIinit() {			
		
		abstractResetFields();
		
		stateChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
			new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
					
					logger.debug("inside chnaged");
					
					if (newValue.intValue() == -1){
						return;
					}
					
					stateCode = stateIdList.get( newValue.intValue() );
										
				}
		});
		
		patNameChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
			new ChangeListener<Number>() {
	
				@Override
				public void changed(ObservableValue<? extends Number> ov,
						Number oldValue, Number newValue) {
					
					if ( newValue.intValue() == -1 ){
						return;
					}
					
					patNameId = patNameIdList.get( newValue.intValue() );
					
					logger.debug("nameId => "+patNameId);

					populateAllFields(patNameId);
												
				}
		});

		// populate name Choice box. Not called until name state box has completed loading
		 final Task patNameChoiceBoxTask = patNamesTask();
			
		 patNameChoiceBoxTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					Map<String, String> nameListMap = (Map<String, String>) patNameChoiceBoxTask.getValue();
					
					patNameList = new ArrayList ( nameListMap.values() );
					patNameIdList = new ArrayList ( nameListMap.keySet() ); 

					patNameChoiceBox.getItems().addAll( patNameList );
					
					patNameChoiceBox.setDisable(false);
					
				}
				
			}	 
	     });
				 
		// populate name State box
		final Task stateTask = stateTask();
		
		stateTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					Map<String, String> stateListMap = (Map<String, String>) stateTask.getValue();
					
					stateIdList = new ArrayList( stateListMap.keySet() );
					stateList = new ArrayList( stateListMap.values()  );
					
					stateChoiceBox.getItems().addAll( stateList );
					
					stateChoiceBox.setValue("-- Select --");
					
					// start populating Name choice box
					
					new Thread( patNameChoiceBoxTask ).start();
				}
				
			}	 
	     });
		     
		 new Thread(stateTask).start();
	}
	
    private Task<Map<String, String>> stateTask(){
		
		return new Task<Map<String, String>>() {
	         @Override protected Map<String, String> call() throws Exception {
	        	 
	        	 Connection dbConn = null;
	     		Map<String, String> stateListMap = null ;
	     					
	     		try {
	     						
	     			dbConn = DriverManager.getConnection( CONN_URL, CONN_UNAME , CONN_PWD );
	     			
	     			logger.debug("after getting db Conn => "+dbConn);
	     			
	     			QueryRunner qRunner = new QueryRunner();
	     			
	     			stateListMap = qRunner.query(dbConn , "select EV_ID , EV_GRP_COD , EV_GRP_VAL from EV_MASTER order by EV_ID" , 
	     					new ResultSetHandler<Map<String, String>>(){

	     						public Map<String, String> handle(ResultSet resultSet) throws SQLException {
	     							
	     							Map<String, String> stateListMap = new LinkedHashMap<String, String>() ;
	     							
	     							stateListMap.put("--","-- Select --");
	     							
	     							while( resultSet.next() ){
	     								stateListMap.put( resultSet.getString("EV_GRP_COD") , resultSet.getString("EV_GRP_VAL") );
	     							}
	     							
	     							return stateListMap;
	     						}
	     				});
	     				     			
	     		}  catch(Exception e) {
	     			
	     			logger.error("Error fetching Column lists " ,e);
	     			stateListMap = new HashMap<String, String>();
	     			
	     		} finally {
	     			DbUtils.closeQuietly(dbConn);
	     		}
				
	     		return stateListMap;
	        	 
	         }
	         
	     };
	     
	}
	
   private Task<Map<String, String>> patNamesTask(){
		
		return new Task<Map<String, String>>() {
	         @Override protected Map<String, String> call() throws Exception {
	        	 
	        	Connection dbConn = null;
	        	
	        	Map<String,String> docNameListMap = new LinkedHashMap<String,String>();
	     					
	     		try {
						
	     			dbConn = DriverManager.getConnection( CONN_URL, CONN_UNAME , CONN_PWD );
	     			
	     			logger.debug("after getting db Conn => "+dbConn);
	     			
	     			QueryRunner qRunner = new QueryRunner();
	     			
	     			docNameListMap = qRunner.query(dbConn , "select EV_PAT_ID , EV_PAT_NAME from EV_PAT order by EV_PAT_NAME" , 
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
	     			
	     		}  catch(Exception e) {
	     			
	     			logger.error("Error saving doc details lists " ,e);
	     			
	     		} finally {

	     			DbUtils.closeQuietly(dbConn);
	     		}
	     		
	     		return docNameListMap;
	        	 
	         }
	         
	     };
	     
	}
   
   private void populateAllFields(final String patNameId){

		final Task <PatRegResponseBean> populateAllFieldsTask = new Task <PatRegResponseBean>() { 
			
	         @Override protected PatRegResponseBean call() throws Exception {     		
	     		Connection dbConn = null;
  					
	     		PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
	     		
	     		if (patNameId.equalsIgnoreCase("--")){
	     			return patRegResponseBean;
	     		}
	     		
	     		try {
	     						
	     			dbConn = DriverManager.getConnection( CONN_URL, CONN_UNAME , CONN_PWD );
	     			
	     			logger.debug("after getting db Conn => "+dbConn);
	     			
	     			QueryRunner qRunner = new QueryRunner();
	     			
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
	     			
	     		}  catch(Exception e) {
	     			
	     			logger.error("Error fetching doc details " ,e);
	     			
	     		} finally {
	     			     				
	     			logger.debug("releasing connection");
	     			DbUtils.closeQuietly(dbConn);
	     		}
	        	 
	        	 return patRegResponseBean;
	         }
	         
	     };
		
	     populateAllFieldsTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					PatRegResponseBean patRegResponseBean = populateAllFieldsTask.getValue();
					
					nameTextField.setText(patRegResponseBean.getNameText());
					
					dateTextField.setText(patRegResponseBean.getPatDay());
					monthChoiceBox.setValue(patRegResponseBean.getPatMon());
					yearTextField.setText(patRegResponseBean.getPatYear());
					
					address1TextField.setText(patRegResponseBean.getAddress1Text());
					address2TextField.setText(patRegResponseBean.getAddress2Text());
									
					int stateIdIndx = stateIdList.indexOf( patRegResponseBean.getStateId() );
					
					stateChoiceBox.setValue( stateIdIndx == -1 ? "-- Select --" : stateList.get(stateIdIndx)  );
					
					pincodeTextField.setText(patRegResponseBean.getPincode());
			
					tel1TextField.setText(patRegResponseBean.getTel1Text());
					tel2TextField.setText(patRegResponseBean.getTel2Text());
										
					logger.debug("stateIdIndx => "+stateIdIndx);
					
					if (stateIdIndx != -1){
											
						nameTextField.setEditable(true);
						dateTextField.setEditable(true);
						monthChoiceBox.setDisable(false);
						yearTextField.setEditable(true);
						address1TextField.setEditable(true);
						address2TextField.setEditable(true);
						stateChoiceBox.setDisable(false);
						pincodeTextField.setEditable(true);
						tel1TextField.setEditable(true);
						tel2TextField.setEditable(true);
						
						nameTextField.requestFocus();
						
					} else {
						
						abstractResetFields();
						
					}
					 
				}
				
			}	 
	     });
	     
		new Thread( populateAllFieldsTask ).start();
		
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
	     			
	     			qRunner.update(dbConn , 
	     					"update EV_PAT set EV_PAT_NAME = ?,EV_PAT_DOB=?,EV_PAT_ADDR1=?,EV_PAT_ADDR2=?,EV_PAT_STATE=?,EV_PAT_PIN_CODE=?,EV_PAT_TEL1=?,EV_PAT_TEL2 = ? where EV_PAT_ID = ? "  , 
	     					new Object[]{patRegRequestBean.getNameText() , patRegRequestBean.getYearText()+"-"+patRegRequestBean.getMonthText()+"-"+patRegRequestBean.getDateText() , patRegRequestBean.getAddress1Text() , patRegRequestBean.getAddress2Text() , patRegRequestBean.getStateId() , patRegRequestBean.getPincode() ,patRegRequestBean.getTel1Text() , patRegRequestBean.getTel2Text() , patNameId  });
	     			
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
		nameTextField.setEditable(false);
		patNameChoiceBox.getSelectionModel().selectFirst();
		
		dateTextField.setEditable(false);
		monthChoiceBox.getSelectionModel().selectFirst();
		monthChoiceBox.setDisable(true);
		yearTextField.setEditable(false);
		
		address1TextField.setEditable(false);
		address2TextField.setEditable(false);
		
		stateChoiceBox.setDisable(true);
		pincodeTextField.setEditable(false);
		
		tel1TextField.setEditable(false);
		tel2TextField.setEditable(false);
	}
}
