import { test, expect } from '@playwright/test';
import path from 'path';

test('E2E Movie Add', async ({ page }) => {
    // Navigate directly to the "Manage Movies" page (since admin is already logged in)
    await page.goto('http://localhost:8080/components/manageMovies.html');

    // Click "Insert Movies" to go to the add movie page
    await page.click('a[href="../components/addMoviePage.html"]');

    // Fill out the add movie form with all necessary fields
    await page.fill('input[name="title"]', 'Test Movie Title');
    await page.fill('input[name="releaseDate"]', '2025-04-01');
    await page.fill('input[name="pgRating"]', 'PG-13');
    await page.fill('textarea[name="synopsis"]', 'This is a test movie synopsis.');
    await page.fill('input[name="genres"]', 'Action, Adventure');
    await page.fill('input[name="productionCompanies"]', 'Test Production Co.');
    await page.fill('input[name="runtime"]', '120');
    await page.fill('input[name="spokenLanguages"]', 'English, Spanish');

    // Handle file upload for the poster
    const filePath = path.resolve(__dirname, '../../src/main/resources/static/userimg/test-poster.jpg');
    await page.setInputFiles('input[name="poster"]', filePath);

    // Wait for the "Add Movie" submit button to be enabled before clicking
    const submitButton = page.locator('button[type="submit"]:not([disabled])');
    await submitButton.waitFor({ state: 'visible' });
    await submitButton.click();

    // Wait for search bar to appear type the movie
    const searchInput = page.locator('input[placeholder="Search for a Movie..."]');
    await searchInput.fill('Test Movie Title');
    // Wait for the search button to be enabled
    const searchButton = page.locator('button:has-text("🔍")');
    await expect(searchButton).toBeEnabled({ timeout: 10000 });
    await searchButton.click();

    // Check if the movie is displayed by verifying the title
    const movieItem = page.locator('.movies .movie_item', { hasText: 'Test Movie Title' });
    await expect(movieItem).toBeVisible({ timeout: 20000 });

    // Verify the movie title text
    const movieTitleLocator = movieItem.locator('.title');
    const movieTitle = await movieTitleLocator.textContent();
    console.log('Movie Title: ', movieTitle);
    expect(movieTitle).toContain('Test Movie Title');

    // Log a success message
    console.log('Test passed: Movie "Test Movie Title" successfully added and displayed!');
});
