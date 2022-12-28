package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * A class that represents a Module.
 */
public class Module
{
    private final String name;
    private final String type;
    private final String groupId;
    private int credits = 0;
    private final int targetCredits;
    private TreeMap<String, Module> modules = new TreeMap<>();
    
    private TreeMap<String, CourseUnit> courseUnits = new TreeMap<>();
    
    /**
     * Constructs a Module object using the ModuleJsonReader class.
     * 
     * @param groupId groupId of the module
     * @throws IOException 
     */
    public Module(String groupId) throws IOException {
        this.groupId = groupId;
        
        var jsonReader = new ModuleJsonReader(groupId);
        this.name = jsonReader.getName();
        this.type = jsonReader.getType();
        this.targetCredits = jsonReader.getTargetCredits();
        
        this.modules = jsonReader.getModules();
        this.courseUnits = jsonReader.getCourseUnits();
    }
    
    /**
     * If the module is a StudyModule, returns credits out of target credits as 
     * a String. Otherwise returns only credits.
     * 
     * @return credits out of targetCredits if StudyModule, otherwise returns 
     * only credits
     */
    public String getCreditsAsString() {
        if(this.type.equals("StudyModule")) {
            return (this.credits + "op / " + this.targetCredits + "op");
        } else {
            if(this.credits == 0) {
                return "";
            } else {
                return this.credits + "op";
            }
        }
    }
    
    /**
     * Adds a module to the modules TreeMap
     * 
     * @param module the module to add
     */
    public void addModule(Module module) {
        modules.put(module.getName(), module);
    }
    
    /**
     * Adds a module using the ModuleJsonReader class.
     * 
     * @param name the name of the module
     * @param groupId the groupId of the module
     * @throws IOException 
     */
    public void addModule(String name, String groupId) throws IOException {
        var module = new Module(groupId);
        modules.put(name, module);
    }
    
    /**
     * Clears all modules under this module.
     */
    public void clearModules() {
        this.modules.clear();
    }
    
    /**
     * Sets a courseUnit as completed.
     * 
     * @param courseUnitName name of the courseUnit 
     * @param moduleNames path of modules to the courseUnit
     */
    public void setCompleted(String courseUnitName, List<String> moduleNames) {
        
        if(moduleNames.isEmpty()) {
            this.courseUnits.get(courseUnitName).setCompleted();
        } else { 
            var module = this.modules.get(moduleNames.get(0));
            moduleNames.remove(0);
            module.setCompleted(courseUnitName, moduleNames);
        }
        
        this.updateCredits();
    }
    
    /**
     * Sets a courseUnit as not completed.
     * 
     * @param courseUnitName name of the courseUnit 
     * @param moduleNames path of modules to the courseUnit
     */
    public void setNotCompleted(String courseUnitName,
            List<String> moduleNames) {
        if(moduleNames.isEmpty()) {
            this.courseUnits.get(courseUnitName).setNotCompleted();
        } else {
            var module = this.modules.get(moduleNames.get(0));
            moduleNames.remove(0);
            module.setNotCompleted(courseUnitName, moduleNames);
        }
        
        this.updateCredits();
    }
    
    /**
     * Updates the credits of this module and all the modules under this module
     * after adding or removing courses.
     */
    public void updateCredits(){
        this.credits = 0;
        Set<String> keySet = modules.keySet();
        for(String key : keySet) {
            modules.get(key).updateCredits();
            this.credits += modules.get(key).getCredits();
        }
        
        keySet = courseUnits.keySet();
        for(String key : keySet) {
            if(courseUnits.get(key).isCompleted()) {
                this.credits += courseUnits.get(key).getCredits();
            }
        }
    }
    
    /**
     * Returns the name of the Module.
     * 
     * @return the name of the Module.
     */
    public String getName() {
        return this.name;
    }
    
    /** 
     * Returns the type of the Module.
     * 
     * @return the type of the Module.
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the groupId of the Module.
     * 
     * @return the groupId of the Module.
     */
    public String getGroupId() {
        return this.groupId;
    }
    
    /**
     * Returns the current credits of the Module.
     * 
     * @return the current credits of the Module.
     */
    public int getCredits() {
        return this.credits;
    }
    
    /**
     * Returns the target credits of the Module.
     * 
     * @return the target credits of the Module.
     */
    public int getTargetCredits() {
        return this.targetCredits;
    }
    
    /**
     * Returns a TreeMap with this module's submodules.
     * 
     * @return a TreeMap with this module's submodules.
     */
    public TreeMap<String, Module> getModules() {
        return this.modules;
    }
    
    /**
     * Returns a TreeMap with this module's course units.
     * 
     * @return a TreeMap with this module's course units.
     */
    public TreeMap<String, CourseUnit> getCourseUnits() {
        return this.courseUnits;
    }
}