package io.redbee.appium;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.appium.java_client.AppiumDriver;
import io.redbee.core.AndroidDriverHelper;

import io.redbee.core.AppiumServerHelper;
 
import org.testng.annotations.BeforeMethod;
 
public class BaseTest {
    protected AndroidDriverHelper androidDriverHelper;
    protected AppiumDriver driver;
    protected AppiumServerHelper server  ;

    @BeforeClass
   
    @Parameters("capabilitiesFile")
    public void setup(@Optional("capabilitiesDefault.properties") String capabilitiesFile) throws Exception {
        server = new AppiumServerHelper();
        server.stop();
        server.start();
       
        System.out.println("Valor del parámetro capabilitiesFile: " + capabilitiesFile);

        androidDriverHelper = AndroidDriverHelper.getInstance();
        androidDriverHelper.startDriver(capabilitiesFile,server);
        driver = androidDriverHelper.getDriver();
    }

    @AfterClass
    public void tearDown() throws Exception {
        androidDriverHelper.stopDriver();
    }
}
