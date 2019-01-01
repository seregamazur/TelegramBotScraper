package utils.scrapper.hotline;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scrapper implements ScrapperSelects {
    public static void main(String[] args) {
        Scrapper.scrap("https://hotline.ua/sales/?type=discount&section=12", "#page-sales > div.cell-fixed-indent.cell-md > div > div.pagination");
    }

    public static void scrap(String URL, String PAGINATOR) {
        int lastPage;
        //initialization webdriver with chrome
        WebDriver driver = chromeDriver();
        StringBuilder sb = new StringBuilder();
        //result hashmap with goods
        HashMap<String, String> goods = new HashMap<>();
        //checkouf if url is OK(contains num of page)
        String TRUE_URL = pageInside(URL);
        //connect to URL
        driver.navigate().to(TRUE_URL);
        try {
            //find paginator and detect numb of pages
            WebElement paginator = driver.findElement(By.cssSelector(PAGINATOR));
            String a = paginator.getText().replaceAll("\n", " ");
            if (a.contains("...")) a = a.replaceAll("...", " ");
            String arr[] = a.split(" ");
            lastPage = Integer.valueOf(arr[arr.length - 1]) + 1;
        } catch (NoSuchElementException ne) {
            lastPage = 1;
        }
        //scraping
        for (int j = userPage(TRUE_URL); j < lastPage; j++) {
            driver.navigate().to(checkedURL(TRUE_URL, j));
            for (int i = 0; i < driver.findElements(By.className("product-item")).size(); i++) {
                try {
                    WebElement elementName = driver.findElement(By.cssSelector(ScrapperSelects.SELECTOR_NAME(i)));
                    WebElement elementPrice = driver.findElement(By.cssSelector(ScrapperSelects.SELECTOR_PRICE(i)));
                    sb.append(elementName.getText().concat(" " + driver.findElement(By.cssSelector(ScrapperSelects.SELECTOR_URL_GOOD(i))).getText()));
                    goods.put(sb.toString(), elementPrice.getText());
                    sb.setLength(0);
                } catch (NoSuchElementException ignored) {
                }
            }
        }
        System.out.println(goods.entrySet());
        System.out.println(goods.keySet().size());
    }
    //return changed number of page while iteration is going
    private static String checkedURL(String URL, int i) {
        return URL.replaceAll("(\\d+)$", String.valueOf(i));
    }
    //connect chromedriver and his options(headless mode)
    private static ChromeDriver chromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/java/utils/scrapper/webdriver/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        return new ChromeDriver(chromeOptions);
    }
    //check if URL containt number of page
    private static String pageInside(String URL){
        if(URL.contains("page=")){
            return URL;}
        else {return URL.concat("&page=0"); }
    }
    //check if user has added his own page from where he wanna start to scrap
    private static int userPage(String URL){
        Pattern patt = Pattern.compile("(\\d+)$");
        Matcher matcher = patt.matcher(URL);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group()); // you can get it from desired index as well
        } else {
            return 0;
        }
    }

}