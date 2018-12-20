package SeleniumFrameWork.prorail.nl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


import utility.*;

public class FrameWork {

	static Logger log = Logger.getLogger(FrameWork.class.getName());
	static Logger testResultsLog = Logger.getLogger("TestResultsLogger");
	LinkedHashMap<String, TestCase> lhmTestCases = new LinkedHashMap<String, TestCase>();
	LinkedHashMap<String, TestField> lhmTestFields = new LinkedHashMap<String, TestField>();
	Controller ctrl;
	Utils utils;
	int testCasesTotalNumber;
	
	ExtentReports extent = Utils.getReporter(); // Initial of the the HTML Report Path
	ExtentTest extentTest = extent.startTest("Dashboard Report For Selenium" , "All Test Cases for FB"); // Using ExtentReport library to generated HTML report. 


	/***	This is basic code for HTML Report. This code is changed After few modification of Utils class. 
	 * Now The report path is read from Utils class.  Please check Utils class to check report path.  
	 
	String reportPath = "V:\\werkmap\\Adib\\Selenium_project\\FBPln\\logs\\Selenium_Report.html";	
	ExtentReports extent = new ExtentReports(reportPath, true);
	ExtentTest extentTest = extent.startTest("Dashboard Report For Selenium" , "All Test Cases for FB");
	*/
	/***implementation of extentReport
	 *
	 *extentTest method for Log report 
	 * //implementation of extentTest.log(LogStatus.PASS,  "TestCase: " + strTestCase + " succeeded " + utils.getCurrentDateTime());
	 * //implementation of extentTest.log(LogStatus.FAIL, "TestCase: " + tcCase.strNumber + " failed! " + utils.getCurrentDateTime());
	 */
	
	
	/***
	 * Empty Constructor
	 */
	public FrameWork() {
		ctrl = new Controller();
		testCasesTotalNumber = 0;
		utils = new Utils();
	}
	
	
	/***
	 * To execute all steps
	 */
	public void start() {
		int intReturn = 0;
		
		/***
		 * 1. Preparation
		 */
		// 1.1 Get the properties
		intReturn = ctrl.setProperties();
		
		// 1.2 Load all available fields.
		if(intReturn == 0) {
			//load the fields
			lhmTestFields = ctrl.loadAllFields();
			
			if(lhmTestFields == null) {
				intReturn = 1;
			}
		}
		
		// 1.3 load the testcases
		if(intReturn == 0) {
			lhmTestCases = ctrl.getTestCases();
			
			if(lhmTestCases == null || !lhmTestCases.isEmpty()) {
				intReturn = 1;
			}
		}
		
		// 1.4 load the content of the testcases
		if(lhmTestCases != null || !lhmTestCases.isEmpty()) {
			//get the testcase contents
			ctrl.loadTestCaseContents(lhmTestCases);
		}
		
		/***
		 * 2. Prepare the testcases
		 * For each action, create a separate function in the controller.
		 */
		LinkedHashMap<String, TestCase> lhmFilteredTestCases = ctrl.filterTestCases(lhmTestCases);
		
		// 2.3 Fill in the missing info for the fields
		ctrl.fillInExtraInfo(lhmFilteredTestCases, lhmTestFields);
		
		/***
		 * 3. Run the filtered testcases.
		 */
		testCasesTotalNumber = lhmFilteredTestCases.size();
		running(lhmFilteredTestCases);
	}

