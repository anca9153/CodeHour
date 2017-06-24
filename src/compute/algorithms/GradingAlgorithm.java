package compute.algorithms;

import compute.Algorithm;
import model.Timetable;
import model.constraint.Constraint;
import model.constraint.types.LimitIdleTimesConstraint;
import model.constraint.types.LimitRepeatActivityConstraint;
import model.constraint.types.LimitRepeatDailyConstraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.solution.Report;
import model.solution.Solution;
import model.solution.Solutions;
import model.time.Time;
import utilities.DeepCloner;
import utilities.PropertiesLoader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anca on 1/18/2017.
 */
public class GradingAlgorithm implements Algorithm {
    private Timetable timetable;
    private Map<String, Integer> grades = new HashMap<>();
    private Solution solution;
    private Solutions solutions;

    @Override
    public Timetable solve(Timetable timetable) {
        this.timetable = timetable;

        solutions = timetable.getSolutions();
        if(solutions == null) {
            solutions = new Solutions();
            solutions.setSolutions(new ArrayList<>());
        }

        List<Event> clonedList = new ArrayList<>();

        for(Event e: timetable.getEvents().getEvents()){
            clonedList.add((Event)DeepCloner.deepClone(e));
        }

        Events clonedEvents = new Events(sortByMostConstraints(clonedList));

        solution = timetable.getSolutions().getSolutions().get(solutions.getSolutions().size()-1);
        solution.setEvents(clonedEvents);

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

        timetable.setSolutions(solutions);
        return timetable;
    }

