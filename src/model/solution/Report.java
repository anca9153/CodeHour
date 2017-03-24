package model.solution;

import model.solution.OutLaws.ConstraintViolatingEvents;
import model.solution.OutLaws.ConstraintViolatingResources;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "report")
public class Report {
    private int infeasibilityValue;
    private ConstraintViolatingResources cvrs;
    private ConstraintViolatingEvents cves;

    public Report(){

    }

    public Report(int infeasibilityValue, ConstraintViolatingResources cvrs) {
        this.setInfeasibilityValue(infeasibilityValue);
        this.cvrs = cvrs;
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

    public ConstraintViolatingEvents getCves() {
        return cves;
    }

    @XmlElement(name = "constraintViolatingEvents")
    public void setCves(ConstraintViolatingEvents cves) {
        this.cves = cves;
    }

}
