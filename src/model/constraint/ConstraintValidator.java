package model.constraint;

import model.event.Event;
import model.resource.Resource;

import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */
public class ConstraintValidator{
    private List<Event> eventList;

    public ConstraintValidator(List<Event> eventList) {
        this.setEventList(eventList);
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public int validate(Constraint constraint, Event event){
        String type = constraint.getId();
        switch(type){
            case "assignResourceConstraint":
                return checkAssignResourceConstraint(constraint, event);
            case "assignTimeConstraint":
                return event.getTime() == null ? constraint.getWeight() : 0;
            default:
                break;
        }
        return 0;
    }

    private int checkAssignResourceConstraint(Constraint constraint, Event event){
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
