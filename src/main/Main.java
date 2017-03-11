package main;

import compute.Algorithm;
import compute.DataLoad;
import compute.algorithms.GradingAlgorithm;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import model.Timetable;
import model.resource.Resource;
import model.resource.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Timetable timetable = DataLoad.loadDataToXML();
        Timetable timetable = DataLoad.loadDataFromXML();

        Algorithm algorithm = new GradingAlgorithm();
        DataLoad.loadSolvedTimetableToXML(algorithm.solve(timetable));

//        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Text leftTitle = new Text("Orare disponibile");
        VBox timetableList = new VBox();
        Button example = new Button("Example Timetable");
        timetableList.getChildren().add(example);

        GridPane leftPane = new GridPane();
        leftPane.setHgap(10);
        leftPane.setVgap(10);
        leftPane.setPadding(new Insets(30, 30, 30, 30));
        leftPane.add(leftTitle, 1, 1);
        leftPane.add(timetableList, 1, 2);

        root.setLeft(leftPane);

        GridPane rightPane = new GridPane();
        rightPane.setHgap(10);
        rightPane.setVgap(10);
        rightPane.setPadding(new Insets(30, 30, 30, 30));
        Button createButton = new Button("Creează orar");
        rightPane.add(createButton, 1, 1);

        root.setRight(rightPane);

        Scene scene = new Scene(root, 800, 550);

        primaryStage.setTitle("CodeHour");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
