import java.net.*;
import java.awt.*;
import javax.swing.*;
import calpa.html.*;

public class MyFifthCalPaneApplication {

   public static void main(String args[]) {

      String markup = "<H1>Hello World</H1><P align=center><OBJECT type=jcomponent jname=mycolorchooser>";

      //create ColorChooser, name it, add it to CalHTMLManager
      JColorChooser chooser = new JColorChooser();
      chooser.setName("mycolorchooser");
      CalHTMLManager.addUserComponent(chooser);

      JFrame f = new JFrame();
      CalHTMLPane pane = new CalHTMLPane();
      f.getContentPane().add(pane, "Center");
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      f.setSize(new Dimension(Math.min(d.width - 10, 800), Math.min(d.height - 40, 600)));
      f.setVisible(true);
      pane.showHTMLDocument(markup);
   }
}

