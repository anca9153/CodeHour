package model.constraint.types;

import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 3/10/2017.
 */
@XmlRootElement
public class AssignTimeConstraint extends Constraint {
    public AssignTimeConstraint() {
    }

    public AssignTimeConstraint(String id, boolean required, int weight, Events appliesToEvents, Resources appliesToResources) {
        super(id, required, weight, appliesToEvents, appliesToResources);
    }

    @Override
    public int validate(Event event) {
        return event.getTime() == null ? this.getWeight() : 0;
    }
}
