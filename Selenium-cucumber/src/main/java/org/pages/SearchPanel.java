package org.pages;

import org.automation.PageBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by shantonu on 5/17/16.
 */
public class SearchPanel extends PageBase {

    @FindBy(xpath = "//div[@class='mk-fullscreen-search-wrapper']/input")
    public WebElement textBox ;

    @FindBy(xpath = "//svg[@class='mk-svg-icon']")//property loading or after parsing or static
    public WebElement button;

    public SearchPanel(WebDriver aDriver) {
        super(aDriver);
    }
}