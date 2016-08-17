package athene.informationView.control;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import athene.informationView.view.ViewInformation;
import athene.informationView.view.dictionaries.Dictionaries;
import athene.informationView.view.dictionaries.VDictionary;
import athene.informationView.view.dictionaries.VDictionaryWeb;
import athene.informationView.view.specializedUtils.ViewDictSwitcher;


public class Controller implements WindowFocusListener, ActionListener{
  
  private ViewInformation view;
  private Dictionaries dictionaries;
  
  public Controller() {
    dictionaries = new Dictionaries();
    view = new ViewInformation(this);
    view.getJbtnExit().addActionListener(this);
  }

  public synchronized void addDictionary(VDictionary xdict) {
    final int loc = dictionaries.getSize();
    dictionaries.addDictionary(xdict);

    
    // add view component
    view.addDictionary(xdict.getPanel(), "" + xdict.getIdentifier(), loc).addActionListener(this);
    view.setVisibleDictionary(getDictionary(loc), loc);
    
  }
  public void rmDictionary(VDictionary xdict) {
    dictionaries.rmDictionary(xdict);
    
    // remove view component
  }
  public Component getDictionary(int xdict) {
    return dictionaries.getDictionary(xdict);
  }
  
  
  
  @SuppressWarnings("serial")
  public static void main(String[] args) throws AWTException {

    
    
    Robot r = new Robot();
    Controller c = new Controller();

    c.addDictionary(new VDictionaryWeb() {
     
      @Override
      protected String getURL(String xsearchWord) {
        return "https://m.dict.cc/deen/?s=" + xsearchWord.replaceAll(" ", "+");
      }
      @Override
      protected boolean demandMobileVersion() {
        return true;
      }
      @Override
      protected double getZoomFactor() {
        // TODO Auto-generated method stub
        return 0.8;
      }
      public Object getIdentifier() {
        // TODO Auto-generated method stub
        return "D";
      }
    });

    c.addDictionary(new VDictionaryWeb() {
      
      @Override
      protected String getURL(String xsearchWord) {
        return "http://de.m.wikipedia.org/wiki/" + xsearchWord.replaceAll(" ", "+");
      }

      @Override
      protected boolean demandMobileVersion() {
        return true;
      }
      @Override
      protected double getZoomFactor() {
        // TODO Auto-generated method stub
        return 0.9;
      }
      public Object getIdentifier() {
        // TODO Auto-generated method stub
        return "W";
      }
    });

    c.addDictionary(new VDictionaryWeb() {
      
      @Override
      protected String getURL(String xsearchWord) {
        return "https://www.startpage.com/do/search?lui=deutsch&language=deutsch&cat=web&query=" + xsearchWord.replaceAll(" ", "+") + "&nj=0";
      }

      @Override
      protected boolean demandMobileVersion() {
        return true;
      }
      @Override
      protected double getZoomFactor() {
        // TODO Auto-generated method stub
        return 0.9;
      }
      public Object getIdentifier() {
        // TODO Auto-generated method stub
        return "G";
      }
    });

    c.displayInformationWindow(
        r.createScreenCapture(new Rectangle(0, 0, 
            Toolkit.getDefaultToolkit().getScreenSize().width, 
            Toolkit.getDefaultToolkit().getScreenSize().height)), 
            new Point(500, 80));
    

      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            System.in));
        
        boolean running = true;
        while (running) {
          System.out.println("Enter command:");
          String[] task = reader.readLine().split(" ");

          c.displayInformation(task[0]);
          
        }
        reader.close();
        
      } catch (IOException ex) {
        
      }
    
  }
  
  public void displayInformation(final String xtext) {
    //TODO: find a way to display the information. It would be really nice
    //      to define an interface that decides how the information is
    //      displayed.
    //      Pre-implemented implementations could just display the content
    //      of a dictionary or the mobile version of an html page.
    dictionaries.lookup(xtext);
    view.setTitle(xtext);
    
  }
  
  
  public void displayInformationWindow(
      final BufferedImage biBackground, final Point p) {
    
    view.display(biBackground, p);
    // Load information
    
  }



  public void windowGainedFocus(WindowEvent e) {
    // TODO Auto-generated method stub
    
  }



  public void windowLostFocus(WindowEvent e) {
    System.out.println("xti");
    view.setVisible(false);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(view.getJbtnExit())) {
      view.setVisible(false);
    } else {

      for (ViewDictSwitcher s : view.getVecDict()) {
        if (e.getSource().equals(s.getActionSource())) {
          view.setVisibleDictionary(getDictionary(s.getIndex()), s.getIndex());
          s.setSelected(true);
        } else {
          s.setSelected(false);
        }
      }
    }
  }
}
