package model.XMLModel;

import model.Subject;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;

/**
 * Created by Anca on 1/17/2017.
 */

@XmlRootElement(name = "subjects")
public class Subjects {
    private List<Subject> subjectList;

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    @XmlElement(name = "subject")
    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }
}
