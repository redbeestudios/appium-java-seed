package io.redbee.appium;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.appium.java_client.AppiumDriver;
import io.redbee.core.AndroidDriverHelper;
 
 
public class BaseTest {
    protected AndroidDriverHelper androidDriverHelper;
    protected AppiumDriver driver;

    @BeforeClass
   
    @Parameters("capabilitiesFile")
    public void setup(@Optional("capabilitiesDefault.properties") String capabilitiesFile) throws Exception {
        System.out.println("Valor del parámetro capabilitiesFile: " + capabilitiesFile);
        androidDriverHelper = AndroidDriverHelper.getInstance();
        androidDriverHelper.stopDriver();

        driver = androidDriverHelper.startDriver(capabilitiesFile);
    
    }

    @AfterClass
    public void tearDown() throws Exception {
        androidDriverHelper.stopDriver();
    }
}
