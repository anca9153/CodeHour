package view.panes.create;

import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import utilities.DataLoader;
import view.panes.CreatePane;
import java.util.List;

/**
 * Created by Anca on 4/24/2017.
 */
public abstract class InsertPane {
    protected Stage primaryStage;
    protected HBox saveButtonHB;

    protected Label getTitleLabel(String titleString){
        Label title = new Label(titleString);
        title.getStyleClass().add("resourceText");
        return title;
    }

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

    protected FlowPane getFlowPane(ObservableList<VBox> vbList){
        FlowPane fp = new FlowPane();

        for(VBox vb : vbList){
            vb.getStyleClass().add("fieldRight");
            fp.getChildren().add(vb);
        }

        fp.setHgap(40);
        fp.setVgap(20);

        fp.getStyleClass().add("rightFlowPane");

        return fp;
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

    protected void saveIntoFile(){
        if (CreatePane.savingFile == null) {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            CreatePane.savingFile = fileChooser.showSaveDialog(primaryStage);
        }

        if (CreatePane.savingFile != null) {
            DataLoader.loadSolvedTimetableToXMLWithPath(CreatePane.timetable, CreatePane.savingFile);

            Label confSave = new Label("Datele au fost salvate. ");
            confSave.getStyleClass().addAll("fieldRightLabel","explanatory1");
            confSave.setId("confSave");

            for(Node n: saveButtonHB.getChildren()){
                if(n.getId()!=null && n.getId().equals("confSave")){
                    saveButtonHB.getChildren().remove(n);
                    break;
                }
            }

            if(saveButtonHB.getChildren().size() < 2) {
                saveButtonHB.getChildren().add(0, confSave);
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(5)
                );
                visiblePause.setOnFinished(
                        e -> confSave.setVisible(false)
                );
                visiblePause.play();
            }
        }
    }

    protected void addSaveButtonIntoHBox(Button saveButton){
        saveButton.setAlignment(Pos.CENTER_RIGHT);

        saveButtonHB = new HBox();
        saveButtonHB.getChildren().add(saveButton);
        saveButtonHB.getStyleClass().add("rightSaveHBox");
        saveButtonHB.setAlignment(Pos.CENTER_RIGHT);
    }

    protected HBox createExplanatory(){
        Label expl1 = new Label("Câmpurile marcate cu ");
        expl1.getStyleClass().addAll("fieldRightLabel","explanatory1");
        Label star = new Label("*");
        star.getStyleClass().addAll("fieldRightLabel","redStar");
        Label expl2 = new Label(" sunt necesare.");
        expl2.getStyleClass().addAll("fieldRightLabel","explanatory1");
        HBox explanatory = new HBox(expl1, star, expl2);
        explanatory.getStyleClass().addAll("fieldRightLabel", "explanatory");

        return explanatory;
    }
}
