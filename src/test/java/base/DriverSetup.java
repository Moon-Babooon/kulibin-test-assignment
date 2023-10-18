package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class DriverSetup {

    public static WebDriver driver;

    @BeforeSuite
    public static void setup() {
        driver = driverInit();
    }

    @AfterSuite
    public static void tearDown() {
        if (driver != null)
            driver.quit();
    }

    public static WebDriver driverInit() {
        String type = System.getProperty("driver.type", "CHROME");
        DriverType driverType = DriverType.valueOf(type);
        return switch (driverType) {
            case CHROME -> chromeDriverInit();
            case FIREFOX -> firefoxDriverInit();
        };
    }

    private static WebDriver chromeDriverInit(){
        return new ChromeDriver(chromeOptions());
    }

    private static WebDriver firefoxDriverInit() {
        return new FirefoxDriver(firefoxOptions());
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.setAcceptInsecureCerts(true);
        return options;
    }

    private static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);
        return options;
    }

}
