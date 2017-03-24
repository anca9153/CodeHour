package model.solution.OutLaws;

import model.constraint.Constraint;
import model.event.Event;
import model.resource.Resource;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 3/24/2017.
 */
@XmlRootElement(name = "constraintViolatingEvent")
public class ConstraintViolatingEvent {
    private Event event;
    private Constraint constraint;
    private int cost;

    public ConstraintViolatingEvent(){

    }

    public ConstraintViolatingEvent(Event event, Constraint constraint, int cost) {
        this.event = event;
        this.constraint = constraint;
        this.cost = cost;
    }

}
