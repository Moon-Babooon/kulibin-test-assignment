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

        u.moveToElement(u.getElement(ELECTROINSTRUMENT));
        u.waitUntilVisibilityOfAllElements(CATEGORY_LIST, 10L);
        List<WebElement> categoryList = u.getElements(CATEGORY_LIST);

        for (WebElement element : categoryList) {
            if (element.getAttribute("href").equals("https://kulibin.com.ua/catalog/dreli/")) {
                u.waitUntilClickable(element, 10L);
                element.click();
                break;
            }
        }

        int numOfItems = 3;

        for (int i=0; i < store.getListOfItems(DISCOUNTED_LIST, numOfItems).size(); i++) {

            By INSIDE_PRICE_TAG = By.xpath("//div[@class='price-row']/span");

            List<WebElement> priceTags = store.getListOfItems(PRICE, numOfItems*2);
            DataHolder.getInstance().put("old", store.priceToNumber(priceTags.get(i*2).getText()));
            DataHolder.getInstance().put("new", store.priceToNumber(priceTags.get(1+(i*2)).getText()));
            WebElement card = store.getListOfItems(DISCOUNTED_LIST, numOfItems).get(i);
            u.scrollIntoView(card);
            card.click();
            List<WebElement> insidePriceTag = store.getListOfItems(INSIDE_PRICE_TAG);
            int oldPrice = store.priceToNumber(insidePriceTag.get(0).getText());
            int newPrice = store.priceToNumber(insidePriceTag.get(1).getText());
            softAssert.assertTrue(insidePriceTag.get(0).isDisplayed() && insidePriceTag.get(1).isDisplayed());
            softAssert.assertEquals(oldPrice, DataHolder.getInstance().get("old"));
            softAssert.assertEquals(newPrice, DataHolder.getInstance().get("new"));
            softAssert.assertAll();
            driver.navigate().back();

        }
    }

}
