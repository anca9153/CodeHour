package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 1/17/2017.
 */
@XmlRootElement(name = "classroom")
public class Classroom {
    private String name;
    private String location;

    public Classroom(){

    }

    public Classroom(String name){
        this.name = name;
    }

    public Classroom(String name, String location){
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(other == this) return true;
        Classroom c = (Classroom) other;
        if(c.getName().matches(this.name)) return true;
        return false;
    }
}
