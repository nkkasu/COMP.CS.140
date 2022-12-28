package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class consisting of JUnit tests for class DegreeProgramme.
 */
public class DegreeProgrammeTest {
    
    public DegreeProgrammeTest() {
    }
    /**
     * Test of constructor of class DegreeProgramme
     */
    @Test
    public void testConstructorException() throws IOException {
        System.out.println("testConstructorException");
        Exception exception = assertThrows(IOException.class, () -> { 
            new Module("TERRIBLE_GROUPID");
        });
    }
    /**
     * Test of getOptionalModules method, of class DegreeProgramme.
     * @throws java.io.IOException
     */
    @Test
    public void testGetOptionalModules() throws IOException {
        System.out.println("testGetOptionalModules");
        DegreeProgramme degreeProg = new DegreeProgramme("otm-fa02a1e7-4fe1-43e3-818b-810d8e723531");
        TreeMap<String, String> expModules = new TreeMap<>(
                Map.of("Tietotekniikka", "otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce",
                        "Sähkötekniikka", "otm-b994335e-8759-4d7e-b3bf-ae505fd3935e")
        );
        assertEquals(expModules, degreeProg.getOptionalModules());
    }
    
}
