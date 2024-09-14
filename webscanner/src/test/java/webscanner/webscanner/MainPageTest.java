package webscanner.webscanner;

import com.lowagie.text.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.io.StringReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;
    private HashMap<String, String> visitedHistory;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("--headless");  // Run Chrome in headless mode

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        mainPage = new MainPage(driver);
        visitedHistory = new HashMap<>();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    public static String getChapterNumber(String url) {
        // Split the URL by '/'
        if (url == null || url.isEmpty())
            return "";

        String[] fsegments = url.split("/");
        // Get the last segment which is expected to be the chapter number
        String chapterPartInUrl = fsegments[fsegments.length - 1];
        // Split the URL by '-'
        String[] segments = chapterPartInUrl.split("-");

        // Get the last segment which is expected to be the chapter number
        String lastSegment = segments != null && segments.length > 1 ? segments[1] : "";

        // Check if the last segment is a number
        if (lastSegment.matches("\\d+")) {
            return lastSegment;  // Return the chapter number
        } else {
            return "";
        }
    }

    @Test
    public void webCrawl() throws IOException, InterruptedException {
        String url = "https://novelfire.docsachhay.net/book/overgeared/";
        int chapterS = 1845;
        String chapterNumberS = "chapter-" + chapterS; // change on your own
        int chapterE = 1879;
        String chapterNumberE = "chapter-" + chapterE; // end chapter

        // get the beginning chapter
        String content = "";

        // Continue reading chapters until there's no next chapter button or it's not clickable
        String currUrl = url + chapterNumberS;
        while (true) {
            String chapNumber = getChapterNumber (currUrl);

            if (!visitedHistory.containsKey(currUrl)) {
                System.out.println("-- READ CONTENT OF CHAPTER -- " + chapNumber);
                content = "<h2>Chapter " + chapNumber + "</h2>";
                // Extract content of the current chapter
                content += getNovelContentsFromUrl(currUrl);

                visitedHistory.put(currUrl, chapNumber);
                storeContentsToHtmlFile(content);
                System.out.println("-- FINISH READING CHAPTER -- " + chapNumber);
            }

            if (chapNumber.equals(chapterE + ""))
                break;

            // Try to locate the "Next Chapter" button
            String newUrl = "";
            try {
                WebElement nextButton = mainPage.nextChapter;
                newUrl = nextButton.getAttribute("href");
            } catch (Exception e) {
                // do nothing
            }

            if (newUrl == null || newUrl.isEmpty()) {
                break;  // Exit loop if there's no href in the 'Next Chapter' button
            } else {
                currUrl = newUrl;
            }

            // Wait for the page to load before proceeding
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        }

        applyTemplate();

        assertTrue(true);
    }

    private void applyTemplate() throws IOException {
        String templateFile = "./html_files/template.html";
        String targetFile = "./html_files/OverGeared.html";
        String htmlContents = new String(Files.readAllBytes(Paths.get(targetFile)));
        String placeholder = "[[TEMPLATE_BODY]]";

        // Read the template file
        StringBuilder templateContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(templateFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                templateContent.append(line).append(System.lineSeparator());
            }

            // Replace the placeholder with the provided content
            String finalContent = templateContent.toString().replace(placeholder, htmlContents);

            // Write the final content to the target file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
                writer.write(finalContent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void pdfConvert() throws IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("./html_files/OverGeared.pdf"));

        String targetFile = "./html_files/OverGeared.html";
        String htmlContents = new String(Files.readAllBytes(Paths.get(targetFile)));

        document.open();
        HTMLWorker htmlWorker = new HTMLWorker(document);
        htmlWorker.parse(new StringReader(htmlContents));
        document.close();

        System.out.println("PDF created successfully.");
    }

    public String getNovelContentsFromUrl(String url) throws InterruptedException {
        // Get the HTML content of the element
        StringBuilder htmlContent = new StringBuilder();

        try {
            // Navigate to the given URL
            driver.get(url);
            // Wait for the page to load before proceeding
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            // Find the element with id #chr-content
            WebElement mainContent = mainPage.mainContent;

            // iterate child nodes
            List<WebElement> ptags = mainContent.findElements(By.cssSelector("p"));
            for ( WebElement i : ptags) {
                htmlContent.append(i.getAttribute("outerHTML"));
            }
        } catch (Exception e) {// ERROR DETECTS
        }

        return "<div>" + htmlContent.toString() + "</div>";
    }

    public void storeContentsToHtmlFile(String content)  throws IOException {
        String targetFile = "./html_files/OverGeared.html";

        // Write the final content to the target file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true))) {
            writer.write(content);
        }
    }
}
