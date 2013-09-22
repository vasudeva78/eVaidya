package com.aj.evaidya.patreg.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import com.aj.evaidya.common.bo.CommonBo;
import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.bo.PatRegBo;
import com.google.inject.Inject;
import com.google.inject.name.Named;


public abstract class AbstractPatRegController implements Initializable {
	
	protected CommonBo commonBo;

	public CommonBo getCommonBo() {
		return commonBo;
	}

	@Inject
	public void setCommonBo(CommonBo commonBo) {
		this.commonBo = commonBo;
	}

	protected PatRegBo patRegBo;
	
	public PatRegBo getPatRegBo() {
		return patRegBo;
	}

	@Inject
	public void setPatRegBo(PatRegBo patRegBo) {
		this.patRegBo = patRegBo;
	}

	protected String dbUrl;
	protected String dbUsername;
	protected String dbPwd;
	
	public String getDbUrl() {
		return dbUrl;
	}

	@Inject
	public void setDbUrl(@Named("dbUrl") String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	@Inject
	public void setDbUsername(@Named("dbUsername") String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPwd() {
		return dbPwd;
	}

	@Inject
	public void setDbPwd(@Named("dbPwd") String dbPwd) {
		this.dbPwd = dbPwd;
	}
	
	@FXML
	protected Label statusLabel;
	
	@FXML
	protected TextField nameTextField;
	
	@FXML
	protected TextField dateTextField;

	@FXML
	protected ChoiceBox<String> monthChoiceBox;

	@FXML
	protected TextField yearTextField;
	
	@FXML
	protected TextField address1TextField;

	@FXML
	protected TextField address2TextField;
	
	@FXML
	protected ChoiceBox<String> stateChoiceBox;
	
	@FXML
	protected TextField pincodeTextField;
	
	@FXML
	protected ToggleGroup radioGroupId;
		
	@FXML
	protected TextField tel1TextField;
	
	@FXML
	protected TextField tel2TextField;
	
	@FXML
	protected TextField fatNameTextField;
	
	protected String stateCode;

	protected List<String> stateList;
	protected List<String> stateIdList;
	
	protected abstract void populateFieldsOnIinit();
	protected abstract PatRegRequestBean populatePatRegRequestBean();
	protected abstract void saveDbTask( PatRegRequestBean patRegRequestBean );
	protected abstract void abstractResetFields();
	
	public final void initialize(URL url, ResourceBundle bundle) {
		// populate other control fields
		populateFieldsOnIinit();
		
	}
	
	protected final void populateStateField(final boolean isStateChoiceBoxDisabled) {
		
		stateChoiceBox.setDisable(true);
		
		stateList = new ArrayList<String>();
		stateIdList = new ArrayList<String>();
		
		new Thread(new Task<Map<String, String>>() {

			@Override
			protected Map<String, String> call() throws Exception {
				return commonBo.getStateDropDownList(dbUrl , dbUsername , dbPwd ); 
			}
			
			@Override
			protected void succeeded(){
				
				Map<String, String> stateListMap = getValue();
				
				stateIdList.addAll( stateListMap.keySet() );
				stateList.addAll( stateListMap.values()  );
				
				stateChoiceBox.getItems().addAll( stateList );
				
				stateChoiceBox.setDisable( isStateChoiceBoxDisabled );
				
				stateChoiceBox.setValue("-- Select --");
				
			}
				
		}).start();   
		
		stateChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
			new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> ov,
						Number oldValue, Number newValue) {
					
					if (newValue.intValue() == -1){
						return;
					}
					
					stateCode = stateIdList.get( newValue.intValue() );
										
				}
			});
	}
	
	public final void saveAction(){
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, nameTextField , "Empty Name ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, nameTextField , "[a-zA-Z ]*" , "Only Letters allowed ...") ) {
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, dateTextField , "Empty Date ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, dateTextField , "[0-9]*" , "Only Digits allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, dateTextField , "\\d{2}" , "Only 2 Digits allowed ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkSelectionBox(statusLabel, monthChoiceBox , "Select Month ...") ){
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, yearTextField , "Empty Year ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, yearTextField , "[0-9]*" , "Only Digits allowed ...") ){
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, yearTextField , "\\d{4}" , "Only 4 Digits allowed ...") ){
			return;
		}

		if( !CommonControlsBoImpl.checkForValidDate(statusLabel, dateTextField , monthChoiceBox ,  yearTextField ) ){
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, address1TextField , "Empty Address ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, address1TextField , "[a-zA-Z0-9 ,-/#]*" , "Only Letters and Symbols , - / # allowed ...") ){
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, address2TextField , "[a-zA-Z0-9 ,-/#]*" , "Only Letters and Symbols , - / # allowed ...") ){
			return;
		}
				
		if( !CommonControlsBoImpl.checkSelectionBox(statusLabel, stateChoiceBox , "Select State ...") ){
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, pincodeTextField , "[0-9]*" , "Only Digits allowed ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, tel1TextField , "Empty Tel ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, tel1TextField , "[0-9 -]*" , "Only Digits and Symbol - allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, tel2TextField , "[0-9 -]*" , "Only Digits and Symbol - allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, fatNameTextField , "Empty Father Name ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, fatNameTextField , "[a-zA-Z ]*" , "Only Letters allowed ...") ) {
			return;
		}
		
		// All good
		
		PatRegRequestBean patRegRequestBean = populatePatRegRequestBean();
		
		saveDbTask(patRegRequestBean);
	
	}
	
	public final void resetAction(){
		nameTextField.setText("");
		dateTextField.setText("");
		monthChoiceBox.getSelectionModel().selectFirst();
		yearTextField.setText("");
		
		address1TextField.setText("");
		address2TextField.setText("");
		
		stateChoiceBox.getSelectionModel().selectFirst();
		pincodeTextField.setText("");
		
		tel1TextField.setText("");
		tel2TextField.setText("");
		
		fatNameTextField.setText("");
		
		abstractResetFields();
		
	}

}
