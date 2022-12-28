package fi.tuni.prog3.sisu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Sisu extends Application {

    /**
     * The name of the folder where the data of all students is stored.
     */
    public static final String USERS_DIR = "Users";
    /**
     * The name of the file where the personal information of a student is 
     * stored.
     */
    public static final String DATA_FILE = "student_data.json";
    /**
     * The character used to separate folders in the operating system.
     */
    public static final String SEPARATOR = File.separator;

    private final String FONT_SIZE_CSS = "-fx-font-size: 16px;";
    private Stage stage = null;
    private Student student = null;
    private TreeMap<String, String> degreeProgrammes = new TreeMap<>();
    private String studyPlanName = null;
    private DegreeProgramme studyPlan = null;
    private boolean readingPlanFromFile = false;
    private DegreeProgramme degree = null;
    private TreeMap<String, String> orientations = null;
    private Module orientation = null;
    private HashMap<String, Object> courseCheckBoxes = new HashMap<>();
    private ArrayList<CheckBox> cboxArray = new ArrayList<>();
    
    private GridPane createStudentInfoGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 40, 40, 40));
        var column0 = new ColumnConstraints();
        column0.setHalignment(HPos.RIGHT);
        grid.getColumnConstraints().add(column0);
        return grid;
    }

    private void askMoreInfo(String studentNum) {
        GridPane grid = createStudentInfoGrid();

        Label infoLabel = new Label();
        infoLabel.setAlignment(Pos.CENTER);
        infoLabel.setText("Tietoja opiskelijanumerolle " + studentNum
                + " ei löytynyt. Täydennä tiedot.");
        infoLabel.setId("infoLabel_1");
        Label firstNameLabel = new Label("Etunimi:");
        firstNameLabel.setId("firstNameLabel_1");
        Label lastNameLabel = new Label("Sukunimi:");
        lastNameLabel.setId("lastNameLabel_1");
        Label startLabel = new Label("Opintojen aloitusvuosi:");
        startLabel.setId("startLabel_1");
        Label targetGradLabel = new Label("Valmistumisen tavoitevuosi:");
        targetGradLabel.setId("targetGradLabel_1");

        TextField firstNameField = new TextField("");
        firstNameField.setId("firstNameField_1");
        TextField lastNameField = new TextField("");
        lastNameField.setId("lastNameField_1");
        ComboBox<Integer> startYearBox = new ComboBox();
        startYearBox.setId("startYearBox");
        ComboBox<Integer> targetGradYearBox = new ComboBox();
        targetGradYearBox.setId("targetGradYearBox");
        Button prevButton = new Button("Takaisin");
        Button nextButton = new Button("Seuraava");
        nextButton.setId("nextButton_1");

        ArrayList<Integer> yearList = new ArrayList<>();
        for (int i = 2000; i < 2051; i++) {
            yearList.add(i);
        }
        startYearBox.getItems().addAll(yearList);
        targetGradYearBox.getItems().addAll(yearList);

        grid.add(infoLabel, 0, 0, 2, 1);
        grid.add(firstNameLabel, 0, 1);
        grid.add(lastNameLabel, 0, 2);
        grid.add(startLabel, 0, 3);
        grid.add(targetGradLabel, 0, 4);
        grid.add(firstNameField, 1, 1);
        grid.add(lastNameField, 1, 2);
        grid.add(startYearBox, 1, 3);
        grid.add(targetGradYearBox, 1, 4);
        grid.add(prevButton, 0, 5);
        grid.add(nextButton, 1, 5);

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                var startYear = startYearBox.getValue();
                var targetGradYear = targetGradYearBox.getValue();
                if (startYear == null || targetGradYear == null
                        || firstName.equals("") || lastName.equals("")) {
                    infoLabel.setText("Tietoja puuttuu. Täydennä tiedot.");
                } else if (startYear > targetGradYear) {
                    infoLabel.setText("Valmistumisvuosi ei voi olla ennen "
                            + "aloitusvuotta.");
                } else {
                    student = new Student(studentNum, firstName, lastName,
                            startYear, targetGradYear);
                    try {
                        student.saveData();
                        initMainWindow();
                    } catch (IOException ex) {
                        infoLabel.setText("Tietojen tallennus tai tutkintojen "
                                + "haku Sisusta epäonnistui. Yritä uudelleen");
                        stage.sizeToScene();
                        System.err.println(ex);
                    }
                }
            }
        });

        prevButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                initStartWindow();
            }
        });

        var scene = new Scene(grid);
        stage.setScene(scene);
    }

    private void initStartWindow() {
        Label infoLabel = new Label("Anna opiskelijanumero");
        infoLabel.setId("infoLabelStudentNumber");
        TextField studentNumField = new TextField("");
        studentNumField.setId("studentNumberField");
        studentNumField.setAlignment(Pos.CENTER);
        Button nextButton = new Button("Seuraava");
        nextButton.setId("nextButton");
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(30, 30, 30, 30));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(infoLabel, studentNumField, nextButton);

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    Files.createDirectories(Paths.get(USERS_DIR));
                } catch (IOException ex) {
                    infoLabel.setText("Kansion luonti epäonnistui. "
                            + "Yritä uudelleen.");
                    System.err.println(ex);
                    return;
                }

                String studentNum = studentNumField.getText();
                if (studentNum.equals("")) {
                    return;
                }
                String path = USERS_DIR + SEPARATOR + studentNum
                        + SEPARATOR + DATA_FILE;
                if (Files.exists(Paths.get(path))) {
                    try {
                        student = Student.readData(path);
                        initMainWindow();
                    } catch (FileNotFoundException ex) {
                        System.err.println("This should never happen");
                        System.err.println(ex);
                    } catch (IOException ex) {
                        infoLabel.setText("Tutkintojen haku Sisusta epäonnistui. "
                                + "Yritä uudelleen");
                        stage.sizeToScene();
                        System.err.println(ex);
                    }
                } else {
                    askMoreInfo(studentNum);
                }
            }
        });

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
    }

    private void initMainWindow() throws IOException {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        degreeProgrammes = Utilities.getDegreeProgrammes();
        createTab1(tabPane);
        createTab2(tabPane);

        Scene scene = new Scene(tabPane, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void createTab1StudentInfo(HBox hbox) {
        Label studentNumLabel = new Label("Opiskelijanumero:");
        studentNumLabel.setId("studentNumLabel_1");
        studentNumLabel.setStyle(FONT_SIZE_CSS);
        Label firstNameLabel = new Label("Etunimi:");
        firstNameLabel.setId("firstNameLabel_2");
        firstNameLabel.setStyle(FONT_SIZE_CSS);
        Label lastNameLabel = new Label("Sukunimi:");
        lastNameLabel.setId("lastNameLabel_2");
        lastNameLabel.setStyle(FONT_SIZE_CSS);
        Label startLabel = new Label("Opintojen aloitusvuosi:");
        startLabel.setId("startLabel_2");
        startLabel.setStyle(FONT_SIZE_CSS);
        Label targetGradLabel = new Label("Valmistumisen tavoitevuosi:");
        targetGradLabel.setId("targetGradLabel_2");
        targetGradLabel.setStyle(FONT_SIZE_CSS);

        Text studentNumText = new Text(student.getStudentNumber());
        studentNumText.setId("studentNumText_1");
        studentNumText.setStyle(FONT_SIZE_CSS);
        Text firstNameText = new Text(student.getFirstName());
        firstNameText.setId("firstNameText_1");
        firstNameText.setStyle(FONT_SIZE_CSS);
        Text lastNameText = new Text(student.getLastName());
        lastNameText.setId("lastNameText_1");
        lastNameText.setStyle(FONT_SIZE_CSS);
        Text startText = new Text(Integer.toString(student.getStartYear()));
        startText.setId("startText_1");
        startText.setStyle(FONT_SIZE_CSS);
        Text targetGradText = new Text(Integer.toString(
                student.getTargetGradYear()));
        targetGradText.setId("targetGradText_1");
        targetGradText.setStyle(FONT_SIZE_CSS);

        GridPane grid = createStudentInfoGrid();
        grid.setAlignment(Pos.TOP_LEFT);

        grid.add(studentNumLabel, 0, 0);
        grid.add(firstNameLabel, 0, 1);
        grid.add(lastNameLabel, 0, 2);
        grid.add(startLabel, 0, 3);
        grid.add(targetGradLabel, 0, 4);

        grid.add(studentNumText, 1, 0);
        grid.add(firstNameText, 1, 1);
        grid.add(lastNameText, 1, 2);
        grid.add(startText, 1, 3);
        grid.add(targetGradText, 1, 4);

        hbox.getChildren().add(grid);
    }

    private void createTab1(TabPane tabPane) {
        Tab tab = new Tab("Opiskelijan tiedot");
        tab.setId("tab1");
        HBox hbox = new HBox();
        createTab1StudentInfo(hbox);

        VBox mainVBox = new VBox(50);
        mainVBox.setPadding(new Insets(20, 40, 40, 40));
        mainVBox.setAlignment(Pos.TOP_CENTER);

        // Create elements for study plan selection
        GridPane grid = new GridPane();
        grid.setHgap(100);
        Label studyPlanLabel = new Label("Valitse opintosuunnitelma");
        studyPlanLabel.setId("studyPlanLabel_1");
        ComboBox<String> studyPlanBox = new ComboBox<>();
        studyPlanBox.getItems().addAll(student.getStudyPlanNames());
        Button createStudyPlanButton = new Button("Luo uusi opintosuunnitelma");
        createStudyPlanButton.setId("createStudyPlanButton_1");
        grid.add(studyPlanLabel, 0, 0);
        grid.add(studyPlanBox, 0, 1);
        grid.add(createStudyPlanButton, 1, 0);

        mainVBox.getChildren().add(grid);

        // Create elements for degree and orientation selection
        VBox secondaryVBox = new VBox(10);
        Label degreeLabel = new Label("Valitse tutkinto-ohjelma");
        degreeLabel.setId("degreeLabel_1");
        secondaryVBox.getChildren().add(degreeLabel);
        ComboBox<String> degreeBox = new ComboBox<>();
        degreeBox.setVisibleRowCount(20);
        degreeBox.getItems().addAll(degreeProgrammes.keySet());
        secondaryVBox.getChildren().add(degreeBox);
        Label orientationLabel = new Label("Valitse opintosuuntaus");
        orientationLabel.setId("orientationLabel_1");
        secondaryVBox.getChildren().add(orientationLabel);
        ComboBox<String> orientationBox = new ComboBox<>();
        secondaryVBox.getChildren().add(orientationBox);

        mainVBox.getChildren().add(secondaryVBox);

        hbox.getChildren().add(mainVBox);
        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        hbox.getChildren().add(1, sep);
        tab.setContent(hbox);
        tabPane.getTabs().add(tab);
        
        createStudyPlanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String planName = "opintosuunnitelma_" + 
                        Utilities.getCurrentTimeStamp();
                var studyPlans = studyPlanBox.getItems();
                String newName = planName;
                int i = 1;
                while (studyPlans.contains(newName)) {
                    newName = planName + "_" + i;
                    i++;
                }
                studyPlanBox.getItems().add(newName);
                studyPlanBox.getSelectionModel().select(newName);
                degreeBox.getSelectionModel().clearSelection();
                orientationBox.getSelectionModel().clearSelection();
            }
        });
        
        studyPlanBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String val1, String val2) {
                studyPlanName = val2;
                if (!student.getStudyPlanNames().contains(studyPlanName)) {
                    return;
                }
                try {
                    readingPlanFromFile = true;
                    studyPlan = student.readStudyPlan(studyPlanName);
                    String degreeName = studyPlan.getName();
                    degreeBox.getSelectionModel().select(degreeName);
                    if (!studyPlan.getOptionalModules().isEmpty()) {
                        String orientationName = studyPlan.getModules()
                                .firstEntry().getValue().getName();
                        orientationBox.getSelectionModel().select(orientationName);                       
                    } else {
                        orientationBox.getSelectionModel().clearSelection();
                    }
                    readingPlanFromFile = false;
                } catch (FileNotFoundException ex) {
                    System.err.println(ex);
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        });
        
        degreeBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String val1, String val2) {
                orientationBox.getItems().clear();
                if (val2 == null) {
                    degree = null;
                    orientations = null;
                    return;
                }
                if (readingPlanFromFile) {
                    degree = studyPlan;
                    orientations = studyPlan.getOptionalModules();
                    orientationBox.getItems().addAll(orientations.keySet());
                    return;
                }
                String groupId = degreeProgrammes.get(val2);
                try {
                    degree = new DegreeProgramme(groupId);
                    orientations = degree.getOptionalModules();
                    orientationBox.getItems().addAll(orientations.keySet());
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        });
        
        orientationBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue ov, String val1, String val2) {
                if (val2 == null) {
                    orientation = null;
                    return;
                }
                if (readingPlanFromFile) {
                    orientation = studyPlan.getModules().firstEntry().getValue();
                    return;
                }
                try {
                    orientation = new Module(orientations.get(val2));
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        });
    }
    private void getTreeViewPath(TreeItem<String> item, ArrayList<String> path) {
        if (item.getParent() != null) {
            getTreeViewPath(item.getParent(), path);
        }
        path.add(parseName(item.getValue()));
    }
    
    private void recursiveTreeViewCreate(Module module, TreeItem<String> prevItem, 
            GridPane checkBoxGrid, HashMap<String, Object> map) throws IOException
    {
        int counter = 0;
        var parentMap = (HashMap<String, Object>) map.get(module.getName());
        
        if ("".equals(module.getCreditsAsString())) {
            prevItem.setValue(module.getName());
        }
        else {
            prevItem.setValue(module.getName() + " : " + module.getCreditsAsString());
        }
        for (var subModule : module.getModules().entrySet()) {

            TreeItem<String> newItem = new TreeItem<>();
            newItem.setValue(subModule.getValue().getName());

            prevItem.getChildren().add(newItem);
            HashMap<String, Object> newMap = new HashMap<>();
            parentMap.put(parseName(subModule.getValue().getName()), newMap);
            recursiveTreeViewCreate(subModule.getValue(), newItem, checkBoxGrid, 
                    parentMap);
        }
        
        ArrayList<CheckBox> checkboxes = new ArrayList<>();
        for (var course : module.getCourseUnits().entrySet()) {
            CheckBox courseCheckBox = new CheckBox();
            courseCheckBox.setStyle("-fx-font-size: 15px;");
            courseCheckBox.setText(course.getValue().getName());
            courseCheckBox.setSelected(course.getValue().isCompleted());

            if (course.getValue().isCompleted()) {
                TreeItem<String> newItem = new TreeItem<>();
                newItem.setValue(course.getValue().getName() + " " +
                                 course.getValue().getCredits() + "op");
                prevItem.getChildren().add(newItem);
            }

            checkBoxGrid.add(courseCheckBox, 0, counter);
            counter++;
            courseCheckBox.setVisible(false);
            cboxArray.add(courseCheckBox);
            checkboxes.add(courseCheckBox);
            
            Tooltip toolTip = new Tooltip(course.getValue().getDescription());
            toolTip.setShowDuration(Duration.seconds(100));
            courseCheckBox.setTooltip(toolTip);
            toolTip.setShowDelay(Duration.seconds(0));
            toolTip.setTextAlignment(TextAlignment.LEFT);
            toolTip.setMaxWidth(450);
            toolTip.setMaxHeight(400);
            toolTip.setWrapText(true);

            courseCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, 
                        Boolean t, Boolean t1) {

                    TreeItem<String> newItem = new TreeItem<>();
                    ArrayList<String> path = new ArrayList<>();
                    getTreeViewPath(prevItem, path);
                    path.remove(0);
                    if (ov.getValue() == true) {
                        course.getValue().getCredits();
                        newItem.setValue(course.getValue().getName() + " " +
                        course.getValue().getCredits() + "op");
                        prevItem.getChildren().add(newItem);
                        studyPlan.setCompleted(course.getValue().getName(), path);
                    }
                    if (!ov.getValue()) {
                        String credits = course.getValue().getCredits() + "op";
                        String name = course.getValue().getName();
                        String itemName = name + " " + credits;
                        for (var item : prevItem.getChildren()) {
                            if (parseName(item.getValue()).equals(itemName)) { //parsename
                                prevItem.getChildren().remove(item);
                                break;
                            }
                        }
                        studyPlan.setNotCompleted(name, path);
                    }
                    TreeItem<String> topItem = getTreeRoot(prevItem);
                    updateTreeViewCredits(topItem, studyPlan);
                } 
            });
        }
        parentMap.put("checkboxes", checkboxes);
    }
    
    private TreeItem<String> getTreeRoot(TreeItem<String> treeItem) {
        if (treeItem.getParent() != null) {
            return getTreeRoot(treeItem.getParent());
        }
        return treeItem;
    }
    
    private String parseName(String str) {
        String[] newStr = str.split(" : ");
        return newStr[0];
    }
    
    private void updateTreeViewCredits(TreeItem<String> item, Module module) {
        if ("".equals(module.getCreditsAsString())) {
            item.setValue(module.getName());
        }
        else {
            item.setValue(module.getName() + " : " + module.getCreditsAsString());
        }
        if (!module.getModules().isEmpty()) {
            ArrayList<Module> subModuleList = new ArrayList<>(module.getModules().values());
            ArrayList<TreeItem> itemList = new ArrayList<>(item.getChildren());
            for (int i = 0; i < module.getModules().size(); i++) {
                itemList.get(i).setValue(subModuleList.get(i).getName() + " : " + 
                subModuleList.get(i).getCreditsAsString());
                updateTreeViewCredits(itemList.get(i), subModuleList.get(i));
            }
        }
    }
    
    private void createTab2(TabPane tabPane) throws IOException {
        Tab tab = new Tab("Opintojen rakenne");
        
        Label studyStructureLabel = new Label("Opintorakenne");
        studyStructureLabel.setFont(Font.font(null, FontWeight.BOLD, 18));
        Button btn = new Button("Tallenna tiedot");
        btn.setStyle(FONT_SIZE_CSS);
        GridPane treeViewGrid = new GridPane();
        treeViewGrid.setAlignment(Pos.BOTTOM_CENTER);
        
        GridPane studyCoursesGrid = new GridPane();
        
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        
        row1.setPercentHeight(5);
        row2.setPercentHeight(89);
        row3.setPercentHeight(6);
        column2.setPercentWidth(50);
        column1.setPercentWidth(50);
        
        treeViewGrid.getColumnConstraints().addAll(column1, column2);
        treeViewGrid.getRowConstraints().addAll(row1, row2, row3);
        treeViewGrid.add(studyStructureLabel, 0, 0, 2, 1);
        tab.setContent(treeViewGrid);
        studyStructureLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        studyStructureLabel.setAlignment(Pos.CENTER);
        treeViewGrid.add(btn, 1, 2);
        ScrollPane scrollPane = new ScrollPane();
        ScrollPane ckBoxScrollPane = new ScrollPane();
        
        treeViewGrid.add(ckBoxScrollPane, 1, 1);
        ckBoxScrollPane.fitToWidthProperty().set(true);
        ckBoxScrollPane.fitToWidthProperty().set(true);
        ckBoxScrollPane.setContent(studyCoursesGrid);
        ckBoxScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        GridPane.setHalignment(btn, HPos.CENTER);
        GridPane.setValignment(btn, VPos.TOP);
        btn.setPadding(new Insets(3, 3, 3, 3));
        
        TreeItem<String> rootItem = new TreeItem<>();
        rootItem.setExpanded(false);
        TreeView<String> tree = new TreeView<>();
        tree.setRoot(rootItem);
        tree.setVisible(false);
        scrollPane.setContent(tree);
        treeViewGrid.add(scrollPane, 0, 1);
        scrollPane.fitToHeightProperty().set(true);
        scrollPane.fitToWidthProperty().set(true);
        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tabPane.getTabs().add(tab);
        MultipleSelectionModel<TreeItem<String>> multipleSelectionModel 
                = tree.getSelectionModel();
        
        // Recognizes if particular TreeItem in TreeView is selected.
        multipleSelectionModel.selectedItemProperty().addListener(
                new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> 
                    selectedTreeItem, TreeItem<String> t, TreeItem<String> t1) {
                for (var cBox : cboxArray) {
                    // Make all cboxes invisible
                    cBox.setVisible(false);
                }
                if (t1 == null) {
                    return;
                }
                             
                ckBoxScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                ArrayList<String> treeViewPath = new ArrayList<>();
                getTreeViewPath(t1, treeViewPath);
                ArrayList<CheckBox> checkBoxes = new ArrayList<>();
                HashMap<String, Object> map = courseCheckBoxes;
                for (String name : treeViewPath) {
                    map = (HashMap<String, Object>) map.get(parseName(name));
                }
                try {
                    checkBoxes = (ArrayList<CheckBox>) map.get("checkboxes");
                } catch (Exception ex) {}
                
                // Module has courseunits
                if (checkBoxes != null && !checkBoxes.isEmpty()) {
                    for (var cBox : checkBoxes)
                    {
                        // Make selected module cboxes visible
                        cBox.setVisible(true);
                    }
                }
            }
            
        });
        
        // Tab2 selected, create TreeView
        tab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (!tab.isSelected()) {
                    return;
                }
                
                if (studyPlanName == null || degree == null 
                        || (!degree.getOptionalModules().isEmpty() 
                        && orientation == null)) {
                    tree.setVisible(false);
                    return;
                }
                
                boolean planIsNew = !student.getStudyPlanNames()
                        .contains(studyPlanName);
                if (planIsNew) {
                    studyPlan = degree;
                    boolean hasOrientations = !studyPlan.getOptionalModules()
                        .isEmpty();
                    if (hasOrientations) {
                        studyPlan.addModule(orientation);
                    }
                } else {
                    boolean hasOrientations = !studyPlan.getOptionalModules()
                        .isEmpty() && !degree.getOptionalModules().isEmpty();
                    boolean degreeChanged = !studyPlan.getName()
                        .equals(degree.getName());
                    boolean orientationChanged = hasOrientations && 
                            !studyPlan.getModules().firstEntry().getValue().
                            getName().equals(orientation.getName());
                    if (degreeChanged || orientationChanged) {
                        studyPlan = degree;
                        if (hasOrientations) {
                            studyPlan.clearModules();
                            studyPlan.addModule(orientation);
                        }
                    }
                }
                
                tree.setVisible(true);
                rootItem.getChildren().clear();
                HashMap<String, Object> map = new HashMap<>();
                courseCheckBoxes.put(degree.getName(), map);
                try {
                    recursiveTreeViewCreate(studyPlan, rootItem, studyCoursesGrid, 
                            courseCheckBoxes);

                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        });
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (studyPlanName == null || studyPlan == null 
                        || (!studyPlan.getOptionalModules().isEmpty()) &&
                        orientation == null) {
                    return;
                }
                try {
                    student.saveStudyPlan(studyPlanName, studyPlan);
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        });
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.setTitle("SISU");
        initStartWindow();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
