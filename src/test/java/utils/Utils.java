package utils;

import base.DriverSetup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class Utils extends DriverSetup {

    public void scrollIntoView(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public WebElement getElement(By locator) {
        waitUntilVisible(locator, 5L);
        return driver.findElement(locator);
    }

    public List<WebElement> getElements(By locator) {
        waitUntilVisible(locator, 5L);
        return driver.findElements(locator);
    }

    public void waitUntilClickable(WebElement element, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitUntilClickable(By locator, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitUntilVisible(WebElement element, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void waitUntilVisible(By locator, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitUntilVisibilityOfAllElements(WebElement elements, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public void waitUntilVisibilityOfAllElements(By locator, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public void moveToElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public Document getHTML() {
        try {
            return Jsoup.connect(driver.getCurrentUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public int priceToNumber(String price) {
        String cleanPrice = price.replace(" грн.", "").replace(" ", "");
        if (cleanPrice.contains(",00")) {
            String finalPrice = cleanPrice.replace(",00", "");
            return Integer.parseInt(finalPrice);
        } else {
            return Integer.parseInt(cleanPrice);
        }
    }

    public double calculateDiscount(int oldPrice, int newPrice) {
        double difference = oldPrice - newPrice;
        return (((difference*100)/oldPrice)/100)+0.01;
    }

    public int randomInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public int calculatePrice(int oldPrice, double discount) {
        double discountedPrice = oldPrice*discount;
        return (int) (oldPrice-discountedPrice);
    }

}
