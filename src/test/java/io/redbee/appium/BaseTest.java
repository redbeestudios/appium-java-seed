package io.redbee.appium;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static java.time.Duration.ofMillis;
import static java.util.Collections.singletonList;

import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;
import io.redbee.core.AndroidDriverHelper;
import java.time.Duration;

public class BaseTest {

    protected AndroidDriverHelper androidDriverHelper;
    protected AppiumDriver driver;

    protected WebDriverWait wait;

    @BeforeClass
    @Parameters("capabilitiesFile")
    public void setup(@Optional("capabilitiesDefault.properties") String capabilitiesFile) throws Exception {
        androidDriverHelper = AndroidDriverHelper.getInstance();
        driver = androidDriverHelper.startDriver(capabilitiesFile);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @AfterClass
    public void tearDown() throws Exception {
        androidDriverHelper.stopDriver();
    }

    private WebElement waitForElementVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitAndClickElement(By locator) {
        waitForElementVisibility(locator).click();
    }

    public void sendKeysToElement(By locator, String text) {
        waitForElementVisibility(locator).sendKeys(text);
    }

    public String getTextOfElement(By locator) {
        return waitForElementVisibility(locator).getText();
    }

    public void horizontalSwipingTest(By locator) {
        // xpath = By.xpath("(//android.view.ViewGroup[@content-desc='card'])[1]");
        // WebElement swip = driver.findElement(locator);

        Point source = waitForElementVisibility(locator).getLocation();
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 0);

        sequence.addAction(finger.createPointerMove(ofMillis(0),
                PointerInput.Origin.viewport(), source.x, source.y));

        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(new Pause(finger, ofMillis(600)));

        sequence.addAction(finger.createPointerMove(ofMillis(600),
                PointerInput.Origin.viewport(), source.x - 1000, source.y));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(singletonList(sequence));

    }

}