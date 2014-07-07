package therap.com;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

    public Login(String LoginName, String password, String code) {
        super(LoginName, password, code);
    }

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        baseURL = "https://staging.therapbd.net";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }

    @Test
    public void LoginTest() {

        //Go to Staging context
        driver.get(baseURL + "/auth/login");
        //Login name validation checking and login name input
        driver.findElement(By.xpath("//input[@value='Login']")).click();
        try {
            driver.findElement(By.id("loginName"));
            System.out.println("Login name blank  validation Error");
            driver.findElement(By.id("loginName")).sendKeys(LoginName);

        }
        catch (NoSuchElementException e){
                System.out.println("No element found");
        }

        //Password blank validation checking and provide password input
        driver.findElement(By.xpath("//input[@value='Login']")).click();
        try {
            driver.findElement(By.id("password"));
            System.out.println("Password blank validation Error");
            driver.findElement(By.id("password")).sendKeys(pasword);

        }
        catch (NoSuchElementException e){
            System.out.println("No element found");
        }
        //Provider Code blank validation checking and provide Provider Code
        driver.findElement(By.xpath("//input[@value='Login']")).click();
        try {
            driver.findElement(By.id("providerCode"));
            System.out.println("Provider Code blank validation Error");
            driver.findElement(By.id("providerCode")).sendKeys(code);

        }
        catch (NoSuchElementException e){
            System.out.println("password blank validation failed");
        }

        driver.findElement(By.xpath("//input[@value='Login']")).click();
        String title = driver.getTitle();
        System.out.println(title);

    }

    @After
    public void logout(){
        driver.findElement(By.linkText("Logout")).click();
        driver.close();
    }
}
