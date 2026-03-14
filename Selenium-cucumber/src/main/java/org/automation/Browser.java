package org.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Browser {
    private static WebDriver driver = null;
    private static String os = System.getProperty("os.name");

    public static WebDriver getInstance() {
        if (driver == null) {
            driver = getABrowser("chrome");
        }
        return driver;
    }

    public static WebDriver getInstance(String browserName) {
        if (driver == null) {
            driver = getABrowser(browserName);
        }
        return driver;
    }

    private Browser() {
    }
    private static WebDriver getABrowser(String nameOfBrowser){
        switch (nameOfBrowser){
            case  "chrome" : {
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(getChromeOptions());
            }
            case  "firefox" : {
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            }
            default: {
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(getChromeOptions());
            }
        }
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        // Disable notifications and popups
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        // Disable extensions
        options.addArguments("--disable-extensions");
        // Start maximized
        options.addArguments("start-maximized");
        // Disable info bars
        options.addArguments("disable-infobars");
        return options;
    }

    public static void close() {
        driver.close();
        driver = null;// to avoid closing time of browser by JVM
    }
}