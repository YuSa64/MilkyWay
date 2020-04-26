package application;

import java.io.BufferedReader;
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
   * @return List of Farm(year-month, , total weight)
   */
  public List<Farm> getMonthSum() {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    ArrayList<Farm> list = new ArrayList<Farm>(dataSet);
    for (Farm f : list) {
      String key = f.getF2().substring(0, 7);
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
   * @return List of Farm(year, , total weight)
   */
  public List<Farm> getYearSum() {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    ArrayList<Farm> list = new ArrayList<Farm>(dataSet);
    for (Farm f : list) {
      String key = f.getF2().substring(0, 4);
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
   * @return List of Farm(farm_id, , total weight)
   */
  public List<Farm> getFarmSum() {
    ArrayList<Farm> output = new ArrayList<Farm>();
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    ArrayList<Farm> list = new ArrayList<Farm>(dataSet);
    for (Farm f : list) {
      String key = f.getF1();
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

  public void readCSV(String CsvFile) {
    
    // TODO: change filePath to your own path
    String FieldDelimiter = ",";
    BufferedReader br;

    try {
      br = new BufferedReader(new FileReader(CsvFile));

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

          Farm record = new Farm(farm_id, inDate, weight);
          add(record);
        }

      }

    } catch (FileNotFoundException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

  }
}
