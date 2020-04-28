// TODO: Right Panel GUI (Chart, Textfield, etc)

package application;

import java.io.File;
import java.util.List;
import javafx.application.Application;
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
  private Button Cfile;
  private GridPane d_grid;
  private PieChart farmChart, monthChart, yearChart;
  private ObservableList<Farm> dataList;

  private TextField farmID;
  private TextField year;
  private TextField month;

  private void chartMaker(PieChart chart, String name, int type, String... strings) {

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    List<Farm> farmList;
    switch (type) {
      case 0:
        if(strings.length != 0)
          farmList = report.getFarmSum(strings[0]);
        else
          farmList = report.getFarmSum(null);          
        break;
      case 1:
        if(strings.length != 0)
          farmList = report.getMonthSum(strings[0]);
        else
          farmList = report.getMonthSum(null);   
        break;
      case 2:
        if(strings.length != 0)
          farmList = report.getYearSum(strings[0]);
        else
          farmList = report.getYearSum(null);   
        break;
      case 3:
        farmList = report.getTargetSum(strings[0], strings[1], strings[2]);   
        break;
      default:
        farmList = report.getAllList();
        break;
    }
    for (Farm f : farmList) {
      PieChart.Data d = new PieChart.Data(f.getF1(), f.getF3());
      if (!pieChartData.contains(d)) {
        pieChartData.add(d);
      }
    }
    chart.setData(pieChartData);
    chart.setTitle(name);
    chart.setLabelsVisible(true);
  }

  private void showData(Stage primaryStage) {
    clearBoard();
    underliner(topB, 0);

    csvTable.setItems(dataList = FXCollections.observableArrayList(report.getAllList()));
    setTableColumn("FARM", "DATE", "WEIGHT");
    d_grid.add(farmChart, 0, 1);
    d_grid.add(monthChart, 1, 1);
    d_grid.add(yearChart, 0, 2);
    farmChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    monthChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    yearChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    chartMaker(farmChart, "FARM", 0);
    chartMaker(monthChart, "MONTH", 1);
    chartMaker(yearChart, "YEAR", 2);

    Cfile.setOnAction(e -> {

      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles != null)
        for (File f : selectedFiles) {
          // try {
          report.readCSV(f);
          // } catch (NumberFormatException nfe) {
          // setTableColumn("NOpe", "yep", "nope");
          // } catch (StringIndexOutOfBoundsException nfe) {
          // setTableColumn("NOpe", "yep", "nope");
          // }
        }
      total.setText(report.getSum() + "");
      showData(primaryStage);
    });

  }

  private void showFarm(Stage primaryStage) {
    clearBoard();
    underliner(topB, 1);

    csvTable.setItems(dataList = FXCollections.observableArrayList(report.getMonthSum(null)));
    setTableColumn("MONTH", "TOTAL WEIGHT");
    d_grid.add(monthChart, 0, 1);
    d_grid.add(yearChart, 1, 1);
    monthChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    yearChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    chartMaker(monthChart, "MONTH", 1);
    chartMaker(yearChart, "YEAR", 2);

    Cfile.setOnAction(e -> {
      List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
      if (selectedFiles != null)
        for (File f : selectedFiles) {
          // try {
          report.readCSV(f);
          // } catch (NumberFormatException nfe) {
          // setTableColumn("NOpe", "yep", "nope");
          // } catch (StringIndexOutOfBoundsException nfe) {
          // setTableColumn("NOpe", "yep", "nope");
          // }
        }
      total.setText(report.getSum() + "");
      showFarm(primaryStage);
    });

  }

  private void showAnnual(Stage primaryStage) {
    clearBoard();
    underliner(topB, 2);
  }

  private void showMonthly(Stage primaryStage) {
    clearBoard();
    underliner(topB, 3);

  }

  private void showRange(Stage primaryStage) {
    clearBoard();
    underliner(topB, 4);

  }

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
    topB = new Button[] {new Button("DATA"), new Button("FARM"), new Button("ANNUAL"),
        new Button("MONTHLY"), new Button("RANGE")};
    rightBottom = new HBox();
    d_grid = new GridPane();

    csvTable.setItems(dataList);
    csvTable.setFocusTraversable(false);
    csvTable.prefHeightProperty().bind(primaryStage.heightProperty());
    csvTable.prefWidthProperty()
        .bind(topB[0].widthProperty().add(topB[1].widthProperty()).add(topB[2].widthProperty())
            .add(topB[3].widthProperty()).add(topB[4].widthProperty()).add(60));
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
    topB[4].setOnAction(e -> {
      showRange(primaryStage);
    });

    // leftpannel
    leftTop.setPadding(new Insets(10));
    leftTop.setSpacing(10);
    leftTop.getChildren().addAll(topB[0], topB[1], topB[2], topB[3], topB[4]);
    leftPanel.getChildren().addAll(leftTop, csvTable);
    root.setLeft(leftPanel);

    // rightpannel
    fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("."));
    Cfile = new Button("Select File(s)...");
    Cfile.setFocusTraversable(false);

    GridPane userGrid = new GridPane();

    farmID = new TextField("Enter a farm ID");
    year = new TextField("Enter a year");
    month = new TextField("Enter a month 1-12");
    Button submit = new Button("Submit");
    Button clear = new Button("Clear");

    Label farmIDWarning = new Label("ID");
    Label yearWarning = new Label("year");

    userGrid.add(new Label("Farm"), 0, 0);
    userGrid.add(new Label("Year"), 0, 1);
    userGrid.add(new Label("Month"), 0, 2);
    userGrid.add(farmID, 1, 0);
    userGrid.add(year, 1, 1);
    userGrid.add(month, 1, 2);
    userGrid.add(clear, 0, 3);
    userGrid.add(submit, 1, 3);
    userGrid.add(farmIDWarning, 2, 0);
    userGrid.add(yearWarning, 2, 1);

    // String farmIDInput;
    // String yearInput;
    // String monthInput;
    submit.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent e) {
        if ((farmID.getText() != null && !farmID.getText().isEmpty())) {
          String farmIDInput = farmID.getText();
        } else {
          farmIDWarning.setText("You must enter a farm ID.");
        }
        if ((year.getText() != null && !year.getText().isEmpty())) {
          String yearInput = year.getText();
        } else {
          farmIDWarning.setText("You must enter a year");
        }
      }
    });

    // System.out.println(yearInput);

    // Setting an action for the Clear button
    clear.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent e) {
        farmID.clear();
        year.clear();
        month.clear();

      }
    });

    rightTop.getChildren().addAll(Cfile, userGrid);

    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    root.setCenter(rightPanel);
    root.setPadding(new Insets(10));
    Label totalWt = new Label("Total Weight:");
    total.setText(report.getSum() + "");
    totalWt.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    totalWt.setFont(new Font(new Label().getFont().getName(), 16));
    total.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    total.setFont(new Font(new Label().getFont().getName(), 16));
    rightBottom.getChildren().addAll(totalWt, total);

    d_grid.setHgap(10);
    d_grid.setVgap(10);

    Label s_label = new Label("Statistic");
    s_label.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    s_label.setFont(new Font(new Label().getFont().getName(), 20));
    d_grid.add(s_label, 0, 0);

    rightPanel.getChildren().addAll(rightTop, d_grid, rightBottom);
  }

  private void setTableColumn(String... columns) {

    TableColumn[] fn = new TableColumn[columns.length];
    int i;
    for (i = 0; i < columns.length - 1; i++) {
      fn[i] = new TableColumn<>(columns[i]);
      fn[i].setCellValueFactory(new PropertyValueFactory<>("f" + (i + 1)));
      fn[i].prefWidthProperty().bind(csvTable.widthProperty()
          .divide((columns.length + 1) * columns.length / 2).multiply(i + 1));
      csvTable.getColumns().add(fn[i]);
    }
    fn[i] = new TableColumn<>(columns[i]);
    fn[i].setCellValueFactory(new PropertyValueFactory<>("f3"));
    fn[i].prefWidthProperty().bind(
        csvTable.widthProperty().divide((columns.length + 1) * columns.length / 2).multiply(i + 1));
    csvTable.getColumns().add(fn[i]);
  }

  private void clearBoard() {
    csvTable.getColumns().clear();
    d_grid.getChildren().clear();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
