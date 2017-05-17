package view.panes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.Timetable;
import model.event.Event;
import model.resource.Resource;
import model.solution.Solution;
import model.solution.Solutions;
import view.panes.display.TimetableGrid;
import java.io.File;
import java.util.*;

/**
 * Created by Anca on 3/16/2017.
 */
public class DisplayPane extends MainPane {
    private File file;
    private Timetable timetable;
    private ObservableList<VBox> optionsList = FXCollections.observableArrayList();
    private TimetableGrid timetableGrid;
    private GridPane centerGrid = new GridPane();
    private ScrollPane rightScrollPane;
    private ComboBox solutionsComboBox;

    //Maps with key = the resource name and value = an ArrayList with all the events linked to the key resource
    private Map<String, List<Event>> studyGroupEvents = new HashMap<>();
    private Map<String, List<Event>> teacherEvents = new HashMap<>();
    private Map<String, List<Event>> classroomEvents = new HashMap<>();

    private Map<String, String> teacherIdName = new HashMap<>();

    VBox v1, v2, v3;
    ListView<String> listView;

    public DisplayPane(){

    }

    public DisplayPane(Map<String, Timetable> idTimetableMap, File file, Timetable timetable){
        this.timetable = timetable;
        this.file = file;
        this.timetableGrid = new TimetableGrid();

        addToolbar(idTimetableMap);
    }

    private void addToolbar(Map<String, Timetable> idTimetableMap){
        //Adding the left part of the toolBox
        HBox leftBox = new HBox();
        leftBox.getChildren().addAll(createHomeButton(), createOtherTimetablesBox(idTimetableMap));

        HBox rightBox = createRightToolBox();

        ToolBar tb = new ToolBar();
        tb.getItems().addAll(leftBox, rightBox);
        tb.getStyleClass().add("toolBar");

        this.setTop(tb);
    }

