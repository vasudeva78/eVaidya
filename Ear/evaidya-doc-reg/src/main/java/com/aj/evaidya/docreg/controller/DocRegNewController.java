package com.aj.evaidya.docreg.controller;

import javafx.concurrent.Task;

import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;

public class DocRegNewController extends AbstractDocRegController {
	
	protected void populateFieldsOnIinit() {			
		super.populateStateField( false );
		super.consulTextArea.setPromptText("Every Monday & Tuesday.\nBetween 10:30 am to 12 pm.");
	}
	
	protected DocRegRequestBean populateDocRegRequestBean(){
		
		DocRegRequestBean docReqBean = new DocRegRequestBean();
		
		docReqBean.setNameText( nameTextField.getText().trim().substring(0, Math.min(1000, nameTextField.getText().trim().length() )) );
		docReqBean.setQualiText( qualiTextField.getText().trim().substring(0, Math.min(5000, qualiTextField.getText().trim().length() )) );
		docReqBean.setDesigText( desigTextField.getText().trim().substring(0, Math.min(5000, desigTextField.getText().trim().length() )) );
		docReqBean.setAddress1Text( address1TextField.getText().substring(0, Math.min(5000, address1TextField.getText().trim().length())) );
		docReqBean.setAddress2Text( address2TextField.getText().substring(0, Math.min(5000, address2TextField.getText().trim().length() )) );
		docReqBean.setConsultText( consulTextArea.getText().substring(0, Math.min(5000, consulTextArea.getText().trim().length())) );
		docReqBean.setStateId( stateCode );
		docReqBean.setPincode( pincodeTextField.getText().substring(0, Math.min(10, pincodeTextField.getText().trim().length())) );
		docReqBean.setEmail( emailTextField.getText().substring(0, Math.min(1000, emailTextField.getText().trim().length())) );
		docReqBean.setTel1Text( tel1TextField.getText().substring(0, Math.min(100, tel1TextField.getText().trim().length() )) );
		docReqBean.setTel2Text( tel2TextField.getText().substring(0, Math.min(100, tel2TextField.getText().trim().length() )) );
		docReqBean.setHospText( hospTextField.getText().substring(0, Math.min(5000, hospTextField.getText().trim().length() )) );
		
		return docReqBean;
		
	}
	
	protected void saveDbTask(final DocRegRequestBean docReqBean){
		
		new Thread(new Task<DocRegResponseBean>() {

			@Override
			protected DocRegResponseBean call() throws Exception {
				
				DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
	     		
	     		try {
	     						
	     			docRegResponseBean = docRegBo.saveDocDtls(docReqBean);
	     			
	     		}  catch(Exception e) {
	     			
	     			docRegResponseBean.setStatus("error");
	     			docRegResponseBean.setMessage("Not Saved ...");
	     			
	     		}
	     		
	        	return docRegResponseBean;
			}
			
			@Override
			protected void succeeded(){
				DocRegResponseBean docRegResponseBean = getValue();
				
				if( "success".equals(docRegResponseBean.getStatus() ) ){
					CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , docRegResponseBean.getMessage() );
				
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
		
	}
}
