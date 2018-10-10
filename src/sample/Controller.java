package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.lucene.document.Document;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

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

        typeDragD.setItems(FXCollections.observableArrayList(
                "Game", "News ",
                new Separator(), "Term","Community"));
        typeDragD.getSelectionModel().selectFirst();
    }

    public void doSearch(ActionEvent actionEvent) {
        String searchQuery = searchText.getText();

        try {
        if (searchQuery.length() == 0 ) {
            WarnText.setText(ReturnValue.ReturnWarn(searchText));
        }
        else {
            HashSet<Document> results = Searcher.search(searchQuery);

            if (results.size() == 0) {
                WarnText.setText(ReturnValue.ReturnWarn(searchText));
            }
            else {
                FXMLLoader newLoader = new FXMLLoader(getClass().getResource("Result.fxml"));
                Parent root = newLoader.load();
                ResultCont resultCont = newLoader.getController();
                resultCont.WarnText.setText(ReturnValue.ReturnWarn(searchText));
                LinkedList<Hyperlink> AnkleList = ReturnValue.ReturnAnkle(searchText);
                for (int i=0;i<AnkleList.size();i++){
                    Hyperlink Hyperurl = AnkleList.get(i);
                    resultCont.resultContainer.getChildren().add(Hyperurl);
                }
                resultCont.resultContainer.setSpacing(5);
                searchBtn.getScene().setRoot(root);
            }
        }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


}