    private List<Event> sortByMostConstraints(List<Event> list){
        Comparator<Event> cmp = new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                Integer o1Con = 0, o2Con = 0;
                for(Constraint c: timetable.getEventConstraints().getConstraints()){
                    if(c.getAppliesToEvents()!=null) {
                        if (c.getAppliesToEvents().getEvents().contains(o1)) {
                            o1Con++;
                        }

                        if (c.getAppliesToEvents().getEvents().contains(o2)) {
                            o2Con++;
                        }
                    }
                }

                if(timetable.getResourceConstraints()!=null && timetable.getResourceConstraints().getConstraints()!=null) {
                    for (Constraint c : timetable.getResourceConstraints().getConstraints()) {
                        for (Resource r : o1.getResources().getResources()) {
                            if (c.getAppliesToResources().getResources().contains(r)) {
                                o1Con++;
                            }
                        }

                        for (Resource r : o2.getResources().getResources()) {
                            if (c.getAppliesToResources().getResources().contains(r)) {
                                o2Con++;
                            }
                        }
                    }
                }

                return o1Con.compareTo(Integer.valueOf(o2Con));
            }
        };

        Collections.sort(list, cmp);
        return list;
    }

    int iteration = 0;
    boolean lastScheduled = false;

    private int solveToDoList(List<Event> toDoList){
        //Printing check
        System.out.println("\n Events in iteration no " + iteration++);
        int countNotScheduled=0;
        for(Event e: solution.getEvents().getEvents()){
            if(e.getTime()!=null) {
                System.out.println(e.getId() + " " + e.getDescription() + " " + (e.getTime() != null ? e.getTime().getId() : "not scheduled") + " " + grades.get(e.getId()));
            }
            else{
                countNotScheduled++;
            }
        }
        System.out.println("Not scheduled "+countNotScheduled +" events");

        for(Event e: toDoList){
            System.out.println("to do list "+e.getId()+" "+(e.getTime() != null ? e.getTime().getId() : "not scheduled") +" "+grades.get(e.getId()));
        }

        //Solving the problem
        if(toDoList.size() != 0) {
            Event e = toDoList.get(0);
            int bestCostValue = 10000;
            List<Time> bestTimes = new ArrayList<>();
            for (Time t : timetable.getTimes().getTimes()) {
                e.setTime(t);
                int costValue = computeInfeasibility(e);
                if (costValue < bestCostValue) {
                    bestCostValue = costValue;
                    bestTimes.clear();
                    bestTimes.add(t);
                }
                else {
                    if (costValue == bestCostValue) {
                        bestTimes.add(t);
                    }
                }
            }

            int index = solution.getEvents().getEvents().indexOf(e);

            for(Time bestTime: bestTimes) {
                lastScheduled = false;

                if (bestCostValue > 0) {
                    e.setTime(bestTime);
                    toDoList.remove(e);
                    List<Event> evs = unscheduleConflictingEvents(e);
                    if(evs.size() == 0){
                        solution.getEvents().getEvents().get(index).setTime(bestTime);
                        lastScheduled = true;
                    }
                    else {
                        List<Event> toRemove = new ArrayList<>();
                        for (Event eEvs : evs) {
                            for (Event eToDo : toDoList) {
                                if (eToDo.getId().equals(eEvs.getId())) {
                                    toRemove.add(eToDo);
                                }
                            }
                        }

                        toDoList.removeAll(toRemove);

                        toDoList.addAll(evs);
                        e.setTime(null);
                        toDoList.add(e);

                        solution.getEvents().getEvents().get(index).setTime(null);
                    }
                }
                else {
                    solution.getEvents().getEvents().get(index).setTime(bestTime);
                    toDoList.remove(e);
                    lastScheduled = true;
                }

                if(solveToDoList(toDoList) == 1){
                    return 1;
                }
                else {
                    if (lastScheduled) {
                        int indexLastProgrammed = solution.getEvents().getEvents().size();
                        solution.getEvents().getEvents().get(indexLastProgrammed - 1).setTime(null);
                        toDoList.add(0, e);
                    }
                }
            }
            return 0;
        }

        return 1;
    }

    private int computeInfeasibility(Event e){
        int infeasibility = 0;

        if(timetable.getEventConstraints() != null && timetable.getEventConstraints().getConstraints() != null) {
            for (Constraint c : timetable.getEventConstraints().getConstraints()) {
                if(!c.getId().startsWith("limitRepeatActivity") && !c.getId().startsWith("limitRepeatDaily")){
                    infeasibility += c.validate(e);
                }
                else{
                    if(e.getTime() != null) {
                        List<String> subjects = readSubjects();

                        if(c.getId().startsWith("limitRepeatActivity")) {
                            ((LimitRepeatActivityConstraint) c).setEventDescriptions(subjects);
                            ((LimitRepeatActivityConstraint) c).setProgrammedEvents(solution.getEvents());
                        }
                        else{
                            ((LimitRepeatDailyConstraint) c).setEventDescriptions(subjects);
                            ((LimitRepeatDailyConstraint) c).setProgrammedEvents(solution.getEvents());
                        }

                        infeasibility += c.validate(e);
                    }
                }
            }
        }

        if(timetable.getResourceConstraints() != null && timetable.getResourceConstraints().getConstraints() != null) {
            for (Constraint c : timetable.getResourceConstraints().getConstraints()) {
                for (Resource r : e.getResources().getResources()) {
                    if(c.getId().startsWith("limitIdleTime")){
                        ((LimitIdleTimesConstraint) c).setEvents(solution.getEvents());
                        ((LimitIdleTimesConstraint) c).setTimes(timetable.getTimes());
                    }
                    infeasibility += c.validate(r);
                }
            }
        }

        Time t = e.getTime();
        e.setTime(null);
        infeasibility += getConflictingEventsCost(t,e);
        return infeasibility;
    }

    private List<String> readSubjects(){
        List<String> subjects = new ArrayList<>();

        //Reading the subjects list from file
        String path = PropertiesLoader.loadSubjectsFilePath();

        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                subjects.add(sCurrentLine);
            }

        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        return subjects;
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
                            if(!conflictingEvents.contains(e)){
                                conflictingEvents.add(e);
                            }
                            break;
                        }
                    }
                }
            }
        }

        for(Constraint c: timetable.getEventConstraints().getConstraints()) {
            if(c instanceof LimitRepeatActivityConstraint) {
                LimitRepeatActivityConstraint lraConstraint = (LimitRepeatActivityConstraint) c;

                List<String> subjects = readSubjects();

                lraConstraint.setEventDescriptions(subjects);
                lraConstraint.setProgrammedEvents(solution.getEvents());

                if(lraConstraint.validate(event)>0){
                    conflictingEvents.addAll(lraConstraint.getConflictingEvents());
                }
            }
            else {
                if (c instanceof LimitRepeatDailyConstraint) {
                    LimitRepeatDailyConstraint lrdConstraint = (LimitRepeatDailyConstraint) c;

                    List<String> subjects = readSubjects();

                    lrdConstraint.setEventDescriptions(subjects);
                    lrdConstraint.setProgrammedEvents(solution.getEvents());

                    if (lrdConstraint.validate(event) > 0) {
                        conflictingEvents.addAll(lrdConstraint.getConflictingEvents());
                    }
                }
            }
        }

        return conflictingEvents;
    }

}
