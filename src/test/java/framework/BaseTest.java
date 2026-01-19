
package framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;
// import io.github.bonigarcia.wdm.WebDriverManager; // optional fallback

public class BaseTest {
    protected WebDriver driver;
    protected String baseUrl;

    @BeforeClass
    public void setUp() {
        baseUrl = System.getProperty("baseUrl", "https://incredible-phoenix-751bd5.netlify.app/");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");

        // Selenium 4 auto-resolves ChromeDriver via Selenium Manager
        // If you face driver issues, uncomment:
        // WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
