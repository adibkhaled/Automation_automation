import { test, expect } from '@playwright/test';
import config from "../config.json";


test.beforeEach(async ({ page })=> {
  await page.goto(config.url)
});

test('Title present verify', async ({ page }) => {
  await expect(page).toHaveTitle(config.title);
});

test('Search button functionality verify', async ({ page }) => {
  await page.getByRole('link', { name: ' Search' }).click();
  await page.getByRole('searchbox', { name: 'To search' }).click();
  await page.getByRole('searchbox', { name: 'To search' }).fill('test 2');
  await page.getByRole('button', { name: 'To search …' }).click();
  await page.locator('#post-800').getByRole('link').first().click();
  await expect(page.getByText('management', { exact: true })).toBeVisible();
});

