import { test, expect } from '@playwright/test';

test('E2E Movie Delete and Search in Header', async ({ page }) => {
    // Navigate directly to the "Manage Movies" page (assuming admin is logged in)
    await page.goto('http://localhost:8080/components/manageMovies.html');

    // Click "Delete Movie" to go to the delete movie page
    await page.click('a[href="../components/deleteMovie.html"]');

    // Search for the movie to delete
    const searchInput = page.locator('input#movie-search');
    await searchInput.fill('Test Movie Title');
    const searchButton = page.locator('button#search-button');
    await searchButton.click();

    // Wait for the search results to load and find the movie
    const movieItem = page.locator('.movie-result', { hasText: 'Test Movie Title' });
    await expect(movieItem).toBeVisible({ timeout: 10000 });

    // Handle the first confirmation alert (Are you sure?)
    page.once('dialog', async dialog => {
        expect(dialog.message()).toContain('Are you sure you want to delete "Test Movie Title"?');
        await dialog.accept();
    });

    // Intercept and wait for the DELETE request
    const [deleteResponse] = await Promise.all([
        page.waitForResponse(response =>
            response.url().includes('/api/movies/delete') && response.status() === 200
        ),
        movieItem.locator('.delete-button').click()
    ]);

    console.log('Delete API Response:', await deleteResponse.text());

    // Ensure the movie is removed from the UI
    await expect(movieItem).toBeHidden({ timeout: 10000 });

    // Now search in the header search bar
    const headerSearchInput = page.locator('input[placeholder="Search for a Movie..."]');
    await headerSearchInput.fill('Test Movie Title');

    // Wait for the search button in the header to be enabled
    const headerSearchButton = page.locator('button:has-text("🔍")');
    await expect(headerSearchButton).toBeEnabled({ timeout: 5000 });

    // Click the search button and wait for results update
    await headerSearchButton.click();

    // Ensure the "No movies found" message is displayed
    const noMoviesMessage = page.locator('p:has-text("No movies found.")');
    await expect(noMoviesMessage).toBeVisible({ timeout: 10000 });

    // Log success message
    console.log('Test passed: Movie "Test Movie Title" successfully deleted and no longer found via header search!');
});

