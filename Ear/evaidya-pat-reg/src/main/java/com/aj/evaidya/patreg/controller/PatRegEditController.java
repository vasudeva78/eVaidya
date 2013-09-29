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
import javafx.scene.control.RadioButton;

import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public class PatRegEditController extends AbstractPatRegController {

	@FXML
	private ChoiceBox<String> patNameChoiceBox;

	private String patNameId;
	private List<String> patNameList;
	private List<String> patNameIdList;

	protected void populateFieldsOnIinit() {
		// super.populateStateField( true );
		
		dateTextField.setPromptText("dd");
		yearTextField.setPromptText("yyyy");
		
		abstractResetFields();
		
//		stateTextField.getSelectionModel().selectedIndexProperty().addListener(
//			new ChangeListener<Number>() {
//
//				@Override
//				public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
//					
//					if (newValue.intValue() == -1){
//						return;
//					}
//					
//					stateCode = stateIdList.get( newValue.intValue() );
//										
//				}
//		});
		
		patNameChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
				new ChangeListener<Number>() {
		
					@Override
					public void changed(ObservableValue<? extends Number> ov,
							Number oldValue, Number newValue) {
						
						if ( newValue.intValue() == -1 ){
							return;
						}
						
						patNameId = patNameIdList.get( newValue.intValue() );
						
						if("--".equalsIgnoreCase( patNameId )){
							
							resetAction();
							nameTextField.requestFocus();
							
							return;
							
						}

						populateAllFields(patNameId);
													
					}
			});
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

		stateTextField.setEditable(false);
		pincodeTextField.setEditable(false);
		
		((RadioButton)radioGroupId.getToggles().get(0)).setDisable(true);
		((RadioButton)radioGroupId.getToggles().get(1)).setDisable(true);

		tel1TextField.setEditable(false);
		tel2TextField.setEditable(false);
		
		fatNameTextField.setEditable(false);
	}


	@Override
	protected PatRegRequestBean populatePatRegRequestBean() {
		
		PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
		
		patRegRequestBean.setNameId(patNameId);
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


	@Override
	protected void saveDbTask(final PatRegRequestBean patRegRequestBean) {
		
		System.out.println("inisde saveDbTask ");
		
		final Task<PatRegResponseBean> saveTask = new Task<PatRegResponseBean>() { 
			
	         @Override protected PatRegResponseBean call() throws Exception {			
	        	 return patRegBo.updatePatDtls(patRegRequestBean);
	         }
		};     
		
		saveTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					PatRegResponseBean patRegResponseBean = saveTask.getValue();
					
					if( "success".equals(patRegResponseBean.getStatus() ) ){
						CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , patRegResponseBean.getMessage() );
					
					}  else {
						CommonControlsBoImpl.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
					}
							
				}
				
			}	 
	     });
	     
		new Thread( saveTask ).start();
		
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
					
		new Thread(new Task <Map<String, String>>() {

			@Override
			protected Map<String, String> call() throws Exception {
				
				PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
				
				patRegRequestBean.setNameText( nameTextField.getText().trim() );
				patRegRequestBean.setDbUrl(dbUrl);
				patRegRequestBean.setDbUsername(dbUsername);
				patRegRequestBean.setDbPwd(dbPwd);
				
				return patRegBo.getPatNames(patRegRequestBean);
				
			}
			
			@Override
			protected void succeeded(){
				
				Map<String, String> patNamesMap = getValue();
				
				// Reset All fields
				resetAction();
				
				switch( patNamesMap.size() ){
					case 1:
						// No pat name found
						CommonControlsBoImpl.showErrorMessage(statusLabel, nameTextField , "No Patient Details Found ..." );
						nameTextField.requestFocus();
						break;
					
					case 2:
						// One pat Name
						List<String> patNameList = new ArrayList<String>( patNamesMap.keySet() );
						patNameId = patNameList.get(1) ;
						
						populateAllFields( patNameId );
						break;
					
					default:
						
						// More pat names. Hence populate in drop down
						
						patNameList = new ArrayList<String> ( patNamesMap.values() );
						patNameIdList = new ArrayList<String> ( patNamesMap.keySet() ); 

						patNameChoiceBox.getItems().clear();
						patNameChoiceBox.getItems().addAll( patNameList );
						
						patNameChoiceBox.setDisable(false);
				}
			}
				
		}).start();
		
	}
	
	private void populateAllFields(final String patNameId){
		
		new Thread(new Task<PatRegResponseBean>() {

			@Override
			protected PatRegResponseBean call() throws Exception {
				
				PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
	        	
	        	patRegRequestBean.setNameId(patNameId);
	    		patRegRequestBean.setDbUrl(dbUrl);
	    		patRegRequestBean.setDbUsername(dbUsername);
	    		patRegRequestBean.setDbPwd(dbPwd);
	        	
	     		return patRegBo.getPatDtls(patRegRequestBean);
			}
			
			@Override
			protected void succeeded(){
				PatRegResponseBean patRegResponseBean = getValue();
				
				nameTextField.setText(patRegResponseBean.getNameText());
				
				dateTextField.setText(patRegResponseBean.getPatDay());
				monthChoiceBox.setValue(patRegResponseBean.getPatMon());
				yearTextField.setText(patRegResponseBean.getPatYear());
				
				address1TextField.setText(patRegResponseBean.getAddress1Text());
				address2TextField.setText(patRegResponseBean.getAddress2Text());
								
				stateTextField.setText(patRegResponseBean.getStateText());
				
				pincodeTextField.setText(patRegResponseBean.getPincode());
				
				if( patRegResponseBean.getSex().equalsIgnoreCase("female") ){
					((RadioButton)radioGroupId.getToggles().get(0)).setSelected(false);
					((RadioButton)radioGroupId.getToggles().get(1)).setSelected(true);
				} else {
					((RadioButton)radioGroupId.getToggles().get(0)).setSelected(true);
					((RadioButton)radioGroupId.getToggles().get(1)).setSelected(false);					
				}
		
				tel1TextField.setText(patRegResponseBean.getTel1Text());
				tel2TextField.setText(patRegResponseBean.getTel2Text());
				
				fatNameTextField.setText(patRegResponseBean.getFatNameText());
				
//				if (stateIdIndx != -1){
										
				nameTextField.setEditable(true);
				dateTextField.setEditable(true);
				monthChoiceBox.setDisable(false);
				yearTextField.setEditable(true);
				address1TextField.setEditable(true);
				address2TextField.setEditable(true);
				stateTextField.setEditable(true);
				pincodeTextField.setEditable(true);
				
				((RadioButton)radioGroupId.getToggles().get(0)).setDisable(false);
				((RadioButton)radioGroupId.getToggles().get(1)).setDisable(false);
				
				tel1TextField.setEditable(true);
				tel2TextField.setEditable(true);
				
				fatNameTextField.setEditable(true);
				
				nameTextField.requestFocus();
					
//				} else {
//					
//					resetAction();
//					
//				}
			}
				
		}).start();

	}
}
