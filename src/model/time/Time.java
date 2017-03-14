package model.time;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Comparator;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "time")
public class Time {
    private String id;
    private String day;

    public Time(){

    }

    public Time(String id, String day) {
        this.id = id;
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(other == this) return true;

        Time t = (Time) other;
        if(t.getId().matches(this.id)) return true;
        return false;
    }
}
