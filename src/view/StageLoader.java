package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.panes.DisplayPane;

import java.io.File;
import java.util.List;

/**
 * Created by Anca on 3/16/2017.
 */
public class StageLoader {
    private static Stage primaryStage;
    private static Scene home;
    private static Scene create;

    public StageLoader(Stage primaryStage, Scene home, Scene create){
        this.primaryStage = primaryStage;
        this.home = home;
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

    public static void loadDisplay(List<File> fileList, File file){
        DisplayPane displayPane = new DisplayPane(fileList, file);
        Scene display = new Scene(displayPane, 900, 550);
        load(display);
    }

    public static void loadCreate(){
        load(create);
    }
}
