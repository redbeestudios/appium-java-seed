package io.redbee.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.redbee.core.AppiumServerHelper;
import io.redbee.core.CapabilitiesHelper;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

 import java.net.MalformedURLException;
import java.net.URL;

public class ExampleTest{

    AppiumServerHelper  server = new AppiumServerHelper();

    @BeforeMethod(groups = {"regression"})
    public void setUP(){
        server.stop();
        server.start();
    }

    @Test(groups = {"regression"})
    public void first_test() {
        AppiumDriver driver;

        CapabilitiesHelper helper = new CapabilitiesHelper();
      //  DesiredCapabilities capabilities = helper.getCapabilities();
        DesiredCapabilities capabilities = helper.getCapabilities("otherCapabilities.properties");
        driver = new AndroidDriver(server.getService(), capabilities);
        driver.quit();
    }
}
