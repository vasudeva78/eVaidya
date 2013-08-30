package com.aj.evaidya.patreg.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import com.aj.evaidya.patreg.beans.PatRegRequestBean;

public class PatRegUploadController extends AbstractPatRegController {
	
	@FXML
	private VBox selectBVox;
	
	protected void populateFieldsOnIinit() {			
		FileChooser fileChooser = new FileChooser();
		
//		/ selectBVox.getChildren().add(fileChooser);
		
	}
	
	protected PatRegRequestBean populatePatRegRequestBean(){
		
		PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
		
		return patRegRequestBean;
		
	}
	
	@Override
	protected void saveDbTask(final PatRegRequestBean patRegRequestBean){
		
	 
	}

	@Override
	protected void abstractResetFields() {
		
	}
}
