package model.event;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
@XmlRootElement(name = "events")
public class Events implements Serializable{
    private List<Event> events;

    public Events(){

    }

    public Events(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }


    @XmlElement(name = "event")
    public void setEvents(List<Event> events) {
        this.events= events;
    }
}
