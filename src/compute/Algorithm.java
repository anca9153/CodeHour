package compute;

import model.Timetable;
import model.solution.Solution;

/**
 * Created by Anca on 3/9/2017.
 */
public interface Algorithm {
    Timetable solve(Timetable timetable);
}
