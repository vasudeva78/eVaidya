package com.aj.evaidya.patreg.controller;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public class PatRegUploadController extends AbstractPatRegController {
	
	@FXML
	protected Label fileLocLabel;
	
	@FXML
	protected Button browseButton;
	
	@FXML
	protected ProgressBar uploadProgressBar;
	
	private FileChooser fChooser;
	
	protected void populateFieldsOnIinit() {			
		fChooser = new FileChooser();
		configureFileChooser(fChooser);
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
	
	public final void browseAction(){
		
		File xlFile = fChooser.showOpenDialog( browseButton.getScene().getWindow() );
		
		if(xlFile.isFile()){
			fileLocLabel.setText( xlFile.getAbsolutePath() );
		}
		
	}
	
	public final void uploadAction(){
		
		if ( !CommonControlsBoImpl.checkLabelForEmptyString(statusLabel, fileLocLabel , "Empty File ...") ) {
			return;
		};
		
		final PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
		patRegRequestBean.setXlFilePath(fileLocLabel.getText());
		
		patRegRequestBean.setDbUrl(dbUrl);
		patRegRequestBean.setDbUsername(dbUsername);
		patRegRequestBean.setDbPwd(dbPwd);
		
		
		final Task<PatRegResponseBean> uploadTask = new Task<PatRegResponseBean>() { 
			
	         @Override protected PatRegResponseBean call() throws Exception {
	        	 
	        	 PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
	        	 
	        	 int excelRowNum = Integer.parseInt( patRegRequestBean.getExcelRowNum() );
	        	 
	        	 for(int i = 1;i<=excelRowNum;i++){
	        		 patRegBo.uploadPatDtls(patRegDao, patRegRequestBean,i,excelRowNum);
	        	 }
	     		
	        	 // return patRegBo.uploadPatDtls(patRegDao, patRegRequestBean);
	        	 
	        	 return patRegResponseBean;
	         }
	     };
	     
	    uploadTask.stateProperty().addListener(new ChangeListener<State>(){

			@Override
			public void changed(ObservableValue<? extends State> ov, State t, State newState) {
				
				if (newState == State.SUCCEEDED) {
					
					PatRegResponseBean patRegResponseBean = uploadTask.getValue();
					
					if( "success".equals(patRegResponseBean.getStatus() ) ){
						CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , patRegResponseBean.getMessage() );
					
					} else {
						CommonControlsBoImpl.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
					}
							
				} 
//				else if (newState == State.RUNNING){
//					System.out.println("work done => "+uploadTask.getWorkDone() + " total work => "+uploadTask.getTotalWork());
//					
//					uploadProgressBar.setProgress(uploadTask.getWorkDone() / uploadTask.getTotalWork() );
//					
//					try {
//						Thread.sleep(2);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			}	 
	     });
	     
	    final Task<PatRegResponseBean> getExcelRowNumTask = new Task<PatRegResponseBean>() { 
			
	         @Override protected PatRegResponseBean call() throws Exception {     		
	     		return patRegBo.getExcelRowsOnUpload(patRegDao, patRegRequestBean);
	         }
	     };
	    		 
	     getExcelRowNumTask.stateProperty().addListener(new ChangeListener<State>(){

				@Override
				public void changed(ObservableValue<? extends State> ov, State t, State newState) {
					if (newState == State.SUCCEEDED) {
						
						PatRegResponseBean patRegResponseBean = getExcelRowNumTask.getValue();
						
						if( "success".equals(patRegResponseBean.getStatus() ) ){
							String excelRowNum = patRegResponseBean.getExcelRowNum();
							
							if ( Integer.parseInt(excelRowNum) != 0 ){
								
								patRegRequestBean.setExcelRowNum(excelRowNum);
								new Thread( uploadTask ).start();
								
	     					} else {
	     						
	     						CommonControlsBoImpl.showFinalFailureStatus( statusLabel , "No Data to Upload..." );
	     					}
						
						} else {
							CommonControlsBoImpl.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
						}
								
					}
					
				}	 
		     });
	     
	     new Thread( getExcelRowNumTask ).start();
	     
	}
	
	private void configureFileChooser(FileChooser fileChooser) {      
        fileChooser.setTitle("Select Excel File ...");
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Excel", "*.xls")
        );
    
	}
	
}
