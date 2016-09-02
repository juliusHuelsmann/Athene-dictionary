package spotlight.athene.view.dictionaries;

import java.awt.Container;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class VDictionaryLocal extends JPanel implements VDictionary  {

  public Container getPanel() {
    return this;
  }
}
