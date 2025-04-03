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
    /* Set the base URL for your tests (optional, for easier linking). */
    // baseURL: 'http://127.0.0.1:3000',

    /* Collect trace when retrying failed tests. */
    trace: 'on-first-retry',
  },

  /* Configure projects for multiple browsers. */
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],

  /* Optionally, run a local dev server before starting the tests. */
  // webServer: {
  //   command: 'npm run start', // Adjust the command to start your local server.
  //   url: 'http://127.0.0.1:3000', // The URL of your dev server.
  //   reuseExistingServer: !process.env.CI, // Reuse existing server if it's not CI.
  // },
});
