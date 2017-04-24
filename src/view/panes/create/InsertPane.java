package view.panes.create;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Anca on 4/24/2017.
 */
public abstract class InsertPane {
    protected HBox makeLabel(String name, boolean required){
        Label l = new Label(name);
        l.getStyleClass().add("fieldRightLabel");

        HBox hb = new HBox(l);

        if(required){
            Label star = new Label("*");
            star.getStyleClass().addAll("fieldRightLabel", "redStar");
            hb.getChildren().add(star);
        }

        return hb;
    }

    protected TextField makeTextField(Pair<String, Boolean> placeHolder){
        TextField tf = new TextField();
        tf.getStyleClass().add("fieldRightTextField");
        if(placeHolder.getValue() == Boolean.TRUE) {
            tf.setPromptText(placeHolder.getKey());
        }
        else{
            tf.setText(placeHolder.getKey());
        }
        return tf;
    }

    protected void clearErrors(List<HBox> labels, List<TextField> textFields){
        for(HBox hb : labels){
            if(hb.getChildren().size()>=3){
                for(Node n: hb.getChildren()){
                    if(n.getId()!=null && n.getId().equals("error")){
                        hb.getChildren().remove(n);
                        break;
                    }
                }
            }
        }

        for(TextField tf : textFields){
            for(String s: tf.getStyleClass()){
                if(s.equals("addRedMargin")){
                    tf.getStyleClass().remove(s);
                    break;
                }
            }
        }
    }

    protected void showErrorMessage(HBox label, String message, Node textField){
        if(label.getChildren().size()<3) {
            Label error = new Label(message);
            error.getStyleClass().addAll("fieldRightLabel", "redStar", "normalFont");

            HBox hb = new HBox(error);
            label.getChildren().add(hb);
            hb.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(hb, Priority.ALWAYS );
            hb.setId("error");
        }
        if(textField.getStyleClass().size()<4){
            textField.getStyleClass().add("addRedMargin");
        }
    }

}
