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
import model.constraint.Constraints;
import model.constraint.types.AssignResourceConstraint;
import model.constraint.types.AssignTimeConstraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.time.Time;
import view.panes.CreatePane;
import java.util.*;

/**
 * Created by Anca on 5/1/2017.
 */
public class InsertEventConstraint extends InsertPaneWithTable {
    private Constraint currentConstraint;
    private VBox finalVBox;
    private String resourceType;
    private String optionName;

    private ComboBox<String> weightCB;
    private CheckBox descriptionCheckBox;
    private FlowPane eventsFP;
    private ComboBox<String> eventsCB;
    private ObservableList<String> eventIds;

    public InsertEventConstraint(Stage primaryStage, String resourceType, Constraint c, String optionName){
        this.currentConstraint = c;
        this.primaryStage = primaryStage;
        this.resourceType = resourceType;
        this.optionName = optionName;

        if(CreatePane.timetable.getEventConstraints() == null) {
            Constraints constraints = new Constraints();
            constraints.setConstraints(new ArrayList<>());
            CreatePane.timetable.setEventConstraints(constraints);
        }

        if(CreatePane.timetable.getEventConstraints().getConstraints() == null) {
            CreatePane.timetable.getEventConstraints().setConstraints(new ArrayList<>());
        }
    }

    private ArrayList<Pair<String, Boolean>> getTextFieldValues(){
        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Adaugă greutate", Boolean.TRUE));

        if(currentConstraint.getWeight() != 0){
            textFieldValues.remove(0);
            textFieldValues.add(0, new Pair(currentConstraint.getWeight(), Boolean.FALSE));
        }

        return textFieldValues;
    }


