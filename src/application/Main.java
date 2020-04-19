package application;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{
  
  private static final int WINDOW_WIDTH = 1600;
  private static final int WINDOW_HEIGHT = 900;

  @Override
  public void start(Stage primaryStage) throws Exception {
    BorderPane root = new BorderPane();
    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    HBox topPanel = new HBox();
    HBox centerPanel = new HBox();
    VBox rightPanel = new VBox();
    TableView csvTable = new TableView();
    
    root.setPadding(new Insets(10));
    
    topPanel.setPadding(new Insets(10));
    topPanel.setSpacing(10);
    topPanel.getChildren().addAll(new Button("DATA"), new Button("FARM"), new Button("ANNUAL"), new Button("MONTHLY"), new Button("RANGE"));
    
    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(10);
    rightPanel.getChildren().add(new Text("Sample"));
    
    centerPanel.setPadding(new Insets(10));
    centerPanel.setSpacing(10);
    centerPanel.getChildren().addAll(csvTable, rightPanel); 
    
    TableColumn f1 = new TableColumn("FARM");
    f1.setCellValueFactory(new PropertyValueFactory<>("farm"));
    TableColumn f2 = new TableColumn("DATE");
    f1.setCellValueFactory(new PropertyValueFactory<>("date"));
    TableColumn f3 = new TableColumn("WEIGHT");
    f1.setCellValueFactory(new PropertyValueFactory<>("weight"));
    csvTable.getColumns().addAll(f1, f2, f3);
    
    root.setTop(topPanel);
    root.setCenter(centerPanel);
    primaryStage.setTitle("Milky Way");
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
