package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FarmReport {
  HashSet<Farm> dataSet;

  public FarmReport() {
    dataSet = new HashSet<Farm>();
  }

  public void add(Farm farm) {
    for (Farm f : dataSet) {
      if (f.equals(farm))
        return;
    }
    dataSet.add(farm);
  }

  public List<Farm> getAllList() {
    List<Farm> output = new ArrayList<Farm>(dataSet);
    return sortF1(sortF2(output));
  }

  public int getSum() {
    ArrayList<Farm> list = new ArrayList<Farm>(dataSet);
    int output = 0;
    for (Farm f : list)
      output += f.getF3();
    return output;
  }

  /**
   * Returns List of sum of each year-month.
   * 
   * @return List of Farm(year-month, , total weight)
   */
  public List<Farm> getMonthSum(String month) {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (Farm f : dataSet) {
      String key = f.getF2().substring(5, 7);
      if (month == null || month.equals(key))
        if (!map.containsKey(key))
          map.put(key, f.getF3());
        else
          map.replace(key, map.get(key) + f.getF3());
    }
    for (Map.Entry<String, Integer> e : map.entrySet()) {
      output.add(new Farm(e.getKey(), "", e.getValue()));
    }
    return sortF1(output);
  }

  /**
   * Returns List of sum of each year.
   * 
   * @return List of Farm(year, , total weight)
   */
  public List<Farm> getYearSum(String year) {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (Farm f : dataSet) {
      String key = f.getF2().substring(0, 4);
      if (year == null || year.equals(key))
        if (!map.containsKey(key))
          map.put(key, f.getF3());
        else
          map.replace(key, map.get(key) + f.getF3());
    }
    for (Map.Entry<String, Integer> e : map.entrySet()) {
      output.add(new Farm(e.getKey(), "", e.getValue()));
    }
    return sortF1(output);
  }

  /**
   * Returns List of sum of each farm.
   * 
   * @return List of Farm(farm_id, , total weight)
   */
  public List<Farm> getFarmSum(String farm_id) {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (Farm f : dataSet) {
      String key = f.getF1();
      if (farm_id == null || farm_id.equals(key))
        if (!map.containsKey(key))
          map.put(key, f.getF3());
        else
          map.replace(key, map.get(key) + f.getF3());
    }
    for (Map.Entry<String, Integer> e : map.entrySet()) {
      output.add(new Farm(e.getKey(), "", e.getValue()));
    }
    return sortF1(output);
  }

  /**
   * Return list of targeted Farms. Null for argument is equal to all.
   * @param farm_id target farm_id (null equals all)
   * @param year target year (null equals all)
   * @param month target month (null equals all)
   * @return List<Farm> with target Farm objects
   */
  public List<Farm> getTargetSum(String farm_id, String year, String month) {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String[], Integer> map = new HashMap<String[], Integer>();

    for (Farm f : dataSet) {
      String key[] = {f.getF1(), f.getF2().substring(0, 4), f.getF2().substring(5, 7)};
      if ((key[0].equals(farm_id) || farm_id == null) && (key[1].equals(year) || year == null)
          && (key[2].equals(month) || month == null)) {
        if (!map.containsKey(key))
          map.put(key, f.getF3());
        else
          map.replace(key, map.get(key) + f.getF3());
      }
    }
    for (Map.Entry<String[], Integer> e : map.entrySet()) {
      output.add(new Farm(e.getKey()[0], e.getKey()[1] + "-" + e.getKey()[2], e.getValue()));
    }
    return sortF1(sortF2(output));
  }

  private List<Farm> sortF1(List<Farm> list) {
    ArrayList<Farm> output = new ArrayList<Farm>(list);
    for (int i = 0; i < output.size(); i++) {
      int smallest = i;
      for (int j = i; j < output.size(); j++) {
        if (output.get(j).getF1().compareTo(output.get(smallest).getF1()) < 0)
          smallest = j;
      }
      Farm temp = output.get(smallest);
      output.set(smallest, output.get(i));
      output.set(i, temp);
    }
    return output;
  }

  private List<Farm> sortF2(List<Farm> list) {
    ArrayList<Farm> output = new ArrayList<Farm>(list);
    for (int i = 0; i < output.size(); i++) {
      int smallest = i;
      for (int j = i; j < output.size(); j++) {
        if (output.get(j).getF2().compareTo(output.get(smallest).getF2()) < 0)
          smallest = j;
      }
      Farm temp = output.get(smallest);
      output.set(smallest, output.get(i));
      output.set(i, temp);
    }
    return output;
  }

  public void readCSV(File f) throws NumberFormatException, StringIndexOutOfBoundsException {

    String FieldDelimiter = ",";
    BufferedReader br;

    try {
      br = new BufferedReader(new FileReader(f));

      String line;
      while ((line = br.readLine()) != null) {
        String[] fields = line.split(FieldDelimiter, -1);
        if (!fields[1].equals("farm_id")) {

          String farm_id = fields[1];
          String date = fields[0];
          String pweight = fields[2];
          int weight = 0;

          farm_id = farm_id.substring(5);
          while (farm_id.length() < 3) {
            farm_id = "0" + farm_id;
          }

          String[] tempDate = date.split("-");
          while (tempDate[1].length() < 2)
            tempDate[1] = "0" + tempDate[1];
          while (tempDate[2].length() < 2)
            tempDate[2] = "0" + tempDate[2];
          String inDate = tempDate[0] + "-" + tempDate[1] + "-" + tempDate[2];

          weight = Integer.parseInt(pweight);
          add(new Farm(farm_id, inDate, weight));
        }

      }

    } catch (FileNotFoundException ex) {
      Alert a = new Alert(AlertType.ERROR);
      a.setContentText("File(s) canno be found. Program halted.\n" + ex.getMessage());
      a.show();
    } catch (IOException ex) {
      Alert a = new Alert(AlertType.ERROR);
      a.setContentText("Program halted.\n" + ex.getMessage());
      a.show();
    } catch (NumberFormatException ex) {
      Alert a = new Alert(AlertType.ERROR);
      a.setContentText(
          "File contains missing or invalid data. Program halted.\n" + ex.getMessage());
      a.show();
    } catch (StringIndexOutOfBoundsException ex) {
      Alert a = new Alert(AlertType.ERROR);
      a.setContentText(
          "File contains missing or invalid data. Program halted.\n" + ex.getMessage());
      a.show();
    }

  }
}
