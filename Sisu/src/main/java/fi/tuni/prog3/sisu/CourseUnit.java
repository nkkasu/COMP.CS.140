package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;

/**
 * A class for representing a CourseUnit.
 */
public final class CourseUnit
{
    private final String name;
    private final String code;
    private int credits = 0;
    private String outcomes = "";
    private String description = "";
    private boolean completed = false;
    
    
    /**
     * Constructs a CourseUnit object with the groupId of the course using 
     * Sisu API.
     * 
     * @param groupId groupId of the courseUnit
     * @throws IOException 
     */
    public CourseUnit(String groupId) throws IOException {
        
        var url = new URL("https://sis-tuni.funidata.fi/kori/api/course-units/"
            + "by-group-id?groupId=" 
            + groupId 
            + "&universityId=tuni-university-root-id");

        String jsonString = new String(url.openStream().readAllBytes());
        JsonObject root = JsonParser
                .parseString(jsonString)
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject();
        
        String language;
        
        if(root.get("name").getAsJsonObject().has("fi")) {
            language = "fi";
        } else if (root.get("name").getAsJsonObject().has("en")) {
            language = "en";
        } else {
            language = "sv";
        }
        
        this.name = root.get("name")
                .getAsJsonObject()
                .get(language)
                .getAsString();

        this.code = root.get("code")
                .getAsString();
        
        if(root.get("credits").getAsJsonObject().has("max")) {
            if(!root.get("credits").getAsJsonObject().get("max").isJsonNull()) {
                this.credits = root.get("credits")
                .getAsJsonObject()
                .get("max")
                .getAsInt();
            }
        } else if (root.get("credits").getAsJsonObject().has("min")){
            if(!root.get("credits").getAsJsonObject().get("min").isJsonNull()) {
                this.credits = root.get("credits")
                .getAsJsonObject()
                .get("min")
                .getAsInt();
            }
        }
        
        if(!root.get("outcomes").isJsonNull()){
            if(root.get("outcomes").getAsJsonObject().has("fi")) {
                this.outcomes = Jsoup.parse(root.get("outcomes")
                    .getAsJsonObject()
                    .get("fi")
                    .getAsString()).text();
            } else if (root.get("outcomes").getAsJsonObject().has("en")) {
                this.outcomes = Jsoup.parse(root.get("outcomes")
                    .getAsJsonObject()
                    .get("en")
                    .getAsString()).text();
            } else if (root.get("outcomes").getAsJsonObject().has("sv")) {
                this.outcomes = Jsoup.parse(root.get("outcomes")
                    .getAsJsonObject()
                    .get("sv")
                    .getAsString()).text();
            }
        }
        
        this.description = this.code+"\n"+this.credits+"op\n\n"+this.outcomes;
    }
    
    /**
     * Returns the code of the course.
     * 
     * @return the code of the course.
     */
    public String getCode() {
        return this.code;
    }
    
    /**
     * Returns the name of the course.
     * 
     * @return the name of the course.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the credits of the course.
     * 
     * @return the credits of the course.
     */
    public int getCredits() {
        return this.credits;
    }
    
    /**
     * Returns the outcomes of the course.
     * 
     * @return the outcomes of the course.
     */
    public String getOutcomes() {
        return this.outcomes;
    }
    
    /**
     * Returns a string that contains code, credits and outcomes.
     * 
     * @return a string that contains code, credits and outcomes.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Returns a boolean of course completion.
     * 
     * @return a boolean of course completion.
     */
    public boolean isCompleted() {
        return this.completed;
    }
    
    /**
     * Sets the course as completed.
     */
    public void setCompleted() {
        this.completed = true;
    }
    
    /**
     * Sets the course as not completed.
     */
    public void setNotCompleted() {
        this.completed = false;
    }
}
