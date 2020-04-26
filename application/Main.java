// TODO: Right Panel GUI (Chart, Textfield, etc)

package application;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
	private Label total;
	private Button Cfile;
    PieChart farmChart;
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
		report = new FarmReport();
		total = new Label();
		dataList = FXCollections.observableArrayList(report.getAllList());
		farmChart = new PieChart();
		showData(primaryStage);
		primaryStage.show();
	}

	private void chartMaker(PieChart chart, String name) {

	    
	    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		List<Farm> farmList = report.getFarmSum();
		for (Farm f : farmList) {
		  PieChart.Data d = new PieChart.Data(f.getF1(), f.getF3());
			if (!pieChartData.contains(d)) {
				pieChartData.add(d);
			}
		}
		System.out.println(report.getSum());
		chart.setData(pieChartData);
		chart.setTitle(name);
		chart.setLabelsVisible(true);
	}

	private void setupScene(Stage primaryStage) {
		root = new BorderPane();
		mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		leftPanel = new VBox();
		leftTop = new HBox();
		rightTop = new HBox();
		rightPanel = new VBox();
		topB = new Button[] { new Button("DATA"), new Button("FARM"), new Button("ANNUAL"), new Button("MONTHLY"),
				new Button("RANGE") };

		csvTable = new TableView<>();
		csvTable.setItems(dataList);
		csvTable.setFocusTraversable(false);
		csvTable.prefHeightProperty().bind(primaryStage.heightProperty());
		csvTable.prefWidthProperty().bind(topB[0].widthProperty().add(topB[1].widthProperty())
				.add(topB[2].widthProperty()).add(topB[3].widthProperty()).add(topB[4].widthProperty()).add(60));
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
		Cfile = new Button("Select File");
		Cfile.setFocusTraversable(false);

		rightTop.getChildren().add(Cfile);
		rightPanel.setPadding(new Insets(10));
		rightPanel.setSpacing(10);
		root.setCenter(rightPanel);
		root.setPadding(new Insets(10));
		rightBottom = new HBox();
		Label totalWt = new Label("Total Weight:");
		total.setText(report.getSum() + "");
		totalWt.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
		totalWt.setFont(new Font(new Label().getFont().getName(), 16));
		total.prefWidthProperty().bind(rightBottom.widthProperty().divide(4));
		total.setFont(new Font(new Label().getFont().getName(), 16));
		rightBottom.getChildren().addAll(totalWt, total);
		rightPanel.getChildren().addAll(rightTop, rightBottom);
	}

	private void setTableColumn(String... columns) {
		TableColumn[] fn = new TableColumn[columns.length];
		int i;
		for (i = 0; i < columns.length - 1; i++) {
			fn[i] = new TableColumn<>(columns[i]);
			fn[i].setCellValueFactory(new PropertyValueFactory<>("f" + (i + 1)));
			fn[i].prefWidthProperty()
					.bind(csvTable.widthProperty().divide((columns.length + 1) * columns.length / 2).multiply(i + 1));
			csvTable.getColumns().add(fn[i]);
		}
		fn[i] = new TableColumn<>(columns[i]);
		fn[i].setCellValueFactory(new PropertyValueFactory<>("f3"));
		fn[i].prefWidthProperty()
				.bind(csvTable.widthProperty().divide((columns.length + 1) * columns.length / 2).multiply(i + 1));
		csvTable.getColumns().add(fn[i]);
	}

	private void showData(Stage primaryStage) {
		setupScene(primaryStage);
		underliner(topB[0], topB[1], topB[2], topB[3], topB[4]);

		// CSV setup
		csvTable.setItems(dataList = FXCollections.observableArrayList(report.getAllList()));
		setTableColumn("FARM", "DATE", "WEIGHT");

		// Setup rightPanel
		GridPane d_grid3 = new GridPane();
		d_grid3.setHgap(10);
		d_grid3.setVgap(10);
		Label s_label = new Label("Statistic");
		s_label.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
		s_label.setFont(new Font(new Label().getFont().getName(), 20));
		farmChart.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
		// PieChart monthChart = chartMaker("MONTH");
		// monthChart.prefWidthProperty().bind(d_grid3.widthProperty().divide(2));
		d_grid3.add(s_label, 0, 0);
		d_grid3.add(farmChart, 0, 1);
		// d_grid3.add(monthChart, 1, 1);
		rightPanel.getChildren().add(1, d_grid3);

		Cfile.setOnAction(e -> {
		  File selectedFile = fileChooser.showOpenDialog(primaryStage);
		  report.readCSV(selectedFile.getPath());
		  csvTable.setItems(dataList = FXCollections.observableArrayList(report.getAllList()));
		  total.setText(report.getSum() + "");
		  chartMaker(farmChart, "FARM");
		});
		
		primaryStage.setScene(mainScene);
	}

	private void showFarm(Stage primaryStage) {
		setupScene(primaryStage);
		underliner(topB[1], topB[0], topB[2], topB[3], topB[4]);

		// CSV setup
		csvTable.setItems(dataList = FXCollections.observableArrayList(report.getMonthSum()));
		setTableColumn("MONTH", "TOTAL WEIGHT");
		Cfile.setOnAction(e -> {
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			report.readCSV(selectedFile.getPath());
			csvTable.setItems(dataList = FXCollections.observableArrayList(report.getMonthSum()));
			total.setText(report.getSum() + "");
		});

		// Setup rightPanel

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
