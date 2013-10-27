package com.aj.evaidya.patreg.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import org.apache.log4j.Logger;
import org.h2.util.IOUtils;

import com.aj.evaidya.common.bo.impl.CommonControlsBoImpl;
import com.aj.evaidya.patreg.beans.PatRegRequestBean;
import com.aj.evaidya.patreg.beans.PatRegResponseBean;

public class PatRegUploadController extends AbstractPatRegController {
	
	private static final Logger logger = Logger.getLogger(PatRegUploadController.class);
	
	private static final Pattern nameRegex = Pattern.compile( "[a-zA-Z ]*" );
	private static final Pattern dobRegex = Pattern.compile( "[0-9]{1,2}-[a-zA-Z]{3}-[0-9]{4}" );
	private static final Pattern addressRegex = Pattern.compile( "[a-zA-Z0-9 ,-/#]*" );
	private static final Pattern stateRegex = Pattern.compile( "[a-zA-Z ]*" );
	private static final Pattern pinCodeRegex = Pattern.compile( "[0-9]*" );
	private static final Pattern telRegex = Pattern.compile( "[0-9 -]*" );
	private static final Pattern dateRegex = Pattern.compile("-");
	
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
		fileLocLabel.setText("");
		resultTextArea.setText("");
		
		uploadProgressBar.setProgress(0);
		uploadProgressBarInd.setProgress(0);
	}
	
	@Override
	protected void abstractResetFields() {
		
	}
	
	public final void browseAction(){
		
		resetUploadAction();
		
		String uploadFileDtls = getUploadFileDtls();

		if(!uploadFileDtls.isEmpty()){
			fChooser.setInitialDirectory(new File(uploadFileDtls));		
		}
			
		File xlFile = fChooser.showOpenDialog( browseButton.getScene().getWindow() );
		
		if(xlFile != null && xlFile.isFile()){
			
			fileLocLabel.setText( xlFile.getAbsolutePath() );
			
			saveUploadFileDtls(xlFile.getParent());
		}
		
	}
	
	public final void uploadAction() throws Exception {
		
		resultTextArea.setText("");
		
		if ( !CommonControlsBoImpl.checkLabelForEmptyString(statusLabel, fileLocLabel , "Empty File ...") ) {
			return;
		};
		
		final PatRegRequestBean patRegRequestBean = new PatRegRequestBean();
		patRegRequestBean.setXlFilePath(fileLocLabel.getText());
		
		final Task<PatRegResponseBean> uploadTask = new Task<PatRegResponseBean>() {

			@Override
			protected PatRegResponseBean call()  {
				
				PatRegResponseBean finalPatRegResponseBean = new PatRegResponseBean();
				
				try {
					finalPatRegResponseBean.setMessage("");
					finalPatRegResponseBean.setStatus("success");
					finalPatRegResponseBean.setRowCnt( 0 );
					 
					int excelRowNum = Integer.parseInt( patRegRequestBean.getExcelRowNum() );
					
					// logger.debug("excelRowNum => "+excelRowNum);
					
					long start = System.nanoTime();
					 
					boolean isLastRow = false;
					
					for(int i = 1;i<=excelRowNum;i++){
						 
						 updateMessage( ((int) ((i/ excelRowNum) * 100))+" %");
						 updateProgress(i, excelRowNum);
						 
						 if (i != excelRowNum) { // To avoid array out of exception when fetching last row
							 
							 PatRegResponseBean patRegResponseBean = patRegBo.getExcelCellDtls(patRegRequestBean,i);
							 
							 if( "success".equals(patRegResponseBean.getStatus() ) ){
								 
								 // Validate each cell
								 
								 if(!checkExcelData(patRegResponseBean,i)){
									 // Error in cell
									 
									 if (finalPatRegResponseBean.getMessage().isEmpty()){
								 			
							 			finalPatRegResponseBean.setMessage( patRegResponseBean.getMessage() );
							 			
							 		 } else {
							 			
							 			finalPatRegResponseBean.setMessage( finalPatRegResponseBean.getMessage().concat("\n").concat(patRegResponseBean.getMessage()) );
							 			
							 		 }
									 continue;
								 }
								 
							 } else {
								 // Error while fetching cells
								 logger.debug("error in fetching cells ");
								 continue;
							 }
							 
							 patRegRequestBean.setNameText( patRegResponseBean.getNameText() );
							 String patDob = patRegResponseBean.getDateOfBirth();
							 
							 String dd = dateRegex.split(patDob)[0];
							 String mmm = dateRegex.split(patDob)[1];
							 String yyyy = dateRegex.split(patDob)[2];
							 
							 if (dd.length() != 2){
								 dd = "0".concat(dd);
							 }
							 
							 String patMon =  monthIndex(mmm) < 10 ? "0" + monthIndex(mmm) : monthIndex(mmm) +"";
							 	
							 // for easy db insertion
							 patRegRequestBean.setDateOfBirth( yyyy.concat("-").concat(patMon).concat("-").concat(dd) );
							 
							 patRegRequestBean.setAddress1Text( patRegResponseBean.getAddress1Text() );
							 patRegRequestBean.setAddress2Text( patRegResponseBean.getAddress2Text() );
							 patRegRequestBean.setStateText( patRegResponseBean.getStateText() );
							 patRegRequestBean.setPincode(patRegResponseBean.getPincode());
							 patRegRequestBean.setSex(patRegResponseBean.getSex());
							 patRegRequestBean.setTel1Text(patRegResponseBean.getTel1Text());
							 patRegRequestBean.setTel2Text(patRegResponseBean.getTel2Text());
							 patRegRequestBean.setFatNameText(patRegResponseBean.getFatNameText());
						 }
						 else {
							 // last row reached. i == excelRowNum
							 isLastRow = true;
						 }
						 						 
						 PatRegResponseBean patRegResponseBean = patRegBo.uploadPatDtlsToDb(patRegRequestBean,isLastRow);
												 
						 switch( patRegResponseBean.getStatus() ){
						 
						 	case "success":
						 		finalPatRegResponseBean.setRowCnt( finalPatRegResponseBean.getRowCnt() +  patRegResponseBean.getRowCnt() );	
						 		break;
						 	
						 	case "error":
						 		if (finalPatRegResponseBean.getMessage().isEmpty()){
						 			
						 			finalPatRegResponseBean.setMessage( patRegResponseBean.getMessage() );
						 			
						 		} else {
						 			
						 			finalPatRegResponseBean.setMessage( finalPatRegResponseBean.getMessage().concat("\n").concat(patRegResponseBean.getMessage()) );
						 			
						 		}
						 		
						 		break;
						 		
						 }
						 					 
						 // Thread.sleep(250);
					 }
					
					System.out.println("total time taken => "+TimeUnit.MILLISECONDS.convert( (System.nanoTime()-start ) , TimeUnit.NANOSECONDS));
					
				}  catch (Exception e) {
					logger.error("uploadTask error ",e);
				}
	        	 
	        	 return finalPatRegResponseBean;
			}
			
			@Override
			protected void succeeded(){
				PatRegResponseBean patRegResponseBean = getValue();
				
				logger.debug("PatRegResponseBean finally "+patRegResponseBean);
				
				if (  patRegResponseBean.getRowCnt() > 0 ){
					
					CommonControlsBoImpl.showFinalSuccessStatus( statusLabel , "Saved" );
				}
				
				resultTextArea.appendText( patRegResponseBean.getMessage() );
				
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
		
		File sampleFile = new File(".","samples.xls");
		
		if(!sampleFile.exists()){
			
			// Copy to current directory
			try(InputStream is = getClass().getResourceAsStream("/samples.xls")){
				Files.copy(is, FileSystems.getDefault().getPath(".","samples.xls"));
			}
		
			Thread.sleep(2);
		}
				
		Desktop.getDesktop().open( new File( new File(".").getCanonicalFile() , "samples.xls" ) );
	}
	
	private int monthIndex(String monthName) throws Exception{
		
		int monthIndx = -1;
		
		try {
			monthIndx = Month.valueOf(monthName.toLowerCase()).ordinal();
		} catch (Exception e) {
			throw new Exception("MONTH");
		}
				
		return  (monthIndx + 1);
	}

	private final boolean checkExcelData( PatRegResponseBean patRegResponseBean , int excelRowNum) throws Exception {
		
		boolean allOk = true;
	
		if(patRegResponseBean.getNameText().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 1 ] : Patient Name ::".concat(" Empty ")) );
			return false;
		}
				
		if ( !nameRegex.matcher( patRegResponseBean.getNameText() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 1 ] : Patient Name ::".concat(" Only Letters Allowed ")) );
			return false;
		}
			
		if(patRegResponseBean.getDateOfBirth().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 2 ] : Date Of Birth ::".concat(" Empty ")) );
			return false;
		}
		
		// Validate patient date of birth
		
		if ( !dobRegex.matcher( patRegResponseBean.getDateOfBirth() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 2 ] : Date Of Birth ::".concat(" Invalid Format. Example for Correct format is 12-May-1977 ")) );
			return false;
		}
		
		String dtStr = patRegResponseBean.getDateOfBirth();
		
		try {
			cal.clear();
			cal.set( Integer.parseInt( dateRegex.split(dtStr)[2] ), monthIndex( dateRegex.split(dtStr)[1].toLowerCase() ), Integer.parseInt(dateRegex.split(dtStr)[0] )) ;
			cal.getTime();
		} catch (Exception e) {
			
			switch( e.getMessage() ){
			
				case "YEAR":
					patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 2 ] : Date Of Birth ::".concat(" Invalid Year")) );
					break;
				
				case "MONTH":
					patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 2 ] : Date Of Birth ::".concat(" Invalid Month")) );
					break;
				
				default:	
					patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1)).concat( " ] Column[ 2 ] : Date Of Birth ::".concat(" Invalid Date")) );
					
			}
			
			return false;
		}
				
		if(patRegResponseBean.getAddress1Text().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1)).concat( " ] Column[ 3 ] : Address 1 ::".concat(" Empty ")) );
			return false;
		}
			
		if ( !addressRegex.matcher( patRegResponseBean.getAddress1Text() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 3 ] : Address 1 ::".concat(" Only Letters and Symbols , - / # Allowed" )) );
			return false;
		}
		
		if ( !addressRegex.matcher( patRegResponseBean.getAddress2Text() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1)).concat( " ] Column[ 4 ] : Address 2 ::".concat(" Only Letters and Symbols , - / # Allowed" )) );
			return false;
		}
		
		if(patRegResponseBean.getStateText().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 5 ] : State ::".concat(" Empty ")) );
			return false;
		}
		
		if ( !stateRegex.matcher( patRegResponseBean.getStateText() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 5 ] : State ::".concat(" Only Letters Allowed" )) );
			return false;
		}
		
		if ( !pinCodeRegex.matcher( patRegResponseBean.getPincode() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 6 ] : Pin Code ::".concat( " Only Digits Allowed" )) );
			return false;
		}

		if(patRegResponseBean.getSex().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 7 ] : Sex ::".concat(" Empty ")) );
			return false;
		}
		
		if(! (patRegResponseBean.getSex().equalsIgnoreCase("Male") || patRegResponseBean.getSex().equalsIgnoreCase("Female")) ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 7 ] : Sex ::".concat(" Only Male or Female values Allowed")) );
			return false;
		}
		
		if(patRegResponseBean.getTel1Text().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 8 ] : Telephone 1 ::".concat(" Empty ")) );
			return false;
		}
		
		if ( !telRegex.matcher( patRegResponseBean.getTel1Text() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 8 ] : Telephone 1 ::".concat(" Only Digits and Symbol - Allowed")) );
			return false;
		}
		
		if ( !telRegex.matcher( patRegResponseBean.getTel2Text() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 9 ] : Telephone 2 ::".concat(" Only Digits and Symbol - Allowed")) );
			return false;
		}
		
		if(patRegResponseBean.getFatNameText().isEmpty()){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 10 ] : Father's Name ::".concat(" Empty ")) );
			return false;
		}
		
		if ( !nameRegex.matcher( patRegResponseBean.getFatNameText() ).matches() ){
			patRegResponseBean.setMessage("Row [ ".concat(String.valueOf( excelRowNum + 1 )).concat( " ] Column[ 10 ] : Father's Name ::".concat(" Only Letters Allowed")) );
			return false;
		}
	
		return allOk;
	}	
}
