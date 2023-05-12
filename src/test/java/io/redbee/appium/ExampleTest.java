package io.redbee.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.redbee.core.CapabilitiesHelper;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

 import java.net.MalformedURLException;
import java.net.URL;

public class ExampleTest{

    @Test
    public void first_test() throws MalformedURLException {
        AppiumDriver driver;

        CapabilitiesHelper helper = new CapabilitiesHelper();
      //  DesiredCapabilities capabilities = helper.getCapabilities();
        DesiredCapabilities capabilities = helper.getCapabilities("otherCapabilities.properties");
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        driver.quit();
    }




}
