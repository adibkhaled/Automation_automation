package SeleniumFrameWork.prorail.nl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import SeleniumFrameWork.prorail.nl.config.*;
import SeleniumFrameWork.prorail.nl.FoundNonExpectedValueException;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Controller {

	static Logger log = Logger.getLogger(Controller.class.getName());
	static Logger testResultsLog = Logger.getLogger("TestResultsLogger");
	public static final String TC_SHEET = "Testcases";
	public static final String PROP_SHEET = "Properties";
	public static final String FF_LOCATION = "D:\\Programs\\Firefox\\firefox.exe";
	static final String BROWSER_FF = "FireFox";	//similar to configuration sheet
	static final String BROWSER_CHROME = "Chrome";	//similar to configuration sheet
	public Configuration conConfig;
	public WebDriver driver;
	public ProcessController conProc;
	
	/**
	 * Constructor.
	 * Create the necessary objects
	 */
	public Controller() {
		conConfig = Configuration.getConfigurationInstance();
		conProc = new ProcessController();
	}
	
	/***
	 * Retrieves settings from Excel and set them for the following run
	 * @return 
	 */
	public int setProperties() {
		int intError = 0;
		
		if (conConfig == null) {
			log.error("An error is occured, running is halted.");
			intError = 1;
		}
		
		return intError;
	}
	
	/****
	 * Get the value of a property
	 * 
	 * @param strProperty
	 * @return
	 */
	public String getProperty(String strProperty) {
		String strReturn;
		
		strReturn = conConfig.getProperty(strProperty);
		
		return strReturn;
	}
	
	/***
	 * Retrieves all testcases from excel
	 * When an error occurs, null is returned
	 * 
	 * @return ArrayList with all testcases
	 */
	public LinkedHashMap<String, TestCase> getTestCases() {
		LinkedHashMap<String, TestCase> lhmReturn = new LinkedHashMap<String, TestCase>();
		XSSFWorkbook wbTestCases = null;
	    XSSFSheet shtTestCase = null;
	    XSSFRow rwTCRow = null;
	    XSSFRow rwTCNameRow = null;
	    XSSFRow rwTCWeightRow = null;
	    XSSFCell clTCCell = null;
	    XSSFCell clTCNameCell = null;
	    XSSFCell clTCWeightCell = null;
	    int intCurrentCell = 0;
	    String strTCName = null;
	    String strTCWeight = null;
	    
	    for(File file: conConfig.TESTCASEFILES)
	    {	
	    	intCurrentCell = 0;
		    try {
		    	String filePath = file.getCanonicalPath().toString();
			    wbTestCases = new XSSFWorkbook( new FileInputStream(filePath));
			    shtTestCase = wbTestCases.getSheet(TC_SHEET);
			    
			      rwTCRow = shtTestCase.getRow(0);	//row with the numbers
			      rwTCNameRow = shtTestCase.getRow(1);	//row with the names
			      rwTCWeightRow = shtTestCase.getRow(2);	//row with the weight
			      
			      int intLastCellNR = rwTCRow.getLastCellNum();
					    
			      while (intCurrentCell <= intLastCellNR) {
			      	
			      	clTCCell = rwTCRow.getCell(intCurrentCell);
			      	
			      	if(clTCCell != null) {
			      		clTCCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			      		
			      		if(clTCCell.getStringCellValue().startsWith("TC")) {
			      			
			      			//get the values from row 1 (Name)
			      			clTCNameCell = rwTCNameRow.getCell(intCurrentCell);
			      			clTCNameCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			      			strTCName = clTCNameCell.getStringCellValue();
			      			
			      			//get the values from row 2 (Weight)
			      			clTCWeightCell = rwTCWeightRow.getCell(intCurrentCell);
			      			clTCWeightCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			      			strTCWeight = clTCWeightCell.getStringCellValue();
			      			
			      			if(lhmReturn.containsKey(strTCName)) {
			      				//is the TestCase already added?
			      				log.error("Duplicate TestCase "+strTCName+"! Please adjust the names.");
			      			}
			      			
			      			//create the new TestCase
			      			TestCase tc = new TestCase(strTCName, strTCWeight);
			      			
			      			//add to the ArrayList
			      			lhmReturn.put(strTCName, tc);
			      		}
			      	}
			      	
					    intCurrentCell++;
				    }	//end while
			    }
			    catch (Exception ex) {
				    //e.printStackTrace();
				    log.error(ex.getMessage(), ex);
			    }
	    	}
	    	log.debug("Loaded " + lhmReturn.size() + " testcases.");
			return lhmReturn;
	}

	/***
	 * Loads all the fields with their settings
	 * When an error occurs, null is returned
	 * @return
	 */
	public LinkedHashMap<String, TestField> loadAllFields() {
		LinkedHashMap<String, TestField> lhmReturn = new LinkedHashMap<String, TestField>();
	  
		FieldController conField = new FieldController();
		lhmReturn = conField.loadAllFields();
		
	  return lhmReturn;
  }

	/***
	 * Adds the needed data to the fields of the testcases.
	 */
	public void loadTestCaseContents(LinkedHashMap<String, TestCase> lhmTestCases) {
		FieldController conField = new FieldController();
		conField.loadTestCasesContent(lhmTestCases);
	}
	
	/***
	 * Initialize and start the browser
	 * 
	 * Put all initialization here
	 */
	public void startBrowser(String strBrowser) {
		BrowserController conBrows = new BrowserController();
		driver = conBrows.loadBrowser(conConfig.getProperty("Browser"));
		driver.get(conConfig.getProperty("URL Browser"));
	  	testResultsLog.info("Open Browser " + conConfig.getProperty("Browser") + "with URL of :" + conConfig.getProperty("URL Browser"));
    // wait for the page to load
	  int intPageLoad = Integer.parseInt(conConfig.getProperty("Pageload"));
    (new WebDriverWait(driver, intPageLoad)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
      	Configuration conConfig = Configuration.getConfigurationInstance();	//otherwise nullpointer
      	
        return d.getTitle().toLowerCase().contains(conConfig.getProperty("Pagetitle").toLowerCase());
      }
    });
    
    //giving the driver away
    conProc.setDriver(driver);
    
    log.info("Firefox started.");
		
	}

	/***
	 * Stopping the browser
	 * 
	 * @param strBrowser Specify the type of browser
	 */
	public void stopBrowser(String strBrowser) {
		
		if(strBrowser.equals(BROWSER_FF)) {
			log.info("Stopping " + BROWSER_FF);
		}
		else if(strBrowser.equals(BROWSER_CHROME)) {
			log.info("Stopping " + BROWSER_CHROME);
		}

		if (driver != null) {
			driver.quit();
			driver = null;
			driver.close();
		}
	    // sleep for 2 seconds
	    try {
	      Thread.sleep(2000);
	    } 
	    catch (InterruptedException ex) {
	    	log.error(ex.getMessage(), ex);
	    }
	}
	
	/***
	 * This method is used to call the functions that filter the Testcases.
	 * With every filter, a new LHM is created. Unknown if it is safe...
	 * @param lhmTestCases 
     * @return  
	 */
	public LinkedHashMap<String,TestCase> filterTestCases(LinkedHashMap<String,TestCase> lhmTestCases) {
		LinkedHashMap<String,TestCase> lhmTemp = new LinkedHashMap<String,TestCase>();
		LinkedHashMap<String,TestCase> lhmTemp2 = new LinkedHashMap<String,TestCase>();
		
		lhmTemp = filterStartTestCase(lhmTestCases);
		lhmTemp2 = filterWeightTestCases(lhmTemp);
		
		return lhmTemp2;
	}
	
	/***
	 * This method filters the testcases that come before the starting number set in the Options
	 * Execute this method as the first, because of the starting numbers.
	 * 
	 * @param lhmTestCases
	 * @return
	 */
	public LinkedHashMap<String,TestCase> filterStartTestCase(LinkedHashMap<String,TestCase> lhmTestCases) {
		
	// 2.1 Get the TC from position X 
			int intTestCaseNumber = 1;
			Set<String> setToRemove = new HashSet<String>();
			
			for (Entry<String, TestCase> entry : lhmTestCases.entrySet()) {
				
				//start with correct testcase
				if(intTestCaseNumber < Integer.parseInt( getProperty("Start with TC") ) ) {
					setToRemove.add(entry.getKey());
				}
				
				intTestCaseNumber++;
			}
			
			for(String strTemp : setToRemove) {
				lhmTestCases.remove(strTemp);
			}
			
			String message = "(filterStartTestCase) Removed " + setToRemove.size() + " testcases. Total is now " + lhmTestCases.size() + ".";
			handleLogging(message);
			
			return lhmTestCases;
	}

	private void handleLogging(String message) {
		log.debug(message);
		testResultsLog.info(message);
	}
	
	/***
	 * This function checks if the testcases have the proper weight.
	 */
	public LinkedHashMap<String,TestCase> filterWeightTestCases(LinkedHashMap<String,TestCase> lhmTestCases) {
			// 2.2 Get the TC with the correct weight
		
		Set<String> setToRemove = new HashSet<String>();	
		int intTCWeight = 0;
		int intPropWeight = Integer.parseInt( getProperty("Weight") );
		
		for (Entry<String, TestCase> entry : lhmTestCases.entrySet()) {
			
			//start with correct testcase
			intTCWeight = entry.getValue().getWeightInt();
			
			//1 = important, 5 = all
			if(intPropWeight > intTCWeight ) {
				setToRemove.add(entry.getKey());
			}
		}
		
		for(String strTemp : setToRemove) {
			lhmTestCases.remove(strTemp);
		}
		
		String message = "(filterWeightTestCases) Removed " + setToRemove.size() + " testcases. Total is now " + lhmTestCases.size() + ".";
		handleLogging(message);
		
		return lhmTestCases;
		
	}

	/***
	 * Fill in extra info needed for the filtered testcases.
	 * The fields of the testcases only have the Name and value. 
	 * The remaining properties have to be filled in and/or additional actions have to be done.
	 * This is done after filtering, to save time.
	 * 
	 * 
	 * @param lhmTestFields
	 * @return
	 */
	public LinkedHashMap<String, TestCase> fillInExtraInfo(LinkedHashMap<String, TestCase> lhmTestCases, 
					                                               LinkedHashMap<String, TestField> lhmTestFields) {
		
		
		log.debug("Retreiving extra info");
		
		TestCase tcTemp;
		ArrayList<TestField> alTemp;
		TestField tfValues = null;
		
		//going through the testcases
		for (Entry<String, TestCase> entry : lhmTestCases.entrySet()) {
			
			tcTemp = entry.getValue();
			alTemp = tcTemp.getAlFields();
			
			//going through the fields
			for (TestField tfField : alTemp) {
				
				tfValues = lhmTestFields.get(tfField.getFieldName());
				
				if(tfValues != null) {
					tfField.setID(tfValues.getID());
					tfField.setName(tfValues.getName());
					tfField.setInfo(tfValues.getInfo());
					tfField.setScreen(tfValues.getScreen());
					tfField.setText(tfValues.getText());
					tfField.setType(tfValues.getType());
					tfField.setXpath(tfValues.getXPath());
				}
				else if( (tfField.getFieldName()).equalsIgnoreCase("LINK") ) {
					//if the field is a link, set the name as a type
					tfField.setType(tfField.getFieldName());
				}
				
			}	
		}
		
		return lhmTestCases;
  }

	/***
	 * Process the given field
	 * 
	 * @param tfTemp
	 * @throws Exception 
	 */
	public void processField(TestField tfTemp) throws Exception {
		
    if (tfTemp != null) {
    	try{
    		conProc.processField(tfTemp);
    	}
    	catch(FoundNonExpectedValueException ex)
    	{
    		throw new FoundNonExpectedValueException(ex.getMessage());
    	}
    	catch(Exception ex)
    	{
    		throw new Exception(ex.getMessage());
    	}
    }	  
}
}
	
