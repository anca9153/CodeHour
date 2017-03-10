package model.constraint.types;

import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import java.util.List;

/**
 * Created by Anca on 3/10/2017.
 */
public class AssignResourceConstraint extends Constraint{
    public AssignResourceConstraint() {
    }

    public AssignResourceConstraint(String id, boolean required, int weight, Events appliesTo) {
        super(id, required, weight, appliesTo);
    }

    @Override
    public int validate(Constraint constraint, Event event) {
        List<Resource> eventResourceList = event.getResources().getResources();
        int infeasability = 0;
        for(Resource r : eventResourceList){
            if (r == null){
                infeasability += constraint.getWeight();
            }
        }
        return infeasability;
    }
}
