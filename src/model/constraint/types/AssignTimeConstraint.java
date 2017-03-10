package model.constraint.types;

import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;

/**
 * Created by Anca on 3/10/2017.
 */
public class AssignTimeConstraint extends Constraint {
    public AssignTimeConstraint() {
    }

    public AssignTimeConstraint(String id, boolean required, int weight, Events appliesTo) {
        super(id, required, weight, appliesTo);
    }

    @Override
    public int validate(Constraint constraint, Event event) {
        return event.getTime() == null ? constraint.getWeight() : 0;
    }
}
