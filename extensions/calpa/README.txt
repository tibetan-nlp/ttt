                             README

                       CalHTMLPane 2.021
          

-----------------------------------------------------------------------
CONTENTS

    - Overview
    - License for use
    - Requirements
    - Using a CalPane
    - Adding your own components and images
    - Intercepting events from within a document
    - Supported tags and attributes
    - Form components
    - The Pane's Dialog
    - The Pane's Focus Manager
    - Optimize Display
    - Viewing Web pages
    - Bug reports
    - Known problems
    - Contacting the author


-----------------------------------------------------------------------
                              OVERVIEW
-----------------------------------------------------------------------

This is version 2.021 of the CalHTMLPane ('CalPane' or 'Pane'). 

The CalHTMLPane is a sub-component of a Java development package
named Calpa, and was built to provide two main services to the package:

   -Instantly accessible HTML Help documentation without the need to
    start up a native Web browser.
   -The display of HTML documents dynamically authored from online
    databases.

A CalHTMLPane allows Java application builders to incorporate HTML
documents into a GUI display. It supports a wide range of HTML3.2 and
HTML4.0 tags so that documents can be authored with popular HTML
editors and immediately displayed within a Java application. HTML form
components can be included in these documents in the normal way, and a
programming API is provided to enable the controlling Java application
to interact with those components, and to enable programmers to
customize the component within their own applications. The Pane also
contains caching and history features which remove much of the burden
of document management. Keyboard support is provided, including
tabbing between and activating hyperlinks via the keyboard.

The Pane can be used to display Web pages from within an application,
and Web browsing capabilities can be built around it. It can
automatically handle basic form submissions, with more complex server
interaction being dealt with by the programmer if so desired.
However the Pane cannot be expected to handle documents which
incorporate inline scripts. That is not the purpose of its design.

This document gives the class user a general introduction to the
CalHTMLPane's API and discusses certain aspects of its implementation.
It is not necessary to read all of the sections below to begin using
a CalPane, but following the first five examples will give the reader
a good insight into how the calpa.html public classes relate to one
another.

Additional documentation is also provided: JavaDoc for the CalPane's
public classes, and HTML documentation giving examples of tag and
attribute usage. The HTML documents need to be viewed within a
running CalPane, not a normal Web browser, as they demonstrate
aspects of the component's form rendering styles as well as other
features specific to a CalPane. 'CalGuide.html' is the main index
of this documentation.
 
The rest of these notes presume a certain level of Java programming
experience on the part of the reader, and strong familiarity with the
Swing package. 

-----------------------------------------------------------------------
                          LICENSE FOR USE
-----------------------------------------------------------------------

You must read the license that accompanies the calp.html package class
files before you may use them. Briefly, however, and without prejudice
to the terms of the license, you are permitted to use the class files:

     -for non-commercial use.

     -for commercial use, provided that you are only using the class
      files to show either :

         (a) Help, Support or Tutorial documentation within your
             application, and provided that the use of the classes
             is incidental to the main function of that application.
      or:

         (b) Advertising or Promotional material relating to your
             application or business.

For other commercial uses you should email the copyright holder at
the following address stating the desired use of the classes, and
consideration will be given to the grant of a commercial license.

Contact:

       Andrew J. Moulden
       offshore@netcomuk.co.uk


-----------------------------------------------------------------------
                            REQUIREMENTS
-----------------------------------------------------------------------

Version 2.0 of the CalHTMLPane requires a Java 2 Virtual Machine, and
the classes have not been tested against any version of the Java
Development Kit before 1.2 Final. 

This component was built and tested on a Win32 machine running
Windows95. It is believed to be written in 100% Java using Sun's JDK
tools and should perform identically elsewhere.

It is best to give an increased amount of memory to the JVM running a
CalPane. The first instantiation of the Pane (including its static
support classes) uses 300K - 400K of memory, but it is the HTML
documents which take up space. As a general guide (assuming you are
caching documents) :

   -For showing fairly simple Help documentation 2MB of system memory
    should be sufficient.
   -For more complex documentation such as the JDK API, 4MB min is
    recommended if 50+ documents will be viewed and cached.
   -For viewing the sort of documents found on commercial Web sites
    consider 8MB+. The images within such pages can consume a great
    deal of memory.

The amount of memory given to the JVM can be set by using the -ms
switch when a program is run. To give an application 8MB of RAM the
command would be:

                   java -ms8m MyApplication

Memory specification is not mandatory however. The VM will allocate
memory on an as-needed basis, though this may slow document loading.

The caching of documents is programmable, and the CalPane's Manager
will start removing the oldest documents from the cache when it gets
full. Caching of images is left to the JVM.

If the CalHTMLPane is run in conjunction with a Just-In-Time (JIT)
compiler, the first two or three documents  will load quite slowly
and the mouse cursor may not focus hyperlinks for a brief moment.
However there should be a significant performance boost thereafter.
On many machines parsing matches or even surpasses that of a native
Web browser, allowing the disabling of caching if documents are
being loaded from a local file system. 


