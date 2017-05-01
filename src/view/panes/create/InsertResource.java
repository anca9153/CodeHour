package view.panes.create;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import model.resource.Resource;
import model.resource.Resources;
import utilities.DataLoader;
import view.panes.CreatePane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anca on 5/1/2017.
 */
public class InsertResource extends InsertPaneWithTable {
    private Resource currentResource = new Resource();
    private VBox finalVBox;

    //Variables that change based on the type of the resource
    private String resourceType;
    private String resourceSingularString;
    private String resourcePluralString;

    public InsertResource(Stage primaryStage, String resourceType, String resourceSingularString, String resourcePluralString){
        this.primaryStage = primaryStage;
        this.resourceType = resourceType;
        this.resourceSingularString = resourceSingularString;
        this.resourcePluralString = resourcePluralString;

        if(CreatePane.timetable.getResources() == null) {
            Resources resources = new Resources();
            resources.setResources(new ArrayList<>());
            CreatePane.timetable.setResources(resources);
        }
    }

    private ArrayList<Pair<String, Boolean>> getTextFieldValues(){
        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Adaugă id " + resourceSingularString, Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă nume " + resourceSingularString, Boolean.TRUE));

        if(currentResource.getId()!=null){
            textFieldValues.remove(0);
            textFieldValues.add(0, new Pair(currentResource.getId(), Boolean.FALSE));
        }

        if(currentResource.getName()!=null){
            textFieldValues.remove(1);
            textFieldValues.add(1, new Pair(currentResource.getName(), Boolean.FALSE));
        }

        return textFieldValues;
    }

    public VBox addRightPane(){
        currentResource.setResourceType(resourceType);

        ArrayList<Pair<String, Boolean>> textFieldValues = getTextFieldValues();

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        HBox idLabel = makeLabel("ID", true);
        TextField idTextField = makeTextField(textFieldValues.get(0));
        vbList.add(new VBox(idLabel, idTextField));

        boolean req = false;
        if(resourceType.equals("teacher")){
            req = true;
        }
        HBox nameLabel = makeLabel("NUME", req);
        TextField nameTextField = makeTextField(textFieldValues.get(1));
        vbList.add(new VBox(nameLabel, nameTextField));

        FlowPane fp = getFlowPane(vbList);

        Button saveButton = new Button("SALVEAZĂ");
        saveButton.getStyleClass().add("rightSaveButton");
        saveButton.setOnAction((ActionEvent event) ->{
            //Clearing all errors
            List<HBox> labels = new ArrayList<>(Arrays.asList(idLabel, nameLabel));
            List<TextField> textFields =  new ArrayList<>(Arrays.asList(idTextField, nameTextField));
            clearErrors(labels, textFields);

            boolean empty = false;

            if(idTextField.getText().isEmpty()){
                showErrorMessage(idLabel, "Id-ul este necesar.", idTextField);
                empty = true;
            }
            else{
                currentResource.setId(idTextField.getText());
            }

            if(!empty) {
                if(!nameTextField.getText().isEmpty()) {
                    currentResource.setName(nameTextField.getText());
                }

                CreatePane.timetable.getResources().getResources().add(currentResource);
                currentResource = new Resource();

                saveIntoFile();
            }

            //Adding the table with the existing resources
            if(CreatePane.timetable.getResources().getResources().size()>0) {
                dealWithTable(finalVBox);
            }

        } );

        addSaveButtonIntoHBox(saveButton);

        finalVBox = new VBox(getTitleLabel("Adaugă " + resourceSingularString), fp, createExplanatory(), saveButtonHB);
        finalVBox.getStyleClass().add("rightVBox");

        //Adding the table with the existing resources
        if(CreatePane.timetable.getResources()!= null && CreatePane.timetable.getResources().getResources().size()>0) {
            for(Resource r: CreatePane.timetable.getResources().getResources()){
                if(r.getResourceType().equals(resourceType)){
                    addTable(finalVBox);
                    break;
                }
            }
        }

        return finalVBox;
    }

    protected VBox createTable(){
        Label existingLabel = new Label(resourcePluralString.substring(0, 1).toUpperCase() + resourcePluralString.substring(1).toLowerCase());
        existingLabel.getStyleClass().add("resourceText");
        Label listLabel = new Label("Listă");
        listLabel.getStyleClass().add("resourceTypeText");

        HBox nameBox = new HBox();
        nameBox.getStyleClass().add("tableTitle");

        nameBox.getChildren().addAll(existingLabel, listLabel);
        nameBox.getStyleClass().add("titleNameBox");
        nameBox.setAlignment(Pos.BOTTOM_LEFT);

        //Creating the table for the already saved times
        table = new TableView();
        TableColumn idColumn = new TableColumn("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<Resource, Integer>("id"));
        idColumn.setMaxWidth(100);
        idColumn.setPrefWidth(100);
        idColumn.setMinWidth(100);
        idColumn.getStyleClass().add("firstColumn");

        TableColumn nameColumn = new TableColumn("Nume");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Resource, String>("name"));

        table.getColumns().addAll(idColumn, nameColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        updateTableData();

        table.setFixedCellSize(35);
        table.setPrefHeight((table.getFixedCellSize()+0.8) * (table.getItems().size()+1));

        VBox vb = new VBox(nameBox, table);
        vb.setId("table");

        return vb;
    }

    protected void updateTableData(){
        ObservableList<Resource> data = FXCollections.observableArrayList();

        for(Resource r: CreatePane.timetable.getResources().getResources()){
            if(r.getResourceType().equals(resourceType)){
                data.add(r);
            }
        }

        table.setItems(data);
    }
}
