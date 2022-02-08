package bdd.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsManager {

    private static SettingsManager instance;

    private SettingsManager() {
        FileInputStream fileStream;
        Properties property = new Properties();

        try {
            fileStream = new FileInputStream("src/test/resources/bdd_example.properties");
            property.load(fileStream);
            baseURI = property.getProperty("baseURI");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    private static String baseURI;

    public static String getBaseURI() {
        return baseURI;
    }

    public static void setBaseURI(String baseURI) {
        SettingsManager.baseURI = baseURI;
    }
}
