import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

class Product {

    Product(WebElement we, char currency) {
        rel = we;
        this.currency = currency;
        fillThePrice();
        fillNameAndId();

    }
    private void fillThePrice() {
        price = Float.parseFloat(rel.findElement(By.cssSelector("span.price")).getText().replace(',','.').replace(currency,' '));
        if(rel.findElement(By.cssSelector("div.product-price-and-shipping")).getText().contains("\n")) {
            isDiscounted = true;
            regularPrice = Float.parseFloat(rel.findElement(By.className("regular-price")).getText().replace(',','.').replace(currency,' '));
            discount =  Float.parseFloat(rel.findElement(By.className("discount-percentage")).getText().replace('-',' ').replace('%',' '));
        }

    }
    private void fillNameAndId() {
        productName = rel.findElement(By.className("h3 product-title")).getText();
        innerId = Integer.parseInt(rel.findElement(By.className("h3 product-title")).getAttribute("data-id-product"));
    }
    public void consoleOutElement() {
        if (isDiscounted)
        {
            System.out.println("\n!!!DISCOUNT!!!\nOld Price: \t" + regularPrice + "\nDiscount: \t" + discount + "%");
        }
        System.out.println("Price = \t" + price + '\n');
        System.out.println("Name = \t" + productName +'\n');
        System.out.println("ProductID = \t" + innerId + '\n');
    }


    WebElement rel;
    float price;
    boolean isDiscounted;
    float discount;
    float regularPrice;
    int innerId;
    String productName;
    char currency;

}