package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Farm {
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
