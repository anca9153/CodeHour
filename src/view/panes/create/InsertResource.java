package view.panes.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import model.resource.Resource;
import model.resource.Resources;
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

    private TextField idTextField;
    private TextField nameTextField;

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

        if(CreatePane.timetable.getResources().getResources() == null){
            CreatePane.timetable.getResources().setResources(new ArrayList<>());
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
        idTextField = makeTextField(textFieldValues.get(0));
        vbList.add(new VBox(idLabel, idTextField));

        boolean req = false;
        if(resourceType.equals("teacher")){
            req = true;
        }
        HBox nameLabel = makeLabel("NUME", req);
        nameTextField = makeTextField(textFieldValues.get(1));
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
                currentResource.setResourceType(resourceType);

                //Checking if the current resource is an existing one and replacing it in the timetable
                int index = 0;
                boolean exists = false;
                for(Resource r:CreatePane.timetable.getResources().getResources()){
                    if(r.getId().equals(currentResource.getId())){
                        CreatePane.timetable.getResources().getResources().remove(r);
                        CreatePane.timetable.getResources().getResources().add(index, currentResource);
                        exists = true;
                        break;
                    }
                    index++;
                }

                if(!exists){
                    CreatePane.timetable.getResources().getResources().add(currentResource);
                    updateTableData();
                }

                if(saveIntoFile()){ //The save button was pressed, the file to save into was chosen
                    currentResource = new Resource();
                    table.getSelectionModel().clearSelection();
                    clearAllFields();
                }
                else{
                    CreatePane.timetable.getResources().getResources().remove(currentResource);
                }

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
        if(CreatePane.timetable.getResources()!= null && CreatePane.timetable.getResources().getResources() != null && CreatePane.timetable.getResources().getResources().size()>0) {
            for(Resource r: CreatePane.timetable.getResources().getResources()){
                if(r.getResourceType().equals(resourceType)){
                    addTable(finalVBox);
                    break;
                }
            }
        }

        return finalVBox;
    }

    private void clearAllFields(){
        idTextField.clear();
        nameTextField.clear();
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

        TableColumn deleteColumn = new TableColumn();
        Callback<TableColumn<Resource, String>, TableCell<Resource, String>> cellFactory =
                new Callback<TableColumn<Resource, String>, TableCell<Resource, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<Resource, String> param )
                    {
                        final TableCell<Resource, String> cell = new TableCell<Resource, String>()
                        {
                            final Button remove = new Button();

                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                if (empty)
                                {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else
                                {
                                    remove.setOnAction((ActionEvent event) ->
                                    {
                                        Resource resource = getTableView().getItems().get(getIndex());
                                        CreatePane.timetable.getResources().getResources().remove(resource);

                                        updateTableData();
                                        saveIntoFile();
                                    } );

                                    ImageView imageView2 = new ImageView(new Image("\\icons\\deleteIcon.png"));
                                    imageView2.setFitHeight(10);
                                    imageView2.setFitWidth(10);
                                    imageView2.setPreserveRatio(true);

                                    remove.setGraphic(imageView2);
                                    remove.getStyleClass().add("removeEventResource");
                                    remove.setMinSize(30, 40);

                                    setGraphic(remove);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        deleteColumn.setCellFactory(cellFactory);
        deleteColumn.setMaxWidth(30);
        deleteColumn.setPrefWidth(30);
        deleteColumn.setMinWidth(30);
        deleteColumn.getStyleClass().add("lastColumn");

        table.getColumns().addAll(idColumn, nameColumn, deleteColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setEditable(true);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                Resource r = (Resource)newValue;
                currentResource = r;
                idTextField.setText(r.getId());
                nameTextField.setText(r.getName());

                if(r.getName()==null) {
                    nameTextField.setText("Adaugă nume " + resourceSingularString);
                }

                if(r.getId() == null){
                    idTextField.setText("Adaugă id " + resourceSingularString);
                }
            }
        });

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

        if(table == null){
            table = new TableView();
        }

        if(!data.isEmpty() && table != null) {
            table.setItems(data);
            table.setPrefHeight((table.getFixedCellSize() + 0.8) * (table.getItems().size() + 1));
        }
    }
}