	/****
	 * Running
	 * 
	 * @param lhmFilteredTestCases
	 */
	public void running(LinkedHashMap<String, TestCase> lhmFilteredTestCases) {
		String strTestCase = null;
		TestCase tcCase = null;
		ArrayList<TestField> alFields = null;

		log.debug("Executing testcases");
		
		if(lhmFilteredTestCases != null && !lhmFilteredTestCases.isEmpty()) {
			
			//check if the browser needs to be started, if no start the browser
			if(ctrl.getProperty("Restart browser").equalsIgnoreCase("No")) {
				ctrl.startBrowser(ctrl.getProperty("Browser"));
			}
			
			// Going through the testcases
			for (Entry<String, TestCase> testCaseEntry : lhmFilteredTestCases.entrySet()) {
				try{
					//retrieve the testcase
					strTestCase = testCaseEntry.getKey();
					tcCase = testCaseEntry.getValue();
					String startMessage = "TestCase: " + strTestCase + " started " + utils.getCurrentDateTime();
					testResultsLog.info(startMessage);
					log.debug(startMessage);
					
					//check if the browser needs to be started
					if(ctrl.getProperty("Restart browser").equalsIgnoreCase("Yes")) {
						ctrl.startBrowser(ctrl.getProperty("Browser"));
					}
					
					//get all the fields of a testcase
					alFields = tcCase.getAlFields();
					
					// Going through the fields of the testcase
					for (TestField tfTemp : alFields) {
						// process the field
						try{
							ctrl.processField(tfTemp);
						}
						catch(FoundNonExpectedValueException fnevEx)
						{
							handleLogging(tcCase, fnevEx);
						}
					}
					
					//check if the browser needs to be stopped
					if(ctrl.getProperty("Restart browser").equalsIgnoreCase("Yes")) {
						ctrl.stopBrowser(ctrl.getProperty("Browser"));
					}
				}catch(Exception e){
					handleLogging(tcCase, e);
					testResultsLog.error("TestCase: " + tcCase.strNumber + " failed " + utils.getCurrentDateTime());
					/*** using extentTest Report* */
					extentTest.log(LogStatus.FAIL, "TestCase: " + strTestCase + " Failed " + utils.getCurrentDateTime());
					extentTest.log(LogStatus.INFO, "Screencast below: " + extentTest.addScreenCapture(utils.setScreenshotPath(strTestCase)));
					ctrl.stopBrowser(ctrl.getProperty("Browser"));
					continue;
				}
				if(!tcCase.failed){
					testResultsLog.info("TestCase: " + strTestCase + " succeeded " + utils.getCurrentDateTime());
					/*** using extentTest Report * */
					extentTest.log(LogStatus.PASS, "TestCase: " + strTestCase + " succeeded " + utils.getCurrentDateTime());
					
				}else{
					testResultsLog.error("TestCase: " + tcCase.strNumber + " failed! " + utils.getCurrentDateTime());
					/*** using extentTest Report * */
					extentTest.log(LogStatus.FAIL, "TestCase: " + strTestCase + " Failed " + utils.getCurrentDateTime());
					extentTest.log(LogStatus.INFO, "Screencast below: " + extentTest.addScreenCapture(utils.setScreenshotPath(strTestCase)));
					
				}
			}	
			
			//check if the browser needs to be stopped, if no stop it the browser
			if(ctrl.getProperty("Restart browser").equalsIgnoreCase("No")) {
				ctrl.stopBrowser(ctrl.getProperty("Browser"));
			}
			testResultsLog.info("Fails: " + CountFailedTestCases(lhmFilteredTestCases)); 
			testResultsLog.info("Succeed: " + CountSucceededTestCases(lhmFilteredTestCases));
			
			
			extentTest.assignAuthor("Test case Run by: " + ctrl.getProperty("Testcase Run By"));
			extent.endTest(extentTest);
			extent.flush();
		}	
	}

	private int CountFailedTestCases(LinkedHashMap<String, TestCase> lhmFilteredTestCases) {
		int failedTestCases = 0;
		for (Entry<String, TestCase> testCaseEntry : lhmFilteredTestCases.entrySet())
		{
			if(testCaseEntry.getValue().failed == true)
			{
				failedTestCases++;
			}
		}
		return failedTestCases;
	}
	
	private int CountSucceededTestCases(LinkedHashMap<String, TestCase> lhmFilteredTestCases) {
		int succeededTestCases = 0;
		for (Entry<String, TestCase> testCaseEntry : lhmFilteredTestCases.entrySet())
		{
			if(testCaseEntry.getValue().failed == false)
			{
				succeededTestCases++;
			}
		}
		return succeededTestCases;
	}

	private void handleLogging(TestCase tcCase, Exception e) {
		log.debug(e.getMessage().toString());
		testResultsLog.error(e.getMessage());
		tcCase.setFailed();
	}
	
}