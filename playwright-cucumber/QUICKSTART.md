# Quick Start Guide

Get your Playwright TypeScript tests running in 5 minutes!

## Step 1: Install Dependencies

```bash
# Navigate to project directory
cd playwright-cucumber

# Install all dependencies
npm install
```

This will install:
- **@playwright/test** - Playwright testing library
- **@cucumber/cucumber** - Cucumber BDD framework
- **typescript** - TypeScript compiler
- All required type definitions

**Installation time:** ~2-3 minutes (depending on internet speed)

## Step 2: Verify Installation

```bash
# Check that npm packages are installed
npm ls

# Should show:
# playwright-cucumber@1.0.0
# ├── @cucumber/cucumber@10.0.1
# ├── @playwright/test@1.48.0
# └── playwright@1.48.0
```

## Step 3: Run Your First Test

```bash
# Run all features
npm test
```

Expected output:
```
Feature: valid search
  Scenario: Going to Devon website and verify page loads  ✓
  Scenario: Search functionality test                     ✓

Feature: Invalid search
  Scenario: Checking invalid search                       ✓


2 features, 3 scenarios (3 passed)
```

## Step 4: Run Tests in Different Modes

```bash
# Chrome only (fastest)
npm run test:chrome

# Firefox
npm run test:firefox

# WebKit (Safari)
npm run test:webkit

# With visible browser window
npm run test:headed

# With debugging
npm run test:debug
```

## Step 5: View Test Reports

After running tests, check:

1. **HTML Report** (Detailed results with screenshots)
   ```
   open cucumber-report.html
   ```

2. **JSON Report** (Machine readable)
   ```
   cat cucumber-report.json
   ```

3. **JUnit Report** (CI/CD integration)
   ```
   cat cucumber-report.xml
   ```

## Project Overview

### Folder Structure

```
playwright-cucumber/
│
├── src/
│   ├── pages/              ← Page Objects
│   │   ├── HomePage.ts
│   │   ├── SearchPanel.ts
│   │   ├── SearchResultPage.ts
│   │   └── PageBase.ts
│   │
│   ├── steps/              ← Test Steps (Gherkin → Code)
│   │   └── search.steps.ts
│   │
│   └── utils/              ← Utilities
│       ├── BrowserManager.ts
│       └── TestUtils.ts
│
├── features/               ← Test Scenarios (Gherkin)
│   ├── search.feature
│   └── invalid_search.feature
│
├── package.json            ← Dependencies
├── tsconfig.json           ← TypeScript config
├── cucumber.js             ← Cucumber config
└── README.md               ← Full documentation
```

## How It Works

### 1. Feature Files (Gherkin)
```gherkin
Feature: valid search

  Scenario: Search something
    Given I open chrome browser
    When I type "https://devon.nl/" and press enter
    Then I can see page loaded with title "Home - Devon"
```

### 2. Step Definitions (TypeScript)
```typescript
Given('I open {word} browser', async function(browserName: string) {
  // Implementation here
});
```

### 3. Page Objects (TypeScript)
```typescript
export class HomePage extends PageBase {
  // Page elements and methods
}
```

## Common Tasks

### Add a New Test Scenario

1. **Edit feature file** (`features/search.feature`)
   ```gherkin
   Scenario: New test
     Given I open chrome browser
     When I do something
     Then I verify something
   ```

2. **Add step definition** (`src/steps/search.steps.ts`)
   ```typescript
   When('I do something', async function() {
     // Code here
   });
   ```

3. **Run test**
   ```bash
   npm test
   ```

### Add a New Page Object

1. **Create file** (`src/pages/NewPage.ts`)
   ```typescript
   import { Page, Locator } from '@playwright/test';
   import { PageBase } from './PageBase';
   
   export class NewPage extends PageBase {
     async doSomething() {
       // Implementation
     }
   }
   ```

2. **Use in steps**
   ```typescript
   import { NewPage } from '../pages/NewPage';
   
   Given('something', async function() {
     const page = new NewPage(page);
   });
   ```

## Test Files Explained

### search.feature
- Tests basic navigation
- Tests search functionality
- Verifies page titles

### invalid_search.feature
- Tests handling of invalid URLs

## Running Specific Tests

### By Feature
```bash
# Run only search feature
npx cucumber-js features/search.feature

# Run only invalid search feature
npx cucumber-js features/invalid_search.feature
```

### By Scenario Name (using tags)
```bash
# Add tag to feature file
@regression
Scenario: My test

# Run tagged tests
npm test -- --tags @regression
```

## Troubleshooting

### Tests Won't Run
```bash
# Clear node_modules and reinstall
rm -r node_modules package-lock.json
npm install
npm test
```

### XPath/CSS Selectors Not Working
```bash
# Generate selectors automatically
npx playwright codegen https://devon.nl
```

### Need to Debug
```bash
# Run tests with debugger
# Pauses at breakpoints (add `debugger;` in code)
npm run test:debug

# Or use headed mode to see what happens
npm run test:headed
```

### Tests Timeout
1. Check if website is accessible
2. Increase timeout in `src/utils/BrowserManager.ts`
3. Add explicit waits for dynamic content

## ✅ You're Ready!

Your Playwright TypeScript testing framework is ready to use!

**Next steps:**
1. ✅ Run tests: `npm test`
2. ✅ Check reports
3. ✅ Add more scenarios
4. ✅ Add more page objects
5. ✅ Integrate with CI/CD

## Need Help?

📖 **Documentation:**
- [Full README](./README.md)
- [Migration Guide](./MIGRATION_GUIDE.md)
- [Playwright Docs](https://playwright.dev/)
- [Cucumber.js Docs](https://cucumber.io/docs/cucumber/)

🐛 **Common Issues:**
- Check browser is installed: `npx playwright install`
- Verify selectors: `npx playwright codegen`
- Review console output for error messages

---

**Happy Testing! 🚀**
