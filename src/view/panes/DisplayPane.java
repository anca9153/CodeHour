package view.panes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Timetable;
import model.event.Event;
import model.resource.Resource;
import model.solution.Solution;
import model.solution.Solutions;
import model.time.Time;
import utilities.DataLoader;
import javafx.scene.control.ToolBar;
import utilities.PropertiesLoader;
import java.io.File;
import java.util.*;

/**
 * Created by Anca on 3/16/2017.
 */
public class DisplayPane extends MainPane {
    private File file;
    private Timetable timetable;

    //Map with key = the resource name and value = an ArrayList with all the events linked to the key resource
    private Map<String, List<Event>> parts = new HashMap<>();


    public DisplayPane(){

    }

    public DisplayPane(List<File> fileList, File file){
        this.file = file;

        //Loading the timetable chosen in the HomePane by the location of the XML document it is placed in
        if(PropertiesLoader.loadXMLLocationFolder().equals(new String(file.getParent()+"\\"))){
            timetable = DataLoader.loadDataFromXML(file.getName());
        }
        else{
            timetable = DataLoader.loadDataFromXMLWithPath(file.getAbsolutePath());
        }

        addToToolbar(fileList);
    }

    private void addToToolbar(List<File> fileList){
        //Adding all the timetables available


        //Adding all the solution names available for the file received
        Solutions solutions = timetable.getSolutions();

        ObservableList<String> solutionList = FXCollections.observableArrayList();
        if(solutions!=null && solutions.getSolutions()!=null) {
            for (Solution s : solutions.getSolutions()) {
                solutionList.add(s.getId());
            }

            ComboBox cb = new ComboBox(solutionList);

            cb.valueProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                    addLeftPane(newValue);
                }
            });

            cb.getSelectionModel().selectFirst();

            HBox leftSection = new HBox();
//            Text orarText = new Text("Orar");
            Text solText = new Text("Soluție");
            leftSection.getChildren().addAll(solText, cb);
            HBox.setHgrow(leftSection, Priority.ALWAYS);
            leftSection.setAlignment(Pos.CENTER_LEFT);

            leftSection.setSpacing(8);

            ToolBar toolBar = new ToolBar();
            toolBar.getItems().add(leftSection);
            toolBar.getItems().addAll(addToolbar(true).getItems());
            this.setTop(toolBar);
        }
        else{
            this.setCenter(new Text("This document has no solution yet."));
        }
    }

    private void addLeftPane(String solutionName){
        //The resources for which the timetable can be displayed
        //List of studyGroups (clase)
        ObservableList<String> cList = FXCollections.observableArrayList();
        //List of teachers (profesori)
        ObservableList<String> pList = FXCollections.observableArrayList();
        //List of classRooms (sali de clasa)
        ObservableList<String> sList = FXCollections.observableArrayList();

        for(Resource r: timetable.getResources().getResources()){
            if(r.getResourceType().equals("studyGroup")){
                cList.add(r.getId());
            }
            if(r.getResourceType().equals("teacher")){
                pList.add(r.getId());
            }
            if(r.getResourceType().equals("classroom")){
                sList.add(r.getId());
            }
        }

        //Finding the solution object that has been selected from toolbar
        Solution solution = new Solution();
        for(Solution s: timetable.getSolutions().getSolutions()){
            if(s.getId().equals(solutionName)){
                solution = s;
                break;
            }
        }

        //parts is a map with key = the resource name and value = an ArrayList with all the events linked to the key resource
        //Initializing the parts map with the corresponding values
        for(Event e: solution.getEvents().getEvents()){
            for(Resource r : e.getResources().getResources()){
                List<Event> subPart = parts.get(r.getId());
                if(subPart == null){
                    subPart = new ArrayList<>();
                }
                subPart.add(e);
                parts.put(r.getId(), subPart);
            }
        }

        //Adding the separate comboBoxes to choose from to the VBox to be included in the left pane
        VBox vBox = addComboBoxToChooseFrom(new VBox(), new Text("Clase"), cList, "studyGroup");
        vBox = addComboBoxToChooseFrom(vBox, new Text("Profesori"), pList, "teacher");
        vBox = addComboBoxToChooseFrom(vBox, new Text("Săli de clasă"), sList, "classroom");

        //Styling
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setSpacing(8);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        this.setLeft(vBox);
    }

    private VBox addComboBoxToChooseFrom(VBox vBox, Text text, ObservableList<String> list, String resourceType){
        ComboBox cbList = new ComboBox(list);
        cbList.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                //Displaying the table for the selected resource
                addRightPane(newValue, resourceType, parts.get(newValue));
            }
        });

        vBox.getChildren().addAll(text, cbList);
        return vBox;
    }

    private void addRightPane(String resource, String resourceType, List<Event> eventList){
        //The timetable for a selected resource, displayed on the right side
        VBox vBox = new VBox(new Text(resource));

        GridPane pane = new GridPane();

        //Sorting the events for the selected resource by their time
        Collections.sort(eventList, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                Time t1 = e1.getTime();
                Time t2 = e2.getTime();
                return t1.getId() - t2.getId();
            }
        });

        //List with the days displayed on the first row of the timetable
        List<String> days = new ArrayList<>(Arrays.asList("monday","tuesday","wednesday","thursday","friday"));

        Map<String, Integer> timetableDays = new HashMap<>();
        int dayCounter = 0;
        for(String s: days){
            timetableDays.put(s, dayCounter++);
        }

        //Adding the head of the table
        int i = 1;
        for(String s :days){
            pane.add(new Text(s), i++,0);
        }

        //Adding the rest of the timetable for the selected resource
        String day = " ";
        Time maxTime = null;
        int maxRow = 0;

        for(Event e: eventList){
            if(!day.equals(e.getTime().getDay())){
                day = e.getTime().getDay();
            }

            StringBuilder sb = new StringBuilder(e.getDescription());
            for(Resource r: e.getResources().getResources()){
                if(!r.getResourceType().equals(resourceType)) {
                    if(!r.getName().isEmpty()) {
                        sb.append("\n" + r.getName());
                    }
                    else{
                        sb.append("\n" + r.getId());
                    }
                }
            }

            int col = timetableDays.get(e.getTime().getDay())+1;
            int row = Integer.valueOf(e.getTime().getName().split("_")[0]);

            if(row>maxRow){
                maxRow = row;
                maxTime = e.getTime();
            }

            pane.add(new Text(sb.toString()), col, row);
        }

        //Adding the hour interval for the timetable
        int row = 1;
        for(Time t: timetable.getTimes().getTimes()){
            if(maxTime.getDay().equals(t.getDay()) && t.getId()<=maxTime.getId()) {
                pane.add(new Text(t.getHourInterval()), 0, row++);
            }
        }

        //Styling
        pane.setVgap(5);
        pane.setHgap(5);
        pane.setPadding(new Insets(10,10,10,10));

        ColumnConstraints columnSize = new ColumnConstraints();
        columnSize.setPercentWidth(100.0/days.size());
        columnSize.setHalignment(HPos.CENTER);
        for(i = 0; i <= days.size(); i++){
            pane.getColumnConstraints().add(columnSize);
        }

        RowConstraints rowSize = new RowConstraints();
        rowSize.setPrefHeight(50.0);
        rowSize.setValignment(VPos.CENTER);
        for(i = 0; i < row; i++){
            pane.getRowConstraints().add(rowSize);
        }

        pane.setGridLinesVisible(true);
        vBox.getChildren().add(pane);

        this.setCenter(vBox);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
