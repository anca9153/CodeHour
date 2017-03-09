package model.solution.OutLaws;

import model.solution.OutLaws.ConstraintViolatingResource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "constraintViolatingResources")
public class ConstraintViolatingResources {
    private List<ConstraintViolatingResource> cvrs;

    public ConstraintViolatingResources(){

    }

    public ConstraintViolatingResources(List<ConstraintViolatingResource> cvrs) {
        this.cvrs = cvrs;
    }

    public List<ConstraintViolatingResource> getCvrs() {
        return cvrs;
    }

    @XmlElement(name = "constraintViolatingResource")
    public void setCvrs(List<ConstraintViolatingResource> cvrs) {
        this.cvrs = cvrs;
    }
}
