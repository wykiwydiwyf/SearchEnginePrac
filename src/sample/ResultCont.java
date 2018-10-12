package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ResultCont implements Initializable {
    public ImageView BG;
    public SplitPane SplitP;
    public Button SearchBtnC;
    public TextField SearchTextC;
    public Text WarnText;
    public AnchorPane AncResultUp;
    public VBox resultContainer;
    public Button goBackbtn;
    public ChoiceBox CtypeDragD;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CtypeDragD.setItems(FXCollections.observableArrayList(
                "games", "people",
                new Separator(), "character","users","feedback","platform"));
        CtypeDragD.getSelectionModel().selectFirst();
    }


    public void SearchAgain(ActionEvent actionEvent) throws Exception {
        String searchQuery = SearchTextC.getText();

        resultContainer.getChildren().clear();
            if (searchQuery.length() == 0 ) {
                WarnText.setText(ReturnValue.ReturnWarn(SearchTextC,CtypeDragD.getValue().toString()));
            }
            else {
                WarnText.setText(ReturnValue.ReturnWarn(SearchTextC,CtypeDragD.getValue().toString()));

                LinkedList<Hyperlink> hyperList = ReturnValue.ReturnHyperlink(SearchTextC,CtypeDragD.getValue().toString());
                LinkedList<String> ankleList = ReturnValue.ReturnAnkle(SearchTextC,CtypeDragD.getValue().toString());
                LinkedList<String> describList = ReturnValue.ReturnDiscrb(SearchTextC,CtypeDragD.getValue().toString());
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
