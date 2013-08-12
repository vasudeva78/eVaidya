package com.aj.evaidya.patreg.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public class PatRegEditController extends AbstractPatRegController {

	@FXML
	private ChoiceBox<String> patNameChoiceBox;

//	private String patNameId;
//	private List<String> patNameList;
//	private List<String> patNameIdList;

	protected void populateFieldsOnIinit() {
		super.populateStateField( true );
		
		abstractResetFields();
	}

	@Override
	protected void abstractResetFields() {
		nameTextField.setEditable(true);
		patNameChoiceBox.getSelectionModel().selectFirst();
		patNameChoiceBox.setDisable(true);

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


	@Override
	protected PatRegRequestBean populatePatRegRequestBean() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void saveDbTask(PatRegRequestBean patRegRequestBean) {
		// TODO Auto-generated method stub
		
	}
	
	public final void searchAction(){
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, nameTextField , "Empty Name ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, nameTextField , "[a-zA-Z ]*" , "Only Letters allowed ...") ) {
			return;
		}
		
		if (nameTextField.getText().trim().length() < 3){
			CommonControlsBoImpl.showErrorMessage(statusLabel, nameTextField , "At least 3 characters ..." );
			return;
		}
				
		// Reset All fields
		abstractResetFields();
		
		final Task <Map<String, String>> getPatNamesListTask = new Task <Map<String, String>>() {

			@Override
			protected Map<String, String> call() throws Exception {
				PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
				
				patRegRequestBean.setNameText( nameTextField.getText().trim() );
				patRegRequestBean.setDbUrl(dbUrl);
				patRegRequestBean.setDbUsername(dbUsername);
				patRegRequestBean.setDbPwd(dbPwd);
				
				return patRegBo.getPatNames(patRegDao, patRegRequestBean);
			} 
			
		};
		
		getPatNamesListTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					Map<String, String> patNamesMap = getPatNamesListTask.getValue();
					
					if (patNamesMap.size() == 2) {
						List<String> patNameList = new ArrayList<String>( patNamesMap.keySet() );
						populateAllFields( patNameList.get(1) );
					}
					else { // More pat names
						
						
					}
				}
			}	
		});

		new Thread( getPatNamesListTask ).start();
	}
	
	private void populateAllFields(final String patNameId){

		final Task <PatRegResponseBean> populateAllFieldsTask = new Task <PatRegResponseBean>() { 
			
	         @Override protected PatRegResponseBean call() throws Exception {
	        	 
	        	PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
	        	
	        	patRegRequestBean.setNameId(patNameId);
	    		patRegRequestBean.setDbUrl(dbUrl);
	    		patRegRequestBean.setDbUsername(dbUsername);
	    		patRegRequestBean.setDbPwd(dbPwd);
	        	
	     		return patRegBo.getPatDtls(patRegDao, patRegRequestBean);
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
}
