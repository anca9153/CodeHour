package model.solution.OutLaws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Anca on 3/24/2017.
 */
@XmlRootElement(name = "constraintViolatingEvents")
public class ConstraintViolatingEvents implements Serializable {
    private List<ConstraintViolatingEvent> cves;

    public ConstraintViolatingEvents(){

    }

    public ConstraintViolatingEvents(List<ConstraintViolatingEvent> cves) {
        this.cves = cves;
    }

    public List<ConstraintViolatingEvent> getCves() {
        return cves;
    }

    @XmlElement(name = "constraintViolatingEvent")
    public void setCves(List<ConstraintViolatingEvent> cves) {
        this.cves = cves;
    }
}
