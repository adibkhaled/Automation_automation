import { Page, Locator } from '@playwright/test';
import { PageBase } from './PageBase';

/**
 * SearchResultPage Page Object
 * Represents the search results page
 */
export class SearchResultPage extends PageBase {
  private invalidSearchResultMessageLocator: Locator;

  constructor(page: Page) {
    super(page);
    // Using the same XPath from Selenium code
    this.invalidSearchResultMessageLocator = page.locator(".//*[@id='content']/p[2]");
  }

  /**
   * Get the invalid search result message element
   */
  getInvalidSearchResultMessage(): Locator {
    return this.invalidSearchResultMessageLocator;
  }

  /**
   * Get the invalid search result message text
   */
  async getInvalidSearchResultMessageText(): Promise<string> {
    return await this.invalidSearchResultMessageLocator.textContent() || '';
  }

  /**
   * Load the search result page
   */
  async load(): Promise<void> {
    if (this.url) {
      await this.open(this.url);
    }
  }
}
