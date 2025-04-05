import { test, expect } from '@playwright/test';

test('E2E Movie Update', async ({ page }) => {
    // Navigate directly to the "Manage Movies" page
    await page.goto('http://localhost:8080/components/manageMovies.html');

    // Click "Update Movie" to go to the update movie page
    await page.click('a[href="../components/updateMoviePage.html"]');

    // Search for the movie to update
    const movieSearchInput = page.locator('input#movie-search');
    await movieSearchInput.fill('Saw');
    const searchButton = page.locator('button#search-button');
    await searchButton.click();

    // Wait for the search results to load and find the movie
    const selectedMovie = page.locator('.movie-result', { hasText: 'Saw' }).nth(1);
    await selectedMovie.waitFor({ state: 'visible', timeout: 15000 });
    await expect(selectedMovie).toBeVisible({ timeout: 10000 });
    await selectedMovie.click();

    // Wait for the page to load
    await page.waitForURL(/updateMovie\.html\?id=\d+/, { timeout: 5000 });

    // Wait for the textarea to be ready and not readonly or disabled
    const synopsisTextarea = page.locator('textarea[name="synopsis"]');
    await page.waitForSelector('textarea[name="synopsis"]:not([readonly]):not([disabled])');

    // Ensure the textarea is clickable and in editable state
    await synopsisTextarea.click();
    await synopsisTextarea.fill('');
    await synopsisTextarea.fill('This is an updated test movie synopsis.');

    // Verify the synopsis is filled properly
    await expect(synopsisTextarea).toHaveValue('This is an updated test movie synopsis.');

    // Click the update button and wait for confirmation
    const updateButton = page.locator('button[type="submit"]:not([disabled])');
    await updateButton.waitFor({ state: 'visible' });
    await updateButton.click();

    // Now search in the header search bar
    const headerSearchInput = page.locator('input[placeholder="Search for a Movie..."]');
    await headerSearchInput.fill('Saw');

    // Wait for the search button in the header to be enabled
    const headerSearchButton = page.locator('button:has-text("🔍")');
    await expect(headerSearchButton).toBeEnabled({ timeout: 5000 });
    await headerSearchButton.click();

    // Wait for the search results to load and find the second movie result (Saw)
    const updatedMovie = page.locator('.movie_item', { hasText: 'Saw' }).nth(1);
    await updatedMovie.waitFor({ state: 'visible', timeout: 15000 });
    await expect(updatedMovie).toBeVisible({ timeout: 10000 });

    // Click the second movie result (Saw)
    await updatedMovie.click();
    await page.waitForURL(/movieDetails\.html\?id=\d+/, { timeout: 5000 });

    // Verify that the movie synopsis is displayed
    const movieSynopsis = page.locator('#movie-overview');
    await expect(movieSynopsis).toHaveText('This is an updated test movie synopsis.', { timeout: 20000 });

    // Log a success message
    console.log('Test passed: Movie "Saw" successfully selected and movie details page displayed!');
});
