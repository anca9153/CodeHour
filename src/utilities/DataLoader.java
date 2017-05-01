package utilities;

import model.Metadata;
import model.Timetable;
import model.constraint.Constraint;
import model.constraint.Constraints;
import model.constraint.types.AssignResourceConstraint;
import model.constraint.types.AssignTimeConstraint;
import model.constraint.types.LimitIdleTimesConstraint;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.ResourceTypes;
import model.resource.Resources;
import model.time.Time;
import model.time.Times;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
public class DataLoader {

    public static Timetable loadDataFromXML(String fileName){
     return loadDataFromXMLWithPath(new File(PropertiesLoader.loadXMLLocationFolder() + fileName));
    }

    public static Timetable loadDataFromXMLWithPath(File file){
        Timetable timetable = new Timetable();
        try{
            //Loading timetable
            JAXBContext jaxbContext = JAXBContext.newInstance(Timetable.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            timetable= (Timetable) jaxbUnmarshaller.unmarshal(file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return timetable;
    }

    public static void loadSolvedTimetableToXML(Timetable timetable, String fileName){
        loadSolvedTimetableToXMLWithPath(timetable, new File(new String(PropertiesLoader.loadXMLLocationFolder() + fileName)));
    }

    public static void loadSolvedTimetableToXMLWithPath(Timetable timetable, File timetableFile){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Timetable.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(timetable, timetableFile);
//            marshaller.marshal(events, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static Timetable loadDataToXML(String location) {
        Metadata metadata = new Metadata("Example Timetable", "Anca Ad", new Date(), "");

        List<String> weekdays = Arrays.asList("monday", "tuesday", "wednesday","thursday", "friday");
        List<Time> timeList = new ArrayList<>();

        int i = 1;
        for(int day = 0; day<5; day++) {
            for (int hour = 0; hour < 3; hour++) {
                StringBuilder sb = new StringBuilder(String.valueOf(hour+8));
                sb.append(":00 - ");
                sb.append(String.valueOf(hour+8));
                sb.append(":50");
                Time time = new Time(i++, new StringBuilder().append(String.valueOf(hour+1)).append("_").append(weekdays.get(day).substring(0, 3)).toString(), weekdays.get(day), sb.toString());
                timeList.add(time);
            }
        }

        Times times = new Times();
        times.setTimes(timeList);

        List<String> rtList = Arrays.asList("teacher", "studyGroup", "classroom");
        ResourceTypes resourceTypes = new ResourceTypes(rtList);

        List<Resource> resourceList = Arrays.asList(
                //Teachers
                new Resource("dpop", "Dan Pop", rtList.get(0)),
                new Resource("catnis", "Catalina Ilas", rtList.get(0)),
                new Resource("vhariuc", "Virgil Hariuc", rtList.get(0)),
                new Resource("asava", "Ana Savascu", rtList.get(0)),
                new Resource("doniciuc", "Daniel Oniciuc", rtList.get(0)),
                new Resource("anac", "Ana Crin", rtList.get(0)),
                new Resource("stm", "Maria Stejar", rtList.get(0)),
                new Resource("cris", "Cristian Sava", rtList.get(0)),
                //StudyGroups
                new Resource("9A", "", rtList.get(1)),
                new Resource("9B", "", rtList.get(1)),
                new Resource("10A", "", rtList.get(1)),
                new Resource("10B", "", rtList.get(1)),
                new Resource("11A", "", rtList.get(1)),
                new Resource("11B", "", rtList.get(1)),
                new Resource("12A", "", rtList.get(1)),
                new Resource("12B", "", rtList.get(1)),
                //Classrooms
                new Resource("101", "", rtList.get(2)),
                new Resource("102", "", rtList.get(2)),
                new Resource("103", "", rtList.get(2)),
                new Resource("104", "", rtList.get(2)),
                new Resource("105", "", rtList.get(2)),
                new Resource("201", "", rtList.get(2)),
                new Resource("202", "", rtList.get(2)),
                new Resource("203", "", rtList.get(2)),
                new Resource("204", "", rtList.get(2)),
                new Resource("205", "", rtList.get(2)),
                new Resource("301", "", rtList.get(2)),
                new Resource("302", "", rtList.get(2))
        );

        Resources resources = new Resources(resourceList);

        List<Event> eventList = Arrays.asList(
                new Event("ev1", 1, null, new Resources(Arrays.asList(resourceList.get(0), resourceList.get(8), resourceList.get(16))), "Mate"),
                new Event("ev2", 1, null, new Resources(Arrays.asList(resourceList.get(0), resourceList.get(8), resourceList.get(16))), "Mate"),
                new Event("ev3", 1, null, new Resources(Arrays.asList(resourceList.get(0), resourceList.get(9), resourceList.get(17))), "Mate"),
                new Event("ev4", 1, null, new Resources(Arrays.asList(resourceList.get(0), resourceList.get(9), resourceList.get(17))), "Mate"),
                new Event("ev5", 1, null, new Resources(Arrays.asList(resourceList.get(0), resourceList.get(10), resourceList.get(18))), "Mate"),
                new Event("ev6", 1, null, new Resources(Arrays.asList(resourceList.get(1), resourceList.get(15), resourceList.get(19))), "Info"),
                new Event("ev7", 1, null, new Resources(Arrays.asList(resourceList.get(1), resourceList.get(14), resourceList.get(20))), "Info"),
                new Event("ev8", 1, null, new Resources(Arrays.asList(resourceList.get(1), resourceList.get(13), resourceList.get(21))), "Info"),
                new Event("ev9", 1, null, new Resources(Arrays.asList(resourceList.get(2), resourceList.get(13), resourceList.get(21))), "Info"),
                new Event("ev10", 1, null, new Resources(Arrays.asList(resourceList.get(2), resourceList.get(14), resourceList.get(20))), "Info"),
                new Event("ev11", 1, null, new Resources(Arrays.asList(resourceList.get(2), resourceList.get(12), resourceList.get(22))), "Info"),
                new Event("ev12", 1, null, new Resources(Arrays.asList(resourceList.get(3), resourceList.get(12), resourceList.get(22))), "Eng"),
                new Event("ev13", 1, null, new Resources(Arrays.asList(resourceList.get(3), resourceList.get(11), resourceList.get(23))), "Eng"),
                new Event("ev14", 1, null, new Resources(Arrays.asList(resourceList.get(4), resourceList.get(12), resourceList.get(22))), "Lb. Rom"),
                new Event("ev15", 1, null, new Resources(Arrays.asList(resourceList.get(4), resourceList.get(11), resourceList.get(23))), "Lb. Rom"),
                new Event("ev16", 1, null, new Resources(Arrays.asList(resourceList.get(5), resourceList.get(10), resourceList.get(18))), "Sport"),
                new Event("ev17", 1, null, new Resources(Arrays.asList(resourceList.get(6), resourceList.get(15), resourceList.get(19))), "Arte"),
                new Event("ev18", 1, null, new Resources(Arrays.asList(resourceList.get(7), resourceList.get(15), resourceList.get(19))), "Arte"),
                new Event("ev19", 1, null, new Resources(Arrays.asList(resourceList.get(7), resourceList.get(10), resourceList.get(18))), "Arte"),
                new Event("ev20", 1, null, new Resources(Arrays.asList(resourceList.get(7), resourceList.get(9), resourceList.get(17))), "Arte")
        );

        Events events = new Events(eventList);

        List<Constraint> eventConstraintList = Arrays.asList(
                new AssignResourceConstraint("assignResourceConstraint", true, 1, events, null),
                new AssignTimeConstraint("assignTimeConstraint", true, 1, events, null)
                );

        Constraints eventConstraints = new Constraints(eventConstraintList);


        List<Constraint> resourceConstraintList = Arrays.asList(
                new LimitIdleTimesConstraint("limitIdleTimeConstraint", true, 1, 1, null, resources)
        );

        Constraints resourceConstraints = new Constraints(resourceConstraintList);


        Timetable timetable = new Timetable("exampleTimetable", metadata, times, resourceTypes, resources, events, eventConstraints, resourceConstraints);

        try {
            File timetableFile = new File(PropertiesLoader.loadXMLLocationFolder() + location);
            JAXBContext jaxbContext = JAXBContext.newInstance(Timetable.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(timetable, timetableFile);
//            marshaller.marshal(events, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return timetable;
    }
}
