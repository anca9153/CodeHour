package compute;

import model.*;
import model.XMLModel.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
public class DataLoad {
    private static String loadPath = new String("D:\\Dropbox\\Licenta\\CodeHour (github)\\CodeHour\\CodeHourXMLs\\");

    public static Events loadDataFromXML(){
        Events events = new Events();
        try{
            //Loading subjects
            File file = new File(loadPath + "subjects.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Subjects.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Subjects subjects = (Subjects) jaxbUnmarshaller.unmarshal(file);
            List<Subject> subjectList = subjects.getSubjectList();
//            System.out.println("/nSubject list:");
//            for(Subject subject : subjectList){
//                System.out.println(subject.getName());
//            }

            //Loading studyGroups
            file = new File(loadPath + "studyGroups.xml");
            jaxbContext = JAXBContext.newInstance(StudyGroups.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StudyGroups studyGroups = (StudyGroups) jaxbUnmarshaller.unmarshal(file);
            List<StudyGroup> studyGroupsList = studyGroups.getStudyGroupList();
//            System.out.println("/nStudyGroup list:");
//            for(StudyGroup sg: studyGroupsList){
//                System.out.println(sg.getName());
//            }

            //Loading teachers
            file = new File(loadPath + "teachers.xml");
            jaxbContext = JAXBContext.newInstance(Teachers.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Teachers teachers = (Teachers) jaxbUnmarshaller.unmarshal(file);
            List<Teacher> teachersList = teachers.getTeacherList();
//            System.out.println("/nTeacher list:");
//            for(Teacher teacher: teachersList){
//                System.out.println(teacher.getName());
//            }

            //Loading events
            file = new File(loadPath + "events.xml");
            jaxbContext = JAXBContext.newInstance(Events.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            events = (Events) jaxbUnmarshaller.unmarshal(file);
            List<Event> eventsList= events.getEventList();
//            System.out.println("/nEvent list:");
//            for(Event event: eventsList){
//                System.out.println(event.getStudyGroup()+" "+event.getSubject());
//            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return events;
    }

    public static Events loadDataToXML() {
        //Adding classrooms
        Classrooms classrooms = new Classrooms();
        classrooms.setClassroomList(Arrays.asList(new Classroom("101"), new Classroom("102"),
                new Classroom("103"), new Classroom("104"), new Classroom("201"), new Classroom("202"),
                new Classroom("203"), new Classroom("204"), new Classroom("301"), new Classroom("302"),
                new Classroom("303"), new Classroom("304")));
        try {
            File classroomsFile = new File(loadPath + "classrooms.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Classrooms.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(classrooms, classroomsFile);
//            marshaller.marshal(classrooms, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //Adding studyGroups
        StudyGroups studyGroups = new StudyGroups();
        StudyGroup sg12A = new StudyGroup("12A");
        StudyGroup sg12B = new StudyGroup("12B");
        StudyGroup sg10B = new StudyGroup("10B");
        studyGroups.setStudyGroupList(Arrays.asList(sg12A, sg12B, sg10B));

        try {
            File studyGroupsFile = new File(loadPath + "studyGroups.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(StudyGroups.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(studyGroups, studyGroupsFile);
//            marshaller.marshal(studyGroups, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //Adding subjects
        Subjects subjects = new Subjects();
        Subject sMate = new Subject("Matematica", 12);
        Subject sInfo = new Subject("Informatica", 12);
        Subject sEng = new Subject("Engleza", 10);
        subjects.setSubjectList(Arrays.asList(sMate, sInfo, sEng));

        try {
            File subjectsFile = new File(loadPath + "subjects.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Subjects.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(subjects, subjectsFile);
//            marshaller.marshal(subjects, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //Adding teachers
        Teachers teachers = new Teachers();
        Teacher dpop = new Teacher("dpop", "Dan", "Pop");
        Teacher ctns = new Teacher("ctns", "Catalina", "Ilas");
        Teacher vhariuc = new Teacher("vhariuc", "Virgil", "Hariuc");
        teachers.setTeacherList(Arrays.asList(dpop, ctns, vhariuc));

        try {
            File teachersFile = new File(loadPath + "teachers.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Teachers.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(teachers, teachersFile);
//            marshaller.marshal(teachers, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //Add events
        Events events = new Events();
        events.setEventList(Arrays.asList(new Event(sg12A, sMate, dpop, 60), new Event(sg12B, sInfo, ctns, 60), new Event(sg10B, sEng, vhariuc, 60)));

        try {
            File eventsFile = new File(loadPath + "events.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Events.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(events, eventsFile);
//            marshaller.marshal(events, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return events;
    }

        public static Classrooms getClassrooms(){
            Classrooms classrooms = new Classrooms();
            try {
            //Loading classrooms
            File file = new File(loadPath + "classrooms.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Classrooms.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            classrooms = (Classrooms) jaxbUnmarshaller.unmarshal(file);
            List<Classroom> classroomsList= classrooms.getClassroomList();
//            System.out.println("/nClassroom list:");
//            for(Classroom cls : classroomsList){
//                System.out.println(cls.getName());
//            }

            } catch (JAXBException e) {
                e.printStackTrace();
            }

            return classrooms;
    }

    public static void loadProgrammedEventsToXML(Events events){
        try {
            File eventsFile = new File(loadPath + "programmedEvents.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Events.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(events, eventsFile);
//            marshaller.marshal(events, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
