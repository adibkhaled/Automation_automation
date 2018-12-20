package SeleniumFrameWork.prorail.nl.config;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Configuration {

	private static Configuration conConfig;
	public final String SCREENSHOTS_LOCATION = getScreenshotLocation();
	public final String HTMLREPORT_LOCATION = getHTMLLocation();
	public final File[] TESTCASEFILES = getTestCaseFiles();
	public final File CONFIGPROPERTIESFILE = getConfigPropertiesFile();
	public final String CONFIG_SHEET = "Configuration";
	public final String PROP_SHEET = "Properties";
	public final String TC_SHEET = "Testcases";
	static Logger log = Logger.getLogger(Configuration.class.getName());
	static Logger testResultsLog = Logger.getLogger("TestResultsLogger");
	public LinkedHashMap<String, String> lhmConfigItems;
	
	/***
	 * Empty Constructor.
	 * Declare the constructor to be private so that no one can create an instance of this class except the class itself
	 */
	private Configuration() {		
	}
	
	private File getConfigPropertiesFile() {
		return new File(".//ConfigurationFiles//Configuration.test.xlsx");
	}
		

	private String getScreenshotLocation() {
		try {
			String path = new File(".//Screenshots//").getCanonicalPath();
			return path;
		} catch (IOException e) {
			String message = "Not able to find screenshot folder";
			log.error(message);
			testResultsLog.error(message);
		}
		return null;
	}
	
	//Date: 27-06-2018: Add new function
	
	private String getHTMLLocation() {
		try {
			String path = new File(".//HTMLReport//").getCanonicalPath();
			return path;
		} catch (IOException e) {
			String message = "Not able to find Log folder";
			log.error(message);
			testResultsLog.error(message);
		}
		return null;
	}

	private File[] getTestCaseFiles() {
		File folder = new File(".//Testcases//");
    	File[] testCaseFiles = folder.listFiles();
    	if(testCaseFiles == null){
    		try {
    			String message = "No files found in: " + folder.getCanonicalPath();
				log.error(message);
				testResultsLog.error(message);
			} catch (IOException e) {
				log.error("No files found!");
				testResultsLog.error("No files found!");
			}
    		return null;
    	}
    	Arrays.sort(testCaseFiles);
		return testCaseFiles;
	}

	/***
	 * This static method will return a reference to the same instance no matter how many times it is called.
	 * 
	 * For now errorhandling is OK.
	 * 
	 * @return
	 */
	public static Configuration getConfigurationInstance() {
		// will be null the first time this is called.
		if ( null == conConfig ) {
			conConfig = new Configuration();
			
			if(conConfig.setConfiguration() != 0) {
				conConfig = null;
			}
    }
		return conConfig;
  }
	
	/***
	 * Retrieves all configuration from Excel and puts them in a linked hashmap.
	 * @return 
	 * @throws Exception 
	 */
	public int setConfiguration() {
	    XSSFWorkbook wbTestCases = null;
	    XSSFSheet shtConfiguration = null;
	    XSSFRow rwRow = null;
	    XSSFCell clCell = null;
	    int intLastRow = 0;
	    int intCurrentRow = 0;
	    String strKey = null;
	    String strValue = null;
	    int intError = 0;
	    
	    lhmConfigItems = new LinkedHashMap<String, String>();
	    File fileLocation = conConfig.CONFIGPROPERTIESFILE;
	        
	    try {
		    wbTestCases = new XSSFWorkbook(new FileInputStream(fileLocation));
		    shtConfiguration = wbTestCases.getSheet(CONFIG_SHEET);
		    
		    intLastRow = shtConfiguration.getLastRowNum();
		    
		    while (intCurrentRow <= intLastRow) {
			    rwRow = shtConfiguration.getRow(intCurrentRow);
			    
			    //Format: Category, Name, Property, Info
			    clCell = rwRow.getCell(1);
			    clCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		      strKey = clCell.getStringCellValue();
		      
			    clCell = rwRow.getCell(2);
			    if(clCell != null) {
			      clCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			      strValue = clCell.getStringCellValue();
			    }
		      
		      if(strKey != null && strValue != null ) {
		        lhmConfigItems.put(strKey, strValue);
		        log.debug("Configuration: " + strKey + " | " + strValue);
		      }
		      
		      intCurrentRow++;
		    }	
		    
		    addDefaultConfigItems();
		    
	    }
	    catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		    intError = 1;
	    }
	    finally {
	    	log.debug("Loaded " + lhmConfigItems.size() + " configuration items.");
	    }
    
    return intError;
	}
	
	private void addDefaultConfigItems() {
		lhmConfigItems.put("LowestPriority", "5");	//work with Strings
  }

	/****
	 * Returns the value of a property
	 * 
	 * @param strProperty
	 * @return
	 */
	public String getProperty(String strProperty) {
		String strReturn;
		
		strReturn = lhmConfigItems.get(strProperty);
		
		return strReturn;
	}
	
}
