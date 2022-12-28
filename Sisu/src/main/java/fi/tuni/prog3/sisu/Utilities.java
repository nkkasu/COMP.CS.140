package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * A class for helper functions
 */
public class Utilities {

    /**
     * Gets all DegreeProgrammes from Sisu API
     * 
     * @return A TreeMap where key is the name and value is the groupId of the 
     * DegreeProgramme 
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static TreeMap<String, String> getDegreeProgrammes()
            throws MalformedURLException, IOException {
        TreeMap<String, String> degreeProgrammes = new TreeMap<>();

        var url = new URL("https://sis-tuni.funidata.fi/kori/api/module-search?"
                + "curriculumPeriodId=uta-lvv-2021&universityId=tuni-university"
                + "-root-id&moduleType=DegreeProgramme&limit=1000");
        String jsonString = new String(url.openStream().readAllBytes());
        JsonArray jsonArray = JsonParser
                .parseString(jsonString).getAsJsonObject()
                .get("searchResults").getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            String name = jsonArray.get(i).getAsJsonObject().get("name")
                    .getAsString();
            String groupId = jsonArray.get(i).getAsJsonObject().get("groupId")
                    .getAsString();
            degreeProgrammes.put(name, groupId);
        }
        return degreeProgrammes;
    }
    
    /**
     * Returns the current date and time as a string
     * 
     * @return the current date and time as a string
     */
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}
