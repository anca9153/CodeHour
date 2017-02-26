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
    private List<Event> eventList;

    public List<Event> getEventList() {
        return eventList;
    }

    @XmlElement(name = "event")
    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
