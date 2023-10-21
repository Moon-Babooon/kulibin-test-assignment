package pages;

import base.DriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import utils.Utils;
import java.util.List;

public class HomePage extends DriverSetup {

    public Utils u = new Utils();

    public void loadPage() {
        driver.get("about:blank");
        driver.get("https://kulibin.com.ua");
    }

    public void chooseLanguage(String language) {
        By LANGUAGE_SELECT_WINDOW = By.className("fancybox-inner");
        By LANG_LIST = By.cssSelector(".modal-form div>a.btn-yellow");
        try {
            List<WebElement> langs = u.getElements(LANG_LIST);
            if (u.getElement(LANGUAGE_SELECT_WINDOW).isDisplayed()) {
                if (language.equalsIgnoreCase("ru"))
                    langs.get(1).click();
                else if (language.equalsIgnoreCase("ua"))
                    langs.get(0).click();
            }
        } catch (TimeoutException e) {
            e.getStackTrace();
        }
    }

    public void selectCategoryByLink(String href) {
        By CATEGORY_LIST = By.cssSelector("ul.sub-nav>li .col>ul>li>a");
        u.waitUntilVisibilityOfAllElements(CATEGORY_LIST, 10L);
        List<WebElement> categoryList = u.getElements(CATEGORY_LIST);
        // Click on the desired category
        for (WebElement element : categoryList) {
            if (element.getAttribute("href").equals(href)) {
                u.waitUntilClickable(element, 5L);
                element.click();
                break;
            }
        }
    }

    public void selectCategoryByName(String name) {
        By CATEGORY_LIST = By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > ul:nth-child(2) > li:nth-child(3) > div:nth-child(2) > div:nth-child(1) > ul > li > a");
        u.waitUntilVisibilityOfAllElements(CATEGORY_LIST, 10L);
        List<WebElement> categoryList = u.getElements(CATEGORY_LIST);
        // Click on the desired category
        for (WebElement element : categoryList) {
            if (element.getText().equals(name)) {
                u.waitUntilClickable(element, 5L);
                element.click();
                break;
            }
        }
    }
}
