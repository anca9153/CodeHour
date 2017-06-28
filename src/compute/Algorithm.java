package compute;

import model.Timetable;

/**
 * Created by Anca on 3/9/2017.
 */
public interface Algorithm {
    Timetable solve(Timetable timetable);
    Timetable addToSolution(Timetable timetable);
}
