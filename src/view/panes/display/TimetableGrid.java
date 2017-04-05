package view.panes.display;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
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

    public ScrollPane addRightPane(Timetable timetable, String resource, String resourceType, List<Event> eventList){
        //The timetable for a selected resource, displayed on the right side
        VBox vBox = new VBox(new Text(resource));

        GridPane pane = new GridPane();

        //Sorting the events for the selected resource by their time
        eventList = sortEventListByTime(eventList);

        //List with the days displayed on the first row of the timetable
        List<String> days = new ArrayList<>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday"));
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
            if(maxTime.getDay().equals(t.getDay())){
                pane.add(new Text(t.getHourInterval()), 0, row++);
            }
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
        firstRowSize.setPrefHeight(20.0);
        firstRowSize.setValignment(VPos.CENTER);
        pane.getRowConstraints().add(firstRowSize);

        RowConstraints rowSize = new RowConstraints();
        rowSize.setPrefHeight(60.0);
        rowSize.setValignment(VPos.CENTER);
        for(i = 0; i < row-1; i++){
            pane.getRowConstraints().add(rowSize);
        }

        pane.setGridLinesVisible(true);
        vBox.getChildren().add(pane);
        vBox.getStyleClass().add("timetableVBox");
        vBox.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scrollPaneTimetable");

        return scrollPane;
    }

    public ScrollPane addRightPaneGeneralCase(Timetable timetable, Map<String, List<Event>> map, String resourceType){
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

            for (Event e : resourceEvents) {
                if (!day.equals(e.getTime().getDay())) {
                    day = e.getTime().getDay();

                    //Styling
                    dayPane = stylingDailyCells(dayPane, maxCounter);

                    col = timetableDays.get(e.getTime().getDay());
                    pane.add(dayPane, col, line);
                    dayPane = new GridPane();
                }

                String hourLabel = getHourLabel(e, resourceType, true);
                int windowCounter = Integer.valueOf(e.getTime().getName().split("_")[0]) - 1;

                dayPane.add(new Text(hourLabel), windowCounter, 0);
            }

            //Styling
            dayPane = stylingDailyCells(dayPane, maxCounter);

            pane.add(dayPane, ++col, line);

            int dayIndex = days.indexOf(day);
            int emptyDaysCounter =  days.size() - 1;

            if(resourceEvents.size() == 0) {
                col--;
                emptyDaysCounter++;
            }
            while(dayIndex != emptyDaysCounter){
                GridPane empty = createEmptyDayCell(maxCounter);
                pane.add(empty, ++col, line);
                dayIndex++;
            }
            line++;
        }


        return addScrollPaneToTable(pane, resourceType, list.size());
    }

    private ScrollPane addScrollPaneToTable(GridPane pane, String resourceType, int rowsNumber){
        VBox vBox = new VBox(new Text(resourceType));

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
        firstRowSize.setPrefHeight(20.0);
        firstRowSize.setValignment(VPos.CENTER);
        pane.getRowConstraints().add(firstRowSize);

        RowConstraints rowSize = new RowConstraints();
        rowSize.setPrefHeight(60.0);
        rowSize.setValignment(VPos.CENTER);
        for(int i = 0; i < rowsNumber; i++){
            pane.getRowConstraints().add(rowSize);
        }

        pane.setGridLinesVisible(true);
        vBox.getChildren().add(pane);
        vBox.getStyleClass().add("timetableVBox");
        vBox.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scrollPaneTimetable");

        return scrollPane;
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
}