-----------------------------------------------------------------------
                           USING A CALPANE                     
-----------------------------------------------------------------------
The CalHTMLPane is a Swing JComponent. Specifically it extends the
JLayeredPane class. It is therefore recommended that the component be
used in a 'lightweight' Swing environment rather than mixing it with
'heavyweight' AWT components.

The JAR file containing the CalPane classes (calpahtml.jar) needs to
be in your classpath. The following code should then load and display
an HTML document of your choosing:

----------------------------------------
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
----------------------------------------
Assuming the document has displayed correctly you should find that you
can navigate any hyperlinks. Any links which can't be followed
will result in an error dialog appearing. This dialog will be
discussed later.

Keyboard navigation should be avaialable within the document. We
called requestFocus() for the Pane, and pressing the TAB key should
move focus to the next hyperlink or form control if either exist.

      -SHIFT-TAB will move backwards through the focus cycle.
      -ENTER will activate a hyperlink if one has focus.
      -CURSOR-DOWN or SPACE will scroll the Pane downward if the
       document is long enough.
      -CURSOR-UP or SHIFT_SPACE will scroll upward.
      -CURSOR_LEFT, CURSOR_RIGHT scroll left and right.
      -PAGE_UP and PAGE_DOWN scroll up and down a page at at time

If there is a Frameset document on screen it is possible to tab
between frames by pressing CTRL-TAB (which also gets the user out
of the Pane completely if there are other controls in the
application which can accept keyboard focus).

                     *           *             *

The CalPane is a component, not an application, and so it is left
to the class user to create customized navigation buttons such
as 'back' and 'forward'. However, the Pane has an internal navigation
bar which is useful for testing purposes, and in the next example we
will make this visible.

This introduces another of the calpa.html public classes -
CalHTMLPreferences. A CalHTMLPreferences ('CalPref') object controls
aspects of a CalPane's behaviour and rendering policy - whether to
underline hyperlinks, handle form submission etc. Every Pane has a
controlling CalHTMLPreferences object, and when the first example
above was run, a default CalPref was used. The programmer can
create an instance of CalHTMLPreferences, modify selected values by
using its access methods, and then pass it to a CalPane constructor.
It is possible to change certain preferences after the Pane has been
constructed, but only before construction can *all* preferences be
set. Note also that a single CalPref can control any number of
CalPanes.

So, in the following slightly modified version of the first example we
will create a CalHTMLPreferences object and use one of its public
methods to get the CalPane's test navigation bar showing:

----------------------------------------
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
----------------------------------------

You should see a rather unexciting navigation bar when you run the
program, but at least it is now possible to traverse the CalPane's
history, reload a document, stop any processes if necessary, navigate
by entering a URL in the textfield, and get some status information
on document loading. Note that this navbar is actually *within* the
Pane and is ignored by the Pane's focus manager. You cannot tab to the
controls because they do not exist as far as the Pane is concerned.

(The textfield will accept a single name as an argument. If you type
'amazon' for example it will treat this as 'http://www.amazon.com')

               *           *             *

Designing your own custom controls for a CalHTMLPane is very
straightforward, as demonstrated in Example 3. We will create some
external buttons ('Back', 'Forward', 'Reload' and 'Stop') to
demonstrate how controls can easily be added.

[ Dean Jones' icon collection at:

           http://webart.javalobby.org/jlicons/

  contains a number of icons which are well-suited for buttons to 
  control a CalHTMLPane. Dean has generously made these free
  for use within Java applications.
]


----------------------------------------
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
----------------------------------------

The JavaDoc included in the download gives details regarding the methods
which can be used to directly control a CalHTMLPane. The reader may be
wondering however how the buttons on the test navbar in example 2 'knew'
when to turn on and off. This introduces a third calpa.html public class
- CalHTMLObserver ('CalObs' or 'Observer'). In the same way a Pane always
has a resident Preferences object, it also always has an Observer. Once
again a default CalHTMLObserver is used if one is not passed to the
Pane's constructor.

CalHTMLObserver is an interface, and an Observer can be created for a
CalHTMLPane in one of two ways:

   -by implementing CalHTMLObserver in a class. This means
    implementing all methods of the interface.

   -by extending the public DefaultCalHTMLObserver class. This class
    implements all the methods of CalHTMLObserver as null-ops, so
    only those methods of interest need be overridden.

The methods of CalHTMLObserver are basically all update methods. When
certain events occur within a CalHTMLPane (e.g. a form submit button is
pressed, a hyperlink gets focus, a document has finished loading) the
Pane will pass information about the event to its resident CalObs, and
from these updates the programmer can determine whether buttons should
be on or off, what the title of the current document is, and so on.

