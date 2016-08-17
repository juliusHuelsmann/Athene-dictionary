import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.*;

import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class Test extends JFrame {
  
  //TODO: einen eigenen gradientPaint schreiben.
  BufferedImage bi;
  Rectangle rectB;
  boolean b[][] = null;
    public Test() {
      super("GradientTranslucentWindow");
      Shape shape = new Shape() {
        
        @Override
        public boolean intersects(double x, double y, double w, double h) {
          // TODO Auto-generated method stub
          return false;
        }
        
        @Override
        public boolean intersects(Rectangle2D r) {
          // TODO Auto-generated method stub
          return false;
        }
        
        @Override
        public PathIterator getPathIterator(AffineTransform at, double flatness) {
          // TODO Auto-generated method stub
          return null;
        }
        
        @Override
        public PathIterator getPathIterator(AffineTransform at) {
          // TODO Auto-generated method stub
          return null;
        }
        
        @Override
        public Rectangle2D getBounds2D() {
          return rectB;
        }
        
        @Override
        public Rectangle getBounds() {
          return rectB;
        }
        
        @Override
        public boolean contains(double x, double y, double w, double h) {
         
          for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
              if (contains(i + x, j + y)) {
                return true;
              }
            }
          } 
          return false;
        }
        
        @Override
        public boolean contains(double x, double y) {
          return b[(int) x][(int) y];
        }
        
        @Override
        public boolean contains(Rectangle2D r) {
          return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
        
        @Override
        public boolean contains(Point2D p) {
          return contains(p.getX(), p.getY());
        }
      };
//      setShape(shape);
        super.setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setSize(new Dimension(300,200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D) {
                    final int R = 240;
                    final int G = 240;
                    final int B = 240;

                    Paint p =
                        new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 0),
                            0.0f, getHeight(), new Color(R, G, B, 255), true);
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setPaint(p);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(panel);
        setLayout(new GridBagLayout());
        JButton b = new JButton("I am a Button");
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorder(null);
        add(b);
    }

    public static void main(String[] args) {
        // Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        boolean isPerPixelTranslucencySupported = 
            gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);

        //If translucent windows aren't supported, exit.
        if (!isPerPixelTranslucencySupported) {
            System.out.println(
                "Per-pixel translucency is not supported");
                System.exit(0);
        }

        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              Test gtw = new
                  Test();

                // Display the window.
                gtw.setVisible(true);
            }
        });
    }
}