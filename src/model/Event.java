package model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Anca on 1/17/2017.
 */
@XmlRootElement(name = "event")
public class Event {
    private StudyGroup studyGroup;
    private Subject subject;
    private Teacher teacher;
    private int duration;
    private int weekDay;
    private int hour;
    private Classroom classroom;

    public Event(){

    }

    public Event(StudyGroup studyGroup, Subject subject, Teacher teacher, int duration){
        this.studyGroup = studyGroup;
        this.subject = subject;
        this.duration = duration;
        this.teacher = teacher;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
