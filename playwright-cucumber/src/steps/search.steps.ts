import { Given, When, Then, Before, After } from '@cucumber/cucumber';
import { expect, Page } from '@playwright/test';
import { BrowserManager } from '../utils/BrowserManager';
import { HomePage } from '../pages/HomePage';
import { SearchResultPage } from '../pages/SearchResultPage';

/**
 * Step Definitions for Search Feature
 * Corresponds to SearchStepDefination.java
 */

let page: Page;
let browserManager: BrowserManager;
let homePage: HomePage;
let searchResultPage: SearchResultPage;
let startTime: number;
let endTime: number;

/**
 * Hook - Before each scenario
 */
Before(async function() {
  console.log('Starting new test scenario...');
  startTime = Date.now();
});

/**
 * Hook - After each scenario
 */
After(async function() {
  console.log('Closing browser after scenario...');
  if (browserManager) {
    await browserManager.closeBrowser();
  }
});

/**
 * Step: Open browser
 * Gherkin: Given I open <browserName> browser
 * Java equivalent: @Given("^I open (.+) browser$")
 */
Given('I open {word} browser', async function(browserName: string) {
  browserManager = BrowserManager.getInstance();
  page = await browserManager.getPage(browserName);
  console.log(`Opening ${browserName} browser`);
});

/**
 * Step: Navigate to URL
 * Gherkin: When I type <url> and press enter
 * Java equivalent: @When("^I type (.+) and press enter$")
 */
When('I type {string} and press enter', async function(url: string) {
  homePage = new HomePage(page);
  startTime = Date.now();
  try {
    await homePage.open(url);
    console.log(`Navigated to ${url}`);
  } catch (error) {
    console.log(`Navigation attempt to ${url}: ${error}`);
    // Continue even if double slash URL fails navigation
  }
});

/**
 * Step: Verify page title
 * Gherkin: Then I can see page loaded with title "<expectedTitle>"
 * Java equivalent: @Then("^I can see page loaded with title \"([^\"]*)\"$")
 */
Then('I can see page loaded with title {string}', async function(expectedTitle: string) {
  const actualTitle = await homePage.getTitle();
  expect(actualTitle).toBe(expectedTitle);
  console.log(`Page title verified: ${actualTitle}`);
});

/**
 * Step: Enter search text and click search
 * Gherkin: And I type "<searchTerm>" in search box And I click search button
 * Java equivalent: @And("^I type \"([^\"]*)\" in search box And I click search button$")
 */
When('I type {string} in search box And I click search button', async function(searchTerm: string) {
  try {
    console.log(`Attempting to search for: ${searchTerm}`);
    
    // Wait a moment for any animations to complete
    await page.waitForTimeout(500);
    
    // Try to click the search area to activate it first
    const searchArea = page.locator('input[id*="search"], input[name="s"], .mk-fullscreen-search-wrapper');
    
    // Make sure element is visible and interact with it
    await homePage.search.enterSearchText(searchTerm);
    console.log(`Entered search term: ${searchTerm}`);
    
    await page.waitForTimeout(300); // Let animations settle
    
    await homePage.search.clickSearchButton();
    console.log('Clicked search button');
    
    // Wait for results page to load
    await page.waitForNavigation({ waitUntil: 'load' }).catch(() => {
      console.log('Page navigation occurred or already loaded');
    });
    
    searchResultPage = new SearchResultPage(page);
  } catch (error) {
    console.log(`Search error: ${error}`);
    throw error;
  }
});

/**
 * Step: Verify invalid search results
 * Gherkin: Then I can see invalid search title with "<title>" and message "<message>"
 * Java equivalent: @Then("^I can see invalid search title with \"([^\"]*)\" and message \"([^\"]*)\"$")
 */
Then('I can see invalid search title with {string} and message {string}', async function(expectedTitle: string, expectedMessage: string) {
  const actualTitle = await searchResultPage.getTitle();
  expect(actualTitle).toBe(expectedTitle);
  
  const actualMessage = await searchResultPage.getInvalidSearchResultMessageText();
  expect(actualMessage).toContain(expectedMessage);
  
  endTime = Date.now();
  console.log(`Search results verified - Title: ${actualTitle}, Message: ${actualMessage}`);
});

/**
 * Step: Verify search results
 * Gherkin: Then I can see search results with title "<expectedTitle>"
 * Java equivalent: @Then("^I can see search results with title \"([^\"]*)\"$")
 */
Then('I can see search results with title {string}', async function(expectedTitle: string) {
  const actualTitle = await searchResultPage.getTitle();
  expect(actualTitle).toBe(expectedTitle);
  
  endTime = Date.now();
  console.log(`Search results verified - Title: ${actualTitle}`);
});

/**
 * Step: Verify search time performance
 * Gherkin: And I can see the search should not take more than <seconds> second
 * Java equivalent: @And("^I can see the search should not take more than (\\d+) second$")
 */
Then('I can see the search should not take more than {int} second', async function(maxSeconds: number) {
  const timeTaken = endTime - startTime;
  const maxTime = maxSeconds * 1000;
  
  expect(timeTaken).toBeLessThan(maxTime);
  console.log(`Search completed in ${timeTaken}ms (max: ${maxTime}ms)`);
});

/**
 * Step: Close browser
 * Gherkin: Then I quit browser
 * Java equivalent: @Then("^I quit browser$")
 */
Then('I quit browser', async function() {
  if (browserManager) {
    await browserManager.closeBrowser();
    console.log('Browser closed');
  }
});
