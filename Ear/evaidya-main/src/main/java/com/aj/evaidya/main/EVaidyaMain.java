package com.aj.evaidya.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Hello world!
 * 
 */
public class EVaidyaMain extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void init(){	
		Font.loadFont( getClass().getResource("/com/aj/evaidya/main/font/OpenSans-Regular.ttf").toExternalForm() , 10);
		Font.loadFont( getClass().getResource("/com/aj/evaidya/main/font/DroidSerif.ttf").toExternalForm() , 10);
	}
		
	@Override
	public void start(Stage stage) throws Exception {
		

		stage.setTitle("EVaidya-1.0");
		
		Scene scene = (Scene)FXMLLoader.load(getClass().getResource("main.fxml"));
		
		Rectangle2D dimension = Screen.getPrimary().getVisualBounds();	
		stage.setWidth( dimension.getWidth() - 10 );
		stage.setHeight( dimension.getHeight() - 10 );
		
		stage.setResizable(false);
		
		stage.setScene ( scene );
        
		stage.show();
		
	}
}
