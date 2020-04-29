package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    return new ArrayList<Farm>(dataSet);
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
      output.add(new Farm(e.getKey(),
          Double.toString((Double.parseDouble(e.getValue()) / totalWeight * 100)).substring(0, 5)
              + "%",
          e.getValue()));
    }
    return output;
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
      output.add(new Farm(e.getKey(),
          Double.toString((Double.parseDouble(e.getValue()) / totalWeight * 100)).substring(0, 5)
              + "%",
          e.getValue()));
    }
    return output;
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
          Double.toString((Double.parseDouble(e.getValue())
              / monthlyWeight[Integer.parseInt(e.getKey()) - 1] * 100)).substring(0, 5) + "%",
          e.getValue()));
    }
    return output;
  }

  public List<Farm> getAnnual() {
    List<Farm> list = getRangeReport(null, null, null, null, null, null, null, null);
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<Farm> output = new ArrayList<Farm>();
    for (Farm f : list) {
      String key = f.getF2().substring(0, 4);
      if (!map.containsKey(key))
        map.put(key, f.getF3());
      else
        map.replace(key, Integer.parseInt(map.get(key)) + Integer.parseInt(f.getF3()) + "");
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey(),
          Double.toString((Double.parseDouble(e.getValue()) / totalWeight * 100)).substring(0, 5)
              + "%",
          e.getValue()));
    }
    return output;
  }

  /**
   * Return list of targeted Farms. Null for argument is equal to all. All arguments are inclusive.
   * 
   * @param sid starting farm id
   * @param syear starting year
   * @param smonth starting month
   * @param sday starting day
   * @param did ending farm id
   * @param dyear ending year
   * @param dmonth ending month
   * @param dday ending day
   * @return list of targeted farms (farm_id, date, total weight)
   */
  public List<Farm> getRangeReport(String sid, String syear, String smonth, String sday, String did,
      String dyear, String dmonth, String dday) {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, String> map = new HashMap<String, String>();

    String s_id, s_year, s_month, s_day, d_id, d_year, d_month, d_day;
    if (sid == null)
      s_id = "000";
    else
      s_id = sid;
    if (syear == null)
      s_year = "2000";
    else
      s_year = syear;
    if (smonth == null)
      s_month = "01";
    else
      s_month = smonth;
    if (sday == null)
      s_day = "01";
    else
      s_day = sday;
    if (did == null)
      d_id = "999";
    else
      d_id = did;
    if (dyear == null)
      d_year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    else
      d_year = dyear;
    if (dmonth == null)
      d_month = "12";
    else
      d_month = dmonth;
    if (dday == null)
      d_day = "31";
    else
      d_day = dday;

    String skey = s_year + "-" + s_month + "-" + s_day;
    String dkey = d_year + "-" + d_month + "-" + d_day;

    for (Farm f : dataSet) {
      String key = f.getF1();
      String datekey = f.getF2();
      if ((key.compareTo(s_id) >= 0 && datekey.compareTo(skey) >= 0)
          && (key.compareTo(d_id) <= 0 && datekey.compareTo(dkey) <= 0)) {
        if (!map.containsKey(key))
          map.put(key + "-" + datekey, f.getF3());
        else
          map.replace(key + "-" + datekey,
              Integer.parseInt(map.get(key + "-" + datekey)) + Integer.parseInt(f.getF3()) + "");
      }
    }
    for (Map.Entry<String, String> e : map.entrySet()) {
      output.add(new Farm(e.getKey().substring(0, 3), e.getKey().substring(4),
          Integer.parseInt(e.getValue()) + ""));
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
