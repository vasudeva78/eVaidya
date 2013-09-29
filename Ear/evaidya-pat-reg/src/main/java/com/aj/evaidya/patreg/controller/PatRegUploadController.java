package com.aj.evaidya.patreg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Properties;
import java.util.regex.Pattern;

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
	
	private enum Month{
		jan,
		feb,
		mar,
		apr,
		may,
		jun,
		jul,
		aug,
		sep,
		oct,
		nov,
		dec
	}
	
	private static final Calendar cal = Calendar.getInstance();
		
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
		
		cal.setLenient(false);
	}
	
	protected PatRegRequestBean populatePatRegRequestBean(){
		
		PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
		
		return patRegRequestBean;
		
	}
	
	@Override
	protected void saveDbTask(final PatRegRequestBean patRegRequestBean){
		
	 
	}

	protected void resetUploadAction(){
		
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
				
				PatRegResponseBean finalPatRegResponseBean = new PatRegResponseBean();
				finalPatRegResponseBean.setMessage("");
				finalPatRegResponseBean.setStatus("success");
				finalPatRegResponseBean.setRowCnt("0");
	        	 
				int excelRowNum = Integer.parseInt( patRegRequestBean.getExcelRowNum() );
				 
				 for(int i = 1;i<=excelRowNum;i++){
					 
					 updateMessage( ((int) ((i/ excelRowNum) * 100))+" %");
					 updateProgress(i, excelRowNum);
					 
					 if (i != excelRowNum){ // To avoid array out of exception when fetching last row
						 
						 PatRegResponseBean patRegResponseBean = patRegBo.getExcelCellDtls(patRegRequestBean,i);
						 
						 if( "success".equals(patRegResponseBean.getStatus() ) ){
							 
							 // Validate each cell
							 
							 if(!checkExcelData(patRegResponseBean,i)){
								 // Error in cell
								 
								 finalPatRegResponseBean.setMessage( finalPatRegResponseBean.getMessage().concat("/n ").concat(patRegResponseBean.getMessage()) );
								 continue;
							 }
							 
						 } else {
							 // Error while fetching cells
							 continue;
						 }
						 
						 // Copy patRegResponseBean To patRegRequestBean
						 
						 patRegRequestBean.setNameText( patRegResponseBean.getNameText() );
						 patRegRequestBean.setDateOfBirth( patRegResponseBean.getDateOfBirth() );
						 patRegRequestBean.setAddress1Text( patRegResponseBean.getAddress1Text() );
						 patRegRequestBean.setAddress2Text( patRegResponseBean.getAddress2Text() );
						 patRegRequestBean.setStateText( patRegResponseBean.getStateText() );
						 patRegRequestBean.setPincode(patRegResponseBean.getPincode());
						 patRegRequestBean.setSex(patRegResponseBean.getSex());
						 patRegRequestBean.setTel1Text(patRegResponseBean.getTel1Text());
						 patRegRequestBean.setTel2Text(patRegResponseBean.getTel2Text());
						 patRegRequestBean.setFatNameText(patRegResponseBean.getFatNameText());
					 }
					 
					 
					 PatRegResponseBean patRegResponseBean = patRegBo.uploadPatDtlsToDb(patRegRequestBean,i,excelRowNum);
					 
					 switch( patRegResponseBean.getStatus() ){
					 
					 	case "success":
					 		finalPatRegResponseBean.setRowCnt( String.valueOf( Integer.parseInt(finalPatRegResponseBean.getRowCnt()) + Integer.parseInt(patRegResponseBean.getRowCnt())) );	
					 		break;
					 	
					 	case "error":
					 		finalPatRegResponseBean.setMessage( finalPatRegResponseBean.getMessage().concat("/n ").concat(patRegResponseBean.getMessage()) );
					 		break;
					 		
					 }
					 					 
					 // Thread.sleep(250);
				 }
	        	 
	        	 return finalPatRegResponseBean;
			}
			
			@Override
			protected void succeeded(){
				PatRegResponseBean patRegResponseBean = getValue();
				
				if (Integer.parseInt( patRegResponseBean.getRowCnt() ) > 0 ){
					CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , "Saved" );
				}
				
				resultTextArea.setText( patRegResponseBean.getMessage() );
				
