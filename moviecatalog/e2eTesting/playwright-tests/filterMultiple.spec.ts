import { test, expect } from '@playwright/test';

test('E2E Combined Filter Test', async ({ page }) => {
    // Navigate to the movie listing page
    await page.goto('http://localhost:8080/components/library.html'); // Adjust URL if necessary

    // Wait for all filter dropdowns to appear
    const genreFilter = page.locator('#genre-filter');
    const pgRatingFilter = page.locator('#pg-rating-filter');
    const languageFilter = page.locator('#languages-filter');

    await Promise.all([
        genreFilter.waitFor({ state: 'visible', timeout: 10000 }),
        pgRatingFilter.waitFor({ state: 'visible', timeout: 10000 }),
        languageFilter.waitFor({ state: 'visible', timeout: 10000 })
    ]);

    // Apply filters
    await genreFilter.selectOption({ label: 'Animation' });
    await pgRatingFilter.selectOption({ label: 'Pg-13' });
    await languageFilter.selectOption({ label: 'English' });

    // Wait for the filtered movie list to update
    const movieItems = page.locator('.movie_item');
    await expect(movieItems).toHaveCount(5, { timeout: 10000 }); // Expected count:5

    // Log success message
    console.log('Test passed: Filters for Genre, PG Rating, and Language work correctly!');
});
