package compute;

import model.Timetable;
import model.constraint.Constraint;
import model.constraint.ConstraintValidator;
import model.constraint.Constraints;
import model.event.Event;
import model.event.Events;
import model.solution.Solution;
import model.solution.Solutions;
import model.time.Time;

import java.util.*;

/**
 * Created by Anca on 1/18/2017.
 */
public class GradingAlgorithm implements Algorithm {
    private Timetable timetable;
    private Map<String, Integer> grades = new HashMap<>();

    @Override
    public Timetable solve(Timetable timetable) {
        this.timetable = timetable;

        //To do list with the events that have not been assigned a Time yet or share resources with other events
        List<Event> toDoList = new ArrayList<>();
        for (Event e : timetable.getEvents().getEvents()){
            int inf = computeInfeasibility(e);
            if (inf > 0){
                toDoList.add(e);
                e.setTime(null);
            }
        }

        //Initialize grades with 0 for each event
        for(Event e : timetable.getEvents().getEvents()){
            grades.put(e.getId(), 0);
        }

        solveToDoList(toDoList);

        Solution solution = new Solution("First Solution", new Events(timetable.getEvents().getEvents()), null);
        Solutions solutions = new Solutions();
        solutions.setSolutions(Arrays.asList(solution));
        timetable.setSolutions(solutions);
        return timetable;
    }
    int iteration = 0;

    private void solveToDoList(List<Event> toDoList){
        System.out.println("Events in iteration no " + iteration++);
        for(Event e: timetable.getEvents().getEvents()){
            System.out.println(e.getId()+" "+(e.getTime() != null ? e.getTime().getId() : "not scheduled"));
        }
        if(toDoList.size() != 0) {
            Event e = toDoList.get(0);
            int bestCostValue = 1000;
            Time bestTime = null;
            for (Time t : timetable.getTimes().getTimes()) {
                int costValue = getConflictingEventsCost(e, t);
                if (costValue < bestCostValue) {
                    bestCostValue = costValue;
                    bestTime = t;
                }
            }
            e.setTime(bestTime);
            toDoList.remove(e);
            toDoList.addAll(unscheduleConflictingEvents(e));
            solveToDoList(toDoList);
        }
    }

    private int computeInfeasibility(Event e){
        ConstraintValidator validator = new ConstraintValidator(timetable.getEvents().getEvents());
        int infeasibility = 0;
        for(Constraint c : timetable.getConstraints().getConstraints()){
            infeasibility += validator.validate(c, e);
        }
        return infeasibility;
    }

    private int getConflictingEventsCost(Event event, Time time){
        int cost = 0;
        for(Event e : timetable.getEvents().getEvents()){
            if(e.getTime()!=null) {
                if (time.equals(e.getTime())) {
                    Integer eCost = grades.get(e.getId());
                    cost += eCost != null ? eCost : 0;
                }
            }
        }
        return cost;
    }

    private List<Event> unscheduleConflictingEvents(Event event){
        List<Event> conflictingEvents = new ArrayList<>();
        for(Event e: timetable.getEvents().getEvents()){
            if(e.getId()!= event.getId() && event.getTime().equals(e.getTime())){
                e.setTime(null);
                grades.put(e.getId(), grades.get(e.getId()) + 1);
                conflictingEvents.add(e);
            }
        }
        return conflictingEvents;
    }

//    public static Events getTimeTable(List<Event> events, List<Classroom> classroomsList){
//        unprogrammedEvents = events;
//        classrooms = classroomsList;
//
//        solve();
//        Events finalEvents = new Events();
//        finalEvents.setEventList(programmedEvents);
//        return finalEvents;
//    }
//
//    private static Event programEvent(Event currentEvent){
//        for(Event e : programmedEvents){
//            if(e.getWeekDay() == currentEvent.getWeekDay() && e.getHour() == currentEvent.getHour() && (e.getStudyGroup().equals(currentEvent.getStudyGroup()) || e.getTeacher().equals(currentEvent.getTeacher()) || e.getClassroom().equals(currentEvent.getClassroom()))){
//                if(currentEvent.getHour() == endHour){
//                    if(currentEvent.getWeekDay()== endWeekDay){
//                        return null;
//                    }
//                    else{
//                        currentEvent.setHour(startHour);
//                        currentEvent.setWeekDay(currentEvent.getWeekDay()+1);
//                        programEvent(currentEvent);
//                    }
//                }
//                else {
//                    currentEvent.setHour(currentEvent.getHour()+currentEvent.getDuration());
//                    programEvent(currentEvent);
//                }
//                break;
//            }
//        }
//        return currentEvent;
//    }
//
//    private static void solve(){
//        Event event;
//        int eventDay = 1;
//        int eventHour = startHour;
//        if(unprogrammedEvents.size() != 0) {
//            event = unprogrammedEvents.get(0);
//            event.setHour(startHour);
//            event.setWeekDay(startWeekDay);
//            if(event.getClassroom()==null){
//                Classroom classroom = classrooms.get(0);
//                event.setClassroom(classroom);
//            }
//            Event programmedEvent = programEvent(event);
//            programmedEvents.add(programmedEvent);
//            unprogrammedEvents.remove(0);
//            solve();
//        }
//    }

}
