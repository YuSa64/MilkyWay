package application;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
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
    TableView<DailyWeight> csvTable = new TableView<>();
    
    rightPanel.setPadding(new Insets(10));
    rightPanel.setSpacing(8);
    rightPanel.getChildren().add(new Text("Sample"));
    
    
    root.setTop(topPanel);
    centerPanel.getChildren().add(csvTable);
    centerPanel.getChildren().add(rightPanel);
    root.setCenter(centerPanel);
    primaryStage.setTitle("Milky Way");
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