//				if( "success".equals(patRegResponseBean.getStatus() ) ){
//					CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , patRegResponseBean.getMessage() );
//				
//				} else {
//					CommonControlsBoImpl.showFinalFailureStatus( statusLabel , patRegResponseBean.getMessage() );
//				}
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
					
					// Error
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
	
	public final void sampleAction() throws Exception {
		
	}
	
	private int monthIndex(String monthName) throws Exception{
		
		int monthIndx = -1;
		
		try {
			monthIndx = Month.valueOf(monthName).ordinal();
		} catch (Exception e) {
			throw new Exception("MONTH");
		}
				
		return  monthIndx;
	}

	private final boolean checkExcelData( PatRegResponseBean patRegResponseBean , int excelRowNum) throws Exception {
		
		boolean allOk = true;
	
		if(patRegResponseBean.getNameText().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[1] : Name ::".concat(" Empty ")) );
			return false;
		}
				
		if ( !Pattern.compile( "[a-zA-Z ]*" ).matcher( patRegResponseBean.getNameText() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[1] : Name ::".concat(" Only Letters Allowed ")) );
			return false;
		}
			
		if(patRegResponseBean.getDateOfBirth().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[2] : Date Of Birth ::".concat(" Empty ")) );
			return false;
		}
		
		// Validate patient date of birth
		
		if ( !Pattern.compile( "[0-9]{1,2}-[a-zA-Z]{3}-[0-9]{4}" ).matcher( patRegResponseBean.getDateOfBirth() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[2] : Date Of Birth ::".concat(" Invalid Format. Example for Correct format is 12-May-1977 ")) );
			return false;
		}
		
		String dtStr = patRegResponseBean.getDateOfBirth();
		
		try {
			cal.clear();
			cal.set( Integer.parseInt( dtStr.split("-")[2] ), monthIndex( dtStr.split("-")[1].toLowerCase() ), Integer.parseInt(dtStr.split("-")[0] )) ;
			cal.getTime();
		} catch (Exception e) {
			
			switch( e.getMessage() ){
			
				case "YEAR":
					patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[2] : Date Of Birth ::".concat(" Invalid Year")) );
					break;
				
				case "MONTH":
					patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[2] : Date Of Birth ::".concat(" Invalid Month")) );
					break;
				
				default:	
					patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[2] : Date Of Birth ::".concat(" Invalid Date")) );
					
			}
			
			return false;
		}
				
		if(patRegResponseBean.getAddress1Text().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[3] : Address 1 ::".concat(" Empty ")) );
			return false;
		}
			
		if ( !Pattern.compile( "[a-zA-Z0-9 ,-/#]*" ).matcher( patRegResponseBean.getAddress1Text() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[3] : Address 1 ::".concat(" Only Letters and Symbols , - / # Allowed" )) );
			return false;
		}
		
		if ( !Pattern.compile( "[a-zA-Z0-9 ,-/#]*" ).matcher( patRegResponseBean.getAddress2Text() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[4] : Address 2 ::".concat(" Only Letters and Symbols , - / # Allowed" )) );
			return false;
		}
		
		if(patRegResponseBean.getState().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[5] : State ::".concat(" Empty ")) );
			return false;
		}
		
		if ( !Pattern.compile( "[0-9]*" ).matcher( patRegResponseBean.getPincode() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[6] : Pincode ::".concat( " Only Digits Allowed" )) );
			return false;
		}

		if(patRegResponseBean.getSex().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[7] : Sex ::".concat(" Empty ")) );
			return false;
		}
		
		if(! (patRegResponseBean.getSex().equalsIgnoreCase("Male") || patRegResponseBean.getSex().equalsIgnoreCase("Female")) ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[7] : Sex ::".concat(" Only Male or Female values Allowed")) );
			return false;
		}
		
		if(patRegResponseBean.getTel1Text().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[8] : Telephone 1 ::".concat(" Empty ")) );
			return false;
		}
		
		if ( !Pattern.compile( "[0-9 -]*" ).matcher( patRegResponseBean.getTel1Text() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[8] : Telephone 1 ::".concat(" Only Digits and Symbol - Allowed")) );
			return false;
		}
		
		if ( !Pattern.compile( "[0-9 -]*" ).matcher( patRegResponseBean.getTel2Text() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[9] : Telephone 2 ::".concat(" Only Digits and Symbol - Allowed")) );
			return false;
		}
		
		if(patRegResponseBean.getFatNameText().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[10] : Father's Name ::".concat(" Empty ")) );
			return false;
		}
		
		if ( !Pattern.compile( "[a-zA-Z ]*" ).matcher( patRegResponseBean.getFatNameText() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum )).concat( " ] Column[10] : Father's Name ::".concat(" Only Letters Allowed")) );
			return false;
		}
	
		return allOk;
	}	
}
