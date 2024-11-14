import { test, expect } from '@playwright/test';


test('Title present', async ({ page }) => {
  await page.goto('https://www.cimsolutions.nl/');
  await expect(page).toHaveTitle(/CIMSOLUTIONS/);
});

test('Search link functionalities', async ({ page }) => {
  await page.goto('https://www.cimsolutions.nl/');
  await page.getByRole('link', { name: ' Search' }).click();
  await page.getByRole('searchbox', { name: 'To search' }).click();
  await page.getByRole('searchbox', { name: 'To search' }).fill('test 2');
  await page.getByRole('button', { name: 'To search …' }).click();
  await page.locator('#post-800').getByRole('link').first().click();
  await expect(page.getByText('management', { exact: true })).toBeVisible();
});

