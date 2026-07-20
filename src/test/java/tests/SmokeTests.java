
package tests;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeTests extends BaseTest {

    @Test
    public void homePageLoads_andModalOpens() {
        driver.get(baseUrl + "/index.html");
        String hero = driver.findElement(By.cssSelector("[data-testid='hero-title']")).getText();
        Assert.assertTrue(hero.contains("Test Site"), "Hero title should contain 'Test Site'");

        driver.findElement(By.cssSelector("[data-testid='open-modal-btn']")).click();
        String cls = driver.findElement(By.cssSelector("[data-testid='modal-backdrop']")).getAttribute("class");
        Assert.assertTrue(cls.contains("show"), "Modal should be visible");
    }

    @Test
    public void products_addToCart_incrementsBadge() {
        driver.get(baseUrl + "/products.html");

        int before = Integer.parseInt(driver.findElement(By.cssSelector("[data-testid='cart-badge']")).getText().trim());
        driver.findElement(By.cssSelector("[data-testid='product-search']")).sendKeys("Beta");
        driver.findElement(By.cssSelector("[data-testid='add-cart-beta']")).click();
        int after = Integer.parseInt(driver.findElement(By.cssSelector("[data-testid='cart-badge']")).getText().trim());

        Assert.assertEquals(after, before + 1, "Cart count should increase by 1");
    }

    @Test
    public void login_then_dashboard_shows_email() {
        driver.get(baseUrl + "/login.html");
        driver.findElement(By.cssSelector("[data-testid='login-email']")).sendKeys("sunder@example.com");
        driver.findElement(By.cssSelector("[data-testid='login-password']")).sendKeys("secret123");
        driver.findElement(By.cssSelector("[data-testid='login-submit']")).click();

        driver.get(baseUrl + "/dashboard.html");
        String email = driver.findElement(By.cssSelector("[data-testid='user-email']")).getText();
        Assert.assertTrue(email.contains("sunder@example.com"));
        System.out.println("Test");
    }
}
