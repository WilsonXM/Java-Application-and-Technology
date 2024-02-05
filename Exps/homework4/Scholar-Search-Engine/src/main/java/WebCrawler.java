import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;

public class WebCrawler {
    public static void main(String[] args) {

        String path = "D:/Subjects/JLP/homework4/Scholar-Search-Engine/src/main/resources/sources";

        List<String> pdfLinks = parseAndStoreWebContent("https://aclanthology.org/", path);

        downloadAndSavePdfFiles(pdfLinks, path);
    }

    public static List<String> parseAndStoreWebContent(String url, String path) {
        List<String> PDFlinks = new ArrayList<>();
        try {
            // 通过循环爬取100篇论文的网站
            for (int i = 0; i < 100; i++) {
                // construct the specific url of each paper
                String paperurl = url + "2023.acl-long." + i + "/";

                // get the html content of from the url
                Document document = Jsoup.connect(paperurl).get();
                System.out.println(document.title());

                // store the html content into local file
                String filepath = path + "/" + i + "/webcontent-" + i + ".html";
                Path Path = Paths.get(filepath);
                Path directory = Path.getParent();
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
                writer.write(document.html());

                // get the PDFlink of the paper
                Elements links = document.select("a[href]");
                for (Element link : links) {
                    String href = link.attr("href");
                    if (href.endsWith(".pdf") && !PDFlinks.contains(href)) {
                        System.out.println(href);
                        PDFlinks.add(href);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PDFlinks;
    }

    private static void downloadAndSavePdfFiles(List<String> pdfLinks, String path) {
        try {
            int i = 0;
            for (String link : pdfLinks) {
                // 从 URL 中提取文件名并构建路径
                String fileName = link.substring(link.lastIndexOf("/") + 1);
                String downloadDirectory = path + "/" + i;
                Path filePath = Paths.get(downloadDirectory, fileName);
                BufferedInputStream in = new BufferedInputStream(new URL(link).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(filePath.toString());
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                i++;
            }

            System.out.println("PDF downloaded successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}