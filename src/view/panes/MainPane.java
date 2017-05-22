package view.panes;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import view.StageLoader;

/**
 * Created by Anca on 3/16/2017.
 */
public abstract class MainPane extends BorderPane {

    protected Button createHomeButton() {
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

    protected HBox createRightToolBox(){
        Label language = new Label("Limbă");
        language.getStyleClass().add("timetableLabel");

        ComboBox languages = new ComboBox(FXCollections.observableArrayList("RO", "ENG"));
        languages.getStyleClass().add("languagesBox");
        languages.getSelectionModel().select("RO");

        HBox settingsBox = new HBox(createSettingsButton());
        settingsBox.getStyleClass().add("settingsBox");

        //Adding the right part of the toolBox
        HBox rightBox = new HBox(language, languages, settingsBox);
        rightBox.getStyleClass().add("rightBoxToolbar");
        HBox.setHgrow(rightBox, Priority.ALWAYS );
        rightBox.setAlignment( Pos.CENTER_RIGHT );

        return rightBox;
    }

    protected MenuButton createSettingsButton(){
        ImageView imageView = new ImageView(new Image("\\icons\\settingsIcon.png"));
        imageView.setFitHeight(10);
        imageView.setFitWidth(10);
        imageView.setPreserveRatio(true);

        MenuItem menuItem1 = new MenuItem("Folderul principal");

        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Option 1 selected");
            }
        });

        MenuButton settings = new MenuButton("Setări", imageView, menuItem1);

//        Button settings = new Button("Setări", imageView);
//        settings.getStyleClass().add("settingsButton");
        settings.setContentDisplay(ContentDisplay.LEFT);
        settings.setOnAction((ActionEvent event) ->
                StageLoader.loadHome()
        );

        return settings;
    }

}
