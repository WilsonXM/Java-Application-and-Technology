import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.pdfbox.text.PDFTextStripper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexFiles {

    private IndexWriter writer; //写索引实例
    private List<File> htmlNeedIndex = null; //所有需要建立索引的文件

    private List<File> pdfNeedIndex = null; //所有需要建立索引的PDF文件

    //构造方法，实例化IndexWriter
    public IndexFiles(String indexDir) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        Analyzer analyzer = new StandardAnalyzer(); //标准分词器，会自动去掉空格啊，is a the等单词
        IndexWriterConfig config = new IndexWriterConfig(analyzer); //将标准分词器配到写索引的配置中
        writer = new IndexWriter(dir, config); //实例化写索引对象
        htmlNeedIndex = new ArrayList<>();
        pdfNeedIndex = new ArrayList<>();
    }

    //关闭写索引
    public void close() throws Exception {
        writer.close();
    }

    //索引指定目录下的所有文件
    public int indexAll(File dataDir) throws Exception {
        //获取该路径下的所有文件
        if (dataDir.isDirectory())
            // 获取目录下的所有文件
            findAll(dataDir);
        else
            System.out.println("The specified path does not represent a directory.");

        if (htmlNeedIndex != null)
            for (File file : htmlNeedIndex)
                indexHtmlFile(file); //调用下面的indexFile方法，对每个文件进行索引
        if (pdfNeedIndex != null)
            for (File file : pdfNeedIndex)
                indexPDFFile(file);
        return writer.numRamDocs(); //返回索引的文件数
    }

    public void findAll(File directory) {
        // 获取目录下的所有文件和子目录
        File[] files = directory.listFiles();
        // 打印文件的绝对路径
        if (files != null) {
            for (File file : files)
                if (file.isDirectory())
                    // 如果是子目录，递归调用 findAll
                    findAll(file);
                else
                    // 如果是html文件，将其加入所有需要被建索引的文件数组变量中
                    if ( file.getName().substring(file.getName().lastIndexOf(".")).equals(".html") )
                        htmlNeedIndex.add(file);
                    else if( file.getName().substring(file.getName().lastIndexOf(".")).equals(".pdf") )
                        pdfNeedIndex.add(file);
        } else {
            System.out.println("No files or directories in the specified directory.");
        }
    }

    //索引指定的文件
    private void indexHtmlFile(File file) throws Exception {
        // System.out.println("索引文件的路径：" + file.getCanonicalPath());
        Document doc = getHTMLDocument(file); //获取该文件的document
        writer.addDocument(doc); //调用下面的getDocument方法，将doc添加到索引中
    }

    private void indexPDFFile(File file) throws Exception {
        Document doc = getPDFDocument(file);
        writer.addDocument(doc);
    }

    private Document getPDFDocument(File file) throws Exception {
        Document doc = new Document();
        org.jsoup.nodes.Document originalFile = Jsoup.parse(file, "UTF-8", "");
        //添加每篇论文中的图表
        PDDocument pddocument = PDDocument.load(file);
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String pdfText = pdfTextStripper.getText(pddocument);
        Pattern pattern = Pattern.compile( "(Figure|Table) \\d+:(.*?)($|[.])");
        Matcher matcher = pattern.matcher(pdfText);
        StringBuilder res = new StringBuilder();
        while (matcher.find())
            res.append(matcher.group());
        doc.add(new TextField("Graphs", res.toString(), Field.Store.YES));
        pddocument.close();
        //添加文件路径
        doc.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        return doc;
    }

    //获取文档，文档里再设置每个字段，就类似于数据库中的一行记录
    private Document getHTMLDocument(File file) throws Exception {
        Document doc = new Document();
        org.jsoup.nodes.Document originalFile = Jsoup.parse(file, "UTF-8", "");
        //添加字段
        //添加文件名，并把这个字段存到索引文件里
        doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
        //添加每篇论文的作者
        StringBuilder strings = new StringBuilder();
        Elements elements = originalFile.select(".lead");
        for (Element name : elements)
            strings.append(name.text()).append(" ");
            //System.out.println(name.text());
        doc.add(new TextField("Author", strings.toString().trim(), Field.Store.YES));
        strings.setLength(0);
        elements.empty();
        //添加每篇论文的标题
        Element element = originalFile.select("h2#title").first();
        doc.add(new TextField("Title", element.text(), Field.Store.YES));
        element.empty();
        //添加每篇论文的摘要
        elements = originalFile.select("div.card-body.acl-abstract");
        for (Element ab : elements) {
            ab.select("h5").remove();
            strings.append(ab.text());
        }
        doc.add(new TextField("Abstract", strings.toString().trim(), Field.Store.YES));
        strings.setLength(0);
        elements.empty();
        //添加每篇论文的会议
        element = originalFile.select("dt:contains(Venue)").first().nextElementSiblings().select("dd").first();
        doc.add(new TextField("Venue", element.text(), Field.Store.YES));

        //添加文件路径
        doc.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        return doc;
    }

    public static void main(String[] args) {
        String indexDir = "D:\\Subjects\\JLP\\homework4\\Scholar-Search-Engine\\src\\main\\resources\\indexes"; //将索引保存到的路径
        String dataDir = "D:/Subjects/JLP/homework4/Scholar-Search-Engine/src/main/resources/sources"; //需要索引的文件数据存放的目录
        IndexFiles indexer = null;
        int indexedNum = 0;
        long startTime = System.currentTimeMillis(); //记录索引开始时间
        try {
            indexer = new IndexFiles(indexDir);
            indexedNum = indexer.indexAll(new File(dataDir));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                indexer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis(); //记录索引结束时间
        System.out.println("索引耗时" + (endTime - startTime) + "毫秒");
        System.out.println("共索引了" + indexedNum + "个文件");
    }
}