package athene.informationView.view.dictionaries;

import java.awt.Component;
import java.util.Vector;

public class Dictionaries {
  

  private Vector<VDictionary> dicts;
  public Dictionaries() { 
    dicts = new Vector<VDictionary>();
  }
  

  public void addDictionary(VDictionary xdict) {
    dicts.add(xdict);
  }
  
  public int getSize() {
    return dicts.size();
  }
  
  
  

  public boolean rmDictionary(VDictionary xdict) {
    return dicts.remove(xdict);
  }
  
  
  public  void lookup(final String text) {
    for (VDictionary d : dicts) {
      d.display(text);
    }
  }
  


  public Component getDictionary(int i) {
    return dicts.get(i).getPanel();
  }


}
