package com.aj.evaidya.patreg.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public class PatRegNewController extends AbstractPatRegController {
	
	private @FXML Label myHeader;
	private @FXML Label labelTextField;
	
	protected void populateFieldsOnIinit() {			
		// super.populateStateField(false);
		
		dateTextField.setPromptText("dd");
		yearTextField.setPromptText("yyyy");

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
		patRegRequestBean.setStateText( stateTextField.getText().trim().substring(0, Math.min(100, stateTextField.getText().trim().length() )) );
		patRegRequestBean.setPincode( pincodeTextField.getText().substring(0, Math.min(10, pincodeTextField.getText().trim().length())) );
		patRegRequestBean.setSex( ((RadioButton)radioGroupId.getSelectedToggle()).getText() ); 
		patRegRequestBean.setTel1Text( tel1TextField.getText().substring(0, Math.min(100, tel1TextField.getText().trim().length() )) );
		patRegRequestBean.setTel2Text( tel2TextField.getText().substring(0, Math.min(100, tel2TextField.getText().trim().length() )) );
		patRegRequestBean.setFatNameText( fatNameTextField.getText().substring(0, Math.min(100, fatNameTextField.getText().trim().length() )) );
		
		patRegRequestBean.setDbUrl(dbUrl);
		patRegRequestBean.setDbUsername(dbUsername);
		patRegRequestBean.setDbPwd(dbPwd);
		
		return patRegRequestBean;
		
	}
	
	protected void saveDbTask(final PatRegRequestBean patRegRequestBean){
		
		new Thread(new Task<PatRegResponseBean>() {

			@Override
			protected PatRegResponseBean call() throws Exception {
				return patRegBo.savePatDtls(patRegRequestBean);
			}
			
			@Override
			protected void succeeded(){
				PatRegResponseBean patRegResponseBean = getValue();
				
				if( "success".equals(patRegResponseBean.getStatus() ) ){
					CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , patRegResponseBean.getMessage() );
				
				} else if( "errorNameExists".equals(patRegResponseBean.getStatus() ) ){
					//CommonControlsBo.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
					CommonControlsBoImpl.showErrorMessage(statusLabel, nameTextField, patRegResponseBean.getMessage());
				
				} else {
					CommonControlsBoImpl.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
				}
			}
				
		}).start();
		
	}

	@Override
	protected void abstractResetFields() {
	
	}
}
