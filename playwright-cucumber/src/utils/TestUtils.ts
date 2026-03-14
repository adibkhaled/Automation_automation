/**
 * Common utilities and test hooks
 */
import { BrowserManager } from './BrowserManager';

export class TestUtils {
  /**
   * Sleep for specified milliseconds
   */
  static async sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  /**
   * Close browser after test
   */
  static async closeBrowser(): Promise<void> {
    const browserManager = BrowserManager.getInstance();
    await browserManager.closeBrowser();
  }

  /**
   * Get page instance
   */
  static getPage() {
    const browserManager = BrowserManager.getInstance();
    return browserManager.getPageInstance();
  }
}
