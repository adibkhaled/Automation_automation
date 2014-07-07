package therap.PDF;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;


/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 3/24/14
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */


public class Login extends ReadCSV {
    WebDriver driver;
    String baseURL;
    String LoginName = "ger_all";
    String password = "therap";
    String code = "SQA-TH";
    String EventStart = EventStartDate;
    String FormId = formID;
    String EventEnd  = EventEndDate;


    public Login(String EventStartDate, String formID, String EventEndDate) {
        super(EventStartDate,formID,EventEndDate);

    }

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        baseURL = "https://develop.therapbd.net";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(baseURL + "/auth/login");
        driver.findElement(By.id("loginName")).sendKeys(LoginName);
        driver.findElement(By.id("Passwd")).sendKeys(password);
        driver.findElement(By.id("providerCode")).sendKeys(code);
        //Login button
        driver.findElement(By.xpath("//input[@value='Login']")).click();

    }

    /* @After
    public void logout(){
        driver.findElement(By.linkText("Logout")).click();
        driver.close();
    }  */
}