A single CalObs can listen to any number of CalPanes, and centralising
events into this single interface is far more efficient than using
normal AWT event firing, additionally avoiding potentially dangerous
synchronization problems. Event multicasting is not normally required
in a component of this type, but if this is necessary the programmer
can maintain listener lists within the CalHTMLObserver implementation.

In the next example we instantiate a CalHTMLPane as before, and this
time add a single label at the bottom in place of the buttons in the
previous example. We create our own CalHTMLObserver by extending
DefaultCalHTMLObserver, and override a single method which allows us
to get updates on focused hyperlinks. We then show the URL of the link
in the label.

----------------------------------------
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
----------------------------------------

When the above example is run, links which get either mouse or
keyboard focus should be shown at the bottom of the frame. When no link
has focus the label's text will be blank (actually, the label does not
clear itself when you go from one document to another, but that would
mean using another update method which would complicate the example).

The example is in fact a little flawed. In the linkFocusedUpdate()
method there is no check made to ensure that the CalHTMLPane sending
the update is the one we originally instantiated. Although it seems
obvious that the CalHTMLPane sending the updates to the CalObserver
*must* be the Pane we created earlier, this is not necessarily so.
HTML allows the author to specify a target frame in hyperlinks, and
if the target is called '_blank' or is not the name of a currently
visible frame, then a new frame should be created to show the linked
document. By default a CalHTMLPane will create a new CalPane if it
comes across a link like this, and this could have happened when the
above example was run.

In most cases the programmer will want a customized version of the
CalHTMLPane to appear in circumstances such as this, and the automatic
opening of new frames can be disabled through a method in
CalHTMLPreferences. When so disabled, the CalPane will call an update
method in CalHTMLObserver requesting the programmer to create a new
CalHTMLPane.


-----------------------------------------------------------------------
           ADDING YOUR OWN COMPONENTS AND IMAGES TO A CALPANE                     
-----------------------------------------------------------------------

There is just one more public calpa.html class that needs to be
discussed - CalHTMLManager ('Manager'). This class consists of a number
of static fields and methods and basically acts as a storage point for
all the CalPanes running in an application. The Manager caches documents
and imagemaps, and performs some other behind-the-scenes
housekeeping functions.

There are also some public methods in CalHTMLManager which allow the
programmer to store JComponents and Images (i.e. instances of
java.awt.Image) with the Manager. Once these are cached with the Manager
they can be incorporated into HTML documents simply by naming them within
a relevant tag. A component or image can be referenced and reused in any
number of documents, and shown in any running CalPane (though of course,
a component can only be in one place at a time).

The ability to add images in this manner was put into the Calpa package
because there are times when it is desirable to display an image which
the class user can be *sure* is available and fully loaded. A document
may for example contain form controls created with the new HTML4.0
<BUTTON> tag (which the CalPane supports) and such buttons can hold
almost any type of HTML content, including images. It might look odd
however if an image which the programmer wants to display in one of these
buttons loads asynchronously. To avoid this the image can be pre-loaded,
added to CalHTMLManager, thus ensuring that it will display immediately
when shown in a document.

For a simple example, try adding this to a test HTML document:

           <IMG jname="cal_warning">

The 'jname' attribute (which is not HTML) tells the CalPane's parser to
use an image that has been cached with CalHTMLManager. Note that there
is no need to use the 'src=..." attribute. The example IMG tag above
will display an exclamation mark image which is used in the CalPane's
error dialog.

For JComponents the <OBJECT> tag or the form <INPUT> tag should be
used. Here are examples of both:

     <OBJECT type=jcomponent jname=mycolorchooser name=chooser>
     <INPUT  type=jcomponent jname=mainmenubar name=menubar>

If a JComponent has been added to CalHTMLManager with the corresponding
name it will be incorporated into the document much like an image or
form control (the name of the component is set by calling the AWT
Component method setName()).

Note that the 'name' attribute is included in the above tags. This is
so that they can be used within an HTML form. Normally when a form is
submitted the browser will gather values from each component and
submit these values as part of the form submission. But how do we find
the 'value' of a custom component? What CalPane does with these custom
objects is to call their toString() method and transmit this with the
form. So, by overriding the toString() method in your custom components
you can pass information out of that component and send it with the
form.

Rather than add JComponents one by one to CalHTMLManager, it is
possible to add an *array* of JComponents. A class name needs to
be specified for the array, and then this array can be referenced
within documents like so:

       <OBJECT type=jcomponent jclass=mycomps jname=mycolorchooser>

Here is some code that will get a JColorChooser showing within an HTML
document, and it also demonstrates another aspect of the CalPane - the
ability to show HTML from a String argument rather than a URL:

----------------------------------------
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


-----------------------------------------------------------------------
             INTERCEPTING EVENTS FROM WITHIN A DOCUMENT                
-----------------------------------------------------------------------

