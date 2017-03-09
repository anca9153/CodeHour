package model.solution;

import model.solution.OutLaws.ConstraintViolatingResources;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "report")
public class Report {
    private int infeasibilityValue;
    private int objectiveValue;
    private ConstraintViolatingResources cvrs;

    public Report(){

    }

    public Report(int infeasibilityValue, int objectiveValue, ConstraintViolatingResources cvrs) {
        this.setInfeasibilityValue(infeasibilityValue);
        this.objectiveValue = objectiveValue;
        this.cvrs = cvrs;
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

    public int getInfeasibilityValue() {
        return infeasibilityValue;
    }

    public void setInfeasibilityValue(int infeasibilityValue) {
        this.infeasibilityValue = infeasibilityValue;
    }
}
