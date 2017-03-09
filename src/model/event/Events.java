package model.XMLModel;

import model.Event;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
@XmlRootElement(name = "events")
public class Events {
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    @XmlElement(name = "event")
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
