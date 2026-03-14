import { Page, Locator } from '@playwright/test';
import { PageBase } from './PageBase';

/**
 * SearchPanel Page Object
 * Encapsulates search functionality
 */
export class SearchPanel extends PageBase {
  private textBoxLocator: Locator;
  private buttonLocator: Locator;

  constructor(page: Page) {
    super(page);
    // Using multiple selector strategies for reliability
    // Primary: The exact XPath from Selenium code
    // Fallback: CSS selectors and alternative XPaths
    this.textBoxLocator = page.locator("//div[@class='mk-fullscreen-search-wrapper']/input").or(
      page.locator('input[type="search"]')
    ).or(
      page.locator('.mk-fullscreen-search-wrapper input')
    ).or(
      page.locator('input[placeholder="Search..."]')
    ).or(
      page.locator('input[class*="search"]')
    );
    
    this.buttonLocator = page.locator("//svg[@class='mk-svg-icon']").or(
      page.locator('svg.mk-svg-icon')
    ).or(
      page.locator('button[type="submit"]')
    ).or(
      page.locator('[class*="search"][class*="button"]')
    );
  }

  /**
   * Get the search text box element
   */
  getTextBox(): Locator {
    return this.textBoxLocator;
  }

  /**
   * Get the search button element
   */
  getButton(): Locator {
    return this.buttonLocator;
  }

  /**
   * Clear and enter search text
   */
  async enterSearchText(text: string): Promise<void> {
    try {
      await this.textBoxLocator.fill(text, { timeout: 10000 });
      console.log(`Entered search text: ${text}`);
    } catch (error) {
      console.log(`Warning: Could not find search box with XPath. ${error}`);
      throw error;
    }
  }

  /**
   * Click search button
   */
  async clickSearchButton(): Promise<void> {
    try {
      await this.buttonLocator.click({ timeout: 10000 });
      console.log('Clicked search button');
    } catch (error) {
      console.log(`Warning: Could not find search button. ${error}`);
      throw error;
    }
  }

  /**
   * Perform search (clear, fill, and click)
   */
  async search(searchTerm: string): Promise<void> {
    await this.enterSearchText(searchTerm);
    await this.clickSearchButton();
  }
}
