package view.panes.display;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import model.Timetable;
import model.event.Event;
import model.resource.Resource;
import model.time.Time;
import java.util.*;

/**
 * Created by Anca on 4/3/2017.
 */
public class TimetableGrid {
    private StackPane cellPane;
    private Event draggedEvent = new Event();

    public ScrollPane addRightPane(Timetable timetable, String resource, String resourceType, List<Event> eventList){
        //The timetable for a selected resource, displayed on the right side
        Text text1 = new Text(resource);
        text1.getStyleClass().add("resourceText");

        String labelString = new String();
        switch (resourceType){
            case ("studyGroup"):
                labelString = "Clasa";
                break;
            case ("teacher"):
                labelString = "Profesor";
                break;
            case("classroom"):
                labelString = "Sala";
        }

        Label text2 = new Label(labelString);
        text2.getStyleClass().add("resourceTypeText");

        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(text1, text2);
        nameBox.getStyleClass().add("titleNameBox");
        nameBox.setAlignment(Pos.BOTTOM_LEFT);
        VBox vBox = new VBox(nameBox);

        GridPane pane = new GridPane();

        //Sorting the events for the selected resource by their time
        eventList = sortEventListByTime(eventList);

        //List with the days displayed on the first row of the timetable
        List<String> days = new ArrayList<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday"));
        List<String> daysRO = new ArrayList<>(Arrays.asList("Luni", "Marți", "Miercuri", "Joi", "Vineri"));
        Map<String, Integer> timetableDays = new HashMap<>();
        int dayCounter = 0;
        for(String s: days){
            timetableDays.put(s, dayCounter++);
        }

        //Adding the head of the table
        Label l = new Label("Ora");
        l.getStyleClass().add("tableHeadLabel");
        StackPane cellPane = new StackPane(l);
        cellPane.getStyleClass().add("tableHeadCell");
        StackPane.setAlignment(l,Pos.CENTER);
        pane.add(cellPane, 0,0);

        int i = 1;
        for(String s :daysRO){
            l = new Label(s);
            l.getStyleClass().add("tableHeadLabel");
            cellPane = new StackPane(l);
            cellPane.getStyleClass().add("tableHeadCell");
            StackPane.setAlignment(l,Pos.CENTER);
            pane.add(cellPane, i++,0);
        }

        //Adding the rest of the timetable for the selected resource
        Time maxTime = null;
        int maxRow = 0;

        //Finding max number of rows to show
        for(Event e: eventList) {
            int row = Integer.valueOf(e.getTime().getName().split("_")[0]);
            if (row > maxRow) {
                maxRow = row;
                maxTime = e.getTime();
            }
        }

        //Adding the hour interval for the timetable
        Label lh = new Label();
        lh.getStyleClass().clear();
        lh.getStyleClass().add("tableHourLabel");

        int row = 1;

        for(Time t: timetable.getTimes().getTimes()){
            if(maxTime!=null && maxTime.getDay().equals(t.getDay())){
                lh = new Label(t.getHourInterval());
                StackPane hourCellPane = new StackPane(lh);
                hourCellPane.getStyleClass().add("tableContentCell");
                StackPane.setAlignment(lh,Pos.CENTER);
                pane.add(hourCellPane, 0, row++);
            }
        }

        int maxTimeInterval = row;

        //Innitializing the table with empty cells
        String day = " ";
        row = 0;
        int column = 0;

        for(int c=1; c<=days.size(); c++){
            for(int r=1; r<maxTimeInterval; r++){
                cellPane = new StackPane();
                cellPane.getStyleClass().add("tableContentCell");
                pane.add(cellPane, c, r);
            }
        }

        for(Event e: eventList){
            if(!day.equals(e.getTime().getDay())){
                day = e.getTime().getDay();
            }

            VBox lab = getHourLabel(e, resourceType, false);

            column = timetableDays.get(e.getTime().getDay())+1;
            row = Integer.valueOf(e.getTime().getName().split("_")[0]);

            lab.getStyleClass().add("tableContentLabel");
            cellPane = new StackPane(lab);
            cellPane.getStyleClass().add("tableContentCell");
            StackPane.setAlignment(lab,Pos.CENTER);

            pane.add(cellPane, column, row);
        }

        int currentRow = row;

        int dayNumber = 0;
        for(String s: days){
            dayNumber++;
            if(s.equals(day)){
                break;
            }
        }

        maxRow = row;
        row = currentRow;

        while(dayNumber <= days.size()) {
            while (row < maxRow) {
                cellPane = new StackPane();
                cellPane.getStyleClass().add("tableContentCell");
                pane.add(cellPane, column, ++row);
            }
            dayNumber++;
            row = 0;
            column++;
        }

        //Styling
        pane.getStyleClass().add("rightTable");

        ColumnConstraints firstColumnSize = new ColumnConstraints();
        firstColumnSize.setPercentWidth(10);
        firstColumnSize.setHalignment(HPos.CENTER);
        pane.getColumnConstraints().add(firstColumnSize);
        for(int j=0; j<5;j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(18.0);
            col.setHalignment(HPos.CENTER);
            pane.getColumnConstraints().add(col);
        }

        RowConstraints firstRowSize = new RowConstraints();
        firstRowSize.setPrefHeight(40.0);
        firstRowSize.setValignment(VPos.CENTER);
        pane.getRowConstraints().add(firstRowSize);

        RowConstraints rowSize = new RowConstraints();
        rowSize.setPrefHeight(80.0);
        rowSize.setValignment(VPos.CENTER);
        for(i = 0; i < maxTimeInterval-1; i++){
            pane.getRowConstraints().add(rowSize);
        }

        vBox.getChildren().add(pane);
        vBox.getStyleClass().add("timetableVBox");
        vBox.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scrollPaneTimetable");
        scrollPane.getStyleClass().add("edge-to-edge");

        return scrollPane;
    }