The 'jname' attribute used above in the <IMG>, <OBJECT> and <INPUT>
tags can also be used within the <A> tag. A CalPane will not try and
follow an activated hyperlink which has a jname, but it will pass the
information to its resident CalHTMLObserver. This means that the
programmer can put links within documents which, when activated, will
be sent externally to the CalPane. This is akin to having a hyperlink
with an ActionListener attached to it.

Note that unlike the <IMG> tag where no 'src' attribute is required,
the 'href' attribute must be specified within the <A> tag. This is to
avoid null URL errors being thrown inside the CalPane and also so that
the hyperlink can still be marked as visited. A real URL should not be
specified, but one that Java's URL class will accept as being
valid. So, an <A> tag might look like this:

    <A href="http://www.dummy.com" jname="load_personnel_files">
    Click here to load the personnel files
    </A>

There is also one other way for the programmer to receive data from
within documents. Normally when the HTML <FORM> tag is used, the
'method' attribute will be given one of two arguments: 'post' or
'get'. Within the CalPane however the value 'jform' can also be used.
The Pane will not attempt to handle a 'jform' submission when a
'submit' control is activated. Like the <A> tag above it will simply
pass on all the form data to its CalHTMLObserver where it can be
handled directly by the programmer. For example, the following might
be put:

   <FORM method=jform action="quit_application">
      <INPUT type=submit value="Quit">
   </FORM>

This will create a single 'Quit' button. There may of course be
situations where users need to be able to choose from a range of
buttons. Here is one of the ways of doing this:

   <FORM method=jform action="user_choice">
       <INPUT type=submit name=yes    value=Yes>
       <INPUT type=submit name=no     value=No>
       <INPUT type=submit name=cancel value=Cancel>
       <INPUT type=submit name=help   value=Help>
   </FORM>

When a user activates a 'submit' control in an HTML form, the
activated control is treated as being successful, and so the form
data will include details of which control has been pressed. Thus
if a user pressed the Help button in the above example, the form
data would include the text: help=Help.


-----------------------------------------------------------------------
                  SUPPORTED HTML TAGS AND ATTRIBUTES                   
-----------------------------------------------------------------------

The CalHTMLPane does not support any particular HTML standard. The aim
has simply been to provide a component for the Calpa package which will
display *most* HTML documents and display them at a speed which a
user of that document will find acceptable.

This speed aspect was considered to be of paramount importance, and it
is the reason why style sheet support has not yet been integrated into
the component. The overhead in parsing style data is simply too great
at the current time. However, the CalHTMLPane's structure has been
designed to support style sheets, and adding this at a later date will
only involve very minor code reauthoring.

The second most obvious omission in the CalPane is support for the
<APPLET> tag. There is a version of the CalPane which fully supports
applets, but this is not being released publicly under free license.

Apart from the above, you should find that the CalPane has fairly
wide tag and attribute support. More details and some examples of
tag/attribute usage can be found in the HTML documentation
included in the download. Generally the Pane supports most HTML3.2
tags and a fair number of HTML4.0 ones. In most cases tags have
been implemented according to HTML4.0 specification rather than
HTML3.2.


Here is a brief summary of a CalHTMLPane's tag support:

  -Tables support the <COLGROUP>, <COL>, <THEAD>, <TBODY> and <TFOOT>
   tags, and widths can be absolute, percentage or relative. The
   bgcolor, bordercolor, bordercolorlight and bordercolordark
   attributes allow almost any color rendering to be specified for
   tables, groups, rows, columns and individual cells. The new <TABLE>
   'frame' and 'rules' attributes are also supported.

  -A CalPane can handle Frames including the HTML4.0 <IFRAME> tag,
   allowing the simple inclusion of documents within documents. The
   <IFRAME> tag has yet to be fully exploited by authors - it is
   possible for example to run a timer which periodically updates an
   <IFRAME> with adverts while the rest of the surrounding document
   stays as normal. The effect is very powerful.

  -The <FONT> tag will take a 'face=xxx' argument, but this is limited
   at present. This is due to a major bug in Java 2 relating to system
   fonts. The code for handling system fonts is already within CalPane
   but is currently disabled because of this bug.

  -Client-side image maps are supported, including the 'circle' and
   'poly' shapes. Coordinates can also expressed in percentage rather
   than absolute coordinates, and HTML4.0 recommendation that even
   Internet Explorer 4 has yet to support. The HTML4.0 specification
   states that image maps can be sourced from other documents, but
   this is very difficult to implement. However this is supported to
   a certain extent - the CalPane will cache any image maps it comes
   across, and if the map is referenced in another document it will be
   available from the cache.

  -Images, tables and form components will 'float' (i.e. text and
   other objects will wrap around them) if they are given either an
   'align=left' or 'align=right' alignment argument. Multiple objects
   can be floated on the same line (left and right).

  -Lists should operate much as normal, though Netscape Navigator and
   Internet Explorer treat the DD/DT tags slightly differently under
   certain circumstances. The Pane acts much like Explorer.

  -The <HR> tag will take a color argument 

  -<STRIKE> and <U> (underline) are both implemented, but the <BLINK>
   tag is not as it has almost universally fallen out of use.

  -Forms are supported and are the subject of the next section. Form
   components support the HTML4.0 'accesskey' argument which allows
   key accelerators to be given to controls . The 'tabindex' attribute
   is in the CalHTMLPane code but is currently disabled as it creates
   a very heavy parsing overhead (because it can be used not only with
   form components, but with hyperlinks too).

  -Finally, the CalPane supports a number of tag attributes (mainly
   color-related) which are not officially supposed to exist. A
   document using these attributes will show as normal in any other
   HTML renderer.


