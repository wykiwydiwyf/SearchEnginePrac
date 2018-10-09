package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import java.util.HashSet;

public class ReturnValue  {

    private static String WarnText = new String();
    private static LinkedList<Hyperlink> ResultAnkleList = new LinkedList<>();
    public static String ReturnWarn(TextField input){
        String searchQuery = input.getText();
        if (searchQuery.length() == 0) {  // textField does not handle (userInput != null)
            WarnText=("Please enter a search query");
            return WarnText;
        }


        try {
            HashSet<Document> results = Searcher.search(searchQuery);


            if (results.size() == 0) {
                String tempText=new String();
                for (int i =0;i<showSimilarWords(searchQuery).size();i++) {
                    tempText += showSimilarWords(searchQuery).get(i);
                }
                WarnText=("The search did not find any results for '" + searchQuery + "'"+"\n ..Do you mean..: "+tempText);
                return WarnText;
            }
            WarnText=("Search Results for "+searchQuery+": \n");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return WarnText;
    }

    public static LinkedList<Hyperlink> ReturnAnkle(TextField input){
        long start = System.currentTimeMillis(); // Search time count start
        String searchQuery = input.getText();
        try {
        HashSet<Document> results = Searcher.search(searchQuery);
        for (Document result : results) {// for-each loop through the result and append
            ResultAnkleList.add(new Hyperlink(String.format(result.get("url"))+"\n"));
        }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ResultAnkleList;
    }
    private static final String INDEX_DIR = "data/index";

    public static LinkedHashMap BuildSimilarWordLib()throws Exception{
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        LinkedHashMap<String, Integer> wordLib = new LinkedHashMap<>();
        IndexReader reader = DirectoryReader.open(dir);

        for (int i=0; i<reader.maxDoc(); i++) {
            Document doc = reader.document(i);
            String[] words = (doc.toString()).split("\\t|,|;|\\.|\\?|!|\\-|\\+|=|:|@| |\\[|\\]|\\(|\\)|\\{|\\}|_|\\*|/");

            for (String word:words){
                word = word.toLowerCase();
                if (wordLib.containsKey(word)) {
                    wordLib.put(word,
                            wordLib.get(word) + 1);
                } else {
                    wordLib.put(word, 1);
                }
            }
        }
        return wordLib;

    }

    /**
     * show similar words
     * @param searchQuery
     */
    private static List<String> showSimilarWords(String searchQuery) throws Exception {
        List<String>SimilarWordList = new LinkedList<>();
        LinkedHashMap hashMap = BuildSimilarWordLib();
        String[]searchQueryWords=searchQuery.split(" ");
        HashSet<String> similarWords = SimilarWords.retrieveSimilarWords(hashMap,searchQueryWords);
        if (!similarWords.isEmpty()) { // If there are no similar words, don't try to display them
            for (String similarWord: similarWords) {
                SimilarWordList.add(similarWord );
            }
        }
        return SimilarWordList;
    }
}
