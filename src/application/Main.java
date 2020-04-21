package application;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import application.Main.Farm;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

  private static final int WINDOW_WIDTH = 1000;
  private static final int WINDOW_HEIGHT = 800;
  private GridPane root;
  private Scene mainScene;
  private HBox topPanel;
  private VBox rightPanel;
  private TableView<Farm> csvTable;
  private ObservableList<Farm> dataList = FXCollections.observableArrayList();

  public class Farm {
    // Assume each record have 6 elements, all String

    private SimpleStringProperty f1, f2, f3;

    public String getF1() {
      return f1.get();
    }

    public String getF2() {
      return f2.get();
    }

    public String getF3() {
      return f3.get();
    }

    Farm(String farm_id, String date, String weight) {
      this.f1 = new SimpleStringProperty(farm_id);
      this.f2 = new SimpleStringProperty(date);
      this.f3 = new SimpleStringProperty(weight);
    }

  }

  private void readCSV() {

    String CsvFile = "src/csv/large/2019-1.csv";
    String FieldDelimiter = ",";

    BufferedReader br;

    try {
      br = new BufferedReader(new FileReader(CsvFile));

      String line;
      while ((line = br.readLine()) != null) {
        String[] fields = line.split(FieldDelimiter, -1);

        Farm record =
            new Farm(fields[1], fields[0], fields[2]);
        dataList.add(record);

      }

    } catch (FileNotFoundException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    root = new GridPane();
    mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    topPanel = new HBox();
    rightPanel = new VBox();
    csvTable = new TableView<>();

    root.setPadding(new Insets(10));
    root.add(topPanel, 0, 0, 5, 1);
    root.add(csvTable, 0, 1, 5, 10);
    root.add(rightPanel, 5, 0, 4, 10);

    topPanel.setPadding(new Insets(10));
    topPanel.setSpacing(10);
    topPanel.getChildren().addAll(new Button("DATA"), new Button("FARM"), new Button("ANNUAL"),
        new Button("MONTHLY"), new Button("RANGE"));

    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    rightPanel.getChildren().add(new Text(csvTable.getMinHeight() + " " + csvTable.getMaxHeight()));

    TableColumn f1 = new TableColumn("FARM");
    f1.setCellValueFactory(new PropertyValueFactory<>("f1"));
    TableColumn f2 = new TableColumn("DATE");
    f2.setCellValueFactory(new PropertyValueFactory<>("f2"));
    TableColumn f3 = new TableColumn("WEIGHT");
    f3.setCellValueFactory(new PropertyValueFactory<>("f3"));
    csvTable.getColumns().addAll(f1, f2, f3);
    csvTable.setItems(dataList);
    readCSV();

    primaryStage.setTitle("Milky Way");
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }


  private void setup() {

  }

  public static void main(String[] args) {
    launch(args);
  }
}
