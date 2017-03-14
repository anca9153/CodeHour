package model.constraint.types;

import model.FitForConstraint;
import model.constraint.Constraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.Resources;
import model.time.Time;
import model.time.Times;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Anca on 3/11/2017.
 */
@XmlRootElement
public class LimitIdleTimesConstraint extends Constraint {
    private Events events;
    private Times times;
    private int maximumIdleHours;

    public LimitIdleTimesConstraint() {
    }

    public LimitIdleTimesConstraint(String id, boolean required, int weight, int maximumIdleHours, Events appliesToEvents, Resources appliesToResources) {
        super(id, required, weight, appliesToEvents, appliesToResources);
        this.setMaximumIdleHours(maximumIdleHours);
    }

    @Override
    public int validate(FitForConstraint val) {
        //The resource to be checked
        Resource resource = (Resource) val;

        //The times when the resource is scheduled
        List<Time> resourceTimes = new ArrayList<>();

        //The lists events and times must be set before the calling of this validate function
        if(events != null && times != null){
            //Searching the times when the resource is scheduled
            for(Event e: events.getEvents()){
                for(Resource r : e.getResources().getResources()){
                    if(resource.equals(r) && e.getTime() != null){
                        resourceTimes.add(e.getTime());
                        break;
                    }
                }
            }

            //Sorting the times when the resource is scheduled
            Collections.sort(resourceTimes, new Comparator<Time>() {
                public int compare(Time one, Time other) {
                    if(one.getDay().matches(other.getDay())){
                        return one.getId().compareTo(other.getId());
                    }
                    return one.getDay().compareTo(other.getDay());
                }
            });

            int cost = 0;

            if(resourceTimes.size()!=0) {
                if (resourceTimes.size() == 1) {
                    return 0;
                }

                for (int i =1; i<resourceTimes.size(); i++) {
                    Time t1 = resourceTimes.get(i-1);
                    Time t2 = resourceTimes.get(i);

                    //If the times have the same day they have maximumIdleHours between them
                    if (t1.getDay().matches(t2.getDay())) {
                        int t1_index = -1;

                        for(int j=0; j<times.getTimes().size()-1; j++){
                            if(times.getTimes().get(j).getId().matches(t1.getId())){
                                t1_index = j;
                            }

                            //If the times compared t1 and t2 are not consecutive the cost is raised
                            if(t1_index >= 0){
                                j++;

                                //Claculating the hours distance between times
                                int hoursBetween = 0;
                                while(j<times.getTimes().size() && !times.getTimes().get(j).getId().matches(t2.getId())){
                                   hoursBetween++;
                                   j++;
                                }

                                //Adding to the cost
                                if(hoursBetween > getMaximumIdleHours()){
                                    cost += this.getWeight() * (hoursBetween - getMaximumIdleHours());
                                }

                                break;
                            }
                        }
                    }
                }

                return cost;
            }
        }
        return 0;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public Times getTimes() {
        return times;
    }

    public void setTimes(Times times) {
        this.times = times;
    }

    public int getMaximumIdleHours() {
        return maximumIdleHours;
    }

    public void setMaximumIdleHours(int maximumIdleHours) {
        this.maximumIdleHours = maximumIdleHours;
    }
}
