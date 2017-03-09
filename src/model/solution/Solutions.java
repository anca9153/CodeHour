package model.solution;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "solutions")
public class Solutions {
    private List<Solution> solutions;

    public Solutions(){

    }

    public Solutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    @XmlElement(name = "solution")
    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }
}
