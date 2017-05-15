package model;

import model.solution.Solutions;
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
    //Cat dureaza un curs, de ex.: 50 min, 60 min, 35 min
    private int basicTimeUnitInMinutes;
    private Metadata metadata;
    private Times times;
    private ResourceTypes resourceTypes;
    private Resources resources;
    private Events events;
    private Constraints eventConstraints;
    private Constraints resourceConstraints;
    private Solutions solutions;

    public Timetable (){

    }

    public Timetable(String id, Metadata metadata, Times times, ResourceTypes resourceTypes, Resources resources, Events events, Constraints eventConstraints, Constraints resourceConstraints) {
        this.id = id;
        this.metadata = metadata;
        this.times = times;
        this.resourceTypes = resourceTypes;
        this.resources = resources;
        this.events = events;
        this.setEventConstraints(eventConstraints);
        this.setResourceConstraints(resourceConstraints);
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

    @XmlElement(name = "times")
    public void setTimes(Times times) {
        this.times = times;
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

    public Solutions getSolutions() {
        return solutions;
    }

    @XmlElement(name = "solutions")
    public void setSolutions(Solutions solutions) {
        this.solutions = solutions;
    }

    public Constraints getEventConstraints() {
        return eventConstraints;
    }

    @XmlElement(name = "eventConstraints")
    public void setEventConstraints(Constraints eventConstraints) {
        this.eventConstraints = eventConstraints;
    }

    public Constraints getResourceConstraints() {
        return resourceConstraints;
    }

    @XmlElement(name = "resourceConstraints")
    public void setResourceConstraints(Constraints resourceConstraints) {
        this.resourceConstraints = resourceConstraints;
    }

    public int getBasicTimeUnitInMinutes() {
        return basicTimeUnitInMinutes;
    }

    public void setBasicTimeUnitInMinutes(int basicTimeUnitInMinutes) {
        this.basicTimeUnitInMinutes = basicTimeUnitInMinutes;
    }
}
