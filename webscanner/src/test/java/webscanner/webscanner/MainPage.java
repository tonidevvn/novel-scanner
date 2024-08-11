package webscanner.webscanner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

// page_url = https://novelbin.com/
public class MainPage {

    @FindBy(css = "[data-test='not-found-element']")
    public WebElement notFoundElement;

    @FindBy(id = "chr-content")
    public WebElement mainContent;

    @FindBy(id = "chr-content")
    public WebElement mainContent;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
