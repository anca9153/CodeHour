package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "metadata")
public class Metadata {
    private String name;
    private String contributor;
    private Date date;
    private String description;

    public Metadata(){

    }

    public Metadata(String name, String contributor, Date date, String description) {
        this.name = name;
        this.contributor = contributor;
        this.date = date;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
