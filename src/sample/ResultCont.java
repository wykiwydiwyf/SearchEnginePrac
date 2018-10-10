package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ResultCont implements Initializable {
    public ImageView BG;
    public SplitPane SplitP;
    public Button SearchBtnC;
    public TextField SearchTextC;
    public ImageView steamLogo;
    public Text WarnText;
    public Hyperlink urlHyper;
    public AnchorPane AncResultUp;
    public VBox resultContainer;
    public Button goBackbtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void SearchAgain(ActionEvent actionEvent) {
        String searchQuery = SearchTextC.getText();
        resultContainer.getChildren().clear();
        resultContainer.getChildren().removeAll();
        try {
            if (searchQuery.length() == 0 ) {
                WarnText.setText(ReturnValue.ReturnWarn(SearchTextC));
            }
            else {
                HashSet<Document> results = Searcher.search(searchQuery);

                if (results.size() == 0) {
                    WarnText.setText(ReturnValue.ReturnWarn(SearchTextC));
                }
                else {
                    WarnText.setText(ReturnValue.ReturnWarn(SearchTextC));
                    LinkedList<Hyperlink> AnkleList = ReturnValue.ReturnAnkle(SearchTextC);
                    resultContainer.getChildren().clear();
                    for (int i=0;i<AnkleList.size();i++){
                        resultContainer.getChildren().add(AnkleList.get(i));
                    }
                    resultContainer.setSpacing(5);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public void goPrevious(ActionEvent actionEvent) {

        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("Search.fxml"));
        try {
            Parent root = newLoader.load();
            goBackbtn.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
