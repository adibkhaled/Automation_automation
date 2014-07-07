package therap.com;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 4/16/14
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class loginGmail {
    WebDriver driver;
    String baseURL;
    String mail = "adibtester052@gmail.com";
    String password = "itestertherap";
    String canvasFrame = "canvas_frame";
    int count;
    String Subject = "This is test Scomm By Adib Bin Khaled";
    String Body = "Test";

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        baseURL = "http://www.gmail.com";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(baseURL);
        //driver.findElement(By.xpath("//button[@value='adibkhaled@gmail.com']")).click();
        driver.findElement(By.id("Email")).sendKeys(mail);
        driver.findElement(By.id("Passwd")).sendKeys(password);
        //Sing In Gmail
        WebElement singIn = driver.findElement(By.id("signIn"));
        singIn.click();

    }

    @Ignore
    public void Mail_Send_Test() {

        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        //Compose Double click
        Actions action = new Actions(driver);
        WebElement composeBtn = driver.findElement(By.xpath("//div[@role='button' and .='COMPOSE']"));
        action.click(composeBtn);
        action.perform();

        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        WebElement toBox = driver.findElement(By.cssSelector("textarea[class='vO']"));
        toBox.sendKeys("adibkhaled@gmail.com");

        WebElement subject = driver.findElement(By.cssSelector("input[name='subjectbox']"));
        subject.sendKeys(Subject);

        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[contains(@tabindex,'1') and contains(@frameborder,'0')]")));
        WebElement body = driver.findElement(By.xpath("//body[@role='textbox']"));
        body.sendKeys(Body);
        driver.switchTo().defaultContent();

        WebElement send = driver.findElement(By.xpath("//div[@role='button' and .='Send']"));
        send.click();
    }

    @Test
    public void Mail_Search_Test() {
/*        WebElement goSend = driver.findElement(By.xpath("//div[@title='Sent Mail' and .='Sent Mail']"));
        goSend.click();*/

        WebElement searchMail = driver.findElement(By.xpath("//input[@class='gbqfif']"));
        searchMail.sendKeys("is:sent " + Subject);

        WebElement searchMailClick = driver.findElement(By.xpath("//button[@aria-label='Google Search']"));
        searchMailClick.click();

        try {

            Assert.assertTrue(Subject,true);
        }
        catch (Exception e){
            return;
        }

        driver.switchTo().frame(canvasFrame);
        WebElement signOut = driver.findElement(By.xpath("//div[.='Sign out']"));
        signOut.click();
        driver.switchTo().defaultContent();

    }

}