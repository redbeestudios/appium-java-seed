package io.redbee.appium;

 import org.testng.annotations.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import static java.time.Duration.ofMillis;
import static java.util.Collections.singletonList;
 
 
 public class ExampleTest extends BaseTest {



 
@Test(groups = {"regression"})
    public void first_test() throws InterruptedException  {
      By xpath = By.xpath("//android.widget.Button[@content-desc='Swipe']");
      Thread.sleep(   1000);
      WebElement button = driver.findElement(xpath);
      button.click();
      Thread.sleep(   2000);



      xpath = By.xpath("(//android.view.ViewGroup[@content-desc='card'])[1]");
      WebElement sw = driver.findElement(xpath);


      Point source = sw.getLocation();
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
  
    Thread.sleep(   2000);

  }

}
