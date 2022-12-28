
package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * A test class for Student class
 */
public class StudentTest {
    
    public StudentTest() {
    }
    
    /**
     * Deletes the folder used in student tests.
     * @throws IOException 
     */
    @AfterAll
    public static void tearDownClass() throws IOException {
        Student.deleteStudentData("H1");
    }

    /**
     * Test of getStudentNumber method, of class Student.
     */
    @Test
    public void testGetStudentNumber() {
        System.out.println("getStudentNumber");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        String expResult = "H1";
        String result = instance.getStudentNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFirstName method, of class Student.
     */
    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        String expResult = "Erkki";
        String result = instance.getFirstName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastName method, of class Student.
     */
    @Test
    public void testGetLastName() {
        System.out.println("getLastName");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        String expResult = "Esimerkki";
        String result = instance.getLastName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartYear method, of class Student.
     */
    @Test
    public void testGetStartYear() {
        System.out.println("getStartYear");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        int expResult = 2000;
        int result = instance.getStartYear();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTargetGradYear method, of class Student.
     */
    @Test
    public void testGetTargetGradYear() {
        System.out.println("getTargetGradYear");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        int expResult = 2001;
        int result = instance.getTargetGradYear();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStudyPlanNames method, of class Student.
     * @throws java.io.IOException
     */
    @Test
    public void testGetStudyPlanNames() throws IOException {
        System.out.println("getStudyPlanNames");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        instance.saveData();
        String path = Sisu.USERS_DIR + Sisu.SEPARATOR + "H1" + Sisu.SEPARATOR + 
                "testplan.json";
        new File(path).createNewFile();
        ArrayList<String> expResult = new ArrayList<>(List.of("testplan"));
        ArrayList<String> result = instance.getStudyPlanNames();
        assertEquals(expResult, result);
        Student.deleteStudentData("H1");
    }

    /**
     * Test of saveData method, of class Student.
     * @throws java.lang.Exception
     */
    @Test
    public void testSaveData() throws Exception {
        System.out.println("saveData");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        String path = Sisu.USERS_DIR + Sisu.SEPARATOR + "H1" 
                + Sisu.SEPARATOR + Sisu.DATA_FILE;
        instance.saveData();
        assertTrue(Files.exists(Paths.get(path)));
        
        try (FileReader reader = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            Gson gson = new Gson();
            instance = gson.fromJson(root, Student.class);
            assertEquals("H1", instance.getStudentNumber());
            assertEquals("Erkki", instance.getFirstName());
            assertEquals("Esimerkki", instance.getLastName());
            assertEquals(2000, instance.getStartYear());
            assertEquals(2001, instance.getTargetGradYear());
        }
        Student.deleteStudentData("H1");
    }

    /**
     * Test of readData method, of class Student.
     * @throws java.lang.Exception
     */
    @Test
    public void testReadData() throws Exception {
        System.out.println("readData");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        instance.saveData();
        String path = Sisu.USERS_DIR + Sisu.SEPARATOR + "H1" 
                + Sisu.SEPARATOR + Sisu.DATA_FILE;
        Student result = Student.readData(path);
        assertEquals("H1", result.getStudentNumber());
        assertEquals("Erkki", result.getFirstName());
        assertEquals("Esimerkki", result.getLastName());
        assertEquals(2000, result.getStartYear());
        assertEquals(2001, result.getTargetGradYear());
        
        String newPath = Sisu.USERS_DIR + Sisu.SEPARATOR + "notAStudentNum" 
                + Sisu.SEPARATOR + Sisu.DATA_FILE;
        assertThrows(FileNotFoundException.class, () -> 
                Student.readData(newPath));
        Student.deleteStudentData("H1");
    }
    
    /**
     * Test of readSudyPlan method, of class Student.
     * @throws java.io.IOException
     */
    @Test
    public void testReadStudyPlan() throws IOException {
        System.out.println("readStudyPlan");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        instance.saveData();
        String groupId = "otm-fa02a1e7-4fe1-43e3-818b-810d8e723531";
        DegreeProgramme studyPlan = new DegreeProgramme(groupId);
        instance.saveStudyPlan("testplan", studyPlan);
        DegreeProgramme result = instance.readStudyPlan("testplan");
        String expResult = "Tieto- ja sähkötekniikan kandidaattiohjelma";
        assertEquals(expResult, result.getName());
        
        assertThrows(FileNotFoundException.class, () -> 
                instance.readStudyPlan("notaplan"));
        Student.deleteStudentData("H1");
    }
    
    /**
     * Test of saveSudyPlan method, of class Student.
     * @throws java.io.IOException
     */
    @Test
    public void testSaveStudyPlan() throws IOException {
        System.out.println("saveStudyPlan");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        instance.saveData();
        String groupId = "otm-fa02a1e7-4fe1-43e3-818b-810d8e723531";
        DegreeProgramme studyPlan = new DegreeProgramme(groupId);
        instance.saveStudyPlan("testplan", studyPlan);
        String path = Sisu.USERS_DIR + Sisu.SEPARATOR + 
                instance.getStudentNumber() + Sisu.SEPARATOR + "testplan.json";
        assertTrue(Files.exists(Paths.get(path)));
        
        try (FileReader reader = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            Gson gson = new Gson();
            DegreeProgramme result = gson.fromJson(root, DegreeProgramme.class);
            String expResult = "Tieto- ja sähkötekniikan kandidaattiohjelma";
            assertEquals(expResult, result.getName());
        }
        Student.deleteStudentData("H1");
    }
    
    /**
     * Test of saveSudyPlan method, of class Student.
     * 
     * @throws java.io.IOException
     */
    @Test
    public void testDeleteStudentData() throws IOException {
        System.out.println("deleteStudentData");
        Student instance = new Student("H1", "Erkki", "Esimerkki", 2000, 2001);
        instance.saveData();
        Student.deleteStudentData("H1");
        assertTrue(!Files.exists(Paths.get("H1")));
    }
}
