package view.panes.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Metadata;
import view.panes.CreatePane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anca on 4/24/2017.
 */
public class InsertGeneralSettings extends InsertPane {

    public InsertGeneralSettings(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    private ArrayList<Pair<String, Boolean>> getTextFieldValues(){
        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Adaugă numele orarului", Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă autorul", Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă descrierea", Boolean.TRUE));

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
        }

        return textFieldValues;
    }

    public VBox addRightPane(){
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

        FlowPane fp = getFlowPane(vbList);

        Button saveButton = new Button("SALVEAZĂ");
        saveButton.getStyleClass().add("rightSaveButton");
        saveButton.setOnAction((ActionEvent event) ->{
            List<HBox> labels = new ArrayList<>(Arrays.asList(idLabel, nameLabel,  contributorLabel));
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
