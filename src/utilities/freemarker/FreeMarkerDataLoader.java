package utilities.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import model.Timetable;
import model.event.Event;
import model.solution.Solution;
import utilities.PropertiesLoader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Anca on 5/18/2017.
 */
public class FreeMarkerDataLoader {

    public static void generateReports(Timetable timetable, Solution solution, Map<String, List<Event>> studyGroupEvents, Map<String, List<Event>> teacherEvents, Map<String, List<Event>> classroomEvents) {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(FreeMarkerDataLoader.class, "templates");

        Map<String, Object> input = new HashMap<>();
        input.put("title", solution.getDescription());

        List<String> studyGroups = new ArrayList<>(studyGroupEvents.keySet());
        List<String> teachers = new ArrayList<>(teacherEvents.keySet());
        List<String> classrooms = new ArrayList<>(classroomEvents.keySet());

        input.put("studyGroups", studyGroups);
        input.put("teachers", teachers);
        input.put("classrooms", classrooms);

        try{
            long count = Files.find(Paths.get("UserFiles\\Reports"), 1, (path, attributes) -> attributes.isDirectory()).count();
            String folderName = new String("report_"+count);
            Files.createDirectory(Paths.get(PropertiesLoader.loadReportsFolderPath()+folderName));

            Template template = cfg.getTemplate("mainPage.ftl");
            Writer fileWriter = new FileWriter(new File(PropertiesLoader.loadReportsFolderPath()+folderName+"\\mainPage.html"));
            template.process(input, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
