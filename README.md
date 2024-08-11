Here is the raw text version of the markdown (md) file:

```
# Web Scanner and PDF Converter

This Java program is designed to crawl web pages of a specific novel, store the content in an HTML file, and convert the content into a PDF file. The program uses several libraries, including `Jsoup` for web scraping, `JUnit` for testing, and `iText` (with `com.lowagie` package) for PDF generation.

## Features

- **Web Scraping**: Extracts content from specified URLs using `Jsoup`.
- **Content Processing**: Modifies the HTML structure by replacing specific tags (e.g., `<h3>` and `<h4>` tags are replaced with `<h2>` tags).
- **HTML File Storage**: Stores the extracted content into an HTML file using a predefined template.
- **PDF Conversion**: Converts the stored HTML content into a PDF file.

## Requirements

- **Java 8 or higher**
- **Maven/Gradle** (if using dependencies)
- **Jsoup**: For web scraping.
- **iText (com.lowagie)**: For PDF generation.
- **JUnit 5**: For unit testing.

## Setup and Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/webscanner.git
    cd webscanner
    ```

2. **Install Dependencies**:
    Ensure that you have `Jsoup`, `JUnit 5`, and `iText` (com.lowagie) libraries included in your project.

    If you're using Maven, add the following dependencies to your `pom.xml`:

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.14.3</version>
        </dependency>
        <dependency>
            <groupId>com.lowagie</groupId>
            <artifactId>itext</artifactId>
            <version>2.1.7</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.7.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    ```

3. **Run the tests**:
    You can execute the tests using your IDE or by running:
    ```bash
    mvn test
    ```

## Usage

### Web Crawling

The `webCrawl()` method scrapes content from a specified range of chapters of the novel. The content is extracted from the `<div id="chr-content">` section of the webpage, and the `<h3>` and `<h4>` tags are replaced with `<h2>` tags for consistent formatting.

### HTML Storage

The `storeContentsToHtmlFile(String content)` method saves the extracted content into an HTML file. It reads from a template HTML file (`template.html`), replaces the `[[TEMPLATE_BODY]]` placeholder with the content, and appends it to the target HTML file (`OverGeared.html`).

### PDF Conversion

The `pdfConvert()` method converts the stored HTML content into a PDF file. The HTML content is read from `OverGeared.html` and is then parsed into a PDF file using the `HTMLWorker` class from the `com.lowagie` package.

## Example

1. **Set the desired URL and chapters to crawl**:
    ```java
    String url = "https://fast.novelupdates.net/book/overgeared-novel/";
    int chapterNumberS = 1; // Start chapter
    int chapterNumberE = 2059; // End chapter
    ```

2. **Run the `webCrawl()` method** to scrape and store content:
    ```java
    webCrawl();
    ```

3. **Convert the HTML to PDF**:
    ```java
    pdfConvert();
    ```

## Notes

- Ensure that the template HTML file (`template.html`) contains the `[[TEMPLATE_BODY]]` placeholder where the content should be inserted.
- The `OverGeared.html` file will be appended with each chapter's content as the `webCrawl()` method iterates through the chapters.
- The PDF conversion will create a single `OverGeared.pdf` file with all the content from the `OverGeared.html` file.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- **Jsoup**: For providing a powerful web scraping library.
- **iText (com.lowagie)**: For the PDF generation capabilities.
- **JUnit 5**: For the unit testing framework.
```****
