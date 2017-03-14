package model.constraint;

import model.constraint.types.AssignResourceConstraint;
import model.constraint.types.AssignTimeConstraint;
import model.constraint.types.LimitIdleTimesConstraint;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Connection;
import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */

@XmlRootElement(name = "constraints")
public class Constraints {
    private List<Constraint> constraints;

    public Constraints(){

    }

    public Constraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    @XmlElements({
            @XmlElement(name = "assignResourceConstraint", type=AssignResourceConstraint.class),
            @XmlElement(name = "assignTimeConstraint", type=AssignTimeConstraint.class),
            @XmlElement(name = "limitIdleTimeConstraint", type=LimitIdleTimesConstraint.class)
    })
    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }
}