    public VBox addRightPane(){
        ArrayList<Pair<String, Boolean>> textFieldValues = getTextFieldValues();

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        ObservableList<String> weights = FXCollections.observableArrayList("Mică (1)", "Medie (2)", "Mare (3)");

        HBox weightLabel = makeLabel("GREUTATE", true);
        weightCB = new ComboBox<>(weights);
        weightCB.getStyleClass().add("specialComboBox");
        weightCB.setPromptText(String.valueOf(textFieldValues.get(0).getKey()));
        vbList.add(new VBox(weightLabel, weightCB));

        HBox descriptionLabel = makeLabel("CONSTRÂNGERE NECESARĂ", false);
        descriptionCheckBox = new CheckBox();
        vbList.add(new VBox(descriptionLabel, descriptionCheckBox));

        FlowPane fp = getFlowPane(vbList);

        Map<String, Event> idEventMap = new HashMap<>();

        //Events that are added in the timetable object
        eventIds = FXCollections.observableArrayList();
        if(CreatePane.timetable.getEvents()!=null && CreatePane.timetable.getEvents().getEvents() != null) {
            for (Event r : CreatePane.timetable.getEvents().getEvents()) {
                eventIds.add(r.getId());
                idEventMap.put(r.getId(), r);
            }
        }

        //Events linked to the current constraint
        //We also remove the events that are already linked to the current constraint from the possible choices of events
        ObservableList<HBox> constraintEventIdLabels = FXCollections.observableArrayList();
        if(currentConstraint.getAppliesToEvents() != null && currentConstraint.getAppliesToEvents().getEvents() != null) {
            for (Event r : currentConstraint.getAppliesToEvents().getEvents()) {
                constraintEventIdLabels.add(createConstraintEventLabel(r.getId()));
                eventIds.remove(r.getId());
            }
        }

        HBox eventsLabel = makeLabel("LISTA EVENIMENTE", true);

        eventsFP = new FlowPane();
        eventsFP.getChildren().addAll(constraintEventIdLabels);

        eventsCB = new ComboBox<>(eventIds);
        eventsCB.getStyleClass().add("specialComboBox");

        ImageView imageView = new ImageView(new Image("\\icons\\addIcon.png"));
        imageView.setFitHeight(12);
        imageView.setFitWidth(12);
        imageView.setPreserveRatio(true);

        Button addButton = new Button();
        addButton.setGraphic(imageView);
        addButton.getStyleClass().add("addButton");

        addButton.setOnAction((ActionEvent event) ->{
            if(eventsCB.getValue()!=null){
                HBox hb = createConstraintEventLabel(eventsCB.getValue());

                Button remove = new Button();
                ImageView imageView2 = new ImageView(new Image("\\icons\\deleteIcon.png"));
                imageView2.setFitHeight(6);
                imageView2.setFitWidth(6);
                imageView2.setPreserveRatio(true);

                remove.setGraphic(imageView2);
                remove.getStyleClass().add("removeEventResource");
                remove.setMaxSize(10, 10);

                remove.setOnAction((ActionEvent e) ->{
                    eventsFP.getChildren().remove(hb);

                    eventIds.add(hb.getId());
                    eventsCB.setItems(null);
                    eventsCB.setItems(eventIds);

                });

                hb.getChildren().add(remove);
                eventsFP.getChildren().add(hb);

                eventIds.remove(eventsCB.getValue());
                eventsCB.setItems(null);
                eventsCB.setItems(eventIds);
            }
        });

        HBox addConstraintHBox = new HBox(eventsCB, addButton);

        VBox constraintEventVBox = new VBox(eventsLabel, eventsFP, addConstraintHBox);
        constraintEventVBox.setPadding(new Insets(20, 0, 0 , 0));

        Button saveButton = new Button("SALVEAZĂ");
        saveButton.getStyleClass().add("rightSaveButton");
        saveButton.setOnAction((ActionEvent event) ->{
            //Clearing all errors
            List<HBox> labels = new ArrayList<>(Arrays.asList(weightLabel, eventsLabel));
            List<ComboBox> comboBoxes = new ArrayList<>(Arrays.asList(eventsCB, weightCB));

            //Clearing all previous errors
            clearErrors(labels, new ArrayList<>());

            for(ComboBox cb: comboBoxes) {
                for (String s : cb.getStyleClass()) {
                    if (s.equals("addRedMargin")) {
                        cb.getStyleClass().remove(s);
                        break;
                    }
                }
            }

            boolean empty = false;

            if(weightCB.getValue() == null){
                showErrorMessage(weightLabel, "Greutatea este necesară.", weightCB);
                empty = true;
            }
            else {
                int chosenValue = 0;
                System.out.println(weightCB.getValue());
                switch (weightCB.getValue()){
                    case "Mică (1)":
                        chosenValue = 1;
                        break;
                    case "Medie (2)":
                        chosenValue = 2;
                        break;
                    case "Mare (3)":
                        chosenValue = 3;
                        break;
                    default:
                        break;
                }

                currentConstraint.setWeight(chosenValue);
            }

            if(eventsFP.getChildren().size() < 1){
                showErrorMessage(eventsLabel, "Lista de evenimente este necesară.", eventsCB);
                empty = true;
            }
            else{
                List<Event> eventList = new ArrayList<>();
                for(Node n: eventsFP.getChildren()){
                    eventList.add(idEventMap.get(n.getId()));
                }
                currentConstraint.setAppliesToEvents(new Events(eventList));
            }

            if(descriptionCheckBox.isSelected()){
                currentConstraint.setRequired(true);
            }
            else{
                currentConstraint.setRequired(false);
            }

            if(!empty) {
                //Checking if the current event is an existing one and replacing it in the timetable
                int index = 0;
                boolean exists = false;
                for(Constraint e:CreatePane.timetable.getEventConstraints().getConstraints()){
                    if(e.getId().equals(currentConstraint.getId())){
                        CreatePane.timetable.getEventConstraints().getConstraints().remove(e);
                        CreatePane.timetable.getEventConstraints().getConstraints().add(index, currentConstraint);
                        exists = true;
                        break;
                    }
                    index++;
                }

                if(!exists){
                    //Finding out how many constraints of the current type there are in the timetable
                    int constrNo = 0;
                    for(Constraint c : CreatePane.timetable.getEventConstraints().getConstraints()){
                        if(c.getId().startsWith(resourceType)){
                            constrNo ++;
                        }
                    }

                    currentConstraint.setId(resourceType + "_" + (constrNo + 1));
                    CreatePane.timetable.getEventConstraints().getConstraints().add(currentConstraint);
                    updateTableData();
                }

                if(saveIntoFile()){ //The save button was pressed, the file to save into was chosen
                    if(currentConstraint instanceof AssignResourceConstraint) {
                        currentConstraint = new AssignResourceConstraint();
                        table.getSelectionModel().clearSelection();
                        clearAllFields();
                    }
                    else{
                        if(currentConstraint instanceof AssignTimeConstraint) {
                            currentConstraint = new AssignTimeConstraint();
                            table.getSelectionModel().clearSelection();
                            clearAllFields();
                        }
                    }
                }
                else{
                    CreatePane.timetable.getEventConstraints().getConstraints().remove(currentConstraint);
                }
            }

            //Adding the table with the existing constraints
            if(CreatePane.timetable.getEventConstraints() != null && CreatePane.timetable.getEventConstraints().getConstraints() != null && CreatePane.timetable.getEventConstraints().getConstraints().size()>0) {
                dealWithTable(finalVBox);
            }

        } );

        addSaveButtonIntoHBox(saveButton);

        finalVBox = new VBox(getTitleLabel("Adaugă eveniment"), fp, constraintEventVBox, createExplanatory(), saveButtonHB);
        finalVBox.getStyleClass().add("rightVBox");

        //Adding the table with the existing times
        if(CreatePane.timetable.getEventConstraints()!= null && CreatePane.timetable.getEventConstraints().getConstraints()!=null && CreatePane.timetable.getEventConstraints().getConstraints().size()>0) {
            addTable(finalVBox);
        }

        return finalVBox;
    }

