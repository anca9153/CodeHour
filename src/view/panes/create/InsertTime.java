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
import model.time.Time;
import model.time.Times;
import view.panes.CreatePane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anca on 4/24/2017.
 */
public class InsertTime extends InsertPaneWithTable{
    private Time currentTime = new Time();
    private VBox finalVBox;

    public InsertTime(Stage primaryStage){
        this.primaryStage = primaryStage;

        if(CreatePane.timetable.getTimes() == null) {
            Times times = new Times();
            times.setTimes(new ArrayList<>());
            CreatePane.timetable.setTimes(times);
        }

        if(CreatePane.timetable.getTimes().getTimes() == null) {
            CreatePane.timetable.getTimes().setTimes(new ArrayList<>());
        }
    }

    private ArrayList<Pair<String, Boolean>> getTextFieldValues(){
        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Alege ziua intervalului", Boolean.TRUE));
        textFieldValues.add(new Pair("8:00 - 9:00", Boolean.TRUE));

        if(currentTime.getDay()!=null){
            textFieldValues.remove(0);
            textFieldValues.add(0, new Pair(currentTime.getDay(), Boolean.FALSE));
        }

        if(currentTime.getHourInterval()!=null){
            textFieldValues.remove(1);
            textFieldValues.add(1, new Pair(currentTime.getHourInterval(), Boolean.FALSE));
        }

        return textFieldValues;
    }

    public VBox addRightPane(){
        ArrayList<Pair<String, Boolean>> textFieldValues = getTextFieldValues();

        ObservableList<String> days = FXCollections.observableArrayList("monday", "tuesday", "wednesday","thursday", "friday", "saturday", "sunday");

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        HBox dayLabel = makeLabel("ZIUA", true);
        ComboBox<String> dayCB = new ComboBox<>(days);
        dayCB.getStyleClass().add("specialComboBox");
        vbList.add(new VBox(dayLabel, dayCB));

        HBox hourIntervalLabel = makeLabel("INTERVALUL ORAR", true);
        TextField hourIntervalTextField = makeTextField(textFieldValues.get(1));
        vbList.add(new VBox(hourIntervalLabel, hourIntervalTextField));

        FlowPane fp = getFlowPane(vbList);

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
                currentTime.setId(CreatePane.timetable.getTimes().getTimes().size()+1);

                int dayNo = 1;

                for(Time t : CreatePane.timetable.getTimes().getTimes()){
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

                CreatePane.timetable.getTimes().getTimes().add(currentTime);
                if(saveIntoFile()){ //The save button was pressed, the file to save into was chosen
                    currentTime = new Time();
                }
                else {
                    CreatePane.timetable.getTimes().getTimes().remove(currentTime);
                }

            }

            //Adding the table with the existing times
            if(CreatePane.timetable.getTimes().getTimes().size()>0) {
               dealWithTable(finalVBox);
            }

        } );

        addSaveButtonIntoHBox(saveButton);

        finalVBox = new VBox(getTitleLabel("Adaugă interval orar"), fp, createExplanatory(), saveButtonHB);
        finalVBox.getStyleClass().add("rightVBox");

        //Adding the table with the existing times
        if(CreatePane.timetable.getTimes()!= null && CreatePane.timetable.getTimes().getTimes() != null && CreatePane.timetable.getTimes().getTimes().size()>0) {
            addTable(finalVBox);
        }

        return finalVBox;
    }

    protected VBox createTable(){
        Label existingLabel = new Label("Intervale orare");
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
        idColumn.setCellValueFactory(new PropertyValueFactory<Time, Integer>("id"));
        idColumn.setMaxWidth(50);
        idColumn.setPrefWidth(50);
        idColumn.setMinWidth(50);
        idColumn.getStyleClass().add("firstColumn");

        TableColumn nameColumn = new TableColumn("Nume");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Time, String>("name"));

        TableColumn dayColumn = new TableColumn("Zi");
        dayColumn.setCellValueFactory(new PropertyValueFactory<Time, String>("day"));

        TableColumn hourIntervalColumn = new TableColumn("Interval");
        hourIntervalColumn.setCellValueFactory(new PropertyValueFactory<Time, String>("hourInterval"));

        TableColumn deleteColumn = new TableColumn();
        Callback<TableColumn<Time, String>, TableCell<Time, String>> cellFactory =
                new Callback<TableColumn<Time, String>, TableCell<Time, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<Time, String> param )
                    {
                        final TableCell<Time, String> cell = new TableCell<Time, String>()
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
                                        Time time = getTableView().getItems().get(getIndex());

                                        int indexOfTime = CreatePane.timetable.getTimes().getTimes().indexOf(time);
                                        CreatePane.timetable.getTimes().getTimes().remove(time);

                                        //Resetting the ids
                                        if(CreatePane.timetable.getTimes().getTimes().size()>1) {
                                            for (Time t : CreatePane.timetable.getTimes().getTimes().subList(indexOfTime, CreatePane.timetable.getTimes().getTimes().size())) {
                                                t.setId(t.getId() - 1);
                                            }
                                        }
                                        else{
                                            if(CreatePane.timetable.getTimes().getTimes().size() == 1) {
                                                CreatePane.timetable.getTimes().getTimes().get(0).setId(1);
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

        table.getColumns().addAll(idColumn, nameColumn, dayColumn, hourIntervalColumn, deleteColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        updateTableData();

        table.setFixedCellSize(35);
        table.setPrefHeight((table.getFixedCellSize()+0.8) * (table.getItems().size()+1));

        VBox vb = new VBox(nameBox, table);
        vb.setId("table");

        return vb;
    }

    protected void updateTableData(){
        ObservableList<Time> data = FXCollections.observableArrayList(CreatePane.timetable.getTimes().getTimes());
        table.setItems(data);
        table.setPrefHeight((table.getFixedCellSize()+0.8) * (table.getItems().size()+1));
    }
}