-----------------------------------------------------------------------
                          FORM COMPONENTS                   
-----------------------------------------------------------------------

As the CalHTMLPane was tested across different Look&Feels it
immediately became apparent that there were major problems in simply
instantiating form controls and adding them into documents without
modification.


The various Pluggable Look And Feel (PLAF) styles supported by Swing
are designed primarily for application-based programming rather than
what might be termed as 'document-based' or 'content-based' GUI
displays. These PLAFs assume the presence of a consistent background
('control') color against which components will be rendered, and
borders and other style features are incorporated into the components
with this control color in mind. It is possible to change the color of
certain basic controls with methods such as setBackground() and
setForeground(), but on more complex components such as comboboxes
these methods have limited effect.

In an HTML document however, the control color is essentially the
background color of the document itself - which can be any color at
all, including black and white. When standard Swing components are
rendered against such backgrounds there can be a clash (or over-
similarity) of colors which causes carefully programmed style features
to completely disappear, leaving a rather odd-looking GUI control
sitting on the reader's screen.

There are other problems too. Some PLAF controls (Motif buttons for
example) are given large borders *outside of the component itself*
and it becomes impossible to line such components up properly within an
HTML document. Opacity is also an issue, with some components
looking bizarre if they are made opaque, and others almost disappearing
if their opacity is set to false.

In order to try and overcome these difficulties a CalHTMLPane supports
two additional rendering styles - THREEDEE and FLUSH. Rendering form
controls in these styles ensures that the document will be displayed
in exactly the same way without regard for the currently installed
LAF.

There is an obvious downside to this - the form rendering style
incorporated into the document may be out of step with the installed
Look&Feel. However, testing has shown that this is not too much of an
issue. The document reader is not overly aware of any disparity,
particularly if the color of the form control is matched to the
document background. The two CalPane form rendering styles give the
document author considerable freedom in setting component colors by
using attributes within the relevant form tags.

The accompanying HTML documentation contains a number of examples
which demonstrate how to use the Pane's own form rendering styles. The
default rendering style is set through a method in CalHTMLPreferences,
but this does not prevent styles being mixed freely within documents.
It is possible to put a LOOKANDFEEL checkbox next to a THREEDEE button
which is next to a FLUSH textfield. The choice of from component
rendering has therefore been left entirely in the hands of the
programmer/author.


-----------------------------------------------------------------------
                       THE PANE'S DIALOG                  
-----------------------------------------------------------------------

When an error is thrown within a CalPane (for example when an invalid
URL is specified in an activated hyperlink) notification of the failure
is sent to the resident CalHTMLObserver. Strictly speaking it is up to
the programmer to inform the user that such an error has occured, as
the CalPane may only be a small part of a much larger application.

However, the CalHTMLPane has a dialog of sorts which at times can be
extremely useful, not only for error messages, but for obtaining
user-input or displaying a menu on top of HTML documents. By default
this dialog will appear when an error occurs, or for example when a
form POST submission is made, when a warning is given to the user that
the transmission will be unsecure. The showing of these messages can
easily be disabled through a method in CalHTMLPreferences.

'Dialog' is perhaps a misnomer. The CalPane's dialog cannot be modal,
it is not resizable and there is no bar along its top with a close
button on it. It should really be thought of as a blank canvas
which is displayed in the JLayeredPane.MODAL_LAYER of the Pane.

To display the dialog the programmer calls one of the CalPane's
showDialog() methods and pass it a text string. This string is parsed
as HTML and the results are displayed in the dialog much as an HTML
document would be. (The only reason the display of a URL-based
dialog has not been allowed is because fetching the dialog data could
itself throw an error and we would have the problem of showing an error
dialog for an error dialog.)

The dialog can handle all supported HTML tags except for inline frames
and framesets. This means form components, images, hyperlinks, tables,
lists etc. can all be included within the dialog. With a bit of
practice the programmer will see that it is possible to simulate almost
any 'normal' dialog by using HTML, with the added bonus that complex
dialogs can be created which would be extremely difficult to produce
without the power of HTML.

