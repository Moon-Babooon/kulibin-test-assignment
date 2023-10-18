package testcases;

import base.DataHolder;
import base.DriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.StorePage;
import utils.Utils;
import java.util.List;
import java.util.Objects;

public class TestAssignment extends DriverSetup {

    public Utils u = new Utils();
    public StorePage store = new StorePage();
    public SoftAssert softAssert = new SoftAssert();

    @BeforeMethod
    public void precondition() {
        By LANGUAGE_SELECT_WINDOW = By.className("fancybox-inner");
        By LANG_LIST = By.cssSelector(".modal-form div>a.btn-yellow");
        driver.get("about:blank");
        driver.get("https://kulibin.com.ua");
        if (driver.findElement(LANGUAGE_SELECT_WINDOW).isDisplayed()) {
            List<WebElement> langs = driver.findElements(LANG_LIST);
            langs.get(1).click();
        }
    }

    @Test
    public void discountPriceTest() {
        By ELECTROINSTRUMENT = By.xpath("//a[@href='/catalog/elektroinstrument/']");
        By CATEGORY_LIST = By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > ul:nth-child(2) > li:nth-child(3) > div:nth-child(2) > div:nth-child(1) > ul > li > a");
        By DISCOUNTED_LIST = By.xpath("//span[@class='old-price']//ancestor::li");
        By PRICE = By.xpath("//span[@class='old-price']//ancestor::div[@class='wrap']/span");

        // Hover over the category
        u.moveToElement(u.getElement(ELECTROINSTRUMENT));
        u.waitUntilVisibilityOfAllElements(CATEGORY_LIST, 10L);
        List<WebElement> categoryList = u.getElements(CATEGORY_LIST);
        // Click on the desired category
        for (WebElement element : categoryList) {
            if (element.getAttribute("href").equals("https://kulibin.com.ua/catalog/dreli/")) {
                u.waitUntilClickable(element, 10L);
                element.click();
                break;
            }
        }
        // Amount of products
        int numOfItems = 3;

        for (int i=0; i < store.getListOfItems(DISCOUNTED_LIST, numOfItems).size(); i++) {

            By INSIDE_PRICE_TAG = By.xpath("//div[@class='price-row']/span");
            // Getting the old price tags and the new price tags for every prod.card
            List<WebElement> priceTags = store.getListOfItems(PRICE, numOfItems*2);
            // Storing integer values of the price tags from the cards
            int oldCardPrice = store.priceToNumber(priceTags.get(i*2).getText());
            int newCardPrice = store.priceToNumber(priceTags.get(1+(i*2)).getText());
            // Clicking on the next card
            WebElement card = store.getListOfItems(DISCOUNTED_LIST, numOfItems).get(i);
            u.scrollIntoView(card);
            card.click();
            List<WebElement> insidePriceTag = store.getListOfItems(INSIDE_PRICE_TAG);
            // Storing integer values of the price tags from the product page
            int oldPagePrice = store.priceToNumber(insidePriceTag.get(0).getText());
            int newPagePrice = store.priceToNumber(insidePriceTag.get(1).getText());
            // Assertions:
            softAssert.assertTrue(insidePriceTag.get(0).isDisplayed() && insidePriceTag.get(1).isDisplayed());
            softAssert.assertEquals(oldPagePrice, oldCardPrice);
            softAssert.assertEquals(newPagePrice, newCardPrice);
            softAssert.assertAll();

            driver.navigate().back();

        }
    }

}
