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
  int totalWeight;
  int[] monthlyWeight;

  public FarmReport() {
    dataSet = new HashSet<Farm>();
    totalWeight = 0;
    monthlyWeight = new int[12];
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

  public int getTotalWeight() {
    return totalWeight;
  }

  public int getMonthlyWeight(int month) {
    return monthlyWeight[month];
  }

  public List<Farm> getMonthlyReport(String year, String month) {
    List<Farm> list = getRangeReport(null, year, month, null, null, year, month, null);
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, String> map = new HashMap<String, String>();
    for (Farm f : list) {
      String key = f.getF1();
      if (!map.containsKey(key))
        map.put(key, f.getF3());
      else
        map.replace(key, Integer.parseInt(map.get(key)) + Integer.parseInt(f.getF3()) + "");
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey(),(Double.parseDouble(e.getValue()) / totalWeight * 100)
          + "", e.getValue()));
    }
    return sortF1(output);
  }

  public List<Farm> getAnnualReport(String year) {
    List<Farm> list = getRangeReport(null, year, null, null, null, year, null, null);
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, String> map = new HashMap<String, String>();
    for (Farm f : list) {
      String key = f.getF1();
      if (!map.containsKey(key))
        map.put(key, f.getF3());
      else
        map.replace(key, Integer.parseInt(map.get(key)) + Integer.parseInt(f.getF3()) + "");
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey(),(Double.parseDouble(e.getValue()) / totalWeight * 100)
          + "", e.getValue()));
    }
    return sortF1(output);
  }


  public List<Farm> getFarmReport(String farm_id, String year) {
    List<Farm> list = getRangeReport(farm_id, year, null, null, farm_id, year, null, null);
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<Farm> output = new ArrayList<Farm>();
    for (Farm f : list) {
      String key = f.getF2().substring(5, 7);
      if (!map.containsKey(key))
        map.put(key, f.getF3());
      else
        map.replace(key, Integer.parseInt(map.get(key)) + Integer.parseInt(f.getF3()) + "");
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey(),
          (Double.parseDouble(e.getValue()) / monthlyWeight[Integer.parseInt(e.getKey()) - 1] * 100)
              + "",
          e.getValue()));
    }
    return sortF1(output);
  }

  /**
   * Return list of targeted Farms. Null for argument is equal to all. All arguments are inclusive.
   * 
   * @param sid starting farm id
   * @param syear starting year
   * @param smonth starting month
   * @param sday starting day
   * @param eid ending farm id
   * @param eyear ending year
   * @param emonth ending month
   * @param eday ending day
   * @return list of targeted farms (farm_id, date, total weight)
   */
  public List<Farm> getRangeReport(String sid, String syear, String smonth, String sday, String eid,
      String eyear, String emonth, String eday) {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String[], String> map = new HashMap<String[], String>();

    for (Farm f : dataSet) {
      String key[] = {f.getF1(), f.getF2().substring(0, 4), f.getF2().substring(5, 7),
          f.getF2().substring(8, 10)};
      if ((((sid == null) || (key[0].compareTo(sid) >= 0))
          && ((eid == null) || (key[0].compareTo(eid) <= 0)))
          && ((((syear == null) || key[1].compareTo(syear) >= 0))
              && (((eyear == null) || key[1].compareTo(eyear) <= 0)))
          && (((smonth == null) || (key[2].compareTo(smonth) >= 0))
              && ((emonth == null) || (key[2].compareTo(emonth) <= 0)))
          && (((sday == null) || (key[3].compareTo(sday) >= 0))
              && ((eday == null) || (key[3].compareTo(eday) <= 0)))) {
        if (!map.containsKey(key))
          map.put(key, f.getF3());
        else
          map.replace(key, Integer.parseInt(map.get(key)) + Integer.parseInt(f.getF3()) + "");
      }
    }
    for (Map.Entry<String[], String> e : map.entrySet()) {
      output.add(new Farm(e.getKey()[0], e.getKey()[1] + "-" + e.getKey()[2] + "-" + e.getKey()[3],
          Integer.parseInt(e.getValue()) + ""));
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
          totalWeight += weight;
          monthlyWeight[Integer.parseInt(tempDate[1]) - 1] += weight;
          add(new Farm(farm_id, inDate, weight + ""));
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
