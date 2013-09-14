package com.aj.evaidya.patreg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import org.h2.util.IOUtils;

import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public class PatRegUploadController extends AbstractPatRegController {
	
	@FXML
	private Label fileLocLabel;
	
	@FXML
	private Button browseButton;
	
	@FXML
	private ProgressBar uploadProgressBar;
	
	@FXML
	private ProgressIndicator uploadProgressBarInd;
	
	@FXML
	private TextArea resultTextArea;
	
	private FileChooser fChooser;
		
	protected void populateFieldsOnIinit() {			
		fChooser = new FileChooser();
		configureFileChooser(fChooser);
				
		try {
			File propFile = new File(".",".evaidya");
		
			if(!propFile.exists()){
				propFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		
		String uploadFileDtls = getUploadFileDtls();
		
		if(!uploadFileDtls.isEmpty()){
			fChooser.setInitialDirectory(new File(uploadFileDtls));		
		}
			
		File xlFile = fChooser.showOpenDialog( browseButton.getScene().getWindow() );
		
		if(xlFile.isFile()){
			
			fileLocLabel.setText( xlFile.getAbsolutePath() );
			
			saveUploadFileDtls(xlFile.getParent());
		}
		
	}
	
	public final void uploadAction() throws Exception {
		
		if ( !CommonControlsBoImpl.checkLabelForEmptyString(statusLabel, fileLocLabel , "Empty File ...") ) {
			return;
		};
		
		final PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
		patRegRequestBean.setXlFilePath(fileLocLabel.getText());
		
		patRegRequestBean.setDbUrl(dbUrl);
		patRegRequestBean.setDbUsername(dbUsername);
		patRegRequestBean.setDbPwd(dbPwd);
		
		final Task<PatRegResponseBean> uploadTask = new Task<PatRegResponseBean>() {

			@Override
			protected PatRegResponseBean call() throws Exception {
				
				PatRegResponseBean patRegResponseBean = new PatRegResponseBean();
	        	 
				int excelRowNum = Integer.parseInt( patRegRequestBean.getExcelRowNum() );
				 
				 for(int i = 1;i<=excelRowNum;i++){
					 
					 patRegResponseBean = patRegBo.uploadPatDtls(patRegRequestBean,i,excelRowNum);
					 
					 updateMessage( ((int) ((i/ excelRowNum) * 100))+" %");
					 updateProgress(i, excelRowNum);
					 
	        		 if (null != patRegResponseBean.getErrMessage() && !patRegResponseBean.getErrMessage().isEmpty() ){
	        			 resultTextArea.setText( resultTextArea.getText().concat("\n").concat(patRegResponseBean.getErrMessage() ) ); 
	        		 }
					 
					 Thread.sleep(250);
				 }
	        	 
	        	 return patRegResponseBean;
			}
			
			@Override
			protected void succeeded(){
				PatRegResponseBean patRegResponseBean = getValue();
				
				if( "success".equals(patRegResponseBean.getStatus() ) ){
					CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , patRegResponseBean.getMessage() );
				
				} else {
					CommonControlsBoImpl.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
				}
			}
				
		};
		
		uploadProgressBar.progressProperty().bind( uploadTask.progressProperty() );
		uploadProgressBarInd.progressProperty().bind( uploadProgressBar.progressProperty() );
    
	    new Thread(new Task<PatRegResponseBean>() {

			@Override
			protected PatRegResponseBean call() throws Exception {
				return patRegBo.getExcelRowsOnUpload(patRegRequestBean);
			}
			
			@Override
			protected void succeeded(){
				PatRegResponseBean patRegResponseBean = getValue();
				
				if( "success".equals(patRegResponseBean.getStatus() ) ){
					String excelRowNum = patRegResponseBean.getExcelRowNum();
					
					if ( Integer.parseInt(excelRowNum) != 0 ){
						
						patRegRequestBean.setExcelRowNum(excelRowNum);
						
						// Got Excel row count
						new Thread(uploadTask).start();
						
 					} else {
 						
 						CommonControlsBoImpl.showFinalFailureStatus( statusLabel , "No Data to Upload..." );
 					}
				
				} else {
					CommonControlsBoImpl.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
				}
			}
				
		}).start();
	     
	}
	
	private void configureFileChooser(FileChooser fileChooser) {      
        fileChooser.setTitle("Select Excel File ...");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Excel", "*.xls")
        );
    
	}
	
	private void saveUploadFileDtls(String filePath) {
        OutputStream os = null;
        
		try{
        	
        	os = new FileOutputStream(new File(".",".evaidya"));
        	Properties props = new Properties();
        	props.setProperty("lastUploadFile", filePath);
        	props.store(os, "");
        	
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	
        	IOUtils.closeSilently(os);
        }
	}
	
	private String getUploadFileDtls() {
		
		String lastUploadFile = "";
        InputStream is = null;
        
		try{
        	
        	is = new FileInputStream(new File(".",".evaidya"));
        	Properties props = new Properties();
        	props.load(is);
        	lastUploadFile = props.getProperty("lastUploadFile");
        	
        }catch(Exception e){
        	e.printStackTrace();
        	
        }finally{
        	
        	IOUtils.closeSilently(is);
        }
		
		return ( lastUploadFile == null ? "" : lastUploadFile );
	}
}
