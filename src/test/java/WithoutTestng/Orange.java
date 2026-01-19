package WithoutTestng;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Orange {

	public static void main(String[] args) {
		
		ExtentReports extent = new ExtentReports();
		String reportDir = "Results";
		ensureDir(reportDir);
		String reportPath = reportDir + File.separator +"OrangeHRM_Login_Report.html";
		ExtentSparkReporter html = new ExtentSparkReporter(reportPath);
		extent.attachReporter(html);
		ExtentTest test = extent.createTest("Login - Valid credentials");
		
		WebDriver driver = new ChromeDriver();
	    driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		test.info("Opened login page");
		test.pass("Login page opened successfully");
		Screenshot(driver, reportDir + File.separator + "LoginPage.png");
		test.addScreenCaptureFromPath(reportDir + File.separator + "LoginPage.png");
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
		test.info("Entered username");
		test.pass("Username entererd successfully");
		Screenshot(driver, reportDir + File.separator + "UsernameEntered.png");
		test.addScreenCaptureFromPath(reportDir + File.separator + "UsernameEntered.png");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");
		test.info("Entered password");
		test.pass("Password Entered successfully");
		Screenshot(driver, reportDir + File.separator + "PasswordEntered.png");
		test.addScreenCaptureFromPath(reportDir + File.separator + "PasswordEntered.png");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.info("clicked on login button");
		test.pass("Login button clicked successfully");
		Screenshot(driver, reportDir + File.separator + "AfterLogin.png");	
		test.addScreenCaptureFromPath(reportDir + File.separator + "AfterLogin.png");
		assert driver.findElement(By.xpath("//h6[text()='Dashboard']")).isDisplayed() : "Login failed - Dashboard not found!";
		test.info("Dashboard is displayed");
		test.pass("Dashboard displayed successfully");
		Screenshot(driver, reportDir + File.separator + "Dashboard.png");
		test.addScreenCaptureFromPath(reportDir + File.separator + "Dashboard.png");
		
		extent.flush();
		
		System.out.println("Test completed. Report generated at: " + reportPath);
		
		driver.quit();

	}
	

private static void ensureDir(String dir) {
        try { Files.createDirectories(Paths.get(dir)); } catch (Exception ignore) {}
}

public static void Screenshot(WebDriver driver, String path) {
	try {
		File src = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
		Files.copy(src.toPath(), Paths.get(path));
	} catch (Exception ignore) {}
	
}


}
