package org.suai.poker.graphics;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionWindow {
	public static void show(Exception e){
		BorderPane root = new BorderPane();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		TextArea info = new TextArea(sw.toString());
		info.setEditable(false);
		root.setCenter(info);
		Scene scene = new Scene(root,600,400);
		Stage stage = new Stage();
		stage.setTitle("Something happened. Here's stack trace.");
		stage.getIcons().add(new Image("org/suai/poker/graphics/image/ico16.png"));
		stage.setResizable(false);
		stage.setAlwaysOnTop(true);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.show();
		stage.requestFocus();
	}
}
