package pages;

import base.DriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StorePage extends DriverSetup {

    public List<WebElement> getListOfItems(By locator, int amount) {
        return driver.findElements(locator).subList(0, amount);
    }

    public List<WebElement> getListOfItems(By locator) {
        return driver.findElements(locator);
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
}
