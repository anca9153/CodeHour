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
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty day = new SimpleStringProperty();
    private SimpleStringProperty hourInterval = new SimpleStringProperty();
    private SimpleIntegerProperty numberOfTimeUnits = new SimpleIntegerProperty();

    public Time(){

    }

    public Time(int id, String name, String day, String hourInterval) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.day = new SimpleStringProperty(day);
        this.hourInterval = new SimpleStringProperty(hourInterval);
    }

    public int getId() {
        return id.getValue();
    }

    @XmlAttribute
    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getDay() {
        return day.getValue();
    }

    public void setDay(String day) {
        this.day = new SimpleStringProperty(day);
    }

    public String getHourInterval() {
        return hourInterval.getValue();
    }

    public void setHourInterval(String hourInterval) {
        this.hourInterval = new SimpleStringProperty(hourInterval);
    }

    public int getNumberOfTimeUnits() {
        return numberOfTimeUnits.getValue();
    }

    public void setNumberOfTimeUnits(int numberOfTimeUnits) {
        this.numberOfTimeUnits = new SimpleIntegerProperty(numberOfTimeUnits);
    }
}
