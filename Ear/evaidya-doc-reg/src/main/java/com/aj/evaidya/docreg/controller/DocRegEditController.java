package com.aj.evaidya.docreg.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;

public class DocRegEditController extends AbstractDocRegController {

	// private static final Logger logger = Logger.getLogger( DocRegEditController.class );
	
	@FXML
	private ChoiceBox<String> nameChoiceBox;
	
	private String nameId;
	private List<String> nameList;
	private List<String> nameIdList;
	
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
					
					populateAllFields(nameId);
												
				}
		});
		
		// populate name Choice box. Not called until name state box has completed loading
		final Thread namesChoiceThread =  new Thread(new Task<Map<String, String>>() {

			@Override protected Map<String, String> call() throws Exception {
	        	 
	        	 DocRegRequestBean docReqBean = new DocRegRequestBean();
	        	 docReqBean.setDbUrl(dbUrl);
	     		 docReqBean.setDbUsername(dbUsername);
	     		 docReqBean.setDbPwd(dbPwd);
	     			        	 
	        	 return docRegBo.getDocNames(docReqBean);	        	 
	         }
			
			@Override
			protected void succeeded(){
				Map<String, String> nameListMap = (Map<String, String>) getValue();
				
				nameList = new ArrayList<String> ( nameListMap.values() );
				nameIdList = new ArrayList<String> ( nameListMap.keySet() ); 

				nameChoiceBox.getItems().addAll( nameList );
				
				nameChoiceBox.setDisable(false);
			}
				
		});
		
		// populate name Choice box. Not called until name state box has completed loading		 
		// populate State box
		 
		 new Thread(new Task<Map<String, String>>() {

			@Override protected Map<String, String> call() throws Exception {
	        	 
	        	 return commonBo.getStateDropDownList(dbUrl , dbUsername , dbPwd ); 
	        	 
	         }
			
			@Override
			protected void succeeded(){
				Map<String, String> stateListMap = (Map<String, String>) getValue();
				
				stateIdList = new ArrayList<String>( stateListMap.keySet() );
				stateList = new ArrayList<String>( stateListMap.values()  );
				
				stateChoiceBox.getItems().addAll( stateList );
				
				stateChoiceBox.setValue("-- Select --");
				
				// start populating Name choice box
				
				namesChoiceThread.start();
			}
				
		}).start();
		 
		
	}
   	
	private void populateAllFields(final String nameId){
		
		new Thread(new Task <DocRegResponseBean>() {

			@Override
			protected DocRegResponseBean call() throws Exception {
				
				DocRegRequestBean docReqBean = new DocRegRequestBean();
        	    
        	    docReqBean.setDbUrl(dbUrl);
	     		docReqBean.setDbUsername(dbUsername);
	     		docReqBean.setDbPwd(dbPwd);
	     		docReqBean.setNameId(nameId);
	     		
	     		return docRegBo.getDocDtls(docReqBean);
			}
			
			@Override
			protected void succeeded(){
				DocRegResponseBean docRegResponseBean = getValue();
				
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
				
		}).start();
		
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
		
		new Thread(new Task<DocRegResponseBean>() {

			@Override
			protected DocRegResponseBean call() throws Exception {
				 docRegRequestBean.setDbUrl(dbUrl);
	        	 docRegRequestBean.setDbUsername(dbUsername);
	        	 docRegRequestBean.setDbPwd(dbPwd);
	        	 
	     		return docRegBo.updateDocDtls(docRegRequestBean);
			}
			
			@Override
			protected void succeeded(){
				DocRegResponseBean docRegResponseBean = getValue();
				
				if( "success".equals(docRegResponseBean.getStatus() ) ){
					CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , docRegResponseBean.getMessage() );
					
					int indxToReplace = nameIdList.indexOf( nameId ) ;
							
					nameChoiceBox.getItems().set(indxToReplace , docRegRequestBean.getNameText());
				
				} else if( "errorNameExists".equals(docRegResponseBean.getStatus() ) ){
					//CommonControlsBo.showFinalFailureStatus( statusLabel , docRegResponseBean.getMessage() );
					CommonControlsBoImpl.showErrorMessage(statusLabel, nameTextField, docRegResponseBean.getMessage());
				
				} else {
					CommonControlsBoImpl.showFinalFailureStatus( statusLabel , docRegResponseBean.getMessage() );
				}
			}
				
		}).start();
		
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