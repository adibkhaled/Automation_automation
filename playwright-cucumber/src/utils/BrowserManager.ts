import { Browser, BrowserContext, Page, chromium, firefox, webkit } from '@playwright/test';

/**
 * Browser Manager Singleton
 * Handles browser instance creation and management
 */
export class BrowserManager {
  private static instance: BrowserManager;
  private browser: Browser | null = null;
  private context: BrowserContext | null = null;
  private page: Page | null = null;

  private constructor() {}

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
        this.browser = await firefox.launch(this.getBrowserOptions());
        break;
      case 'webkit':
        this.browser = await webkit.launch(this.getBrowserOptions());
        break;
      case 'chrome':
      default:
        this.browser = await chromium.launch(this.getBrowserOptions());
        break;
    }

    return this.browser;
  }

  async getPage(browserName: string = 'chrome'): Promise<Page> {
    if (this.page) {
      return this.page;
    }

    const browser = await this.getBrowser(browserName);
    this.context = await browser.newContext(this.getContextOptions());
    this.page = await this.context.newPage();

    return this.page;
  }

  async closeBrowser(): Promise<void> {
    if (this.page) {
      await this.page.close();
      this.page = null;
    }

    if (this.context) {
      await this.context.close();
      this.context = null;
    }

    if (this.browser) {
      await this.browser.close();
      this.browser = null;
    }
  }

  private getBrowserOptions() {
    const isHeaded = process.env.HEADED === 'true';
    return {
      headless: !isHeaded,
      args: [
        '--disable-blink-features=AutomationControlled',
        '--start-maximized'
      ]
    };
  }

  private getContextOptions() {
    return {
      viewport: { width: 1920, height: 1080 },
      ignoreHTTPSErrors: true
    };
  }

  getPageInstance(): Page | null {
    return this.page;
  }
}
