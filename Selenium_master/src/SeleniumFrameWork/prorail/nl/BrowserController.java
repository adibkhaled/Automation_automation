package SeleniumFrameWork.prorail.nl;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.relevantcodes.extentreports.ExtentReports;

import org.openqa.selenium.chrome.ChromeDriver;
import SeleniumFrameWork.prorail.nl.config.*;



public class BrowserController {
	
	static Logger log = Logger.getLogger(BrowserController.class.getName());
	public Configuration conConfig;
	
	/***
	 * Empty constructor
	 */
	public BrowserController() {
		conConfig = Configuration.getConfigurationInstance();
	}
	
	/***
	 * Function that loads the browser. 
	 */
	public WebDriver loadBrowser(String strBrowser) {
		
		WebDriver driver = null;
			
		// This code: if driver can not read configuration file.
		//System.setProperty("webdriver.gecko.driver", "V:\\werkmap\\Adib\\Selenium_project\\geckodriver\\geckodriver.exe");
		
		System.setProperty(conConfig.getProperty("browserDriver"), conConfig.getProperty("geckoDriverUrl")); // This code for reading driver information using configuration file.


		if(strBrowser.equals("FireFox")) {
			// Start FireFox
		    log.info("Configuring firefox");


/*	
 * This is legecy code for windows configuration.  	    
 * 
//		    //Windows config
//		    capabilities.setCapability("binary", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
//		    
//		    //Linux config
//		    //capabilities.setCapability("binary", "/usr/bin/firefox");
//	        
//            //setting the profile
//		    FirefoxProfile profile = new FirefoxProfile();
//		    profile.setEnableNativeEvents(true);
//		    profile.setPreference( "intl.accept_languages", "no,en-us,en" ); 
//		    profile.setPreference( "privacy.clearOnShutdown.cookies", true );
//		    profile.setPreference( "privacy.clearOnShutdown.formdata", true );
//		    profile.setPreference( "privacy.donottrackheader.enabled", true );
//		    profile.setPreference( "browser.tabs.tabMaxWidth", 100 );
//		    profile.setPreference( "browser.cache.disk.capacity", 10240 );
//		    profile.setPreference( "browser.link.open_newwindow.override.external", 2);
//		    capabilities.setCapability(FirefoxDriver.PROFILE, profile);
*/		    
		    driver = new FirefoxDriver();
		    log.info("Starting firefox");   
		    driver.manage().timeouts().implicitlyWait(Long.parseLong(conConfig.getProperty("Implicit wait")), TimeUnit.SECONDS);
		    log.info("Firefox loaded.");

		}
		else if(strBrowser.equals("Chrome")) {
			log.info("Configuring Chrome");
			System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");

			log.info("Starting Chrome");
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(Long.parseLong(conConfig.getProperty("Implicit wait")), TimeUnit.SECONDS);
		    log.info("Chrome loaded.");
		}

		else if (strBrowser.equals("Geko")){
			log.info("Starting Geko");
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(Long.parseLong(conConfig.getProperty("Implicit wait")), TimeUnit.SECONDS);
			log.info("Geko loaded.");
		}
		
		return driver;
	}
}
