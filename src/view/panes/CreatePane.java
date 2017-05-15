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
import model.constraint.types.AssignResourceConstraint;
import model.constraint.types.AssignTimeConstraint;
import model.resource.ResourceTypes;
import utilities.DataLoader;
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

                    //Set extension filter
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show load file dialog
                    loadedFile = fileChooser.showOpenDialog(primaryStage);

                    if (loadedFile != null) {
                        savingFile = loadedFile;
                        timetable = DataLoader.loadDataFromXMLWithPath(loadedFile);

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
        VBox vBoxConstraints = createLeftOption("Constrângeri", "Resurse alocate", "Interval alocat", "Limitare feresetre");

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

        generateVBox = new VBox(generate, error);
        generateVBox.getStyleClass().addAll("leftScreen", "addBorderGenerate");
        generateVBox.setMaxHeight(120);
        generateVBox.setMinHeight(120);
        generateVBox.setPrefHeight(120);
        generateVBox.setAlignment(Pos.CENTER);

        generate.setOnAction((ActionEvent event) ->
        {
            if (savingFile != null) {
                if (timetable.getEvents() != null && timetable.getEvents().getEvents() != null && timetable.getEvents().getEvents().size() > 0) {
                    Algorithm algorithm = new GradingAlgorithm();
                    Timetable solvedTimetable = algorithm.solve(timetable);

                    //Saving into the files
                    DataLoader.loadSolvedTimetableToXMLWithPath(solvedTimetable, savingFile);

                    if(!savingFile.getPath().equals(PropertiesLoader.loadXMLLocationFolder())){
                        File f = new File(new String(PropertiesLoader.loadXMLLocationFolder() + savingFile.getName()));
                        DataLoader.loadSolvedTimetableToXMLWithPath(solvedTimetable, f);
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
                            t = DataLoader.loadDataFromXML(file.getName());
                        } else {
                            t = DataLoader.loadDataFromXMLWithPath(file);
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
                error.setText("Nu este încărcat un orar!");

                if(generateVBox.getChildren().size()<2) {
                    generateVBox.getChildren().add(0, error);
                }
                else{
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

            //The timetable has been generated
            //Now loading the display pane


        });

        generate.getStyleClass().addAll("createButton");

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
                (ObservableValue<? extends String> ov, String old_val,
                 String new_val) -> {
                    if(new_val != null) {
                        clearSelections(labelString);
                        addRightCreatePane(new_val);
                    }
                });

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
            case "Resurse alocate":
                InsertEventConstraint rightPane7 = new InsertEventConstraint(primaryStage, "assignResourceConstraint", new AssignResourceConstraint(), rightPaneName);
                this.setCenter(rightPane7.addRightPane());
                break;
            case "Interval alocat":
                InsertEventConstraint rightPane8 = new InsertEventConstraint(primaryStage, "assignTimeConstraint", new AssignTimeConstraint(), rightPaneName);
                this.setCenter(rightPane8.addRightPane());
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
