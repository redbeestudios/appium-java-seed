package io.redbee.core;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AppiumServerHelper {
    private static final Logger LOGGER = Logger.getLogger(AppiumServerHelper.class.getName());

    private final AppiumServiceBuilder builder;
    private AppiumDriverLocalService service;

    public AppiumServerHelper() {
        builder = new AppiumServiceBuilder();
        // Now is all hardcoded but should be configure from properties file
        builder.withIPAddress("");
        builder.usingAnyFreePort();
        //builder.withAppiumJS(null);
        //builder.usingDriverExecutable();
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "info");
    }

    /**
     * Create {{@link AppiumDriverLocalService}} from {{@link AppiumServiceBuilder}}
     */
    private void createService() {
        service = AppiumDriverLocalService.buildService(builder);
    }

    /**
     * Start the {{@link AppiumDriverLocalService}}
     */
    public void start() {
        if (!isServiceRunning()) {
            createService();
            LOGGER.log(Level.INFO, "Appium Service is not running. Starting new service.");
            service.start();
        } else {
            LOGGER.log(Level.WARNING, "Appium Service is already running. Unnecessary start it.");
        }
    }

    /**
     * Check if {{@link AppiumDriverLocalService}} is running
     *
     * @return boolean
     */
    public boolean isServiceRunning() {
        if (service == null) return false;
        return service.isRunning();
    }

    /**
     * Stop the {{@link AppiumDriverLocalService}}
     */
    public void stop() {
        if (isServiceRunning()) {
            LOGGER.log(Level.INFO, "Stopping the Appium Service.");
            service.stop();
        } else {
            LOGGER.log(Level.WARNING, "Cant Stop the Appium Service because it is not running.");
        }
    }

    /**
     * Returns this service {@link AppiumDriverLocalService}.
     *
     * @return {{@link AppiumDriverLocalService}}
     */
    public AppiumDriverLocalService getService() {
        return service;
    }
}
