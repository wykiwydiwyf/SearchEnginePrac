package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.lucene.document.Document;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class Controller implements Initializable {
    public Text WarnText;
    public AnchorPane AncMain;
    public ImageView BG;
    public Pane CenterContainer;
    public HBox SearchContainer;
    public ChoiceBox typeDragD;
    public TextField searchText;
    public Button searchBtn;
    public Label searchTitle;
    public Button SponceBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        typeDragD.setItems(FXCollections.observableArrayList(
                "Game", "News ",
                new Separator(), "Term","Community"));
        typeDragD.getSelectionModel().selectFirst();
    }

    public void doSearch(ActionEvent actionEvent) throws Exception {
        String searchQuery = searchText.getText();


        if (searchQuery.length() == 0 ) {
            WarnText.setText(ReturnValue.ReturnWarn(searchText));
        }
        else {
            LinkedList<Document> results = Searcher.search(searchQuery);

            if (results.size() == 0) {
                WarnText.setText(ReturnValue.ReturnWarn(searchText));
            }
            else {
                FXMLLoader newLoader = new FXMLLoader(getClass().getResource("Result.fxml"));
                Parent root = newLoader.load();
                ResultCont resultCont = newLoader.getController();
                resultCont.WarnText.setText(ReturnValue.ReturnWarn(searchText));

                LinkedList<Hyperlink> hyperList = ReturnValue.ReturnHyperlink(searchText);
                LinkedList<String> ankleList = ReturnValue.ReturnAnkle(searchText);
                LinkedList<String> describList = ReturnValue.ReturnDiscrb(searchText);
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
                    TdescribText.wrappingWidthProperty().bind(AncMain.widthProperty());
                    dummyVbox.getChildren().add(TdescribText);
                    dummyVbox.setSpacing(3);
                    resultCont.resultContainer.getChildren().add(dummyVbox);
                }

                resultCont.resultContainer.setSpacing(10);
                resultCont.SearchTextC.setText(searchText.getText());
                searchBtn.getScene().setRoot(root);
            }
        }

    }


    public void ShowSponcer(ActionEvent actionEvent) {

            Desktop d = Desktop.getDesktop();
            try {
                URI website = new URI("https://www.igdb.com/");
                d.browse(website);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }

    }
    DropShadow shadow = new DropShadow();
    public void addShadow(MouseEvent mouseEvent) {
        SponceBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        SponceBtn.setEffect(shadow);
                    }
                });
        //Removing the shadow when the mouse cursor is off
        SponceBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        SponceBtn.setEffect(null);
                    }
                });
    }
}
