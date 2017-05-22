package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.freemarker.FreeMarkerDataLoader;
import view.StageLoader;
import view.panes.HomePane;

public class Main extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String location = new String("Timetable6.xml");

//        Timetable timetable = XMLDataLoader.loadDataToXML(location);
//        Timetable timetable = XMLDataLoader.loadDataFromXML(location);

//        Algorithm algorithm = new GradingAlgorithm();
//        XMLDataLoader.loadSolvedTimetableToXML(algorithm.solve(timetable), location);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HomePane home = new HomePane();

        int width = 1366;
        int height = 768;

        Scene homeScene = new Scene(home, width, height);
        homeScene.getStylesheets().add("styles/homeStyle.css");

        StageLoader loader = new StageLoader(primaryStage, homeScene, width, height);

        home.setLoader(loader);

        loader.loadHome();
    }

}
