package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Farm {
  private SimpleStringProperty f1, f2, f3;

  public String getF1() {
    return f1.get();
  }

  public String getF2() {
    return f2.get();
  }

  public String getF3() {
    return f3.get();
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Farm) {
      Farm f = (Farm)o;
      if(f.getF1().equals(f1.get()) && f.getF2().equals(f2.get()) && f.getF3().equals(f3.get()))
        return true;
      else return false;
    }
    else
      return false;
  }
  
  Farm(String farm_id, String date, String weight) {
    this.f1 = new SimpleStringProperty(farm_id);
    this.f2 = new SimpleStringProperty(date);
    this.f3 = new SimpleStringProperty(weight);
  }
}
