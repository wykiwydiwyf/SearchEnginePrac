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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class ResultCont implements Initializable {
    public ImageView BG;
    public SplitPane SplitP;
    public Button SearchBtnC;
    public TextField SearchTextC;
    public ImageView steamLogo;
    public Text WarnText;
    public Hyperlink urlHyper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }


    public void SearchAgain(ActionEvent actionEvent) {
        WarnText.setText(ReturnValue.ReturnWarn(SearchTextC));
    }



}