    private void clearAllFields(){
        weightCB.setValue("1");
        eventsCB.setValue("Adaugă eveniment");
        eventsFP.getChildren().clear();
        descriptionCheckBox.setSelected(false);
    }

    private HBox createConstraintEventLabel(String text){
        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add("resourceLabelForEvent");

        HBox hb = new HBox(label);
        hb.setPadding(new Insets(3, 5, 10, 0));
        hb.setId(text);

        return hb;
    }

    protected VBox createTable(){
        Label existingLabel = new Label("Constrângeri tip " + optionName);
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
        idColumn.setCellValueFactory(new PropertyValueFactory<Constraint, String>("id"));
        idColumn.setMaxWidth(200);
        idColumn.setPrefWidth(200);
        idColumn.setMinWidth(200);
        idColumn.getStyleClass().add("firstColumn");

        TableColumn descriptionColumn = new TableColumn("Necesar");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Constraint, Boolean>("required"));
        descriptionColumn.setMaxWidth(100);
        descriptionColumn.setPrefWidth(100);
        descriptionColumn.setMinWidth(100);

        TableColumn weightColumn = new TableColumn("Greutate");
        weightColumn.setCellValueFactory(new PropertyValueFactory<Constraint, Integer>("weight"));
        weightColumn.setMaxWidth(100);
        weightColumn.setPrefWidth(100);
        weightColumn.setMinWidth(100);

        TableColumn eventsColumn = new TableColumn("Evenimente");
        eventsColumn.setCellValueFactory(new PropertyValueFactory<Constraint, Events>("appliesToEvents"));
        eventsColumn.setCellFactory(new Callback<TableColumn<Constraint, Events>, TableCell<Constraint, Events>>(){

            @Override
            public TableCell<Constraint, Events> call(TableColumn<Constraint, Events> param) {

                TableCell<Constraint, Events> cityCell = new TableCell<Constraint, Events>(){

                    @Override
                    protected void updateItem(Events item, boolean empty) {
                        if (item != null) {
                            FlowPane fp = new FlowPane();
                            for(Event r: item.getEvents()){
                                Label l = new Label();
                                l.setText(r.getId());
                                l.getStyleClass().add("resourceLabelForEvent");

                                fp.getChildren().add(l);
                                fp.setHgap(5);
                                fp.setVgap(5);
                                fp.setAlignment(Pos.CENTER_LEFT);
                            }
                            setGraphic(fp);
                        }
                    }
                };

                return cityCell;
            }

        });

