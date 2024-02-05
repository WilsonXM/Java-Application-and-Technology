import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Paths;
import java.util.Scanner;

public class Searcher {

    public static void search(String indexDir, String type, String q) throws Exception {

        Directory dir = FSDirectory.open(Paths.get(indexDir)); //获取要查询的路径，也就是索引所在的位置
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer(); //标准分词器，会自动去掉空格啊，is a the等单词
        QueryParser parser = new QueryParser(type, analyzer); //查询解析器
        Query query = parser.parse(q); //通过解析要查询的String，获取查询对象

        long startTime = System.currentTimeMillis(); //记录索引开始时间
        TopDocs docs = searcher.search(query, 100);//开始查询，查询前10条数据，将记录保存在docs中
        long endTime = System.currentTimeMillis(); //记录索引结束时间
        System.out.println("匹配" + q + "共耗时" + (endTime - startTime) + "毫秒");
        System.out.println("查询到" + docs.totalHits + "条记录");

        int i = 1;
        for (ScoreDoc scoreDoc : docs.scoreDocs) { //取出每条查询结果
            Document doc = searcher.doc(scoreDoc.doc); //scoreDoc.doc相当于docID,根据这个docID来获取文档
            System.out.print(i++ + ". ");
            if (type.equals("Graphs"))
                System.out.println(doc.get("fullPath"));
            else
                System.out.println(doc.get("Title"));
        }
        reader.close();
    }

    private static boolean isValidInput(String input, String[] validStrings) {
        for (String validString : validStrings) {
            if (input.equals(validString)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String indexDir = "D:\\Subjects\\JLP\\homework4\\Scholar-Search-Engine\\src\\main\\resources\\indexes";
        Scanner input = new Scanner(System.in);
        String type, str;
        String[] validStrings = {"Author", "Title", "Abstract", "Venue", "Graphs"};
        System.out.println("====Scholar-Search-Engine====");
        System.out.println("Searching types available: ");
        System.out.println("Author    Title     Abstract    Venue    Graphs");
        System.out.print("Your choice is: ");
        type = input.nextLine();
        try {
            while (!type.equals("exit")) {
                if (isValidInput(type, validStrings)) {
                    System.out.print("Your prompt is: ");
                    str = input.nextLine();
                    if (!str.equals("exit")) {
                        search(indexDir, type, str);
                        System.out.println("Searching types available: ");
                        System.out.println("Author    Title     Abstract    Venue    Graphs");
                        System.out.print("Your choice is: ");
                        type = input.nextLine();
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Please choose from the types below: ");
                    System.out.println("Author    Title     Abstract    Venue    Graphs");
                    System.out.print("Your choice is: ");
                    type = input.nextLine();
                }
            }
            System.out.println("====Bye Bye====");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}