The dialog can be shown anywhere within the Pane, at almost any size.
Events such as button presses can be picked up in the CalHTMLObserver,
and the programmer can add custom Swing components (with listeners
attached) to CalHTMLManager and have them displayed in the dialog using
the <OBJECT> tag. 

The following simple program creates a CalHTMLPane with a JTextArea
below it and a 'Show Dialog' button. HTML can be enetred into the
textarea and when the button is pressed the dialog will appear showing
the rendered HTML. The HTML can be edited and another button press
will update the dialog display.

Here is some example HTML that you might want to try first:

   <TABLE width=100% bgcolor=navy border=1 rules=none cellspacing=0>
      <TR><TD>
         <FONT face=helvetica color=white><B>User Log-on</B></FONT>
      </TD></TR>
   </TABLE>
   <FORM method=jform action=user_logon>
   <TABLE width=100% cellspacing=0>
      <COLGROUP align=center></COLGROUP>
      <TR><TD>
         Please enter your username:
      </TD></TR>
      <TR><TD>
         <INPUT type=password>
      </TD></TR>
      <TR><TD>
         <BR>
         <INPUT type=submit value=Log-On name=logon>
         &nbsp;&nbsp;&nbsp;
         <INPUT type=submit value=Cancel name=cancel>
      </TD></TR>
   </TABLE>
   </FORM>

Some points to note:

     -The dialog has a marginwidth/marginheight of 0. If you want some
      insets around your HTML content, wrap the content in a <TABLE>
      tag with cellspacing or cellpadding set to the desired border.

     -Because we send a width of -1 to the showDialog() method in the
      example, the Pane shows the dialog with a default width. This
      is why the dialog may be wider than the HTML content you enter.

     -Setting the 'bgcolor' attribute in a <BODY> tag will change the
      background color of the dialog, and its border colors as well.

     -When the dialog is displayed it gets keyboard focus. Pressing the
      ESC key will close the dialog.

     -The dialog will not display scrollbars.

     -The dialog's default font sizes are smaller than those used
      within normal documents.

     -Try this: <FORM method=jform action=close_dialog>
                   <INPUT type=submit value=Close>
                </FORM>


----------------------------------------
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


-----------------------------------------------------------------------
                    THE PANE'S FOCUS MANAGER                  
-----------------------------------------------------------------------

