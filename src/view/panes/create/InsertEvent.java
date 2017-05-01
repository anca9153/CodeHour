package view.panes.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;
import model.time.Time;
import view.panes.CreatePane;

import java.util.*;

/**
 * Created by Anca on 5/1/2017.
 */
public class InsertEvent extends InsertPaneWithTable {
    private Event currentEvent = new Event();
    private VBox finalVBox;

    public InsertEvent(Stage primaryStage){
        this.primaryStage = primaryStage;

        if(CreatePane.timetable.getEvents() == null) {
            Events events = new Events();
            events.setEvents(new ArrayList<>());
            CreatePane.timetable.setEvents(events);
        }

        if(CreatePane.timetable.getEvents().getEvents() == null) {
            CreatePane.timetable.getEvents().setEvents(new ArrayList<>());
        }
    }

    private ArrayList<Pair<String, Boolean>> getTextFieldValues(){
        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Alege durata evenimentului", Boolean.TRUE));
        textFieldValues.add(new Pair("Alege intervalul orar", Boolean.TRUE));
        textFieldValues.add(new Pair("Alege resursa", Boolean.TRUE));
        textFieldValues.add(new Pair("Adaugă descrierea", Boolean.TRUE));

        if(currentEvent.getDuration()!=0){
            textFieldValues.remove(0);
            textFieldValues.add(0, new Pair(currentEvent.getDuration(), Boolean.FALSE));
        }

        if(currentEvent.getTime()!=null){
            textFieldValues.remove(1);
            textFieldValues.add(1, new Pair(currentEvent.getTime().getId(), Boolean.FALSE));
        }

        if(currentEvent.getDescription()!=null){
            textFieldValues.remove(3);
            textFieldValues.add(3, new Pair(currentEvent.getDescription(), Boolean.FALSE));
        }

        return textFieldValues;
    }

