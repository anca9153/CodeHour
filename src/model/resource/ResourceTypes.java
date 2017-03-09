package model.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "resourceTypes")
public class ResourceTypes {
    private List<String> resourceTypes;

    public ResourceTypes(){

    }

    public ResourceTypes(List<String> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }

    public List<String> getResourceTypes() {
        return resourceTypes;
    }

    @XmlElement(name = "resourceType")
    public void setResourceTypes(List<String> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }
}
