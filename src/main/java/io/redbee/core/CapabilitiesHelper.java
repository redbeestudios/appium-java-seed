package io.redbee.core;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CapabilitiesHelper {
    private DesiredCapabilities capabilities;
    private String defaultPath;

    public CapabilitiesHelper() {
        defaultPath = "src/resources/capabilities/";
        capabilities = new DesiredCapabilities();
    }

    public DesiredCapabilities getCapabilities() {
        return getCapabilities("capabilitiesDefault.properties");
    }

    public DesiredCapabilities getCapabilities(String fileName) {
        String filePath = defaultPath + fileName;
        // Leer el archivo de propiedades y agregar las capacidades
        try (FileInputStream input = new FileInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(input);
            properties.setProperty("app",
                    properties.getProperty("app").replace("${user.dir}", System.getProperty("user.dir")));
            for (String key : properties.stringPropertyNames()) {
                capabilities.setCapability(key, properties.getProperty(key));
            }
            return capabilities;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de capacidades: " + e.getMessage(), e);
        }
    }

}
