package therap.com;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.lang.String;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 3/24/14
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */



public class SetupBase {
    WebDriver driver;
    String baseURL;

    public void setUp() {
        driver = new FirefoxDriver();
        baseURL = "https://staging.therapbd.net";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }
        public void closeUp(){
            driver.findElement(By.linkText("Logout")).click();
            driver.quit();
    }
}
