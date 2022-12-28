package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class of class Module. Test Class consists of JUnit unit tests for class Module.
 */
public class ModuleTest {
    
    public ModuleTest() {
    }
    
    /**
     * Test of clearModules, of class Module. Clears subModules of module.
     * @throws IOException 
     */
    @Test
    public void testClearModules() throws IOException {
        Module m = new Module("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        int expValue = 5;
        assertEquals(expValue, m.getModules().size());
        expValue = 0;
        m.clearModules();
        assertEquals(expValue, m.getModules().size());
    }
    /**
     * Test of getCreditsAsString, of class Module. Tests that method works for
     * both studyModule and groupingModule.
     * @throws IOException 
     */
    @Test
    public void testGetCreditsAsString() throws IOException {
        
        Module studyModule = new Module("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        String expResult = "0op / 180op";
        assertEquals(expResult, studyModule.getCreditsAsString());
        ArrayList<String> al = new ArrayList<>();
        
        Module m2 = new Module("otm-33a53ebb-8cbb-4315-9021-ed14742bfa60");
        expResult = "0op / 40op";
        assertEquals(expResult, m2.getCreditsAsString());
        
        for (var courseUnit : m2.getCourseUnits().entrySet()) {
            courseUnit.getValue().setCompleted();
        }
        m2.updateCredits();
        expResult = "40op / 40op";
        assertEquals(expResult, m2.getCreditsAsString());
        
        Module groupingModule = new Module("otm-7ca282c4-4e88-4732-a3e3-573d35d33863");
        expResult = "";
        assertEquals(expResult, groupingModule.getCreditsAsString());
        
        Module groupingModule2 = new Module("otm-6b723b60-5fda-4358-9ed4-6c508289420d");
        
        for (var module : groupingModule2.getModules().entrySet()) {
            for (var courseUnit : module.getValue().getCourseUnits().entrySet()) {
                courseUnit.getValue().setCompleted();
            }
        }
        groupingModule2.updateCredits();
        expResult = "312op";
        assertEquals(expResult, groupingModule2.getCreditsAsString());
    }
    /**
     * Test of constructor, of class Module.
     * @throws java.io.IOException
     */
    @Test
    public void testConstructorException() throws IOException {
        System.out.println("testConstructorException");
        Exception exception = assertThrows(IOException.class, () -> { 
            new Module("TERRIBLE_GROUPID");
        });
    }
    /**
     * Test of getName method, of class Module.
     * @throws java.io.IOException
     */
    @Test
    public void testGetName() throws IOException {
        System.out.println("testGetName");
        Module m = new Module("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        String expName = "Tietotekniikka";
        assertEquals(expName, m.getName());
    }
    /**
     * Test of getType method, of class Module.
     * @throws java.io.IOException
     */
    @Test
    public void testGetType() throws IOException {
        System.out.println("testGetType");
        Module m = new Module("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        String expModule = "StudyModule";
        assertEquals("StudyModule", m.getType());
        
        expModule = "GroupingModule";
        Module m2 = new Module("otm-7ca282c4-4e88-4732-a3e3-573d35d33863");
        assertEquals("GroupingModule", m2.getType());
    }
    /**
     * Test of getGroupId method, of class Module.
     * @throws java.io.IOException
     */
    @Test
    public void testGetGroupdId() throws IOException {
        System.out.println("testGetGroupId");
        Module m = new Module("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        String expGroupId = "otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce";
        assertEquals(expGroupId, m.getGroupId());
    }
    /**
     * Test of addModule method, of class Module.
     * @throws java.io.IOException
     */
    @Test
    public void testAddModule() throws IOException {
        System.out.println("testAddModule");
        
        TreeMap<String, Module> expModule = new TreeMap<>();
        Exception exception = assertThrows(IOException.class, () -> { 
            new Module("INCORRECT_GROUPID");
        });
        
        // https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=
        // otm-316ac8bf-ff36-4ec0-8997-617976500368&universityId=tuni-university-root-id
        Module m = new Module("otm-316ac8bf-ff36-4ec0-8997-617976500368");
        int expSize = 0;
        assertEquals(expSize, m.getModules().size());
        
        // https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=
        // otm-e0486fac-3401-47e9-97d7-968bed034227&universityId=tuni-university-root-id
        Module m2 = new Module("otm-e0486fac-3401-47e9-97d7-968bed034227");
        m.addModule(m2);
        expSize = 1;
        assertEquals(expSize, m.getModules().size());
        expModule.put(m2.getName(), m2);
        assertEquals(expModule, m.getModules());
        
        // https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=
        // otm-7ca282c4-4e88-4732-a3e3-573d35d33863&universityId=tuni-university-root-id
        m.addModule("Vapaasti valittavat opintokokonaisuudet", "otm-7ca282c4-4e88-4732-a3e3-573d35d33863");
        expSize = 2;
        assertEquals(expSize, m.getModules().size());
        
        Exception exception2 = assertThrows(IOException.class, () -> { 
            m.addModule("INCORRECT_NAME", "INCORRECT_GROUPID");
        });   
    }
    @Test
    /**
     * Test of getTargetCredits method, of class Module.
     * @throws java.io.IOException
     */
    public void testGetTargetCredits() throws IOException {
        System.out.println("testGetTargetCredits");
        Module m = new Module("otm-316ac8bf-ff36-4ec0-8997-617976500368");
        int expCredits = 65;
        assertEquals(expCredits, m.getTargetCredits());
    }
    /**
     * Test of updateCredits method, of class Module. Tests that module updates
     * credits and adds completed courses when method is called.
     * @throws java.io.IOException
     */
    @Test
    public void testUpdateCredits() throws IOException {
        System.out.println("testUpdateCredits");
        Module m = new Module("otm-6b723b60-5fda-4358-9ed4-6c508289420d");
        int expCredits = 312;
        for (var module : m.getModules().entrySet()) {
            for (var courseUnit : module.getValue().getCourseUnits().entrySet()) {
                courseUnit.getValue().setCompleted();
            }
        }
        m.updateCredits();
        assertEquals(expCredits, m.getCredits());
    }
    /**
     * Test of setCompleted and setNotCompleted method, of class Module.
     */
    @Test
    public void testSetCompletedAndSetNotCompleted() throws IOException {
        System.out.println("testSetCompleted");
        ArrayList<String> al = new ArrayList<>();
        Module m = new Module("otm-316ac8bf-ff36-4ec0-8997-617976500368");
        m.setCompleted("Teollisuustalouden perusteet", al);
        boolean expValue = true;
        CourseUnit cu = m.getCourseUnits().get("Teollisuustalouden perusteet");
        assertEquals(expValue, cu.isCompleted());
        cu.setNotCompleted();
        expValue = false;
        assertEquals(expValue, cu.isCompleted());
    }
    /**
     * Test of getGroupId method, of class Module.
     * @throws java.io.IOException
     */
    @Test
    public void testGetGroupId() throws IOException {
        System.out.println("testGetGroupId");
        Module m = new Module("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        String expModuleName = "otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce";
        assertEquals(expModuleName, m.getGroupId());
    }

    /**
     * Test of getModules method, of class Module.
     * @throws java.io.IOException
     */
    @Test
    public void testGetModules() throws IOException {
        System.out.println("testGetModules");
        Module m = new Module("otm-e0486fac-3401-47e9-97d7-968bed034227");
        TreeMap<String, Module> expModules = new TreeMap<>();
        
        assertEquals(expModules, m.getModules());
        Module subModule = new Module("otm-316ac8bf-ff36-4ec0-8997-617976500368");
        expModules.put(subModule.getName(), subModule);
        Module subModule2 = new Module("otm-f9e99608-4f97-4dc6-9beb-998b3873e249");
        expModules.put(subModule2.getName(), subModule2);
        Module subModule3 = new Module("otm-6b723b60-5fda-4358-9ed4-6c508289420d");
        expModules.put(subModule3.getName(), subModule3);
        m.addModule(subModule);
        m.addModule(subModule2);
        m.addModule(subModule3);
        assertEquals(expModules, m.getModules());
    }
    /**
     * Test of getCourseUnits method, of class Module.
     * @throws java.io.IOException
     */
    @Test
    public void testGetCourseUnits() throws IOException {
        
        System.out.println("testGetCourseUnits");
        Module m = new Module("otm-e4a8addd-5944-4f94-9e56-d1b51d1f22ce");
        TreeMap<String, CourseUnit> expCourseUnits = new TreeMap<>();
        assertEquals(expCourseUnits, m.getCourseUnits());
        
        // https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=otm-316ac8bf-ff36-4ec0-8997-617976500368&universityId=tuni-university-root-id
        Module m2 = new Module("otm-316ac8bf-ff36-4ec0-8997-617976500368");
        TreeMap<String, CourseUnit> moduleWithCourseUnits = new TreeMap<>();
        moduleWithCourseUnits = m2.getCourseUnits();
        int expSize = 15;
        assertEquals(expSize, moduleWithCourseUnits.size());
    }
    
}
