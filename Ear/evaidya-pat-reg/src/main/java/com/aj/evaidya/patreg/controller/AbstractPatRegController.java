package com.aj.evaidya.patreg.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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


public abstract class AbstractPatRegController implements Initializable {
	
	protected static final Pattern namePattern = Pattern.compile("[a-zA-Z ]*");
	private static final Pattern ddYYYYPattern = Pattern.compile("[0-9]*");
	private static final Pattern ddDigitPattern = Pattern.compile("\\d{2}"); 
	private static final Pattern yyyyDigitPattern = Pattern.compile("\\d{4}");
	private static final Pattern addrPattern = Pattern.compile("[a-zA-Z0-9 ,-/#]*");
	private static final Pattern statePattern = Pattern.compile("[a-zA-Z ]*");
	private static final Pattern pincodePattern = Pattern.compile("[0-9]*");
	private static final Pattern telPattern = Pattern.compile("[0-9 -]*");
	
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
	protected TextField stateTextField;
	
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
	
//	protected String stateCode;

//	protected List<String> stateList;
//	protected List<String> stateIdList;
	
	protected abstract void populateFieldsOnIinit();
	protected abstract PatRegRequestBean populatePatRegRequestBean();
	protected abstract void saveDbTask( PatRegRequestBean patRegRequestBean );
	protected abstract void abstractResetFields();
	
	public final void initialize(URL url, ResourceBundle bundle) {
		// populate other control fields
		populateFieldsOnIinit();
		
	}
	
//	protected final void populateStateField(final boolean isStateChoiceBoxDisabled) {
//		
//		stateChoiceBox.setDisable(true);
//		
//		stateList = new ArrayList<String>();
//		stateIdList = new ArrayList<String>();
//		
//		new Thread(new Task<Map<String, String>>() {
//
//			@Override
//			protected Map<String, String> call() throws Exception {
//				return commonBo.getStateDropDownList(dbUrl , dbUsername , dbPwd ); 
//			}
//			
//			@Override
//			protected void succeeded(){
//				
//				Map<String, String> stateListMap = getValue();
//				
//				stateIdList.addAll( stateListMap.keySet() );
//				stateList.addAll( stateListMap.values()  );
//				
//				stateChoiceBox.getItems().addAll( stateList );
//				
//				stateChoiceBox.setDisable( isStateChoiceBoxDisabled );
//				
//				stateChoiceBox.setValue("-- Select --");
//				
//			}
//				
//		}).start();   
//		
//		stateChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
//			new ChangeListener<Number>() {
//
//				@Override
//				public void changed(ObservableValue<? extends Number> ov,
//						Number oldValue, Number newValue) {
//					
//					if (newValue.intValue() == -1){
//						return;
//					}
//					
//					stateCode = stateIdList.get( newValue.intValue() );
//										
//				}
//			});
//	}
	
	public final void saveAction(){
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, nameTextField , "Empty Name ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(namePattern, statusLabel, nameTextField , "Only Letters allowed ...") ) {
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, dateTextField , "Empty Date ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(ddYYYYPattern, statusLabel, dateTextField , "Only Digits allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(ddDigitPattern, statusLabel, dateTextField , "Only 2 Digits allowed ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkSelectionBox(statusLabel, monthChoiceBox , "Select Month ...") ){
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, yearTextField , "Empty Year ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(ddYYYYPattern, statusLabel, yearTextField , "Only Digits allowed ...") ){
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(yyyyDigitPattern, statusLabel, yearTextField , "Only 4 Digits allowed ...") ){
			return;
		}

		if( !CommonControlsBoImpl.checkForValidDate(statusLabel, dateTextField , monthChoiceBox ,  yearTextField ) ){
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, address1TextField , "Empty Address ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(addrPattern, statusLabel, address1TextField , "Only Letters and Symbols , - / # allowed ...") ){
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(addrPattern, statusLabel, address2TextField , "Only Letters and Symbols , - / # allowed ...") ){
			return;
		}
			
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, stateTextField , "Empty State ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statePattern, statusLabel, stateTextField , "Only Letters allowed ...") ) {
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(pincodePattern, statusLabel, pincodeTextField , "Only Digits allowed ...") ) {
			return;
		}
		
		if( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, tel1TextField , "Empty Tel ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(telPattern, statusLabel, tel1TextField , "Only Digits and Symbol - allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(telPattern, statusLabel, tel2TextField , "Only Digits and Symbol - allowed ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, fatNameTextField , "Empty Father Name ...") ) {
			return;
		}
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(namePattern, statusLabel, fatNameTextField , "Only Letters allowed ...") ) {
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
		
		stateTextField.setText("");
		pincodeTextField.setText("");
		
		tel1TextField.setText("");
		tel2TextField.setText("");
		
		fatNameTextField.setText("");
		
		abstractResetFields();
		
	}

}
