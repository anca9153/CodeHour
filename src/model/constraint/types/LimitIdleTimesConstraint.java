package model.constraint.types;

import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;
import model.time.Time;
import model.time.Times;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

/**
 * Created by Anca on 3/11/2017.
 */
@XmlRootElement
public class LimitIdleTimesConstraint extends Constraint {
    private Events events;
    private Times times;
    private int maximumIdleHours;
    private List<Event> conflictingEvents;

    public LimitIdleTimesConstraint() {
    }

    public LimitIdleTimesConstraint(String id, boolean required, int weight, int maximumIdleHours, Events appliesToEvents, Resources appliesToResources) {
        super(id, required, weight, appliesToEvents, appliesToResources);
        this.maximumIdleHours = maximumIdleHours;
    }

    @Override
    public int validate(Event event) {
        int cost = 0;
        Map<Integer, Event> timeIdEventMap = new HashMap<>();
        conflictingEvents = new ArrayList<>();

        //The resource to be checked
        List<Resource> resources = event.getResources().getResources();
        for(Resource resource: resources) {
            if (appliesToResources.getResources().contains(resource)) {
                //The times when the resource is scheduled
                List<Time> resourceTimes = new ArrayList<>();

                //The lists events and times must be set before the calling of this validate function
                //Searching the times when the resource is scheduled
                for (Event e : events.getEvents()) {
                    for (Resource r : e.getResources().getResources()) {
                        if (e.getTime() != null && resource.getId().equals(r.getId())) {
                            resourceTimes.add(e.getTime());
                            timeIdEventMap.put(e.getTime().getId(), e);
                            break;
                        }
                    }
                }

                //Sorting the times when the resource is scheduled
                Collections.sort(resourceTimes, new Comparator<Time>() {
                    public int compare(Time one, Time other) {
                        return Integer.valueOf(one.getId()).compareTo(Integer.valueOf(other.getId()));
                    }
                });

                int eventTimeIndex = -1;
                if (resourceTimes.size() > 1) {
                    for (Time t: resourceTimes) {
                        if(t.getId() == event.getTime().getId()){
                            eventTimeIndex = resourceTimes.indexOf(t);
                            break;
                        }
                    }

                    int maximumPosibleEvents = unprogrammedEventsWithResource(resource);

                    if(eventTimeIndex>0){
                        Time previousTime = resourceTimes.get(eventTimeIndex-1);
                        if(previousTime.getDay().equals(event.getTime().getDay())) {
                            int hoursBetween = event.getTime().getId() - previousTime.getId();
                            if (hoursBetween - maximumPosibleEvents > maximumIdleHours) {
                                cost += this.getWeight() * (hoursBetween - maximumIdleHours);
                                conflictingEvents.add(timeIdEventMap.get(previousTime.getId()));
                            }
                        }
                    }
                    if(eventTimeIndex<resourceTimes.size() - 1){
                        Time nextTime = resourceTimes.get(eventTimeIndex+1);
                        if(nextTime.getDay().equals(event.getTime().getDay())) {
                            int hoursBetween = nextTime.getId() - event.getTime().getId();
                            if (hoursBetween - maximumPosibleEvents > maximumIdleHours) {
                                cost += this.getWeight() * (hoursBetween - maximumIdleHours);
                                conflictingEvents.add(timeIdEventMap.get(nextTime.getId()));
                            }
                        }
                    }
                }
            }
        }

        return cost;
    }

    private int unprogrammedEventsWithResource(Resource resource){
        int counter = 0;
        for(Event e: events.getEvents()){
            if(e.getTime() == null) {
                for (Resource r : e.getResources().getResources()) {
                    if (r.getId().equals(resource.getId())) {
                        counter ++;
                        break;
                    }
                }
            }
        }
        return counter;
    }

    public Events getEvents() {
        return events;
    }

    @XmlTransient
    public void setEvents(Events events) {
        this.events = events;
    }

    public Times getTimes() {
        return times;
    }

    @XmlTransient
    public void setTimes(Times times) {
        this.times = times;
    }

    public int getMaximumIdleHours() {
        return maximumIdleHours;
    }

    public void setMaximumIdleHours(int maximumIdleHours) {
        this.maximumIdleHours = maximumIdleHours;
    }

    public List<Event> getConflictingEvents() {
        return conflictingEvents;
    }

    @XmlTransient
    public void setConflictingEvents(List<Event> conflictingEvents) {
        this.conflictingEvents = conflictingEvents;
    }
}
