package SeleniumFrameWork.prorail.nl;


import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import SeleniumFrameWork.prorail.nl.config.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class FieldController {
	
	static Logger log = Logger.getLogger(FieldController.class.getName());
	public Configuration conConfig;
	WebDriver driver;
	ArrayList<String> alElementTypes;
	
	public FieldController() {
		conConfig = Configuration.getConfigurationInstance();
		
		//TODO: Isn't there another way of doing this?
		alElementTypes = new ArrayList<String>();	//start fresh
		alElementTypes.add("Link no wait");
		alElementTypes.add("Button no wait");
		alElementTypes.add("Dialog");
	}
	
	public FieldController(WebDriver driver) {
		this();
		
		this.driver = driver;
  }

	/***
	 * Loads all the fields with their settings
	 * When an error occurs, null is returned
	 * @return
	 */
	public LinkedHashMap<String, TestField> loadAllFields() {
		LinkedHashMap<String, TestField> lhmReturn = new LinkedHashMap<String, TestField>();
	  XSSFWorkbook wbTestCases = null;
    XSSFSheet shtTestCase = null;
    XSSFRow rwFieldRow = null;
    XSSFCell clTempCell = null;
    TestField fldField = null;
    String strScreen = null;
    String strFieldName = null;
    String strFieldType = null;
    String strFieldID = null;
    String strXPath = null;
    String strText = null;
    String strInfo = null;
    String strName = null;
    int intLastRow = 0;
    int intLastColumn = 0;
    int intCurrentRow = 0;
    int intCurrentColumn = 0;
    
    try {
	    wbTestCases = new XSSFWorkbook(new FileInputStream(conConfig.CONFIGPROPERTIESFILE));
	    shtTestCase = wbTestCases.getSheet(conConfig.PROP_SHEET);
	    
	    intLastRow = shtTestCase.getLastRowNum();
	    intLastColumn = shtTestCase.getRow(0).getLastCellNum();	//first row, always filled
	    
	    /***
	     * Going through all the rows
	     */
	    while (intCurrentRow <= intLastRow && intCurrentColumn < intLastColumn) {
	    	rwFieldRow = shtTestCase.getRow(intCurrentRow);
	    	
	    	//Screen*	Field*	Type*	ID	Name	X-path	Text	Info
        /**********************
         * Setting the values *
         **********************/
        // Screen
	    	clTempCell = rwFieldRow.getCell(0);
        if (clTempCell != null) {
          strScreen = clTempCell.getStringCellValue().trim();
        }

        clTempCell = rwFieldRow.getCell(1);
        if (clTempCell != null) {
          strFieldName = clTempCell.getStringCellValue().trim();
        }

        clTempCell = rwFieldRow.getCell(2);
        if (clTempCell != null) {
          strFieldType = clTempCell.getStringCellValue().trim();
        }

        clTempCell = rwFieldRow.getCell(3);
        if (clTempCell != null) {
          strFieldID = clTempCell.getStringCellValue().trim();
        }

        clTempCell = rwFieldRow.getCell(4);
        if (clTempCell != null) {
        	strName = clTempCell.getStringCellValue().trim();
        }
        
        clTempCell = rwFieldRow.getCell(5);
        if (clTempCell != null) {
          strXPath = clTempCell.getStringCellValue().trim();
        }

        clTempCell = rwFieldRow.getCell(6);
        if (clTempCell != null) {
          strText = clTempCell.getStringCellValue().trim();
        }

        clTempCell = rwFieldRow.getCell(7);
        if (clTempCell != null) {
          strInfo = clTempCell.getStringCellValue().trim();
        }
	    	
        /***
         * Adding the values to the collection
         */
        if (strScreen != null) { 
        	
        	if (!strScreen.equals("Screen")) { 
        		
        		if (!lhmReturn.containsKey(strFieldName)) { 
        			fldField = new TestField(strScreen, strFieldName, strFieldType, strFieldID, strName, strXPath, strText, strInfo);
              
      	    	lhmReturn.put(strFieldName, fldField);

              // Script logic
              // first the field is added, then the checks are done.
              if (strFieldType != null && strFieldType.equalsIgnoreCase("Check Table")) {
              	
                if (strXPath == null || strXPath.equals("")) {
                  log.error("Field has no XPath: " + strFieldName + " (" + strScreen + ")");
                }
              }
            } 
            else {
              log.error("Field is double: " + strFieldName + " (" + strScreen + ")");
            }
        	} 
        }
        
        // remove all values, to clear up some memory
        clTempCell = null;
        strFieldName = null;
        strScreen = null;
        strFieldType = null;
        strFieldID = null;
        strName = null;
        strXPath = null;
        strText = null;
        strInfo = null;

        intCurrentRow++; 
        
	    }	
    }
    catch (Exception ex) {
	    log.error(ex.getMessage(), ex);
	    lhmReturn = null;
    }
    finally {
    	log.debug("Loaded " + lhmReturn.size() + " fields.");
    }
	  
	  return lhmReturn;
  }

	/***
	 * Get the content of the testcases.
	 * This function retrieves the Fieldname and its value. Then creates a nl.spp.TestField
	 *
	 * @return
	 */
	public LinkedHashMap<String,TestCase> loadTestCasesContent(LinkedHashMap<String,TestCase> lhmTestCases) {
		XSSFWorkbook wbTestCases = null;
	    XSSFSheet shtTestCase = null;
	    XSSFRow rwTCRow = null;
	    XSSFCell clTCCell = null;
	    XSSFCell clTempCell = null;
	    TestCase tcTemp = null;
	    String strFieldName = null;
	    String strValue = null;
	    String yearValue = null;
	    
	    log.debug("Retreiving the content of the testcases");
	    for(File testCaseFile : conConfig.TESTCASEFILES){
	    	int intCurrentRow = 0;
		    int intCurrentColumn = 2;	//Start with column C
		    int intLastRow = 0;
		    int intLastColumn = 0;
		    try {
		    	String testCaseFileLocation = testCaseFile.getCanonicalPath();
			    wbTestCases = new XSSFWorkbook(new FileInputStream(testCaseFileLocation));
			    shtTestCase = wbTestCases.getSheet(conConfig.TC_SHEET);
			    
			    intLastRow = shtTestCase.getLastRowNum();
			    intLastColumn = shtTestCase.getRow(0).getLastCellNum();	//first row, always filled
			    
			    while (intCurrentRow <= intLastRow && intCurrentColumn < intLastColumn) {
			    	rwTCRow = shtTestCase.getRow(intCurrentRow);
			    	
			    	//check if the row is not empty
			    	if(rwTCRow != null) {
			    		clTCCell = rwTCRow.getCell(intCurrentColumn);
			    	}
			    	else {
			    		clTCCell = null;
			    	}
			    	
				    if(clTCCell != null) {
				    	/****
				    	 * Get the Name of the TestCase
				    	 */
				    	if(intCurrentRow == 1) {
				    		tcTemp = lhmTestCases.get(clTCCell.getStringCellValue());
				    		if(tcTemp== null || tcTemp.equals("")){
				    			break;
				    		}
				    		log.debug("TestCase " + clTCCell.getStringCellValue() + " loaded.");
				    	}
				    	
				    	if(intCurrentRow >= 1) {
				    		
				    		/***
				    		 * Get the screen name
				    		 */
				    		clTempCell = rwTCRow.getCell(0);
				    		if(clTempCell != null) {
				    			clTempCell.getStringCellValue().trim();
				    		}
				    		
				    		/***'
				    		 * Get the field name
				    		 */
				    		clTempCell = rwTCRow.getCell(1);
				    		if(clTempCell != null) {
				    			strFieldName = clTempCell.getStringCellValue().trim();
				    		}
				    		
				    		/*****
				    		 * Retreive the value
				    		 */
				    		clTempCell = rwTCRow.getCell(intCurrentColumn);
				    		if(clTempCell != null) {
				    			
				    			//determine the type of cell
		    			switch (clTempCell.getCellType()) {
		    			
		    				case Cell.CELL_TYPE_STRING: // 1
		    					strValue = clTempCell.getStringCellValue();
		    					break;
				    					
				    		case Cell.CELL_TYPE_NUMERIC: // 0
				              if (DateUtil.isCellDateFormatted(clTempCell)) {
				            	  Date date = clTempCell.getDateCellValue();
				            	  SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
				                  SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
				                  SimpleDateFormat formatYearOnly = new SimpleDateFormat("yyyy");
				                  yearValue = formatYearOnly.format(date);
				                  if(yearValue.equals("1899")){
				                	  strValue = formatTime.format(date);
				                  } else {
				                	  strValue = formatDate.format(date);
				                  }
				                  
				              } 
				              else {
				                strValue = (int)clTempCell.getNumericCellValue() + "";
				              }
				              break;
				              
				    		case Cell.CELL_TYPE_BOOLEAN: // 4
				              strValue = clTempCell.getBooleanCellValue() + "";
				              break;
				              
				            case Cell.CELL_TYPE_FORMULA: // 2
				            	if (HSSFDateUtil.isCellDateFormatted(clTempCell)) {
				                    Date date = clTempCell.getDateCellValue();
				                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				                    strValue = format.format(date);
				                    break;
				                }
				            				        
				              strValue = clTempCell.getStringCellValue();
				              break;
				              
				            case Cell.CELL_TYPE_BLANK: // 3
				              strValue = "";
				              break;
				              
				            case Cell.CELL_TYPE_ERROR: // 5
				              strValue = clTempCell.getErrorCellString();
				              break;
				              
				            default:
				              log.debug("Undefined cell type: " + clTempCell.getCellType());
				              //break;
				    	}
				    			
		    			if(!strValue.equals("") && !strFieldName.equals("")) {
			    			//when a value is found, add it to the collection
			    			TestField tfTemp = new TestField(strFieldName.trim(), strValue.trim());
			    			tcTemp.addTestField(tfTemp);
		    			}
				    			
				    }	
				    		
				} 
			}
				    
	    	
			    	if (intCurrentRow == intLastRow) {
			    		intCurrentColumn++;
			    		intCurrentRow = 0;
			    	}
			    	else {
			    		intCurrentRow++;	
			    	}
			    } 
		    }
			  catch (Exception ex) {
			  	log.error(clTempCell + " - C" + clTempCell.getColumnIndex() + "/R" + clTempCell.getRowIndex());
			    log.error(ex.getMessage(), ex);
		    }
	    }
    	log.debug("Updated " + lhmTestCases.size() + " testcases.");
		return lhmTestCases;
	}
	
  /***
   * Contains the logic to fill in a value in a html-select
   * 
   * When a value is given, it will try to select the value. If the value is not
   * found, an exception is thrown. This is caught and all possible options are
   * listed in the logging.
   */
  public void SelectInput(TestField tfTemp) throws Exception {
    String strValue = tfTemp.getValue();
    Select selectBox = null;

    if (strValue != null && !strValue.equals("")) { // no value, no filling!

      try {        
      	if(tfTemp.getID() != null && !tfTemp.getID().equals("")) {
      		selectBox = new Select(driver.findElement(By.id(tfTemp.getID())));
      	}
      	else if(tfTemp.getName() != null && !tfTemp.getName().equals("")) {
      		selectBox = new Select(driver.findElement(By.name(tfTemp.getName())));
      	}
      	else if(tfTemp.getXPath() != null && !tfTemp.getXPath().equals("")) {
      		selectBox = new Select(driver.findElement(By.xpath(tfTemp.getXPath())));
      	}
      	else if(tfTemp.getText() != null && !tfTemp.getText().equals("")){
      		selectBox = new Select(driver.findElement(By.partialLinkText(tfTemp.getText())));
      	}
      	
        if (strValue.equalsIgnoreCase("[empty]")) {
          // when a select is empty, do nothing!
        } 
        else {
          if (tfTemp.getType().equalsIgnoreCase("Select")) {
            selectBox.selectByVisibleText(strValue);
          } 
          else if (tfTemp.getType().equalsIgnoreCase("Select Value")) {
            selectBox.selectByValue(strValue);
          }
        }
      } 
      catch (NoSuchElementException ex) { // catch any errors
       if (ex.getMessage().startsWith("Cannot locate element with text")) {
          List<WebElement> lstElements = selectBox.getOptions();

          String strOptions = null;
          Iterator<WebElement> iter = lstElements.iterator();

          while (iter.hasNext()) {
            WebElement weOption = iter.next();
            strOptions = strOptions + weOption.getText() + ", ";
          }

          strOptions = strOptions.substring(0, strOptions.length() - (", ").length());
          log.error("Possible options for '" + tfTemp.getFieldName() + "': " + strOptions);

        }
        else {
        	ex.printStackTrace();
        }
     
        throw new Exception(ex.getMessage());
      }
      catch (Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
      }

    } 
  } 

  /***
   * Executes a double click on the given field. The field is located by use of
   * its X-Path
   * 
   * @param csdbf
   * @throws Exception
   */
  public void ExecuteDoubleClick(TestField tfTemp) throws Exception {
    /***
     * For extra info:
     * http://stackoverflow.com/questions/3982442/selenium-2-webdriver
     * -how-to-double-click-a-table-row-which-opens-a-new-window
     */
    WebElement element = null;

    if (tfTemp.getValue() != null) {
      element = driver.findElement(By.xpath(tfTemp.getXPath()));
      // log.debug("Element: " + element.getText());

      ((JavascriptExecutor) driver)
          .executeScript(
              "var evt = document.createEvent('MouseEvents');"
                  + "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                  + "arguments[0].dispatchEvent(evt);", element);

      // wait for the update page
      element = driver.findElement(By.id("loading_display"));
      while (element.isDisplayed()) {
        // just wait.
      }

    } 
  }

  /***
   * Use it when elements are not visible. 
   * 
   * @param element
   */
  public void scrollToElement(WebElement element){
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
  }
  
	/***
	 * With this method it is possible to wait for a certain element.
	 * This has been separated from the Controller, because it is related with business logic.
	 * 
	 * Only for a number of types it is possible to wait, for now these are:
	 * - Link no wait
	 * 
	 * @param csdbf
	 * The field to wait for or after.
	 * 
	 * @param strWaitElementName
	 * The name to wait for. For CSDB it usually is the loading_display
	 * 
	 * @return
	 * When everything is OK, 0 is returned.
	 */
	public int waitForElement(TestField tfTemp, String strWaitElementName) {
		int intReturn = -1;	//if ok, return 0
		WebElement element = null;
		
		// wait for the page to update
    if (alElementTypes.contains(tfTemp.getType())) {
      element = driver.findElement(By.id(strWaitElementName));	//loading_display

      while (element.isDisplayed()) {
        // just wait.
      } 
      
      intReturn = 0;
      
    } 
		
		return intReturn;
	  
  }
	
	/***
	 * When a page is reloaded, a field will not be found.
	 * This method tries to solve that.
	 * 
	 * @param linkText
	 */
	public void clickAnElementByLinkText(String linkText) {
    while(ExpectedConditions.presenceOfElementLocated(By.linkText(linkText)) != null) {
    	//...
    }
    
    driver.findElement(By.linkText(linkText)).click();
  }
  
}
