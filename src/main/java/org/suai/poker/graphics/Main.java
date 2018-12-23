package org.suai.poker.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	private static boolean status;
	
	private Image icon;
	
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			icon = new Image("org/suai/poker/graphics/image/ico16.png");
			stage.getIcons().add(icon);
			stage.setMinWidth(800);
			stage.setMinHeight(600);
			stage.setTitle("Texas Holdem");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionWindow.show(e);
		}
	}
	
	@Override
	public void stop() throws Exception{
		status = false;	
	}
	
	public static void main(String[] args) {
		status = true;
		launch(args);
	}

	public static boolean getStatus(){
		return status;
	}

	public static String getBuild(){
		return "v0.2.0.1";
	}
}
