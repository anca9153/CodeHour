package main;

import compute.Algorithm;
import compute.DataLoad;
import compute.algorithms.GradingAlgorithm;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import model.Timetable;
import model.constraint.types.AssignTimeConstraint;
import model.constraint.types.LimitIdleTimesConstraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;
import model.time.Time;
import model.time.Times;

import java.io.File;
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

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        File loadFolder = new File(DataLoad.getLoadPath());
        List<File> timetablesToDisplay = new ArrayList<>();

        for (final File fileEntry : loadFolder.listFiles()) {
            if (!fileEntry.isDirectory() && fileEntry.getName().startsWith("timetable")){
                timetablesToDisplay.add(fileEntry);
            }
        }

        BorderPane displayTimetable = new BorderPane();

        Button mainPageButton = new Button("Main Page");
        mainPageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = new Scene(root, 800, 550);

                primaryStage.setTitle("CodeHour");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });

        displayTimetable.setTop(mainPageButton);


        Text leftTitle = new Text("Orare disponibile");
        VBox timetableList = new VBox();

        for(File fileEntry: timetablesToDisplay){
            Button fileButton= new Button(fileEntry.getName());
            fileButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    Scene scene = new Scene(displayTimetable, 800, 550);

                    primaryStage.setTitle("CodeHour");
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            });
            timetableList.getChildren().add(fileButton);
        }

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
