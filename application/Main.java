package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
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

  private static final int WINDOW_WIDTH = 800; // Width
  private static final int WINDOW_HEIGHT = 600; // Height
  private BorderPane root; // Root Pane of UI
  private Scene mainScene; // Main Scene of Program
  private VBox leftPanel, rightPanel; // UI Panels
  private HBox leftTop, rightBottom, rightTop; // UI Panels
  private TableView<Farm> csvTable; // Table of Data
  private FarmReport report; // FarmReport of program
  private Button[] tabB; // Tab Buttons
  private FileChooser fileChooser; // FileChooser to choose file
  private Label total, average; // Label to display Total Weight, Average Weight
  private Button selectfile, exportfile, Isearch, Iclear; // Button to Choose File, Extract, Search
                                                          // by input, Clear input
  private GridPane chartgrid, inputGrid; // Grid for PieChart and input section
  private PieChart farmChart, monthChart, yearChart; // PieChart for farm, month, year
  private ObservableList<Farm> dataList; // List for TableView
  private TextField farmID; // User input for Farm ID
  private ComboBox<String> year, month, day, dyear, dmonth, dday; // User input for year, month, day

  /**
   * Make chart based on data
   * 
   * @param chart - Chart to make
   * @param title - Title of Chart
   * @param type - type of Chart
   */
  private void chartMaker(PieChart chart, String title, int type) {
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    List<Farm> farmList;
    switch (type) {
      case 0: // Chart of Each Chart
        farmList = report.getFarmReport(null, null);
        break;
      case 1: // Chart of Each Month
        farmList = report.getAnnualMonthlyReport(null, null);
        break;
      case 2: // Chart of Each Year
        farmList = report.getAnnualData();
        break;
      default: // Chart of Each Data
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
    chart.setTitle(title);
    chart.setLabelsVisible(true);
  }

  /**
   * Show Data/Range report tab
   * 
   * @param primaryStage - Stage of Program
   */
  private void showData(Stage primaryStage) {
    clearTab();
    underliner(tabB, 0);


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

    average.setText("Average Daily Weight: \n"
        + report.getDailyAverage(null, null, null, null, null, null)
        + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
        + "\nAverage Annual Weight: \n" + report.getAnnualAverage() + "\nAverage Farm Weight: \n"
        + report.getFarmAverage(null, null) + "\nTable Average Weight: \n"
        + report.getDailyAverage(null, null, null, null, null, null));

    Isearch.setOnAction(e -> {
      csvTable.setItems(FXCollections
          .observableArrayList(report.getRangeReport(null, year.getValue(), month.getValue(),
              day.getValue(), null, dyear.getValue(), dmonth.getValue(), dday.getValue())));
      csvTable.getSortOrder().addAll(csvTable.getColumns().get(0), csvTable.getColumns().get(1));
      average.setText("Average Daily Weight: \n"
          + report.getDailyAverage(null, null, null, null, null, null)
          + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
          + "\nAverage Annual Weight: \n" + report.getAnnualAverage() + "\nAverage Farm Weight: \n"
          + report.getFarmAverage(null, null) + "\nTable Average Weight: \n"
          + report.getDailyAverage(year.getValue(), month.getValue(), day.getValue(),
              dyear.getValue(), dmonth.getValue(), dday.getValue()));
    });

    selectfile.setOnAction(e -> {
      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles == null)
        return;
      for (File f : selectedFiles)
        report.readCSV(f);
      total.setText("Total Weight: " + report.getTotalWeight());
      average.setText("Average Daily Weight: \n"
          + report.getDailyAverage(null, null, null, null, null, null)
          + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
          + "\nAverage Annual Weight: \n" + report.getAnnualAverage() + "\nAverage Farm Weight: \n"
          + report.getFarmAverage(null, null) + "\nTable Average Weight: \n"
          + report.getDailyAverage(null, null, null, null, null, null));
      showData(primaryStage);
    });

  }

  /**
   * Show Farm report tab
   * 
   * @param primaryStage - Stage of Program
   */
  private void showFarm(Stage primaryStage) {
    clearTab();
    underliner(tabB, 1);

    csvTable
        .setItems(dataList = FXCollections.observableArrayList(report.getFarmReport(null, null)));
    setTableColumn("MONTH", "PERCENTAGE", "TOTAL WEIGHT");

    inputGrid.add(farmID, 1, 0);
    inputGrid.add(year, 2, 0);
    inputGrid.add(Isearch, 4, 0);
    inputGrid.add(Iclear, 5, 0);

    average.setText(
        "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
            + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
            + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
            + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
            + "\nTable Average Weight: \n" + report.getMonthlyAverage(null, null));

    Isearch.setOnAction(e -> {
      if (farmID.getText().equals("")) {
        csvTable.setItems(
            FXCollections.observableArrayList(report.getFarmReport(null, year.getValue())));
        average.setText(
            "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
                + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
                + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
                + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
                + "\nTable Average Weight: \n" + report.getMonthlyAverage(null, year.getValue()));
      } else {
        csvTable.setItems(FXCollections
            .observableArrayList(report.getFarmReport(farmID.getText(), year.getValue())));
        average.setText(
            "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
                + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
                + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
                + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
                + "\nTable Average Weight: \n"
                + report.getMonthlyAverage(farmID.getText(), year.getValue()));
      }
      csvTable.getSortOrder().add(csvTable.getColumns().get(0));

    });

    selectfile.setOnAction(e -> {
      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles == null)
        return;
      for (File f : selectedFiles)
        report.readCSV(f);
      total.setText("Total Weight: " + report.getTotalWeight());
      average.setText(
          "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
              + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
              + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
              + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
              + "\nTable Average Weight: \n" + report.getMonthlyAverage(null, null));
      showFarm(primaryStage);
    });

  }

  /**
   * Show Annual report Tab
   * 
   * @param primaryStage - Stage of Program
   */
  private void showAnnual(Stage primaryStage) {
    clearTab();
    underliner(tabB, 2);

    csvTable.setItems(
        dataList = FXCollections.observableArrayList(report.getAnnualMonthlyReport(null, null)));
    setTableColumn("FARM", "PERCENTAGE", "TOTAL WEIGHT");

    inputGrid.add(year, 1, 0);
    inputGrid.add(Isearch, 4, 0);
    inputGrid.add(Iclear, 5, 0);

    average.setText(
        "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
            + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
            + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
            + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
            + "\nTable Average Weight: \n" + report.getFarmAverage(null, null));

    Isearch.setOnAction(e -> {
      csvTable.setItems(
          FXCollections.observableArrayList(report.getAnnualMonthlyReport(year.getValue(), null)));
      average.setText(
          "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
              + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
              + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
              + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
              + "\nTable Average Weight: \n" + report.getFarmAverage(year.getValue(), null));
      csvTable.getSortOrder().add(csvTable.getColumns().get(0));
    });

    selectfile.setOnAction(e -> {
      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles == null)
        return;
      for (File f : selectedFiles)
        report.readCSV(f);
      total.setText("Total Weight: " + report.getTotalWeight());
      average.setText(
          "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
              + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
              + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
              + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
              + "\nTable Average Weight: \n" + report.getFarmAverage(null, null));
      showAnnual(primaryStage);
    });
  }

  /**
   * Show Monthly report Tab
   * 
   * @param primaryStage - Stage of Program
   */
  private void showMonthly(Stage primaryStage) {
    clearTab();
    underliner(tabB, 3);

    csvTable.setItems(
        dataList = FXCollections.observableArrayList(report.getAnnualMonthlyReport(null, null)));
    setTableColumn("FARM", "PERCENTAGE", "TOTAL WEIGHT");

    inputGrid.add(year, 1, 0);
    inputGrid.add(month, 2, 0);
    inputGrid.add(Isearch, 4, 0);
    inputGrid.add(Iclear, 5, 0);

    average.setText(
        "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
            + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
            + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
            + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
            + "\nTable Average Weight: \n" + report.getFarmAverage(null, null));

    Isearch.setOnAction(e -> {
      csvTable.setItems(FXCollections
          .observableArrayList(report.getAnnualMonthlyReport(year.getValue(), month.getValue())));
      csvTable.getSortOrder().add(csvTable.getColumns().get(0));
      average.setText("Average Daily Weight: \n"
          + report.getDailyAverage(null, null, null, null, null, null)
          + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
          + "\nAverage Annual Weight: \n" + report.getAnnualAverage() + "\nAverage Farm Weight: \n"
          + report.getFarmAverage(null, null) + "\nTable Average Weight: \n"
          + report.getFarmAverage(year.getValue(), month.getValue()));
    });

    selectfile.setOnAction(e -> {
      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles == null)
        return;
      for (File f : selectedFiles)
        report.readCSV(f);
      total.setText("Total Weight: " + report.getTotalWeight());
      average.setText(
          "Average Daily Weight: \n" + report.getDailyAverage(null, null, null, null, null, null)
              + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
              + "\nAverage Annual Weight: \n" + report.getAnnualAverage()
              + "\nAverage Farm Weight: \n" + report.getFarmAverage(null, null)
              + "\nTable Average Weight: \n" + report.getFarmAverage(null, null));
      showMonthly(primaryStage);
    });
  }

  /**
   * Set underline for tab buttons
   * 
   * @param buttons - tab buttons
   * @param index - index of button to activate underline
   */
  private void underliner(Button[] buttons, int index) {
    for (Button b : buttons)
      b.setUnderline(false);
    buttons[index].setUnderline(true);
  }

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
   * Setup main Scene
   * 
   * @param primaryStage - Stage of Program
   */
  private void setupScene(Stage primaryStage) {

    // Initialization
    root = new BorderPane();
    mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    leftPanel = new VBox();
    leftTop = new HBox();
    rightTop = new HBox();
    rightPanel = new VBox();
    csvTable = new TableView<>();
    report = new FarmReport();
    total = new Label();
    average = new Label();
    dataList = FXCollections.observableArrayList(report.getAllList());
    farmChart = new PieChart();
    monthChart = new PieChart();
    yearChart = new PieChart();
    rightBottom = new HBox();
    chartgrid = new GridPane();
    tabB = new Button[] {new Button("DATA"), new Button("FARM"), new Button("ANNUAL"),
        new Button("MONTHLY")};
    fileChooser = new FileChooser();
    selectfile = new Button("Select File(s)...");
    exportfile = new Button("Export Table");
    inputGrid = new GridPane();
    Isearch = new Button("Search");
    Iclear = new Button("Clear");
    farmID = new TextField();
    year = new ComboBox<String>();
    month = new ComboBox<String>();
    day = new ComboBox<String>();
    dyear = new ComboBox<String>();
    dmonth = new ComboBox<String>();
    dday = new ComboBox<String>();

    // Left Top Setup: Tab Buttons
    tabB[0].setOnAction(e -> {
      showData(primaryStage);
    });
    tabB[1].setOnAction(e -> {
      showFarm(primaryStage);
    });
    tabB[2].setOnAction(e -> {
      showAnnual(primaryStage);
    });
    tabB[3].setOnAction(e -> {
      showMonthly(primaryStage);
    });

    // Left Bottom Setup: TableView setup
    csvTable.setItems(dataList);
    csvTable.setFocusTraversable(false);
    csvTable.prefHeightProperty().bind(primaryStage.heightProperty());
    csvTable.prefWidthProperty().bind(tabB[0].widthProperty().add(tabB[1].widthProperty())
        .add(tabB[2].widthProperty()).add(tabB[3].widthProperty()).add(80));
    for (Button b : tabB)
      b.setFocusTraversable(false);

    // Left Panel Setup
    leftTop.setPadding(new Insets(10));
    leftTop.setSpacing(10);
    leftTop.getChildren().addAll(tabB[0], tabB[1], tabB[2], tabB[3]);
    leftPanel.getChildren().addAll(leftTop, csvTable);
    root.setLeft(leftPanel);

    // Right Top Setup: FileChooser
    fileChooser.setInitialDirectory(new File("."));
    fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
    selectfile.setFocusTraversable(false);
    exportfile.setFocusTraversable(false);
    exportfile.setOnAction(e -> {
      File file = fileChooser.showSaveDialog(primaryStage);
      if (file == null)
        return;
      Writer writer = null;
      try {
        writer = new BufferedWriter(new FileWriter(file));
        writer.write(
            csvTable.getColumns().get(0).getText() + "," + csvTable.getColumns().get(1).getText()
                + "," + csvTable.getColumns().get(2).getText() + "\n");
        for (Farm f : csvTable.getItems()) {
          writer.write(f.getF1() + "," + f.getF2() + "," + f.getF3() + "\n");
        }
        writer.flush();
        writer.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    });

    // Right Top Setup: User Input
    inputGrid.setHgap(10);
    inputGrid.setVgap(10);
    Isearch.setFocusTraversable(false);
    Isearch.prefWidthProperty().bind(inputGrid.widthProperty().divide(7));
    Iclear.setFocusTraversable(false);
    Iclear.prefWidthProperty().bind(inputGrid.widthProperty().divide(7));
    Iclear.setOnAction(e -> {
      clearInput();
    });
    farmID.setFocusTraversable(false);
    farmID.setPromptText("Enter a farm ID");
    year.setFocusTraversable(false);
    year.setPromptText("YEAR");
    month.setFocusTraversable(false);
    month.setPromptText("MONTH");
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
        if (month.getValue().equals("01") || month.getValue().equals("03")
            || month.getValue().equals("05") || month.getValue().equals("07")
            || month.getValue().equals("08") || month.getValue().equals("10")
            || month.getValue().equals("12"))
          day.getItems().add("31");
      } else if (year.getValue() != null && Integer.parseInt(year.getValue()) % 4 == 0)
        day.getItems().add("29");
    });
    dyear.setFocusTraversable(false);
    dyear.setPromptText("YEAR");
    dmonth.setFocusTraversable(false);
    dmonth.setPromptText("MONTH");
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

    // Right Middle Setup
    chartgrid.setHgap(10);
    chartgrid.setVgap(10);
    chartgrid.add(farmChart, 0, 1);
    chartgrid.add(monthChart, 1, 1);
    chartgrid.add(yearChart, 0, 2);
    chartgrid.add(average, 1, 2);
    average.setText("Average Daily Weight: \n"
        + report.getDailyAverage(null, null, null, null, null, null)
        + "\nAverage Monthly Weight: \n" + report.getMonthlyAverage(null, null)
        + "\nAverage Annual Weight: \n" + report.getAnnualAverage() + "\nAverage Farm Weight: \n"
        + report.getFarmAverage(null, null) + "\nTable Average Weight: \n"
        + report.getDailyAverage(null, null, null, null, null, null));
    farmChart.prefWidthProperty().bind(chartgrid.widthProperty().divide(2));
    monthChart.prefWidthProperty().bind(chartgrid.widthProperty().divide(2));
    yearChart.prefWidthProperty().bind(chartgrid.widthProperty().divide(2));
    Label s_label = new Label("Statistic");
    s_label.prefWidthProperty().bind(chartgrid.widthProperty().divide(2));
    s_label.setFont(new Font(new Label().getFont().getName(), 20));
    chartgrid.add(s_label, 0, 0);

    // Right Bottom Setup
    total.setText("Total Weight: " + report.getTotalWeight());
    total.setFont(new Font(new Label().getFont().getName(), 16));
    rightBottom.getChildren().addAll(total);

    // Right Pannel Setup
    rightTop.getChildren().addAll(selectfile, exportfile);
    rightTop.setSpacing(10);
    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    root.setCenter(rightPanel);
    root.setPadding(new Insets(10));
    rightPanel.getChildren().addAll(rightTop, inputGrid, chartgrid, rightBottom);
  }

  /**
   * Setup Table Columns
   * 
   * @param columns - Name of Column
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
   * Clears Inputs
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
   * Clears Tabs
   */
  private void clearTab() {
    csvTable.getColumns().clear();
    inputGrid.getChildren().clear();
    chartMaker(monthChart, "MONTHLY", 0);
    chartMaker(farmChart, "FARM", 1);
    chartMaker(yearChart, "ANNUAL", 2);
    clearInput();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
