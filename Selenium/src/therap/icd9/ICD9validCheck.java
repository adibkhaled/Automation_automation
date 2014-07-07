package therap.icd9;

import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Adib
 * Date: 5/8/14
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ICD9validCheck extends  ReadCSV{

    private WebDriver driver;
    String baseURL="http://www.cms.gov/medicare-coverage-database/staticpages/icd-9-code-lookup.aspx";
    TestResultXmlUtility testResultXmlUtility=new TestResultXmlUtility();
    List<TestCases> testcases=new ArrayList<TestCases>();

    public ICD9validCheck(String ICD9Code) {
        super(ICD9Code);
    }

/*    public void setUp() {
        driver = new FirefoxDriver();

    }*/

    @Test
    public void ICD9checkTest() throws ParserConfigurationException {


        driver.findElement(By.name("ctl00$ctl00$CMSGMainStaticContentPlaceHolder$MCDContentPlaceHolder$SearchICD9Code")).sendKeys(ICD9Code);
        driver.findElement(By.id("ctl00_ctl00_CMSGMainStaticContentPlaceHolder_MCDContentPlaceHolder_SubmitButton")).click();
        //validation check
        try{
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue("Invalid ICD9 code", bodyText.contains("There are no ICD-9 Codes that match that fragment"));
        System.out.println("Invalid ICD9 code: " + ICD9Code);
         //obtain windows handler name
         String windowsHandle=driver.getWindowHandle();
         //assert that a window has been launched
         assertEquals(true, windowsHandle.length() > 0);
         //add a test case to the testcases list as pass
         testcases.add(new TestCases(ICD9Code,"Invalid","Pass"));
        //write the test result to xml file with file name TestResult
        testResultXmlUtility.WriteTestResultToXml("TestResult.xml", testcases);
        }
        catch (AssertionFailedError ex){
            System.out.println("valid ICD9: " + ICD9Code);
            testcases.add(new TestCases(ICD9Code,"valid ","Fail"));
            //write the test result to xml file with file name TestResult
            testResultXmlUtility.WriteTestResultToXml("TestResult.xml", testcases);
            driver.close();
        }
        driver.close();
    }

    @Before
    public void Setup() {
        try
        {
            //initialize Firefox driver
            driver=new FirefoxDriver();
            driver.get(baseURL);

        }
        catch(Exception e)
        {
            ////add a test case to the testcases list as Fail
            testcases.add(new TestCases(ICD9Code,"valid ","Fail"));
        }

    }

    @After
    public void TearDown() throws IOException, ParserConfigurationException {
        //close the driver
        driver.close();
        //quit the driver
        driver.quit();
    }

}
