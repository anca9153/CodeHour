package view.panes;

import compute.DataLoader;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.StageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anca on 3/16/2017.
 */
public class HomePane extends MainPane {
    private StageLoader loader;

    public HomePane(){
        addToolbar(false);

        Pane left = getAvailableTimetables();
        Pane right = getCreateTimetable();

        HBox box = new HBox();

        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        box.getChildren().addAll(left, right);
        box.setMinWidth(400);
        box.setMinHeight(350);

        this.setCenter(box);
    }

    private Pane getAvailableTimetables(){
        File loadFolder = new File(DataLoader.getLoadPath());

        List<File> timetablesToDisplay = new ArrayList<>();
        ObservableList<String> buttonList = FXCollections.observableArrayList();

        for (final File fileEntry : loadFolder.listFiles()) {
            if (!fileEntry.isDirectory() && fileEntry.getName().startsWith("timetable")){
                timetablesToDisplay.add(fileEntry);
                buttonList.add(fileEntry.getName());
            }
        }

        ListView<String> listView = new ListView<>();
        listView.setItems(buttonList);
        listView.setPrefSize(150, 200);
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val,
                 String new_val) -> {
                    StageLoader.loadDisplay(timetablesToDisplay.get(buttonList.indexOf(new_val)));

                });

        VBox timetableList = new VBox();
        timetableList.getChildren().add(listView);

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.add(new Text("Orare disponibile"), 0, 0);
        pane.add(timetableList, 0, 1);

        pane.setAlignment(Pos.CENTER);

        return pane;
    }

    private Pane getCreateTimetable(){
        Button create = new Button("Creeaza orar");
        create.setOnAction((ActionEvent event) ->
            StageLoader.loadCreate()
        );

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.add(new Text("Orar nou"), 0, 0);
        pane.add(create, 0, 1);

        pane.setAlignment(Pos.CENTER);
//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return pane;
    }

    public void setLoader(StageLoader loader) {
        this.loader = loader;
    }
}
