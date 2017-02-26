package model.XMLModel;

import model.StudyGroup;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
@XmlRootElement(name = "studyGroups")
public class StudyGroups {
    private List<StudyGroup> studyGroupList;

    public List<StudyGroup> getStudyGroupList() {
        return studyGroupList;
    }

    @XmlElement(name = "studyGroup")
    public void setStudyGroupList(List<StudyGroup> studyGroupList) {
        this.studyGroupList = studyGroupList;
    }

}
