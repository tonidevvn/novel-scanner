package webscanner.webscanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.StringReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainPageTest {
    // private WebDriver driver;
    // private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        //  ChromeOptions options = new ChromeOptions();
        //  // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
        //  options.addArguments("--remote-allow-origins=*");
        //  driver = new ChromeDriver(options);
        //  driver.manage().window().maximize();
        //  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        //
        //  mainPage = new MainPage(driver);
    }

    @AfterEach
    public void tearDown() {
        //driver.quit();
    }

    @Test
    public void webCrawl() throws IOException {
        String url = "https://fast.novelupdates.net/book/overgeared-novel/";
        String chapter = "chapter-";
        int chapterNumberS = 1; // change on your own
        int chapterNumberE = 2059; // end chapter

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

    public void pdfConvert() throws IOException {

        com.lowagie.text.Document document = new com.lowagie.text.Document();
        PdfWriter.getInstance(document, new FileOutputStream("./html_files/OverGeared.pdf"));

        String targetFile = "./html_files/OverGeared.html";
        String htmlContents = new String(Files.readAllBytes(Paths.get(targetFile)));

        document.open();
        HTMLWorker htmlWorker = new HTMLWorker(document);
        htmlWorker.parse(new StringReader(htmlContents));
        document.close();

        System.out.println("PDF created successfully.");
    }

    public String getNovelContentsFromUrl(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .timeout(5000).get();

            Element mainContent = doc.selectFirst("#chr-content");
            // Regular expression to replace all <h3> & <h4> tags with <h2> tags
            String h3ToH2PatternOpen = "<h3>";
            String h3ToH2PatternClose = "</h3>";
            String h4ToH2PatternOpen = "<h4>";
            String h4ToH2PatternClose = "</h4>";
            assert mainContent != null;
            String htmlContent = mainContent.html();
            htmlContent = htmlContent.replaceAll(h3ToH2PatternOpen, "<h2>");
            htmlContent = htmlContent.replaceAll(h3ToH2PatternClose, "</h2>");
            htmlContent = htmlContent.replaceAll(h4ToH2PatternOpen, "<h2>");
            htmlContent = htmlContent.replaceAll(h4ToH2PatternClose, "</h2>");

            return htmlContent;
        } catch (IOException ignored) {
        }
        return ""; // empty
    }

    public void storeContentsToHtmlFile(String content)  throws IOException {
        String templateFile = "./html_files/template.html";
        String targetFile = "./html_files/OverGeared.html";
        String placeholder = "[[TEMPLATE_BODY]]";

        // Read the template file
        StringBuilder templateContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(templateFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                templateContent.append(line).append(System.lineSeparator());
            }
        }

        // Replace the placeholder with the provided content
        String finalContent = templateContent.toString().replace(placeholder, content);

        // Write the final content to the target file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true))) {
            writer.write(finalContent);
        }
    }
}
