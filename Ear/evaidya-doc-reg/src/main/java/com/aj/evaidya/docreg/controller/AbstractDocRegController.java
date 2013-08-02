package com.aj.evaidya.docreg.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.apache.log4j.Logger;

import com.aj.evaidya.common.bo.impl.CommonBoImpl;
import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.common.dao.impl.CommonDaoImpl;
import com.aj.evaidya.docreg.beans.DocRegRequestBean;
import com.aj.evaidya.docreg.bo.DocRegBo;
import com.aj.evaidya.docreg.dao.DocRegDao;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public abstract class AbstractDocRegController implements Initializable {
	
	protected CommonBoImpl commonBoImpl;
	
	private static final Logger logger = Logger.getLogger( AbstractDocRegController.class );
	
	protected DocRegBo docRegBo;
	
	public DocRegBo getDocRegBo() {
		return docRegBo;
	}

	@Inject
	public void setDocRegBo(DocRegBo docRegBo) {
		this.docRegBo = docRegBo;
	}
	
	protected DocRegDao docRegDao;
	
	public DocRegDao getDocRegDao() {
		return docRegDao;
	}

	@Inject
	public void setDocRegDao(DocRegDao docRegDao) {
		this.docRegDao = docRegDao;
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
	protected ChoiceBox<String> stateChoiceBox;
	
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
		
		CommonDaoImpl commonDaoImpl =  new CommonDaoImpl();
		commonBoImpl = new CommonBoImpl(commonDaoImpl);
		
		// populate other control fields
		populateFieldsOnIinit();
		
	}
	
	protected final void populateStateField() {
		stateChoiceBox.setDisable(true);
		
		stateList = new ArrayList<String>();
		stateIdList = new ArrayList<String>();
		
		// commonBoImpl.getStateDropDownList(dbUrl , dbUsername , dbPwd , stateChoiceBox, stateList , stateIdList);
		
		final Task<Map<String, String>> choiceListTask = new Task<Map<String, String>>() {
	         @Override protected Map<String, String> call() throws Exception {
	        	 
	        	 return commonBoImpl.getStateDropDownList( dbUrl , dbUsername , dbPwd ); 
	        	 
	         }
	         
	     };
	     
	     choiceListTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				if (newState == State.SUCCEEDED) {
					
					Map<String, String> stateListMap = choiceListTask.getValue();
					
					stateIdList.addAll( stateListMap.keySet() );
					stateList.addAll( stateListMap.values()  );
					
					stateChoiceBox.getItems().addAll( stateList );
					
					stateChoiceBox.setDisable(false);
					
					stateChoiceBox.setValue("-- Select --");
					
				}
				
			}	 
	     });
	     
	     new Thread(choiceListTask).start();
	     
		
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
		
		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, nameTextField , "Empty Name ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, nameTextField , "[a-zA-Z ]*" , "Only Letters allowed ...") ) {
			return;
		}

		if ( !CommonControlsBoImpl.checkTextFieldForEmptyString(statusLabel, qualiTextField , "Empty Qualification ...") ) {
			return;
		};
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, qualiTextField , "[a-zA-Z -().,]*" , "Only Letters and Symbols , - ( ) . allowed ...") ) {
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
		
		if ( !CommonControlsBoImpl.checkTextFieldForInvalidLetters(statusLabel, emailTextField , EMAIL_PATTERN , "Valid Email Id allowed ...") ) {
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
