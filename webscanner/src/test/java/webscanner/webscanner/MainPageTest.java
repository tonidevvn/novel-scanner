package webscanner.webscanner;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        mainPage = new MainPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void webCrawl() throws IOException {
        String url = "https://fast.novelupdates.net/book/overgeared-novel/";
        String chapter = "chapter-";
        int chapterNumberS = 1526; // change on your own
        int chapterNumberE = 1528; // change on your own

        int index = chapterNumberS;
        do {
            try {
                String contents = getNovelContentsFromUrl(url + chapter + index);
                storeContentsToHtmlFile(contents);
                System.out.println("-- READING HTML BODY OF CHAPTER -- " + index);
            } catch (Exception e) {
                System.out.println("-- ENCOUNTER ERROR AT CHAPTER -- " + index);
            }
            index ++;

        } while (index <= chapterNumberE);

        assertTrue(true);
    }

    public String getNovelContentsFromUrl(String url) {
        driver.get(url);
        StringBuilder htmlContent = new StringBuilder();

        List<WebElement> paragraphElements = mainPage.mainContent.findElements(By.tagName("p"));
        for (WebElement p : paragraphElements) {
            htmlContent.append(p.getAttribute("outerHTML"));
        }

        // Regular expression to replace all <h3> & <h4> tags with <h2> tags
        String h3ToH2PatternOpen = "<h3>";
        String h3ToH2PatternClose = "</h3>";
        String h4ToH2PatternOpen = "<h4>";
        String h4ToH2PatternClose = "</h4>";
        htmlContent = new StringBuilder(htmlContent.toString().replaceAll(h3ToH2PatternOpen, "<h2>"));
        htmlContent = new StringBuilder(htmlContent.toString().replaceAll(h3ToH2PatternClose, "</h2>"));
        htmlContent = new StringBuilder(htmlContent.toString().replaceAll(h4ToH2PatternOpen, "<h2>"));
        htmlContent = new StringBuilder(htmlContent.toString().replaceAll(h4ToH2PatternClose, "</h2>"));

        return htmlContent.toString();
    }

    public void storeContentsToHtmlFile(String content)  throws IOException {
        String targetFile = "./html_files/overgeared.html";

        // Append content to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true))) {
            writer.write(content);
        }
    }
}
