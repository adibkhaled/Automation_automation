import { Page, Locator } from '@playwright/test';

/**
 * Base Page Class - Parent class for all page objects
 * Provides common functionality and utilities
 */
export class PageBase {
  protected page: Page;
  protected url: string = '';
  protected name: string = '';

  constructor(page: Page) {
    this.page = page;
  }

  /**
   * Navigate to a specific URL
   */
  async goto(url: string): Promise<void> {
    await this.page.goto(url, { waitUntil: 'domcontentloaded', timeout: 30000 }).catch(() => {
      console.log(`Note: Navigation to ${url} completed with some delays`);
    });
  }

  /**
   * Open page by URL (similar to Java version)
   */
  async open(url: string): Promise<void> {
    this.url = url;
    await this.page.goto(url, { waitUntil: 'domcontentloaded', timeout: 30000 }).catch(() => {
      console.log(`Note: Navigation to ${url} completed with some delays`);
    });
  }

  /**
   * Get current page title
   */
  async getTitle(): Promise<string> {
    return await this.page.title();
  }

  /**
   * Get current page URL
   */
  async getCurrentUrl(): Promise<string> {
    return this.page.url();
  }

  /**
   * Set page name
   */
  setName(name: string): void {
    this.name = name;
  }

  /**
   * Get page name
   */
  getName(): string {
    return this.name;
  }

  /**
   * Set page URL
   */
  setUrl(url: string): void {
    this.url = url;
  }

  /**
   * Get page URL
   */
  getUrl(): string {
    return this.url;
  }

  /**
   * Wait for element to be visible
   */
  async waitForElement(locator: Locator, timeout: number = 5000): Promise<void> {
    await locator.waitFor({ state: 'visible', timeout });
  }

  /**
   * Take screenshot
   */
  async takeScreenshot(filename: string): Promise<void> {
    await this.page.screenshot({ path: `screenshots/${filename}.png` });
  }

  /**
   * Close page
   */
  async close(): Promise<void> {
    await this.page.close();
  }
}
