package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Created by Anca on 1/17/2017.
 */

@XmlRootElement(name = "subject")
public class Subject {
    private String name;
    private int studyYear;

    public Subject(){

    }

    public Subject(String name, int studyYear){
        this.name = name;
        this.studyYear = studyYear;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(int studyYear) {
        this.studyYear = studyYear;
    }
}
