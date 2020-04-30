package application;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

  private static final int WINDOW_WIDTH = 800;
  private static final int WINDOW_HEIGHT = 600;
  private BorderPane root;
  private Scene mainScene;
  private VBox leftPanel, rightPanel, rightTop;
  private HBox leftTop, rightBottom;
  private TableView<Farm> csvTable;
  private FarmReport report;
  private Button[] topB;
  private FileChooser fileChooser;
  private Label total;
  private Button Cfile, Isearch, Iclear;
  private GridPane d_grid, inputGrid;
  private PieChart farmChart, monthChart, yearChart;
  private ObservableList<Farm> dataList;
  private TextField farmID;
  private ComboBox<String> year, month, day, dyear, dmonth, dday;

  /**
   * 
   * @param chart
   * @param name
   * @param type
   */
  private void chartMaker(PieChart chart, String name, int type) {
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    List<Farm> farmList;
    switch (type) {
      case 0:
        farmList = report.getFarmReport(null, null);
        break;
      case 1:

        farmList = report.getAnnualMonthlyReport(null, null);
        break;
      case 2:
        farmList = report.getAnnualData();
        break;
      default:
        farmList = report.getAllList();
        break;
    }
    for (Farm f : farmList) {
      PieChart.Data d = new PieChart.Data(f.getF1(), Integer.parseInt(f.getF3()));
      if (!pieChartData.contains(d)) {
        pieChartData.add(d);
      }
    }
    chart.setData(pieChartData);
    chart.setTitle(name);
    chart.setLabelsVisible(true);
  }

  /**
   * 
   * @param primaryStage
   */
  private void showData(Stage primaryStage) {
    clearBoard();
    underliner(topB, 0);

    csvTable.setItems(dataList = FXCollections.observableArrayList(report.getAllList()));
    setTableColumn("FARM", "DATE", "WEIGHT");
    csvTable.getSortOrder().add(csvTable.getColumns().get(1));

    inputGrid.add(new Label("FROM"), 0, 0);
    inputGrid.add(new Label("TO"), 0, 1);
    inputGrid.add(year, 1, 0);
    inputGrid.add(month, 2, 0);
    inputGrid.add(day, 3, 0);
    inputGrid.add(dyear, 1, 1);
    inputGrid.add(dmonth, 2, 1);
    inputGrid.add(dday, 3, 1);
    inputGrid.add(Isearch, 4, 0);
    inputGrid.add(Iclear, 5, 0);


    Isearch.setOnAction(e -> {
      csvTable.setItems(FXCollections
          .observableArrayList(report.getRangeReport(null, year.getValue(), month.getValue(),
              day.getValue(), null, dyear.getValue(), dmonth.getValue(), dday.getValue())));
      csvTable.getSortOrder().add(csvTable.getColumns().get(0));
    });

    Cfile.setOnAction(e -> {

      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles != null)
        for (File f : selectedFiles) {
          report.readCSV(f);
        }
      total.setText(report.getTotalWeight() + "");
      showData(primaryStage);
    });

  }

  /**
   * 
   * @param primaryStage
   */
  private void showFarm(Stage primaryStage) {
    clearBoard();
    underliner(topB, 1);

    csvTable
        .setItems(dataList = FXCollections.observableArrayList(report.getFarmReport(null, null)));
    setTableColumn("MONTH", "PERCENTAGE", "TOTAL WEIGHT");

    inputGrid.add(farmID, 1, 0);
    inputGrid.add(year, 2, 0);
    inputGrid.add(Isearch, 4, 0);
    inputGrid.add(Iclear, 5, 0);

    Isearch.setOnAction(e -> {
      if (farmID.getText().equals("")) {
        csvTable.setItems(
            FXCollections.observableArrayList(report.getFarmReport(null, year.getValue())));
      } else {
        csvTable.setItems(FXCollections
            .observableArrayList(report.getFarmReport(farmID.getText(), year.getValue())));
      }
      csvTable.getSortOrder().add(csvTable.getColumns().get(0));
    });

    Cfile.setOnAction(e -> {
      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles != null)
        for (File f : selectedFiles) {
          report.readCSV(f);
        }
      total.setText(report.getTotalWeight() + "");
      showFarm(primaryStage);
    });

  }

  /**
   * 
   * @param primaryStage
   */
  private void showAnnual(Stage primaryStage) {
    clearBoard();
    underliner(topB, 2);

    csvTable.setItems(dataList = FXCollections.observableArrayList(report.getAnnualMonthlyReport(null, null)));
    setTableColumn("FARM", "PERCENTAGE", "TOTAL WEIGHT");

    inputGrid.add(year, 1, 0);
    inputGrid.add(Isearch, 4, 0);
    inputGrid.add(Iclear, 5, 0);

    Isearch.setOnAction(e -> {
      csvTable.setItems(FXCollections.observableArrayList(report.getAnnualMonthlyReport(year.getValue(), null)));
      csvTable.getSortOrder().add(csvTable.getColumns().get(0));
    });

    Cfile.setOnAction(e -> {
      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles != null)
        for (File f : selectedFiles) {
          report.readCSV(f);
        }
      total.setText(report.getTotalWeight() + "");
      showAnnual(primaryStage);
    });
  }

  /**
   * 
   * @param primaryStage
   */
  private void showMonthly(Stage primaryStage) {
    clearBoard();
    underliner(topB, 3);

    csvTable.setItems(
        dataList = FXCollections.observableArrayList(report.getAnnualMonthlyReport(null, null)));
    setTableColumn("FARM", "PERCENTAGE", "TOTAL WEIGHT");

    inputGrid.add(year, 1, 0);
    inputGrid.add(month, 2, 0);
    inputGrid.add(Isearch, 4, 0);
    inputGrid.add(Iclear, 5, 0);

    Isearch.setOnAction(e -> {
      csvTable.setItems(FXCollections
          .observableArrayList(report.getAnnualMonthlyReport(year.getValue(), month.getValue())));
      csvTable.getSortOrder().add(csvTable.getColumns().get(0));
    });

    Cfile.setOnAction(e -> {
      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles != null)
        for (File f : selectedFiles) {
          report.readCSV(f);
        }
      total.setText(report.getTotalWeight() + "");
      showMonthly(primaryStage);
    });
  }

  /**
   * 
   * @param buttons
   * @param index
   */
  private void underliner(Button[] buttons, int index) {
    for (Button b : buttons)
      b.setUnderline(false);
    buttons[index].setUnderline(true);
  }

  /**
   * 
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setResizable(false);
    primaryStage.setTitle("Milky Way");
    setupScene(primaryStage);
    showData(primaryStage);
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }

  /**
   * 
   * @param primaryStage
   */
  private void setupScene(Stage primaryStage) {
    root = new BorderPane();
    mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    leftPanel = new VBox();
    leftTop = new HBox();
    rightTop = new VBox();
    rightPanel = new VBox();
    csvTable = new TableView<>();
    report = new FarmReport();
    total = new Label();
    dataList = FXCollections.observableArrayList(report.getAllList());
    farmChart = new PieChart();
    monthChart = new PieChart();
    yearChart = new PieChart();
    rightBottom = new HBox();
    d_grid = new GridPane();
    topB = new Button[] {new Button("DATA"), new Button("FARM"), new Button("ANNUAL"),
        new Button("MONTHLY")};

    csvTable.setItems(dataList);
    csvTable.setFocusTraversable(false);
    csvTable.prefHeightProperty().bind(primaryStage.heightProperty());
    csvTable.prefWidthProperty().bind(topB[0].widthProperty().add(topB[1].widthProperty())
        .add(topB[2].widthProperty()).add(topB[3].widthProperty()).add(80));
    for (Button b : topB)
      b.setFocusTraversable(false);

    // Action Event: Buttons
    topB[0].setOnAction(e -> {
      showData(primaryStage);

    });
    topB[1].setOnAction(e -> {
      showFarm(primaryStage);

    });
    topB[2].setOnAction(e -> {
      showAnnual(primaryStage);
    });
    topB[3].setOnAction(e -> {
      showMonthly(primaryStage);

    });

    // leftpannel
    leftTop.setPadding(new Insets(10));
    leftTop.setSpacing(10);
    leftTop.getChildren().addAll(topB[0], topB[1], topB[2], topB[3]);
    leftPanel.getChildren().addAll(leftTop, csvTable);
    root.setLeft(leftPanel);

    // rightpannel
    fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("."));
    Cfile = new Button("Select File(s)...");
    Cfile.setFocusTraversable(false);

    // user input
    inputGrid = new GridPane();
    inputGrid.setHgap(10);
    inputGrid.setVgap(10);

    Isearch = new Button("Search");
    Isearch.setFocusTraversable(false);
    Isearch.prefWidthProperty().bind(inputGrid.widthProperty().divide(7));
    Iclear = new Button("Clear");
    Iclear.setFocusTraversable(false);
    Iclear.prefWidthProperty().bind(inputGrid.widthProperty().divide(7));

    Iclear.setOnAction(e -> {
      clearInput();
    });

    farmID = new TextField();
    farmID.setFocusTraversable(false);
    farmID.setPromptText("Enter a farm ID");

    year = new ComboBox<String>();
    year.setFocusTraversable(false);
    year.setPromptText("YEAR");
    month = new ComboBox<String>();
    month.setFocusTraversable(false);
    month.setPromptText("MONTH");
    day = new ComboBox<String>();
    day.setFocusTraversable(false);
    day.setPromptText("DAY");
    for (int i = 2000; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
      year.getItems().add(i + "");
    }
    year.setOnAction(e -> {
      month.getItems().clear();
      month.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
          "12");
    });
    month.setOnAction(e -> {
      day.getItems().clear();
      for (int i = 1; i <= 28; i++) {
        String input = i + "";
        if (input.length() < 2)
          input = "0" + input;
        day.getItems().add(input);
      }
      if (!month.getItems().isEmpty() && !month.getValue().equals("02")) {
        day.getItems().addAll("29", "30");
        if (month.getValue().equals("04") || month.getValue().equals("06")
            || month.getValue().equals("08") || month.getValue().equals("10")
            || month.getValue().equals("12"))
          day.getItems().add("31");
      }
    });

    dyear = new ComboBox<String>();
    dyear.setFocusTraversable(false);
    dyear.setPromptText("YEAR");
    dmonth = new ComboBox<String>();
    dmonth.setFocusTraversable(false);
    dmonth.setPromptText("MONTH");
    dday = new ComboBox<String>();
    dday.setFocusTraversable(false);
    dday.setPromptText("DAY");
    for (int i = 2000; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
      dyear.getItems().add(i + "");
    }
    dyear.setOnAction(e -> {
      dmonth.getItems().clear();
      dmonth.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
          "12");
    });
    dmonth.setOnAction(e -> {
      dday.getItems().clear();
      for (int i = 1; i <= 28; i++) {
        String input = i + "";
        if (input.length() < 2)
          input = "0" + input;
        dday.getItems().add(input);
      }
      if (!dmonth.getItems().isEmpty() && !dmonth.getValue().equals("02")) {
        dday.getItems().addAll("29", "30");
        if (dmonth.getValue().equals("04") || dmonth.getValue().equals("06")
            || dmonth.getValue().equals("08") || dmonth.getValue().equals("10")
            || dmonth.getValue().equals("12"))
          dday.getItems().add("31");
      }
    });

    rightTop.getChildren().addAll(Cfile, inputGrid);
    rightTop.setSpacing(10);

    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    root.setCenter(rightPanel);
    root.setPadding(new Insets(10));
    Label totalWt = new Label("Total Weight:");
    total.setText(report.getTotalWeight() + "");
    totalWt.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    totalWt.setFont(new Font(new Label().getFont().getName(), 16));
    total.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    total.setFont(new Font(new Label().getFont().getName(), 16));
    rightBottom.getChildren().addAll(totalWt, total);


    d_grid.setHgap(10);
    d_grid.setVgap(10);
    d_grid.add(farmChart, 0, 1);
    d_grid.add(monthChart, 1, 1);
    d_grid.add(yearChart, 0, 2);
    farmChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    monthChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    yearChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));

    Label s_label = new Label("Statistic");
    s_label.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    s_label.setFont(new Font(new Label().getFont().getName(), 20));
    d_grid.add(s_label, 0, 0);

    rightPanel.getChildren().addAll(rightTop, d_grid, rightBottom);
  }

  /**
   * 
   * @param columns
   */
  private void setTableColumn(String... columns) {
    TableColumn[] fn = new TableColumn[columns.length];
    int i;
    for (i = 0; i < columns.length; i++) {
      fn[i] = new TableColumn<>(columns[i]);
      fn[i].setCellValueFactory(new PropertyValueFactory<>("f" + (i + 1)));
      fn[i].prefWidthProperty().bind(csvTable.widthProperty()
          .divide((columns.length + 2) * (columns.length + 1) / 2 - 1).multiply(i + 2));
      csvTable.getColumns().add(fn[i]);
    }
    csvTable.getSortOrder().add(fn[0]);
  }

  /**
   * 
   */
  private void clearInput() {
    farmID.clear();
    year.getSelectionModel().clearSelection();
    month.getSelectionModel().clearSelection();
    day.getSelectionModel().clearSelection();
    year.setPromptText("YEAR");
    month.setPromptText("MONTH");
    day.setPromptText("DAY");
    farmID.setPromptText("Enter a farm ID");
    dyear.setPromptText("YEAR");
    dmonth.setPromptText("MONTH");
    dday.setPromptText("DAY");
  }

  /**
   * 
   */
  private void clearBoard() {
    csvTable.getColumns().clear();
    inputGrid.getChildren().clear();
    chartMaker(monthChart, "MONTHLY", 0);
    chartMaker(farmChart, "FARM", 1);
    chartMaker(yearChart, "ANNUAL", 2);
    clearInput();
  }

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }
}
