import { Page, Locator } from '@playwright/test';
import { PageBase } from './PageBase';
import { SearchPanel } from './SearchPanel';

/**
 * HomePage Page Object
 * Represents the home page of the application
 */
export class HomePage extends PageBase {
  public search: SearchPanel;

  constructor(page: Page) {
    super(page);
    this.search = new SearchPanel(page);
  }

  /**
   * Navigate to the home page
   */
  async load(): Promise<void> {
    if (this.url) {
      await this.open(this.url);
    }
  }
}
