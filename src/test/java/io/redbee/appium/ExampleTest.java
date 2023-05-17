package io.redbee.appium;

 import org.testng.annotations.Test;

 import org.openqa.selenium.By;
 import org.openqa.selenium.WebElement;
 
 public class ExampleTest extends BaseTest {



 
@Test(groups = {"regression"})
    public void first_test() throws InterruptedException  {
      By xpath = By.xpath("//android.widget.Button[@content-desc='Login']");
      Thread.sleep(   1000);
      WebElement button = driver.findElement(xpath);
      button.click();
      Thread.sleep(   2000);
  }

}


 