package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import org.junit.jupiter.api.AfterAll;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.api.FxRobot;
import org.testfx.matcher.control.ComboBoxMatchers;
import org.testfx.matcher.control.TextMatchers;

/**
 * Test class for main SISU GUI. Tests functionality of startWindow, askMoreInfo
 * window and tab1.
 * 
 */
@ExtendWith(ApplicationExtension.class)
class SisuTest {
    private Scene scene;
    
    
    @AfterAll
    public static void tearDownClass() throws IOException {
        Student.deleteStudentData("H250001");
    }
    @Start
    public void start(Stage stage) throws Exception {
        new Sisu().start(stage);
    }
    /**
     * Test of start window of Sisu GUI.
     * @param robot Robot clicking on nodes and typing.
     */
    @Test
    void testStartWindow(FxRobot robot) {
        System.out.println("testStartWindow");
        FxAssert.verifyThat("#infoLabelStudentNumber", hasText("Anna opiskelijanumero"));
        FxAssert.verifyThat("#nextButton", hasText("Seuraava"));
        FxAssert.verifyThat("#studentNumberField", TextInputControlMatchers.hasText(""));
        robot.clickOn("#studentNumberField");
        robot.write("H250250");
        FxAssert.verifyThat("#studentNumberField", TextInputControlMatchers.hasText("H250250"));
        robot.eraseText(7);
        FxAssert.verifyThat("#studentNumberField", TextInputControlMatchers.hasText(""));
    }
    /**
     * Test of GUI startWindow, of main class Sisu.
     * @param robot Robot clicking on nodes and typing.
     * @throws InterruptedException 
     */
    @Test
    void testAddAccount(FxRobot robot) throws InterruptedException {
        System.out.println("testAddAccount");
        robot.clickOn("#studentNumberField");
        robot.write("H250000");
        robot.clickOn("#nextButton");
        
        String[] queries = {"#infoLabel_1",
        "#firstNameLabel_1",
        "#lastNameLabel_1",
        "#startLabel_1",
        "#targetGradLabel_1"
        };
        String[] texts = {"Tietoja opiskelijanumerolle H250000 ei löytynyt. Täydennä tiedot.",
            "Etunimi:",
            "Sukunimi:",
            "Opintojen aloitusvuosi:",
            "Valmistumisen tavoitevuosi:"
        };
        for (int i = 0; i < queries.length; i++) {
            FxAssert.verifyThat(queries[i], hasText(texts[i]));
        }
        FxAssert.verifyThat("#firstNameField_1", TextInputControlMatchers.hasText(""));
        
        robot.clickOn("#firstNameField_1");
        robot.write("Mikko");
        robot.clickOn("#lastNameField_1");
        robot.write("Malliesimerkki");
        FxAssert.verifyThat("#firstNameField_1", TextInputControlMatchers.hasText("Mikko"));
        FxAssert.verifyThat("#lastNameField_1", TextInputControlMatchers.hasText("Malliesimerkki"));
        FxAssert.verifyThat("#startYearBox", ComboBoxMatchers.hasItems(51));
        FxAssert.verifyThat("#targetGradYearBox", ComboBoxMatchers.hasItems(51));
        
        robot.clickOn("#nextButton_1");
        FxAssert.verifyThat("#infoLabel_1", hasText("Tietoja puuttuu. Täydennä tiedot."));
        
        robot.clickOn("#startYearBox");
        robot.moveBy(0, 120);
        robot.clickOn(MouseButton.PRIMARY);
        robot.clickOn("#targetGradYearBox");
        robot.moveBy(0, 90);
        robot.clickOn(MouseButton.PRIMARY);
        robot.clickOn("#nextButton_1");
        FxAssert.verifyThat("#infoLabel_1", hasText("Valmistumisvuosi ei voi olla ennen aloitusvuotta."));
    }
    /**
     * Test of GUI tab1, of main class Sisu.
     * @param robot Robot clicking on nodes and typing.
     */
    @Test
    void testTab1(FxRobot robot) {
        System.out.println("testTab1");
        // start Window
        robot.clickOn("#studentNumberField");
        robot.write("H250001");
        robot.clickOn("#nextButton");
        // Login window
        robot.clickOn("#firstNameField_1");
        robot.write("Mikko");
        robot.clickOn("#lastNameField_1");
        robot.write("Malliesimerkki");
        
        robot.clickOn("#startYearBox");
        robot.moveBy(0, 50);
        robot.clickOn(MouseButton.PRIMARY);
        robot.clickOn("#targetGradYearBox");
        robot.moveBy(0, 200);
        robot.clickOn(MouseButton.PRIMARY);
        robot.clickOn("#nextButton_1");
        
        String[] queries = {"#studentNumLabel_1",
        "#firstNameLabel_2",
        "#lastNameLabel_2",
        "#startLabel_2",
        "#targetGradLabel_2",
        "#degreeLabel_1",
        "#orientationLabel_1",
        "#createStudyPlanButton_1",
        "#orientationLabel_1"
        };
        String[] texts = {"Opiskelijanumero:",
            "Etunimi:",
            "Sukunimi:",
            "Opintojen aloitusvuosi:",
            "Valmistumisen tavoitevuosi:",        
            "Valitse tutkinto-ohjelma",
            "Valitse opintosuuntaus",
            "Luo uusi opintosuunnitelma",
            "Valitse opintosuuntaus"
        };
        for (int i = 0; i < queries.length; i++) {
            FxAssert.verifyThat(queries[i], hasText(texts[i]));
        }
        String[] queries2 = {"#studentNumText_1",
            "#firstNameText_1",
            "#lastNameText_1",
            "#startText_1",
            "#targetGradText_1"
        };
        String[] texts2 = {"H250001",
            "Mikko",
            "Malliesimerkki",
            "2001",
            "2007"};
        for (int i = 0; i < queries2.length; i++) {
            FxAssert.verifyThat(queries2[i], TextMatchers.hasText(texts2[i]));
        }
    }
}