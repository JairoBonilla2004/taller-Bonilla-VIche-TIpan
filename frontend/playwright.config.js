// @ts-check
const { defineConfig, devices } = require('@playwright/test');

module.exports = defineConfig({
  testDir: './tests',
  timeout: 30 * 1000,
  expect: {
    timeout: 5 * 1000,
  },
  fullyParallel: true,
  retries: 0,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL: 'http://127.0.0.1:3000',
    // Enable artifacts for visual auditing
    trace: 'on', // capture full trace for every test run
    screenshot: 'on', // capture screenshots for all steps
    video: 'retain-on-failure', // record video and keep only when a test fails
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
  webServer: process.env.PW_NO_WEBSERVER ? undefined : {
    command: process.platform === 'win32'
      ? 'set CI=true&& set BROWSER=none&& npm start'
      : 'CI=true BROWSER=none npm start',
    url: 'http://127.0.0.1:3000',
    reuseExistingServer: true,
    timeout: 120 * 1000,
  },
});