        TableColumn deleteColumn = new TableColumn();
        Callback<TableColumn<Constraint, String>, TableCell<Constraint, String>> cellFactory =
                new Callback<TableColumn<Constraint, String>, TableCell<Constraint, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<Constraint, String> param )
                    {
                        final TableCell<Constraint, String> cell = new TableCell<Constraint, String>()
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
                                        Constraint constraint = getTableView().getItems().get(getIndex());

                                        int indexOfConstraint = CreatePane.timetable.getEventConstraints().getConstraints().indexOf(constraint);
                                        CreatePane.timetable.getEventConstraints().getConstraints().remove(constraint);

                                        //Resetting the ids
                                        if(CreatePane.timetable.getEventConstraints().getConstraints().size()>1) {
                                            for (Constraint c : CreatePane.timetable.getEventConstraints().getConstraints().subList(indexOfConstraint, CreatePane.timetable.getEventConstraints().getConstraints().size())) {
                                                String[] splitted = c.getId().split("_");
                                                c.setId(splitted[0]+"_"+(Integer.valueOf(splitted[1])-1));
                                            }
                                        }
                                        else{
                                            if(CreatePane.timetable.getEventConstraints().getConstraints().size() == 1) {
                                                Constraint c = CreatePane.timetable.getEventConstraints().getConstraints().get(0);
                                                String[] splitted = c.getId().split("_");
                                                CreatePane.timetable.getEventConstraints().getConstraints().get(0).setId(splitted[0]+"_1");
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

        table.getColumns().addAll(idColumn, descriptionColumn, weightColumn, eventsColumn, deleteColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setEditable(true);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                Constraint e = (Constraint) newValue;
                currentConstraint = e;

                String weightString = null;
                switch (e.getWeight()){
                    case (1):
                        weightString = "Mică (1)";
                        break;
                    case (2):
                        weightString = "Medie (2)";
                        break;
                    case (3):
                        weightString = "Mare (3)";
                        break;
                    default:
                        break;
                }

                weightCB.setValue(weightString);

                descriptionCheckBox.setSelected(e.getRequired());

                ObservableList<HBox> constraintEventIdLabels = FXCollections.observableArrayList();
                if(currentConstraint.getAppliesToEvents().getEvents()!=null) {
                    for (Event r : currentConstraint.getAppliesToEvents().getEvents()) {
                        final HBox hb;
                        hb = createConstraintEventLabel(r.getId());
                        eventIds.remove(r.getId());

                        Button remove = new Button();
                        ImageView imageView2 = new ImageView(new Image("\\icons\\deleteIcon.png"));
                        imageView2.setFitHeight(6);
                        imageView2.setFitWidth(6);
                        imageView2.setPreserveRatio(true);

                        remove.setGraphic(imageView2);
                        remove.getStyleClass().add("removeEventResource");
                        remove.setMaxSize(10, 10);

                        remove.setOnAction((ActionEvent ev) ->{
                            eventsFP.getChildren().remove(hb);

                            eventIds.add(hb.getId());
                            eventsCB.setItems(null);
                            eventsCB.setItems(eventIds);

                        });

                        hb.getChildren().add(remove);
                        constraintEventIdLabels.add(hb);
                    }
                }

                eventsFP.getChildren().clear();
                eventsFP.getChildren().addAll(constraintEventIdLabels);
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
        ObservableList<Constraint> data = FXCollections.observableArrayList();

        String constraintType = null;

        if(currentConstraint instanceof AssignResourceConstraint){
            constraintType = "assignResourceConstraint";
        }
        else{
            if(currentConstraint instanceof AssignTimeConstraint){
                constraintType = "assignTimeConstraint";
            }
        }

        for(Constraint c: CreatePane.timetable.getEventConstraints().getConstraints()){
            if(c.getId().startsWith(constraintType)){
                data.add(c);
            }
        }

        if(table == null){
            table = new TableView();
        }

        table.getItems().clear();
        if(data!=null) {
            table.setItems(data);
            table.setPrefHeight((table.getFixedCellSize() + 0.8) * (table.getItems().size() + 1));
        }
        else{
            table.setItems(FXCollections.observableArrayList());
        }
    }
}
