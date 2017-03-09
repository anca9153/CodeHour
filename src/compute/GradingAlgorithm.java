package compute;

import model.Timetable;
import model.event.Event;
import model.event.Events;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
public class GradingAlgorithm implements Algorithm {

    @Override
    public Timetable solve() {
        return null;
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
