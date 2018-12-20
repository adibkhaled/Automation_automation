package utility;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import SeleniumFrameWork.prorail.nl.Controller;
import SeleniumFrameWork.prorail.nl.TestCase;
import SeleniumFrameWork.prorail.nl.config.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;

public class Utils {

	public Configuration conConfig = Configuration.getConfigurationInstance();
	static Logger log = Logger.getLogger(Utils.class.getName());
	private static ExtentReports extent;
	static Logger testResultsLog = Logger.getLogger("TestResultsLogger");
	Controller ctrl;

	/***
	 * Empty constructor
	 */
	public Utils() {

	}

	/**
	 * Use this method to return the actual date - time 
	 * Format used is for: FramWork class
	 * 
	 * @return
	 */
	public String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		Calendar calendar = Calendar.getInstance();

		return dateFormat.format(calendar.getTime());
	}

	public static String getReportCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}

	public static synchronized ExtentReports getReporter() {
		String strReportFileName;
		String reportPath = null;
		try {
			if (extent == null || extent != null) {
				// if yes, use default location
				reportPath = Configuration.getConfigurationInstance().HTMLREPORT_LOCATION + "\\";
				strReportFileName = reportPath + "HTMLReport_" + getReportCurrentDateTime() + ".html";
				extent = new ExtentReports(strReportFileName, true);
			}
			log.debug(" HTML Report Created: " + extent);

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage(), ex);
		}
		return extent;
	}

	/****
	 * This method is used to take a screenshot.
	 * 
	 * strFileName: [TestCaseName].png 
	 * strLocation: V:\werkmap\Adib\Selenium_project\FBPln\Screenshots\
	 * 
	 * @param driver
	 * @param strLocation
	 * TODO: Check if simplify the code
	 */
	public void takeScreenshot(WebDriver driver, String strLocation) {
		String strFileName;
		String strTestCase = null;
		
		// Get All Testcases
		ctrl = new Controller();
		LinkedHashMap<String, TestCase> lhmTestCases = new LinkedHashMap<String, TestCase>();
		lhmTestCases = ctrl.getTestCases();
		
		
		if (lhmTestCases != null) {
			// Get the all Testcase contents
			ctrl.loadTestCaseContents(lhmTestCases);
		}
		
		LinkedHashMap<String, TestCase> lhmFilteredTestCases = ctrl.filterTestCases(lhmTestCases);
		
		// Testcases NULL check
		if (lhmFilteredTestCases != null && !lhmFilteredTestCases.isEmpty()) {
			
			// This for loop to get each Testcase Name
			
			for (Entry<String, TestCase> testCaseEntry : lhmFilteredTestCases.entrySet()) {
				// Get Testcase contents key value
				strTestCase = testCaseEntry.getKey();
				
				// Get Testcase contents string value
				strTestCase = strTestCase.toString();
								
				try {
					
					if (strLocation == null || strLocation.equals("") || strLocation.equalsIgnoreCase("[Yes]")) {
						// if yes, use default location
						strLocation = conConfig.SCREENSHOTS_LOCATION + "\\";
					}
					
					// set location and rename the FileName
					strFileName = strLocation + strTestCase + ".png";
					File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					screenshot.renameTo(new File(strFileName));
					testResultsLog.info("Screenshots taken Path: " + strFileName);

				} catch (Exception ex) {
					ex.printStackTrace();
					log.error(ex.getMessage(), ex);
				}
			}
		}
	}

	/****
	 * This method is used by FrameWork to Set a Screenshot Location for the ExtendReport.
	 * 
	 * @param strTestCase
	 * @return strLocation
	 */
	public String setScreenshotPath(String strLocation) {
		
		//default ScreenShots Location.
		strLocation = conConfig.SCREENSHOTS_LOCATION + "\\" + strLocation + ".png";
		return strLocation;
	}

}