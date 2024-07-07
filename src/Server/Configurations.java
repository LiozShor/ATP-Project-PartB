package Server;

import java.io.*;
import java.util.Properties;

public class Configurations {
    private static final String CONFIG_FILE = "resources/properties.config";

    private static Configurations instance;
    private Properties properties;

    private Configurations() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configurations getInstance() {
        if (instance == null) {
            instance = new Configurations();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getThreadPoolSize() {
        return Integer.parseInt(getProperty("threadPoolSize"));
    }

    public String getMazeGeneratingAlgorithm() {
        return getProperty("mazeGeneratingAlgorithm");
    }

    public String getMazeSearchingAlgorithm() {
        return getProperty("mazeSearchingAlgorithm");
    }
}
