package com.aj.evaidya.menu.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import com.aj.evaidya.config.EvaidyaBindings;
import com.aj.evaidya.docreg.controller.DocRegEditController;
import com.aj.evaidya.docreg.controller.DocRegNewController;
import com.aj.evaidya.patreg.controller.PatRegEditController;
import com.aj.evaidya.patreg.controller.PatRegNewController;
import com.aj.evaidya.patreg.controller.PatRegUploadController;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class MenuController implements Initializable {
	
	private Map<String, Initializable> menuControllerKeys;

//	@FXML
//	private AnchorPane navigationPane;
	
	@FXML
	private AnchorPane menuFieldsPane;
	
//	@FXML
//	private DocNewRegControllers docNewRegControllers;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		// Load All dependencies
		
		Injector injector = Guice.createInjector(new EvaidyaBindings());
		
		DocRegNewController docRegNewController = injector.getInstance(DocRegNewController.class);
		DocRegEditController docRegEditController = injector.getInstance(DocRegEditController.class);
		
		PatRegNewController patRegNewController = injector.getInstance(PatRegNewController.class);
		PatRegEditController patRegEditController = injector.getInstance(PatRegEditController.class);
		PatRegUploadController patRegUploadController = injector.getInstance(PatRegUploadController.class);
		
		menuControllerKeys = new HashMap<String, Initializable>();
		menuControllerKeys.put("docRegNew", docRegNewController);
		menuControllerKeys.put("docRegEdit", docRegEditController);
		menuControllerKeys.put("patRegNew", patRegNewController);
		menuControllerKeys.put("patRegEdit", patRegEditController);
		menuControllerKeys.put("patRegUpload", patRegUploadController);
		
	}
	
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
        	
        	FXMLLoader loader = new FXMLLoader( getClass().getResource( menuItemId.concat(".fxml")) );
        	loader.setController( menuControllerKeys.get(menuItemId) );
        	
			GridPane includeGridPane = (GridPane) loader.load();
			
			((Label)((VBox)includeGridPane.getChildren().get(0)).getChildren().get(0)).setText(menu.getParentMenu().getParentMenu().getText().trim() + " >> " +menu.getParentMenu().getText().trim() + " >> "+ menu.getText().trim() );
			
			menuFieldsPane.getChildren().add( includeGridPane );
			
		} catch (Exception e) {

			e.printStackTrace();
		}
        
    }

}
