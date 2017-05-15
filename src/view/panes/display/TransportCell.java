package view.panes.display;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Anca on 5/3/2017.
 */
public class TransportCell implements Serializable {
    public List<Node> nodes;

    public TransportCell(VBox vBox){
        nodes = vBox.getChildren();
    }
}
