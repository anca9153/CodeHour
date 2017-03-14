package model.time;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "times")
public class Times {
    private List<Time> times;

    public Times(){

    }

    public Times(List<Time> times) {
        this.times = times;
    }

    public List<Time> getTimes() {
        return times;
    }

    @XmlElement(name = "time")
    public void setTimes(List<Time> times) {
        this.times = times;
    }
}
