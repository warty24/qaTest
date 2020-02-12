import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class Actions {
        Actions (WebDriver driver) {
            this.driver = driver;
        }
    private WebDriver driver;
    private String currencyUSD = "USD \u0024";
    private String currencyEUR = "EUR \u20AC";
    private String currencyUAH = "UAH \u20B4";
    int foundProductPositions;
    private List productList;
    private char currentCurrency;
    String[] ar = new String[10];
    private static final String PRODUCTS = "/html/body/main/section/div/div/section/section";

    protected void changeCurrencyTo(String currency)
    {
        Solution.wait.until(ExpectedConditions.elementToBeClickable(By.className("currency-selector")));
        assert (currency == "USD"||currency == "EUR"| currency == "UAH");
        WebElement dropDownList = driver.findElement(By.className("currency-selector"));
        dropDownList.click();
        if (currency == "USD") {
            try {
                Solution.wait.until(ExpectedConditions.elementToBeClickable(By.linkText(currencyUSD)));
            }
            catch (TimeoutException e) {
                driver.navigate().refresh();
            }
            finally {
                WebElement currencyChase = driver.findElement(By.linkText(currencyUSD));
                currencyChase.click();
                currentCurrency = '\u0024';
            }
        }
        else if (currency == "EUR"){
            try {
                Solution.wait.until(ExpectedConditions.elementToBeClickable(By.linkText(currencyEUR)));
            }
            catch (TimeoutException e) {
                driver.navigate().refresh();
            }
            finally {
                WebElement currencyChase = driver.findElement(By.linkText(currencyEUR));
                currencyChase.click();
                currentCurrency = '\u20AC';
            }
        }
        else {
            try {
                Solution.wait.until(ExpectedConditions.elementToBeClickable(By.linkText(currencyUAH)));
            }
            catch (TimeoutException e) {
                driver.navigate().refresh();
            }
            finally {
                WebElement currencyChase = driver.findElement(By.linkText(currencyUAH));
                currencyChase.click();
                currentCurrency = '\u20B4';
            }
        }

    }
    protected char getActualCurrency()
    {
        try {
            Solution.wait.until(ExpectedConditions.elementToBeClickable(By.className("currency-selector")));
        }
        catch (Exception e)
        {
            driver.navigate().refresh();
        }
        finally {
            WebElement dropDownList = driver.findElement(By.className("currency-selector"));
            System.out.println(dropDownList.getText());
            if(dropDownList.getText().contains("\u0024")) return '\u0024';
            if(dropDownList.getText().contains("\u20AC")) return '\u20AC';
            if(dropDownList.getText().contains("\u20B4")) return '\u20B4';
            else throw new RuntimeException("Could not get valid currency");
        }

    }
    protected void findProducts()
    {
        Solution.wait.until(ExpectedConditions.elementToBeClickable(By.className("thumbnail-container")));
        List<WebElement> priceList = driver.findElements(By.className("product-description"));
        productList = priceList;
        foundProductPositions = productList.size();
    }
    protected void isProductsCurrencyActual()
    {
        if (currentCurrency != '\u0024'| currentCurrency != '\u20AC'| currentCurrency != '\u20B4')currentCurrency = getActualCurrency();
        findProducts();
       WebElement productList = (driver.findElement(By.xpath(PRODUCTS)));
        System.out.println(foundProductPositions);
       List foundElements = productList.findElements(By.className("product-price-and-shipping"));

       assertTrue(foundElements.size() == foundProductPositions);
    }
    protected void sortingCheck() {
        findProducts();
     // WebElement productList = (driver.findElement(By.xpath(PRODUCTS)));
        ArrayList<WebElement> products = new ArrayList<WebElement>();
     // products.addAll(driver.findElements(By.className("regular-price")));
        currentCurrency = getActualCurrency();

        ArrayList<WebElement> newList = new ArrayList<WebElement>();

        products.addAll(driver.findElements(By.className("product-price-and-shipping")));
        for (WebElement i: products
             ) {
            newList.add(i.findElement(By.cssSelector("Span")));
        }
        for(int i = 1; i < newList.size(); i++) {
            System.out.println(Float.valueOf(newList.get(i-1).getText().replace(currentCurrency,' ').replace(',','.')));
            assertTrue(Float.valueOf(newList.get(i-1).getText().replace(currentCurrency,' ').replace(',','.')) >= Float.valueOf(newList.get(i).getText().replace(currentCurrency,' ').replace(',','.')));
        }
        System.out.println(Float.valueOf(newList.get(products.size()-1).getText().replace(currentCurrency,' ').replace(',','.')));

    }
    void discountCheck()
    {
        ArrayList<WebElement> products = new ArrayList<WebElement>();
        ArrayList<WebElement> newList = new ArrayList<WebElement>();
        ArrayList<WebElement> newList2 = new ArrayList<WebElement>();
        ArrayList<WebElement> newList3 = new ArrayList<WebElement>();
        products.addAll(driver.findElements(By.className("product-price-and-shipping")));
     /*
        for (WebElement i: products
             ) {
            try {
                newList.add(i.findElement(By.className("regular-price")));
                newList2.add(i.findElement(By.className("discount-percentage")));
            }
            catch (Exception e)
            {
                System.out.println("e = " + e.getCause());
            }

        }
     */
        newList.addAll(driver.findElements(By.className("regular-price")));
        newList2.addAll(driver.findElements(By.className("discount-percentage")));
        System.out.println(" W Discount: ");
     for (WebElement i: newList
             ) {
            System.out.println(i.getText());
        }
        System.out.println("\nDiscountSize: ");
        for (WebElement i: newList2
        ) {
            System.out.println(i.getText());
        }
        newList3.addAll(driver.findElements(By.className("price")));
        newList3.remove(driver.findElements(By.className("regular-price")));
        for (WebElement i: newList3
        ) {
            System.out.println(i.getText());
        }



    }

}