    public VBox addRightPane(){
        ArrayList<Pair<String, Boolean>> textFieldValues = getTextFieldValues();

        ObservableList<Integer> durations = FXCollections.observableArrayList();
        for(int i = 1; i<24; i++){
            durations.add(i);
        }

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        HBox descriptionLabel = makeLabel("DESCRIERE", true);
        TextField descriptionTextField = makeTextField(textFieldValues.get(3));
        vbList.add(new VBox(descriptionLabel, descriptionTextField));

        HBox durationLabel = makeLabel("DURATA", true);
        ComboBox<Integer> durationCB = new ComboBox<>(durations);
        durationCB.getStyleClass().add("specialComboBox");
        vbList.add(new VBox(durationLabel, durationCB));

               ObservableList<String> timeIds = FXCollections.observableArrayList();
        if(CreatePane.timetable.getTimes()!=null && CreatePane.timetable.getTimes().getTimes() != null) {
            for (Time t : CreatePane.timetable.getTimes().getTimes()) {
                timeIds.add(t.getName());
            }
        }

        HBox timeLabel = makeLabel("INTERVALUL ORAR", false);
        ComboBox<String> timeCB = new ComboBox<>(timeIds);
        timeCB.getStyleClass().add("specialComboBox");
        vbList.add(new VBox(timeLabel, timeCB));

        FlowPane fp = getFlowPane(vbList);

        Map<String, Resource> idResourceMap = new HashMap<>();

        //Resources that are added in the timetable object
        ObservableList<String> resourceIds = FXCollections.observableArrayList();
        if(CreatePane.timetable.getResources()!=null) {
            for (Resource r : CreatePane.timetable.getResources().getResources()) {
                if(r.getResourceType().equals("teacher")){
                    resourceIds.add(r.getName());
                    idResourceMap.put(r.getName(), r);
                }
                else {
                    resourceIds.add(r.getId());
                    idResourceMap.put(r.getId(), r);
                }
            }
        }

        //Resources linked to the current event
        //We also remove the resources that are already linked to the current event from the possible choices of resources
        ObservableList<HBox> eventResourceIdLabels = FXCollections.observableArrayList();
        if(currentEvent.getResources()!=null) {
            for (Resource r : currentEvent.getResources().getResources()) {
                if(r.getResourceType().equals("teacher")){
                    eventResourceIdLabels.add(createEventResourceLabel(r.getName()));
                    resourceIds.remove(r.getName());
                }
                else {
                    eventResourceIdLabels.add(createEventResourceLabel(r.getId()));
                    resourceIds.remove(r.getId());
                }
            }
        }

        HBox resourceLabel = makeLabel("LISTA RESURSE", true);

        FlowPane resourcesFP = new FlowPane();
        resourcesFP.getChildren().addAll(eventResourceIdLabels);

        ComboBox<String> resourceCB = new ComboBox<>(resourceIds);
        resourceCB.getStyleClass().add("specialComboBox");

        ImageView imageView = new ImageView(new Image("\\icons\\addIcon.png"));
        imageView.setFitHeight(12);
        imageView.setFitWidth(12);
        imageView.setPreserveRatio(true);

        Button addButton = new Button();
        addButton.setGraphic(imageView);
        addButton.getStyleClass().add("addButton");

        addButton.setOnAction((ActionEvent event) ->{
            if(resourceCB.getValue()!=null){
                HBox hb = createEventResourceLabel(resourceCB.getValue());

                Button remove = new Button();
                remove.setGraphic(imageView);
                remove.getStyleClass().add("removeEventResource");
                imageView.setFitHeight(6);
                imageView.setFitWidth(6);
                remove.setMaxSize(10, 10);

                remove.setOnAction((ActionEvent e) ->{
                    resourcesFP.getChildren().remove(hb);

                    resourceIds.add(hb.getId());
                    resourceCB.setItems(null);
                    resourceCB.setItems(resourceIds);

                });

                hb.getChildren().add(remove);
                resourcesFP.getChildren().add(hb);

                resourceIds.remove(resourceCB.getValue());
                resourceCB.setItems(null);
                resourceCB.setItems(resourceIds);
            }
        });

        HBox addEventHBox = new HBox(resourceCB, addButton);

        VBox eventResourceVBox = new VBox(resourceLabel, resourcesFP, addEventHBox);
        eventResourceVBox.setPadding(new Insets(20, 0, 0 , 0));

        Button saveButton = new Button("SALVEAZĂ");
        saveButton.getStyleClass().add("rightSaveButton");
        saveButton.setOnAction((ActionEvent event) ->{
            //Clearing all errors
            List<HBox> labels = new ArrayList<>(Arrays.asList( durationLabel, timeLabel, resourceLabel, descriptionLabel));
            List<TextField> textFields =  new ArrayList<>(Arrays.asList(descriptionTextField));
            List<ComboBox> comboBoxes = new ArrayList<>(Arrays.asList(durationCB, timeCB, resourceCB));

            clearErrors(labels, textFields);

            for(ComboBox cb: comboBoxes) {
                for (String s : cb.getStyleClass()) {
                    if (s.equals("addRedMargin")) {
                        cb.getStyleClass().remove(s);
                        break;
                    }
                }
            }

            boolean empty = false;

            if(durationCB.getValue() == null){
                showErrorMessage(durationLabel, "Durata este necesară.", durationCB);
                empty = true;
            }
            else {
                currentEvent.setDuration(durationCB.getValue());
            }

            if(timeCB.getValue() !=null){
                for(Time t: CreatePane.timetable.getTimes().getTimes()){
                    if(t.getName().equals(timeCB.getValue())){
                        currentEvent.setTime(t);
                    }
                }
            }

            if(descriptionTextField.getText().isEmpty()){
                showErrorMessage(descriptionLabel, "Descrierea este necesară.", descriptionTextField);
                empty = true;
            }
            else {
                currentEvent.setDescription(descriptionTextField.getText());
            }

            if(resourcesFP.getChildren().size() < 1){
                showErrorMessage(resourceLabel, "Lista de resurse este necesară.", resourceCB);
                empty = true;
            }
            else{
                List<Resource> resourceList = new ArrayList<>();
                for(Node n: resourcesFP.getChildren()){
                    resourceList.add(idResourceMap.get(n.getId()));
                }
                currentEvent.setResources(new Resources(resourceList));
            }

            if(!empty) {
                currentEvent.setId("ev"+(int)(CreatePane.timetable.getEvents().getEvents().size()+1));

                CreatePane.timetable.getEvents().getEvents().add(currentEvent);
                currentEvent = new Event();

                saveIntoFile();
            }

            //Adding the table with the existing events
            if(CreatePane.timetable.getEvents() != null && CreatePane.timetable.getEvents().getEvents() != null && CreatePane.timetable.getEvents().getEvents().size()>0) {
                dealWithTable(finalVBox);
            }

        } );

        addSaveButtonIntoHBox(saveButton);

        finalVBox = new VBox(getTitleLabel("Adaugă eveniment"), fp, eventResourceVBox, createExplanatory(), saveButtonHB);
        finalVBox.getStyleClass().add("rightVBox");

        //Adding the table with the existing times
        if(CreatePane.timetable.getEvents()!= null && CreatePane.timetable.getEvents().getEvents()!=null && CreatePane.timetable.getEvents().getEvents().size()>0) {
            addTable(finalVBox);
        }

        return finalVBox;
    }

