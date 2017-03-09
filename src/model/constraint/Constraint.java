package model;

import model.XMLModel.Resources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "constraint")
public class Constraint {
    private String id;
    private String required;
    private Resources appliesTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public Resources getAppliesTo() {
        return appliesTo;
    }

    public void setAppliesTo(Resources appliesTo) {
        this.appliesTo = appliesTo;
    }
}
