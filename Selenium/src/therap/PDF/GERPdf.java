package therap.PDF;

import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 6/24/14
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class GERPdf extends Login {


    public GERPdf(String EventStart, String FormId, String EventEnd) {
        super(EventStart, FormId, EventEnd);
    }


    @Test
    public void GERPdfSave () {

        driver.get(baseURL + "ma/fpage2/search.do?formType=GER");
        driver.findElement(By.linkText("Search")).click();
        driver.findElement(By.id("formId")).sendKeys(FormId);
        driver.findElement(By.id("beginDate")).sendKeys(EventStart);
        driver.findElement(By.id("endDate")).sendKeys(EventEnd);
        driver.findElement(By.xpath("//input[@value='Search']")).click();


    }


}

