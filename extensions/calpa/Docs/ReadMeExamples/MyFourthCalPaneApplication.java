import java.net.*;
import java.awt.*;
import javax.swing.*;
import calpa.html.*;

public class MyFourthCalPaneApplication {

   public static void main(String args[]) {

      URL url = null;
      try {
         url = new URL("file:///c:/jdk1.2/docs/api/overview-summary.html");
      } catch (MalformedURLException e) {
         System.err.println("Malformed URL");
         System.exit(1);
      }

      JFrame f = new JFrame();
      JLabel label = new JLabel("0");
      label.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
      label.setPreferredSize(label.getPreferredSize());
      label.setText("");
      CalHTMLPane pane = new CalHTMLPane(null, new MyCalHTMLObserver(label), null);
      f.getContentPane().add(pane, "Center");
      f.getContentPane().add(label, "South");
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      f.setSize(new Dimension(Math.min(d.width - 10, 800), Math.min(d.height - 40, 600)));
      f.setVisible(true);
      if (url != null) {
         pane.showHTMLDocument(url);
      }
   }

  
   private static class MyCalHTMLObserver extends DefaultCalHTMLObserver {

      JLabel label;

      public MyCalHTMLObserver(JLabel label) {

         super();
         this.label = label;
      }

      public void linkFocusedUpdate(CalHTMLPane p, URL url) {

         label.setText((url == null) ? "" : url.toExternalForm());
      }
   }
}
