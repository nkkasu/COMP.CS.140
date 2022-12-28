package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.TreeMap;

/**
 * A class that represents a degree programme
 */
public class DegreeProgramme extends Module {   
    
    private TreeMap<String, String> optionalModules = new TreeMap<>();
    
    /**
     * Constructs a DegreeProgramme object
     * @param groupId
     * @throws IOException 
     */
    public DegreeProgramme(String groupId) throws IOException {
        super(groupId);
        var jsonReader = new ModuleJsonReader(groupId);
        this.optionalModules = jsonReader.getOptionalModules();
    }
    
    /**
     * Returns a TreeMap of optional modules/study fields
     * @return a TreeMap of optional modules/study fields
     */
    public TreeMap<String, String> getOptionalModules() {
        return this.optionalModules;
    }
}
