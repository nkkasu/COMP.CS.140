package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class consisting of JUnit Tests for ModuleJsonReader. 
 */
public class ModuleJsonReaderTest {
    
    public ModuleJsonReaderTest() {
    }
    /**
     * Test of constructor exception, of class ModuleJsonReader.
     * @throws java.io.IOException
     * @throws java.net.MalformedURLException
     */
    @Test
    public void testException() throws IOException, MalformedURLException {
        Exception exception = assertThrows(IOException.class, () -> { 
            new ModuleJsonReader("TERRIBLE_GROUPID");
        });
    }
    /**
     * Test of getName method, of class ModuleJsonReader.
     * @throws java.io.IOException
     */
    @Test
    public void testGetName() throws IOException {
        System.out.println("testGetName");
        ModuleJsonReader mjsonR = new ModuleJsonReader("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        String expName = "Tietotekniikka";
        assertEquals(expName, mjsonR.getName());
    }
    /**
     * Test of getType method, of class ModuleJsonReader.
     * @throws java.io.IOException
     */
    @Test 
    public void testGetType() throws IOException {
        System.out.println("testGetType");
        ModuleJsonReader mjsonR = new ModuleJsonReader("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        String expType = "StudyModule";
        assertEquals(expType, mjsonR.getType());
    }
    /**
     * Test of getTargetCredits, of class ModuleJsonReader.
     * @throws java.io.IOException
     */
    @Test
    public void testGetTargetCredits() throws IOException {
        System.out.println("testGetTargetCredits");
        ModuleJsonReader mjsonR = new ModuleJsonReader("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        int expTargetCredits = 180;
        assertEquals(expTargetCredits, mjsonR.getTargetCredits());
    }
    /**
     * Test of getOptionalModules, of class ModuleJsonReader.
     * @throws java.io.IOException
     */
    @Test
    public void testGetOptionalModules() throws IOException {
        System.out.println("testGetOptionalModules");
        ModuleJsonReader mjsonR = new ModuleJsonReader("otm-fa02a1e7-4fe1-43e3-818b-810d8e723531");
        TreeMap<String, String> expModules = new TreeMap<>(
                Map.of("Tietotekniikka", "otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce",
                        "Sähkötekniikka", "otm-b994335e-8759-4d7e-b3bf-ae505fd3935e")
        );
        assertEquals(expModules, mjsonR.getOptionalModules());
    }
    @Test
    public void testGetCourseUnits() throws IOException {
        ModuleJsonReader mjsonR = new ModuleJsonReader("otm-316ac8bf-ff36-4ec0-8997-617976500368");
        TreeMap<String, CourseUnit> moduleWithCourseUnits = new TreeMap<>();
        moduleWithCourseUnits = mjsonR.getCourseUnits();
        int expSize = 15;
        assertEquals(expSize, moduleWithCourseUnits.size());
    }
    /**
     * Test of getModules method, of class ModuleJsonReader.
     * @throws java.io.IOException
     */
    @Test
    public void testGetModules() throws IOException {
        System.out.println("testGetModules");
        ModuleJsonReader mjsonR = new ModuleJsonReader("otm-6b723b60-5fda-4358-9ed4-6c508289420d");
        int expSize = 4;
        
        assertEquals(expSize, mjsonR.getModules().size());
    }
}
