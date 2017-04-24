package view.panes;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Metadata;
import model.Timetable;
import view.panes.create.InsertGeneralSettings;
import view.panes.create.InsertTime;
import java.io.File;
import java.util.*;

/**
 * Created by Anca on 3/16/2017.
 */
public class CreatePane extends MainPane {

    private Stage primaryStage;
    private Timetable timetable;
    private ObservableList<ListView> listViewList = FXCollections.observableArrayList();
    public static File savingFile;

    public CreatePane(Stage primaryStage, Timetable timetable){
        this.primaryStage = primaryStage;
        this.timetable = timetable;

        addToolbar();
        clearSelections(" ");
        addLeftOptions();

        timetable.setMetadata(new Metadata());

        timetable.getMetadata().setDate(new Date());
    }

    private void addToolbar(){
        //Clearing the listview list
        listViewList.clear();

        //Adding the left part of the toolBox
        Label createLabel = new Label("Creare orar");
        createLabel.getStyleClass().add("timetableLabel");

        HBox t = new HBox(createLabel);
        t.getStyleClass().add("otherTimetables");
        t.setAlignment(Pos.CENTER_LEFT);

        HBox leftBox = new HBox();
        leftBox.getChildren().addAll(createHomeButton(), t);

        HBox rightBox = createRightToolBox();

        ToolBar tb = new ToolBar();
        tb.getItems().addAll(leftBox, rightBox);
        tb.getStyleClass().add("toolBar");

        this.setTop(tb);
    }

    private void addLeftOptions(){
        VBox vBoxDetails = createLeftOption("Detalii orar", "Metadata");
        VBox vBoxTimes = createLeftOption("Intervale orare", "Adaugă intervale");
        VBox vBoxResources = createLeftOption("Resurse", "Clase", "Profesori", "Săli de clasă");
        VBox vBoxEvents = createLeftOption("Eveniment", "Adaugă evenimente");
        VBox vBoxConstraints = createLeftOption("Constrângeri", "Pentru evenimente", "Pentru resurse");

        VBox vBox = new VBox(vBoxDetails, vBoxTimes, vBoxResources, vBoxEvents, vBoxConstraints);
        vBox.getStyleClass().add("leftScreen");

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("edge-to-edge");

        this.setLeft(scrollPane);
    }

    private VBox createLeftOption(String labelString, String... buttonStringList){
        VBox vBox = new VBox();

        Label label = new Label(labelString);
        label.getStyleClass().add("leftLabel");

        ListView<String> listView = new ListView<>();
        listView.setId(labelString);

        final ObservableList items = FXCollections.observableArrayList(buttonStringList);
        listView.getItems().addAll(items);
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val,
                 String new_val) -> {
                    if(new_val != null) {
                        clearSelections(labelString);
                        addRightCreatePane(new_val);
                    }
                });

        listView.getStyleClass().add("generalListView");
        listView.setPrefHeight(44*buttonStringList.length);

        listViewList.add(listView);

        vBox.getChildren().addAll(label, listView);
        vBox.getStyleClass().add("optionVBox");

        return vBox;
    }

    private void clearSelections(String leaveOut){
        for(ListView l : listViewList){
            if(!l.getId().equals(leaveOut) && l.getSelectionModel().getSelectedItems().size() > 0) {
                l.getSelectionModel().clearSelection();
            }
        }
    }

    private void addRightCreatePane(String rightPaneName){
        switch(rightPaneName){
            case "Metadata":
                InsertGeneralSettings rightPane1 = new InsertGeneralSettings(timetable, primaryStage);
                this.setCenter(rightPane1.addRightPane());
                break;
            case "Adaugă intervale":
                InsertTime rightPane2 = new InsertTime(timetable, primaryStage);
                this.setCenter(rightPane2.addRightPane());
            default:
                break;
        }
    }

}
