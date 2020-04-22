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
  // private GridPane root;
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

  @Override
  public void start(Stage primaryStage) throws Exception {
    // root = new GridPane();
    root = new BorderPane();
    mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    leftPanel = new VBox();
    leftTop = new HBox();
    rightTop = new HBox();
    rightPanel = new VBox();
    csvTable = new TableView<>();
    Button b_data = new Button("DATA"), b_farm = new Button("FARM"),
        b_annual = new Button("ANNUAL"), b_monthly = new Button("MONTHLY"),
        b_range = new Button("RANGE");
    rP_Label = new Label("Data Pressed");
    b_data.setUnderline(true);

    // left-top
    leftTop.setPadding(new Insets(10));
    leftTop.setSpacing(10);
    leftTop.getChildren().addAll(b_data, b_farm, b_annual, b_monthly, b_range);

    // right-top
    Label d_label = new Label("Date");
    d_label.prefWidthProperty().bind(rightTop.widthProperty().divide(4).multiply(3));
    d_label.setFont(new Font(d_label.getFont().getName(), 20));
    Label f_label = new Label("Farm ID");
    f_label.prefWidthProperty().bind(rightTop.widthProperty().divide(4));
    f_label.setFont(new Font(f_label.getFont().getName(), 20));
    rightTop.getChildren().addAll(d_label, f_label);

    // data grid 1
    GridPane d_grid1 = new GridPane();
    d_grid1.setHgap(10);
    d_grid1.setVgap(10);
    Label year = new Label("YEAR");
    year.setStyle("-fx-border-color: black;");
    year.setPadding(new Insets(5));
    year.prefWidthProperty().bind(d_grid1.widthProperty().divide(8));
    Label month = new Label("MONTH");
    month.setStyle("-fx-border-color: black;");
    month.setPadding(new Insets(5));
    month.prefWidthProperty().bind(d_grid1.widthProperty().divide(4));
    Label day = new Label("DAY");
    day.setStyle("-fx-border-color: black;");
    day.setPadding(new Insets(5));
    day.prefWidthProperty().bind(d_grid1.widthProperty().divide(8));
    Label id = new Label("#");
    id.setStyle("-fx-border-color: black;");
    id.setPadding(new Insets(5));
    id.prefWidthProperty().bind(d_grid1.widthProperty().divide(8));
    d_grid1.add(year, 0, 0);
    d_grid1.add(month, 1, 0);
    d_grid1.add(day, 2, 0);
    d_grid1.add(id, 10, 0);

    // data grid 2
    GridPane d_grid2 = new GridPane();
    d_grid2.setHgap(10);
    d_grid2.setVgap(10);
    Label w_label = new Label("Weight");
    w_label.prefWidthProperty().bind(d_grid2.widthProperty().divide(2));
    w_label.setFont(new Font(w_label.getFont().getName(), 20));
    Label wt = new Label("(weight)");
    wt.setStyle("-fx-border-color: black;");
    wt.setPadding(new Insets(5));
    wt.prefWidthProperty().bind(d_grid2.widthProperty().divide(3));
    Button add = new Button("ADD");
    Button edit = new Button("EDIT");
    Button remove = new Button("REMOVE");
    d_grid2.add(w_label, 0, 0);
    d_grid2.add(wt, 0, 1);
    d_grid2.add(add, 1, 1);
    d_grid2.add(edit, 2, 1);
    d_grid2.add(remove, 3, 1);

    // data grid 3
    GridPane d_grid3 = new GridPane();
    d_grid3.setHgap(10);
    d_grid3.setVgap(10);
    Label s_label = new Label("Statistic");
    s_label.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    s_label.setFont(new Font(d_label.getFont().getName(), 20));
    farmChart = chartMaker("FARM");
    monthChart = chartMaker("MONTH");
    farmChart.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    monthChart.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    d_grid3.add(s_label, 0, 0);
    d_grid3.add(farmChart, 0, 1);
    d_grid3.add(monthChart, 1, 1);

    // data HBox bottom
    rightBottom = new HBox();
    Label totalWt = new Label("Total Weight:");
    Label total = new Label("(total)");
    totalWt.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    totalWt.setFont(new Font(f_label.getFont().getName(), 16));
    total.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    total.setFont(new Font(f_label.getFont().getName(), 16));
    rightBottom.getChildren().addAll(totalWt, total);
    root.setPadding(new Insets(10));

    // leftPanel
    leftPanel.getChildren().addAll(leftTop, csvTable);

    // rightPanel
    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    rightPanel.getChildren().addAll(rP_Label, rightTop, d_grid1, d_grid2, d_grid3, rightBottom);


    root.setLeft(leftPanel);
    root.setCenter(rightPanel);

    // Action Event: Buttons
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

    csvTable.prefHeightProperty().bind(primaryStage.heightProperty());
    csvTable.prefWidthProperty()
        .bind(b_data.widthProperty().add(b_farm.widthProperty()).add(b_annual.widthProperty())
            .add(b_monthly.widthProperty()).add(b_range.widthProperty()).add(60));

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