    private HBox createOtherTimetablesBox(Map<String, Timetable> idTimetableMap){
        Label timetableLabel = new Label("Orar");
        timetableLabel.getStyleClass().add("timetableLabel");

        ObservableList<String> timetableList = FXCollections.observableArrayList();
        timetableList.addAll(idTimetableMap.keySet());

        ComboBox timetables = new ComboBox(timetableList);
        timetables.getStyleClass().add("timetablesCombo");
        timetables.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                timetable = idTimetableMap.get(newValue);

                studyGroupEvents.clear();
                teacherEvents.clear();
                classroomEvents.clear();

                optionsList.clear();

                Solutions solutions = timetable.getSolutions();

                Map<String, Solution> idSolutionMap = new HashMap<>();

                for (Solution s : solutions.getSolutions()) {
                    idSolutionMap.put(s.getId(), s);
                }

                addLeftPaneSolutions(solutions.getSolutions().get(0), idSolutionMap);
                clearRightPane();
            }
        });
        timetables.getSelectionModel().select(timetable.getId());

        HBox t = new HBox(timetableLabel, timetables);
        t.getStyleClass().add("otherTimetables");
        t.setAlignment(Pos.CENTER_LEFT);

        return t;
    }

    private void addLeftPaneSolutions(Solution sol, Map<String, Solution> idSolutionMap) {
        //Adding all the solution names available for the file received
        ObservableList<String> solutionList = FXCollections.observableArrayList(idSolutionMap.keySet());
        solutionsComboBox = new ComboBox(solutionList);
        solutionsComboBox.getStyleClass().add("chooseLeft");

        solutionsComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String oldValue, String newValue) {
                studyGroupEvents.clear();
                teacherEvents.clear();
                classroomEvents.clear();

                optionsList.clear();

                addLeftPaneButtons(idSolutionMap.get(newValue));

                unselectItems(listView, v1, v2, v3);
                clearRightPane();
            }
        });

        solutionsComboBox.getSelectionModel().select(sol.getId());
    }

    private void clearRightPane(){
        removeRightScrollPane();
        centerGrid.add(new VBox(), 1, 0);
        this.setCenter(centerGrid);
    }

    private VBox createGeneralOptionBox(){
        Label generalLabel = new Label("Orare generale");
        generalLabel.getStyleClass().add("leftLabel");

        listView = new ListView<>();
//        listView.setPrefSize(150, 195);

        final ObservableList items = FXCollections.observableArrayList("Clase", "Profesori", "Săli de clasă");
        listView.getItems().addAll(items);

        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String oldVal,
                 String newVal) -> {
                    //Getting the resource type
                    String resourceType = null;
                    Map<String, List<Event>> map = new HashMap<>();

                    if(newVal!=null) {
                        switch (newVal) {
                            case "Clase":
                                resourceType = "studyGroup";
                                map = studyGroupEvents;
                                break;
                            case "Profesori":
                                resourceType = "teacher";
                                map = teacherEvents;
                                break;
                            case "Săli de clasă":
                                resourceType = "classroom";
                                map = classroomEvents;
                                break;
                            default:
                                break;
                        }

                        //Displaying the table for the selected resource
                        addRightPaneGeneralCase(map, resourceType);
                        unselectItems(new ListView<>(), v1, v2, v3);
                    }
                });

        listView.getStyleClass().add("generalListView");

        VBox vBox  = new VBox(generalLabel, listView);
        vBox.getStyleClass().add("generalVBox");

        return vBox;
    }

    public void addRightPaneGeneralCase(Map<String, List<Event>> map, String resourceType){
        removeRightScrollPane();
        rightScrollPane = timetableGrid.addRightPaneGeneralCase(this, file, timetable, map, resourceType);
        setCenterTable();
    }

    private void removeRightScrollPane(){
        centerGrid.getChildren().remove(rightScrollPane);
    }

    private void setCenterTable(){
        centerGrid.add(rightScrollPane, 1, 0);
        GridPane.setVgrow(rightScrollPane, Priority.ALWAYS);
        GridPane.setHgrow(rightScrollPane, Priority.ALWAYS);

        this.setCenter(centerGrid);
    }

    private void addLeftPaneButtons(Solution sol){
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
                pList.add(r.getName());
            }
            if(r.getResourceType().equals("classroom")){
                sList.add(r.getId());
            }
        }

        //Finding the solution object that has been selected from toolbar
        Solution solution = new Solution();
        for(Solution s: timetable.getSolutions().getSolutions()){
            if(s.getId().equals(sol.getId())){
                solution = s;
                break;
            }
        }

        //Maps with key = the resource name and value = an ArrayList with all the events linked to the key resource
        //Initializing the maps with the corresponding values
        initializeEventsMap(studyGroupEvents, solution, "studyGroup", timetable);
        initializeEventsMap(teacherEvents, solution, "teacher", timetable);
        initializeEventsMap(classroomEvents, solution, "classroom", timetable);

        //Adding the separate comboBoxes to choose from to the VBox to be included in the left pane
        v1 = getOption(new Label("Clase"), cList, "studyGroup", studyGroupEvents,"Selectează clasa");
        v2 = getOption(new Label("Profesori"), pList, "teacher", teacherEvents,"Selectează profesorul");
        v3 = getOption(new Label("Săli de clasă"), sList, "classroom", classroomEvents,"Selectează sala");
        optionsList.clear();
        optionsList.addAll(v1, v2, v3);

        addLeftButtons(sol);
    }

    private void addLeftButtons(Solution sol){
        Label sLabel = new Label("Soluție");
        sLabel.getStyleClass().add("leftLabel");

        Label details = new Label();
        details.setText("Detalii");
        details.addEventFilter(MouseEvent.MOUSE_CLICKED, new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
        details.getStyleClass().add("detailsLabel");

        HBox detailsBox = new HBox(details);
        HBox.setHgrow(detailsBox, Priority.ALWAYS );
        detailsBox.setAlignment(Pos.CENTER_RIGHT );
        detailsBox.getStyleClass().add("detailsBox");

        Label scoreLabel = new Label("Scor");
        scoreLabel.getStyleClass().add("scoreLabel");

        int solutionScore = sol.getReport() != null? sol.getReport().getInfeasibilityValue() : 88;

        Label scoreNumber = new Label(String.valueOf(solutionScore));
        scoreNumber.getStyleClass().add("scoreNumber");

        HBox rightScoreBox = new HBox(scoreNumber);
        HBox.setHgrow(rightScoreBox, Priority.ALWAYS );
        rightScoreBox.setAlignment(Pos.CENTER_RIGHT );

        HBox scoreBox = new HBox(scoreLabel, rightScoreBox);
        scoreBox.getStyleClass().add("scoreBox");

        VBox smallVBox = new VBox(new HBox(sLabel, detailsBox), solutionsComboBox, scoreBox);
        smallVBox.getStyleClass().add("optionVBox");

        VBox vBox = new VBox(smallVBox);
        vBox.getChildren().addAll(optionsList);
        vBox.getChildren().add(createGeneralOptionBox());
        vBox.getStyleClass().add("leftScreen");

        optionsList.clear();

        centerGrid.add(vBox, 0, 0);
        GridPane.setVgrow(vBox, Priority.ALWAYS);

        this.setCenter(centerGrid);
    }

    public static void initializeEventsMap(Map<String, List<Event>> map, Solution solution, String resourceType, Timetable timetable){
        for(Resource r: timetable.getResources().getResources()){
            if(r.getResourceType().equals(resourceType)){
                if(resourceType.equals("teacher")){
                    //If there are two teachers with the same name we add their id's
                    if(map.get(r.getName()) != null){
                        map.remove(r.getName());
                        map.put(r.getName()+" "+r.getId(), new ArrayList<>());

                        for(Resource teacher: timetable.getResources().getResources()){
                            if(teacher.getResourceType().equals("teacher") && teacher.getName().equals(r.getName()) && !teacher.getId().equals(r.getId())){
                                //We found the other teacher with the same name as the current one
                                map.put(teacher.getName()+" "+r.getId(), new ArrayList<>());
                            }
                        }

                    }
                    else {
                        map.put(r.getName(), new ArrayList<>());
                    }
                }
                else {
                    map.put(r.getId(), new ArrayList<>());
                }
            }
        }

        for(Event e: solution.getEvents().getEvents()){
            for(Resource r : e.getResources().getResources()){
                if(r.getResourceType().equals(resourceType)) {
                    List<Event> subPart;
                    if(resourceType.equals("teacher")){
                        String id = map.get(r.getName()) != null? r.getName() : new String(r.getName()+" "+r.getId());
                        subPart = map.get(id);
                        subPart.add(e);
                        map.put(id, subPart);
                    }
                    else {
                        subPart = map.get(r.getId());
                        subPart.add(e);
                        map.put(r.getId(), subPart);
                    }
                }
            }
        }
    }

    private VBox getOption(Label label, ObservableList<String> list, String resourceType, Map<String, List<Event>> map, String placeholder){
        ComboBox cbList = new ComboBox(list);

        cbList.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                //Displaying the table for the selected resource
                if(newValue != null) {
                    addRightPane(newValue, resourceType, map.get(newValue));
                }
            }
        });

        cbList.setPromptText(placeholder);

        label.getStyleClass().add("leftLabel");
        cbList.getStyleClass().add("chooseLeft");

        VBox vBox = new VBox(label, cbList);
        vBox.getStyleClass().add("optionVBox");
        vBox.setId(resourceType);
        vBox.addEventFilter(MouseEvent.MOUSE_CLICKED, new javafx.event.EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(!vBox.getStyleClass().contains("selectedVBox")) {
                        vBox.getStyleClass().add("selectedVBox");
                    }
                    if(vBox.getId().equals(v1.getId())){
                        unselectItems(listView, v2, v3);
                    }
                    else{
                        if(vBox.getId().equals(v2.getId())){
                            unselectItems(listView, v1, v3);
                        }
                        else{
                            if(vBox.getId().equals(v3.getId())) {
                                unselectItems(listView, v1, v2);
                            }
                        }
                    }

                }
        });

        cbList.addEventFilter(MouseEvent.MOUSE_CLICKED, new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(vBox.getId().equals(v1.getId())){
                    unselectItems(listView, v2, v3);
                }
                else{
                    if(vBox.getId().equals(v2.getId())){
                        unselectItems(listView, v1, v3);
                    }
                    else{
                        if(vBox.getId().equals(v3.getId())) {
                            unselectItems(listView, v1, v2);
                        }
                    }
                }

            }
        });

        return vBox;
    }

    private void addRightPane(String resource, String resourceType, List<Event> eventList){
        removeRightScrollPane();
        rightScrollPane = timetableGrid.addRightPane(timetable, resource, resourceType, eventList);
        setCenterTable();
    }

    private void unselectItems(ListView<String> listView, VBox... elements){
        if(listView != null && listView.getSelectionModel().getSelectedItems().size() > 0) {
            listView.getSelectionModel().clearSelection();
        }
        for(VBox v: elements){
            if(v!=null) {
                if (v.getStyleClass().contains("selectedVBox")) {
                    v.getStyleClass().remove("selectedVBox");
                }

                for (Node n : v.getChildren()) {
                    if (n.getClass().getName().contains("ComboBox")) {
                        ComboBox cb = (ComboBox) n;
                        if (cb.getSelectionModel().getSelectedItem() != null) {
                            cb.getSelectionModel().clearSelection();
                        }
                    }
                }
            }
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
