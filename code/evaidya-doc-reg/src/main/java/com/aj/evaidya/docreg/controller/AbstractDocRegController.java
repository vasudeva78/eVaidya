package com.aj.evaidya.docreg.controller;

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
import com.aj.evaidya.docreg.beans.DocRegRequestBean;

public abstract class AbstractDocRegController implements Initializable {
	
	protected CommonBo commonBo;
	
	private static final Logger logger = Logger.getLogger( AbstractDocRegController.class );
	
	protected static final String CONN_URL = "jdbc:h2:file:D:/AJ/projects/eVaidya/data/evaidya";
	protected static final String CONN_UNAME = "ASDs!@DSFDS55";
	protected static final String CONN_PWD = "SDXytytWQE!31 GHGFHytyy!#@^d";
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@FXML
	protected Label statusLabel;
	
	@FXML
	protected TextField nameTextField;
	
	@FXML
	protected TextField qualiTextField;
	
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
	
	@FXML
	protected TextField emailTextField;
	
	protected String stateCode;

	protected List<String> stateList;
	protected List<String> stateIdList;
	
	protected abstract void populateFieldsOnIinit();
	protected abstract DocRegRequestBean populateDocRegRequestBean();
	protected abstract void saveDbTask( DocRegRequestBean docRegRequestBean );
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

		if ( !CommonControlsBo.checkTextFieldForEmptyString(statusLabel, qualiTextField , "Empty Qualification ...") ) {
			return;
		};
		
		if ( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, qualiTextField , "[a-zA-Z -().]*" , "Only Letters and Symbols , - ( ) . allowed ...") ) {
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
		
		if ( !CommonControlsBo.checkTextFieldForInvalidLetters(statusLabel, emailTextField , EMAIL_PATTERN , "Valid Email Id allowed ...") ) {
			return;
		}
		
		// All good
		
		DocRegRequestBean docRegRequestBean = populateDocRegRequestBean();
		
		saveDbTask(docRegRequestBean);
	
	}
	
	public final void resetAction(){
		nameTextField.setText("");
		qualiTextField.setText("");
		
		address1TextField.setText("");
		address2TextField.setText("");
		
		stateChoiceBox.getSelectionModel().selectFirst();
		pincodeTextField.setText("");
		emailTextField.setText("");
		
		tel1TextField.setText("");
		tel2TextField.setText("");
		
		abstractResetFields();
		
	}

}
