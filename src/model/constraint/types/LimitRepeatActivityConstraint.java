package model.constraint.types;
import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anca on 6/20/2017.
 */
@XmlRootElement
public class LimitRepeatActivityConstraint extends Constraint {
    private Events programmedEvents;
    private List<String> eventDescriptions;
    private int maximumNumberOfRepetitions;
    private List<Event> conflictingEvents;

    public LimitRepeatActivityConstraint(){

    }

    public LimitRepeatActivityConstraint(String id, boolean required, int weight, int maximumNumberOfRepetitions, Events appliesToEvents, Resources appliesToResources) {
        super(id, required, weight, appliesToEvents, appliesToResources);
        this.maximumNumberOfRepetitions = maximumNumberOfRepetitions;
    }

    @Override
    public int validate(Event event) {
        //The interval in which the events with the same description are found
        int upperInterval = event.getTime().getId();
        int lowerInterval = event.getTime().getId();

        //List of programmed events must be set before calling this function
        if(eventDescriptions.contains(event.getDescription())) {
            return checkInterval(event, upperInterval, lowerInterval);
        }

        return 0;
    }

    private int checkInterval(Event event, int upperInterval, int lowerInterval){
        conflictingEvents = new ArrayList<>();
        Resource studyGroup =  null;
        for(Resource r: event.getResources().getResources()){
            if(r.getResourceType().equals("studyGroup")){
                studyGroup = r;
            }
        }

        boolean intervalGotBigger = true;
        while(intervalGotBigger && studyGroup!=null) {
            intervalGotBigger = false;
            for (Event e : programmedEvents.getEvents()) {
                if (!e.getId().equals(event.getId()) && e.getDescription().equals(event.getDescription())) {
                    Resource eStudyGroup = null;
                    for(Resource r: e.getResources().getResources()){
                        if(r.getResourceType().equals("studyGroup")){
                            eStudyGroup = r;
                        }
                    }

                    if(eStudyGroup!=null && studyGroup.getId().equals(eStudyGroup.getId())) {
                        if (upperInterval - e.getTime().getId() == 1) {
                            upperInterval--;
                            intervalGotBigger = true;
                            conflictingEvents.add(e);
                        } else {
                            if (e.getTime().getId() - lowerInterval == 1) {
                                lowerInterval++;
                                intervalGotBigger = true;
                                conflictingEvents.add(e);
                            }
                        }
                        if (lowerInterval - upperInterval > maximumNumberOfRepetitions) {
                            return weight;
                        }
                    }
                }
            }
        }

        return 0;
    }

    public List<String> getEventDescriptions() {
        return eventDescriptions;
    }

    public void setEventDescriptions(List<String> eventDescriptions) {
        this.eventDescriptions = eventDescriptions;
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

    @XmlTransient
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

    @XmlTransient
    public void setConflictingEvents(List<Event> conflictingEvents) {
        this.conflictingEvents = conflictingEvents;
    }
}
