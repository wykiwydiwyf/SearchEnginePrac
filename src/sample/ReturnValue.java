package sample;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.application.Application;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import java.util.HashSet;
import java.util.List;

public class ReturnValue   {

    private static String WarnText = new String();
    private HostServices hostServices ;
    public HostServices getHostServices() {
        return hostServices ;
    }
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices ;
    }



    public static String ReturnWarn(TextField input){
        String searchQuery = input.getText();
        if (searchQuery.length() == 0) {  // textField does not handle (userInput != null)
            WarnText=("Please enter a search query");
            return WarnText;
        }


        try {
            LinkedList<Document> results = Searcher.search(searchQuery);


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

    public static LinkedList<String> ReturnAnkle(TextField input) throws Exception {
        String searchQuery = input.getText();
        LinkedList<Document> results = Searcher.search(searchQuery);
        LinkedList<String>listAnkle = new LinkedList<>();
        for (Document result : results) {
            String urlText = new URL(result.get("url")).getPath();
            //Get word from url
            urlText = urlText.replaceAll("[/\\-_]"," ");
            //Capitalize First Letter and highlight type
            urlText = urlText.substring(1);
            String[] test1 = urlText.split(" ");
            String RurlText=" ";
            for (String t:test1){
                if (t.length()>1) {
                    t = t.substring(0, 1).toUpperCase() + t.substring(1);
                }
                RurlText = RurlText + " "+t;
            }
            RurlText = RurlText.substring(2);
            String[] test = RurlText.split(" ",2);
            if (test.length>1) {
                String firstWord = test[0];
                String restWord = test[1];

                listAnkle.add("["+firstWord+"]"+restWord);
            }
            else listAnkle.add(RurlText);
        }

        return listAnkle;
    }

    public static LinkedList<Hyperlink> ReturnHyperlink(TextField input) throws Exception {
        String searchQuery = input.getText();
        LinkedList<Document> results = Searcher.search(searchQuery);
        LinkedList<Hyperlink>listAnkle = new LinkedList<>();
        for (Document result : results) {
            Hyperlink urlText = new Hyperlink(result.get("url"));
            urlText.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Desktop d = Desktop.getDesktop();
                    try {
                        URI website = new URI(urlText.getText());
                        d.browse(website);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            listAnkle.add(urlText);
        }
        return listAnkle;
    }

    public static LinkedList<String> ReturnDiscrb(TextField input) throws Exception {
        String searchQuery = input.getText();
        LinkedList<Document> results = Searcher.search(searchQuery);
        LinkedList<String>listAnkle = new LinkedList<>();
        for (Document result : results) {
            String discribText = (result.get("description"));
            listAnkle.add(discribText);
        }
        return listAnkle;
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
