import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
    await page.goto('http://localhost:8080/components/signUp.html');
    await page.getByRole('textbox', { name: 'Full Name:' }).click();
    await page.getByRole('textbox', { name: 'Full Name:' }).fill('person');
    await page.getByRole('textbox', { name: 'Username:' }).click();
    await page.getByRole('textbox', { name: 'Username:' }).fill('Brandon');
    await page.getByRole('textbox', { name: 'Password:', exact: true }).click();
    await page.getByRole('textbox', { name: 'Password:', exact: true }).fill('testing123');
    await page.getByRole('textbox', { name: 'Confirm Password:' }).click();
    await page.getByRole('textbox', { name: 'Confirm Password:' }).fill('testing123');
    await page.getByRole('button', { name: 'Sign Up' }).click();

    page.once('dialog', dialog => {
        console.log(`Dialog message: ${dialog.message()}`);
        dialog.dismiss().catch(() => {});
    });

    await page.getByRole('textbox', { name: 'Username:' }).click();
    await page.getByRole('textbox', { name: 'Username:' }).fill('Brandon');
    await page.getByRole('textbox', { name: 'Password:' }).click();
    await page.getByRole('textbox', { name: 'Password:' }).fill('testing123');
    await page.getByRole('button', { name: 'Sign In' }).click();
    await expect(page.getByRole('button', { name: 'Sign Out' })).toBeVisible();
});