    public ScrollPane addRightPaneGeneralCase(Timetable timetable, Map<String, List<Event>> map, String resourceType){
        GridPane pane = new GridPane();

        //List with the days displayed on the first row of the timetable
        List<String> days = new ArrayList<>(Arrays.asList("monday","tuesday","wednesday","thursday","friday"));
        List<String> daysRO = new ArrayList<>(Arrays.asList("Luni", "Marți", "Miercuri", "Joi", "Vineri"));

        Map<String, Integer> timetableDays = new HashMap<>();
        int dayCounter = 0;
        for(String s: days){
            timetableDays.put(s, dayCounter++);
        }

        //Adding the head of the table
        String cornerString = new String();
        switch (resourceType){
            case ("studyGroup"):
                cornerString = "Clasa";
                break;
            case ("teacher"):
                cornerString = "Profesor";
                break;
            case("classroom"):
                cornerString = "Sala";
                break;
            default:
                break;
        }
        Label l = new Label(cornerString);
        l.getStyleClass().add("tableHeadLabel");
        cellPane = new StackPane(l);
        cellPane.getStyleClass().add("tableHeadCell");
        StackPane.setAlignment(l,Pos.CENTER);
        pane.add(cellPane, 0,0);
        int i = 1;
        for(String s : daysRO){
            l = new Label(s);
            l.getStyleClass().add("tableHeadLabel");
            cellPane = new StackPane(l);
            cellPane.getStyleClass().add("tableHeadCell");
            StackPane.setAlignment(l,Pos.CENTER);
            pane.add(cellPane, i++,0);
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
            Label lh = new Label(s);
            lh.getStyleClass().add("tableHourLabel");
            StackPane hourCellPane = new StackPane(lh);
            hourCellPane.getStyleClass().add("tableContentCell");
            StackPane.setAlignment(lh,Pos.CENTER);
            pane.add(hourCellPane, 0, line);

            List<Event> resourceEvents = map.get(s);
            resourceEvents = sortEventListByTime(resourceEvents);

            //Adding the rest of the timetable for each resource
            day = days.get(0);
            GridPane dayPane = createEmptyDayCell(maxCounter);
            int col = 0;
            for (Event e : resourceEvents) {
                if (!day.equals(e.getTime().getDay())) {
                    day = e.getTime().getDay();

                    //Styling
                    dayPane = stylingDailyCells(dayPane, maxCounter);

                    col = timetableDays.get(e.getTime().getDay());
                    StackPane sp = new StackPane(dayPane);
                    sp.getStyleClass().add("tableContentCell");
                    pane.add(sp, col, line);

                    dayPane = createEmptyDayCell(maxCounter);
                }

                VBox hourLabel = getHourLabel(e, resourceType, true);
                hourLabel.getStyleClass().add("tableContentLabel");
                cellPane = new StackPane(hourLabel);
                cellPane.getStyleClass().add("tableContentCell");
                StackPane.setAlignment(hourLabel,Pos.CENTER);

                cellPane.setOnDragDetected(new EventHandler<MouseEvent>(){
                    public void handle(MouseEvent event){
                        Dragboard db = hourLabel.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent content = new ClipboardContent();
                        content.putString(resourceType);
                        db.setContent(content);
                        System.out.println("Drag detected");
                        draggedEvent = e;
                        event.consume();
                    }
                });

                cellPane.setOnDragOver(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        event.acceptTransferModes(TransferMode.ANY);
                        System.out.println("Drag Over Detected");
                        event.consume();
                    }
                });

                cellPane.setOnDragDropped(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        event.acceptTransferModes(TransferMode.ANY);
                        Dragboard db = event.getDragboard();

                        event.setDropCompleted(false);
                        if(db.hasString()) {
                            String resourceType = db.getString();

                            System.out.println("Drag dropped "+cellPane.getChildren().size());
                            VBox oldCell = (VBox)cellPane.getChildren().get(0);
                            cellPane.getChildren().clear();
                            cellPane.getChildren().add(getHourLabel(draggedEvent, resourceType, true));
                            event.setDropCompleted(true);
                        }

                        event.consume();
                    }
                });

