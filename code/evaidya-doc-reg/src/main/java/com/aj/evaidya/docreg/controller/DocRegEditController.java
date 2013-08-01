package com.aj.evaidya.docreg.controller;

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
import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;

public class DocRegEditController extends AbstractDocRegController {

	private static final Logger logger = Logger.getLogger( DocRegEditController.class );
	
	@FXML
	private ChoiceBox nameChoiceBox;
	
	private String nameId;
	private List<String> nameList;
	private List<String> nameIdList;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void populateFieldsOnIinit() {
		
		nameTextField.setEditable(false);
		qualiTextField.setEditable(false);
		address1TextField.setEditable(false);
		address2TextField.setEditable(false);
		stateChoiceBox.setDisable(true);
		pincodeTextField.setEditable(false);
		emailTextField.setEditable(false);
		tel1TextField.setEditable(false);
		tel2TextField.setEditable(false);
		nameChoiceBox.setDisable(true);
		
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
		
		nameChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
			new ChangeListener<Number>() {
	
				@Override
				public void changed(ObservableValue<? extends Number> ov,
						Number oldValue, Number newValue) {
					
					if ( newValue.intValue() == -1 ){
						return;
					}
					
					nameId = nameIdList.get( newValue.intValue() );
					
					logger.debug("nameId => "+nameId);

					populateAllFields(nameId);
												
				}
		});

		// populate name Choice box. Not called until name state box has completed loading
		 final Task nameChoiceBoxTask = namesTask();
			
		 nameChoiceBoxTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					Map<String, String> nameListMap = (Map<String, String>) nameChoiceBoxTask.getValue();
					
					nameList = new ArrayList ( nameListMap.values() );
					nameIdList = new ArrayList ( nameListMap.keySet() ); 

					nameChoiceBox.getItems().addAll( nameList );
					
					nameChoiceBox.setDisable(false);
					
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
					
					new Thread( nameChoiceBoxTask ).start();
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
	
   private Task<Map<String, String>> namesTask(){
		
		return new Task<Map<String, String>>() {
	         @Override protected Map<String, String> call() throws Exception {
	        	 
	        	Connection dbConn = null;
	        	
	        	Map<String,String> docNameListMap = new LinkedHashMap<String,String>();
	     					
	     		try {
						
	     			dbConn = DriverManager.getConnection( CONN_URL, CONN_UNAME , CONN_PWD );
	     			
	     			logger.debug("after getting db Conn => "+dbConn);
	     			
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
	     			
	     		}  catch(Exception e) {
	     			
	     			logger.error("Error saving doc details lists " ,e);
	     			
	     		} finally {

	     			DbUtils.closeQuietly(dbConn);
	     		}
	     		
	     		return docNameListMap;
	        	 
	         }
	         
	     };
	     
	}

