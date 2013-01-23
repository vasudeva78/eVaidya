package com.aj.evaidya.docreg.controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.FadeTransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import org.apache.log4j.Logger;

import com.aj.evaidya.common.bo.CommonBo;
import com.aj.evaidya.common.dao.CommonDao;

public class DocRegController implements Initializable{

	private CommonBo commonBo;
	private static final Logger logger = Logger.getLogger(DocRegController.class);
	
	@FXML
	private Label statusLabel;
	
	@FXML
	private TextField nameTextField;
	
	@FXML
	private ChoiceBox stateChoiceBox;
		
	public void initialize(URL url, ResourceBundle bundle) {
		
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
			.node(nameTextField)
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
					.node(nameTextField)
					.fromX( -25 )
					.toX( 0 )
					.cycleCount( 1 )
					.autoReverse(false)
					.build().play();
			}
		});
		
			
		CommonDao commonDao =  new CommonDao();
		commonBo = new CommonBo(commonDao);
		
		stateChoiceBox.setDisable(true);
		
		commonBo.getStateDropDownList(stateChoiceBox);
		
	}
	
	public void saveAction(){
		logger.debug("name => "+nameTextField.getText());
	}

}
