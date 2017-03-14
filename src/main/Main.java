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
import model.constraint.types.AssignTimeConstraint;
import model.constraint.types.LimitIdleTimesConstraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;
import model.time.Time;
import model.time.Times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Resource r = new Resource("dpop", "Dan Pop", "teacher");
//        List<Resource> resourceList = Arrays.asList(
//                //Teachers
//                r,
//                new Resource("catnis", "Catalina Ilas", "teacher"),
//                new Resource("vhariuc", "Virgil Hariuc", "teacher"),
//                new Resource("asava", "Ana Savascu", "teacher"),
//                new Resource("doniciuc", "Daniel Oniciuc", "teacher"),
//                //StudyGroups
//                new Resource("9A", "", "studyGroup"),
//                new Resource("9B", "", "studyGroup"),
//                new Resource("10A", "", "studyGroup"),
//                new Resource("10B", "", "studyGroup"),
//                new Resource("11A", "", "studyGroup"),
//                new Resource("12A", "", "studyGroup")
//        );
//
//        List<String> weekdays = Arrays.asList("monday", "tuesday", "wednesday","thursday", "friday");
//        List<Time> timeList = new ArrayList<>();
//
//        for(int day = 0; day<5; day++) {
//            for (int hour = 0; hour < 8; hour++) {
//                Time time = new Time(new StringBuilder().append(String.valueOf(hour+1)).append("_").append(weekdays.get(day).substring(0, 3)).toString(), weekdays.get(day));
//                timeList.add(time);
//            }
//        }
//
//        Times times = new Times();
//        times.setTimes(timeList);
//
//        List<Event> eventList = Arrays.asList(
//                new Event("ev1", 1, new Time("3_mon", "monday"), new Resources(Arrays.asList(resourceList.get(0), resourceList.get(5),null))),
//                new Event("ev2", 1, new Time("1_mon", "monday"), new Resources(Arrays.asList(resourceList.get(0), resourceList.get(5), null))),
//                new Event("ev3", 1, new Time("5_mon", "monday"), new Resources(Arrays.asList(resourceList.get(0), resourceList.get(6), null))),
//                new Event("ev4", 1, new Time("3_fri", "friday"), new Resources(Arrays.asList(resourceList.get(0), resourceList.get(7), null))),
//                new Event("ev5", 1, new Time("7_fri", "friday"), new Resources(Arrays.asList(resourceList.get(0), resourceList.get(8), null))),
//                new Event("ev6", 1, null, new Resources(Arrays.asList(resourceList.get(0), resourceList.get(9), null))),
//                new Event("ev7", 1, null, new Resources(Arrays.asList(resourceList.get(4), resourceList.get(10), null))),
//                new Event("ev8", 1, null, new Resources(Arrays.asList(resourceList.get(4), resourceList.get(6), null))),
//                new Event("ev9", 1, null, new Resources(Arrays.asList(resourceList.get(2), resourceList.get(10), null))),
//                new Event("ev10", 1, null, new Resources(Arrays.asList(resourceList.get(2), resourceList.get(10), null)))
//        );
//
//        LimitIdleTimesConstraint c = new LimitIdleTimesConstraint("limitIdleTimeConstraint", true, 1, 0, null, new Resources(resourceList));
//        c.setEvents(new Events(eventList));
//        c.setTimes(new Times(timeList));
//        System.out.println("cost for prof: "+c.validate(r));

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
