import { test, expect } from '@playwright/test';

test('E2E Movie Search', async ({ page }) => {
    // Navigate directly to the "Manage Movies" page (since admin is already logged in)
    await page.goto('http://localhost:8080/index.html');

    // Wait for the search bar to appear
    const searchInput = page.locator('input[placeholder="Search for a Movie..."]');
    await searchInput.waitFor({ state: 'visible', timeout: 10000 });

    // Type the movie title
    await searchInput.fill('Inception');

    // Wait for the search button to be enabled
    const searchButton = page.locator('button:has-text("🔍")');
    await expect(searchButton).toBeEnabled({ timeout: 5000 });

    // Click the search button and wait for results update
    await searchButton.click();

    // Check if the movie is displayed by verifying the title
    const movieItem = page.locator('.movies .movie_item', { hasText: 'Inception' });
    await expect(movieItem).toBeVisible({ timeout: 10000 });

    // Verify the movie title text
    const movieTitleLocator = movieItem.locator('.title');
    const movieTitle = await movieTitleLocator.textContent();
    console.log('Movie Title: ', movieTitle);
    expect(movieTitle).toContain('Inception');

    // Log a success message
    console.log('Test passed: Movie "Inception" successfully found and displayed!');
});