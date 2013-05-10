package com.aj.evaidya.common.bo;

import java.util.Calendar;
import java.util.regex.Pattern;

import javafx.animation.FadeTransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import org.apache.log4j.Logger;

public final class CommonControlsBo {

	private static final Logger logger = Logger.getLogger(CommonControlsBo.class);
	
	private static final Calendar now;
	
	static{
		now = Calendar.getInstance();
		now.setLenient(false);
	}
	
	private CommonControlsBo(){}
	
	public static boolean checkTextFieldForEmptyString (Label statusLabel,TextField textField,String errMessage) {
		
		if ( textField.getText().trim().isEmpty() ){
			CommonControlsBo.showErrorMessage(statusLabel, textField , errMessage );
			
			return false;
		}
		
		return true;
		
	}

   public static void showFinalSuccessStatus (Label statusLabel , String errMessage) {
		
		if ( !errMessage.trim().isEmpty() ){
			
			statusLabel.setText( errMessage );
			statusLabel.setTextFill(Color.GREEN);
			
			// Fade status label contents
			FadeTransitionBuilder.create()
				.duration(Duration.seconds(4))
				.node( statusLabel )
				.fromValue(1)
				.toValue(-1)
				.cycleCount( 1 )
				.build().play();
		}
		
	}

   public static void showFinalFailureStatus (Label statusLabel , String errMessage) {
		
		if ( !errMessage.trim().isEmpty() ){
			
			statusLabel.setText( errMessage );
			statusLabel.setTextFill(Color.RED);
			
			// Fade status label contents
			FadeTransitionBuilder.create()
				.duration(Duration.seconds(4))
				.node( statusLabel )
				.fromValue(1)
				.toValue(-1)
				.cycleCount( 1 )
				.build().play();
		}
		
	}
   
	public static boolean checkTextFieldForInvalidLetters (Label statusLabel, TextField textField ,String regExp ,String errMessage) {
		
		if ( !Pattern.compile( regExp ).matcher( textField.getText().trim() ).matches() ){
			CommonControlsBo.showErrorMessage(statusLabel, textField , errMessage );
			
			return false;
		}
		
		return true;
	}
	
	public static boolean checkSelectionBox (Label statusLabel, ChoiceBox choiceBox , String errMessage) {
		
		logger.debug("choiceBox value => "+choiceBox.getValue());
		
		if ( choiceBox.getValue().toString().trim().equals( "-- Select --" ) || choiceBox.getValue().toString().trim().equals( "--" ) ){
			CommonControlsBo.showErrorMessage(statusLabel, choiceBox , errMessage );
			
			return false;
		}
		
		return true;
	}
	
   public static boolean checkForValidDate (Label statusLabel, TextField dateTextField ,ChoiceBox monthChoiceBox , TextField yearTextField) {
	
		try{
			
			now.set( Integer.parseInt( yearTextField.getText()) , monthChoiceBox.getSelectionModel().getSelectedIndex() - 1 , Integer.parseInt( dateTextField.getText() ) );
			
			now.getTime();
			
			return true;
		}catch(Exception e){
			
			if (e.getMessage().equals("YEAR")){
				CommonControlsBo.showErrorMessage(statusLabel, yearTextField , "Invalid Year." );
			} else if (e.getMessage().equals("MONTH")){
				CommonControlsBo.showErrorMessage(statusLabel, monthChoiceBox , "Invalid Month." );
			} else {
				CommonControlsBo.showErrorMessage(statusLabel, dateTextField , "Invalid Date." );
			}
				
			return false;
		}

   }

	public static void showErrorMessage(Label statusLabel,final Node inputControlNode,String errMessage){
		
		statusLabel.setText( errMessage );
		statusLabel.setTextFill(Color.RED);
		
		inputControlNode.requestFocus();
		
		// Fade status label contents
		FadeTransitionBuilder.create()
			.duration(Duration.seconds(4))
			.node( statusLabel )
			.fromValue(1)
			.toValue(-1)
			.cycleCount( 1 )
			.build().play();
		
		// Shake text field on error			
		final TranslateTransition translateTransition = TranslateTransitionBuilder.create()
			.duration(Duration.seconds(0.15))
			.node(inputControlNode)
			.fromX( -25 )
			.toX( 25 )
			.cycleCount( 4 )
			.autoReverse(true)
			.build();
		
		translateTransition.play();
		
		translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				TranslateTransitionBuilder.create()
					.duration(Duration.seconds(0.15))
					.node( inputControlNode )
					.fromX( -25 )
					.toX( 0 )
					.cycleCount( 1 )
					.autoReverse(false)
					.build().play();
			}
		});
				
	}

}