package model.time;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "time")
public class Time implements Serializable{
    private Integer id = 0;
    private String name;
    private String day;
    private String hourInterval;
    private Integer numberOfTimeUnits = 0;

    public Time(){

    }

    public Time(int id, String name, String day, String hourInterval) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.hourInterval = hourInterval;
    }

    public int getId() {
        return id;
    }

    @XmlAttribute
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHourInterval() {
        return hourInterval;
    }

    public void setHourInterval(String hourInterval) {
        this.hourInterval = hourInterval;
    }

    public int getNumberOfTimeUnits() {
        return numberOfTimeUnits;
    }

    public void setNumberOfTimeUnits(int numberOfTimeUnits) {
        this.numberOfTimeUnits = numberOfTimeUnits;
    }
}
