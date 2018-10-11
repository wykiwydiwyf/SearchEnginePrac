package sample;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import sun.awt.image.ImageWatched;

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


    public void SearchAgain(ActionEvent actionEvent) throws Exception {
        String searchQuery = SearchTextC.getText();
        resultContainer.getChildren().clear();
            if (searchQuery.length() == 0 ) {
                WarnText.setText(ReturnValue.ReturnWarn(SearchTextC));
            }
            else {
                WarnText.setText(ReturnValue.ReturnWarn(SearchTextC));

                LinkedList<Hyperlink> hyperList = ReturnValue.ReturnHyperlink(SearchTextC);
                LinkedList<String> ankleList = ReturnValue.ReturnAnkle(SearchTextC);
                LinkedList<String> describList = ReturnValue.ReturnDiscrb(SearchTextC);
                for (int i=0;i<hyperList.size();i++){
                    VBox dummyVbox = new VBox();
                    Hyperlink Hyperurl = hyperList.get(i);
                    String Ankletext = ankleList.get(i);
                    String describText = describList.get(i);
                    Text TankleText = new Text(Ankletext);
                    TankleText.setFont(Font.font(null, FontWeight.BOLD, 15));
                    dummyVbox.getChildren().add(TankleText);
                    dummyVbox.getChildren().add(Hyperurl);
                    Text TdescribText = new Text(describText);
                    TdescribText.wrappingWidthProperty().bind(SplitP.widthProperty());
                    dummyVbox.getChildren().add(TdescribText);
                    dummyVbox.setSpacing(3);
                    resultContainer.getChildren().add(dummyVbox);
                }

                resultContainer.setSpacing(10);

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
