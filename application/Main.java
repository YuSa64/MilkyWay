package application;

import java.io.File;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.HashSet;
=======
import java.util.Calendar;
>>>>>>> 67872ea8bff70378526dc975fbd11c2efe83002e
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

<<<<<<< HEAD
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
	private TextField Day;
	private TextField year1;
	private TextField month1;
	private TextField Day1;

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
		d_grid.add(farmChart, 0, 4);
		d_grid.add(monthChart, 1, 4);
		d_grid.add(yearChart, 0, 5);
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
						report.readCSV(f);

				}
			csvTable.setItems(dataList = FXCollections.observableArrayList(report.getAllList()));
			total.setText(report.getSum() + "");
			showData(primaryStage);
		});

		farmID = new TextField("Enter a farm ID");
		year = new TextField("Enter a year");
		month = new TextField("Enter a month 1-12");
		Button submit = new Button("Submit");
		Button clear = new Button("Clear");

		Label farmIDWarning = new Label("ID");
		Label yearWarning = new Label("year");

		d_grid.add(new Label("Farm"), 0, 0);
		d_grid.add(new Label("Year"), 0, 1);
		d_grid.add(new Label("Month"), 0, 2);
		d_grid.add(farmID, 1, 0);
		d_grid.add(year, 1, 1);
		d_grid.add(month, 1, 2);
		d_grid.add(clear, 0, 3);
		d_grid.add(submit, 1, 3);
		d_grid.add(farmIDWarning, 2, 0);
		d_grid.add(yearWarning, 2, 1);
		
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

		clear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				farmID.clear();
				year.clear();
				month.clear();

			}
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
						report.readCSV(f);
				}
			total.setText(report.getSum() + "");
			showFarm(primaryStage);
		});

	}
	

	private void showAnnual(Stage primaryStage) {
		clearBoard();
		underliner(topB, 2);
		
		year = new TextField("Enter a Year");
		d_grid.add(new Label("Date"), 0, 0);
		d_grid.add(year, 1, 0);
		Button submit = new Button("Submit");
		Button clear = new Button("Clear");
		Label dateWarning = new Label("Type Year");
		d_grid.add(submit, 0, 1);
		d_grid.add(clear, 1, 1);
		d_grid.add(dateWarning, 2, 0);
		
		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				if (year.getText() != null ) {
					String yearInput = year.getText();
				} else {
					dateWarning.setText("You must enter a year.");
				}
			}
		});

		clear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				year.clear();
			}
		});
		d_grid.add(farmChart, 0, 2);
		csvTable.setItems(dataList = FXCollections.observableArrayList(report.getFarmSum(null)));
		setTableColumn("FARM", "TOTAL WEIGHT");
		farmChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
		chartMaker(farmChart, "FARM", 0);
		
		Cfile.setOnAction(e -> {
			List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
			if (selectedFiles != null)
				for (File f : selectedFiles) {
						report.readCSV(f);
				}
			total.setText(report.getSum() + "");
			showFarm(primaryStage);
		});
		
		
		
	}

	private void showMonthly(Stage primaryStage) {
		clearBoard();
		underliner(topB, 3);
		
		month = new TextField("Month");
		year = new TextField("Year");
		d_grid.add(new Label("Date"), 0, 0);
		d_grid.add(month, 1, 0);
		d_grid.add(year, 2, 0);
		Button submit = new Button("Submit");
		Button clear = new Button("Clear");
		d_grid.add(submit, 0, 1);
		d_grid.add(clear, 1, 1);
		
		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				if ((year.getText() != null && !month.getText().isEmpty())) {
					String yearInput = year.getText();
					String monthInput = month.getText();
				} 
			}
		});

		clear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				year.clear();
				month.clear();
			}
		});
		
		d_grid.add(farmChart, 0, 2);
		csvTable.setItems(dataList = FXCollections.observableArrayList(report.getFarmSum(null)));
		setTableColumn("FARM", "TOTAL WEIGHT");
		farmChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
		chartMaker(farmChart, "FARM", 0);
		
		Cfile.setOnAction(e -> {
			List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
			if (selectedFiles != null)
				for (File f : selectedFiles) {
						report.readCSV(f);
				}
			total.setText(report.getSum() + "");
			showFarm(primaryStage);
		});
	}

	private void showRange(Stage primaryStage) {
		clearBoard();
		underliner(topB, 4);
		
		year = new TextField("Year");
		month = new TextField("Month");
		Day = new TextField("Day");
		year1 = new TextField("Year");
		month1 = new TextField("Month");
		Day1 = new TextField("Day");
		d_grid.add(new Label("From"), 0, 0);
		d_grid.add(year, 1, 0);
		d_grid.add(month, 2, 0);
		d_grid.add(Day, 3, 0);
		d_grid.add(new Label("To"), 0, 1);
		d_grid.add(year1, 1, 1);
		d_grid.add(month1, 2, 1);
		d_grid.add(Day1, 3, 1);
		
		Button submit = new Button("Submit");
		Button clear = new Button("Clear");
		d_grid.add(submit, 0, 2);
		d_grid.add(clear, 1, 2);
		
		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				if ((month.getText() != null && !year.getText().isEmpty() && !Day.getText().isEmpty())) {
					String DayInput = Day.getText();
					String monthInput = month.getText();
					String yearInput = year.getText();
				}
			}
		});

		clear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				Day.clear();
				month.clear();
				year.clear();
			}
		});
		
		d_grid.add(farmChart, 0, 3);
		csvTable.setItems(dataList = FXCollections.observableArrayList(report.getFarmSum(null)));
		setTableColumn("FARM", "TOTAL WEIGHT");
		farmChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
		chartMaker(farmChart, "FARM", 0);
		
		Cfile.setOnAction(e -> {
			List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
			if (selectedFiles != null)
				for (File f : selectedFiles) {
						report.readCSV(f);
				}
			total.setText(report.getSum() + "");
			showFarm(primaryStage);
		});

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
		topB = new Button[] { new Button("DATA"), new Button("FARM"), new Button("ANNUAL"), new Button("MONTHLY"),
				new Button("RANGE") };
		rightBottom = new HBox();
		d_grid = new GridPane();

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
		Cfile = new Button("Select File(s)...");
		Cfile.setFocusTraversable(false);

		GridPane userGrid = new GridPane();
		
		Label s_label = new Label("Statistic");
		userGrid.add(s_label, 0, 4);
		
		
		s_label.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
		s_label.setFont(new Font(new Label().getFont().getName(), 20));


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

		
		

		rightPanel.getChildren().addAll(rightTop, d_grid, rightBottom);
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

	private void clearBoard() {
		csvTable.getColumns().clear();
		d_grid.getChildren().clear();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
=======
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

  private void chartMaker(PieChart chart, String name, int type, String... strings) {

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    List<Farm> farmList;
    switch (type) {
      case 0:
        if (strings.length != 0)
          farmList = report.getFarmReport(strings[0], strings[1]);
        else
          farmList = report.getFarmReport(null, null);
        break;
      case 1:
        if (strings.length != 0)
          farmList = report.getMonthlyReport(strings[0], strings[1]);
        else
          farmList = report.getMonthlyReport(null, null);
        break;
      case 2:
        if (strings.length != 0)
          farmList = report.getAnnualReport(strings[0]);
        else
          farmList = report.getAnnualReport(null);
        break;
      case 3:
        farmList = report.getRangeReport(strings[0], strings[1], strings[2], strings[3], strings[4],
            strings[5], strings[6], strings[7]);
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

  private void showData(Stage primaryStage) {
    clearBoard();
    underliner(topB, 0);

    csvTable.setItems(dataList = FXCollections.observableArrayList(report.getAllList()));
    setTableColumn("FARM", "DATE", "WEIGHT");
    csvTable.getSortOrder().add(csvTable.getColumns().get(1));

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
    });

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
      total.setText(report.getTotalWeight() + "");
      showData(primaryStage);
    });

  }

  private void showFarm(Stage primaryStage) {
    clearBoard();
    underliner(topB, 1);

    csvTable
        .setItems(dataList = FXCollections.observableArrayList(report.getFarmReport(null, null)));
    setTableColumn("MONTH", "PERCENTAGE", "TOTAL WEIGHT");

    Isearch.setOnAction(e -> {
      if (farmID.getText().equals(""))
        csvTable.setItems(
            FXCollections.observableArrayList(report.getFarmReport(null, year.getValue())));
      else
        csvTable.setItems(FXCollections
            .observableArrayList(report.getFarmReport(farmID.getText(), year.getValue())));
    });

    inputGrid.add(farmID, 1, 0);
    inputGrid.add(year, 2, 0);
    inputGrid.add(Isearch, 4, 0);
    inputGrid.add(Iclear, 5, 0);

    d_grid.add(monthChart, 0, 1);
    d_grid.add(yearChart, 1, 1);
    monthChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    yearChart.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    chartMaker(monthChart, "FARM", 1);
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
      total.setText(report.getTotalWeight() + "");
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

    // user input
    inputGrid = new GridPane();
    inputGrid.setHgap(10);
    inputGrid.setVgap(10);

    Isearch = new Button("Search");
    Isearch.prefWidthProperty().bind(inputGrid.widthProperty().divide(7));
    Iclear = new Button("Clear");
    Iclear.prefWidthProperty().bind(inputGrid.widthProperty().divide(7));

    Iclear.setOnAction(e -> {
      farmID.clear();
      month.getItems().clear();
      day.getItems().clear();
      dyear.setPromptText("YEAR");
      dmonth.setPromptText("MONTH");
      dday.setPromptText("DAY");
      year.setPromptText("YEAR");
      month.setPromptText("MONTH");
      day.setPromptText("DAY");
      farmID.setPromptText("Enter a farm ID");
    });

    farmID = new TextField();
    farmID.setPromptText("Enter a farm ID");
    year = new ComboBox<String>();
    year.setPromptText("YEAR");
    month = new ComboBox<String>();
    month.setPromptText("MONTH");
    day = new ComboBox<String>();
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
    dyear.setPromptText("YEAR");
    dmonth = new ComboBox<String>();
    dmonth.setPromptText("MONTH");
    dday = new ComboBox<String>();
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

    Label s_label = new Label("Statistic");
    s_label.prefWidthProperty().bind(d_grid.widthProperty().divide(2));
    s_label.setFont(new Font(new Label().getFont().getName(), 20));
    d_grid.add(s_label, 0, 0);

    rightPanel.getChildren().addAll(rightTop, d_grid, rightBottom);
  }

  private void setTableColumn(String... columns) {

    TableColumn[] fn = new TableColumn[columns.length];
    int i;
    for (i = 0; i < columns.length; i++) {
      fn[i] = new TableColumn<>(columns[i]);
      fn[i].setCellValueFactory(new PropertyValueFactory<>("f" + (i + 1)));
      fn[i].prefWidthProperty().bind(csvTable.widthProperty()
          .divide((columns.length + 1) * columns.length / 2).multiply(i + 1));
      csvTable.getColumns().add(fn[i]);
    }
    csvTable.getSortOrder().add(fn[0]);
  }

  private void clearBoard() {
    csvTable.getColumns().clear();
    d_grid.getChildren().clear();
    inputGrid.getChildren().clear();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
>>>>>>> 67872ea8bff70378526dc975fbd11c2efe83002e
