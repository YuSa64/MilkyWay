// TODO: Right Panel GUI (Chart, Textfield, etc)

package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

  private static final int WINDOW_WIDTH = 800;
  private static final int WINDOW_HEIGHT = 600;
  private BorderPane root;
  private Scene mainScene;
  private VBox leftPanel;
  private VBox rightPanel;
  private HBox leftTop;
  private HBox rightTop;
  private HBox rightBottom;
  private TableView<Farm> csvTable;
  private PieChart farmChart;
  private PieChart monthChart;
  Button[] leftB;
  Label[] rightL;
  private ObservableList<Farm> dataList = FXCollections.observableArrayList();


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


  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setResizable(false);
    primaryStage.setTitle("Milky Way");
    showData(primaryStage);
    primaryStage.show();
  }

  private void readCSV() {

    // TODO: change filePath to your own path
    String CsvFile = "large/2019-1.csv";
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
          while (farm_id.length() < 3) {
            farm_id = "0" + farm_id;
          }
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

  private PieChart chartMaker(String chartName) {
    // temp pie-chart
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(new PieChart.Data("farm1", 13),
            new PieChart.Data("farm2", 25), new PieChart.Data("farm3", 10),
            new PieChart.Data("farm4", 22), new PieChart.Data("farm5", 30));
    PieChart pieChart = new PieChart(pieChartData);
    pieChart.setTitle(chartName);
    pieChart.setLabelsVisible(false);
    return pieChart;
  }

  private void setupScene(Stage primaryStage) {
    root = new BorderPane();
    mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    leftPanel = new VBox();
    leftTop = new HBox();
    rightTop = new HBox();
    rightPanel = new VBox();
    csvTable = new TableView<>();
    leftB = new Button[] {new Button("DATA"), new Button("FARM"), new Button("ANNUAL"),
        new Button("MONTHLY"), new Button("RANGE")};
    for (Button b : leftB)
      b.setFocusTraversable(false);
    rightL = new Label[] {new Label(), new Label()};
    // Action Event: Buttons
    leftB[0].setOnAction(e -> {
      showData(primaryStage);
    });
    leftB[1].setOnAction(e -> {
      showFarm(primaryStage);

    });
    leftB[2].setOnAction(e -> {
      showAnnual(primaryStage);

    });
    leftB[3].setOnAction(e -> {
      showMonthly(primaryStage);

    });
    leftB[4].setOnAction(e -> {
      showRange(primaryStage);

    });

    // left-top
    leftTop.setPadding(new Insets(10));
    leftTop.setSpacing(10);
    leftTop.getChildren().addAll(leftB[0], leftB[1], leftB[2], leftB[3], leftB[4]);

    leftPanel.getChildren().addAll(leftTop, csvTable);
    root.setLeft(leftPanel);
    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    root.setCenter(rightPanel);
    root.setPadding(new Insets(10));
  }

  private void showData(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(leftB[0], leftB[1], leftB[2], leftB[3], leftB[4]);

    // leftPanel
    TableColumn<Farm, String> f1 = new TableColumn<>("FARM");
    f1.setCellValueFactory(new PropertyValueFactory<>("f1"));
    TableColumn<Farm, String> f2 = new TableColumn<>("DATE");
    f2.setCellValueFactory(new PropertyValueFactory<>("f2"));
    TableColumn<Farm, Integer> f3 = new TableColumn<>("WEIGHT");
    f3.setCellValueFactory(new PropertyValueFactory<>("f3"));
    f1.prefWidthProperty().bind(csvTable.widthProperty().divide(6));
    f2.prefWidthProperty().bind(csvTable.widthProperty().divide(6).multiply(2));
    f3.prefWidthProperty().bind(csvTable.widthProperty().divide(6).multiply(3));
    csvTable.getColumns().addAll(f1, f2, f3);
    csvTable.setItems(dataList);
    csvTable.setFocusTraversable(false);
    csvTable.prefHeightProperty().bind(primaryStage.heightProperty());
    csvTable.prefWidthProperty()
        .bind(leftB[0].widthProperty().add(leftB[1].widthProperty()).add(leftB[2].widthProperty())
            .add(leftB[3].widthProperty()).add(leftB[4].widthProperty()).add(60));
    readCSV();

    // rightPanel
    GridPane d_grid3 = new GridPane();
    d_grid3.setHgap(10);
    d_grid3.setVgap(10);
    Label s_label = new Label("Statistic");
    s_label.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    s_label.setFont(new Font(rightL[0].getFont().getName(), 20));
    farmChart = chartMaker("FARM");
    monthChart = chartMaker("MONTH");
    farmChart.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    monthChart.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    d_grid3.add(s_label, 0, 0);
    d_grid3.add(farmChart, 0, 1);
    d_grid3.add(monthChart, 1, 1);
    rightBottom = new HBox();
    Label totalWt = new Label("Total Weight:");
    Label total = new Label("(total)");
    totalWt.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    totalWt.setFont(new Font(rightL[1].getFont().getName(), 16));
    total.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    total.setFont(new Font(rightL[1].getFont().getName(), 16));
    rightBottom.getChildren().addAll(totalWt, total);
    rightPanel.getChildren().addAll(rightTop, d_grid3, rightBottom);

    primaryStage.setScene(mainScene);
  }

  private void showFarm(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(leftB[1], leftB[0], leftB[2], leftB[3], leftB[4]);

    primaryStage.setScene(mainScene);
  }

  private void showAnnual(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(leftB[2], leftB[0], leftB[1], leftB[3], leftB[4]);

    primaryStage.setScene(mainScene);
  }

  private void showMonthly(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(leftB[3], leftB[0], leftB[1], leftB[2], leftB[4]);

    primaryStage.setScene(mainScene);
  }

  private void showRange(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(leftB[4], leftB[0], leftB[1], leftB[2], leftB[3]);

    primaryStage.setScene(mainScene);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
