package model.XMLModel;

import model.Teacher;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
@XmlRootElement(name = "teachers")
public class Teachers {
    private List<Teacher> teacherList;

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    @XmlElement(name = "Teacher")
    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }
}