    private HBox createEventResourceLabel(String text){
        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add("resourceLabelForEvent");

        ImageView imageView = new ImageView(new Image("\\icons\\deleteIcon.png"));
        imageView.setPreserveRatio(true);

        HBox hb = new HBox(label);
        hb.setPadding(new Insets(3, 5, 10, 0));
        hb.setId(text);

        return hb;
    }

    protected VBox createTable(){
        Label existingLabel = new Label("Evenimente");
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
        idColumn.setCellValueFactory(new PropertyValueFactory<Event, Integer>("id"));
        idColumn.setMaxWidth(70);
        idColumn.setPrefWidth(70);
        idColumn.setMinWidth(70);
        idColumn.getStyleClass().add("firstColumn");

        TableColumn descriptionColumn = new TableColumn("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Event, Integer>("description"));
        descriptionColumn.setMaxWidth(100);
        descriptionColumn.setPrefWidth(100);
        descriptionColumn.setMinWidth(100);

        TableColumn hoursColumn = new TableColumn("Interval");
        hoursColumn.setCellValueFactory(new PropertyValueFactory<Event, Time>("time"));
        hoursColumn.setCellFactory(new Callback<TableColumn<Event, Time>, TableCell<Event, Time>>(){

            @Override
            public TableCell<Event, Time> call(TableColumn<Event, Time> param) {

                    TableCell<Event, Time> cityCell = new TableCell<Event, Time>(){

                    @Override
                    protected void updateItem(Time item, boolean empty) {
                        if (item != null) {
                            setText(item.getName());
                        }
                    }
                };

                return cityCell;
            }

        });

        TableColumn resorucesColumn = new TableColumn("Resurse");
        resorucesColumn.setCellValueFactory(new PropertyValueFactory<Event, Resources>("resources"));
        resorucesColumn.setCellFactory(new Callback<TableColumn<Event, Resources>, TableCell<Event, Resources>>(){

            @Override
            public TableCell<Event, Resources> call(TableColumn<Event, Resources> param) {

                TableCell<Event, Resources> cityCell = new TableCell<Event, Resources>(){

                    @Override
                    protected void updateItem(Resources item, boolean empty) {
                        if (item != null) {
                            HBox hb = new HBox();
                            for(Resource r: item.getResources()){
                                Label l = new Label();

                                if(r.getResourceType().equals("teacher")){
                                    l.setText(r.getName());
                                }
                                else{
                                    l.setText(r.getId());
                                }

                                l.getStyleClass().add("resourceLabelForEvent");
                                hb.getChildren().add(l);
                                hb.setSpacing(5);
                                hb.setAlignment(Pos.CENTER_LEFT);
                            }
                            setGraphic(hb);
                        }
                    }
                };

                return cityCell;
            }

        });

        table.getColumns().addAll(idColumn, descriptionColumn, hoursColumn, resorucesColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        updateTableData();

        table.setFixedCellSize(35);
        table.setPrefHeight((table.getFixedCellSize()+0.8) * (table.getItems().size()+1));

        VBox vb = new VBox(nameBox, table);
        vb.setId("table");

        return vb;
    }

    protected void updateTableData(){
        ObservableList<Event> data = FXCollections.observableArrayList(CreatePane.timetable.getEvents().getEvents());
        table.setItems(data);
    }

}
