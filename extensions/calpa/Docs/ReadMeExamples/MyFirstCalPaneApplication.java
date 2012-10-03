import java.net.*;
import java.awt.*;
import javax.swing.*;
import calpa.html.*;

public class MyFirstCalPaneApplication {

   public static void main(String args[]) {

      URL url = null;
      try {
         //This is an example URL. You need to format your own.
         //If you use an http URL you'll need an open http connection
         url = new URL("file:///c:/jdk1.2/docs/api/overview-summary.html");
      } catch (MalformedURLException e) {
         System.err.println("Malformed URL");
         System.exit(1);
      }

      JFrame f = new JFrame();
      CalHTMLPane pane = new CalHTMLPane();
      f.getContentPane().add(pane, "Center");
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      f.setSize(new Dimension(Math.min(d.width - 10, 800), Math.min(d.height - 40, 600)));
      f.setVisible(true);
      if (url != null) {
         pane.showHTMLDocument(url);
         pane.requestFocus();
      }
   }
}
