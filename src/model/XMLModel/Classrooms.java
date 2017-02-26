package model.XMLModel;

import model.Classroom;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
@XmlRootElement(name = "classrooms")
public class Classrooms {
    private List<Classroom> classroomList;

    public List<Classroom> getClassroomList() {
        return classroomList;
    }

    @XmlElement(name = "classroom")
    public void setClassroomList(List<Classroom> classroomList) {
        this.classroomList = classroomList;
    }
}
