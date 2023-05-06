package io.redbee.core;

import io.redbee.testSupport.LogsHandler;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.logging.*;


public class AppiumServerHelperTest {
    private static final Logger logger = Logger.getLogger(AppiumServerHelper.class.getName());

    private LogsHandler logsHandler;
    private AppiumServerHelper appiumServerHelper;


    @BeforeMethod(groups = {"regression"})
    public void setUp() {
        logsHandler = new LogsHandler();
        appiumServerHelper = new AppiumServerHelper();
    }

    /**
     * AppiumServerHelper should be able to start the Appium Service
     */
    @Test(groups = {"regression"})
    public void startingNewAppiumServiceWithValidConfiguration_runsAppiumService() {
        logger.addHandler(logsHandler);

        appiumServerHelper.start();

        Assert.assertEquals(logsHandler.getLogRecords().size(), 1);
        Assert.assertEquals(logsHandler.getLogRecords().get(0).getMessage(),
                "Appium Service is not running. Starting new service.",
                "Info about starting server was not detected");
        Assert.assertTrue(appiumServerHelper.isServiceRunning(), "Appium Service is not running");
    }

    /**
     * AppiumServerHelper doesn't should be able to start the Appium Service that is running.
     */
    @Test(groups = {"regression"})
    public void startingAppiumServiceWithItAlreadyRunning_notRunsOtherAppiumService() {
        appiumServerHelper.start();
        logger.addHandler(logsHandler);

        appiumServerHelper.start();

        Assert.assertEquals(logsHandler.getLogRecords().size(), 1);
        Assert.assertEquals(logsHandler.getLogRecords().get(0).getMessage(),
                "Appium Service is already running. Unnecessary start it.",
                "Warning about other server already running was not detected");
        Assert.assertTrue(appiumServerHelper.isServiceRunning(), "Appium Service is not running");
    }

    /**
     * AppiumServerHelper should be able to stop the Appium Server that is running
     */
    @Test(groups = {"regression"})
    public void stoppingAppiumLocalServiceThatIsRunning_stopsAppiumLocalService() {
        appiumServerHelper.start();
        logger.addHandler(logsHandler);

        appiumServerHelper.stop();

        Assert.assertEquals(logsHandler.getLogRecords().size(), 1);
        Assert.assertEquals(logsHandler.getLogRecords().get(0).getMessage(), "Stopping the Appium Service.",
                "Info about stopping server was not detected");
        Assert.assertFalse(appiumServerHelper.isServiceRunning(), "Appium Server is running");
    }

    /**
     * AppiumServerHelper should be able to stop the Appium Server that is running
     */
    @Test(groups = {"regression"})
    public void stoppingAppiumServiceThatIsNotRunning_happenNothing() {

        logger.addHandler(logsHandler);

        appiumServerHelper.stop();
        Assert.assertEquals(logsHandler.getLogRecords().size(), 1);
        Assert.assertEquals(logsHandler.getLogRecords().get(0).getMessage(),
                "Cant Stop the Appium Service because it is not running.",
                "Warning about other server already running was not detected");
        Assert.assertFalse(appiumServerHelper.isServiceRunning(), "Appium Server is running");
    }
}