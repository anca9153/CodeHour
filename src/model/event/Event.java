package model;

import model.XMLModel.Resources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 1/17/2017.
 */
@XmlRootElement(name = "event")
public class Event {
    private String id;
    private int duration;
    private Time time;
    private Resources resources;

    public Event(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

}
