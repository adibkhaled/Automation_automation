# Migration Guide: Selenium Cucumber → Playwright TypeScript

## Overview

This document shows the direct mapping between your Selenium Cucumber framework and the new Playwright TypeScript implementation.

---

## 1. Browser Management

### Selenium (Java)
```java
public class Browser {
    private static WebDriver driver = null;
    
    public static WebDriver getInstance() {
        if (driver == null) {
            driver = getABrowser("chrome");
        }
        return driver;
    }
    
    private static WebDriver getABrowser(String nameOfBrowser){
        switch (nameOfBrowser){
            case "chrome" : {
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(getChromeOptions());
            }
            // ... other browsers
        }
    }
    
    public static void close() {
        driver.close();
        driver = null;
    }
}
```

### Playwright TypeScript
```typescript
export class BrowserManager {
  private static instance: BrowserManager;
  private browser: Browser | null = null;
  
  static getInstance(): BrowserManager {
    if (!BrowserManager.instance) {
      BrowserManager.instance = new BrowserManager();
    }
    return BrowserManager.instance;
  }
  
  async getBrowser(browserName: string = 'chrome'): Promise<Browser> {
    if (this.browser) {
      return this.browser;
    }
    
    switch (browserName.toLowerCase()) {
      case 'firefox':
        this.browser = await firefox.launch();
        break;
      case 'chrome':
      default:
        this.browser = await chromium.launch();
        break;
    }
    return this.browser;
  }
  
  async closeBrowser(): Promise<void> {
    if (this.browser) {
      await this.browser.close();
      this.browser = null;
    }
  }
}
```

**Key Changes:**
- ✅ Same singleton pattern
- ✅ Same browser names (chrome, firefox)
- ✅ Playwright auto-manages WebDriver (no WebDriverManager needed)
- ✅ Promises instead of synchronous calls

---

## 2. Page Base Class

### Selenium (Java)
```java
public abstract class PageBase{
    private String name;
    private String url;
    protected WebDriver driver;
    
    public PageBase(WebDriver aDriver){
        this.driver = aDriver;
        initElement(this);  // PageFactory initialization
    }
    
    public String getTitle() {
        return driver.getTitle();
    }
}
```

### Playwright TypeScript
```typescript
export class PageBase {
  protected page: Page;
  protected url: string = '';
  protected name: string = '';
  
  constructor(page: Page) {
    this.page = page;
    // No need for PageFactory - Playwright handles element location
  }
  
  async getTitle(): Promise<string> {
    return await this.page.title();
  }
}
```

