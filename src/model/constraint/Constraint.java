package model.constraint;

import model.event.Event;
import model.event.Events;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlTransient
public abstract class Constraint {
    private String id;
    private boolean required;
    private int weight;
    private Events appliesTo;

    public Constraint(){

    }

    public Constraint(String id, boolean required, int weight, Events appliesTo) {
        this.id = id;
        this.required = required;
        this.weight = weight;
        this.appliesTo = appliesTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Events getAppliesTo() {
        return appliesTo;
    }

    public void setAppliesTo(Events appliesTo) {
        this.appliesTo = appliesTo;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public abstract int validate(Constraint constraint, Event event);
}
