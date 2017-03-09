package model;

import model.time.Times;

import model.constraint.Constraints;
import model.event.Events;
import model.resource.ResourceTypes;
import model.resource.Resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 2/6/2017.
 */

@XmlRootElement(name = "timetable")
public class Timetable {
    private String id;
    private Metadata metadata;
    private Times times;
    private ResourceTypes resourceTypes;
    private Resources resources;
    private Events events;
    private Constraints constraints;

    public Timetable (){

    }

    public Timetable(String id, Metadata metadata, Times times, ResourceTypes resourceTypes, Resources resources, Events events, Constraints constraints) {
        this.id = id;
        this.metadata = metadata;
        this.times = times;
        this.resourceTypes = resourceTypes;
        this.resources = resources;
        this.events = events;
        this.constraints = constraints;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @XmlElement(name = "metadata")
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Events getEvents() {
        return events;
    }

    @XmlElement(name = "events")
    public void setEvents(Events events) {
        this.events = events;
    }

    public Times getTimes() {
        return times;
    }

    @XmlElement(name = "Times")
    public void setTimes(Times times) {
        this.times = times;
    }

    public Constraints getConstraints() {
        return constraints;
    }

    @XmlElement(name = "constraints")
    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }

    public Resources getResources() {
        return resources;
    }

    @XmlElement(name = "resources")
    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public ResourceTypes getResourceTypes() {
        return resourceTypes;
    }

    @XmlElement(name = "resourceTypes")
    public void setResourceTypes(ResourceTypes resourceTypes) {
        this.resourceTypes = resourceTypes;
    }
}
