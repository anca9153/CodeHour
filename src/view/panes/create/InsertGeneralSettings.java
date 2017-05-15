package view.panes.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Metadata;
import view.panes.CreatePane;

import java.util.*;

/**
 * Created by Anca on 4/24/2017.
 */
public class InsertGeneralSettings extends InsertPane {
    private Map<String, Integer> numberMap;

    public InsertGeneralSettings(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    private ArrayList<Pair<String, Boolean>> getTextFieldValues(){
        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Adaugă numele orarului", Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă autorul", Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă descrierea", Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă durata unui curs", Boolean.TRUE));

        if(CreatePane.timetable.getMetadata() != null){
            if(CreatePane.timetable.getMetadata().getName()!=null){
                textFieldValues.remove(0);
                textFieldValues.add(0, new Pair(CreatePane.timetable.getMetadata().getName(), Boolean.FALSE));
            }

            if(CreatePane.timetable.getMetadata().getContributor()!=null){
                textFieldValues.remove(1);
                textFieldValues.add(1, new Pair(CreatePane.timetable.getMetadata().getContributor(), Boolean.FALSE));
            }

            if(CreatePane.timetable.getMetadata().getDescription()!=null){
                textFieldValues.remove(2);
                textFieldValues.add(2, new Pair(CreatePane.timetable.getMetadata().getDescription(), Boolean.FALSE));
            }

            if(CreatePane.timetable.getBasicTimeUnitInMinutes() > 0){
                textFieldValues.remove(3);
                int x = CreatePane.timetable.getBasicTimeUnitInMinutes();
                for(String s: numberMap.keySet()){
                    if(numberMap.get(s) == x){
                        textFieldValues.add(3, new Pair(s, Boolean.FALSE));
                        break;
                    }
                }
            }
        }

        return textFieldValues;
    }

    public VBox addRightPane(){
        numberMap = new HashMap<>();
        ObservableList<String> numberOptions = FXCollections.observableArrayList();
        for(int i=1;i<1440;i++){
            String s;
            if(i<60){
                s = new String(i+" min");
            }
            else{
                s = new String(i/60+" h "+i%60+" min");
            }

            numberOptions.add(s);
            numberMap.put(s, i);
        }

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        HBox idLabel = makeLabel("ID", true);
        TextField idTextField = makeTextField(CreatePane.timetable.getId() == null ? new Pair("Adaugă id-ul orarului", Boolean.TRUE) : new Pair(CreatePane.timetable.getId(), Boolean.FALSE));
        vbList.add(new VBox(idLabel, idTextField));

        ArrayList<Pair<String, Boolean>> textFieldValues = getTextFieldValues();

        HBox nameLabel = makeLabel("NUME", true);
        TextField nameTextField = makeTextField(textFieldValues.get(0));
        vbList.add(new VBox(nameLabel, nameTextField));

        HBox contributorLabel = makeLabel("AUTOR", true);
        TextField contributorTextField = makeTextField(textFieldValues.get(1));
        vbList.add(new VBox(contributorLabel, contributorTextField));

        HBox descriptionLabel = makeLabel("DESCRIERE", false);
        TextField descriptionTextField = makeTextField(textFieldValues.get(2));
        vbList.add(new VBox(descriptionLabel, descriptionTextField));

        HBox timeIntervalLabel = makeLabel("DURATA UNEI UNITĂȚI DE TIMP", true);
        ComboBox<String> timeIntervalCB = new ComboBox<>(numberOptions);
        timeIntervalCB.getStyleClass().add("specialComboBox");
        timeIntervalCB.setPromptText(textFieldValues.get(3).getKey());
        vbList.add(new VBox(timeIntervalLabel, timeIntervalCB));

        FlowPane fp = getFlowPane(vbList);

        Button saveButton = new Button("SALVEAZĂ");
        saveButton.getStyleClass().add("rightSaveButton");
        saveButton.setOnAction((ActionEvent event) ->{
            List<HBox> labels = new ArrayList<>(Arrays.asList(idLabel, nameLabel,  contributorLabel, timeIntervalLabel));
            List<TextField> textFields =  new ArrayList<>(Arrays.asList(idTextField, nameTextField, contributorTextField));
            clearErrors(labels, textFields);

            boolean empty = false;
            if(idTextField.getText().isEmpty()){
                //Show message that the id field id empty
                showErrorMessage(idLabel, "Id-ul este necesar.", idTextField);
                empty = true;
            }
            else{
                CreatePane.timetable.setId(idTextField.getText());
            }

            Metadata meta = CreatePane.timetable.getMetadata();
            if(meta == null){
                meta = new Metadata();
            }

            if(nameTextField.getText().isEmpty()){
                showErrorMessage(nameLabel, "Numele este necesar.", nameTextField);
                empty = true;
            }
            else{
                meta.setName(nameTextField.getText());
            }

            if(contributorTextField.getText().isEmpty()){
                showErrorMessage(contributorLabel, "Autorul este necesar.", contributorTextField);
                empty = true;
            }
            else{
                meta.setContributor(contributorTextField.getText());
            }

            if(timeIntervalCB.getValue().isEmpty()){
                showErrorMessage(timeIntervalLabel, "Durata este necesară", timeIntervalCB);
                empty = true;
            }
            else{
                CreatePane.timetable.setBasicTimeUnitInMinutes(numberMap.get(timeIntervalCB.getValue()));
            }

            if(!descriptionTextField.getText().isEmpty()){
                meta.setDescription(descriptionTextField.getText());
            }

            CreatePane.timetable.setMetadata(meta);

            if(!empty) {
                saveIntoFile();
            }

        } );

        addSaveButtonIntoHBox(saveButton);

        VBox vb = new VBox(getTitleLabel("Adaugă metadate"), fp, createExplanatory(), saveButtonHB);
        vb.getStyleClass().add("rightVBox");

        return vb;
    }

}
