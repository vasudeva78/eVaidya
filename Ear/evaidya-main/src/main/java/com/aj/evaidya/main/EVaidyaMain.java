package com.aj.evaidya.main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
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
	public void start(Stage stage) throws Exception {

		Label label = LabelBuilder.create().text("Maven + JavaFX = true")
				.alignment(Pos.CENTER).build();

		Scene scene = new Scene(label, 200, 100);
		stage.setScene(scene);
		stage.show();
	}
}
