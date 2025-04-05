import { test, expect } from '@playwright/test';

test('E2E Genre Filter', async ({ page }) => {
    // Navigate to the movie listing page
    await page.goto('http://localhost:8080/components/library.html'); // Adjust URL if necessary

    // Wait for the genre filter dropdown to appear
    const genreFilter = page.locator('#genre-filter');
    await genreFilter.waitFor({ state: 'visible', timeout: 10000 });

    // Select a genre from the dropdown
    await genreFilter.selectOption({ label: 'Animation' }); // Change 'Animation' to a relevant genre

    // Wait for the movie list to be updated
    const movieItems = page.locator('.movie_item');
    await expect(movieItems).toHaveCount(23, { timeout: 10000 }); //expected is 23 for animation movies

    // Log a success message
    console.log('Test passed: Genre filter works correctly for "Animation"!');
});
