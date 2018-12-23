package org.suai.poker.graphics;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageWindow {
	public static void show(String title, String message){
		Label label = new Label(message);
		label.setPadding(new Insets(5,0,5,10));
		Scene scene = new Scene(label);
		Stage stage = new Stage();
		stage.getIcons().add(new Image("org/suai/poker/graphics/image/ico16.png"));
		stage.setTitle(title);
		stage.setResizable(false);
		stage.setAlwaysOnTop(true);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.show();
		stage.requestFocus();
	}
}
