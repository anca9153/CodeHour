package model.resource;

import model.resource.Resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "resources")
public class Resources {
    private List<Resource> resources;

    public Resources(){

    }

    public Resources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Resource> getResources() {
        return resources;
    }

    @XmlElement(name = "resource")
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

}
