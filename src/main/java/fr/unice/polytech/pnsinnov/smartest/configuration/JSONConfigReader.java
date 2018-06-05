package fr.unice.polytech.pnsinnov.smartest.configuration;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONConfigReader implements ConfigReader {
    private static final Logger logger = LogManager.getLogger(JSONConfigReader.class);

    @Override
    public Configuration readConfig(Path path) {
        logger.info("Reading " + path + " as a json file");
        JSONObject config = readJSONFromFile(path);

        return new ConfigurationHolder(
                Paths.get(getFieldFromConfig(ConfigKey.VCS_PATH, config)).toAbsolutePath(),
                Paths.get(getFieldFromConfig(ConfigKey.PLUGIN_PATH, config)).toAbsolutePath(),
                Paths.get(getFieldFromConfig(ConfigKey.PROJECT_PATH, config)).toAbsolutePath(),
                getFieldFromConfig(ConfigKey.LANGUAGE, config),
                getFieldFromConfig(ConfigKey.PRODUCTION_TOOL, config),
                getFieldFromConfig(ConfigKey.TEST_FRAMEWORK, config),
                getFieldFromConfig(ConfigKey.VCS, config)
        );
    }

    private String getFieldFromConfig(ConfigKey key, JSONObject config){
        if(config.containsKey(key.getConfigName())){
            return config.get(key.getConfigName()).toString();
        }
        return key.getDefaultValue();
    }

    private JSONObject readJSONFromFile(Path configPath) {
        FileReader fileReader;
        JSONParser jsonParser = new JSONParser();

        try {
            fileReader = new FileReader(configPath.toAbsolutePath().toFile());
            JSONObject res = (JSONObject) jsonParser.parse(fileReader);
            fileReader.close();

            return res;
        } catch (ParseException | IOException e) {
            logger.error("Could not find or parse the config.smt file", e);
            return new JSONObject();
        }
    }
}
