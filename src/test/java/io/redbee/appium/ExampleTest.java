package io.redbee.appium;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
  
 public class ExampleTest extends BaseTest {

 
@Test(groups = {"regression"})
    public void first_test() throws InterruptedException  {
      By xpath = By.xpath("//android.widget.Button[@content-desc='Swipe']");
      Thread.sleep(   1000);
      WebElement button = driver.findElement(xpath);
      button.click();
      Thread.sleep(   2000);

      xpath = By.xpath("(//android.view.ViewGroup[@content-desc='card'])[1]");
      horizontalSwipingTest(xpath);
 
    Thread.sleep(   2000);

  }
}
