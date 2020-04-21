// TODO: Right Panel GUI (Chart, Textfield, etc)

package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import application.Main.Farm;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
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
  private PieChart chart;
  private Label rP_Label;
  private ObservableList<Farm> dataList = FXCollections.observableArrayList();

  public class Farm {
    // Assume each record have 6 elements, all String

    private SimpleStringProperty f1, f2;
    private SimpleIntegerProperty f3;

    public String getF1() {
      return f1.get();
    }

    public String getF2() {
      return f2.get();
    }

    public int getF3() {
      return f3.get();
    }

    Farm(String farm_id, String date, int weight) {
      this.f1 = new SimpleStringProperty(farm_id);
      this.f2 = new SimpleStringProperty(date);
      this.f3 = new SimpleIntegerProperty(weight);
    }

  }

  private void readCSV() {

    // TODO: change filePath to your own path
    String CsvFile = "src/csv/large/2019-1.csv";
    String FieldDelimiter = ",";

    BufferedReader br;

    try {
      br = new BufferedReader(new FileReader(CsvFile));

      String line;
      while ((line = br.readLine()) != null) {
        String[] fields = line.split(FieldDelimiter, -1);
        if (!fields[1].equals("farm_id")) {
          String farm_id = fields[1];
          String date = fields[0];
          String pweight = fields[2];
          int weight = 0;

          farm_id = farm_id.substring(5);
          while (farm_id.length() < 3)
            farm_id = "0" + farm_id;

          weight = Integer.parseInt(pweight);

          Farm record = new Farm(farm_id, date, weight);
          dataList.add(record);
        }

      }

    } catch (FileNotFoundException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  private void underliner(Button target, Button b1, Button b2, Button b3, Button b4) {
    if (b1.isUnderline()) {
      b1.setUnderline(false);
    }
    if (b2.isUnderline()) {
      b2.setUnderline(false);
    }
    if (b3.isUnderline()) {
      b3.setUnderline(false);
    }
    if (b4.isUnderline()) {
      b4.setUnderline(false);
    }
    if (!target.isUnderline()) {
      target.setUnderline(true);
    }
  }

  private PieChart chartMaker() {
    // temp pie-chart
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(new PieChart.Data("farm1", 13),
            new PieChart.Data("farm2", 25), new PieChart.Data("farm3", 10),
            new PieChart.Data("farm4", 22), new PieChart.Data("farm5", 30));
    PieChart pieChart = new PieChart(pieChartData);
    pieChart.setTitle("Default Chart");
    return pieChart;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    root = new GridPane();

    mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    topPanel = new HBox();
    // rightPanel = new VBox();
    rightPanel = new VBox();
    csvTable = new TableView<>();
    Button b_data = new Button("DATA"), b_farm = new Button("FARM"),
        b_annual = new Button("ANNUAL"), b_monthly = new Button("MONTHLY"),
        b_range = new Button("RANGE");
    rP_Label = new Label("Data Pressed");
    b_data.setUnderline(true);

    GridPane.setHgrow(topPanel, Priority.ALWAYS);
    GridPane.setVgrow(topPanel, Priority.ALWAYS);
    GridPane.setHgrow(csvTable, Priority.ALWAYS);
    GridPane.setVgrow(csvTable, Priority.ALWAYS);
    GridPane.setHgrow(rightPanel, Priority.ALWAYS);
    GridPane.setVgrow(rightPanel, Priority.ALWAYS);
    root.setPadding(new Insets(10));
    root.add(topPanel, 0, 0, 5, 1);
    root.add(csvTable, 0, 1, 5, 10);
    root.add(rightPanel, 5, 0, 4, 10);

    b_data.setOnAction(e -> {
      underliner(b_data, b_farm, b_annual, b_monthly, b_range);
      showData();
    });
    b_farm.setOnAction(e -> {
      underliner(b_farm, b_data, b_annual, b_monthly, b_range);
      showFarm();
    });
    b_annual.setOnAction(e -> {
      underliner(b_annual, b_farm, b_data, b_monthly, b_range);
      showAnnual();
    });
    b_monthly.setOnAction(e -> {
      underliner(b_monthly, b_farm, b_data, b_annual, b_range);
      showMonthly();
    });
    b_range.setOnAction(e -> {
      underliner(b_range, b_farm, b_data, b_annual, b_monthly);
      showRange();
    });

    topPanel.setPadding(new Insets(10));
    topPanel.setSpacing(10);
    topPanel.getChildren().addAll(b_data, b_farm, b_annual, b_monthly, b_range);

    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    rightPanel.getChildren().add(rP_Label);
    // add temp chart --> rightPanel.getChildren().add(chart=chartMaker());
    rightPanel.prefWidthProperty().bind(primaryStage.widthProperty());

    TableColumn f1 = new TableColumn("FARM");
    f1.setCellValueFactory(new PropertyValueFactory<>("f1"));
    TableColumn f2 = new TableColumn("DATE");
    f2.setCellValueFactory(new PropertyValueFactory<>("f2"));
    TableColumn f3 = new TableColumn("WEIGHT");
    f3.setCellValueFactory(new PropertyValueFactory<>("f3"));
    csvTable.getColumns().addAll(f1, f2, f3);
    csvTable.setItems(dataList);
    csvTable.prefHeightProperty().bind(primaryStage.heightProperty());

    readCSV();

    primaryStage.setResizable(false);
    primaryStage.setTitle("Milky Way");
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }

  private void showData() {
    rP_Label.setText("Data Pressed");
  }

  private void showFarm() {
    rP_Label.setText("Farm Pressed");
  }

  private void showAnnual() {
    rP_Label.setText("Annual Pressed");
  }

  private void showMonthly() {
    rP_Label.setText("Monthly Pressed");
  }

  private void showRange() {
    rP_Label.setText("Range Pressed");
  }

  public static void main(String[] args) {
    launch(args);
  }
}
