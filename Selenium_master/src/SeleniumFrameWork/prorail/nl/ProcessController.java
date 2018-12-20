package SeleniumFrameWork.prorail.nl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import SeleniumFrameWork.prorail.nl.config.*;
import SeleniumFrameWork.prorail.nl.FoundNonExpectedValueException;
import utility.*;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.Color;


public class ProcessController {

	static Logger log = Logger.getLogger(ProcessController.class.getName());
	static Logger testResultsLog = Logger.getLogger("TestResultsLogger");
	public Configuration conConfig = Configuration.getConfigurationInstance();
	WebDriver driver;
	Utils utils;
	ColorUtils clutils; 
	Map<String, String> rememberMap = new HashMap<String, String>();
	String originalHandle;
	WaitTimeForResponse waitTimeForResponse = new WaitTimeForResponse();

	public ProcessController() {
		utils = new Utils();
		clutils = new ColorUtils();
	}

	/***
	 * Sets the WebDriver needed to do the actions
	 * 
	 * @param driver
	 */
	public void setDriver(WebDriver driver) {
		this.driver = driver;
		this.originalHandle = driver.getWindowHandle();
	}

	/***
	 * Process the given field
	 * 
	 * @param tfTemp
	 * @throws Exception
	 */
	public void processField(TestField tfTemp) throws Exception {
		String strType = tfTemp.getType();
		String strValue = tfTemp.getValue();

		// Check if not null
		if (tfTemp != null) {
			log.debug("Processing field: " + tfTemp.getFieldName() + " - " + tfTemp.getType() + " - " + strValue);
			
			if (strType == null && !tfTemp.getFieldName().equals("Testcase")
					&& !tfTemp.getFieldName().equals("Weight")) {
				testResultsLog.error("Type is NULL of field: " + tfTemp.getFieldName() + " - value: " + strValue);
			}
			
			try {
				if (strType != null) {

					// types start with the function
					if (strType.toUpperCase().startsWith("CHECK")) {
						checkInput(tfTemp);
					} else if (strType.toUpperCase().startsWith("REMEMBER")) {
						rememberValue(tfTemp);
					} else if (strType.toUpperCase().startsWith("BROWSER")) {
						browserInput(tfTemp);
					}
					// else if ( strType.toUpperCase().startsWith("QUERY") ) {
					// Database db = new Database();
					// db.processQuery(tfTemp);
					// }
					else if (strType.toUpperCase().startsWith("WAIT")) {
						wait(tfTemp);
					} else {
						processInput(tfTemp);
					}

					// Thread.sleep(1000);
				}
			} catch (FoundNonExpectedValueException ex) {
				throw new FoundNonExpectedValueException(ex.getMessage());
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());
			}
		}
	}

	private void browserInput(TestField tfTemp) {
		String strType = tfTemp.getType();
		if (strType.equalsIgnoreCase("Browser Close Tabs")) {
			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(originalHandle)) {
					driver.switchTo().window(handle);
					driver.close();
				}
			}
			driver.switchTo().window(originalHandle);
		} else if (strType.equalsIgnoreCase("Browser Back")) {
			driver.navigate().back();
		}
	}

	private void rememberValue(TestField tfTemp) {
		String strXPath = tfTemp.getXPath();
		String strValue = tfTemp.getValue();

		if (strXPath != null && !strXPath.equals("")) {
			String key = strValue.toString();
			String value = findElementByXPath(strXPath).getAttribute("value");
			rememberMap.put(key, value);
		}
	}

	private void processDocumentEditor(TestField tfTemp) {
		String strXPath = tfTemp.getXPath();
		String strValue = tfTemp.getValue();

		if (strXPath != null && !strXPath.equals("")) {
			WebElement iframe = findElementByXPath(strXPath);
			driver.switchTo().frame(iframe);

			WebElement description = findElementByCssSelector("body");
			description.clear();
			description.sendKeys(strValue);

			driver.switchTo().defaultContent();
		}
	}

	private WebElement findElementByCssSelector(String selector) {
		return driver.findElement(By.cssSelector(selector));
	}

	private void wait(TestField tfTemp) {
		String strId = tfTemp.getID();
		String strType = tfTemp.getType();

		if (strType.equalsIgnoreCase("Wait ID")) {
			if (strId != null && !strId.equals("")) {
				(new WebDriverWait(driver, waitTimeForResponse.getWaitTime()))
						.until(ExpectedConditions.presenceOfElementLocated(By.id(strId)));

				System.out.println("WAIT TIME " + waitTimeForResponse.getWaitTime());
			}

			String strName = tfTemp.getName();
			if (strName != null && !strName.equals("")) {
				(new WebDriverWait(driver, waitTimeForResponse.getWaitTime()))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(strName)));
				System.out.println("WAIT TIME " + waitTimeForResponse.getWaitTime());
			}
		} else if (strType.equalsIgnoreCase("Wait Time")) {
			try {
				Thread.sleep(waitTimeForResponse.getSleepTime());
				System.out.println("Sleep TIME " + waitTimeForResponse.getSleepTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/****
	 * Create a screenshot
	 * 
	 * @param tfTemp
	 */
	private void makeScreenshot(TestField tfTemp) {
		String strValue = tfTemp.getValue();

		if (strValue != null) { // value is not mandatory

			if (strValue.equalsIgnoreCase("[Yes]")) { // Yes, I do!
				utils.takeScreenshot(driver, null);
			} else if (strValue.equalsIgnoreCase("[No]")) { // No, I won't!
				// do nothing
			} else if (strValue.equalsIgnoreCase("")) { // No, I won't!
				// do nothing
			} else {
				// assume it is a location
				utils.takeScreenshot(driver, strValue);
			}
		}
	}

	/***
	 * Checking the contents of fields
	 * 
	 * @param strFieldName
	 * @param strValue
	 * @throws Exception
	 */
	public void checkInput(TestField tfTemp) throws Exception {

		WebElement element = null;
		String strValueToCheck = null;
		Boolean blnOK = false;
		String strType = tfTemp.getType();
		String strXPath = tfTemp.getXPath();
		String strName = tfTemp.getName();
		String strFieldName = tfTemp.getFieldName();
		String strID = tfTemp.getID();
		String strValue = tfTemp.getValue();

		try {
			/*******************
			 * INPUT
			 *******************/
			if (strType.equalsIgnoreCase("Check Input")) {
				if (strID != null && !strID.equals("")) {
					element = findElementById(strID);
				} else {
					element = findElementByName(strName);
				}
				strValueToCheck = element.getAttribute("value");
			}
			/*******************
			 * SELECT
			 *******************/
			else if ((strType).equalsIgnoreCase("Check Select")) {

				Select selectBox = null;

				if (strID != null && !strID.equals("")) {
					selectBox = new Select(findElementById(strID));
				} else {
					selectBox = new Select(findElementByName(strName));
				}

				element = selectBox.getFirstSelectedOption();

				strValueToCheck = element.getText();

			}
			/*******************
			 * CHECK
			 *******************/
			else if (strType.equalsIgnoreCase("Check Check")) {
				if (strID != null && !strID.equals("")) {
					element = findElementById(strID);
				} else {
					element = findElementByName(strName);
				}

				if (element.isSelected()) {
					strValueToCheck = "[CHECKED]";
				} else {
					strValueToCheck = "[UNCHECKED]";
				}
			}
			/*******************
			 * TABLE
			 *******************/
			else if (strType.equalsIgnoreCase("Check Table")) {
				element = findElementByXPath(strXPath);
				strValueToCheck = element.getText().trim();
			}
			/*******************
			 * TABLE CONTAINS
			 *******************/
			else if (strType.equalsIgnoreCase("Check TableContains")) {
				element = findElementByXPath(strXPath);
				strValueToCheck = element.getText().trim();
				blnOK = strValueToCheck.contains(strValue);
			}
			/*******************
			 * Alt Image
			 *******************/
			else if (strType.equalsIgnoreCase("Check AltImage")) {
				element = findElementByXPath(strXPath);
				strValueToCheck = element.getAttribute("alt");
			}
			/*******************
			 * Class
			 *******************/
			else if (strType.equalsIgnoreCase("Check Class")) {
				element = findElementByXPath(strXPath);
				strValueToCheck = element.getAttribute("class").trim();
			}
			/***********************************
			 * Controlling / Checking
			 ****************/
			if (blnOK != true) {
				if (strValue == null) {
					// nothing to do
					blnOK = null;
				} else if (strValue.equalsIgnoreCase("[EMPTY]")) {
					if (strValueToCheck.equals("")) {
						// ok
						blnOK = true;
					}
				} else if (strValue.equals(strValueToCheck)) {
					blnOK = true;
				}
			}

			/***********************************
			 * Reporting
			 ****************/
			if (blnOK == null) {
				// nothing to report
			} else if (blnOK) {
				log.info("The value of '" + strFieldName + "' is as expected!");
			} else {
				utils.takeScreenshot(driver, null); // could come in handy
				String errorMessage = ("The value of '" + strFieldName + "' is NOT OK!" + " Expected: '" + strValue
						+ "'" + " Found: '" + strValueToCheck + "'");
				writeToLog(errorMessage);
				throw new FoundNonExpectedValueException(errorMessage);
			}
		} catch (NoSuchElementException nseex) {
			utils.takeScreenshot(driver, null);
			String errorMessage = ("The field \'" + strFieldName + "\' on screen \'" + tfTemp.getScreen()
					+ "\' cannot be found!");
			writeToLog(errorMessage);
			throw new NoSuchElementException(errorMessage);
		} catch (FoundNonExpectedValueException ex) {
			throw new FoundNonExpectedValueException(ex.getMessage());
		} catch (Exception ex) {
			writeToLog(ex.getMessage());
			throw new Exception(ex.getMessage());
		}
	}

	private WebElement findElementByXPath(String xpath) {
		return driver.findElement(By.xpath(xpath));
	}
	

	private WebElement findElementByName(String name) {
		return driver.findElement(By.name(name));
	}

	private WebElement findElementById(String strID) {
		return driver.findElement(By.id(strID));		 
	}

	private void writeToLog(String errorMessage) {
		log.error(errorMessage);
		testResultsLog.error(errorMessage);
	}
	
	private WebElement findElementByXPathWait(String xpath) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}

	/***
	 * Fill in the fields in the application
	 * 
	 * @param csdbf
	 * @throws Exception
	 */
	public void processInput(TestField tfTemp) throws Exception {

		WebElement element = null;
		String strValue = tfTemp.getValue();
		String strType = tfTemp.getType();
		String strXPath = tfTemp.getXPath();
		String strName = tfTemp.getName();
		String strText = tfTemp.getText();
		String strID = tfTemp.getID();
		String strFieldName = tfTemp.getFieldName();

		FieldController fc = new FieldController(driver);

		try {
			/*******************
			 * INPUT
			 *******************/
			if (strType.equalsIgnoreCase("Input")) {

				if (strValue != null && !strValue.equals("")) {

					if (strID != null && !strID.equals("")) {
						while (ExpectedConditions.presenceOfElementLocated(By.id(strID)) == null) {
							// ...
						}
						element = findElementById(strID);
					} else if (strText != null && !strText.equals("")) {
						while (ExpectedConditions.presenceOfElementLocated(By.partialLinkText(strText)) == null) {
							// ...
						}
						element = findElementByPartialLinkText(strText);
					} else if (strXPath != null && !strXPath.equals("")) {
						while (ExpectedConditions.presenceOfElementLocated(By.xpath(strXPath)) == null) {
							// ...
						}
						
						//element = findElementByXPath(strXPath);
						element = findElementByXPathWait(strXPath);
					} else {
						while (ExpectedConditions.presenceOfElementLocated(By.name(strName)) == null) {
							// ...
						}
						element = findElementByName(strName);
					}

					if (!element.isDisplayed()) {
						fc.scrollToElement(element);
					}

					if (element.isDisplayed()) {
						if (element.isEnabled()) {
							element.clear();

							// get remembered value from map
							
							strValue = strValue.trim();
							if (strValue.startsWith("{") && strValue.endsWith("}")) {
								strValue = rememberMap.get(strValue.substring(1, strValue.length() - 1));
							}

							// if a value is entered, filled it in
							if (!strValue.equalsIgnoreCase("[empty]")) { // unless
																			// it
																			// is
																			// needed
																			// to
																			// be
																			// empty
								element.sendKeys(strValue);
								Thread.sleep(500);
							}
						} else {
							log.error("Field is not enabled: " + strID);
						}
					} else {
						log.error("Field is not displayed: " + strID);
					}
				}
			}
			/*******************
			 * LINK
			 *******************/
			else if (strType.equalsIgnoreCase("Link") || strType.equalsIgnoreCase("Link no wait")) {
				if (strValue != null) {
					if (strXPath != null && !strXPath.equals("")) {
						element = findElementByXPath(strXPath);
					} else if (strText != null && !strText.equals("")) {
						log.info(strText);
						element = findElementByPartialLinkText(strText);
					} else {
						element = findElementByPartialLinkText(strValue);
					}

					// Wait if the link is not displayed
					while (!element.isDisplayed()) {
						log.info("Link element not found!");
					}
					while (!element.isEnabled()) { // not so neat, but is works
						// just wait
					}
					element.click();
					Thread.sleep(1000);

					log.info("Clicked link: " + strValue);
				}
			}
			/*******************
			 * SELECT
			 *******************/
			else if (strType.equalsIgnoreCase("Select") || strType.equalsIgnoreCase("Select Value")) {

				try {
					fc.SelectInput(tfTemp);
				} catch (Exception ex) {
					// catch the exception and throw it up.
					throw new Exception(ex.getMessage());
				}
			}
			/*******************
			 * CHECK
			 *******************/
			else if (strType.equalsIgnoreCase("Input Check")) {
				// DONE: First check if the element is selected already!

				if (strID == null || strID.equals("")) {
					element = findElementByName(strXPath);
				} else {
					element = findElementById(strID);
				}

				if (element.isSelected()) {
					if (strValue.equalsIgnoreCase("[UNCHECKED]")) {
						// uncheck
						element.click();
					}
				} else {
					if (strValue.equalsIgnoreCase("[Checked]")) {
						element.click();
					}
				}
			}
			/*******************
			 * BUTTON
			 *******************/
			else if (strType.equalsIgnoreCase("Button") || strType.equalsIgnoreCase("Button no wait")) {
				// Use Button no wait for popups
				if (strXPath != null && !strXPath.equals("")) {
					Thread.sleep(1000);
					element = findElementByXPath(strXPath);
				} else if (strName != null && !strName.equals("")) {
					
					element = findElementByName(strName);
					
					
				} else if (strID != null && !strID.equals("")) {
					element = findElementById(strID);
				}

				while (!element.isDisplayed()) {
					log.error("Not found Element for" + strID + element);
				}
				
				element.click();
				Thread.sleep(1000);
				log.info("clicked button: " + strFieldName);

				// wait if needed
				fc.waitForElement(tfTemp, "loading_display");

				log.info("Clicked on button: " + strValue);
			}
			/*******************
			 * CLICK
			 *******************/
			else if (strType.equalsIgnoreCase("CLICK")) {
				// Correct value
				if (strValue.equalsIgnoreCase("[YES]")) {

					// XPATH
					if (!strXPath.equals("")) {
						// Check if the element can be loaded
						while (ExpectedConditions.presenceOfElementLocated(By.xpath(strXPath)) == null) {
							// ...
						}

						element = findElementByXPath(strXPath);
					}

					// on screen
					while (!element.isDisplayed()) {
						fc.scrollToElement(element);
					}

					element.click();
				} else {
					log.error("Wrong value " + strValue + " found for type CLICK in field " + strFieldName);
				}
			}

			/*******************
			 * RADIO
			 *******************/
			else if (strType.equalsIgnoreCase("RADIO")) {
				if (strName != null && !strName.equals("")) {
					element = findElementByCssSelector(
							"input[name='" + strName + "'][value='" + strValue + "'][type='radio']");
				} else if (strID != null && !strID.equals("")) {
					element = findElementByCssSelector(
							"input[id='" + strID + "'][value='" + strValue + "'][type='radio']");
				}
				element.click();
			}
			/*******************
			 * EDITOR
			 *******************/
			else if (strType.equalsIgnoreCase("EDITOR")) {
				processDocumentEditor(tfTemp);
			}
			/*******************
			 * DATEINPUT
			 *******************/
			else if (strType.equalsIgnoreCase("DateInput")) {
				if (strValue != null && !strValue.equals("")) {
					// click calendar icon
					if (strXPath != null && !strXPath.equals("")) {
						element = findElementByXPath(strXPath);
					} else if (strName != null && !strName.equals("")) {
						element = findElementByName(strName);
					}
					element.click();

					String parentWindowHandler = switchToPopUp();

					DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					Calendar date = Calendar.getInstance();
					date.setTime(df.parse(strValue));
					Calendar now = Calendar.getInstance();

					pickYear(date, now);
					pickMonth(date, now);
					pickDayOfMonth(date);

					driver.switchTo().window(parentWindowHandler);
				}

			}
			/*******************
			 * Assert
			 *******************/
			else if (strType.equalsIgnoreCase("Assert")) {
				
				if (strXPath != null && !strXPath.equals("")) {
					element = findElementByXPath(strXPath);
				} else if (strName != null && !strName.equals("")) {
					element = findElementByName(strName);
				} else if (strID != null && !strID.equals("")) {
					element = findElementById(strID);
				}

				while (!element.isDisplayed()) {
					log.error("Not found Element for" + strID + element);
				}
				
				Assert.assertTrue(element.isDisplayed());
				Thread.sleep(1000);
				log.info("Asserting match: " + strFieldName);

				// wait if needed
				fc.waitForElement(tfTemp, "loading_display");

				log.info("Match asserting value: " + strValue);
			}
			
			/*******************
			 * BACKGROUND COLOR
			 * TODO: Code In Progress
			 * @Date: 06-07-2018 
			 * 
			 *******************/
			else if (strType.equalsIgnoreCase("Color")) {
				
				if (strXPath != null && !strXPath.equals("")) {
					element = findElementByXPath(strXPath);
				} else if (strName != null && !strName.equals("")) {
					element = findElementByName(strName);
				} else if (strID != null && !strID.equals("")) {
					element = findElementById(strID);
				}

				while (!element.isDisplayed()) {
					log.error("Not found Element for" + strID + element);
				}
				
				String strColor = element.getCssValue("background-color");
				String strhex = Color.fromString(strColor).asHex();
				Integer i = Integer.parseInt(strhex.substring(1),16);
				String strColorName = clutils.getColorNameFromHex(i);
				Thread.sleep(1000);
				testResultsLog.info("Background color: " + strColorName + "Filed Name: "  + tfTemp.strValue.toString());

				// wait if needed
				fc.waitForElement(tfTemp, "loading_display");
			}
			
			/*******************
			 * SCREENSHOT
			 *******************/
			else if (strType.equalsIgnoreCase("SCREENSHOT")) {
				makeScreenshot(tfTemp);
			}
			/*******************
			 * TESTCASE
			 *******************/
			else if (strType.equalsIgnoreCase("TESTCASE")) {
				log.debug("Starting testcase: " + strValue);
			}
		}

		/******************
		 * Error handling *
		 ******************/
		catch (NoSuchElementException nseex) {
			utils.takeScreenshot(driver, null);
			log.error("The field \'" + strFieldName + "\' (" + strType + ") cannot be found!");
			throw new Exception(nseex.getMessage());
		} catch (Exception ex) {
			utils.takeScreenshot(driver, null);
			log.error("Problem with " + strFieldName + " with value " + strValue);
			log.error(ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		}
	}

	private WebElement findElementByPartialLinkText(String strText) {
		return driver.findElement(By.partialLinkText(strText));
	}

	private void pickYear(Calendar date, Calendar now) {
		int yearsDifference = date.get(Calendar.YEAR) - now.get(Calendar.YEAR);
		if (yearsDifference > 0) {
			WebElement linkNextYear = findElementByClassName("dp-nav-next-year");
			while (yearsDifference > 0) {
				linkNextYear.click();
				yearsDifference--;
			}
		} else if (yearsDifference < 0) {
			WebElement linkPreviousYear = findElementByClassName("dp-nav-prev-year");
			while (yearsDifference < 0) {
				linkPreviousYear.click();
				yearsDifference++;
			}
		}
	}

	private WebElement findElementByClassName(String className) {
		return driver.findElement(By.className(className));
	}

	private void pickMonth(Calendar date, Calendar now) {
		int monthsDifference = date.get(Calendar.MONTH) - now.get(Calendar.MONTH);
		if (monthsDifference > 0) {
			WebElement linkNextMonth = findElementByClassName("dp-nav-next-month");
			while (monthsDifference > 0) {
				linkNextMonth.click();
				monthsDifference--;
			}
		} else if (monthsDifference < 0) {
			WebElement linkPreviousMonth = findElementByClassName("dp-nav-prev-month");
			while (monthsDifference < 0) {
				linkPreviousMonth.click();
				monthsDifference++;
			}
		}
	}

	private void pickDayOfMonth(Calendar cal) {
		// application specific, find calendar div
		List<WebElement> columns = driver.findElements(
				By.xpath("//*[@id='dp-popup']/div[3]/table/tbody/tr/td[contains(@class,'current-month')]"));
		// click number of day in calendar
		for (WebElement cell : columns) {
			if (cell.getText().equals(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)))) {
				cell.click();
				break;
			}
		}
	}

	private String switchToPopUp() {
		String parentWindowHandler = driver.getWindowHandle(); // Store your
																// parent window
		String subWindowHandler = null;

		java.util.Set<String> handles = driver.getWindowHandles(); // get all
																	// window
																	// handles
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()) {
			subWindowHandler = iterator.next();
		}
		driver.switchTo().window(subWindowHandler); // switch to popup window
													// perform operations on
													// popup
		return parentWindowHandler;
	}

}
