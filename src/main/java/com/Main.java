package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
//		FXMLLoader loader = new FXMLLoader(new File("src/main/resources/sample.fxml").toURI().toURL());
//		System.out.println(getClass().getClassLoader().getResource("sample.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("Decaf Code Editor");

		Scene sc = new Scene(root, 1000, 500);
		sc.getStylesheets().add("java-keywords.css");
		primaryStage.setScene(sc);
		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