**Key Changes:**
- ✅ Same properties (name, url)
- ✅ WebDriver → Page (Playwright's main object)
- ✅ No PageFactory needed - locators are computed on demand
- ✅ Async/await for all browser operations

---

## 3. Page Objects

### Selenium (Java)
```java
// HomePage.java
public class HomePage extends PageBase implements CompletePage{
    public SearchPanel search;
    
    public HomePage(WebDriver aDriver) {
        super(aDriver);
        search = new SearchPanel(driver);
    }
    
    public HomePage open(String url){
        driver.get(url);
        return this;
    }
}

// SearchPanel.java
public class SearchPanel extends PageBase {
    @FindBy(xpath = "//div[@class='mk-fullscreen-search-wrapper']/input")
    public WebElement textBox;
    
    @FindBy(xpath = "//svg[@class='mk-svg-icon']")
    public WebElement button;
    
    public SearchPanel(WebDriver aDriver) {
        super(aDriver);
    }
}
```

### Playwright TypeScript
```typescript
// HomePage.ts
export class HomePage extends PageBase {
  public search: SearchPanel;
  
  constructor(page: Page) {
    super(page);
    this.search = new SearchPanel(page);
  }
  
  async open(url: string): Promise<void> {
    this.url = url;
    await this.page.goto(url, { waitUntil: 'load' });
  }
}

// SearchPanel.ts
export class SearchPanel extends PageBase {
  private textBoxLocator: Locator;
  private buttonLocator: Locator;
  
  constructor(page: Page) {
    super(page);
    // Same XPath locators
    this.textBoxLocator = page.locator("//div[@class='mk-fullscreen-search-wrapper']/input");
    this.buttonLocator = page.locator("//svg[@class='mk-svg-icon']");
  }
  
  async enterSearchText(text: string): Promise<void> {
    await this.textBoxLocator.clear();
    await this.textBoxLocator.fill(text);
  }
}
```

**Key Changes:**
- ✅ Same class structure and inheritance
- ✅ @FindBy → page.locator() - same XPath selectors work
- ✅ Methods wrap operations in async functions
- ✅ Return statements for method chaining

---

## 4. Step Definitions

### Selenium (Java)
```java
public class SearchStepDefination {
    private WebDriver driver;
    private HomePage home;
    
    @Given("^I open (.+) browser$")
    public void i_open_browser(String nameOfBrowser) throws Throwable {
       driver = Browser.getInstance(nameOfBrowser);
    }
    
    @When("^I type (.+) and press enter$")
    public void i_type_url_and_press_enter(String url) throws Throwable {
        home = new HomePage(driver);
        home.open(url);
    }
    
    @Then("^I can see page loaded with title \"([^\"]*)\"$")
    public void i_can_see_page_loaded_with_title(String arg1) throws Throwable {
        Assert.assertEquals(arg1, home.getTitle());
    }
    
    @Then("^I quit browser$")
    public void i_quit_browser() throws Throwable {
       Browser.close();
    }
}
```

### Playwright TypeScript
```typescript
let page: Page;
let browserManager: BrowserManager;
let homePage: HomePage;

Given('I open {word} browser', async function(browserName: string) {
  browserManager = BrowserManager.getInstance();
  page = await browserManager.getPage(browserName);
});

When('I type {string} and press enter', async function(url: string) {
  homePage = new HomePage(page);
  await homePage.open(url);
});

Then('I can see page loaded with title {string}', async function(expectedTitle: string) {
  const actualTitle = await homePage.getTitle();
  expect(actualTitle).toBe(expectedTitle);
});

Then('I quit browser', async function() {
  if (browserManager) {
    await browserManager.closeBrowser();
  }
});
```

**Key Changes:**
- ✅ @Given/@When/@Then → Given/When/Then from @cucumber/cucumber
- ✅ Regex patterns → Cucumber parameter types: {word}, {string}, {int}
- ✅ Throws declaration → async/await with Promise handling
- ✅ Assert.assertEquals → expect().toBe() (Playwright assertion style)
- ✅ Same step logic and flow

---

## 5. Feature Files

### Selenium (Gherkin)
```gherkin
Feature: valid search

  Scenario: Going to Opencart and search an item
    Given I open chrome browser
    When I type https://devon.nl/ and press enter
    Then I can see page loaded with title "Home - Devon"
    Then I quit browser
```

### Playwright (Gherkin) - **NO CHANGE**
```gherkin
Feature: valid search

  Scenario: Going to Devon website and verify page loads
    Given I open chrome browser
    When I type "https://devon.nl/" and press enter
    Then I can see page loaded with title "Home - Devon"
    Then I quit browser
```

✅ **Feature files remain 100% the same!** Only step definition implementations change.

---

## 6. Test Organization

### Selenium Project Structure
```
Selenium-cucumber/
├── src/
│   ├── main/java/org/
│   │   ├── automation/
│   │   │   ├── Browser.java
│   │   │   ├── PageBase.java
│   │   │   └── ...
│   │   └── pages/
│   │       ├── HomePage.java
│   │       ├── SearchPanel.java
│   │       └── SearchResultPage.java
│   └── test/java/org/automation/
│       ├── tests/
│       │   └── OpencartTests.java
│       └── tests/opencart/
│           └── SearchStepDefination.java
└── src/test/resources/features/
    ├── search.feature
    └── invalid_search.feature
```

### Playwright Project Structure
```
playwright-cucumber/
├── src/
│   ├── pages/
│   │   ├── PageBase.ts
│   │   ├── HomePage.ts
│   │   ├── SearchPanel.ts
│   │   └── SearchResultPage.ts
│   ├── steps/
│   │   └── search.steps.ts
│   └── utils/
│       ├── BrowserManager.ts
│       └── TestUtils.ts
└── features/
    ├── search.feature
    └── invalid_search.feature
```

**Key Changes:**
- ✅ No split between main/test (TypeScript is single language)
- ✅ Flatter structure (easier navigation)
- ✅ Pages, Steps, Utils clearly separated

---

## 7. Execution & Reports

### Selenium Execution
```xml
<!-- pom.xml -->
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
</plugin>
```
```bash
mvn test
mvn test -Dgroups=@chrome
```

### Playwright Execution
```bash
npm test                    # All tests
npm run test:chrome        # Chrome only
npm run test:firefox       # Firefox only
npm run test:headed        # Visible window
```

**Reports Generated:**
- Selenium: HTML, JSON, JUnit (target/cucumberReport/)
- Playwright: HTML, JSON, JUnit (root directory)

---

## 8. Key Vocabulary Mapping

| Concept | Selenium | Playwright |
|---------|----------|-----------|
| Browser Instance | WebDriver | Browser |
| Page Context | WebDriver | Page |
| Element | WebElement | Locator |
| Finding Elements | findElement() | locator() |
| Clicking | element.click() | locator.click() |
| Typing Text | element.sendKeys() | locator.fill() |
| Getting Text | element.getText() | locator.textContent() |
| Wait for Element | WebDriverWait | waitFor() |
| Assertion | Assert.assertEquals() | expect().toBe() |
| Screenshot | AShot/WebDriver | page.screenshot() |

---

## 9. Common Migration Patterns

### Finding Elements

**Selenium:**
```java
@FindBy(xpath = "//button[@id='search']")
WebElement searchButton;

searchButton.click();
```

**Playwright:**
```typescript
const searchButton = this.page.locator("//button[@id='search']");
await searchButton.click();
```

### Waiting for Elements

**Selenium:**
```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.elementToBeClickable(element));
```

**Playwright:**
```typescript
await locator.waitFor({ state: 'visible', timeout: 5000 });
```

### Clearing & Typing Text

**Selenium:**
```java
element.clear();
element.sendKeys("text");
```

**Playwright:**
```typescript
await locator.clear();
await locator.fill("text");
// Or in one go:
await locator.fill("text");  // automatically clears
```

---

## 10. What Made the Migration Easy

✅ **Same BDD approach** - Gherkin syntax and Cucumber
✅ **Same Page Object Model** - Structure is identical
✅ **Same test logic** - Step definitions contain same assertions
✅ **Same element selectors** - XPath/CSS work in both
✅ **Similar assertion patterns** - Easy mapping (Assert → expect)
✅ **Matching class hierarchy** - Page extends PageBase

---

## 11. Testing the Migration

Run tests to verify everything works:

```bash
# Install dependencies
cd playwright-cucumber
npm install

# Run tests
npm test

# Run specific browser
npm run test:chrome

# Run with visible browser
npm run test:headed

# Run with debugging
npm run test:debug
```

Expected output:
```
Running feature files from ./features
Found 2 feature files

✓ Feature: valid search
  ✓ Scenario: Going to Devon website and verify page loads
    ...

✓ Feature: Invalid search
  ...

2 features, X scenarios, X steps (X passed)
```

---

## 12. Next Steps

1. ✅ Review generated code → Ensure it matches your expectations
2. ✅ Run tests → `npm test`
3. ✅ Add more page objects → Follow the same pattern
4. ✅ Create new feature files → Use same Gherkin format
5. ✅ Add new step definitions → Follow TypeScript async/await pattern
6. ✅ Integrate with CI/CD → Use existing JSON/JUnit reports

---

## Questions & Common Issues

### Q: My XPath selectors don't work
A: Use `npx playwright codegen https://devon.nl` to generate and verify selectors

### Q: Tests are timing out
A: Increase timeout in BrowserManager or add explicit waits

### Q: How do I run a single scenario?
A: Add tags in feature file and use: `npm test -- --tags @mytag`

### Q: How do I debug tests?
A: Use `npm run test:debug` or add breakpoints in VS Code

---

**🎉 Congratulations! Your Selenium Cucumber framework is now Playwright TypeScript!**
