package view.panes;

import compute.Algorithm;
import compute.algorithms.GradingAlgorithm;
import javafx.animation.PauseTransition;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Metadata;
import model.Timetable;
import model.constraint.Constraint;
import model.constraint.Constraints;
import model.constraint.types.AssignResourceConstraint;
import model.constraint.types.AssignTimeConstraint;
import model.constraint.types.LimitIdleTimesConstraint;
import model.resource.ResourceTypes;
import utilities.XMLDataLoader;
import utilities.PropertiesLoader;
import view.StageLoader;
import view.panes.create.*;
import java.io.File;
import java.util.*;

/**
 * Created by Anca on 3/16/2017.
 */
public class CreatePane extends MainPane {

    private Stage primaryStage;
    public static Timetable timetable;
    private ObservableList<ListView> listViewList = FXCollections.observableArrayList();
    public static File savingFile;
    private File loadedFile;
    private HBox leftToolbar;
    private String currentOption = new String();
    private VBox generateVBox;

    public CreatePane(Stage primaryStage, Timetable timetable){
        this.primaryStage = primaryStage;
        this.timetable = timetable;

        addToolbar();
        clearSelections(" ");
        addLeftOptions();

        timetable.setMetadata(new Metadata());

        timetable.getMetadata().setDate(new Date());
    }

    private void addToolbar(){
        //Clearing the listview list
        listViewList.clear();

        Button loadTimetable = new Button("Alege");

        loadTimetable.setOnAction((ActionEvent event) ->
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmare");
                alert.setHeaderText("Încărcarea unui orar implică pierderea oricărei informații nesalvate!");
                alert.setContentText("Doriți încărcarea unui orar?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialDirectory(new File(PropertiesLoader.loadXMLLocationFolder()));

                    //Set extension filter
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show load file dialog
                    loadedFile = fileChooser.showOpenDialog(primaryStage);

                    if (loadedFile != null) {
                        savingFile = null;
                        timetable = XMLDataLoader.loadDataFromXMLWithPath(loadedFile);

                        Label confLoad = new Label(" Orarul a fost încărcat.");
                        confLoad.getStyleClass().addAll("fieldRightLabel", "explanatory1");
                        confLoad.setId("confLoad");

                        for (Node n : leftToolbar.getChildren()) {
                            if (n.getId() != null && n.getId().equals("confLoad")) {
                                leftToolbar.getChildren().remove(n);
                                break;
                            }
                        }

                        leftToolbar.getChildren().add(confLoad);
                        PauseTransition visiblePause1 = new PauseTransition(
                                Duration.seconds(5)
                        );
                        visiblePause1.setOnFinished(
                                e -> {
                                    for (Node n : leftToolbar.getChildren()) {
                                        if (n.getId() != null && n.getId().equals("confLoad")) {
                                            leftToolbar.getChildren().remove(n);
                                            break;
                                        }
                                    }
                                }
                        );
                        visiblePause1.play();

                        addRightCreatePane(currentOption);
                    }
                }
            }
        );
        loadTimetable.getStyleClass().add("settingsButton");

        Button newTimetableButton = new Button("Orar nou");

