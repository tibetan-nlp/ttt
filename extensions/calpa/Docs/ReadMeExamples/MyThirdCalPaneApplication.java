import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import calpa.html.*;

public class MyThirdCalPaneApplication {

   public static void main(String args[]) {

      URL url = null;
      try {
         url = new URL("file:///c:/jdk1.2/docs/api/overview-summary.html");
      } catch (MalformedURLException e) {
         System.err.println("Malformed URL");
         System.exit(1);
      }

      JFrame f = new JFrame();
      CalHTMLPane pane = new CalHTMLPane();
      f.getContentPane().add(pane, "Center");

      //create a panel, add buttons, and add a listener to the buttons
      JPanel p  = new JPanel();
      MyListener ml = new MyListener(pane);
      String[] s = {"Reload", "Back", "Forward", "Stop"};
      JButton b;
      for (int i=0; i<4; i++) {
         b = new JButton(s[i]);
         b.addActionListener(ml);
         p.add(b);
      }
      f.getContentPane().add(p, "South");

      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      f.setSize(new Dimension(Math.min(d.width - 10, 800), Math.min(d.height - 40, 600)));
      f.setVisible(true);
      if (url != null) {
         pane.showHTMLDocument(url);
      }
   }

  
   private static class MyListener implements ActionListener {

      CalHTMLPane pane;
 
      public MyListener(CalHTMLPane pane) {

         this.pane = pane;
      }

      public void actionPerformed(ActionEvent e) {

         String s = e.getActionCommand();

         if (("Reload").equals(s)) {
            pane.reloadDocument();
         } else if (("Back").equals(s)) {
            pane.goBack();
         } else if (("Forward").equals(s)) {
            pane.goForward();
         } else if (("Stop").equals(s)) {
            pane.stopAll();
         }
      }
   }
}
