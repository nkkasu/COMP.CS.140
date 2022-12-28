package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;

/**
 * Reads data of modules from Sisu API json files.
 */
public class ModuleJsonReader {
    
    private final String name;
    private final String type;
    private  int targetCredits = 0;
    
    private final TreeMap<String, String> optionalModules = new TreeMap<>();
    private final TreeMap<String, Module> modules = new TreeMap<>();
    
    private final TreeMap<String, CourseUnit> courseUnits= new TreeMap<>();
    
    
    /**
     * Constructs a ModuleJsonReader Object.
     * 
     * @param groupId groupId of the Module
     * @throws MalformedURLException
     * @throws IOException 
     */
    public ModuleJsonReader(String groupId) throws MalformedURLException,
            IOException {
        
        JsonObject root = ModuleJsonReader.getJsonObject(groupId);
        
        var rule = root.get("rule").getAsJsonObject();
        this.type = root.get("type").getAsString();
        
        String language  = "fi";
        if(root.get("name").getAsJsonObject().has("fi")){
            language = "fi";
        } else if (root.get("name").getAsJsonObject().has("en")) {
            language = "en";
        } else if (root.get("name").getAsJsonObject().has("sv")){
            language = "sv";
        }
        
        this.name = root.get("name")
                        .getAsJsonObject()
                        .get(language)
                        .getAsString();
        
        if(this.type.equals("StudyModule")){
            if(!root.get("targetCredits").getAsJsonObject().get("min")
                        .isJsonNull()) {
                this.targetCredits = root.get("targetCredits")
                        .getAsJsonObject()
                        .get("min")
                        .getAsInt();
            }
        }
        
        addRules(rule, false);
    }
    
    /**
     * Adds information about modules and course units to their containers.
     * 
     * @param rule rule that contains the rules to add
     * @param isMandatory boolean of if the rules are mandatory
     * @throws IOException 
     */
    private void addRules(JsonObject rule, boolean isMandatory) 
            throws IOException {
        
        String ruleType = rule.get("type").getAsString();
        
        if(ruleType.equals("CreditsRule")) {
            addRules(rule.get("rule").getAsJsonObject(), false);
        } else if(ruleType.equals("CompositeRule")) {
            var rules = rule.get("rules").getAsJsonArray();
                
            if(rule.get("allMandatory").getAsBoolean()) {
                for(int i = 0;i<rules.size();i++) {
                   addRules(rules.get(i).getAsJsonObject(),
                           rule.get("allMandatory").getAsBoolean());
                }
            } else {
                for(int i = 0;i<rules.size();i++) {
                   addRules(rules.get(i).getAsJsonObject(),
                           rule.get("allMandatory").getAsBoolean());
                }
            }
        } else if(ruleType.equals("ModuleRule")) {
            String moduleGroupId = rule.get("moduleGroupId").getAsString();
                String moduleName = ModuleJsonReader.checkName(moduleGroupId);
                
                Module module;
                
                if(!isMandatory && this.type.equals("DegreeProgramme")) {
                    this.optionalModules.put(moduleName, moduleGroupId);
                } else {
                    module = new Module(moduleGroupId);
                    this.modules.put(moduleName, module);
                } 
        } else if(ruleType.equals("CourseUnitRule")) {
            String courseUnitGroupId = rule.get("courseUnitGroupId")
                        .getAsString();
            var courseUnit = new CourseUnit(courseUnitGroupId);
            String courseUnitName = courseUnit.getName();

            this.courseUnits.put(courseUnitName, courseUnit);
        }
    }
    
    /**
     * Returns a JsonObject of a module from Sisu API.
     * 
     * @param groupId the groupId of the module
     * @return a JsonObject of a module from Sisu API.
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static JsonObject getJsonObject(String groupId) throws 
            MalformedURLException, IOException {
        var url = new URL("https://sis-tuni.funidata.fi/kori/api/modules/"
                + "by-group-id?groupId="
                +groupId+"&universityId=tuni-university-root-id");
        
        String data = new String(url.openStream().readAllBytes());
        return JsonParser.parseString(data).getAsJsonArray().get(0)
                .getAsJsonObject();
    }
    
    /**
     * Returns the type of the module without creating a new ModuleJsonReader
     * object.
     * 
     * @param groupId the groupId of the module
     * @return the type of the module 
     * @throws IOException 
     */
    public static String checkType(String groupId) throws IOException {
        JsonObject root = ModuleJsonReader.getJsonObject(groupId);
        return root.get("type").getAsString();
    }
    
    /**
     * Returns the name of the module without creating a new ModuleJsonReader
     * object.
     * 
     * @param groupId the groupId of the module
     * @return the name of the module 
     * @throws IOException 
     */
    public static String checkName(String groupId) throws IOException {
        JsonObject root = ModuleJsonReader.getJsonObject(groupId);
        
        String language  = "fi";
        if(root.get("name").getAsJsonObject().has("fi")) {
            language = "fi";
        } else if (root.get("name").getAsJsonObject().has("en")) {
            language = "en";
        } else if (root.get("name").getAsJsonObject().has("sv")) {
            language = "sv";
        }
        
        return root.get("name").getAsJsonObject().get(language).getAsString();
    }
    
    /**
     * Returns the name of the module.
     * 
     * @return the name of the module.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the type of the module.
     * 
     * @return the type of the module.
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the target credits of the module.
     * 
     * @return the target credits of the module.
     */
    public int getTargetCredits() {
        return this.targetCredits;
    }
    
    /**
     * Returns a TreeMap of optional modules with name of the module as key and 
     * groupId of the module as value.
     * 
     * @return a TreeMap of optional modules.
     */
    public TreeMap<String, String> getOptionalModules() {
        return this.optionalModules;
    }
    
    /**
     * Returns a TreeMap of mandatory modules.
     * 
     * @return a TreeMap of mandatory modules.
     */
    public TreeMap<String, Module> getModules() {
        return this.modules;
    }
    
    /**
     * Returns a list of course groupIds.
     * 
     * @return a list of course groupIds.
     */
    public TreeMap<String, CourseUnit> getCourseUnits() {
        return this.courseUnits;
    }
}
