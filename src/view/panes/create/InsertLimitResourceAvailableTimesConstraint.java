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
import model.constraint.types.LimitResourceAvailableTimesConstraint;
import model.resource.Resource;
import model.resource.Resources;
import model.time.Time;
import model.time.Times;
import view.panes.CreatePane;

import java.util.*;

/**
 * Created by Anca on 5/1/2017.
 */
public class InsertLimitResourceAvailableTimesConstraint extends InsertPaneWithTable {
    private LimitResourceAvailableTimesConstraint currentConstraint;
    private VBox finalVBox;
    private String resourceType;
    private String optionName;

    private ComboBox<String> weightCB;
    private CheckBox descriptionCheckBox;
    private FlowPane resourcesFP;
    private FlowPane timesFP;
    private ObservableList<String> resourceIds;
    private ObservableList<String> timeIds;
    private ObservableList<String> initialResourceIds;
    private ObservableList<String> initialTimeIds;
    private ComboBox<String> resourceCB;
    private ComboBox<String> timeCB;

    public InsertLimitResourceAvailableTimesConstraint(Stage primaryStage, String resourceType, LimitResourceAvailableTimesConstraint c, String optionName){
        this.currentConstraint = c;
        this.primaryStage = primaryStage;
        this.resourceType = resourceType;
        this.optionName = optionName;

        if(CreatePane.timetable.getResourceConstraints() == null) {
            Constraints constraints = new Constraints();
            constraints.setConstraints(new ArrayList<>());
            CreatePane.timetable.setResourceConstraints(constraints);
        }

        if(CreatePane.timetable.getResourceConstraints().getConstraints() == null) {
            CreatePane.timetable.getResourceConstraints().setConstraints(new ArrayList<>());
        }
    }

