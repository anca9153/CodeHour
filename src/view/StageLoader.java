package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.stage.Stage;
import model.Timetable;
import view.panes.CreatePane;
import view.panes.DisplayPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Anca on 3/16/2017.
 */
public class StageLoader {
    private static Stage primaryStage;
    private static Scene home;
    private static List<Double> sizes = new ArrayList<>();

    public StageLoader(Stage primaryStage, Scene home, double width, double height){
        this.primaryStage = primaryStage;
        this.home = home;
        sizes.add(width);
        sizes.add(height);

//        this.home.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println("Width: " + newSceneWidth);
//                sizes.remove(0);
//                sizes.add(0, newSceneWidth.doubleValue());
//            }
//        });
//
//        this.home.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                System.out.println("Height: " + newSceneHeight);
//                sizes.remove(1);
//                sizes.add(1, newSceneHeight.doubleValue());
//            }
//        });
    }

    private static void load(Scene scene){
        System.out.println(sizes.get(0)+" "+ sizes.get(1));

        primaryStage.setTitle("CodeHour");
        primaryStage.setScene(scene);
        primaryStage.setWidth(sizes.get(0));
        primaryStage.setHeight(sizes.get(1));
        primaryStage.show();
    }

    public static void loadHome(){
        load(home);
    }

    public static void loadDisplay(Map<String, Timetable> idTimetableMap, File file, Timetable timetable){
        DisplayPane displayPane = new DisplayPane(idTimetableMap, file, timetable);
        Scene display = new Scene(displayPane, sizes.get(0), sizes.get(1));

//        display.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println("Width: " + newSceneWidth);
//                sizes.remove(0);
//                sizes.add(0, newSceneWidth.doubleValue());
//            }
//        });
//
//        display.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                System.out.println("Height: " + newSceneHeight);
//                sizes.remove(1);
//                sizes.add(1, newSceneHeight.doubleValue());
//            }
//        });

        display.getStylesheets().add("styles/displayStyle.css");

        load(display);
    }

    public static void loadCreate(Timetable t, File f){
        CreatePane createPane = new CreatePane(primaryStage, t);

        if(f!=null){
            CreatePane.savingFile = f;
        }

        Scene createScene = new Scene(createPane, sizes.get(0), sizes.get(1));

//        createScene.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println("Width: " + newSceneWidth);
//                sizes.remove(0);
//                sizes.add(0, newSceneWidth.doubleValue());
//            }
//        });
//
//        createScene.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                System.out.println("Height: " + newSceneHeight);
//                sizes.remove(1);
//                sizes.add(1, newSceneHeight.doubleValue());
//            }
//        });

        createScene.getStylesheets().add("styles/createStyle.css");

        load(createScene);
    }

}
