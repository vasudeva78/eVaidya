package com.aj.evaidya.docreg.controller;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.apache.log4j.Logger;

import com.aj.evaidya.common.bo.CommonBo;
import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.common.dao.CommonDao;
import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.bo.DocRegBo;
import com.google.inject.Inject;

public abstract class AbstractDocRegController implements Initializable {
	
	protected CommonBo commonBo;
	
	public CommonBo getCommonBo() {
		return commonBo;
	}

	@Inject
	public void setCommonBo(CommonBo commonBo) {
		this.commonBo = commonBo;
	}

	protected CommonDao commonDao;
	
	public CommonDao getCommonDao() {
		return commonDao;
	}

	@Inject
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	private static final Logger logger = Logger.getLogger( AbstractDocRegController.class );
	
	protected DocRegBo docRegBo;
	
	public DocRegBo getDocRegBo() {
		return docRegBo;
	}

	@Inject
	public void setDocRegBo(DocRegBo docRegBo) {
		this.docRegBo = docRegBo;
	}
		
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
	protected TextField desigTextField;
	
	@FXML
	protected TextField address1TextField;

	@FXML
	protected TextField address2TextField;
	
	@FXML
	protected TextArea consulTextArea;
	
	@FXML
	protected ChoiceBox<String> stateChoiceBox;
	
	@FXML
	protected TextField pincodeTextField;
	
	@FXML
	protected TextField tel1TextField;
	
	@FXML
	protected TextField tel2TextField;
	
	@FXML
	protected TextField emailTextField;
	
	@FXML
	protected TextField hospTextField;
	
	protected String stateCode;

	protected List<String> stateList;
	protected List<String> stateIdList;
	
	protected abstract void populateFieldsOnIinit();
	protected abstract DocRegRequestBean populateDocRegRequestBean();
	protected abstract void saveDbTask( DocRegRequestBean docRegRequestBean );
	protected abstract void abstractResetFields();
	
	protected long startTime;
	
	public final void initialize(URL url, ResourceBundle bundle) {
		
		// populate other control fields
		populateFieldsOnIinit();
		
		System.out.println("inside initialise");
	}
	
	protected final void populateStateField(final boolean isStateFieldDisabled) {
		stateChoiceBox.setDisable(true);
		
		stateList = new ArrayList<String>();
		stateIdList = new ArrayList<String>();
		
		new Thread(new Task<Map<String, String>>() {

			@Override
			protected Map<String, String> call() throws Exception {
				return commonBo.getStateDropDownList(); 
			}
			
			@Override
			protected void succeeded(){
				Map<String, String> stateListMap = getValue();
				
				stateIdList.addAll( stateListMap.keySet() );
				stateList.addAll( stateListMap.values()  );
				
				stateChoiceBox.getItems().addAll( stateList );
				
				stateChoiceBox.setDisable( isStateFieldDisabled );
				
				stateChoiceBox.setValue("-- Select --");
			}
				
		}).start(); 
		
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
		
		startTime = System.nanoTime();
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, nameTextField , "Empty Name ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, nameTextField , "[a-zA-Z ]*" , "Only Letters allowed ...") ) {
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, qualiTextField , "Empty Qualification ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, qualiTextField , "[a-zA-Z -().,]*" , "Only Letters and Symbols - ( ) . , allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, desigTextField , "Empty Designation ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, desigTextField , "[a-zA-Z -().,]*" , "Only Letters and Symbols - ( ) . , allowed ...") ) {
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
			
		if ( !CommonControlsBoImpl.checkTextAreaForEmptyString(statusLabel, consulTextArea , "Empty Consultation Details ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextAreaForInvalidLetters(statusLabel, consulTextArea , "[a-zA-Z0-9 -():.,]*" , "Only Numbers , Letters and Symbols - ( ) : . , allowed ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkSelectionBox(statusLabel, stateChoiceBox , "Select State ...") ){
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, pincodeTextField , "[0-9]*" , "Only Digits allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, emailTextField , EMAIL_PATTERN , "Valid Email Id allowed ...") ) {
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
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, hospTextField , "Empty Hospital ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, hospTextField , "[a-zA-Z -().,]*" , "Only Letters and Symbols , - ( ) . allowed ...") ) {
			return;
		}
		
		
		// All good
		
		DocRegRequestBean docRegRequestBean = populateDocRegRequestBean();
		
		saveDbTask(docRegRequestBean);
	
	}
	
	public final void resetAction(){
		nameTextField.setText("");
		qualiTextField.setText("");
		desigTextField.setText("");
		
		address1TextField.setText("");
		address2TextField.setText("");
		
		consulTextArea.setText("");
		
		stateChoiceBox.getSelectionModel().selectFirst();
		pincodeTextField.setText("");
		emailTextField.setText("");
		
		tel1TextField.setText("");
		tel2TextField.setText("");
		
		hospTextField.setText("");
		
		abstractResetFields();
		
	}

}
