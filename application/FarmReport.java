package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FarmReport {
  HashSet<Farm> dataSet;

  public FarmReport() {
    dataSet = new HashSet<Farm>();
  }

  public void add(Farm farm) {
    dataSet.add(farm);
  }

  public List<Farm> getAllList() {
    List<Farm> output = new ArrayList<Farm>(dataSet);
    return sortID(sortDate(output));
  }

  public int getSum() {
    int output = 0;
    for (Farm f : dataSet)
      output += f.getF3();
    return output;
  }

  public List<Farm> getMonthSum() {
    return null;
  }

  public List<Farm> getYearSum() {
    return null;
  }

  public List<Farm> getIDSum() {
    return null;
  }

  private List<Farm> sortID(List<Farm> list) {
    ArrayList<Farm> temp = new ArrayList<Farm>(list);
    ArrayList<Farm> output = new ArrayList<Farm>();
    for (int i = 0; i < temp.size(); i++) {
      Farm smallestFarm = temp.get(i);
      for (int j = i; j < temp.size(); j++) {
        if (temp.get(j).getF1().compareTo(smallestFarm.getF1()) < 0)
          smallestFarm = temp.get(j);
      }
      temp.remove(smallestFarm);
      output.add(smallestFarm);
    }
    return output;
  }

  private List<Farm> sortDate(List<Farm> list) {
    ArrayList<Farm> temp = new ArrayList<Farm>(list);
    ArrayList<Farm> output = new ArrayList<Farm>();
    for (int i = 0; i < temp.size(); i++) {
      Farm smallestFarm = temp.get(i);
      for (int j = i; j < temp.size(); j++) {
        if (temp.get(j).getF2().compareTo(smallestFarm.getF2()) < 0)
          smallestFarm = temp.get(j);
      }
      temp.remove(smallestFarm);
      output.add(smallestFarm);
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
