package io.redbee.core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public void startDriver(AppiumServerHelper server) throws Exception {
        startDriver(null, server);
    }

    public void startDriver(String capabilitiesFile, AppiumServerHelper server) throws Exception {
        DesiredCapabilities capabilities;
        LOGGER.log(Level.INFO, "Driver starting...");
        cleanupEmulator();
        try {
            if (capabilitiesFile != null) {
                LOGGER.log(Level.INFO, "set capabilities from file " + capabilitiesFile);
                capabilities = capabilitiesHelper.getCapabilities(capabilitiesFile);
            } else {
                // Obtener capacidades por defecto
                LOGGER.log(Level.INFO, "set default capabilities");
                capabilities = capabilitiesHelper.getCapabilities();
            }
            startAndroidEmulator();

            // Iniciar el driver de Appium
            driver = new AndroidDriver(server.getUrl(), capabilities);
            LOGGER.log(Level.INFO, "Driver started successfully");
            // } catch (MalformedURLException e) {
            // handleError(e, "Error when initializing the driver.",Level.SEVERE);
        } catch (TimeoutException e) {
            handleError(e, "Timeout when initializing the driver", Level.SEVERE);
        } catch (Exception e) {
            handleError(e, "Unknown error occurred while initializing the driver.", Level.SEVERE);
        }

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

    private void startAndroidEmulator() throws Exception {
        LOGGER.log(Level.INFO, "Android Emulator starting...");
        String emulatornName = getEmulatorName();
        String commandStartEmulator = "emulator -avd <emulator_name> -wipe-data";
        commandStartEmulator = commandStartEmulator.replace("<emulator_name>", emulatornName);
        try {
            Runtime.getRuntime().exec(commandStartEmulator);
            LOGGER.log(Level.INFO, "Emulator staring... ");

            waitUntilEmulatorIsReady();
        } catch (Exception e) {
            handleError(e, "Error while start emulator: " + e.getMessage(), Level.SEVERE);
        }
    }

    private String getEmulatorName() throws Exception {
        LOGGER.log(Level.INFO, "getEmulatorName...");
        String name = "";
        try {
            Process process = Runtime.getRuntime().exec("emulator -list-avds");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();

            LOGGER.log(Level.INFO, "getEmulatorName output " + output);

            if (output != null) {
                String[] emulatorNames = output.split("\\r?\\n");
                name = emulatorNames[0];
                LOGGER.log(Level.INFO, "getEmulatorName emulatorNames " + name);
            } else {
                throw new IllegalStateException("No available emulators found");
            }

        } catch (Exception e) {
            handleError(e, "Error getting available emulators: " + e.getMessage(), Level.SEVERE);
        }
        LOGGER.log(Level.INFO, "getEmulatorName return " + name);
        return name;
    }

    private void waitUntilEmulatorIsReady() throws Exception {
        String commandCheckEmulatorStatus = "adb wait-for-device shell getprop sys.boot_completed";
        boolean emulatorStarted = false;
        int maxAttempts = 75;
        int attempt = 0;

        try {
            Process process = Runtime.getRuntime().exec(commandCheckEmulatorStatus);
            InputStream inputStream = process.getInputStream();

            // Leer la salida del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder output = new StringBuilder();
            String line;

            while (!emulatorStarted && attempt < maxAttempts) {
                if (reader.ready()) {
                    line = reader.readLine();
                    output.append(line);
                } else {
                    // Esperar un tiempo antes de volver a comprobar
                    Thread.sleep(1000);
                }

                // Comprobar si la salida contiene el valor "1" 
                if (output.toString().trim().equals("1")) {
                    emulatorStarted = true;
                    LOGGER.log(Level.INFO, "the emulator is up!");
                }

                attempt++;
            }

            if (!emulatorStarted) {
                throw new TimeoutException("The emulator did not start in the maximum time allowed.");
            }
        } catch (Exception e) {
            handleError(e, "Error wait until emulator is ready: " + e.getMessage(), Level.SEVERE);
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
            String commandDeleteTempFiles = "adb emu avd snapshot delete all <emulator_name>";
            String emulatorName = getEmulatorName();
            commandDeleteTempFiles = commandDeleteTempFiles.replace("<emulator_name>", emulatorName);

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
