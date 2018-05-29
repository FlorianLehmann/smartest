package fr.unice.polytech.pnsinnov.smartest.explorertree;



import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class ConfigReader {

    private JSONObject content;

    private String jsonLangage = "langage";
    private String jsontFramework = "tFramework";
    private String jsonManagement = "management";

    public ConfigReader(String configPath){
        this.content = readJSONFromFile(configPath);
    }

    public String getLangageFromConfig(){
        return content.get(jsonLangage).toString();
    }

    public String getTestFrameworkFromConfig(){
        return content.get(jsontFramework).toString();
    }

    public Optional<String> getManagementFromConfig(){
        if(content.containsKey(jsonManagement)){
            return Optional.of(content.get(jsonManagement).toString());
        }
        return Optional.empty();
    }

    private JSONObject readJSONFromFile(String configPath) {
        FileReader fileReader;
        JSONParser jsonParser = new JSONParser();

        try {
            fileReader = new FileReader(configPath);
            JSONObject res = (JSONObject) jsonParser.parse(fileReader);
            fileReader.close();

            return res;
        } catch (IOException|ParseException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }
}
