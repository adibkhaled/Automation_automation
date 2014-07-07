package therap.com;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 3/24/14
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Scomm extends Login {
    int count;
    String subject = "This is scomm" + count;
    String body= "Lore Lipsum" + count;

    public Scomm(String LoginName, String password, String code) {
        super(LoginName, password, code);
    }

    /*This Test check the Scomm(s) validation message and Create multiple Scomm
    Method=scommCreateTest
    * */
    public void scommCreateTest() {


        //Login Therap Application using LoginTest()
        super.LoginTest();
        //Wait 2 sec after login
        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);

        //click compose button
        driver.get(baseURL + "/ma/scomm/composeBasic?type=3");
        driver.switchTo().defaultContent().findElement(By.name("send_")).click();

        //validation error checking

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue("Recipient validation not found!", bodyText.contains("At least one recipient must be selected"));
        assertTrue("Subject validation not found!", bodyText.contains("Required"));


        //For loop for Multiple Scomm(s) create.
            for (int i = 0; i < 1; i++) {
            driver.get(baseURL + "/ma/scomm/composeBasic?type=3");
            WebElement divId;
            divId = driver.findElement(By.id("loginDiv"));
            if (divId != null) {
                driver.findElement(By.xpath("//div[@id='loginDiv']/table/tbody/tr[td[contains(.,'Adib')]]")).click();//Select Recipient which contains Adib.
            }

            driver.findElement(By.xpath("//div[@onclick='addToRecipient();']")).click();
            driver.findElement(By.id("radio2")).click();
            driver.findElement(By.id("summary")).sendKeys(subject);
            driver.switchTo().frame("messageBody_ifr").findElement(By.id("tinymce")).sendKeys(body);
            driver.switchTo().defaultContent().findElement(By.name("send_")).click();
            count = count + 1;
            }
    }

    /*After create Scomm, Check tha Scomm from Sent Box*/
    @Test
    public void scommListTest()  {

        super.LoginTest();
        driver.get(baseURL + "/ma/scomm/showMailbox?selectedFolderId=2&fromMailbox=true");

        WebElement SendSComm = driver.findElement(By.xpath("//tbody/tr[@class='mailbox_row']//td//span[contains(.,'This is scomm')]"));
        SendSComm.click();
        driver.manage().timeouts().implicitlyWait(6000, TimeUnit.MILLISECONDS);
        try {

            driver.findElement(By.tagName("body")).equals(subject);
            String subjectPrint=driver.findElement(By.xpath("//td[@class='headPart1']//tr[3]/td[3]")).getText();
            System.out.println("Subject text : \n"+ subjectPrint);
        }
        catch (WebDriverException ex){
            System.out.println("Can not found subject");
        }


    }

}



