package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.awt.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TextFlow resultText;
    public Text WarnText;
    public AnchorPane AncMain;
    public ImageView BG;
    public Pane CenterContainer;
    public HBox SearchContainer;
    public ChoiceBox typeDragD;
    public TextField searchText;
    public Button searchBtn;
    public ImageView steamLogo;
    public Label searchTitle;
    public TextFlow warmContainer;
    public Text SimilarText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }



    public void doSearch(ActionEvent actionEvent) {
        WarnText.setText(ReturnValue.ReturnWarn(searchText));
    }

    public TextField ReturnSearchText(){
        return searchText;
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
    private void showSimilarWords(String searchQuery) throws Exception {
        LinkedHashMap hashMap = BuildSimilarWordLib();
        String[]searchQueryWords=searchQuery.split(" ");
        HashSet<String> similarWords = SimilarWords.retrieveSimilarWords(hashMap,searchQueryWords);
        if (!similarWords.isEmpty()) { // If there are no similar words, don't try to display them
            WarnText.setText("\n ..Do you mean..: ");
            String tempText= new String();
            for (String similarWord: similarWords) {
                SimilarText.setText(similarWord + ", " + tempText);
                tempText = SimilarText.getText();
            }
        }
    }
}
