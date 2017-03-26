package main;

import compute.Algorithm;
import compute.algorithms.GradingAlgorithm;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Timetable;
import utilities.DataLoader;
import utilities.PropertiesLoader;
import view.StageLoader;
import view.panes.CreatePane;
import view.panes.HomePane;

public class Main extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String location = new String("timetable4.xml");

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

        Scene homeScene = new Scene(home, 800, 550);
        Scene createScene = new Scene(create, 800, 550);

        StageLoader loader = new StageLoader(primaryStage, homeScene, createScene);

        home.setLoader(loader);

        loader.loadHome();
    }

}
