package utilities.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import model.Timetable;
import model.event.Event;
import model.event.Events;
import model.solution.Solution;
import model.time.Time;
import utilities.PropertiesLoader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Anca on 5/18/2017.
 */
public class FreeMarkerDataLoader {

    private Timetable timetable;
    private Solution solution;
    private Map<String, List<Event>> studyGroupEvents;
    private Map<String, List<Event>> teacherEvents;
    private Map<String, List<Event>> classroomEvents;

    public FreeMarkerDataLoader(Timetable timetable, Solution solution, Map<String, List<Event>> studyGroupEvents, Map<String, List<Event>> teacherEvents, Map<String, List<Event>> classroomEvents){
        this.timetable = timetable;
        this.solution = solution;
        this.studyGroupEvents = studyGroupEvents;
        this.teacherEvents = teacherEvents;
        this.classroomEvents = classroomEvents;
    }

    public void generateReports() {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(FreeMarkerDataLoader.class, "templates");

        String folderName = null;

        try {
            long count = Files.find(Paths.get("UserFiles\\Reports"), 1, (path, attributes) -> attributes.isDirectory()).count();
            folderName = new String("report_" + count);
            Files.createDirectory(Paths.get(PropertiesLoader.loadReportsFolderPath() + folderName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeMainPage(cfg, folderName);
        writeResourcePages(cfg, folderName, "studyGroup", studyGroupEvents);
        writeResourcePages(cfg, folderName, "teacher", teacherEvents);
        writeResourcePages(cfg, folderName, "classroom", classroomEvents);
    }

    private void writeMainPage(Configuration cfg, String folderName){
        Map<String, Object> input = new HashMap<>();
        input.put("title", solution.getDescription());

        List<String> studyGroups = new ArrayList<>(studyGroupEvents.keySet());
        List<String> teachers = new ArrayList<>(teacherEvents.keySet());
        List<String> classrooms = new ArrayList<>(classroomEvents.keySet());

        List<String> studyGroupLinks = new ArrayList<>();
        for(String s: studyGroupEvents.keySet()){
            studyGroupLinks.add(s+".html");
        }

        List<String> teacherLinks = new ArrayList<>();
        for(String s: teacherEvents.keySet()){
            s = s.replaceAll("\\s+","");
            teacherLinks.add(s+".html");
        }

        List<String> classroomLinks = new ArrayList<>();
        for(String s: classroomEvents.keySet()){
            classroomLinks.add(s+".html");
        }

        input.put("studyGroups", studyGroups);
        input.put("teachers", teachers);
        input.put("classrooms", classrooms);

        input.put("studyGroupLinks", studyGroupLinks);
        input.put("teacherLinks", teacherLinks);
        input.put("classroomLinks", classroomLinks);

        try{
            Template templateMainPage = cfg.getTemplate("mainPage.ftl");
            Writer fileWriterMainPage = new FileWriter(new File(PropertiesLoader.loadReportsFolderPath()+folderName+"\\mainPage.html"));
            templateMainPage.process(input, fileWriterMainPage);
            fileWriterMainPage.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    private void writeResourcePages(Configuration cfg, String folderName, String resourceType, Map<String, List<Event>> resourceEvents){
        Map<String, Object> input = new HashMap<>();
        input.put("title", solution.getDescription());

        try{
            Template template = cfg.getTemplate(resourceType+".ftl");
            for(String s: resourceEvents.keySet()) {
                input.put("resourceId", s);

                //Sorting the list by days
                List<Event> eventsByDays = new ArrayList<>(resourceEvents.get(s));
                sortEventListByTime(eventsByDays);

                input.put("events", eventsByDays);
                s = s.replaceAll("\\s+","");
                Writer fileWriter = new FileWriter(new File(PropertiesLoader.loadReportsFolderPath() + folderName + "\\" + s + ".html"));
                template.process(input, fileWriter);
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    private List<Event> sortEventListByTime(List<Event> eventList){
        //Sorting the events for the selected resource by their time
        Collections.sort(eventList, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                Time t1 = e1.getTime();
                Time t2 = e2.getTime();
                return t1.getId() - t2.getId();
            }
        });

        return eventList;
    }
}
