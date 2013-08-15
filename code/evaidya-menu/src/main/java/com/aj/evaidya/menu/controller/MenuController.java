package com.aj.evaidya.menu.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MenuController {

//	@FXML
//	private AnchorPane navigationPane;
	
	@FXML
	private AnchorPane menuFieldsPane;
	
//	@FXML
//	private DocNewRegControllers docNewRegControllers;
	
	@FXML
    private void menuClicked(ActionEvent event) {
		MenuItem menu = (MenuItem)event.getSource();
		
        String menuItemId = menu.getId();
        
        try {
        		
        	// navigationPane.getChildren().clear();
        	
        	// GridPane navigationGridPane = (GridPane)FXMLLoader.load(getClass().getResource( "eVaidya-navigation.fxml" ));
        	
        	// navigationPane.getChildren().add( navigationGridPane );
        	
        	// ((Label)navigationGridPane.getChildren().get(0)).setText(menu.getParentMenu().getParentMenu().getText().trim() + " >> " +menu.getParentMenu().getText().trim() + " >> "+ menu.getText().trim() +" ... ");
        	
        	menuFieldsPane.getChildren().clear();
        	
			GridPane includeGridPane = (GridPane)FXMLLoader.load(getClass().getResource( "eVaidya-"+menuItemId+".fxml" ));
			
			((Label)((VBox)includeGridPane.getChildren().get(0)).getChildren().get(0)).setText(menu.getParentMenu().getParentMenu().getText().trim() + " >> " +menu.getParentMenu().getText().trim() + " >> "+ menu.getText().trim() );
			
			menuFieldsPane.getChildren().add( includeGridPane );
			
		} catch (Exception e) {

			e.printStackTrace();
		}
        
    }

}
