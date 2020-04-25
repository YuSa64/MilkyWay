package application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

  private List<Farm> sortID(List<Farm> list) {
    ArrayList<Farm> temp = new ArrayList<Farm>(list);
    ArrayList<Farm> output = new ArrayList<Farm>();
    for (int i = 0; i < temp.size(); i++) {
      Farm smallestFarm = temp.get(i);
      for(int j = i; j < temp.size(); j++) {
        if(temp.get(j).getF1().compareTo(smallestFarm.getF1()) < 0)
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
      for(int j = i; j < temp.size(); j++) {
        if(temp.get(j).getF2().compareTo(smallestFarm.getF2()) < 0)
          smallestFarm = temp.get(j);
      }
      temp.remove(smallestFarm);
      output.add(smallestFarm);
    }
    return output;
  }
}
