package model.solution;

import model.solution.OutLaws.ConstraintViolatingResources;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "report")
public class Report {
    private int infesabilityValue;
    private int objectiveValue;
    private ConstraintViolatingResources cvrs;

    public Report(){

    }

    public Report(int infesabilityValue, int objectiveValue, ConstraintViolatingResources cvrs) {
        this.infesabilityValue = infesabilityValue;
        this.objectiveValue = objectiveValue;
        this.cvrs = cvrs;
    }

    public int getInfesabilityValue() {
        return infesabilityValue;
    }

    public void setInfesabilityValue(int infesabilityValue) {
        this.infesabilityValue = infesabilityValue;
    }

    public int getObjectiveValue() {
        return objectiveValue;
    }

    public void setObjectiveValue(int objectiveValue) {
        this.objectiveValue = objectiveValue;
    }

    public ConstraintViolatingResources getCvrs() {
        return cvrs;
    }

    @XmlElement(name = "constraintViolatingResources")
    public void setCvrs(ConstraintViolatingResources cvrs) {
        this.cvrs = cvrs;
    }
}
