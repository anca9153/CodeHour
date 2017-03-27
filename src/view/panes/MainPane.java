package view.panes;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import view.StageLoader;

/**
 * Created by Anca on 3/16/2017.
 */
public abstract class MainPane extends BorderPane {

    protected Button getHomeButton() {
        Text text1 = new Text("Code");
        text1.getStyleClass().add("codeText");
        Text text2 = new Text("Hour");
        text2.getStyleClass().add("hourText");

        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(text1, text2);
        nameBox.getStyleClass().add("nameBox");

        Button home = new Button();
        home.setGraphic(nameBox);
        home.setOnAction((ActionEvent event) ->
                StageLoader.loadHome()
        );
        home.getStyleClass().add("homeButton");

        return home;
    }

    protected Button getSettingsButton(){
        Button settings = new Button("Setari");
        settings.setOnAction((ActionEvent event) ->
            StageLoader.loadHome()
        );

//        HBox rightSection = new HBox();
//        rightSection.getChildren().add(settings);
//        HBox.setHgrow( rightSection, Priority.ALWAYS );
//        rightSection.setAlignment( Pos.CENTER_RIGHT );
//
//        final int spacing = 8;
//        toolBar.setPadding( new Insets( 5, spacing, 5, spacing ) );
//        rightSection.setSpacing( spacing );
//
//        toolBar.getItems().addAll(leftSection,rightSection);
//
//        return toolBar;
        return settings;
    }

}
