package model.solution.OutLaws;

import model.constraint.Constraint;
import model.resource.Resource;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "constraintViolatingResource")
public class ConstraintViolatingResource {
    private Resource resource;
    private Constraint constraint;
    private int cost;

    public ConstraintViolatingResource(){

    }

    public ConstraintViolatingResource(Resource resource, Constraint constraint, int cost) {
        this.resource = resource;
        this.constraint = constraint;
        this.cost = cost;
    }
}
