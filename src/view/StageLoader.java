package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.panes.DisplayPane;

import java.io.File;

/**
 * Created by Anca on 3/16/2017.
 */
public class StageLoader {
    private static Stage primaryStage;
    private static Scene home;
    private static Scene display;
    private static Scene create;

    public StageLoader(Stage primaryStage, Scene home, Scene display, Scene create){
        this.primaryStage = primaryStage;
        this.home = home;
        this.display = display;
        this.create = create;
    }

    private static void load(Scene scene){
        primaryStage.setTitle("CodeHour");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadHome(){
        load(home);
    }

    public static void loadDisplay(File file){
        DisplayPane displayPane = new DisplayPane();
        displayPane.setFile(file);
        display.setRoot(displayPane);
        load(display);
    }

    public static void loadCreate(){
        load(create);
    }
}
