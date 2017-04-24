package view.panes.create;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import model.Timetable;
import model.time.Time;
import model.time.Times;
import utilities.DataLoader;
import view.panes.CreatePane;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anca on 4/24/2017.
 */
public class InsertTime extends InsertPane{
    private Timetable timetable;
    private Stage primaryStage;
    private HBox saveButtonHB;
    private Time currentTime = new Time();

    public InsertTime(Timetable timetable, Stage primaryStage){
        this.timetable = timetable;
        this.primaryStage = primaryStage;

        Times times = new Times();
        times.setTimes(new ArrayList<>());
        timetable.setTimes(times);
    }

    public VBox addRightPane(){
        Label title = new Label("Adaugă intervale orare");
        title.getStyleClass().add("resourceText");

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Adaugă ziua intervalului", Boolean.TRUE));
        textFieldValues.add(new Pair("8:00 - 9:00", Boolean.TRUE));

        if(currentTime.getDay()!=null){
            textFieldValues.remove(0);
            textFieldValues.add(0, new Pair(currentTime.getDay(), Boolean.FALSE));
        }

        if(currentTime.getHourInterval()!=null){
            textFieldValues.remove(1);
            textFieldValues.add(1, new Pair(currentTime.getHourInterval(), Boolean.FALSE));
        }

        ObservableList<String> days = FXCollections.observableArrayList("monday", "tuesday", "wednesday","thursday", "friday", "saturday", "sunday");

        HBox dayLabel = makeLabel("ZIUA", true);
        ComboBox<String> dayCB = new ComboBox<String>(days);
        vbList.add(new VBox(dayLabel, dayCB));

        HBox hourIntervalLabel = makeLabel("INTERVALUL ORAR", true);
        TextField hourIntervalTextField = makeTextField(textFieldValues.get(1));
        vbList.add(new VBox(hourIntervalLabel, hourIntervalTextField));

        FlowPane fp = new FlowPane();

        for(VBox vb : vbList){
            vb.getStyleClass().add("fieldRight");
            fp.getChildren().add(vb);
        }

        fp.setHgap(40);
        fp.setVgap(20);

        fp.getStyleClass().add("rightFlowPane");

        Button saveButton = new Button("SALVEAZĂ");
        saveButton.getStyleClass().add("rightSaveButton");
        saveButton.setOnAction((ActionEvent event) ->{
            //Clearing all errors
            List<HBox> labels = new ArrayList<>(Arrays.asList(dayLabel, hourIntervalLabel));
            List<TextField> textFields =  new ArrayList<>(Arrays.asList(hourIntervalTextField));
            clearErrors(labels, textFields);
            for(String s: dayCB.getStyleClass()){
                if(s.equals("addRedMargin")){
                    dayCB.getStyleClass().remove(s);
                    break;
                }
            }

            boolean empty = false;

            if(dayCB.getValue() == null){
                showErrorMessage(dayLabel, "Ziua este necesară.", dayCB);
                empty = true;
            }
            else {
                currentTime.setDay(dayCB.getValue());
            }

            if(hourIntervalTextField.getText().isEmpty()){
                showErrorMessage(hourIntervalLabel, "Intervalul este necesar.", hourIntervalTextField);
                empty = true;
            }
            else{
                currentTime.setHourInterval(hourIntervalTextField.getText());
            }

            if(!empty) {
                currentTime.setId(timetable.getTimes().getTimes().size()+1);

                int dayNo = 1;

                for(Time t : timetable.getTimes().getTimes()){
                    if(t.getDay().equals(currentTime.getDay())){
                        dayNo++;
                    }
                }

                for(String s: days){
                    if(currentTime.getDay().equals(s)){
                        currentTime.setName(dayNo + "_" + s.substring(0,3));
                        break;
                    }
                }

                timetable.getTimes().getTimes().add(currentTime);
                currentTime = new Time();

                if (CreatePane.savingFile == null) {
                    FileChooser fileChooser = new FileChooser();

                    //Set extension filter
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    CreatePane.savingFile = fileChooser.showSaveDialog(primaryStage);
                }

                if (CreatePane.savingFile != null) {
                    DataLoader.loadSolvedTimetableToXMLWithPath(timetable, CreatePane.savingFile);

                    Label confSave = new Label("Datele au fost salvate. ");
                    confSave.getStyleClass().addAll("fieldRightLabel","explanatory1");
                    confSave.setId("confSave");

                    for(Node n: saveButtonHB.getChildren()){
                        if(n.getId()!=null && n.getId().equals("confSave")){
                            saveButtonHB.getChildren().remove(n);
                            break;
                        }
                    }

                    if(saveButtonHB.getChildren().size() < 2) {
                        saveButtonHB.getChildren().add(0, confSave);
                        PauseTransition visiblePause = new PauseTransition(
                                Duration.seconds(5)
                        );
                        visiblePause.setOnFinished(
                                e -> confSave.setVisible(false)
                        );
                        visiblePause.play();
                    }
                }
            }

        } );

        saveButton.setAlignment(Pos.CENTER_RIGHT);

        Label expl1 = new Label("Câmpurile marcate cu ");
        expl1.getStyleClass().addAll("fieldRightLabel","explanatory1");
        Label star = new Label("*");
        star.getStyleClass().addAll("fieldRightLabel","redStar");
        Label expl2 = new Label(" sunt necesare.");
        expl2.getStyleClass().addAll("fieldRightLabel","explanatory1");
        HBox explanatory = new HBox(expl1, star, expl2);
        explanatory.getStyleClass().addAll("fieldRightLabel", "explanatory");

        saveButtonHB = new HBox();
        saveButtonHB.getChildren().add(saveButton);
        saveButtonHB.getStyleClass().add("rightSaveHBox");
        saveButtonHB.setAlignment(Pos.CENTER_RIGHT);

        HBox nameBox = new HBox();

        //Adding the table with the existing times
        if(timetable.getTimes().getTimes().size()>0) {
            Label existingLabel = new Label("Intervale orare");
            existingLabel.getStyleClass().add("resourceText");
            Label listLabel = new Label("Listă");
            listLabel.getStyleClass().add("resourceTypeText");

            nameBox.getChildren().addAll(existingLabel, listLabel);
            nameBox.getStyleClass().add("titleNameBox");
            nameBox.setAlignment(Pos.BOTTOM_LEFT);
        }

        VBox vb = new VBox(title, fp, explanatory, saveButtonHB, nameBox);
        vb.getStyleClass().add("rightVBox");

        return vb;
    }

}
