package model.XMLModel;

import model.Constraint;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */

@XmlRootElement(name = "constraints")
public class Constraints {
    private List<Constraint> constraints;

    public List<Constraint> getConstraints() {
        return constraints;
    }

    @XmlElement(name = "constraint")
    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }
}
