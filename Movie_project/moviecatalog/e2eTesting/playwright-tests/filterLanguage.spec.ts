import { test, expect } from '@playwright/test';

test('E2E Language Filter', async ({ page }) => {
    // Navigate to the movie listing page
    await page.goto('http://localhost:8080/components/library.html'); // Adjust URL if necessary

    // Wait for the language filter dropdown to appear
    const languageFilter = page.locator('#languages-filter');
    await languageFilter.waitFor({ state: 'visible', timeout: 10000 });

    // Select a language from the dropdown
    await languageFilter.selectOption({ label: 'English' }); // Change 'English' to a relevant language

    // Wait for the movie list to be updated
    const movieItems = page.locator('.movie_item');
    await expect(movieItems).toHaveCount(186, { timeout: 10000 }); // Expected number for English movies is 186

    // Log a success message
    console.log('Test passed: Language filter works correctly for "English"!');
});
