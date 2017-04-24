package view.panes.create;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import model.Metadata;
import model.Timetable;
import utilities.DataLoader;
import view.panes.CreatePane;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anca on 4/24/2017.
 */
public class InsertGeneralSettings extends InsertPane {
    private Timetable timetable;
    private Stage primaryStage;
    private HBox saveButtonHB;

    public InsertGeneralSettings(Timetable timetable, Stage primaryStage){
        this.timetable = timetable;
        this.primaryStage = primaryStage;
    }

    public VBox addRightPane(){
        Label title = new Label("Adaugă metadate");
        title.getStyleClass().add("resourceText");

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        HBox idLabel = makeLabel("ID", true);
        TextField idTextField = makeTextField(timetable.getId() == null ? new Pair("Adaugă id-ul orarului", Boolean.TRUE) : new Pair(timetable.getId(), Boolean.FALSE));
        vbList.add(new VBox(idLabel, idTextField));

        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Adaugă numele orarului", Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă autorul", Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă descrierea", Boolean.TRUE));
        if(timetable.getMetadata() != null){
            if(timetable.getMetadata().getName()!=null){
                textFieldValues.remove(0);
                textFieldValues.add(0, new Pair(timetable.getMetadata().getName(), Boolean.FALSE));
            }

            if(timetable.getMetadata().getContributor()!=null){
                textFieldValues.remove(1);
                textFieldValues.add(1, new Pair(timetable.getMetadata().getContributor(), Boolean.FALSE));
            }

            if(timetable.getMetadata().getDescription()!=null){
                textFieldValues.remove(2);
                textFieldValues.add(2, new Pair(timetable.getMetadata().getDescription(), Boolean.FALSE));
            }
        }

        HBox nameLabel = makeLabel("NUME", true);
        TextField nameTextField = makeTextField(textFieldValues.get(0));
        vbList.add(new VBox(nameLabel, nameTextField));

        HBox contributorLabel = makeLabel("AUTOR", true);
        TextField contributorTextField = makeTextField(textFieldValues.get(1));
        vbList.add(new VBox(contributorLabel, contributorTextField));

        HBox descriptionLabel = makeLabel("DESCRIERE", false);
        TextField descriptionTextField = makeTextField(textFieldValues.get(2));
        vbList.add(new VBox(descriptionLabel, descriptionTextField));

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
                timetable.setId(idTextField.getText());
            }

            Metadata meta = timetable.getMetadata();

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

            timetable.setMetadata(meta);

            if(!empty) {
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

        VBox vb = new VBox(title, fp, explanatory, saveButtonHB);
        vb.getStyleClass().add("rightVBox");

        return vb;
    }

}
