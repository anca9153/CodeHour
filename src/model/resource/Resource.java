package model.resource;

import model.FitForConstraint;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "resource")
public class Resource extends FitForConstraint{
    private String id;
    private String name;
    private String resourceType;

    public Resource(){

    }

    public Resource(String id,String name, String resourceType) {
        this.id = id;
        this.name = name;
        this.resourceType = resourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(other == this) return true;

        Resource r = (Resource) other;
        if(r.getId().matches(this.getId())) return true;
        return false;
    }
}
