package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 * A class for representing a student.
 */
public class Student {

    private final String studentNumber;
    private final String firstName;
    private final String lastName;
    private final int startYear;
    private final int targetGradYear;
    private ArrayList<String> studyPlanNames = new ArrayList<>();

    /**
     * Constructs a Student object with the given data.
     *
     * @param studentNumber the student number of the student
     * @param firstName the first name of the student
     * @param lastName the last name of the student
     * @param startYear the start year of the student's studies
     * @param targetGradYear the target graduation year of the student's studies
     */
    public Student(String studentNumber, String firstName, String lastName,
            int startYear, int targetGradYear) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.startYear = startYear;
        this.targetGradYear = targetGradYear;
    }

    /**
     * Returns the student number of the student.
     *
     * @return the student number of the student
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * Returns the first name of the student.
     *
     * @return the first name of the student
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the student.
     *
     * @return the last name of the student
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the start year of the student's studies.
     *
     * @return the start year of the student's studies
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Returns the target graduation year of the student's studies.
     *
     * @return the target graduation year of the student's studies
     */
    public int getTargetGradYear() {
        return targetGradYear;
    }

    /**
     * Returns the names of the student's study plans.
     *
     * @return the names of the student's study plans
     */
    public ArrayList<String> getStudyPlanNames() {
        this.readStudyPlanNames();
        return studyPlanNames;
    }

    /**
     * Saves the data of the student into a JSON file. The data is stored into a
     * folder with the same name as the student's student number.
     *
     * @throws IOException
     */
    public void saveData() throws IOException {
        String dirPath = Sisu.USERS_DIR + Sisu.SEPARATOR + studentNumber;
        Files.createDirectories(Paths.get(dirPath));
        String path = dirPath + Sisu.SEPARATOR + Sisu.DATA_FILE;

        try ( FileWriter writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, writer);
            writer.flush();
        }
    }

    /**
     * Reads the data of the student from a JSON file in the specified path and
     * creates a Student object from it.
     *
     * @param path the path from which to read the student data
     * @return a Student object with the data read from the JSON file
     * @throws FileNotFoundException
     */
    public static Student readData(String path)
            throws FileNotFoundException, IOException {
        try (FileReader reader = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            Gson gson = new Gson();
            Student student = gson.fromJson(root, Student.class);
            return student;
        }
    }
    
    /**
     * Reads the specified study plan the from a file.
     * 
     * @param planName the name of the study plan
     * @return a DegreeProgramme object representing the study plan
     * @throws FileNotFoundException 
     */
    public DegreeProgramme readStudyPlan(String planName) 
            throws FileNotFoundException, IOException {
        String path = Sisu.USERS_DIR + Sisu.SEPARATOR + studentNumber 
                + Sisu.SEPARATOR + planName + ".json";
        try (FileReader reader = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            Gson gson = new Gson();
            DegreeProgramme studyPlan = gson.fromJson(root, 
                    DegreeProgramme.class);
            return studyPlan;
        }
    }
    
    /**
     * Writes the specified study plan to a file.
     * 
     * @param planName the name of the study plan
     * @param studyPlan a DegreeProgramme object representing the study plan
     * @throws IOException 
     */
    public void saveStudyPlan(String planName, DegreeProgramme studyPlan) 
            throws IOException {
        String path = Sisu.USERS_DIR + Sisu.SEPARATOR + studentNumber + 
                Sisu.SEPARATOR + planName + ".json";

        try ( FileWriter writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(studyPlan, writer);
            writer.flush();
        }
    }
    
    /**
     * Deletes all data of the student with the specified student number
     * 
     * @param studentNum the student number of the student
     * @throws java.io.IOException
     */
    public static void deleteStudentData(String studentNum) throws IOException {
        String path = Sisu.USERS_DIR + Sisu.SEPARATOR + studentNum;
        File file = new File(path);
        if (file.exists()) {
            FileUtils.cleanDirectory(file);
            file.delete();
        }
    }
    
    /**
     * Reads the names of the student's study plans
     */
    private void readStudyPlanNames() {
        studyPlanNames = new ArrayList<>();
        String path = Sisu.USERS_DIR + Sisu.SEPARATOR + studentNumber;
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isFile() && !file.getName().equals(Sisu.DATA_FILE)) {
                String studyPlanName = file.getName().replace(".json", "");
                studyPlanNames.add(studyPlanName);
            }
        }
    }
}
