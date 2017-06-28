package model.constraint;

import model.event.Event;
import model.event.Events;
import model.resource.Resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlTransient
public abstract class Constraint {
    protected String id;
    protected Boolean required;
    protected int weight;
    protected Events appliesToEvents;
    protected Resources appliesToResources;

    public Constraint(){

    }

    public Constraint(String id, boolean required, int weight, Events appliesToEvents, Resources appliesToResources) {
        this.id = id;
        this.required = required;
        this.weight = weight;
        this.appliesToEvents = appliesToEvents;
        this.appliesToResources = appliesToResources;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Events getAppliesToEvents() {
        return appliesToEvents;
    }

    public void setAppliesToEvents(Events appliesToEvents) {
        this.appliesToEvents = appliesToEvents;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Resources getAppliesToResources() {
        return appliesToResources;
    }

    public void setAppliesToResources(Resources appliesToResources) {
        this.appliesToResources = appliesToResources;
    }

    public abstract int validate(Event event);
}
