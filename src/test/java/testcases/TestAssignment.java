package testcases;

import base.DriverSetup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HomePage;
import pages.StorePage;
import utils.Utils;
import java.util.List;

public class TestAssignment extends DriverSetup {

    public Utils u = new Utils();
    public HomePage homePage = new HomePage();
    public StorePage store = new StorePage();
    By ELECTROINSTRUMENT = By.xpath("//a[@href='/catalog/elektroinstrument/']");
    By DISCOUNTED_LIST = By.xpath("//span[@class='old-price']//ancestor::li");
    By PRICE = By.xpath("//span[@class='old-price']//ancestor::div[@class='wrap']/span");
    By INSIDE_PRICE_TAG = By.xpath("//div[@class='price-row']/span");

    @BeforeMethod
    public void precondition() {
        homePage.loadPage();
        driver.manage().window().maximize();
        homePage.chooseLanguage("ru");
    }

    @Test
    public void priceTest() {
        SoftAssert softAssert = new SoftAssert();
        // Hover over the category
        u.moveToElement(u.getElement(ELECTROINSTRUMENT));
        homePage.selectCategoryByName("Дрели");
        // Amount of items to verify
        int numOfItems = 3;
        for (int i=0; i < store.getListOfItems(DISCOUNTED_LIST, numOfItems).size(); i++) {
            // Getting the old price tags and the new price tags for every prod.card
            List<WebElement> priceTags = store.getListOfItems(PRICE, numOfItems*2);
            // Storing integer values of the price tags from the cards
            int cardPriceOld = u.priceToNumber(priceTags.get(i*2).getText());
            int cardPriceNew = u.priceToNumber(priceTags.get(1+(i*2)).getText());
            // Clicking on the next card
            WebElement card = store.getListOfItems(DISCOUNTED_LIST, numOfItems).get(i);
            u.scrollIntoView(card);
            card.click();
            List<WebElement> insidePriceTag = store.getListOfItems(INSIDE_PRICE_TAG);
            // Storing integer values of the price tags from the product page
            int pagePriceOld = u.priceToNumber(insidePriceTag.get(0).getText());
            int pagePriceNew = u.priceToNumber(insidePriceTag.get(1).getText());
            // Assertions:
            softAssert.assertTrue(insidePriceTag.get(0).isDisplayed() && insidePriceTag.get(1).isDisplayed());
            softAssert.assertEquals(pagePriceOld, cardPriceOld);
            softAssert.assertEquals(pagePriceNew, cardPriceNew);
            softAssert.assertAll();
            driver.navigate().back();
        }
    }

    @Test
    public void discountLabelTest() {
        SoftAssert softAssert = new SoftAssert();
        u.moveToElement(u.getElement(ELECTROINSTRUMENT));
        homePage.selectCategoryByName("Перфораторы");
        Document document = u.getHTML();
        Elements discountList = document.select(".js-product").select(".old-price");
        softAssert.assertTrue(!discountList.isEmpty());
        softAssert.assertAll();
    }

    @Test
    public void creditLabelsTest() {
        String cardSelector = ".js-product";
        String creditSelector = ".info-wrap__credit-opener>img:first-child";
        String titleSelector = ".s_title>a>span";
        u.moveToElement(u.getElement(ELECTROINSTRUMENT));
        homePage.selectCategoryByName("Болгарки");
        for (int i=0; i < 3; i++) {
            Document document = u.getHTML();
            for (Element e : document.select(cardSelector)) {
                if (e.select(creditSelector).hasAttr("src")) {
                    System.out.println(e.select(titleSelector).text());
                }
            }
            store.gotoNextPage();
        }
    }

    @Test
    public void discountPriceTest() {
        SoftAssert softAssert = new SoftAssert();
        u.moveToElement(u.getElement(ELECTROINSTRUMENT));
        homePage.selectCategoryByName("Шуруповерты");
        for (int i=0; i < 10; i++) {
            List<WebElement> discountedList = u.getElements(DISCOUNTED_LIST);
            WebElement card = discountedList.get(u.randomInt(1, discountedList.size()-1));
            u.scrollIntoView(card);
            card.click();
            Document document = u.getHTML();
            Elements priceTag = document.selectXpath("//div[@class='price-row']");
            Elements name = document.select("h1[itemprop='name']");
            for (Element tag : priceTag) {
                String productName = name.text();
                int oldPrice = u.priceToNumber(tag.select(".item_old_price").text());
                int newPrice = u.priceToNumber(tag.select(".price").text());
                double discount = u.calculateDiscount(oldPrice, newPrice);
                int expectedPrice = u.calculatePrice(oldPrice, discount);
                softAssert.assertEquals(newPrice, expectedPrice, "Item "+productName+ " has incorrect price.");
                driver.navigate().back();
            }
            store.gotoNextPage();
        }
        softAssert.assertAll();
    }

}
