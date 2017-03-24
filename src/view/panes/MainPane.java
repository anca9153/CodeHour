package view.panes;

import compute.DataLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.StageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anca on 3/16/2017.
 */
public abstract class MainPane extends BorderPane {

    protected void addToolbar(boolean withHomeButton){
        ToolBar toolBar = new ToolBar();

        if(withHomeButton){
            Button home = new Button("Acasa");
            home.setOnAction((ActionEvent event) ->
                    StageLoader.loadHome()
            );
            toolBar.getItems().add(home);
        }

        Button settings = new Button("Setari");
        settings.setOnAction((ActionEvent event) ->
            StageLoader.loadHome()
        );
        toolBar.getItems().add(settings);

        this.setTop(toolBar);
    }

}
