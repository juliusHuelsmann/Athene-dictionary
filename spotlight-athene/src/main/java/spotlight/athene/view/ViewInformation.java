package spotlight.athene.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import movelistener.control.Movelistener;
import opaqueFrame.OpFrame;
import spotlight.athene.view.dictionaries.VDictionary;
import spotlight.athene.view.dictionaries.VDictionaryWeb;
import spotlight.athene.view.specializedUtils.DoubleButton;
import spotlight.athene.view.specializedUtils.ViewDictSwitcher;
import utils.Utils;

@SuppressWarnings("serial")
public class ViewInformation extends OpFrame {
  
  
  /**
   * The exit button.
   */
  private DoubleButton jbtnExit;
  
  /**
   * The (preprocessed) background image.
   * @see #preprocessBackground().
   */
  private BufferedImage biWindowShape;
  
  /**
   * The standard size of the JFrame that is not changeable and chosen 
   * according to the size of the background image that is used.
   */
  private final static Dimension dimStandardWindow = new Dimension(350, 450);//= new Dimension(262, 357);
  
  /**
   * The relative displacement of the window towards the location of the
   * word. Depends on the location of the arch inside the 
   * {@link #biWindowShape}.
   * 
   * This value is not always used (the y coordinate may be swapped)
   */
  private final Point pntDisplacementWindow = new Point(-151, 118);
  
  
  /**
   * Contains the screenshot image given by #display function.
   */
  private JLabel jlblBackground;

  
  /**
   * Contains the border of the window;
   */
  private JLabel jlblWindowShape; 
  
  /**
   * The tile
   */
  private JLabel jlblTitle;
  
  
  /**
   * The jPanel that receives the content.
   */
  private JPanel jpnlContent;
  
  private JPanel jpnlSwitcher;
  
  private Vector<ViewDictSwitcher> vecDict;
  private ViewArrow va;
  
