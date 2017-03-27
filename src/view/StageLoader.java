package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Timetable;
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
    private static Scene create;
    private static int width;
    private static int height;

    public StageLoader(Stage primaryStage, Scene home, Scene create, int width, int height){
        this.primaryStage = primaryStage;
        this.home = home;
        this.create = create;
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
        load(create);
    }
}
