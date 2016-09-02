package spotlight.athene.view.dictionaries;

import java.awt.Container;

public interface VDictionary {
  
  
  /**
   * Return the BufferedImage or string that is used for identifying the
   * Dictionary inside the list of dictionaries (for user).
   * @return
   */
  public abstract Object getIdentifier();
  public abstract void display(final String xword);
  public abstract Container getPanel();
}
