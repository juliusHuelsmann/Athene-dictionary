package spotlight.athene.view;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings({ "serial" })
class ViewArrow extends opaqueFrame.OpFrame {

  /**
   * The (preprocessed) background image.
   * @see #preprocessBackground().
   */
  private BufferedImage biWindowShape;
  /**
   * The (preprocessed) background image.
   * @see #preprocessBackground().
   */
  private BufferedImage biWindowShapeInverted;
  
  /**
   * The standard size of the JFrame that is not changeable and chosen 
   * according to the size of the background image that is used.
   */
  protected final Dimension dimStandardArrow = new Dimension(39, 118);

  /**
   * The relative displacement of the window towards the location of the
   * word. Depends on the location of the arch inside the 
   * {@link #biWindowShape}.
   */
  protected final Point pntDisplacementArrow = new Point(-35, 5);

  /**
   * Contains the screenshot image given by #display function.
   */
  private JLabel jlblBackground;

  
  /**
   * Contains the border of the window;
   */
  private JLabel jlblWindowShape; 
  
  private void preprocessArchBackground() {

    biWindowShapeInverted = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    biWindowShape = ViewInformation
        .preprocessBackground("/spotlight/athene/res/bgArch.png",
        getSize(), null, biWindowShapeInverted);

    jlblWindowShape.setIcon(new ImageIcon(biWindowShape));
  }
  public ViewArrow() {


    this.getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);
    super.setUndecorated(true);
    super.setLayout(null);
    super.setSize(dimStandardArrow);
    super.setVisible(false);
    super.setResizable(false);

    jlblWindowShape = new JLabel();
    jlblWindowShape.setOpaque(false);
    jlblWindowShape.setSize(getSize());
    super.add(jlblWindowShape);

    jlblBackground = new JLabel();
    jlblBackground.setSize(getSize());
//    super.add(jlblBackground);
    
    preprocessArchBackground();
    new Thread() {
      public void run() {
        //fadeout
        final int maxIter = 50;

        // Determine if the GraphicsDevice supports translucency.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        //If translucent windows aren't supported, exit.
        
        for (int i = 0; i < maxIter && gd.isWindowTranslucencySupported(
            GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT); i++) {
          
        }
      }
    } .start();
  }

  public void setLocation(final BufferedImage xbiB, final int x, final int y, final boolean shifted) {

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
    
    if (shifted) {
      jlblWindowShape.setIcon(new ImageIcon(biWindowShapeInverted));
    } else {
      jlblWindowShape.setIcon(new ImageIcon(biWindowShape));
      
    }
  }

}

