package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.apache.lucene.document.Document;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

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
                "games", "people",
                new Separator(), "character","users","feedback","platform"));
        typeDragD.getSelectionModel().selectFirst();


    }

    public void doSearch() throws Exception {
        searchMethod();

    }


    public void ShowSponcer() {

            Desktop d = Desktop.getDesktop();
            try {
                URI website = new URI("https://www.igdb.com/");
                d.browse(website);
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }

    }
    private DropShadow shadow = new DropShadow();

    public void addShadow() {
        SponceBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> SponceBtn.setEffect(shadow));
        //Removing the shadow when the mouse cursor is off
        SponceBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> SponceBtn.setEffect(null));
    }

    public void enterSearch(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode()== KeyCode.ENTER){
            searchMethod();
        }
    }
        public void searchMethod ()throws Exception {
            long beginTime = System.currentTimeMillis();
            String searchQuery = searchText.getText();


            if (searchQuery.length() == 0) {
                WarnText.setText(ReturnValue.ReturnWarn(searchText, typeDragD.getValue().toString()));
            } else {
                LinkedList<Document> results = Searcher.search(searchQuery, typeDragD.getValue().toString());

                if (results.size() == 0) {
                    WarnText.setText(ReturnValue.ReturnWarn(searchText, typeDragD.getValue().toString()));
                } else {
                    FXMLLoader newLoader = new FXMLLoader(getClass().getResource("Result.fxml"));
                    Parent root = newLoader.load();
                    ResultCont resultCont = newLoader.getController();


                    List<Hyperlink> hyperList = ReturnValue.ReturnHyperlink(searchText, typeDragD.getValue().toString());
                    List<String> ankleList = ReturnValue.ReturnAnkle(searchText, typeDragD.getValue().toString());
                    List<String> describList = ReturnValue.ReturnDiscrb(searchText, typeDragD.getValue().toString());
                    for (int i = 0; i < hyperList.size(); i++) {
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

                    TextField author = new TextField("Author : YifeiWang");
                    resultCont.resultContainer.getChildren().add(author);
                    TextField blank = new TextField();
                    resultCont.resultContainer.getChildren().add(blank);
                    resultCont.resultContainer.setSpacing(10);

                    resultCont.SearchTextC.setText(searchText.getText());

                    long endTime = System.currentTimeMillis();
                    resultCont.WarnText.setText(ReturnValue.ReturnWarn(searchText, typeDragD.getValue().toString()) +
                            "                         Showing result  " + "0 - 10" +
                            "                         Total time: " + (endTime - beginTime) + " millisecond(s).");
                    searchBtn.getScene().setRoot(root);


                }
            }

        }
    }
