package view.panes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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

    //Maps with key = the resource name and value = an ArrayList with all the events linked to the key resource
    private Map<String, List<Event>> studyGroupEvents = new HashMap<>();
    private Map<String, List<Event>> teacherEvents = new HashMap<>();
    private Map<String, List<Event>> classroomEvents = new HashMap<>();

    public DisplayPane(){

    }

    public DisplayPane(Map<String, Timetable> idTimetableMap, File file, Timetable timetable){
        this.timetable = timetable;
        this.file = file;

        addToolbar(idTimetableMap);
    }

    private void addToolbar(Map<String, Timetable> idTimetableMap){
        Label timetable = new Label("Orar");
        timetable.getStyleClass().add("timetableLabel");

        ObservableList<String> timetableList = FXCollections.observableArrayList();
        timetableList.addAll(idTimetableMap.keySet());

        ComboBox timetables = new ComboBox(timetableList);
        timetables.getStyleClass().add("timetablesCombo");

        HBox t = new HBox(timetable, timetables);
        t.getStyleClass().add("otherTimetables");

        HBox leftBox = new HBox();
        leftBox.getChildren().addAll(getHomeButton(), t);

        ToolBar tb = new ToolBar();
        tb.getItems().add(leftBox);
        tb.getStyleClass().add("toolBar");

        this.setTop(tb);
    }


    private void addToToolbar(List<File> fileList){
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
//            toolBar.getItems().addAll(addToolbar(true).getItems());
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
        //List of general items
        ObservableList<String> gList = FXCollections.observableArrayList(Arrays.asList("Clase", "Profesori", "Săli de clasă"));

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

        //Maps with key = the resource name and value = an ArrayList with all the events linked to the key resource
        //Initializing the maps with the corresponding values
        initializeEventsMap(studyGroupEvents, solution, "studyGroup");
        initializeEventsMap(teacherEvents, solution, "teacher");
        initializeEventsMap(classroomEvents, solution, "classroom");

        //Adding the separate comboBoxes to choose from to the VBox to be included in the left pane
        VBox vBox = addComboBoxToChooseFrom(new VBox(), new Text("Clase"), cList, "studyGroup", studyGroupEvents);
        vBox = addComboBoxToChooseFrom(vBox, new Text("Profesori"), pList, "teacher", teacherEvents);
        vBox = addComboBoxToChooseFrom(vBox, new Text("Săli de clasă"), sList, "classroom", classroomEvents);
        vBox = addComboBoxToChooseFrom(vBox, new Text("Orare generale"), gList, "general", null);

        //Styling
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setSpacing(8);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        this.setLeft(vBox);
    }

    private void initializeEventsMap(Map<String, List<Event>> map, Solution solution, String resourceType){
        for(Event e: solution.getEvents().getEvents()){
            for(Resource r : e.getResources().getResources()){
                if(r.getResourceType().equals(resourceType)) {
                    List<Event> subPart = map.get(r.getId());
                    if (subPart == null) {
                        subPart = new ArrayList<>();
                    }
                    subPart.add(e);
                    map.put(r.getId(), subPart);
                }
            }
        }
    }

    private VBox addComboBoxToChooseFrom(VBox vBox, Text text, ObservableList<String> list, String resourceType, Map<String, List<Event>> map){
        ComboBox cbList = new ComboBox(list);
        cbList.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                //Displaying the table for the selected resource
                if(resourceType.equals("general")){
                    chooseGeneralCaseResources(newValue);
                }
                else {
                    addRightPane(newValue, resourceType, map.get(newValue));
                }
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
        eventList = sortEventListByTime(eventList);

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

            String hourLabel = getHourLabel(e, resourceType, false);

            int col = timetableDays.get(e.getTime().getDay())+1;
            int row = Integer.valueOf(e.getTime().getName().split("_")[0]);

            if(row>maxRow){
                maxRow = row;
                maxTime = e.getTime();
            }

            pane.add(new Text(hourLabel), col, row);
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

        ColumnConstraints firstColumnSize = new ColumnConstraints();
        firstColumnSize.setMinWidth(70);
        firstColumnSize.setHalignment(HPos.CENTER);
        pane.getColumnConstraints().add(firstColumnSize);

        ColumnConstraints columnSize = new ColumnConstraints();
        columnSize.setPercentWidth(100.0/days.size());
        columnSize.setHalignment(HPos.CENTER);
        for(i = 1; i <= days.size(); i++){
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

    private List<Event> sortEventListByTime(List<Event> eventList){
        //Sorting the events for the selected resource by their time
        Collections.sort(eventList, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                Time t1 = e1.getTime();
                Time t2 = e2.getTime();
                return t1.getId() - t2.getId();
            }
        });

        return eventList;
    }

    private String getHourLabel(Event e, String resourceType, boolean onlyId){
        StringBuilder sb = new StringBuilder(e.getDescription());
        for(Resource r: e.getResources().getResources()){
            if(!r.getResourceType().equals(resourceType)) {
                if(!onlyId && !r.getName().isEmpty()) {
                    sb.append("\n" + r.getName());
                }
                else{
                    sb.append("\n" + r.getId());
                }
            }
        }
        return sb.toString();
    }

    private void chooseGeneralCaseResources(String resource){
        switch (resource){
            case "Clase":
                addRightPaneGeneralCase(studyGroupEvents, "studyGroup");
                break;
            case "Profesori":
                addRightPaneGeneralCase(teacherEvents, "teacher");
                break;
            case "Săli de clasă":
                addRightPaneGeneralCase(classroomEvents, "classroom");
                break;
            default:
                break;
        }
    }

    public void addRightPaneGeneralCase(Map<String, List<Event>> map, String resourceType){
        GridPane pane = new GridPane();

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

        //Sorting the map keys by their id
        Collection<String> unsorted = map.keySet();
        List<String> list = new ArrayList<>(unsorted);
        Collections.sort(list);

        //Finding out how many hour intervals there can be in a day
        int maxCounter = 0;
        int counter = 0;
        String day = days.get(0);
        for(Time t: timetable.getTimes().getTimes()){
            if(t.getDay().equals(day)){
                counter++;
            }
            else{
                day = t.getDay();
                if(maxCounter<counter){
                    maxCounter = counter;
                }
                counter = 0;
            }
        }

        int line = 1;
        for(String s: list){
            //Adding the first column of the table
            pane.add(new Text(s), 0, line);

            List<Event> resourceEvents = map.get(s);
            resourceEvents = sortEventListByTime(resourceEvents);

            //Adding the rest of the timetable for each resource
            day = days.get(0);
            GridPane dayPane = new GridPane();
            int col = 0;
            for(Event e: resourceEvents){
                if(!day.equals(e.getTime().getDay())){
                    day = e.getTime().getDay();

                    //Styling
                    dayPane = stylingDailyCells(dayPane, maxCounter);

                    col = timetableDays.get(e.getTime().getDay());
                    pane.add(dayPane, col, line);
                    dayPane = new GridPane();
                }

                String hourLabel = getHourLabel(e, resourceType, true);
                int windowCounter = Integer.valueOf(e.getTime().getName().split("_")[0])-1;

                dayPane.add(new Text(hourLabel), windowCounter,0);
            }

            //Styling
            dayPane = stylingDailyCells(dayPane, maxCounter);

            pane.add(dayPane, ++col, line);

            int dayIndex = days.indexOf(day);
            while(dayIndex != days.size() - 1){
                GridPane empty = createEmptyDayCell(maxCounter);
                pane.add(empty, ++col, line);
                dayIndex++;
            }
            line++;
        }

        //Styling
        pane.setVgap(5);
        pane.setHgap(5);
        pane.setPadding(new Insets(10,10,10,10));


        ColumnConstraints firstColumnSize = new ColumnConstraints();
        firstColumnSize.setMinWidth(30);
        firstColumnSize.setHalignment(HPos.CENTER);
        pane.getColumnConstraints().add(firstColumnSize);

        ColumnConstraints columnSize = new ColumnConstraints();
        columnSize.setPercentWidth(100.0/days.size());
        columnSize.setHalignment(HPos.CENTER);
        for(i = 1; i <= days.size(); i++){
            pane.getColumnConstraints().add(columnSize);
        }

        pane.setGridLinesVisible(true);

        this.setCenter(pane);
    }

    private GridPane createEmptyDayCell(int maxCounter){
        GridPane empty = new GridPane();

        for(int i = 0; i < maxCounter; i++) {
            empty.add(new Text(" \n\n "), i, 0);
        }

        empty = stylingDailyCells(empty, maxCounter);

        return empty;
    }

    private GridPane stylingDailyCells(GridPane dayPane, int maxCounter){
        dayPane.setHgap(2);
        ColumnConstraints columnSize = new ColumnConstraints();
        columnSize.setPercentWidth(100.0/maxCounter);
        columnSize.setHalignment(HPos.CENTER);

        for(int i = 0; i < maxCounter; i++){
            dayPane.getColumnConstraints().add(columnSize);
        }

        dayPane.setGridLinesVisible(true);

        return dayPane;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
