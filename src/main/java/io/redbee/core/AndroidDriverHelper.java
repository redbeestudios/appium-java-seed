package io.redbee.core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;

public class AndroidDriverHelper {
    private static AndroidDriverHelper instance;
    private AndroidDriver driver;
    private CapabilitiesHelper capabilitiesHelper;
    private static final Logger LOGGER = Logger.getLogger(AndroidDriverHelper.class.getName());

    private AndroidDriverHelper() {
        capabilitiesHelper = new CapabilitiesHelper();

    }
 
    public static synchronized AndroidDriverHelper getInstance() {
        if (instance == null) {
            instance = new AndroidDriverHelper();
        }
        return instance;
    }
 
    public AppiumDriver getDriver() throws Exception {
        if (driver == null) {
            IllegalStateException e = new IllegalStateException("Driver has not been started. Call startDriver() first.");
            handleError(e, e.getMessage(), Level.SEVERE);        
        }
        return driver;
    }

    public void startDriver() throws Exception {
        startDriver(null);
    }

    public void startDriver(String capabilitiesFile) throws Exception {
        DesiredCapabilities capabilities;
        LOGGER.log(Level.INFO, "Driver starting...");
        try {
            if (capabilitiesFile != null) {
                LOGGER.log(Level.INFO, "set capabilities from file " + capabilitiesFile);
                capabilities = capabilitiesHelper.getCapabilities(capabilitiesFile);
            } else {
                // Obtener capacidades por defecto
                LOGGER.log(Level.INFO, "set default capabilities");
                capabilities = capabilitiesHelper.getCapabilities();
            }

            // Iniciar el driver de Appium
            driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
            LOGGER.log(Level.INFO, "Driver started successfully");
        } catch (MalformedURLException e) {
            handleError(e, "Error when initializing the driver.",Level.SEVERE);
        } catch (TimeoutException e) {
            handleError(e, "Timeout when initializing the driver",Level.SEVERE);
        } catch (Exception e) {
            handleError(e, "Unknown error occurred while initializing the driver.",Level.SEVERE);
        }

    }

    public void stopDriver() throws Exception {
        if (driver != null) {
            try {
                driver.quit();
                driver = null;
                LOGGER.log(Level.INFO, "Driver stopped successfully");
            } catch (Exception e) {
                handleError(e, "Error while stopping the driver: " + e.getMessage() ,Level.WARNING);
         
            }
        }
    }

    

    private void handleError(Exception e, String message, Level l) throws Exception {
        LOGGER.log(l, message, e);
        e.printStackTrace();
        // mas cositas

        throw e;
    }
}
