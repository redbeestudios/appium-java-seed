package io.redbee.core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;
import java.util.logging.Logger;


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
            IllegalStateException e = new IllegalStateException(
                    "Driver has not been started. Call startDriver() first.");
            handleError(e, e.getMessage(), Level.SEVERE);
        }
        return driver;
    }

    public AppiumDriver  startDriver( ) throws Exception {
        startDriver(null);
        return getDriver();
    }

    public AppiumDriver  startDriver(String capabilitiesFile ) throws Exception {
        AppiumServerHelper server = new AppiumServerHelper();
        server.stop();
        server.start();
        DesiredCapabilities capabilities;
        LOGGER.log(Level.INFO, "Driver starting...");
        cleanupEmulator();
       
            if (capabilitiesFile != null) {
                LOGGER.log(Level.INFO, "set capabilities from file " + capabilitiesFile);
                capabilities = capabilitiesHelper.getCapabilities(capabilitiesFile);
            } else {
                // Obtener capacidades por defecto
                LOGGER.log(Level.INFO, "set default capabilities");
                capabilities = capabilitiesHelper.getCapabilities();
            }
            try {
            // Iniciar el driver de Appium
            driver = new AndroidDriver(server.getUrl(), capabilities);
            LOGGER.log(Level.INFO, "Driver started successfully");
        } catch (TimeoutException e) {
            handleError(e, "Timeout when initializing the driver", Level.SEVERE);
        } catch (Exception e) {
            handleError(e, "Unknown error occurred while initializing the driver.", Level.SEVERE);
        }
        return getDriver();

    }

    public void stopDriver() throws Exception {
        if (driver != null) {
            try {
                driver.quit();
                driver = null;
                // Detener el emulador
                cleanupEmulator();
                LOGGER.log(Level.INFO, "Driver stopped successfully");
            } catch (Exception e) {
                handleError(e, "Error while stopping the driver: " + e.getMessage(), Level.WARNING);

            }
        }
    }    

    private void cleanupEmulator() throws Exception {
        // Detener el emulador de Android
       stopAndroidEmulator();

        // Eliminar archivos temporales del emulador
        deleteEmulatorTempFiles();
    }

    private void stopAndroidEmulator() throws Exception {
        try {
            String commandStopEmulator = "adb emu kill";
            Runtime.getRuntime().exec(commandStopEmulator);
        } catch (Exception e) {
            handleError(e, "Error while stop emulator: " + e.getMessage(), Level.WARNING);
        }
    }

    private void deleteEmulatorTempFiles() throws Exception {
        try {
            // Comando para eliminar archivos temporales del emulador
            String commandDeleteTempFiles = "adb emu avd snapshot delete all";
  
            // Ejecutar el comando
            Process process = Runtime.getRuntime().exec(commandDeleteTempFiles);
            process.waitFor();

        } catch (Exception e) {
            handleError(e, "Error while deleting emulator temp files: " + e.getMessage(), Level.WARNING);
        }
    }

    private void handleError(Exception e, String message, Level l) throws Exception {
        LOGGER.log(l, message, e);
        e.printStackTrace();
        // mas cositas

        throw e;
    }
}
