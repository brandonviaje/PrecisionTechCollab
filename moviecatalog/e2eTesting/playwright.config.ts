import { defineConfig, devices } from '@playwright/test';

/**
 * Read environment variables from file.
 * https://github.com/motdotla/dotenv
 */
// import dotenv from 'dotenv';
// import path from 'path';
// dotenv.config({ path: path.resolve(__dirname, '.env') });

/**
 * Playwright Test Configuration.
 * See https://playwright.dev/docs/test-configuration for full documentation.
 */
export default defineConfig({
  // The directory where your tests are located
  testDir: './playwright-tests',

  /* Run tests in files in parallel. */
  fullyParallel: true,

  /* Fail the build on CI if you accidentally left test.only in the source code. */
  forbidOnly: !!process.env.CI,

  /* Retry failed tests on CI only. */
  retries: process.env.CI ? 2 : 0,

  /* Opt out of parallel tests on CI by limiting the number of workers. */
  workers: process.env.CI ? 1 : undefined,

  /* Reporter to use. Here, we're using the HTML reporter for a visual result. */
  reporter: 'html',

  /* Shared settings for all the projects below. */
  use: {
    trace: 'on-first-retry',
    headless: false,
    launchOptions: {
      slowMo: 1000, // Slow down actions by 50ms
    },
  },

  /* Configure projects for multiple browsers. */
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],

});
