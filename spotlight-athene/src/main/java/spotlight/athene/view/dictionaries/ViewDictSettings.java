package spotlight.athene.view.dictionaries;

import java.awt.Dimension;


/**
 * NEEDS TO STAY IN THIS PACKAGE!
 * @author juli
 *
 */
public class ViewDictSettings {

  /**
   * The standard size of the JFrame that is not changeable and chosen 
   * according to the size of the background image that is used.
   */
  public final static Dimension dimStandardWindow = new Dimension(350, 450);//= new Dimension(262, 357);
  
  /**
   * @return the dimStandardWindow
   */
  public static Dimension getAvailableSpace() {
    return new Dimension(dimStandardWindow.width - 10,
        dimStandardWindow.height - 83);
  }
  /**
   * @return the dimStandardWindow
   */
  public static Dimension getDimStandardWindow() {
    return ViewDictSettings.dimStandardWindow;
  }
}
