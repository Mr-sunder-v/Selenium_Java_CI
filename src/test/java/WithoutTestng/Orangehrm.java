package WithoutTestng;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Orangehrm {

    public static void main(String[] args) {
        // ====== Configurable inputs (VM args or env) ======
        String baseUrl   = sysOrEnv("baseUrl",   "https://opensource-demo.orangehrmlive.com"); // demo site root
        String username  = sysOrEnv("username",  "Admin");      // demo creds show on page
        String password  = sysOrEnv("password",  "admin123");   // demo creds show on page
        boolean headless = Boolean.parseBoolean(sysOrEnv("headless", "false"));

        // ====== ExtentReports setup ======
        String reportDir = "reports";
        String reportName = "OrangeHRM_Login_Report.html";
        ensureDir(reportDir);
        ExtentSparkReporter spark = new ExtentSparkReporter(reportDir + File.separator + reportName);
        spark.config().setDocumentTitle("OrangeHRM Login - Selenium (No TestNG)");
        spark.config().setReportName("OrangeHRM Login Automation");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Author", "Sunder V");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("Mode", headless ? "Headless" : "Headed");

        ExtentTest test = extent.createTest("Login - Valid credentials");

        WebDriver driver = null;
        try {
            // ====== Browser setup (Selenium 4+ can auto-manage drivers) ======
            ChromeOptions options = new ChromeOptions();
            if (headless) options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            driver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // ====== Step 1: Navigate to login page ======
            String loginUrl = baseUrl + "/web/index.php/auth/login";
            driver.get(loginUrl);
            test.info("Opened login page: " + loginUrl);

            // ====== Step 2: Enter username & password ======
            // OrangeHRM demo uses name='username' and name='password' on login page
            // Button is //button[@type='submit']
            // (These are stable on the demo and widely referenced) 
            // Fill username
            WebElement userInput = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.name("username")));
            userInput.clear();
            userInput.sendKeys(username);
            test.pass("Entered username");

            // Fill password
            WebElement pwdInput = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.name("password")));
            pwdInput.clear();
            pwdInput.sendKeys(password);
            test.pass("Entered password");

            // Click Login
            WebElement loginBtn = wait.until(ExpectedConditions
                    .elementToBeClickable(By.xpath("//button[@type='submit']")));
            loginBtn.click();
            test.info("Clicked Login");

            // ====== Step 3: Verify successful login ======
            // Common success checks:
            //  - URL contains /dashboard
            //  - A "Dashboard" header (h6) becomes visible
            wait.until(ExpectedConditions.urlContains("/dashboard"));
            WebElement dashboardHeader =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']")));
            test.pass("Login successful. Landed on: " + driver.getCurrentUrl());

        } catch (Throwable t) {
            test.fail("Test failed: " + t.getMessage());
            // Attach screenshot
            if (driver != null) {
                try {
                    String png = takeScreenshot(driver, "login_failure");
                    test.addScreenCaptureFromPath(png);
                } catch (Exception e) {
                    test.warning("Failed to attach screenshot: " + e.getMessage());
                }
            }
        } finally {
            if (driver != null) driver.quit();
            extent.flush();
            System.out.println("Report generated at: " + new File("reports/OrangeHRM_Login_Report.html").getAbsolutePath());
        }
    }

    // --- Helpers ---

    private static String sysOrEnv(String key, String def) {
        String v = System.getProperty(key);
        if (v == null || v.trim().isEmpty()) v = System.getenv(key);
        return (v == null || v.trim().isEmpty()) ? def : v;
    }

    private static void ensureDir(String dir) {
        try { Files.createDirectories(Paths.get(dir)); } catch (Exception ignore) {}
    }

    private static String takeScreenshot(WebDriver driver, String baseName) throws Exception {
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        String ts = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
        String path = "reports" + File.separator + baseName + "_" + ts + ".png";
        Files.write(Paths.get(path), bytes);
        return path;
    }
}