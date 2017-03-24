package view.panes;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import view.StageLoader;

/**
 * Created by Anca on 3/16/2017.
 */
public abstract class MainPane extends BorderPane {

    protected ToolBar addToolbar(boolean withHomeButton){
        ToolBar toolBar = new ToolBar();
        HBox rightSection = new HBox();

        if(withHomeButton){
            Button home = new Button("Acasa");
            home.setOnAction((ActionEvent event) ->
                    StageLoader.loadHome()
            );
            rightSection.getChildren().add(home);
        }

        Button settings = new Button("Setari");
        settings.setOnAction((ActionEvent event) ->
            StageLoader.loadHome()
        );

        rightSection.getChildren().add(settings);
        HBox.setHgrow( rightSection, Priority.ALWAYS );
        rightSection.setAlignment( Pos.CENTER_RIGHT );

        final int spacing = 8;
        toolBar.setPadding( new Insets( 5, spacing, 5, spacing ) );
        rightSection.setSpacing( spacing );

        toolBar.getItems().add(rightSection);

        return toolBar;
    }

}
