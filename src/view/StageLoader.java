package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Timetable;
import view.panes.CreatePane;
import view.panes.DisplayPane;
import view.panes.HomePane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Anca on 3/16/2017.
 */
public class StageLoader {
    private static Stage primaryStage;
    private static HomePane homeStage;
    private static Scene home;
    private static List<Double> sizes = new ArrayList<>();

    public StageLoader(Stage primaryStage, HomePane homeStage, Scene homeScene, double width, double height){
        this.primaryStage = primaryStage;
        this.homeStage = homeStage;
        this.home = homeScene;
        sizes.add(width);
        sizes.add(height);
    }

    private static void load(Scene scene){
        primaryStage.setTitle("CodeHour");
        primaryStage.setScene(scene);
        primaryStage.setWidth(sizes.get(0));
        primaryStage.setHeight(sizes.get(1));
        primaryStage.show();
    }

    public static void loadHome(){
        homeStage.resetAvailableTimetablesListView();
        home.setRoot(homeStage);
        load(home);
    }

    public static void loadDisplay(Map<String, Timetable> idTimetableMap, File file, Timetable timetable){
        DisplayPane displayPane = new DisplayPane(idTimetableMap, file, timetable);
        Scene display = new Scene(displayPane, sizes.get(0), sizes.get(1));

        display.getStylesheets().add("styles/displayStyle.css");

        load(display);
    }

    public static void loadCreate(Timetable t, File f){
        CreatePane createPane = new CreatePane(primaryStage, t);

        if(f!=null){
            CreatePane.savingFile = f;
        }

        Scene createScene = new Scene(createPane, sizes.get(0), sizes.get(1));

        createScene.getStylesheets().add("styles/createStyle.css");

        load(createScene);
    }

}