  private void init() {
    this.getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);

//    removeNotify();
//    setUndecorated(true);
//    AWTUtilities.setWindowOpaque(this, false); 
  }
  public ViewInformation(final WindowFocusListener wf) {
//    init();
    super.setUndecorated(true);
    super.setLayout(null);
    super.setSize(dimStandardWindow);
    super.setVisible(false);
    super.setResizable(false);
    
//    Movelistener ml = new Movelistener(this);
//    super.addMouseListener(ml);
//    super.addMouseMotionListener(ml);
    vecDict = new Vector<ViewDictSwitcher>();

    jbtnExit = new DoubleButton("x");
    jbtnExit.setBounds(getWidth() - 20, 5, 14, 14);
    super.add(jbtnExit);
    
    jlblTitle = new JLabel("Suchwort");
    jlblTitle.setFont(new Font("Avenir next", Font.PLAIN, 30));
    jlblTitle.setLocation(6, 6);
    jlblTitle.setSize(getWidth(), 35);
    jlblTitle.setOpaque(false);
    jlblTitle.setVerticalTextPosition(SwingConstants.CENTER);
    jlblTitle.setHorizontalTextPosition(SwingConstants.CENTER);
    jlblTitle.setVerticalAlignment(SwingConstants.CENTER);
    jlblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    super.add(jlblTitle);

    jpnlSwitcher = new JPanel();
    jpnlSwitcher.setOpaque(false);
    jpnlSwitcher.setLayout(null);
    jpnlSwitcher.setLocation(jlblTitle.getX(), jlblTitle.getHeight() + jlblTitle.getY());
    jpnlSwitcher.setSize(jlblTitle.getSize());
    super.add(jpnlSwitcher);
    
    jpnlContent = new JPanel();
    jpnlContent.setOpaque(false);
    jpnlContent.setLayout(null);
    jpnlContent.setLocation(jpnlSwitcher.getX(), jpnlSwitcher.getHeight() + jpnlSwitcher.getY());
    jpnlContent.setSize(getAvailableSpace());
    super.add(jpnlContent);
    

    jlblWindowShape = new JLabel();
    jlblWindowShape.setOpaque(false);
    jlblWindowShape.setSize(getSize());
    super.add(jlblWindowShape);

    jlblBackground = new JLabel();
    jlblBackground.setSize(getSize());
//    super.add(jlblBackground);
    

    // load Bi-background
    preprocessShapeBackground();
    


    va = new ViewArrow();
    init();
  }
  
  public void setLocation(final int xx, final int xy) {
	  super.setLocation(xx, xy);
  }
  
  public void setTitle(final String xtitle) {
    jlblTitle.setText(xtitle);
  }
  
  public ViewDictSwitcher addDictionary(final Container c, final Object xtxt, final int i ) {
    ViewDictSwitcher jbtnAdded = new ViewDictSwitcher(xtxt, i);
    final int width = jpnlSwitcher.getHeight() - 4;
    jbtnAdded.setSize(width, width);
    jbtnAdded.setLocation(width * i, 2);
    vecDict.add(i, jbtnAdded);
    jpnlSwitcher.add(jbtnAdded); 
    c.setSize(jpnlContent.getSize());
    jpnlContent.add(c);
    return jbtnAdded;
  }
  private Thread thrdSwap;
  
  private int visibleIndex;
  private Component visibleComp;
  public synchronized void setVisibleDictionary(final Component c, final int xvisibleIndex) {
    
    final int pev = visibleIndex;
    final Component prevC = visibleComp;
    this.visibleIndex = xvisibleIndex;
    this.visibleComp = c;
    
    if (prevC == null) {
      return;
    }
    if (thrdSwap != null) {
      thrdSwap.interrupt();
      thrdSwap = null;
    }
    
    thrdSwap = new Thread() {
      public void run() {
        final int signum;
        if (pev > xvisibleIndex) {
          signum = -1;
        } else if (pev < xvisibleIndex) {
          signum = 1;
        } else {
          signum = 0;
          return;
        }

        c.setLocation(signum * getWidth(), c.getY());
        prevC.setLocation(0, 0);
        c.setVisible(true);
        prevC.setVisible(true);

        final int steps = 30;
        for (int i = 0; i <= steps; i++) {
          c.setLocation(signum * getWidth() * (steps - i) / steps, c.getY());
          prevC.setLocation(-signum * getWidth() * i / steps, prevC.getY());
          try {
            Thread.sleep(6);
          } catch (InterruptedException e) {
            i = steps;
            doOnInterrupt();
            continue;
          }
        }
        doOnInterrupt();
      }
      private void doOnInterrupt() {

        
        for (Component x : jpnlContent.getComponents()) {
            x.setVisible(false);
            x.setLocation(0, 0);
        }
        c.setVisible(true);
        
      }
    };
    thrdSwap.start();
    
    

  }
  
  
  private void preprocessShapeBackground() {
    biWindowShape = preprocessBackground("/spotlight/athene/res/bgShape.png",
        getSize(), new Rectangle(4, 4, dimStandardWindow.width - 9, dimStandardWindow.height - 9), null);

    jlblWindowShape.setIcon(new ImageIcon(biWindowShape));
  }
  
  protected static BufferedImage preprocessBackground(
      final String xpath, final Dimension xdim, final 
      Rectangle xNotProcess, BufferedImage biWindowShapeInverted) {
    
    // load image as RGBA bufferedImage
    BufferedImage processedImg = Utils.resize(xpath, xdim.width, xdim.height);

    
    for (int x = 0; x < processedImg.getWidth(); x++) {
      for (int y = 0; y < processedImg.getHeight(); y++) {

        final Color c = new Color(processedImg.getRGB(x, y));
        if (xNotProcess == null || !(x >= xNotProcess.x && y >= xNotProcess.y 
            && x <= xNotProcess.x + xNotProcess.width 
            && y <= xNotProcess.y + xNotProcess.height)) {
          
          int r = Math.max(0, -80 + c.getRed());
          int g = Math.max(0, -90 + c.getGreen());
          int b = Math.max(0, -120 + c.getBlue());
          final int sum = (r + g + b);
          final int alpha = Math.min(255, Math.max(0, 255 - sum * 255 / (135 + 165 + 175)));
          processedImg.setRGB(x, y, new Color(80, 90, 120, alpha).getRGB());
          processedImg.setRGB(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB());
        } 
        
        if (biWindowShapeInverted != null) {
          biWindowShapeInverted.setRGB(processedImg.getWidth() - x - 1,
              processedImg.getHeight() - y - 1, processedImg.getRGB(x, y));
        }
      }
    }
    return processedImg;
  }
  
  
  public void display(final BufferedImage xbiScreenshot, 
      final Point xLocationText) {
    

    //
    // Compute the new y coordinate of the window
    final int newYBottomW = xLocationText.y + pntDisplacementWindow.y;
    final int newYBottomA = xLocationText.y + va.pntDisplacementArrow.y;
    final int newYTopW = xLocationText.y - pntDisplacementWindow.y - getHeight();
    final int newYTopA = xLocationText.y - va.pntDisplacementArrow.y - va.getHeight();
    final int newYW;
    final int newYA;
    final boolean inverted;
    if (newYBottomW < 0 
        || newYBottomW + getHeight() > Toolkit.getDefaultToolkit().getScreenSize().height) {
      

      inverted = true;
      if (newYTopW < 0 || newYTopW + getHeight() > Toolkit.getDefaultToolkit().getScreenSize().height) {
        
        System.out.println(newYTopW);
        System.out.println("unable to display window 1.");
        return;
      } else {
        newYW = newYTopW;
        newYA = newYTopA;
      }
    } else {
      newYW = newYBottomW;
      newYA = newYBottomA;
      inverted = false;
    }
    
    
    

    //
    // Compute the new x coordinate of the window
    int newXW = xLocationText.x + pntDisplacementWindow.x;
    if (newXW <= 0) {
      newXW = 0;
    }
    
    if (newXW + getWidth() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
      newXW = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth());
      
      if (newXW <= 0) {
        System.out.println("unable to display window.");
        return;
      }
    }
    

    //
    // Compute the new x coordinate of the window
    int newXA = xLocationText.x + va.pntDisplacementArrow.x;
    if (newXA <= 0) {
      newXA = 0;
    }
    
    if (newXA + va.getWidth() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
      newXA = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - va.getWidth());
      
      if (newXA <= 0) {
        System.out.println("unable to display window.");
        return;
      }
    }
    va.setLocation(xbiScreenshot, newXA, newYA, inverted);
    this.setLocation(xbiScreenshot, newXW, newYW);
  }

  public void setVisible(final boolean vis) {
	  super.setVisible(vis);
	  if (va != null) {
		  va.setVisible(vis);
	  }
  }
  public void setLocation(final BufferedImage xbiB, final int x, final int y) {

    super.setLocation(x, y);
    
    //
    //
    // Preprocess the image.
    int[] rgbArray = new int[getWidth() * getHeight()];
    xbiB.getRGB(getX(), getY(), getWidth(), 
        getHeight(), rgbArray, 0, getWidth());
    BufferedImage biBackground = new BufferedImage(getWidth(), getHeight(), 
        BufferedImage.TYPE_INT_RGB);
    biBackground.setRGB(0, 0, getWidth(), getHeight(), rgbArray, 0, getWidth());
    jlblBackground.setIcon(new ImageIcon(biBackground));
    super.setVisible(true);
  }
  /**
   * @return the dimStandardWindow
   */
  public static Dimension getDimStandardWindow() {
    return dimStandardWindow;
  }
  /**
   * @return the dimStandardWindow
   */
  public static Dimension getAvailableSpace() {
    return new Dimension(dimStandardWindow.width - 10,
        dimStandardWindow.height - 83);
  }
  /**
   * @return the vecDict
   */
  public Vector<ViewDictSwitcher> getVecDict() {
    return vecDict;
  }
  /**
   * @return the jbtnExit
   */
  public JButton getJbtnExit() {
    return jbtnExit.getActionSource();
  }

}


