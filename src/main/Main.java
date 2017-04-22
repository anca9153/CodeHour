package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.StageLoader;
import view.panes.CreatePane;
import view.panes.HomePane;

public class Main extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String location = new String("Timetable6.xml");

//        Timetable timetable = DataLoader.loadDataToXML(location);
//        Timetable timetable = DataLoader.loadDataFromXML(location);

//        Algorithm algorithm = new GradingAlgorithm();
//        DataLoader.loadSolvedTimetableToXML(algorithm.solve(timetable), location);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HomePane home = new HomePane();
        CreatePane create = new CreatePane();

        int width = 1050;
        int height = 600;

        Scene homeScene = new Scene(home, width, height);
        homeScene.getStylesheets().add("styles/homeStyle.css");
        Scene createScene = new Scene(create, width, height);
        createScene.getStylesheets().add("styles/createStyle.css");

        StageLoader loader = new StageLoader(primaryStage, homeScene, createScene, width, height);

        home.setLoader(loader);

        loader.loadHome();
    }

}
