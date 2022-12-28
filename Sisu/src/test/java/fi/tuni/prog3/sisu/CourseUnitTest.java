package fi.tuni.prog3.sisu;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class of class CourseUnit. Test Class consists of JUnit unit tests for class Module.
 */
public class CourseUnitTest {
    
    public CourseUnitTest() {
    }
    /**
     * Test of IOException for constructor of class CourseUnit.
     * @throws IOException 
     */
    @Test
    public void testConstructorException() throws IOException {
        System.out.println("testException");
        Exception exception = assertThrows(IOException.class, () -> { 
            new CourseUnit("TERRIBLE_GROUPID");
        });
    }
    /**
     * Test of getCode method, of class CourseUnit.
     * @throws java.io.IOException
     */
    @Test
    public void testGetCode() throws IOException {
        System.out.println("testGetCode");
        // https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=otm-9392333f-bff2-4c3d-b1cf-717ce9990253&universityId=tuni-university-root-id
        CourseUnit cu = new CourseUnit("otm-9392333f-bff2-4c3d-b1cf-717ce9990253");
        assertEquals("COMP.CS.140", cu.getCode());
        int expValue = 5;
        assertEquals(expValue, cu.getCredits());
    }
    
    /**
     * Test of getName method, of class CourseUnit.
     * @throws java.io.IOException
     */
    @Test
    public void testGetName() throws IOException {
        System.out.println("testGetName");
        CourseUnit cu = new CourseUnit("otm-9392333f-bff2-4c3d-b1cf-717ce9990253");
        String expName = "Ohjelmointi 3: Rajapinnat ja tekniikat";
        assertEquals(expName, cu.getName());
    }

    /**
     * Test of getCredits method, of class CourseUnit.
     * @throws java.io.IOException
     */
    @Test
    public void testGetCredits() throws IOException {
        CourseUnit cu = new CourseUnit("otm-9392333f-bff2-4c3d-b1cf-717ce9990253");
        System.out.println("testGetCredits");
        int expValue = 5;
        assertEquals(expValue, cu.getCredits());
    }

    /**
     * Test of isCompleted method, of class CourseUnit.
     * @throws java.io.IOException
     */
    @Test
    public void testIsCompleted() throws IOException {
        System.out.println("testIsCompleted");
        CourseUnit cu = new CourseUnit("otm-9392333f-bff2-4c3d-b1cf-717ce9990253");
        boolean expValue = false;
        assertEquals(expValue, cu.isCompleted());
        cu.setCompleted();
        expValue = true;
        assertEquals(expValue, cu.isCompleted());
    }
    /**
     * Test of setCompleted and setNotCompleted method, of class CourseUnit.
     * @throws java.io.IOException
     */
    @Test
    public void testSetCompletedAndNotCompleted() throws IOException {
        System.out.println("testSetCompleted");
        System.out.println("testSetNotCompleted");
        CourseUnit cu = new CourseUnit("otm-9392333f-bff2-4c3d-b1cf-717ce9990253");
        assertEquals(false, cu.isCompleted());
        cu.setCompleted();
        assertEquals(true, cu.isCompleted());
        cu.setNotCompleted();
        assertEquals(false, cu.isCompleted());
    }
}
