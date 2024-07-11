package Testing.Nimap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.TestNG;

import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class LoginTest {

    private WebDriver driver;
    private Properties properties;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        // Set ChromeOptions to use a custom User-Agent and additional options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);

        // Load properties file
        properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @BeforeMethod
    public void beforeMethod() {
        // Navigate to the login page before each test
        driver.get("https://testffc.nimapinfotech.com/auth/login");
    }

    @Test
    public void login() throws InterruptedException {
        // Get the email and password from properties
        String Email_id = properties.getProperty("Email_id");
        String password = properties.getProperty("password");

        // Locate the username and password input fields
        WebElement usernameField = driver.findElement(By.xpath("//input[@id='mat-input-0']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='mat-input-1']"));

        // Enter the provided email ID and password
        usernameField.sendKeys(Email_id);
        passwordField.sendKeys(password);

        // Show the JOptionPane dialog to manually solve the CAPTCHA
        JOptionPane.showMessageDialog(null, "Please solve the CAPTCHA manually and click OK to continue...");

        // Find the Sign In button and click it
        WebElement signInButton = driver.findElement(By.id("kt_login_signin_submit"));
        signInButton.click();

        // Wait for the dashboard element to be visible
        WebDriverWait dashboardWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement dashboardElement = dashboardWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard-element-id")));

            // Perform actions on the dashboard
            WebElement customer = driver.findElement(By.cssSelector(".kt-menu__item--active > a:nth-child(1) > span:nth-child(2)"));
            customer.click();

            // Add a new customer
            WebElement addCustomer = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@mattooltip='Create new customer']")));
            addCustomer.click();

            // Enter customer name
            WebElement customerName = driver.findElement(By.id("mat-input-36"));
            customerName.sendKeys("abc");

            // Click on save
            WebElement saveButton = driver.findElement(By.cssSelector("div.text-right:nth-child(1) > div:nth-child(1) > button:nth-child(2) > div:nth-child(2)"));
            saveButton.click();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Unable to access the dashboard or customer section.");
        }
    }

//    @AfterMethod
//    public void tearDown() {
//        driver.quit();
//    }

    public static void main(String[] args) {
        // Set email and password parameters
        System.setProperty("Email_id", "mishraaditya1901@gmail.com");
        System.setProperty("password", "Aditya123");

        // Run the TestNG tests
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { LoginTest.class });
        testng.run();
    }
}
