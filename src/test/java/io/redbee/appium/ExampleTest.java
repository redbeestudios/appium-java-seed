package io.redbee.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class ExampleTest{

    @Test
    public void first_test() throws MalformedURLException {
        AppiumDriver driver;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "11.0");
        capabilities.setCapability("deviceName", "Pixel_3_API_30");
        capabilities.setCapability("appWaitActivity", "*");
        capabilities.setCapability("app",
                System.getProperty("user.dir") + "/apps/Demo.apk");
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        driver.quit();
    }




}
