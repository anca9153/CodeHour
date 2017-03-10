package model.constraint;

import model.constraint.types.AssignResourceConstraint;
import model.constraint.types.AssignTimeConstraint;

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
            @XmlElement(name = "constraint", type=AssignResourceConstraint.class),
            @XmlElement(name = "constraint", type=AssignTimeConstraint.class)
    })
    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }
}