        newTimetableButton.setOnAction((ActionEvent event) ->
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmare");
                alert.setHeaderText("Crearea unui nou orar implică pierderea oricărei informații nesalvate!");
                alert.setContentText("Doriți crearea unui nou orar?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    savingFile = null;
                    timetable = new Timetable();
                    List<String> resourceTypesList = Arrays.asList("teacher", "studyGroup", "classroom");
                    ResourceTypes resourceTypes = new ResourceTypes(resourceTypesList);
                    timetable.setResourceTypes(resourceTypes);

                    Label confNew = new Label(" Orarul nou a fost creat.");
                    confNew.getStyleClass().addAll("fieldRightLabel", "explanatory1");
                    confNew.setId("confNew");
                    confNew.setId("confNew");

                    for (Node n : leftToolbar.getChildren()) {
                        if (n.getId() != null && n.getId().equals("confNew")) {
                            leftToolbar.getChildren().remove(n);
                            break;
                        }
                    }

                    leftToolbar.getChildren().add(confNew);
                    PauseTransition visiblePause2 = new PauseTransition(
                            Duration.seconds(5)
                    );
                    visiblePause2.setOnFinished(
                            e -> {
                                for (Node n : leftToolbar.getChildren()) {
                                    if (n.getId() != null && n.getId().equals("confNew")) {
                                        leftToolbar.getChildren().remove(n);
                                        break;
                                    }
                                }
                            }
                    );
                    visiblePause2.play();

                    addRightCreatePane(currentOption);
                }

            }
        );
        newTimetableButton.getStyleClass().add("settingsButton");

        leftToolbar = new HBox(loadTimetable, newTimetableButton);
        leftToolbar.setSpacing(10);
        leftToolbar.setPadding(new Insets(0, 10, 0, 10));
        leftToolbar.getStyleClass().add("otherTimetables");
        leftToolbar.setAlignment(Pos.CENTER_LEFT);

        HBox leftBox = new HBox();
        leftBox.getChildren().addAll(createHomeButton(), leftToolbar);

        HBox rightBox = createRightToolBox();

        ToolBar tb = new ToolBar();
        tb.getItems().addAll(leftBox, rightBox);
        tb.getStyleClass().add("toolBar");

        this.setTop(tb);
    }

    private void addLeftOptions(){
        VBox vBoxDetails = createLeftOption("Detalii orar", "Metadata");
        VBox vBoxTimes = createLeftOption("Intervale orare", "Adaugă intervale");
        VBox vBoxResources = createLeftOption("Resurse", "Clase", "Profesori", "Săli de clasă");
        VBox vBoxEvents = createLeftOption("Eveniment", "Adaugă evenimente");
        VBox vBoxConstraints = createLeftOption("Constrângeri", "Limitare feresetre");

        VBox vBox = new VBox(vBoxDetails, vBoxTimes, vBoxResources, vBoxEvents, vBoxConstraints);
        vBox.getStyleClass().add("leftScreen");

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("edge-to-edge");

        final Label error = new Label("");
        error.getStyleClass().addAll("fieldRightLabel", "redStar", "normalFont");
        error.setTextFill(Color.WHITE);
        Button generate = new Button("GENEREAZĂ ORAR");
        Button addToSolution = new Button("ADAUGĂ ÎN ORAR");

        generateVBox = new VBox(generate, addToSolution, error);
        generateVBox.setSpacing(10);
        generateVBox.getStyleClass().addAll("leftScreen", "addBorderGenerate");
        generateVBox.setMaxHeight(150);
        generateVBox.setMinHeight(150);
        generateVBox.setPrefHeight(150);
        generateVBox.setAlignment(Pos.CENTER);

        generate.setOnAction((ActionEvent event) ->
        {
            if (savingFile != null) {
                if (timetable.getEvents() != null && timetable.getEvents().getEvents() != null && timetable.getEvents().getEvents().size() > 0) {
                    //Adding the base constraints
                    List<Constraint> eventConstraintList = Arrays.asList(
                            new AssignResourceConstraint("assignResourceConstraint", true, 1, timetable.getEvents(), null),
                            new AssignTimeConstraint("assignTimeConstraint", true, 1, timetable.getEvents(), null)
                    );

                    Constraints eventConstraints = new Constraints(eventConstraintList);


                    List<Constraint> resourceConstraintList = Arrays.asList(
                            new LimitIdleTimesConstraint("limitIdleTimeConstraint", true, 1, 1, null, timetable.getResources())
                    );

                    Constraints resourceConstraints = new Constraints(resourceConstraintList);

                    timetable.setEventConstraints(eventConstraints);
                    timetable.setResourceConstraints(resourceConstraints);

                    Algorithm algorithm = new GradingAlgorithm();
                    Timetable solvedTimetable = algorithm.solve(timetable);

                    //Saving into the files
                    XMLDataLoader.loadSolvedTimetableToXMLWithPath(solvedTimetable, savingFile);

                    if(!savingFile.getPath().equals(PropertiesLoader.loadXMLLocationFolder())){
                        File f = new File(new String(PropertiesLoader.loadXMLLocationFolder() + savingFile.getName()));
                        XMLDataLoader.loadSolvedTimetableToXMLWithPath(solvedTimetable, f);
                        savingFile = f;
                    }

                    //Preparing for loading the display pane
                    File loadFolder = new File(PropertiesLoader.loadXMLLocationFolder());

                    List<File> timetablesToDisplay = new ArrayList<>();

                    //Reading the XMLs from the given directory
                    for (final File fileEntry : loadFolder.listFiles()) {
                        if (!fileEntry.isDirectory()){
                            timetablesToDisplay.add(fileEntry);
                        }
                    }

                    Map<String, Timetable> idTimetableWithSolutionMap = new HashMap<>();
                    Timetable t;
                    for(File file: timetablesToDisplay) {
                        if (PropertiesLoader.loadXMLLocationFolder().equals(new String(file.getParent() + "\\"))) {
                            t = XMLDataLoader.loadDataFromXML(file.getName());
                        } else {
                            t = XMLDataLoader.loadDataFromXMLWithPath(file);
                        }
                        if(t.getSolutions() != null){
                            idTimetableWithSolutionMap.put(t.getId(), t);
                        }

                    }

                    //Opening display pane
                    StageLoader.loadDisplay(idTimetableWithSolutionMap, savingFile, timetable);

                } else {
                    error.setText("Evenimentele sunt necesare!");

                    if (generateVBox.getChildren().size() < 2) {
                        generateVBox.getChildren().add(0, error);
                    } else {
                        error.setVisible(true);
                    }

                    PauseTransition visiblePause = new PauseTransition(
                            Duration.seconds(5)
                    );
                    visiblePause.setOnFinished(
                            e -> error.setVisible(false)
                    );
                    visiblePause.play();
                }
            }
            else {
                //Show window to choose file
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File(PropertiesLoader.loadXMLLocationFolder()));

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                CreatePane.savingFile = fileChooser.showSaveDialog(primaryStage);
            }

            //The timetable has been generated
            //Now loading the display pane

        });

        generate.getStyleClass().add("createButton");
        addToSolution.getStyleClass().add("createButton");

        VBox leftVBox = new VBox(scrollPane, generateVBox);

        this.setLeft(leftVBox);
    }

    private VBox createLeftOption(String labelString, String... buttonStringList){
        VBox vBox = new VBox();

        Label label = new Label(labelString);
        label.getStyleClass().add("leftLabel");

        ListView<String> listView = new ListView<>();
        listView.setId(labelString);

        final ObservableList items = FXCollections.observableArrayList(buttonStringList);
        listView.getItems().addAll(items);
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if(new_val != null) {
                        clearSelections(labelString);
                        addRightCreatePane(new_val);
                    }
                }
        );

        listView.getStyleClass().add("generalListView");
        listView.setPrefHeight(44*buttonStringList.length);

        listViewList.add(listView);

        vBox.getChildren().addAll(label, listView);
        vBox.getStyleClass().add("optionVBox");

        return vBox;
    }

    private void clearSelections(String leaveOut){
        for(ListView l : listViewList){
            if(!l.getId().equals(leaveOut) && l.getSelectionModel().getSelectedItems().size() > 0) {
                l.getSelectionModel().clearSelection();
            }
        }
    }

    private void addRightCreatePane(String rightPaneName){
        currentOption = rightPaneName;
        switch(rightPaneName){
            case "Metadata":
                InsertGeneralSettings rightPane1 = new InsertGeneralSettings(primaryStage);
                this.setCenter(rightPane1.addRightPane());
                break;
            case "Adaugă intervale":
                InsertTime rightPane2 = new InsertTime(primaryStage);
                this.setCenter(rightPane2.addRightPane());
                break;
            case "Clase":
                InsertResource rightPane3 = new InsertResource(primaryStage, "studyGroup", "clasă", "clase");
                this.setCenter(rightPane3.addRightPane());
                break;
            case "Profesori":
                InsertResource rightPane4 = new InsertResource(primaryStage, "teacher", "profesor", "profesori");
                this.setCenter(rightPane4.addRightPane());
                break;
            case "Săli de clasă":
                InsertResource rightPane5 = new InsertResource(primaryStage, "classroom", "sală", "săli");
                this.setCenter(rightPane5.addRightPane());
                break;
            case "Adaugă evenimente":
                InsertEvent rightPane6 = new InsertEvent(primaryStage);
                this.setCenter(rightPane6.addRightPane());
                break;
            case "Limitare feresetre":
                InsertResourceConstraint rightPane9 = new InsertResourceConstraint(primaryStage, "assignTimeConstraint", new AssignTimeConstraint(), rightPaneName);
                this.setCenter(rightPane9.addRightPane());
                break;
            default:
                break;
        }
    }

}
