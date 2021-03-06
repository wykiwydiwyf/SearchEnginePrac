package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

class ReturnValue   {

    private static String WarnText = "";

    static String ReturnWarn(TextField input,String fieldsForb){
        String searchQuery = input.getText();
        if (searchQuery.length() == 0) {  // textField does not handle (userInput != null)
            WarnText=("Please enter a search query");
            return WarnText;
        }

        try {
            LinkedList<Document> results = Searcher.search(searchQuery,fieldsForb);


            if (results.size() == 0) {
                StringBuilder tempText= new StringBuilder();
                for (int i = 0; showSimilarWords(searchQuery).size() > i; i++) {
                    tempText.append("  ").append(showSimilarWords(searchQuery).get(i));
                    if (i > 5)
                        break;
                }
                WarnText=("The search did not find any results for '" + searchQuery + "'"+"\n ..Do you mean..: "+tempText);
                return WarnText;
            }
            WarnText = ("Search Results for " + searchQuery + ": \n" + Searcher.getTotalResult() + "   Total results");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return WarnText;
    }

    static List<String> ReturnAnkle(TextField input, String fieldsForb) throws Exception {
        String searchQuery = input.getText();
        LinkedList<Document> results = Searcher.search(searchQuery,fieldsForb);
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
        if (ResultCont.currentPage + 10 >= 10 && ResultCont.currentPage + 10 < Searcher.getTotalResult()) {
            List<String> trueListAnkle = listAnkle.subList(ResultCont.currentPage, ResultCont.currentPage + 10);
            return trueListAnkle;
        } else if (ResultCont.currentPage + 10 > Searcher.getTotalResult() && ResultCont.currentPage < Searcher.getTotalResult() && Searcher.getTotalResult() > 10) {
            List<String> trueListDiscrb = listAnkle.subList((int) (Searcher.getTotalResult() - 10), (int) (Searcher.getTotalResult() - 10 + 10));
            return trueListDiscrb;
        } else {
            List<String> trueListDiscrb = listAnkle.subList(0, listAnkle.size());
            return trueListDiscrb;
        }
    }

    static List<Hyperlink> ReturnHyperlink(TextField input, String fieldsForb) throws Exception {
        String searchQuery = input.getText();
        LinkedList<Document> results = Searcher.search(searchQuery,fieldsForb);
        LinkedList<Hyperlink> listHyper = new LinkedList<>();
        for (Document result : results) {
            Hyperlink urlText = new Hyperlink(result.get("url"));
            urlText.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Desktop d = Desktop.getDesktop();
                    try {
                        URI website = new URI(urlText.getText());
                        d.browse(website);
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            listHyper.add(urlText);
        }
        if (ResultCont.currentPage + 10 >= 10 && ResultCont.currentPage + 10 < Searcher.getTotalResult()) {
            List<Hyperlink> trueListHyper = listHyper.subList(ResultCont.currentPage, ResultCont.currentPage + 10);
            return trueListHyper;
        } else if (ResultCont.currentPage + 20 > Searcher.getTotalResult() && ResultCont.currentPage < Searcher.getTotalResult() && Searcher.getTotalResult() > 10) {
            List<Hyperlink> trueListHyper = listHyper.subList((int) (Searcher.getTotalResult() - 10), (int) (Searcher.getTotalResult() - 10 + 10));
            return trueListHyper;
        } else {
            List<Hyperlink> trueListHyper = listHyper.subList(0, listHyper.size());
            return trueListHyper;
        }
    }

    static List<String> ReturnDiscrb(TextField input, String fieldsForb) throws Exception {
        String searchQuery = input.getText();
        LinkedList<Document> results = Searcher.search(searchQuery,fieldsForb);
        LinkedList<String> listDiscrb = new LinkedList<>();
        for (Document result : results) {
            String discribText = (result.get("description"));
            listDiscrb.add(discribText);
        }
        if (ResultCont.currentPage + 10 >= 10 && ResultCont.currentPage + 10 < Searcher.getTotalResult()) {
            List<String> trueListDiscrb = listDiscrb.subList(ResultCont.currentPage, ResultCont.currentPage + 10);
            return trueListDiscrb;
        } else if (ResultCont.currentPage + 10 > Searcher.getTotalResult() && ResultCont.currentPage < Searcher.getTotalResult() && Searcher.getTotalResult() > 10) {
            List<String> trueListDiscrb = listDiscrb.subList((int) (Searcher.getTotalResult() - 10), (int) (Searcher.getTotalResult() - 10 + 10));
            return trueListDiscrb;
        } else {
            List<String> trueListDiscrb = listDiscrb.subList(0, listDiscrb.size());
            return trueListDiscrb;
        }
    }

    private static final String INDEX_DIR = "data/index";


    public static LinkedHashMap BuildSimilarWordLib()throws Exception{
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        LinkedHashMap<String, Integer> wordLib = new LinkedHashMap<>();
        IndexReader reader = DirectoryReader.open(dir);

        for (int i = 0; i < 100; i++) {
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
        String[] searchQueryWords = searchQuery.split(" ");
        HashSet<String> similarWords = SimilarWords.retrieveSimilarWords(hashMap, searchQueryWords);
        if (!similarWords.isEmpty()) { // If there are no similar words, don't try to display them
            SimilarWordList.addAll(similarWords);
        }
        return SimilarWordList;
    }

}
