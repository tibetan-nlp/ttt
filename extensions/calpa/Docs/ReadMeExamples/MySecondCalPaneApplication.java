import java.net.*;
import java.awt.*;
import javax.swing.*;
import calpa.html.*;

public class MySecondCalPaneApplication {

   public static void main(String args[]) {

      URL url = null;
      try {
         url = new URL("file:///c:/jdk1.2/docs/api/overview-summary.html");
      } catch (MalformedURLException e) {
         System.err.println("Malformed URL");
         System.exit(1);
      }

      JFrame f = new JFrame();

      //create a Preferences object
      CalHTMLPreferences pref = new CalHTMLPreferences();

      //use one of its methods to enable the test navbar
      pref.setShowTestNavBar(true);

      //now pass the pref object to the Pane's constructor
      CalHTMLPane pane = new CalHTMLPane(pref, null, null);
      f.getContentPane().add(pane, "Center");
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      f.setSize(new Dimension(Math.min(d.width - 10, 800), Math.min(d.height - 40, 600)));
      f.setVisible(true);
      if (url != null) {
         pane.showHTMLDocument(url);
      }
   }
}
