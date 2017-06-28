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
public class LimitResourceAvailableTimesConstraint extends Constraint {
    private Events events;
    private Times notAvailableTimes;

    public LimitResourceAvailableTimesConstraint() {
    }

    public LimitResourceAvailableTimesConstraint(String id, boolean required, int weight, Events appliesToEvents, Resources appliesToResources, Times notAvailableTimes) {
        super(id, required, weight, appliesToEvents, appliesToResources);
        this.notAvailableTimes = notAvailableTimes;
    }

    @Override
    public int validate(Event event) {
        int cost = 0;
        for(Resource r: event.getResources().getResources()){
            for(Resource r1: appliesToResources.getResources()){
                if(r1.getId().equals(r.getId())){
                    boolean ok = true;
                    for(Time t: notAvailableTimes.getTimes()){
                        if(t.getId() == event.getTime().getId()){
                            ok = false;
                            break;
                        }
                    }
                    if(!ok){
                        cost += weight;
                    }
                    break;
                }
            }
        }
        return cost;
    }

    public Events getEvents() {
        return events;
    }

    @XmlTransient
    public void setEvents(Events events) {
        this.events = events;
    }

    public Times getNotAvailableTimes() {
        return notAvailableTimes;
    }

    public void setNotAvailableTimes(Times notAvailableTimes) {
        this.notAvailableTimes = notAvailableTimes;
    }
}
