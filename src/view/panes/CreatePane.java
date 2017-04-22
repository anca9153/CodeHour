package view.panes;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Anca on 3/16/2017.
 */
public class CreatePane extends MainPane {

    private GridPane centerGrid = new GridPane();
    private ObservableList<ListView> listViewList = FXCollections.observableArrayList();

    public CreatePane(){
        addToolbar();
        clearSelections(" ");
        addLeftOptions();
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
        VBox vBoxDetails = createLeftOption("Detalii orar", "Setări generale");
        VBox vBoxTimes = createLeftOption("Intervale orare", "Adaugă intervale");
        VBox vBoxResources = createLeftOption("Resurse", "Clase", "Profesori", "Săli de clasă");
        VBox vBoxEvents = createLeftOption("Eveniment", "Adaugă evenimente");
        VBox vBoxConstraints = createLeftOption("Constrângeri", "Pentru evenimente", "Pentru resurse");

        VBox vBox = new VBox(vBoxDetails, vBoxTimes, vBoxResources, vBoxEvents, vBoxConstraints);
        vBox.getStyleClass().add("leftScreen");

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scrollPaneTimetable");
        scrollPane.getStyleClass().add("edge-to-edge");


        centerGrid.add(scrollPane, 0, 0);
        GridPane.setVgrow(vBox, Priority.ALWAYS);

        this.setCenter(centerGrid);
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
                        addRightCreatePane();
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
        System.out.println();
    }

    private void addRightCreatePane(){

    }

}
