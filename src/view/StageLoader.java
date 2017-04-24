package view;

import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.stage.Stage;
import model.Timetable;
import view.panes.CreatePane;
import view.panes.DisplayPane;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Anca on 3/16/2017.
 */
public class StageLoader {
    private static Stage primaryStage;
    private static Scene home;
    private static int width;
    private static int height;

    public StageLoader(Stage primaryStage, Scene home, int width, int height){
        this.primaryStage = primaryStage;
        this.home = home;
        this.width = width;
        this.height = height;
    }

    private static void load(Scene scene){
        primaryStage.setTitle("CodeHour");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadHome(){
        load(home);
    }

    public static void loadDisplay(Map<String, Timetable> idTimetableMap, File file, Timetable timetable){
        DisplayPane displayPane = new DisplayPane(idTimetableMap, file, timetable);
        Scene display = new Scene(displayPane, width, height);
        display.getStylesheets().add("styles/displayStyle.css");

        load(display);
    }

    public static void loadCreate(){
        Timetable t = new Timetable();
        CreatePane createPane = new CreatePane(primaryStage, t);
        Scene createScene = new Scene(createPane, width, height);
        createScene.getStylesheets().add("styles/createStyle.css");

        load(createScene);
    }

}
