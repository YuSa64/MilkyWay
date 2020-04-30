package application;

import javafx.beans.property.SimpleStringProperty;

/**
 * Node class to store data in three Strings.
 * @author Jun
 *
 */
public class Farm {
  private SimpleStringProperty f1, f2, f3; //Properties for Data Table

  /**
   * Returns String of f1
   * @return f1
   */
  public String getF1() {
    return f1.get();
  }

  /**
   * Returns String of f2
   * @return f2
   */
  public String getF2() {
    return f2.get();
  }

  /**
   * Returns String of f3
   * @return f3
   */
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
  
  /**
   * Constructor of Farm
   * @param f1 - First String Property (usually Farm ID)
   * @param f2 - Second String Property (usually Date)
   * @param f3 - Third String Property (usually Weight)
   */
  public Farm(String f1, String f2, String f3) {
    this.f1 = new SimpleStringProperty(f1);
    this.f2 = new SimpleStringProperty(f2);
    this.f3 = new SimpleStringProperty(f3);
  }
}
