package compute.algorithms;

import compute.Algorithm;
import model.Timetable;
import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.solution.Report;
import model.solution.Solution;
import model.solution.Solutions;
import model.time.Time;
import utilities.DeepCloner;

import java.util.*;

/**
 * Created by Anca on 1/18/2017.
 */
public class GradingAlgorithm implements Algorithm {
    private Timetable timetable;
    private Map<String, Integer> grades = new HashMap<>();
    private List<Event> conflictingEvents = new ArrayList<>();
    private Solution solution;

    @Override
    public Timetable solve(Timetable timetable) {
        this.timetable = timetable;

        List<Event> clonedList = new ArrayList<>();

        for(Event e: timetable.getEvents().getEvents()){
            clonedList.add((Event)DeepCloner.deepClone(e));
        }

        Events clonedEvents = new Events(clonedList);

        solution = new Solution("sol1","First Solution", clonedEvents, null);

        //To do list with the events that have not been assigned a Time yet or share resources with other events
        List<Event> toDoList = new ArrayList<>();
        for (Event e : solution.getEvents().getEvents()){
            int inf = computeInfeasibility(e);
            if (inf > 0){
                toDoList.add(e);
                e.setTime(null);
            }
        }

        //Initialize grades for each event
        for(Event e : solution.getEvents().getEvents()){
            grades.put(e.getId(), 0);
        }

        solveToDoList(toDoList);

        Report report = new Report();
        Solutions solutions = new Solutions();
        solutions.setSolutions(Arrays.asList(solution));
        timetable.setSolutions(solutions);
        return timetable;
    }

    int iteration = 0;

    private void solveToDoList(List<Event> toDoList){
        System.out.println("Events in iteration no " + iteration++);
        for(Event e: solution.getEvents().getEvents()){
            System.out.println(e.getId()+" "+(e.getTime() != null ? e.getTime().getId() : "not scheduled") +" "+grades.get(e.getId()));
        }
        for(Event e: conflictingEvents){
            System.out.println("conflicting "+e.getId()+" "+(e.getTime() != null ? e.getTime().getId() : "not scheduled") +" "+grades.get(e.getId()));
        }

        if(toDoList.size() != 0) {
            Event e = toDoList.get(0);
            int bestCostValue = 10000;
            Time bestTime = null;
            for (Time t : timetable.getTimes().getTimes()) {
                e.setTime(t);
                int costValue = computeInfeasibility(e);
                if (costValue < bestCostValue) {
                    bestCostValue = costValue;
                    bestTime = t;
                }
            }

            e.setTime(bestTime);
            if(bestCostValue>0){
                conflictingEvents.add(e);
                solution.getEvents().getEvents().remove(e);
            }
            toDoList.remove(e);
            if(bestCostValue == 0){
                toDoList.addAll(unscheduleConflictingEvents(e));
            }
            solveToDoList(toDoList);
        }
    }

    private int computeInfeasibility(Event e){
        int infeasibility = 0;
        for(Constraint c : timetable.getEventConstraints().getConstraints()){
            infeasibility += c.validate(e);
        }
        for(Constraint c: timetable.getResourceConstraints().getConstraints()){
            for(Resource r: e.getResources().getResources()){
                infeasibility += c.validate(r);
            }
        }
        Time t = e.getTime();
        e.setTime(null);
        infeasibility += getConflictingEventsCost(t,e);
        return infeasibility;
    }

    private int getConflictingEventsCost(Time time, Event event){
        int cost = 0;
        for(Event e : solution.getEvents().getEvents()){
            if(e.getTime()!=null && time.getId() == e.getTime().getId()){
                for(Resource r : event.getResources().getResources()){
                    for(Resource re : e.getResources().getResources()){
                        if(r.equals(re)) {
                            cost++;
//                            System.out.println(e.getId()+" "+r.getId()+" "+cost);
                        }
                    }
                }
                Integer eCost = grades.get(e.getId());
                cost += eCost != null ? eCost : 0;
            }
        }
        return cost;
    }

    private List<Event> unscheduleConflictingEvents(Event event){
        List<Event> conflictingEvents = new ArrayList<>();
        for(Event e: solution.getEvents().getEvents()){
            if(!e.getId().matches(event.getId()) && event.getTime().equals(e.getTime())){
                for(Resource r : event.getResources().getResources()){
                    for(Resource re : e.getResources().getResources()){
                        if(r.equals(re)) {
                            e.setTime(null);
                            grades.put(e.getId(), grades.get(e.getId()) + 1);
                            conflictingEvents.add(e);
                            break;
                        }
                    }
                }
            }
        }
        return conflictingEvents;
    }


}
