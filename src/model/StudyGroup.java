package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 1/17/2017.
 */
@XmlRootElement(name = "studyGroup")
public class StudyGroup {
    private String name;
    private int numberOfParticipants;

    public StudyGroup(){

    }

    public StudyGroup(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(other == this) return true;
        StudyGroup sg = (StudyGroup) other;
        if(sg.getName().matches(this.name)) return true;
        return false;
    }
}