    private ArrayList<Pair<String, Boolean>> getTextFieldValues(){
        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Adaugă greutate", Boolean.TRUE));
        textFieldValues.add(new Pair("Alege resursa", Boolean.TRUE));
        textFieldValues.add(new Pair("Alege intervalul", Boolean.TRUE));

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
        new ComboBoxAutoComplete<>(weightCB);
        vbList.add(new VBox(weightLabel, weightCB));

        HBox descriptionLabel = makeLabel("CONSTRÂNGERE NECESARĂ", false);
        descriptionCheckBox = new CheckBox();
        vbList.add(new VBox(descriptionLabel, descriptionCheckBox));

        ObservableList<Integer> numbersList = FXCollections.observableArrayList();
        for(int i=0; i<50; i++){
            numbersList.add(i);
        }

        FlowPane fp = getFlowPane(vbList);

        Map<String, Resource> idResourceMap = new HashMap<>();

        //Resources that are added in the timetable object
        resourceIds = FXCollections.observableArrayList();
        initialResourceIds = FXCollections.observableArrayList();
        if(CreatePane.timetable.getResources()!=null && CreatePane.timetable.getResources().getResources() != null) {
            for (Resource r : CreatePane.timetable.getResources().getResources()) {
                if(r.getResourceType().equals("teacher")){
                    resourceIds.add(r.getName());
                    initialResourceIds.add(r.getName());
                    idResourceMap.put(r.getName(), r);
                }
                else {
                    resourceIds.add(r.getId());
                    initialResourceIds.add(r.getId());
                    idResourceMap.put(r.getId(), r);
                }
            }
        }

        resourceCB = new ComboBox<>(resourceIds);
        resourceCB.getStyleClass().add("specialComboBox");
        resourceCB.setPromptText(textFieldValues.get(1).getKey());
        new ComboBoxAutoComplete<>(resourceCB);

        //Resources linked to the current constraint
        //We also remove the resources that are already linked to the current constraint from the possible choices of resources
        ObservableList<HBox> constraintResourceIdLabels = FXCollections.observableArrayList();
        if(currentConstraint.getAppliesToResources()!=null) {
            for (Resource r : currentConstraint.getAppliesToResources().getResources()) {
                final HBox hb;
                if(r.getResourceType().equals("teacher")){
                    hb = createConstraintResourceLabel(r.getName());
                    resourceIds.remove(r.getName());
                }
                else {
                    hb = createConstraintResourceLabel(r.getId());
                    resourceIds.remove(r.getId());
                }

                Button remove = new Button();
                ImageView imageView2 = new ImageView(new Image("\\viewicons\\deleteIcon.png"));
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
                constraintResourceIdLabels.add(hb);
            }
        }

        HBox resourcesLabel = makeLabel("LISTA RESURSE", true);

        resourcesFP = new FlowPane();
        resourcesFP.getChildren().addAll(constraintResourceIdLabels);

        ImageView imageView = new ImageView(new Image("\\view\\icons\\addIcon.png"));
        imageView.setFitHeight(12);
        imageView.setFitWidth(12);
        imageView.setPreserveRatio(true);

        Button addButton = new Button();
        addButton.setGraphic(imageView);
        addButton.getStyleClass().add("addButton");

        addButton.setOnAction((ActionEvent event) ->{
            if(resourceCB.getValue()!=null){
                HBox hb = createConstraintResourceLabel(resourceCB.getValue());

                Button remove = new Button();
                ImageView imageView2 = new ImageView(new Image("\\view\\icons\\deleteIcon.png"));
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

        HBox addConstraintHBox = new HBox(resourceCB, addButton);

        VBox constraintResourceVBox = new VBox(resourcesLabel, resourcesFP, addConstraintHBox);
        constraintResourceVBox.setPadding(new Insets(20, 0, 0 , 0));


        Map<String, Time> idTimeMap = new HashMap<>();

        timeIds = FXCollections.observableArrayList();
        initialTimeIds = FXCollections.observableArrayList();
        if(CreatePane.timetable.getTimes()!=null && CreatePane.timetable.getTimes().getTimes() != null) {
            for (Time t : CreatePane.timetable.getTimes().getTimes()) {
                timeIds.add(t.getDay()+" "+t.getHourInterval());
                initialTimeIds.add(t.getDay()+" "+t.getHourInterval());
                idTimeMap.put(t.getDay()+" "+t.getHourInterval(), t);
            }
        }

        timeCB = new ComboBox<>(timeIds);
        timeCB.getStyleClass().add("specialComboBox");
        timeCB.setPromptText(textFieldValues.get(2).getKey());
        new ComboBoxAutoComplete<>(timeCB);

        ObservableList<HBox> constraintTimeIdLabels = FXCollections.observableArrayList();
        if(currentConstraint.getNotAvailableTimes()!=null) {
            for (Time t : currentConstraint.getNotAvailableTimes().getTimes()) {
                final HBox hb;
                hb = createConstraintResourceLabel(t.getDay()+" "+t.getHourInterval());
                timeIds.remove(t.getDay()+" "+t.getHourInterval());

                Button remove = new Button();
                ImageView imageView2 = new ImageView(new Image("\\viewicons\\deleteIcon.png"));
                imageView2.setFitHeight(6);
                imageView2.setFitWidth(6);
                imageView2.setPreserveRatio(true);

                remove.setGraphic(imageView2);
                remove.getStyleClass().add("removeEventResource");
                remove.setMaxSize(10, 10);

                remove.setOnAction((ActionEvent e) ->{
                    timesFP.getChildren().remove(hb);

                    timeIds.add(hb.getId());
                    timeCB.setItems(null);
                    timeCB.setItems(timeIds);

                });

                hb.getChildren().add(remove);
                constraintTimeIdLabels.add(hb);
            }
        }

        HBox timesLabel = makeLabel("LISTA ORE INDISPONIBILE", true);

        timesFP = new FlowPane();
        timesFP.getChildren().addAll(constraintTimeIdLabels);

        ImageView imageView3 = new ImageView(new Image("\\view\\icons\\addIcon.png"));
        imageView3.setFitHeight(12);
        imageView3.setFitWidth(12);
        imageView3.setPreserveRatio(true);

        Button addButton2 = new Button();
        addButton2.setGraphic(imageView3);
        addButton2.getStyleClass().add("addButton");

        addButton2.setOnAction((ActionEvent event) ->{
            if(timeCB.getValue()!=null){
                HBox hb = createConstraintResourceLabel(timeCB.getValue());

                Button remove = new Button();
                ImageView imageView2 = new ImageView(new Image("\\view\\icons\\deleteIcon.png"));
                imageView2.setFitHeight(6);
                imageView2.setFitWidth(6);
                imageView2.setPreserveRatio(true);

                remove.setGraphic(imageView2);
                remove.getStyleClass().add("removeEventResource");
                remove.setMaxSize(10, 10);

                remove.setOnAction((ActionEvent e) ->{
                    timesFP.getChildren().remove(hb);

                    timeIds.add(hb.getId());
                    timeCB.setItems(null);
                    timeCB.setItems(timeIds);

                });

                hb.getChildren().add(remove);
                timesFP.getChildren().add(hb);

                timeIds.remove(timeCB.getValue());
                timeCB.setItems(null);
                timeCB.setItems(timeIds);
            }
        });

        HBox addConstraintHBox2 = new HBox(timeCB, addButton2);

        VBox constraintResourceVBox2 = new VBox(timesLabel, timesFP, addConstraintHBox2);
        constraintResourceVBox2.setPadding(new Insets(20, 0, 0 , 0));


        Button saveButton = new Button("SALVEAZĂ");
        saveButton.getStyleClass().add("rightSaveButton");
        saveButton.setOnAction((ActionEvent event) ->{
            //Clearing all errors
            List<HBox> labels = new ArrayList<>(Arrays.asList(weightLabel, resourcesLabel, timesLabel));
            List<ComboBox> comboBoxes = new ArrayList<>(Arrays.asList(resourceCB, weightCB, timeCB));

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

            if(resourcesFP.getChildren().size() < 1){
                showErrorMessage(resourcesLabel, "Lista de resurse este necesară.", resourceCB);
                empty = true;
            }
            else{
                List<Resource> resourceList = new ArrayList<>();
                for(Node n: resourcesFP.getChildren()){
                    resourceList.add(idResourceMap.get(n.getId()));
                }
                currentConstraint.setAppliesToResources(new Resources(resourceList));
            }

            if(timesFP.getChildren().size() < 1){
                showErrorMessage(timesLabel, "Lista de ore este necesară.", timeCB);
                empty = true;
            }
            else{
                List<Time> timeList = new ArrayList<>();
                for(Node n: timesFP.getChildren()){
                    timeList.add(idTimeMap.get(n.getId()));
                }
                currentConstraint.setNotAvailableTimes(new Times(timeList));
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
                for(Constraint e:CreatePane.timetable.getResourceConstraints().getConstraints()){
                    if(e.getId().equals(currentConstraint.getId())){
                        CreatePane.timetable.getResourceConstraints().getConstraints().remove(e);
                        CreatePane.timetable.getResourceConstraints().getConstraints().add(index, currentConstraint);
                        exists = true;
                        break;
                    }
                    index++;
                }

                if(!exists){
                    //Finding out how many constraints of the current type there are in the timetable
                    int constrNo = 0;
                    for(Constraint c : CreatePane.timetable.getResourceConstraints().getConstraints()){
                        if(c.getId().startsWith(resourceType)){
                            constrNo ++;
                        }
                    }

                    currentConstraint.setId(resourceType + "_" + (constrNo + 1));
                    CreatePane.timetable.getResourceConstraints().getConstraints().add(currentConstraint);
                    updateTableData();
                }

                if(saveIntoFile()){ //The save button was pressed, the file to save into was chosen
                    currentConstraint = new LimitResourceAvailableTimesConstraint();
                    table.getSelectionModel().clearSelection();
                    clearAllFields();
                }
                else{
                    CreatePane.timetable.getResourceConstraints().getConstraints().remove(currentConstraint);
                }
            }

            //Adding the table with the existing constraints
            if(CreatePane.timetable.getResourceConstraints() != null && CreatePane.timetable.getResourceConstraints().getConstraints() != null && CreatePane.timetable.getResourceConstraints().getConstraints().size()>0) {
                dealWithTable(finalVBox);
            }

        } );

        addSaveButtonIntoHBox(saveButton);

        finalVBox = new VBox(getTitleLabel("Adaugă resursă"), fp, constraintResourceVBox, constraintResourceVBox2, createExplanatory(), saveButtonHB);
        finalVBox.getStyleClass().add("rightVBox");

        //Adding the table with the existing times
        if(CreatePane.timetable.getResourceConstraints()!= null && CreatePane.timetable.getResourceConstraints().getConstraints()!=null && CreatePane.timetable.getResourceConstraints().getConstraints().size()>0) {
            addTable(finalVBox);
        }

        return finalVBox;
    }

    private void clearAllFields(){
        weightCB.setValue("1");
        resourceCB.setValue("Adaugă resursa");
        timeCB.setValue("Adaugă interval");
        resourcesFP.getChildren().clear();
        timesFP.getChildren().clear();
        resourceIds.setAll(initialResourceIds);
        timeIds.setAll(initialTimeIds);
        descriptionCheckBox.setSelected(false);
    }

    private HBox createConstraintResourceLabel(String text){
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
        idColumn.setCellValueFactory(new PropertyValueFactory<LimitResourceAvailableTimesConstraint, String>("id"));
        idColumn.setMaxWidth(200);
        idColumn.setPrefWidth(200);
        idColumn.setMinWidth(200);
        idColumn.getStyleClass().add("firstColumn");

        TableColumn descriptionColumn = new TableColumn("Necesar");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<LimitResourceAvailableTimesConstraint, Boolean>("required"));
        descriptionColumn.setMaxWidth(100);
        descriptionColumn.setPrefWidth(100);
        descriptionColumn.setMinWidth(100);

        TableColumn weightColumn = new TableColumn("Greutate");
        weightColumn.setCellValueFactory(new PropertyValueFactory<LimitResourceAvailableTimesConstraint, Integer>("weight"));
        weightColumn.setMaxWidth(100);
        weightColumn.setPrefWidth(100);
        weightColumn.setMinWidth(100);

        TableColumn resourcesColumn = new TableColumn("Resurse");
        resourcesColumn.setCellValueFactory(new PropertyValueFactory<LimitResourceAvailableTimesConstraint, Resources>("appliesToResources"));
        resourcesColumn.setCellFactory(new Callback<TableColumn<LimitResourceAvailableTimesConstraint, Resources>, TableCell<LimitResourceAvailableTimesConstraint, Resources>>(){

            @Override
            public TableCell<LimitResourceAvailableTimesConstraint, Resources> call(TableColumn<LimitResourceAvailableTimesConstraint, Resources> param) {

                TableCell<LimitResourceAvailableTimesConstraint, Resources> cityCell = new TableCell<LimitResourceAvailableTimesConstraint, Resources>(){

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

        TableColumn timesColumn = new TableColumn("Intervale indisponibile");
        timesColumn.setCellValueFactory(new PropertyValueFactory<LimitResourceAvailableTimesConstraint, Times>("notAvailableTimes"));
        timesColumn.setCellFactory(new Callback<TableColumn<LimitResourceAvailableTimesConstraint, Times>, TableCell<LimitResourceAvailableTimesConstraint, Times>>(){

            @Override
            public TableCell<LimitResourceAvailableTimesConstraint, Times> call(TableColumn<LimitResourceAvailableTimesConstraint, Times> param) {

                TableCell<LimitResourceAvailableTimesConstraint, Times> cityCell = new TableCell<LimitResourceAvailableTimesConstraint, Times>(){

                    @Override
                    protected void updateItem(Times item, boolean empty) {
                        if (item != null) {
                            HBox hb = new HBox();
                            for(Time r: item.getTimes()){
                                Label l = new Label(r.getDay()+" "+r.getHourInterval());

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

                                        int indexOfConstraint = CreatePane.timetable.getResourceConstraints().getConstraints().indexOf(constraint);
                                        CreatePane.timetable.getResourceConstraints().getConstraints().remove(constraint);

                                        //Resetting the ids
                                        if(CreatePane.timetable.getResourceConstraints().getConstraints().size()>1) {
                                            for (Constraint c : CreatePane.timetable.getResourceConstraints().getConstraints().subList(indexOfConstraint, CreatePane.timetable.getResourceConstraints().getConstraints().size())) {
                                                String[] splitted = c.getId().split("_");
                                                c.setId(splitted[0]+"_"+(Integer.valueOf(splitted[1])-1));
                                            }
                                        }
                                        else{
                                            if(CreatePane.timetable.getResourceConstraints().getConstraints().size() == 1) {
                                                Constraint c = CreatePane.timetable.getResourceConstraints().getConstraints().get(0);
                                                String[] splitted = c.getId().split("_");
                                                CreatePane.timetable.getResourceConstraints().getConstraints().get(0).setId(splitted[0]+"_1");
                                            }
                                        }

                                        updateTableData();
                                        saveIntoFile();
                                    } );

                                    ImageView imageView2 = new ImageView(new Image("\\view\\icons\\deleteIcon.png"));
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

        table.getColumns().addAll(idColumn, descriptionColumn, weightColumn, resourcesColumn, timesColumn, deleteColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setEditable(true);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                LimitResourceAvailableTimesConstraint e = (LimitResourceAvailableTimesConstraint) newValue;
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

                ObservableList<HBox> constraintResourceIdLabels = FXCollections.observableArrayList();
                if(currentConstraint.getAppliesToResources().getResources()!=null) {
                    for (Resource r : currentConstraint.getAppliesToResources().getResources()) {
                        final HBox hb;
                        if(r.getResourceType().equals("teacher")){
                            hb = createConstraintResourceLabel(r.getName());
                        }
                        else{
                            hb = createConstraintResourceLabel(r.getId());
                        }
                        resourceIds.remove(r.getId());

                        Button remove = new Button();
                        ImageView imageView2 = new ImageView(new Image("\\view\\icons\\deleteIcon.png"));
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
                        constraintResourceIdLabels.add(hb);
                    }
                }

                resourcesFP.getChildren().clear();
                resourcesFP.getChildren().addAll(constraintResourceIdLabels);

                ObservableList<HBox> constraintTimeIdLabels = FXCollections.observableArrayList();
                if(currentConstraint.getNotAvailableTimes().getTimes()!=null) {
                    for (Time t : currentConstraint.getNotAvailableTimes().getTimes()) {
                        final HBox hb;
                        hb = createConstraintResourceLabel(t.getDay()+" "+t.getHourInterval());
                        timeIds.remove(String.valueOf(t.getDay()+" "+t.getHourInterval()));

                        Button remove = new Button();
                        ImageView imageView2 = new ImageView(new Image("\\view\\icons\\deleteIcon.png"));
                        imageView2.setFitHeight(6);
                        imageView2.setFitWidth(6);
                        imageView2.setPreserveRatio(true);

                        remove.setGraphic(imageView2);
                        remove.getStyleClass().add("removeEventResource");
                        remove.setMaxSize(10, 10);

                        remove.setOnAction((ActionEvent ev) ->{
                            timesFP.getChildren().remove(hb);

                            timeIds.add(hb.getId());
                            timeCB.setItems(null);
                            timeCB.setItems(timeIds);

                        });

                        hb.getChildren().add(remove);
                        constraintTimeIdLabels.add(hb);
                    }
                }

                timesFP.getChildren().clear();
                timesFP.getChildren().addAll(constraintTimeIdLabels);
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

        for(Constraint c: CreatePane.timetable.getResourceConstraints().getConstraints()){
            if(c.getId().startsWith(resourceType)){
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
