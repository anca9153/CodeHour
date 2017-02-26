package model;

import model.XMLModel.Subjects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 1/17/2017.
 */
@XmlRootElement(name = "teacher")
public class Teacher {
    private String name;
    private String firstName;
    private String lastName;
//    private Subjects subjects;

    public Teacher(){

    }

    public Teacher(String name, String firstName, String lastName){
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//    public Subjects getSubjects() {
//        return subjects;
//    }
//
//    public void setSubjects(Subjects subjects) {
//        this.subjects = subjects;
//    }

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(other == this) return true;
        Teacher t = (Teacher) other;
        if(t.getName().matches(this.name)) return true;
        return false;
    }
}
