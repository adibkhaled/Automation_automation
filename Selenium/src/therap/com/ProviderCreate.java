package therap.com;

import org.openqa.selenium.By;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static therap.com.UniqueID.anyRandomInt;


/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 6/3/14
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProviderCreate extends Login {

    public static void main(String[] args) {

        Random random = new Random();
        // generates a random int in a range from low int to high int
        for (int i = 0; i < 3; i++) {
            anyRandomInt(random);
        }
    }


    public ProviderCreate(String LoginName, String password, String code) {
        super(LoginName, password, code);
    }
    public void ProviderCreateTest(Random random){

        int count =random.nextInt();
        //Login Therap Application using LoginTest()
        super.LoginTest();
        //Wait 2 sec after login
        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        driver.findElement(By.id("provider.code")).sendKeys("DDS-AR");
        driver.findElement(By.id("provider.name")).sendKeys("ARAKANSAS STATE " + count );
        driver.findElement(By.id("provider.address.street1")).sendKeys("House#001, Road#09, Plot# 8, Gulshan");
        driver.findElement(By.id("provider.county")).sendKeys("AR");
        driver.findElement(By.id("provider.address.city")).sendKeys("Arkansas");



    }
}
