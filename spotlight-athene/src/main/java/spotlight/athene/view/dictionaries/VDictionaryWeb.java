package spotlight.athene.view.dictionaries;

import java.awt.Container;
import java.awt.Dimension;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


@SuppressWarnings({ "restriction", "serial" })
public abstract class VDictionaryWeb extends JFXPanel implements VDictionary {

  
  
  /**
   * Return the URL.
   * @param   the extracted word that is to be looked up 
   * @return  the url
   */
  protected abstract String getURL(final String xsearchWord);
  
  
  /**
   * Whether or whether not to demand the mobile version. The mobile version
   * often suits the size of the window, but sometimes the link differs for
   * the mobile version and nothing is being displayed in case true is selected.
   * @return  if mobile version is to be displayed
   */
  protected abstract boolean demandMobileVersion();
  
  
  /**
   * Return the zoom factor. <1: smaller display, > 1 zoom in.
   * @return the zoom factor.
   */
  protected abstract double getZoomFactor();
  
  
  

  /**
   * 
   */
  public final void display(final String xword) {

    Platform.runLater(new Runnable() {
        public void run() {
          if (b != null && b.isReady()) {
            b.loadPage(getURL(xword));
          } else {
            final int maxDemands = 50;
            final int sleepTime = 100;
            for (int i = 0; i < maxDemands; i++) {
              try {
                Thread.sleep(sleepTime);
              } catch (InterruptedException e) {
                e.printStackTrace();
                i = maxDemands;
                continue;
              }
              if (b != null && b.isReady()) {
                b.loadPage(getURL(xword));

                i = maxDemands;
                continue;
              } 
            }
          }
        }
   });
  }
  
  
  


  /**
   * Return the JFXPanel.
   */
  public Container getPanel() {
    return this;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * The Browser that is used for searching.
   */
  private Browser b;

  /**
   * Constructor: initializes the Panel and directly searches for text.
   *              In case the search is to be performed later, call other
   *              constructor.
   * 
   * @param xtext the word that is to be searched for.
   */
  public VDictionaryWeb(final String xtext) {
    this(ViewDictSettings.getAvailableSpace(), xtext);
  }
  
  
  /**
   * Constructor: initializes the Panel and directly searches for text.
   *              In case the search is to be performed later, call other
   *              constructor.
   * @param xsize the size of the JPanel's owner.
   * 
   * @param xtext the word that is to be searched for.
   */
  public VDictionaryWeb(final Dimension xsize, final String xtext) {

    super();
    super.setLayout(null);
    super.setOpaque(false);
    super.setSize(xsize);
    Platform.runLater(new Runnable() {
        public void run() {

          b = new Browser(getSize(), demandMobileVersion(), getZoomFactor());
          // This method is invoked on the JavaFX thread
          Scene scene = new Scene(b, getWidth(), getHeight(),
              Color.web("#666970"));
//          scene.getStylesheets().add("webviewsample/BrowserToolbar.css");    
          VDictionaryWeb.this.setScene(scene);
          b.setReady();
        
          
          if (xtext != null) {
            display(xtext);
          }
        }
   });
  }

  /**
   * Constructor: initializes the Panel.
   */
  public VDictionaryWeb() {
    this(ViewDictSettings.getAvailableSpace());
  }

  /**
   * Constructor: initializes the Panel.
   * 
   * @param xsize the size of the JPanel's owner.
   */
  public VDictionaryWeb(final Dimension xsize) {
    this(xsize, null);
  }
  
}


/**
 * Implementation of the Region.
 * 
 * @author Julius Huelsmann
 * @since 1.0
 * @version %I%, %U%
 */
@SuppressWarnings("restriction")
class Browser extends Region {
 
  
  /**
   * Returns whether the Browser is ready to search something. Has to be 
   * set from outside.
   */
  private boolean ready;
  
  /**
   * The only created instance of Webview.
   */
  private final WebView wv;
  
  /**
   * The only created WebEngine instance.
   */
  final WebEngine we;
  
  /**
   * The preferred size (is equal to the size of the owner).
   */
  final Dimension size;
  
  /**
   * Browser: initialize the components and load styles.
   * @param xsize
   */
  public Browser(final Dimension xsize, final boolean xmobileVersion, 
      final double xzoom) {

    //
    // store the size parameter
    this.size = xsize;
    this.ready = false;
    this.setWidth(xsize.width);
    this.setHeight(xsize.height);
    // 
    // initialize WebView and WebEngine
    this.wv = new WebView();
    this.we = wv.getEngine();
    wv.setZoom(xzoom);
    wv.setOnScroll(new EventHandler<javafx.scene.input.ScrollEvent>() {
      public void handle (javafx.scene.input.ScrollEvent event) {

      }
    });
    // for opening in browser http://stackoverflow.com/questions/27047447/customized-context-menu-on-javafx-webview-webengine
    
    //
    // apply styles and apply mobile version in case set up.
    getStyleClass().add("browser");
    if (xmobileVersion) {
      we.setUserAgent("/Android.*Mobile/"); 
    }
    getChildren().add(wv);
  }
  
  public final void loadPage(final String xpage) {

    //
    // load specified URL and add the webView to the scene
    we.load(xpage);
  }
  
  
 
  @Override protected void layoutChildren() {
    double w = getWidth();
    double h = getHeight();
    layoutInArea(wv, 0, 0, w, h, 0,  HPos.CENTER, VPos.CENTER);
  }
 
  @Override protected double computePrefWidth(double xheight) {
    return size.width;
  }
 
  @Override protected double computePrefHeight(double xwidth) {
    return size.height;
  }

  /**
   * @return the ready
   */
  public boolean isReady() {
    return ready;
  }

  /**
   * @param xready the ready to set
   */
  public void setReady() {
    this.ready = true;
  }
}


