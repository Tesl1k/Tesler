package configs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private final Properties properties;
    private final String fileName;

    public Config(String fileName) {
        this.fileName = fileName;
        properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(fileName);
            properties.load(input);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key, "");
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
        try {
            FileOutputStream output = new FileOutputStream(fileName);
            properties.store(output, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
