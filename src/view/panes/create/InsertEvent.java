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
import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;
import model.time.Time;
import utilities.PropertiesLoader;
import view.panes.CreatePane;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Anca on 5/1/2017.
 */
public class InsertEvent extends InsertPaneWithTable {
    private Event currentEvent = new Event();
    private VBox finalVBox;

    private ComboBox<String> descriptionCB;
    private ComboBox<String> timeCB;
    private FlowPane resourcesFP;
    private ObservableList<String> resourceIds;
    private ComboBox<String> resourceCB;

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

        textFieldValues.add(new Pair("Alege intervalul orar", Boolean.TRUE));
        textFieldValues.add(new Pair("Alege resursa", Boolean.TRUE));
        textFieldValues.add(new Pair("Alege materia", Boolean.TRUE));

        if(currentEvent.getTime()!=null){
            textFieldValues.remove(0);
            textFieldValues.add(0, new Pair(currentEvent.getTime().getId(), Boolean.FALSE));
        }

        if(currentEvent.getDescription()!=null){
            textFieldValues.remove(2);
            textFieldValues.add(2, new Pair(currentEvent.getDescription(), Boolean.FALSE));
        }

        return textFieldValues;
    }

    public VBox addRightPane(){
        ArrayList<Pair<String, Boolean>> textFieldValues = getTextFieldValues();

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        ObservableList<String> subjects = FXCollections.observableArrayList();

        //Reading the subjects list from file
        String path = PropertiesLoader.loadSubjectsFilePath();

        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                subjects.add(sCurrentLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HBox descriptionLabel = makeLabel("MATERIE", true);
        descriptionCB = new ComboBox<>(subjects);
        descriptionCB.getStyleClass().add("specialComboBox");
        descriptionCB.setPromptText(textFieldValues.get(2).getKey());
        descriptionCB.setEditable(true);
        vbList.add(new VBox(descriptionLabel, descriptionCB));

        ObservableList<String> timeIds = FXCollections.observableArrayList();
        if(CreatePane.timetable.getTimes()!=null && CreatePane.timetable.getTimes().getTimes() != null) {
            for (Time t : CreatePane.timetable.getTimes().getTimes()) {
                timeIds.add(t.getName());
            }
        }

        HBox timeLabel = makeLabel("INTERVALUL ORAR", false);
        timeCB = new ComboBox<>(timeIds);
        timeCB.getStyleClass().add("specialComboBox");
        timeCB.setPromptText(String.valueOf(textFieldValues.get(0).getKey()));
        vbList.add(new VBox(timeLabel, timeCB));

        FlowPane fp = getFlowPane(vbList);

        Map<String, Resource> idResourceMap = new HashMap<>();

        //Resources that are added in the timetable object
        resourceIds = FXCollections.observableArrayList();
        if(CreatePane.timetable.getResources()!=null && CreatePane.timetable.getResources().getResources() != null) {
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

        resourceCB = new ComboBox<>(resourceIds);
        resourceCB.getStyleClass().add("specialComboBox");
        resourceCB.setPromptText(textFieldValues.get(1).getKey());

        //Resources linked to the current event
        //We also remove the resources that are already linked to the current event from the possible choices of resources
        ObservableList<HBox> eventResourceIdLabels = FXCollections.observableArrayList();
        if(currentEvent.getResources()!=null) {
            for (Resource r : currentEvent.getResources().getResources()) {
                final HBox hb;
                if(r.getResourceType().equals("teacher")){
                    hb = createEventResourceLabel(r.getName());
                    resourceIds.remove(r.getName());
                }
                else {
                    hb = createEventResourceLabel(r.getId());
                    resourceIds.remove(r.getId());
                }

                Button remove = new Button();
                ImageView imageView2 = new ImageView(new Image("\\icons\\deleteIcon.png"));
                imageView2.setFitHeight(6);
                imageView2.setFitWidth(6);
                imageView2.setPreserveRatio(true);

                remove.setGraphic(imageView2);
                remove.getStyleClass().add("removeEventResource");
                remove.setMaxSize(10, 10);

                remove.setOnAction((ActionEvent e) ->{
                    resourcesFP.getChildren().remove(hb);

                    resourceIds.add(hb.getId());
                    resourceCB.setItems(null);
                    resourceCB.setItems(resourceIds);

                });

                hb.getChildren().add(remove);
                eventResourceIdLabels.add(hb);
            }
        }

        HBox resourceLabel = makeLabel("LISTA RESURSE", true);

        resourcesFP = new FlowPane();
        resourcesFP.getChildren().addAll(eventResourceIdLabels);

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
                ImageView imageView2 = new ImageView(new Image("\\icons\\deleteIcon.png"));
                imageView2.setFitHeight(6);
                imageView2.setFitWidth(6);
                imageView2.setPreserveRatio(true);

                remove.setGraphic(imageView2);
                remove.getStyleClass().add("removeEventResource");
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
            List<HBox> labels = new ArrayList<>(Arrays.asList( timeLabel, resourceLabel, descriptionLabel));
            List<TextField> textFields =  new ArrayList<>();
            List<ComboBox> comboBoxes = new ArrayList<>(Arrays.asList(timeCB, resourceCB, descriptionCB));

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

            if(timeCB.getValue() !=null){
                for(Time t: CreatePane.timetable.getTimes().getTimes()){
                    if(t.getName().equals(timeCB.getValue())){
                        currentEvent.setTime(t);
                    }
                }
            }

            if(descriptionCB.getValue()== null || descriptionCB.getValue().isEmpty()){
                showErrorMessage(descriptionLabel, "Descrierea este necesară.", descriptionCB);
                empty = true;
            }
            else {
                currentEvent.setDescription(descriptionCB.getValue());
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
                //Checking if the current event is an existing one and replacing it in the timetable
                int index = 0;
                boolean exists = false;
                for(Event e:CreatePane.timetable.getEvents().getEvents()){
                    if(e.getId().equals(currentEvent.getId())){
                        CreatePane.timetable.getEvents().getEvents().remove(e);
                        CreatePane.timetable.getEvents().getEvents().add(index, currentEvent);

                        //Changing all the instances of the changed object from the timetable
                        if(CreatePane.timetable.getEventConstraints()!=null && CreatePane.timetable.getEventConstraints().getConstraints()!=null) {
                            for (Constraint c : CreatePane.timetable.getEventConstraints().getConstraints()) {
                                int eIndex = 0;
                                for (Event ev : c.getAppliesToEvents().getEvents()) {
                                    if (ev.getId().equals(currentEvent.getId())) {
                                        c.getAppliesToEvents().getEvents().set(eIndex, currentEvent);
                                    }
                                    eIndex++;
                                }
                            }
                        }

                        exists = true;
                        break;
                    }
                    index++;
                }

                if(!exists){
                    currentEvent.setId("ev_"+(int)(CreatePane.timetable.getEvents().getEvents().size()+1));
                    CreatePane.timetable.getEvents().getEvents().add(currentEvent);
                    updateTableData();
                }

                if(saveIntoFile()){ //The save button was pressed, the file to save into was chosen
                    currentEvent = new Event();
                    table.getSelectionModel().clearSelection();
                    clearAllFields();
                }
                else{
                    CreatePane.timetable.getEvents().getEvents().remove(currentEvent);
                }
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

    private void clearAllFields(){
        descriptionCB.setValue(null);
        descriptionCB.setPromptText("Alege materia");
        timeCB.setValue(null);
        timeCB.setPromptText("Alege intervalul orar");
        resourcesFP.getChildren().clear();
    }

    private HBox createEventResourceLabel(String text){
        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add("resourceLabelForEvent");

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

        TableColumn descriptionColumn = new TableColumn("Materie");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Event, Integer>("description"));

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

        TableColumn deleteColumn = new TableColumn();
        Callback<TableColumn<Event, String>, TableCell<Event, String>> cellFactory =
                new Callback<TableColumn<Event, String>, TableCell<Event, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<Event, String> param )
                    {
                        final TableCell<Event, String> cell = new TableCell<Event, String>()
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
                                        Event ev = getTableView().getItems().get(getIndex());

                                        int indexOfEv = CreatePane.timetable.getEvents().getEvents().indexOf(ev);
                                        CreatePane.timetable.getEvents().getEvents().remove(ev);

                                        //Changing all the instances of the changed object from the timetable
                                        if(CreatePane.timetable.getEventConstraints()!=null && CreatePane.timetable.getEventConstraints().getConstraints()!=null) {
                                            for (Constraint c : CreatePane.timetable.getEventConstraints().getConstraints()) {
                                                List<Event> toRemove = new ArrayList<>();
                                                for (Event evn : c.getAppliesToEvents().getEvents()) {
                                                    if (evn.getId().equals(ev.getId())) {
                                                        toRemove.add(evn);
                                                    }
                                                }
                                                c.getAppliesToEvents().getEvents().removeAll(toRemove);
                                            }
                                        }

                                        //Resetting the ids
                                        if(CreatePane.timetable.getEvents().getEvents().size()>1) {
                                            for (Event e : CreatePane.timetable.getEvents().getEvents().subList(indexOfEv, CreatePane.timetable.getEvents().getEvents().size())) {
                                                String[] splitted = e.getId().split("_");
                                                e.setId(splitted[0]+"_"+(Integer.valueOf(splitted[1])-1));
                                            }
                                        }
                                        else{
                                            if(CreatePane.timetable.getEvents().getEvents().size() == 1) {
                                                Event e = CreatePane.timetable.getEvents().getEvents().get(0);
                                                String[] splitted = e.getId().split("_");
                                                CreatePane.timetable.getEvents().getEvents().get(0).setId(splitted[0]+"_1");
                                            }
                                        }

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

        table.getColumns().addAll(idColumn, descriptionColumn, hoursColumn, resorucesColumn, deleteColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setEditable(true);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                Event e = (Event)newValue;
                currentEvent = e;
                descriptionCB.setValue(e.getDescription());

                if(e.getTime()!=null) {
                    timeCB.setValue(String.valueOf(e.getTime().getId()));
                }
                else{
                    timeCB.setValue("Alege intervalul orar");
                }

                ObservableList<HBox> eventResourceIdLabels = FXCollections.observableArrayList();
                if(currentEvent.getResources()!=null) {
                    for (Resource r : currentEvent.getResources().getResources()) {
                        final HBox hb;
                        if(r.getResourceType().equals("teacher")){
                            hb = createEventResourceLabel(r.getName());
                            resourceIds.remove(r.getName());
                        }
                        else {
                            hb = createEventResourceLabel(r.getId());
                            resourceIds.remove(r.getId());
                        }

                        Button remove = new Button();
                        ImageView imageView2 = new ImageView(new Image("\\icons\\deleteIcon.png"));
                        imageView2.setFitHeight(6);
                        imageView2.setFitWidth(6);
                        imageView2.setPreserveRatio(true);

                        remove.setGraphic(imageView2);
                        remove.getStyleClass().add("removeEventResource");
                        remove.setMaxSize(10, 10);

                        remove.setOnAction((ActionEvent ev) ->{
                            resourcesFP.getChildren().remove(hb);

                            resourceIds.add(hb.getId());
                            resourceCB.setItems(null);
                            resourceCB.setItems(resourceIds);

                        });

                        hb.getChildren().add(remove);
                        eventResourceIdLabels.add(hb);
                    }
                }

                resourcesFP.getChildren().clear();
                resourcesFP.getChildren().addAll(eventResourceIdLabels);
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
        ObservableList<Event> data = FXCollections.observableArrayList(CreatePane.timetable.getEvents().getEvents());

        if(table == null){
            table = new TableView();
        }

        if(!data.isEmpty()) {
            table.setItems(data);
            table.setPrefHeight((table.getFixedCellSize() + 0.8) * (table.getItems().size() + 1));
        }
        else{
            table.setItems(FXCollections.observableArrayList());
        }
    }

}
