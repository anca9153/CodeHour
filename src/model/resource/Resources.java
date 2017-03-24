package model.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "resources")
public class Resources implements Serializable{
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

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(other == this) return true;

        Resources rs = (Resources) other;
        List<Resource> newList = new ArrayList<>(rs.getResources());
        newList.removeAll(this.getResources());
        return newList.size() == 0 ? true : false;
    }

}
