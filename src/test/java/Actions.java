import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.testng.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public char currentCurrency;
    String[] ar = new String[10];
    private static final String PRODUCTS = "/html/body/main/section/div/div/section/section";

    //accepts only "USD" | "EUR" | "UAH"
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

    public char getActualCurrency()
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
            if(dropDownList.getText().contains("\u0024")) return '\u0024';
            if(dropDownList.getText().contains("\u20AC")) return '\u20AC';
            if(dropDownList.getText().contains("\u20B4")) return '₴';
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
        System.out.println(currentCurrency + " At all "+ foundProductPositions+ " positions");
       List foundElements = productList.findElements(By.className("product-price-and-shipping"));

       assertTrue(foundElements.size() == foundProductPositions);
    }

    protected void sortingCheck() {
        findProducts();
        ArrayList<WebElement> products = new ArrayList<WebElement>();
        currentCurrency = getActualCurrency();
        ArrayList<WebElement> newList = new ArrayList<WebElement>();

        products.addAll(driver.findElements(By.className("product-price-and-shipping")));
        for (WebElement i: products
             ) {
            newList.add(i.findElement(By.cssSelector("Span")));
        }
        for(int i = 1; i < newList.size(); i++) {
            System.out.println("product #"+i+ " costs "+Float.valueOf(newList.get(i-1).getText().replace(currentCurrency,' ').replace(',','.')));
            assertTrue(Float.valueOf(newList.get(i-1).getText().replace(currentCurrency,' ').replace(',','.')) >= Float.valueOf(newList.get(i).getText().replace(currentCurrency,' ').replace(',','.')));
        }
        System.out.println("product #"+newList.size()+" costs "+ Float.valueOf(newList.get(products.size()-1).getText().replace(currentCurrency,' ').replace(',','.')));

    }

    void discountCheck()
    {
        ArrayList<WebElement> products = new ArrayList<WebElement>(driver.findElements(By.cssSelector("article.product-miniature.js-product-miniature")));
        ArrayList<Product> positions = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
            positions.add(new Product(products.get(i), getActualCurrency()));
        }
        for (int i = 0; i < positions.size(); i++)
        {
            if(positions.get(i).isDiscounted)
            {
                assertTrue((positions.get(i).price - 0.5) <= (((positions.get(i).regularPrice) *(100 -positions.get(i).discount))/100)|((((positions.get(i).regularPrice) *(100 -positions.get(i).discount))/100) <= positions.get(i).price + 0.5));
                System.out.println("Discount "+ positions.get(i).discount +"% is Actual");
            }
        }
    }
// IDE push check
    protected void createFile() {
        try {
            File logFile = new File("log.txt");
            if (logFile.createNewFile()) {
                System.out.println("Created: " + logFile.getName()+ "\nAt: " + logFile.getPath());
            }
        }
        catch (IOException e) {
            System.out.println("En error occurred.");
            e.printStackTrace();
        }
    }


}


