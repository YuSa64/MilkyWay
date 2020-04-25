// TODO: Right Panel GUI (Chart, Textfield, etc)

package application;


import java.io.File;
import javafx.application.Application;
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
import javafx.stage.FileChooser;
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
  private FarmReport report;
  private Button[] topB;
  private FileChooser fileChooser;
  private Button Cfile;
  private ObservableList<Farm> dataList;


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
    dataList = FXCollections.observableArrayList();
    showData(primaryStage);
    primaryStage.show();
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
    topB = new Button[] {new Button("DATA"), new Button("FARM"), new Button("ANNUAL"),
        new Button("MONTHLY"), new Button("RANGE")};
    csvTable = new TableView<>();
    csvTable.setItems(dataList);
    report = new FarmReport();
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

    // left-top
    leftTop.setPadding(new Insets(10));
    leftTop.setSpacing(10);
    leftTop.getChildren().addAll(topB[0], topB[1], topB[2], topB[3], topB[4]);

    leftPanel.getChildren().addAll(leftTop, csvTable);
    root.setLeft(leftPanel);
    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    root.setCenter(rightPanel);
    root.setPadding(new Insets(10));

    // right-top
    fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("."));
    Cfile = new Button("Select File");
    Cfile.setFocusTraversable(false);

    rightTop.getChildren().add(Cfile);
  }

  private void setTableColumn(String... columns) {
    TableColumn[] fn = new TableColumn[columns.length];
    for (int i = 0; i < columns.length; i++) {
      fn[i] = new TableColumn<>(columns[i]);
      fn[i].setCellValueFactory(new PropertyValueFactory<>("f" + (i + 1)));
      fn[i].prefWidthProperty().bind(csvTable.widthProperty()
          .divide((columns.length + 1) * columns.length / 2).multiply(i + 1));
      csvTable.getColumns().add(fn[i]);
    }
  }

  private void showData(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(topB[0], topB[1], topB[2], topB[3], topB[4]);
    setTableColumn("FARM", "DATE", "WEIGHT");
    csvTable.setFocusTraversable(false);
    csvTable.prefHeightProperty().bind(primaryStage.heightProperty());
    csvTable.prefWidthProperty()
        .bind(topB[0].widthProperty().add(topB[1].widthProperty()).add(topB[2].widthProperty())
            .add(topB[3].widthProperty()).add(topB[4].widthProperty()).add(60));
    Cfile.setOnAction(e -> {
      File selectedFile = fileChooser.showOpenDialog(primaryStage);
      report.readCSV(selectedFile.getPath());
      csvTable.setItems(FXCollections.observableArrayList(report.getAllList()));
    });

    // Setup rightPanel
    GridPane d_grid3 = new GridPane();
    d_grid3.setHgap(10);
    d_grid3.setVgap(10);
    Label s_label = new Label("Statistic");
    s_label.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    s_label.setFont(new Font(new Label().getFont().getName(), 20));
    PieChart farmChart = chartMaker("FARM");
    PieChart monthChart = chartMaker("MONTH");
    farmChart.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    monthChart.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
    d_grid3.add(s_label, 0, 0);
    d_grid3.add(farmChart, 0, 1);
    d_grid3.add(monthChart, 1, 1);
    rightBottom = new HBox();
    Label totalWt = new Label("Total Weight:");
    Label total = new Label("(total)");
    totalWt.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    totalWt.setFont(new Font(new Label().getFont().getName(), 16));
    total.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
    total.setFont(new Font(new Label().getFont().getName(), 16));
    rightBottom.getChildren().addAll(totalWt, total);
    rightPanel.getChildren().addAll(rightTop, d_grid3, rightBottom);

    primaryStage.setScene(mainScene);
  }

  private void showFarm(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(topB[1], topB[0], topB[2], topB[3], topB[4]);

    primaryStage.setScene(mainScene);
  }

  private void showAnnual(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(topB[2], topB[0], topB[1], topB[3], topB[4]);

    primaryStage.setScene(mainScene);
  }

  private void showMonthly(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(topB[3], topB[0], topB[1], topB[2], topB[4]);

    primaryStage.setScene(mainScene);
  }

  private void showRange(Stage primaryStage) {
    setupScene(primaryStage);
    underliner(topB[4], topB[0], topB[1], topB[2], topB[3]);

    primaryStage.setScene(mainScene);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