	private void populateAllFields(final String nameId){

		final Task <DocRegResponseBean> populateAllFieldsTask = new Task <DocRegResponseBean>() { 
			
	         @Override protected DocRegResponseBean call() throws Exception {     		
	     		Connection dbConn = null;
   					
	     		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
	     		
	     		if (nameId.equalsIgnoreCase("--")){
	     			return docRegResponseBean;
	     		}
	     		
	     		try {
	     						
	     			dbConn = DriverManager.getConnection( CONN_URL, CONN_UNAME , CONN_PWD );
	     			
	     			logger.debug("after getting db Conn => "+dbConn);
	     			
	     			QueryRunner qRunner = new QueryRunner();
	     			
	     			docRegResponseBean = qRunner.query(dbConn , "select EV_DOC_ID , EV_DOC_NAME , EV_DOC_QUALI , EV_DOC_ADDR1, EV_DOC_ADDR2 , EV_DOC_STATE , EV_DOC_PIN_CODE , EV_DOC_TEL1 , EV_DOC_TEL2 , EV_DOC_EMAIL from EV_DOC where EV_DOC_ID = '"+nameId+"'" , 
	    					new ResultSetHandler<DocRegResponseBean>(){

	    						public DocRegResponseBean handle(ResultSet resultSet) throws SQLException {
	    							
	    							DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
	    							
	    							resultSet.next();
	    							docRegResponseBean.setNameText(resultSet.getString("EV_DOC_NAME"));
	    							docRegResponseBean.setQualiText(resultSet.getString("EV_DOC_QUALI"));
	    							docRegResponseBean.setAddress1Text(resultSet.getString("EV_DOC_ADDR1"));
	    							docRegResponseBean.setAddress2Text(resultSet.getString("EV_DOC_ADDR2"));
	    							docRegResponseBean.setStateId(resultSet.getString("EV_DOC_STATE"));
	    							docRegResponseBean.setPincode(resultSet.getString("EV_DOC_PIN_CODE"));
	    							docRegResponseBean.setTel1Text(resultSet.getString("EV_DOC_TEL1"));
	    							docRegResponseBean.setTel2Text(resultSet.getString("EV_DOC_TEL2"));
	    							docRegResponseBean.setEmail(resultSet.getString("EV_DOC_EMAIL"));
	    							
	    							return docRegResponseBean;
	    						}
	    				});
	     			
	     		}  catch(Exception e) {
	     			
	     			logger.error("Error fetching doc details " ,e);
	     			
	     		} finally {
	     			     				
	     			logger.debug("releasing connection");
	     			DbUtils.closeQuietly(dbConn);
	     		}
	        	 
	        	 return docRegResponseBean;
	         }
	         
	     };
		
	     populateAllFieldsTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					DocRegResponseBean docRegResponseBean = populateAllFieldsTask.getValue();
					
					nameTextField.setText(docRegResponseBean.getNameText());
					qualiTextField.setText(docRegResponseBean.getQualiText());
					address1TextField.setText(docRegResponseBean.getAddress1Text());
					address2TextField.setText(docRegResponseBean.getAddress2Text());
									
					int stateIdIndx = stateIdList.indexOf( docRegResponseBean.getStateId() );
					
					stateChoiceBox.setValue( stateIdIndx == -1 ? "-- Select --" : stateList.get(stateIdIndx)  );
					
					pincodeTextField.setText(docRegResponseBean.getPincode());
			
					tel1TextField.setText(docRegResponseBean.getTel1Text());
					tel2TextField.setText(docRegResponseBean.getTel2Text());
					
					emailTextField.setText(docRegResponseBean.getEmail());
					
					logger.debug("stateIdIndx => "+stateIdIndx);
					
					if (stateIdIndx != -1){
											
						nameTextField.setEditable(true);
						qualiTextField.setEditable(true);
						address1TextField.setEditable(true);
						address2TextField.setEditable(true);
						stateChoiceBox.setDisable(false);
						pincodeTextField.setEditable(true);
						emailTextField.setEditable(true);
						tel1TextField.setEditable(true);
						tel2TextField.setEditable(true);
						
						nameTextField.requestFocus();
						
					} else {
						
						nameTextField.setEditable(false);
						qualiTextField.setEditable(false);
						address1TextField.setEditable(false);
						address2TextField.setEditable(false);
						stateChoiceBox.setDisable(true);
						pincodeTextField.setEditable(false);
						emailTextField.setEditable(false);
						tel1TextField.setEditable(false);
						tel2TextField.setEditable(false);
						
					}
					 
				}
				
			}	 
	     });
	     
		new Thread( populateAllFieldsTask ).start();
		
	}

	@Override
	protected DocRegRequestBean populateDocRegRequestBean() {
		
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
		docReqBean.setNameId(nameId);
		
		return docReqBean;
	}

	@Override
	protected void saveDbTask(final DocRegRequestBean docRegRequestBean) {
		
		final Task<DocRegResponseBean> saveTask = new Task<DocRegResponseBean>() { 
			
	         @Override protected DocRegResponseBean call() throws Exception {     		
	     		Connection dbConn = null;
   					
	     		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
	     		
	     		try {
	     						
	     			dbConn = DriverManager.getConnection( CONN_URL, CONN_UNAME , CONN_PWD );
	     			
	     			logger.debug("after getting db Conn => "+dbConn);
	     			
	     			dbConn.setAutoCommit(false);
	     			
	     			QueryRunner qRunner = new QueryRunner();
	     			     			
	     			qRunner.update(dbConn , 
	     					"update EV_DOC set EV_DOC_NAME = ? , EV_DOC_QUALI = ? , EV_DOC_ADDR1 = ? ,EV_DOC_ADDR2 = ? , EV_DOC_STATE = ? , EV_DOC_PIN_CODE = ? ,EV_DOC_TEL1 = ? ,EV_DOC_TEL2 = ? , EV_DOC_EMAIL = ? , EV_ENTRY_TIME = ( select now() ) where EV_DOC_ID = ? "  , 
	     					new Object[]{docRegRequestBean.getNameText() ,  docRegRequestBean.getQualiText(), docRegRequestBean.getAddress1Text() , docRegRequestBean.getAddress2Text() , docRegRequestBean.getStateId() , docRegRequestBean.getPincode() ,docRegRequestBean.getTel1Text() , docRegRequestBean.getTel2Text() , docRegRequestBean.getEmail() , docRegRequestBean.getNameId() });
	     			
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
						
						int indxToReplace = nameIdList.indexOf( nameId ) ;
								
						nameChoiceBox.getItems().set(indxToReplace , docRegRequestBean.getNameText());
					
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

		nameChoiceBox.getSelectionModel().selectFirst();
		
		nameTextField.setEditable(false);
		qualiTextField.setEditable(false);
		address1TextField.setEditable(false);
		address2TextField.setEditable(false);
		stateChoiceBox.setDisable(true);
		pincodeTextField.setEditable(false);
		emailTextField.setEditable(false);
		tel1TextField.setEditable(false);
		tel2TextField.setEditable(false);
		
	}

	
}
