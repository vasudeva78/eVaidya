package com.aj.evaidya.patreg.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.apache.log4j.Logger;

import com.aj.evaidya.common.bo.CommonBo;
import com.aj.evaidya.common.bo.CommonControlsBo;
import com.aj.evaidya.common.dao.CommonDao;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;


public abstract class AbstractPatRegController implements Initializable {
	
	protected CommonBo commonBo;
	
	private static final Logger logger = Logger.getLogger( AbstractPatRegController.class );
	
	protected static final String CONN_URL = "jdbc:h2:file:D:/AJ/projects/eVaidya/data/evaidya";
	protected static final String CONN_UNAME = "ASDs!@DSFDS55";
	protected static final String CONN_PWD = "SDXytytWQE!31 GHGFHytyy!#@^d";
	
	@FXML
	protected Label statusLabel;
	
	@FXML
	protected TextField nameTextField;
	
	@FXML
	protected TextField dateTextField;

	@FXML
	protected ChoiceBox monthChoiceBox;

	@FXML
	protected TextField yearTextField;
	
	@FXML
	protected TextField address1TextField;

	@FXML
	protected TextField address2TextField;
	
	@FXML
	protected ChoiceBox stateChoiceBox;
	
	@FXML
	protected TextField pincodeTextField;
	
	@FXML
	protected TextField tel1TextField;
	
	@FXML
	protected TextField tel2TextField;
	
	protected String stateCode;

	protected List<String> stateList;
	protected List<String> stateIdList;
	
	protected abstract void populateFieldsOnIinit();
	protected abstract PatRegRequestBean populatePatRegRequestBean();
	protected abstract void saveDbTask( PatRegRequestBean patRegRequestBean );
	protected abstract void abstractResetFields();
	
	public final void initialize(URL url, ResourceBundle bundle) {
		
		CommonDao commonDao =  new CommonDao();
		commonBo = new CommonBo(commonDao);
		
		// populate other control fields
		populateFieldsOnIinit();
		
	}
	
	protected final void populateStateField() {
		stateChoiceBox.setDisable(true);
		
		stateList = new ArrayList<String>();
		stateIdList = new ArrayList<String>();
		
		commonBo.getStateDropDownList(CONN_URL , CONN_UNAME , CONN_PWD , stateChoiceBox, stateList , stateIdList);
		
		stateChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
			new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> ov,
						Number oldValue, Number newValue) {
					
					logger.debug("inside chnaged");
					
					if (newValue.intValue() == -1){
						return;
					}
					
					stateCode = stateIdList.get( newValue.intValue() );
										
				}
			});
	}
	
	public final void saveAction(){
		
		if ( !CommonControlsBo.checkTextFieldForEmptyString(statusLabel, nameTextField , "Empty Name ...") ) {
			return;
		};
		
		if ( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, nameTextField , "[a-zA-Z ]*" , "Only Letters allowed ...") ) {
			return;
		}

		if ( !CommonControlsBo.checkTextFieldForEmptyString(statusLabel, dateTextField , "Empty Date ...") ) {
			return;
		};
		
		if ( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, dateTextField , "[0-9]*" , "Only Digits allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, dateTextField , "\\d{2}" , "Only 2 Digits allowed ...") ) {
			return;
		}
		
		if( !CommonControlsBo.checkSelectionBox(statusLabel, monthChoiceBox , "Select Month ...") ){
			return;
		}

		if ( !CommonControlsBo.checkTextFieldForEmptyString(statusLabel, yearTextField , "Empty Year ...") ) {
			return;
		}
		
		if( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, yearTextField , "[0-9]*" , "Only Digits allowed ...") ){
			return;
		}
		
		if( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, yearTextField , "\\d{4}" , "Only 4 Digits allowed ...") ){
			return;
		}

		if( !CommonControlsBo.checkForValidDate(statusLabel, dateTextField , monthChoiceBox ,  yearTextField ) ){
			return;
		}
		
		if ( !CommonControlsBo.checkTextFieldForEmptyString(statusLabel, address1TextField , "Empty Address ...") ) {
			return;
		}
		
		if( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, address1TextField , "[a-zA-Z0-9 ,-/#]*" , "Only Letters and Symbols , - / # allowed ...") ){
			return;
		}
		
		if( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, address2TextField , "[a-zA-Z0-9 ,-/#]*" , "Only Letters and Symbols , - / # allowed ...") ){
			return;
		}
				
		if( !CommonControlsBo.checkSelectionBox(statusLabel, stateChoiceBox , "Select State ...") ){
			return;
		}

		if ( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, pincodeTextField , "[0-9]*" , "Only Digits allowed ...") ) {
			return;
		}
		
		if( !CommonControlsBo.checkTextFieldForEmptyString(statusLabel, tel1TextField , "Empty Tel ...") ) {
			return;
		}
		
		if ( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, tel1TextField , "[0-9 -]*" , "Only Digits and Symbol - allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, tel2TextField , "[0-9 -]*" , "Only Digits and Symbol - allowed ...") ) {
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
		
		abstractResetFields();
		
	}

}
