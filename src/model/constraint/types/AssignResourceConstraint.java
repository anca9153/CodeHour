package model.constraint.types;

import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 3/10/2017.
 */
@XmlRootElement
public class AssignResourceConstraint extends Constraint{
    public AssignResourceConstraint() {
    }

    public AssignResourceConstraint(String id, boolean required, int weight, Events appliesToEvents, Resources appliesToResources) {
        super(id, required, weight, appliesToEvents, appliesToResources);
    }

    @Override
    public int validate(Event event) {
        List<Resource> eventResourceList = event.getResources().getResources();
        int infeasability = 0;
        for (Resource r : eventResourceList) {
            if (r == null) {
                infeasability += this.getWeight();
            }
        }
        return infeasability;
    }
}
