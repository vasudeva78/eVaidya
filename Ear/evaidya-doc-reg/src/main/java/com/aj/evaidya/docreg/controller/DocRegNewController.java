package com.aj.evaidya.docreg.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;

import org.apache.log4j.Logger;

import com.aj.evaidya.common.bo.CommonControlsBo;
import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.beans.DocRegResponseBean;
import com.aj.evaidya.docreg.bo.DocRegBo;
import com.aj.evaidya.docreg.dao.DocRegDao;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DocRegNewController extends AbstractDocRegController {
	
	private static final Logger logger = Logger.getLogger( DocRegNewController.class );
	
	private DocRegBo docRegBo;
	
	public DocRegBo getDocRegBo() {
		return docRegBo;
	}

	@Inject
	public void setDocRegBo(DocRegBo docRegBo) {
		this.docRegBo = docRegBo;
	}
	
	private DocRegDao docRegDao;
	
	public DocRegDao getDocRegDao() {
		return docRegDao;
	}

	@Inject
	public void setDocRegDao(DocRegDao docRegDao) {
		this.docRegDao = docRegDao;
	}
	
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
		
		docReqBean.setDbUrl(dbUrl);
		docReqBean.setDbUsername(dbUsername);
		docReqBean.setDbPwd(dbPwd);
		
		return docReqBean;
		
	}
	
	protected void saveDbTask(final DocRegRequestBean docReqBean){
		
		final Task<DocRegResponseBean> saveTask = new Task<DocRegResponseBean>() { 
			
	         @Override protected DocRegResponseBean call() throws Exception {     		
  					
	     		DocRegResponseBean docRegResponseBean = new DocRegResponseBean();
	     		
	     		try {
	     						
	     			docRegResponseBean = docRegBo.saveDbTask(docRegDao , docReqBean);
	     			
	     		}  catch(Exception e) {
	     			
	     			docRegResponseBean.setStatus("error");
	     			docRegResponseBean.setMessage("Not Saved ...");
	     			
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
