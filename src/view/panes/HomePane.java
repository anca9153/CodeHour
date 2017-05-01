package view.panes;

import javafx.scene.control.*;
import model.Timetable;
import utilities.DataLoader;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import utilities.PropertiesLoader;
import view.StageLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anca on 3/16/2017.
 */
public class HomePane extends MainPane {
    private StageLoader loader;

    public HomePane(){
        Pane left = getAvailableTimetables();
        Pane right = getCreateTimetable();

        GridPane gridPane = new GridPane();

        GridPane.setHgrow(left, Priority.ALWAYS);
        GridPane.setHgrow(right, Priority.ALWAYS);

        gridPane.add(left, 0,0);
        gridPane.add(right, 1,0);
        gridPane.setMinWidth(400);
        gridPane.setMinHeight(350);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(100.0/2);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(100.0/2);
        gridPane.getColumnConstraints().addAll(c1, c2);

        RowConstraints r1 = new RowConstraints();
        r1.setPercentHeight(100.0);
        gridPane.getRowConstraints().add(r1);

        this.setCenter(gridPane);
    }

    private Pane getAvailableTimetables(){
        File loadFolder = new File(PropertiesLoader.loadXMLLocationFolder());

        List<File> timetablesToDisplay = new ArrayList<>();

        //Reading the XMLs from the given directory
        for (final File fileEntry : loadFolder.listFiles()) {
            if (!fileEntry.isDirectory()){
                timetablesToDisplay.add(fileEntry);
            }
        }

        ListView<String> listView = new ListView<>();
        listView.setPrefSize(150, 195);

        Map<String, Timetable> idTimetableWithSolutionMap = new HashMap<>();
        Map<String, Timetable> idTimetableNoSolutionMap = new HashMap<>();
        Map<String, File> idFileMap = new HashMap<>();

        Timetable t;
        for(File file: timetablesToDisplay) {
            if (PropertiesLoader.loadXMLLocationFolder().equals(new String(file.getParent() + "\\"))) {
                t = DataLoader.loadDataFromXML(file.getName());
            } else {
                t = DataLoader.loadDataFromXMLWithPath(file);
            }
            if(t.getSolutions() == null){
                idTimetableNoSolutionMap.put(t.getId(), t);
            }
            else{
                idTimetableWithSolutionMap.put(t.getId(), t);
            }

            idFileMap.put(t.getId(), file);

            listView.getItems().add(t.getId());
        }

        listView.setCellFactory(i -> new ListCell<String>() {
            @Override
            protected void updateItem (String item,boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    if(idTimetableNoSolutionMap.get(item) != null){
                        Label l = new Label("NEFINALIZAT");
                        setGraphic(l);
                    }
                }
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener(
        (ObservableValue<? extends String> ov, String old_val,
         String new_val) -> {
            File f = idFileMap.get(new_val);
            Timetable tt = idTimetableNoSolutionMap.get(new_val);
            if(tt == null){
                tt = idTimetableWithSolutionMap.get(new_val);
                StageLoader.loadDisplay(idTimetableWithSolutionMap, f, tt);
            }
            else{
                StageLoader.loadDisplay(idTimetableNoSolutionMap, f, tt);
            }
        });

        listView.setMaxSize(300, 200);
        listView.setFixedCellSize(42);

        VBox vBox = new VBox();
        Label label = new Label("Orare disponibile");
        label.setPadding(new Insets(0, 30, 30, 30));

        vBox.getChildren().add(label);
        vBox.getChildren().add(listView);

        //Styling
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10, 20, 20, 20));
        vBox.getStyleClass().add("leftBox");

        return vBox;
    }

    private Pane getCreateTimetable(){
        Button create = new Button("ADAUGĂ ORAR");
        create.setOnAction((ActionEvent event) ->
            StageLoader.loadCreate()
        );
        create.getStyleClass().add("createButton");

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));

        //Adding the name and the buttons
        Text text1 = new Text("Code");
        text1.getStyleClass().add("codeText");
        Text text2 = new Text("Hour");
        text2.getStyleClass().add("hourText");

        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(text1, text2);
        nameBox.getStyleClass().add("nameBox");

        Button settings = new Button("SETĂRI");
        settings.getStyleClass().add("settingsButton");

        pane.add(nameBox, 0, 0);
        pane.add(create, 0, 1);
        pane.add(settings, 0, 2);

        for(int i=0;i<2; i++){
            pane.add(new Text(), 0, i+3);
        }

        pane.setAlignment(Pos.CENTER);
        pane.getStyleClass().add("rightPane");
        return pane;
    }

    public void setLoader(StageLoader loader) {
        this.loader = loader;
    }
}
