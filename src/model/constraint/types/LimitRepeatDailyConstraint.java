package model.constraint.types;

import model.FitForConstraint;
import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anca on 6/20/2017.
 */
@XmlRootElement
public class LimitRepeatDailyConstraint extends Constraint {
    private Events programmedEvents;
    private List<String> eventDescriptions;
    private int maximumNumberOfRepetitions;
    private List<Event> conflictingEvents;

    public LimitRepeatDailyConstraint(){

    }

    public LimitRepeatDailyConstraint(String id, boolean required, int weight, int maximumNumberOfRepetitions, Events appliesToEvents, Resources appliesToResources) {
        super(id, required, weight, appliesToEvents, appliesToResources);
        this.maximumNumberOfRepetitions = maximumNumberOfRepetitions;
    }

    @Override
    public int validate(FitForConstraint value) {
        Event event = (Event) value;

        Resource studyGroup =  null;
        for(Resource r: event.getResources().getResources()){
            if(r.getResourceType().equals("studyGroup")){
                studyGroup = r;
            }
        }

        conflictingEvents = new ArrayList<>();

        if(eventDescriptions.contains(event.getDescription())) {
            int repeated = 0;

            for (Event e : programmedEvents.getEvents()) {
                if (e.getDescription().equals(event.getDescription()) && !e.getId().equals(event.getId()) && e.getTime().getDay().equals(event.getTime().getDay())) {
                    Resource eStudyGroup =  null;
                    for(Resource r: e.getResources().getResources()){
                        if(r.getResourceType().equals("studyGroup")){
                            eStudyGroup = r;
                        }
                    }

                    if(studyGroup.getId().equals(eStudyGroup.getId())) {
                        repeated++;
                        conflictingEvents.add(e);
                    }
                }

                if (repeated > maximumNumberOfRepetitions) {
                    return weight;
                }
            }
        }

        return 0;
    }

    public int getMaximumNumberOfRepetitions() {
        return maximumNumberOfRepetitions;
    }

    public void setMaximumNumberOfRepetitions(int maximumNumberOfRepetitions) {
        this.maximumNumberOfRepetitions = maximumNumberOfRepetitions;
    }

    public Events getProgrammedEvents() {
        return programmedEvents;
    }

    public void setProgrammedEvents(Events events) {
        List<Event> progEv = new ArrayList<>();
        for(Event e: events.getEvents()){
            if(e.getTime() != null){
                progEv.add(e);
            }
        }

        this.programmedEvents = new Events(progEv);
    }

    public List<Event> getConflictingEvents() {
        return conflictingEvents;
    }

    public void setConflictingEvents(List<Event> conflictingEvents) {
        this.conflictingEvents = conflictingEvents;
    }

    public List<String> getEventDescriptions() {
        return eventDescriptions;
    }

    public void setEventDescriptions(List<String> eventDescriptions) {
        this.eventDescriptions = eventDescriptions;
    }
}
