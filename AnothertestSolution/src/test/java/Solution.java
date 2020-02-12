import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;


public class Solution {
    WebDriver driver;
    static WebDriverWait wait;
    Actions actions;

    private char currentCurrency;

    public static final String MAIN_PAGE = "http://prestashop-automation.qatestlab.com.ua/ru";
    public static final String SEARCH_KEYWORDS = "dress";
    public static final String POPULAR_PRODUCT_SECTION = "/html/body/main/section/div/div/section/section/section/div";
    public static final String FOUND_PRODUCT_POSITIONS = "/html/body/main/section/div/div/section/section/div[1]/div/div[1]/p";
    public static final String SEARCH_FIELD = "/html/body/main/header/div/div/div[1]/div[2]/div/div[2]/form/input[2]";
    public static final String SORT_BUTTON = "//*[@id=\"js-product-list-top\"]/div[2]/div/div/a";
    public static final String HIGH_TO_LOW = "/html/body/main/section/div/div/section/section/div[1]/div/div[2]/div/div/div/a[5]";


    @BeforeTest

    public void start() {
        driver = new ChromeDriver();
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, 10);

        driver.get(MAIN_PAGE);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        assertTrue(driver.getCurrentUrl().contains("http://prestashop-automation.qatestlab.com.ua"));
    }

    @Test
    public void openMainPage1() {
        System.out.println("Started Successfully");
    }

    @Test
    public void checkCurrency2() {
        actions.isProductsCurrencyActual();
    }
    @Test
    public void setPriseToUSD3() {
        actions.changeCurrencyTo("USD");
        assertTrue(actions.getActualCurrency()== '\u0024');
    }
    @Test
    public void searchByKeywords4() {
        WebElement element = driver.findElement(By.xpath(SEARCH_FIELD));
        element.clear();
        element.sendKeys(SEARCH_KEYWORDS);
        element.submit();
        assertTrue(driver.getCurrentUrl().contains("search?"));
    }
    @Test
    public void searchAudition5() {
        searchByKeywords4();
        actions.findProducts();
        System.out.println(driver.findElement(By.xpath(FOUND_PRODUCT_POSITIONS)).getText());
        System.out.println(actions.foundProductPositions);
        assertTrue(driver.findElement(By.xpath(FOUND_PRODUCT_POSITIONS)).getText().contains("Товаров: "+ actions.foundProductPositions));
    }
    @Test
    public void newCheckCurrency6() {
        actions.isProductsCurrencyActual();
    }
    @Test
    public void productSorting7() {
        searchByKeywords4();
    driver.findElement(By.xpath(SORT_BUTTON)).click();
    driver.findElement(By.xpath(HIGH_TO_LOW)).click();
        System.out.println("Sorted!");
    }
    @Test
    public void sortingCheck8() {
        productSorting7();
        actions.sortingCheck();
    }
    @Test void discountCheck10() {
        actions.discountCheck();
    }
    @AfterTest
    public void thatsAllKiddies() {
       //driver.close();
    }

}
