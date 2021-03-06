package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
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
    public static Integer currentPage = 0;
    public Button lastPageBtn;
    public Button nextPageBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CtypeDragD.setItems(FXCollections.observableArrayList(
                "games", "people",
                new Separator(), "character","users","feedback","platform"));
        CtypeDragD.getSelectionModel().selectFirst();
    }


    public void SearchAgain() throws Exception {
        currentPage = 0;
        searchMethod ();

    }


    public void goPrevious() {

        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("Search.fxml"));
        try {
            Parent root = newLoader.load();
            goBackbtn.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void enterSearchC(KeyEvent keyEvent) throws Exception{
        if (keyEvent.getCode()== KeyCode.ENTER){
            searchMethod ();
        }
    }

    public void goLastPage() throws Exception {
        if (currentPage > 0 && currentPage >= 10) {
            currentPage = currentPage - 10;
            searchMethod();
        } else if (currentPage - 10 < 1 && currentPage >= 0 && currentPage >= 10) {
            currentPage = 0;
            searchMethod();
        } else if (currentPage < 10) {
            currentPage = 0;
            searchMethod();
        }
    }

    public void goNextPage() throws Exception {
        if (currentPage < Searcher.getTotalResult() - 10 && Searcher.getTotalResult() > 10 && currentPage + 20 <= Searcher.getTotalResult()) {
            currentPage = currentPage + 10;
            searchMethod();
        } else if (currentPage > Searcher.getTotalResult() - 10 && currentPage < Searcher.getTotalResult() && Searcher.getTotalResult() > 10 && currentPage + 20 <= Searcher.getTotalResult()) {
            currentPage = (int) (Searcher.getTotalResult() - 10);
            searchMethod();
        } else if (currentPage + 20 > Searcher.getTotalResult()) {
            currentPage = (int) (Searcher.getTotalResult() - 10);
            searchMethod();
        }
    }

    public void searchMethod ()throws Exception{
        String searchQuery = SearchTextC.getText();
        long beginTime = System.currentTimeMillis();
        resultContainer.getChildren().clear();
        if (searchQuery.length() == 0 ) {
            WarnText.setText(ReturnValue.ReturnWarn(SearchTextC,CtypeDragD.getValue().toString()));
        }
        else {


            List<Hyperlink> hyperList = ReturnValue.ReturnHyperlink(SearchTextC, CtypeDragD.getValue().toString());
            List<String> ankleList = ReturnValue.ReturnAnkle(SearchTextC, CtypeDragD.getValue().toString());
            List<String> describList = ReturnValue.ReturnDiscrb(SearchTextC, CtypeDragD.getValue().toString());
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
            TextField author = new TextField("Author : YifeiWang");
            resultContainer.getChildren().add(author);
            TextField blank = new TextField();
            resultContainer.getChildren().add(blank);
            resultContainer.setSpacing(10);
            long endTime = System.currentTimeMillis();
            WarnText.setText(ReturnValue.ReturnWarn(SearchTextC, CtypeDragD.getValue().toString()) +
                    "                         Showing result  " + currentPage + " - " + (currentPage + 10) +
                    "                         Total time: " + (endTime - beginTime) + " millisecond(s).");
        }
    }

}
