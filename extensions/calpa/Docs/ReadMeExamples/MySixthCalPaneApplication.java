import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import calpa.html.*;

public class MySixthCalPaneApplication {

   public static void main(String args[]) {

      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      d.width  = Math.min(d.width,  800);
      d.height = Math.min(d.height, 600);
      JFrame f = new JFrame();
      CalHTMLPane pane = new CalHTMLPane();
      JPanel p = new JPanel();
      JTextArea ta = new JTextArea();
      ta.setLineWrap(true);
      JScrollPane sp = new JScrollPane(ta);
      sp.setPreferredSize(new Dimension(d.width / 2, d.height / 4));
      JButton b = new JButton("Show Dialog");
      b.addActionListener(new MyListener(pane, ta));
      p.add(sp);
      p.add(b);
      f.getContentPane().add(pane, "Center");
      f.getContentPane().add(p, "South");
      f.setSize(new Dimension(d.width - 10, d.height - 40));
      f.setVisible(true);
   }

   private static class MyListener implements ActionListener {

      CalHTMLPane pane;
      JTextArea   ta;

      public MyListener(CalHTMLPane pane, JTextArea ta) {

         this.pane = pane;
         this.ta   = ta;
      }

      public void actionPerformed(ActionEvent e) {

         pane.showDialog(ta.getText(), null, -1, -1, -1, -1);
      }
   }

}

