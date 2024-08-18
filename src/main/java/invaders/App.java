package invaders;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Scanner;

import invaders.engine.GameEngine;
import invaders.engine.GameWindow;
import invaders.singleton.DifficultChooser;

public class App extends Application {
    private GameEngine model;
    private GameWindow window;
    private String configPath;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setTitle("Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void getEasy(){
        DifficultChooser d = DifficultChooser.getInstance();
        this.configPath = d.getEasy();
    }

    public void getMedium(){
        DifficultChooser d = DifficultChooser.getInstance();
        this.configPath = d.getMedium();
    }

    public void getHard(){
        DifficultChooser d = DifficultChooser.getInstance();
        this.configPath = d.getHard();
    }

    public void startGame() throws CloneNotSupportedException{
        if(this.configPath == null){
            this.configPath = "src/main/resources/config_easy.json";
        }
        model = new GameEngine(this.configPath);
        window = new GameWindow(model);
        window.run();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(window.getScene());
        window.setStage(primaryStage);
        primaryStage.show();

        window.run();
        // primaryStage.close();
        // System.out.println("haha");

    }
}