//                image.setOnDragDone(new EventHandler <DragEvent>() {
//                    public void handle(DragEvent event){
//                        if (event.getTransferMode() == TransferMode.MOVE){
//                            image.setImage(null);
//                        }
//                        System.out.println("Drag Complete!");
//                        event.consume();
//                    }
//                });

                int windowCounter = Integer.valueOf(e.getTime().getName().split("_")[0]) - 1;

                dayPane.add(cellPane, windowCounter, 0);
            }

            //Styling
            dayPane = stylingDailyCells(dayPane, maxCounter);

            StackPane sp = new StackPane(dayPane);
            sp.getStyleClass().add("tableContentCell");
            pane.add(sp, ++col, line);

            int dayIndex = days.indexOf(day);
            int emptyDaysCounter =  days.size() - 1;

            if(resourceEvents.size() == 0) {
                col--;
                emptyDaysCounter++;
            }
            while(dayIndex != emptyDaysCounter){
                GridPane empty = createEmptyDayCell(maxCounter);
                StackPane spe = new StackPane(empty);
                spe.getStyleClass().add("tableContentCell");
                pane.add(spe, ++col, line);
                dayIndex++;
            }
            line++;
        }


        return addScrollPaneToTable(pane, resourceType, list.size(), maxCounter);
    }

    private ScrollPane addScrollPaneToTable(GridPane pane, String resourceType, int rowsNumber, int maxCounter){
        //Adding the title label for the right table on displayPane
        String labelString = new String();
        switch (resourceType){
            case ("studyGroup"):
                labelString = "Clase";
                break;
            case ("teacher"):
                labelString = "Profesori";
                break;
            case("classroom"):
                labelString = "Săli";
                break;
            default:
                break;
        }

        Label label = new Label(labelString);
        label.getStyleClass().add("resourceText");
        VBox vBox = new VBox(label);

        pane.getStyleClass().add("rightTable");

        ColumnConstraints firstColumnSize = new ColumnConstraints();
//        firstColumnSize.setPercentWidth(10);
        firstColumnSize.setPrefWidth(80);
        firstColumnSize.setMinWidth(80);
        firstColumnSize.setHalignment(HPos.CENTER);
        pane.getColumnConstraints().add(firstColumnSize);
        ColumnConstraints col = new ColumnConstraints();
//            col.setPercentWidth(18.0);
        col.setHalignment(HPos.CENTER);
//            col.setMinWidth(70);
        col.setPrefWidth(50 * maxCounter);
        col.setMinWidth(50 * maxCounter);
        for(int j=0; j<5;j++) {
            pane.getColumnConstraints().add(col);
        }

        RowConstraints firstRowSize = new RowConstraints();
        firstRowSize.setPrefHeight(40.0);
        firstRowSize.setMinHeight(40.0);
        firstRowSize.setValignment(VPos.CENTER);
        pane.getRowConstraints().add(firstRowSize);

        RowConstraints rowSize = new RowConstraints();
        rowSize.setPrefHeight(50.0);
        rowSize.setValignment(VPos.CENTER);
        for(int i = 0; i < rowsNumber; i++){
            pane.getRowConstraints().add(rowSize);
        }

        vBox.getChildren().add(pane);
        vBox.getStyleClass().add("timetableVBox");
        vBox.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scrollPaneTimetable");
        scrollPane.getStyleClass().add("edge-to-edge");

        return scrollPane;
    }

    private VBox getHourLabel(Event e, String resourceType, boolean onlyId){
        VBox detailsCell = new VBox();

        Map<String, String> resourceTypeText = new HashMap<>();
        resourceTypeText.put("studyGroup","Clasa ");
        resourceTypeText.put("teacher","Prof. ");
        resourceTypeText.put("classroom","Sala ");

        for(Resource r: e.getResources().getResources()){
            if(!r.getResourceType().equals(resourceType)) {
                Label l;
                if(!onlyId) {
                    if (!r.getName().isEmpty()) {
                        l = new Label(resourceTypeText.get(r.getResourceType()) + r.getName());
                    } else {
                        l = new Label(resourceTypeText.get(r.getResourceType()) + r.getId());
                    }
                }
                else{
                    l = new Label(r.getId());
                }
                detailsCell.getChildren().add(l);
            }
        }

        detailsCell.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(e.getDescription());
        if(!onlyId) {
            nameLabel.getStyleClass().add("cellBoldWriting");
        }

        VBox cellVBox = new VBox(nameLabel, detailsCell);
        cellVBox.setAlignment(Pos.CENTER);

        return cellVBox;
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

    private GridPane createEmptyDayCell(int maxCounter){
        GridPane empty = new GridPane();

        for(int i = 0; i < maxCounter; i++) {
            Label l = new Label(" \n\n ");
            l.getStyleClass().add("tableContentLabel");
            StackPane cellPane = new StackPane(l);
            cellPane.getStyleClass().add("tableContentCell");
            StackPane.setAlignment(l,Pos.CENTER);
            empty.add(cellPane, i, 0);
        }

        empty = stylingDailyCells(empty, maxCounter);

        return empty;
    }

    private GridPane stylingDailyCells(GridPane dayPane, int maxCounter){
        ColumnConstraints columnSize = new ColumnConstraints();
        columnSize.setPercentWidth(100.0/maxCounter);
        columnSize.setHalignment(HPos.CENTER);

        dayPane.getColumnConstraints().clear();

        for(int i = 0; i < maxCounter; i++){
            dayPane.getColumnConstraints().add(columnSize);
        }

        RowConstraints rowSize = new RowConstraints();
        rowSize.setPrefHeight(50.0);
        rowSize.setMinHeight(50.0);
        rowSize.setValignment(VPos.CENTER);

        dayPane.getRowConstraints().clear();
        dayPane.getRowConstraints().add(rowSize);

        return dayPane;
    }
}
