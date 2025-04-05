import { test, expect } from '@playwright/test';

test('E2E Favourite Movie', async ({ page }) => {
    // Navigate to the sign-in page
    await page.goto('http://localhost:8080/components/signIn.html');

    // Fill in the sign-in form and submit
    const usernameInput = page.locator('#login-username');
    const passwordInput = page.locator('#password');
    const submitButton = page.locator('button[type="submit"]:not([disabled])');

    await usernameInput.fill('hello');
    await passwordInput.fill('hello123');
    await submitButton.click();

    // Wait for the sign-in page to redirect to the homepage (index.html)
    await page.waitForURL('http://localhost:8080/index.html', { timeout: 10000 });

    // Open the menu and click on 'View All' link
    const menuBtn = page.locator('#menu-btn');
    await menuBtn.click();  // Open the dropdown menu
    const viewAllLink = page.locator('a[href="/components/library.html"]');
    await viewAllLink.waitFor({ state: 'visible', timeout: 5000 });
    await viewAllLink.click();

    // Wait for navigation to "library.html"
    await page.waitForURL('http://localhost:8080/components/library.html', { timeout: 10000 });

    // Wait for the first movie item to be visible and click on it
    const firstMovie = page.locator('.movie_item').first();
    await firstMovie.waitFor({ state: 'visible', timeout: 10000 });
    await expect(firstMovie).toBeVisible();
    await firstMovie.click();
    await page.waitForLoadState('load', { timeout: 10000 });

    // Click the heart icon to add to favorites
    const heartIcon = page.locator('#favourite-btn');
    await expect(heartIcon).toBeVisible();
    await heartIcon.click();

    // Open the menu and click on "Favorites"
    await menuBtn.click();
    const favoritesLink = page.locator('a[href="/components/favourite.html"]');  // "Favorites" link
    await favoritesLink.waitFor({ state: 'visible', timeout: 5000 });
    await favoritesLink.click();

    // Wait for the page to fully load before proceeding
    await page.waitForLoadState('load');

    // Wait for the movie cards to be visible
    const movieCards = page.locator('.movie-card');

    // Iterate through each movie card and check for the title "300"
    const titles = await movieCards.locator('.movie-title').allTextContents();
    const isMovie300Present = titles.includes('300');

    // Assert that "300" is one of the titles
    expect(isMovie300Present).toBe(true);

    // Success message
    console.log('Test passed: Movie "300" successfully added to favorites!');
});