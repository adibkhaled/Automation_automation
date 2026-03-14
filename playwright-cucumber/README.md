# Playwright TypeScript Cucumber Testing Framework

This project is a TypeScript conversion of the Selenium Cucumber automation framework, using Playwright instead of Selenium WebDriver.

## Project Structure

```
playwright-cucumber/
├── src/
│   ├── pages/                 # Page Object Models
│   │   ├── PageBase.ts        # Base class for all pages
│   │   ├── HomePage.ts        # Home page object
│   │   ├── SearchPanel.ts     # Search panel component
│   │   ├── SearchResultPage.ts # Search results page
│   │   └── index.ts           # Export file
│   ├── steps/                 # Cucumber step definitions
│   │   └── search.steps.ts    # Search feature steps
│   └── utils/                 # Utilities
│       ├── BrowserManager.ts  # Browser singleton manager
│       ├── TestUtils.ts       # Common test utilities
│       └── index.ts           # Export file
├── features/                  # Gherkin feature files
│   ├── search.feature         # Search functionality tests
│   └── invalid_search.feature # Invalid search tests
├── cucumber.js               # Cucumber configuration
├── package.json              # NPM dependencies
└── tsconfig.json            # TypeScript configuration
```

## Key Features

### 1. **Page Object Model (POM)**
   - `PageBase.ts`: Base class with common page operations
   - `HomePage.ts`: Home page object with search panel
   - `SearchPanel.ts`: Reusable component for search functionality
   - `SearchResultPage.ts`: Search results page object

### 2. **Browser Management**
   - Singleton pattern for browser instance management
   - Support for Chrome, Firefox, and WebKit
   - Automatic browser and context management
   - Headless/headed mode support

### 3. **BDD with Cucumber**
   - Feature files with Gherkin syntax
   - Step definitions matching your Selenium steps
   - Same test scenarios converted to Playwright

### 4. **Equivalence with Selenium Code**

| Selenium (Java) | Playwright (TypeScript) |
|---|---|
| `Browser.getInstance(browserName)` | `BrowserManager.getInstance().getPage(browserName)` |
| `HomePage extends PageBase` | `HomePage extends PageBase` |
| `PageFactory.initElements()` | Playwright locators automatically initialized |
| `@FindBy` annotations | Direct `page.locator()` calls |
| `WebDriver` | `Page` from Playwright |
| `@Given/@When/@Then` | `Given/When/Then` from @cucumber/cucumber |

## Installation

### Prerequisites
- Node.js 18+ 
- npm or yarn

### Setup Steps

```bash
# Navigate to the project directory
cd playwright-cucumber

# Install dependencies
npm install

# The setup will automatically install:
# - @playwright/test
# - @cucumber/cucumber
# - typescript
# - Required type definitions
```

## Running Tests

### Run all tests
```bash
npm test
```

### Run specific browser
```bash
# Chrome (Chromium)
npm run test:chrome

# Firefox
npm run test:firefox

# WebKit (Safari)
npm run test:webkit
```

### Run with headed browser (visible window)
```bash
npm run test:headed
```

### Run with debug mode
```bash
npm run test:debug
```

## Feature Files

### Search Functionality (search.feature)
Tests basic website navigation and search functionality:
- Opens browser
- Navigates to page
- Verifies page title
- Performs search
- Validates results

### Invalid Search (invalid_search.feature)
Tests handling of invalid URLs

## Step Definitions

All step definitions are in `src/steps/search.steps.ts` and correspond directly to your Selenium step definitions:

```typescript
// Example: Opening browser
Given('I open {word} browser', async function(browserName: string) {
  // Same logic as Java: Browser.getInstance(browserName)
})

// Example: Navigation
When('I type {string} and press enter', async function(url: string) {
  // Same logic as Java: home.open(url)
})

// Example: Assertion
Then('I can see page loaded with title {string}', async function(expectedTitle: string) {
  // Same assertion as Java: Assert.assertEquals()
})
```

## Page Objects

### HomePage
```typescript
const homePage = new HomePage(page);
await homePage.open('https://devon.nl/');
await homePage.search.enterSearchText('search term');
const title = await homePage.getTitle();
```

### SearchResultPage
```typescript
const resultsPage = new SearchResultPage(page);
const messageText = await resultsPage.getInvalidSearchResultMessageText();
```

## Configuration

### Browser Options
Edit `src/utils/BrowserManager.ts`:
```typescript
private getBrowserOptions() {
  const isHeaded = process.env.HEADED === 'true';
  return {
    headless: !isHeaded,
    args: [...]
  };
}
```

### Context Options
Modify viewport, network conditions, geolocation, etc.:
```typescript
private getContextOptions() {
  return {
    viewport: { width: 1920, height: 1080 },
    ignoreHTTPSErrors: true
  };
}
```

## Cucumber Configuration

Edit `cucumber.js`:
```javascript
module.exports = {
  default: {
    requireModule: ['ts-node/register'],
    require: ['src/steps/**/*.ts'],
    format: [
      'progress-bar',
      'html:cucumber-report.html',
      'json:cucumber-report.json'
    ]
  }
};
```

## Reports

After running tests, reports are generated:
- **HTML Report**: `cucumber-report.html` (detailed test results)
- **JSON Report**: `cucumber-report.json` (machine-readable format)
- **JUnit Report**: `cucumber-report.xml` (CI/CD integration)

## Adding New Tests

### 1. Create Feature File (features/)
```gherkin
Feature: New Feature

  Scenario: New Test
    Given I open chrome browser
    When I do something
    Then I verify something
```

### 2. Create Page Object (src/pages/)
```typescript
export class NewPage extends PageBase {
  private elementLocator = this.page.locator('selector');
  
  async clickElement() {
    await this.elementLocator.click();
  }
}
```

### 3. Add Step Definitions (src/steps/)
```typescript
Given('step definition', async function() {
  // Implementation
});
```

## Troubleshooting

### Locators Not Found
- Ensure XPath/CSS selectors are correct
- Check website hasn't changed structure
- Use `npx playwright codegen https://devon.nl` to generate selectors

### Tests Timing Out
- Increase timeout in BrowserManager
- Wait for navigation: `page.waitForNavigation()`
- Use explicit waits for dynamic content

### Browser Won't Close
- Ensure `close()` is called in `After()` hook
- Check for unresolved promises

## Migration from Selenium Notes

✅ **What's the Same:**
- Feature files and scenarios
- Page Object Model pattern
- Step definition logic
- Test assertions

✅ **What's Different:**
- Async/await instead of synchronous calls
- Playwright locators instead of Selenium WebElements
- Different assertion library (Playwright's expect vs JUnit)
- Simpler browser/driver management

## Best Practices

1. **Always use Page Objects** - Keep pages separate from steps
2. **Wait Properly** - Use Playwright's auto-waiting where possible
3. **Clean Up** - Ensure After hooks close browsers
4. **Meaningful Step Names** - Match Gherkin keywords
5. **Separate Concerns** - One step = one logical action

## Resources

- [Playwright Documentation](https://playwright.dev/)
- [Cucumber.js Documentation](https://cucumber.io/docs/cucumber/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)

## License

MIT

## Author

Converted from Selenium Cucumber Framework to Playwright TypeScript