When a CalPane is instantiated, it immediately removes the existing
Focus Manager (usually Swing's DefaultFocusManager) and installs a
CalFocusManager to handle keyboard events. This action can be prevented
via a method in CalHTMLManager. 

Swing's DefaultFocusManager uses a top-down, left-right tabbing order
which is not really suitable for HTML documents. The HTML4.0
specification states that the default tab order within documents should
be determined by the order in which entities appear in tags, but this
will not happen with Swing's Manager. If, for example, a button is put
in a document and then a textarea is placed to the right of it, the
textarea will most likely receive focus before the button because it is
taller and hence has a lower y-coordinate on the screen.

Even more importantly, the Swing Manager will only tab between
components, whereas in an HTML document we need to be able to tab
between hyperlinks as well.

Swing only allows a single Focus Manager per thread, so running the
CalFocusManager is an all-or-nothing affair. There is a method in
the JComponent class which looks as if it could solve the problem
of focus management without installing a new Manager: by overriding
isManagingFocus() and returning a value of true it is possible for
a component to handle its own keyboard events. 

Unfortunately Swing's DefaultFocusManager will still manage focus for
components *within* the CalPane, even when the CalPane is handling its
own focus. Nevertheless, we could still solve this if there was a
method in JComponent such as:

   setIsManagingFocus(boolean b)

We could then call this method with an argument of 'true' for all the
CalPane's children. This would cause the Swing Manager to ignore key
events from such components and we could pick these up from a
listener. As far as can be seen however, the only way to tell the
Swing Manager that we are handling focus is to *extend* the component
and override isManagingFocus(). This is totally impractical.

Most users need not worry about the above. The CalFocusManager should
operate in much the same way as Swing's Manager when keyboard events
occur outside the CalPane. Only *inside* the Pane will tabbing be
handled differently.


-----------------------------------------------------------------------
                        OPTIMIZE DISPLAY                  
-----------------------------------------------------------------------

'Optimize display' is a feature of the CalHTMLPane which has yet to be
fully implemented. However, programmers may find it useful in certain
situations.

By default, optimizeDisplay is disabled within the pane. A method in
CalHTMLPreferences allows it to be set to one of two levels:

           CalCons.OPTIMIZE_FONTS
           CalCons.OPTIMIZE_ALL

When OPTIMIZE_FONTS is enabled, the default font size of a document
will change depending on the width of its display area. This can be
quite useful when displaying help documentation in a frameset.
The JDK API documents are a good example. When shown in frames, the
index part of the documentation is displayed in two narrow frames at
the side of the screen. If these are viewed in a CalPane with
OPTIMIZE_FONTS switched on, more of the index is visible because
the fonts used are smaller. The fonts used in the main display frame
will remain as normal.

If the frames are now resized by dragging the mouse it will be seen
that OPTIMIZE_FONTS is dynamic - as soon as a frame's width crosses
a certain threshold, the document will be reformatted with a new base
font size.

OPTIMIZE_ALL takes this a step further by trying to alter certain HTML
parameters in order to fit the current document into its display area.
With HTML tables for example, a table's border, cellspacing and
cellpadding will be progressively reduced until they reach their
absolute minimum levels. Table preferred widths will be overriden where
possible so that column sizes can be reduced. In short, the CalPane
will do whatever it can to avoid horizontal scrolling.

The optimizeDisplay feature is probably most useful when dealing with
technical and support documentation that could be running on any size
of display. It is not wise to use it when trying to show the sort of
graphically-rich HTML pages found on commercial Web sites. Such pages
tend to be formatted by their authors to absolute pixel widths, and
any attempt to scale these pages usually upsets the fine balance of
their design.


-----------------------------------------------------------------------
                        VIEWING WEB PAGES             
-----------------------------------------------------------------------

It must be stressed that if this component is used on the Web the user
will encounter a number of pages which render incompletely. Inline
scripting is becoming very common on the major sites, partcularly for
the dynamic creation of form controls (e.g. the checkboxes which give
suggested extra search parameters on Excite). The CalHTMLPane simply
ignores these scripts.

Programmers should also be aware of how the CalPane handles many of
today's commercial Web pages. These tend to be constructed in a similar
manner: a banner (which often includes an animated GIF) is placed
across the top of the page, and the rest of the document consists of
one or two large tables which format the content to a 640 or 800 pixel
display width.

Like the majority of browsers, the CalPane does not support incremental
table display, so most of the page will not be shown until all the
table data has been received. This is not normally a problem as the
CalPane can parse this data as fast as Java can deliver it over the
Web. However, a difficulty arises when the page contains images of
unspecified width and height.

If an author puts the widths and heights of images within <IMG> tags
then the document format can be determined before the image has even
begun loading. Without this size information however, an HTML renderer
can handle the page in two basic ways:

   -a default size can be given to the image before its true size is
    known. This means that the page can be displayed immediately, but
    has the severe disadvantage that the whole document needs to be
    completely reformatted once the actual image size is determined.
    If there are many such images within the page, the constant
    reformatting is extremely irritating to the user, and can
    considerably lengthen the total time taken to display the page.

   -we can wait until all image sizes are known and then display the
    document in a single pass. The downside here is that the user's
    screen is going to remain blank for some time as the images load.

The CalPane takes the latter approach, and the same effect can be
seen on many Web browsers. Apart from perhaps the page banner
appearing, nothing else is shown until the whole page suddenly
appears fully formatted with text and images. Some document authors
actually favor this display method because the user gets the full
impact of their page design rather than seeing it a piece at a time.

If you are monitoring events from within the Pane via the
statusUpdate() method in CalHTMLObserver you will receive a
WAITING_FOR_IMAGES update when this situation occurs. This message
indicates that the document has been fully parsed but the final
display format cannot be completed until all image sizes are known.

You may also encounter problems with Web pages that contain several
animated GIFs. See the 'known problems' section for details.


-----------------------------------------------------------------------
                            BUG REPORTS
-----------------------------------------------------------------------

Please report any bugs you encounter with the component. It would
be extemely helpful if you could describe exactly the circumstances
under which the problem arose. In particular a sample HTML document
which trips the bug would be most welcome.

Not every apparently incorrect rendering is due to code deficiency.
When testing Web pages it immediately becomes apparent that many
authors do not check to see how their documents display on different
browsers. There are of course only two browsers that really matter,
but page designers may be surprised to see how differently the latest
versions of Explorer and Navigator handle identical HTML. It is
therefore possible that programmers will come across apparent
rendering bugs which are accounted for by a differing interpretation
of the underlying HTML tags.

Send bug reports to:  offshore@netcomuk.co.uk


-----------------------------------------------------------------------
                        KNOWN PROBLEMS              
-----------------------------------------------------------------------

Below are listed the known problems with the CalHTMLPane (in estimated
order of seriousness).

    -There are occasional rendering problems on certain Web pages which
     appear to be caused by incorrect character conversion within the
     Java stream classes. Colors sometime display incorrectly and text
     information disappears. If the Web page is displayed within a
     browser such as Netscape Navigator and saved to a local file, when
     that local file is loaded into the CalPane it displays perfectly.
     If however the character stream sent from Java is saved to a
     local file, the characters are slightly different to those in the
     Navigator file.

     This could be caused by incorrect byte-to-character conversion, or
     it could be that Netscape's programmers have built in some error
     correction into their byte converter which fixes what experience
     has shown to be 'bad' byte sequences. 

                          *           *            *

    -There can be problems with Web pages which contain 5+ animated
     GIFs.

     It does not seem to be documented anywhere, but there appears to
     be a limit to the number of images Java will load asynchronously.
     With an animated GIF however, the imageUpdate method never sets
     the ALLBITS flag because image frames need to be constantly
     streamed in. We get a FRAMEBITS flag instead. This means that to
     keep showing the animated image we never return 'false' to
     imageUpdate, and the image thread is permanently active while the
     page is loaded. 

     Unfortunately this animated image counts towards the asynchronous
     limit and once there are 5 (or whatever the limit is) such GIFs
     running all other image loading becomes blocked.

     One obvious way to deal with this would be to delay the loading of
     animated GIFs until all other images had loaded. Unfortunately
     there is no simple way to tell if an image is a GIF89 until
     loading has begun. If we then halt the GIF once we have
     determined its type to allow other images to load, subsequent
     paint operations on that image later can lead to the throwing of
     a java.lang.SecurityException from within the Sun classes. This
     will not halt the program, but the animated GIF sometimes will not
     display correctly.

     As a consequence certain pages with a high number of GIF89s will
     not load all images, and the DOC_LOADED call to CalHTMLObserver
     will not be made.

                       *           *            *

   - A wait cursor ought to be displayed in the period from when a
     hyperlink is pressed to the opening of the URL connection.
     This cannot currently be implemented due to a Java bug (which
     is peculiar to Win32) which stops the cursor changing until the
     mouse is moved. This can result in the wait cursor continuing
     to display long after a method call to change it back to the
     default cursor.

     Surprisingly JavaSoft engineers refuse to accept the validity of
     this bug, despite constant complaints from developers.

                         *           *            *

   - JScrollPane will sometimes fail to display a horizontal scrollbar
     when one is needed. This problem has been in Swing since its very
     early release. Under certain circumstances the calculations which
     determine whether or not a horizontal scrollbar is required seem
     to fail to take account of the fact that a *vertical* scrollbar
     is going to be displayed, narrowing the viewport width.

                         *           *            *

   - There is a Java 2 bug which can cause incorrect style rendering
     of system fonts. This can cause a whole page of text to be
     rendered in italic for example instead of plain. Java's logical
     fonts are not affected, so until this bug is fixed the support of
     system fonts within CalPane has been disabled.

                         *           *            *

   - Swing contains a bug which prevents the background of a component
     being painted when the component dimensions are very large. This
     can cause some temporarily strange behavior with a loading Web
     page which has a wallpaper background image. The page should
     display correctly when it has finished loading.

                         *           *            *

   - If a document contains an inline frame (created with the
     <IFRAME> tag) and a number of documents have been viewed
     within it, tracking back through the Pane's history will take
     the user back to those documents as normal.However, if the user
     has scrolled up or down the document which contains the IFRAME
     and the IFRAME is no longer in view within the viewport, the
     history changes will not be visible. The user will be pressing
     the BACK button and nothing will appear to be happening.

     In theory the IFRAME should be scrolled into view when history
     events like this take place, but this is very difficult to
     implement because of the way frame histories work. Microsoft's
     programmers have not been able to solve this in Internet
     Explorer 4.

                         *           *            *

   - The setting of percentage widths/heights for images within
     nested entities (i.e. tables) has been disabled. It is clear
     from seeing pages authored with such dimensions that there is
     no consensus as to what the proper rendering should be. HTML4.0
     specifies that the %width/height relates not to the image
     size but to the amount of 'available width' - the space
     between the current left and right margins. But does this
     mean the document width or the TD/TH width? The latter seems
     the obvious answer, but then we find authors using markup such
     as:

            <TABLE><TR><TD>
               <IMG src="pic.jpg" width=20% height=10%>
            </TD></TR></TABLE>

     ...which is chicken-and-egg HTML. There's no way the table size
     can be computed.

                         *           *            *

   - The <INPUT type=file> tag has not been implemented because
     Swing's JFileChooser is still unstable.
 
                         *           *            *

   - There's a minor problem with the Windows L&F. The scrollbar track
     color in this L&F is gray, but it has no border. If a document is
     displayed which has an identical background color to the
     scrollbar track color, the two merge into one and this results in
     a scrollbar thumb that appears to be floating in mid-air.


-----------------------------------------------------------------------
                          ACKNOWLEDGEMENTS
-----------------------------------------------------------------------

Thanks to:

   -Roedy Green, Elliotte Rusty Harold, and Peter van der Linden for
    the wealth of Java information they freely supply.

   -Steve Wilson, co-author of the MetalLookAndFeel, for his patient
    explanations to Swing developers.


-----------------------------------------------------------------------
                        CONTACTING THE AUTHOR              
-----------------------------------------------------------------------

Please contact:

      Andrew Moulden
      offshore@netcomuk.co.uk

Address:

      82A Queens Road,
      Leicester
      LE2 1TU
      United Kingdom
      +44 116 270 5090

