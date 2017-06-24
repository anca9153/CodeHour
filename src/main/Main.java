package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.StageLoader;
import view.panes.HomePane;

public class Main extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HomePane home = new HomePane();

        int width = 1366;
        int height = 768;

        Scene homeScene = new Scene(home, width, height);
        homeScene.getStylesheets().add("view/styles/homeStyle.css");

        StageLoader loader = new StageLoader(primaryStage, home, homeScene, width, height);

        home.setLoader(loader);

        loader.loadHome();
    }

}
