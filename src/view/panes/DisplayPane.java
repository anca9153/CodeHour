package view.panes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
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

import javax.lang.model.element.Element;
import java.io.File;
import java.util.*;

/**
 * Created by Anca on 3/16/2017.
 */
public class DisplayPane extends MainPane {
    private File file;
    private Timetable timetable;

    public DisplayPane(){

    }

    public DisplayPane(List<File> fileList, File file){
        this.file = file;

        if(PropertiesLoader.loadXMLLocationFolder().equals(new String(file.getParent()+"\\"))){
            timetable = DataLoader.loadDataFromXML(file.getName());
        }
        else{
            timetable = DataLoader.loadDataFromXMLWithPath(file.getAbsolutePath());
        }

        addToToolbar(fileList);
//
//        HBox box = new HBox();
//
//        this.setCenter(box);
    }

    private void addToToolbar(List<File> fileList){
        ToolBar toolBar = new ToolBar();

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

            toolBar.getItems().add(leftSection);
            toolBar.getItems().addAll(addToolbar(true).getItems());
        }

        this.setTop(toolBar);
    }

    private void addLeftPane(String solutionName){
        //The resources for which the timetable can be displayed
        //This resource lists are on the left side
        ObservableList<String> cList = FXCollections.observableArrayList();
        ObservableList<String> pList = FXCollections.observableArrayList();
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

        Map<String, List<Event>> parts = new HashMap<>();

        Solution solution = new Solution();
        for(Solution s: timetable.getSolutions().getSolutions()){
            if(s.getId().equals(solutionName)){
                solution = s;
                break;
            }
        }

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

        Text clase = new Text("Clase");

        ComboBox cbClase = new ComboBox(cList);
        cbClase.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
               //Displaying the table for the selected resource
                addRightPane(newValue, parts.get(newValue));
            }
        });
        cbClase.getSelectionModel().selectFirst();

        Text profesori = new Text("Profesori");

        ComboBox cbProfesori= new ComboBox(pList);
//        cbClase.getSelectionModel().selectFirst();

        Text sali = new Text("Săli de clasă");

        ComboBox cbSali = new ComboBox(sList);
//        cbClase.getSelectionModel().selectFirst();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(clase, cbClase, profesori, cbProfesori, sali, cbSali);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        this.setLeft(vbox);
    }

    private void addRightPane(String resource,List<Event> eventList){
        //The timetable for a selected resource, displayed on the right side
        GridPane pane = new GridPane();
        Collections.sort(eventList, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                Time t1 = e1.getTime();
                Time t2 = e2.getTime();
                return t1.getId() - t2.getId();
            }
        });

        Map<String, Integer> weekdays = new HashMap<String, Integer>();
        weekdays.put("monday", 1);
        weekdays.put("tuesday",2);
        weekdays.put("wednesday",3);
        weekdays.put("thursday",4);
        weekdays.put("friday",5);

        String day = " ";
        for(Event e: eventList){
            if(!day.equals(e.getTime().getDay())){
                day = e.getTime().getDay();
                ColumnConstraints column = new ColumnConstraints(100);
                pane.getColumnConstraints().add(column);
            }

            StringBuilder sb = new StringBuilder(e.getTime().getName()+" ");
            for(Resource r: e.getResources().getResources()){
                sb.append(r.getId()+" ");
            }

            pane.add(new Text(sb.toString()), weekdays.get(e.getTime().getDay()), Integer.valueOf(e.getTime().getName().split("_")[0]));
        }

        this.setCenter(pane);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
