import { test, expect } from '@playwright/test';

test('E2E Movie Rating Filter', async ({ page }) => {
    // Navigate to the movie listing page
    await page.goto('http://localhost:8080/components/library.html'); // Adjust URL if necessary

    // Wait for the PG rating filter dropdown to appear
    const pgRatingFilter = page.locator('#pg-rating-filter');
    await pgRatingFilter.waitFor({ state: 'visible', timeout: 10000 });

    // Select a PG rating from the dropdown
    await pgRatingFilter.selectOption({ label: 'R' });

    // Wait for the movie list to be updated
    const movieItems = page.locator('.movie_item');
    await expect(movieItems).toHaveCount(50, { timeout: 10000 }); // Expected number for PG-rated movies

    // Log a success message
    console.log('Test passed: Movie Ratings filter works correctly for "R"!');
});
