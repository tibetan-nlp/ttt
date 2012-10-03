/*
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the THDL web site 
(http://www.thdl.org/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.bibl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

    /**
    * <p>This class creates a panel that displays all the relevant
    * diacritic characters used in Asian Studies.  In the context of
    * TiblEdit, its constructor takes a TibFrame which is its
    * parent. When the mouse is clicked on any diacritic character
    * displayed in DiacriticPanel, it inserts that character at the
    * cursor of the TibFrame's {@link TextPane}. All characters are,
    * of course, in Unicode. The file that is the list of diacritics
    * is dia.dat in the same directory as this class's class file. It
    * is a flat file list of the hexadecimal codes for the Unicode
    * diacritics.  These are read and converted into characters by
    * DiacriticPanel and they are displayed in the order they are
    * read.  Thus, to reorder, modify, or add diacritics, one needs
    * only to make the appropriate changes to the dia.dat file.</p>
    *
    * @author Than Garson, Tibetan and Himalayan Digital Library */

public class DiacriticPanel extends JPanel implements TibConstants
{

    // Attributes
    protected TibFrame frame;
    protected JTextComponent jtc;
    private TiblEdit controller;
    //private StringTokenizer dataSplitter;
    private char chosenChar;
    private boolean hasPicked; // DLC FIXME: unused
    private boolean isGeneral;

    /**
    * Sets the panel's layout, size, and background, and reads in the diacritic data.
    */
    private void init()
    {
        // Creating content panel to use for frame's content & initialize

        //JPanel charPanel = new JPanel(new BorderLayout());
        setLayout(new GridLayout(0,3));
        setSize(new Dimension(100,450));
        setBackground(Color.white);

        // Split the data, create individual line labels
        try {
            URL url = DiacriticPanel.class.getResource(DIA_DATA);
            if (url == null) {
                System.err.println("Cannot find " + DIA_DATA + "; aborting.");
                System.exit(1);
            }
            InputStreamReader isr = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while(line != null) {
                char diaChar = (char)Long.parseLong(line.trim(),16);
                String diaStr = Character.toString(diaChar);
                JLabel diaLab = getDiaLabel(diaStr);
                this.add(diaLab);
                line = br.readLine();
            }
            br.close();
        } catch (IOException ioe) {
            System.out.println("An IOE caught: " + ioe.getMessage());
            ioe.printStackTrace();
            org.thdl.util.ThdlDebug.noteIffyCode();
        }
    }

    // Accessors
    /**
    * This method takes a character, which is actually a String variable and creates a label
    * with a border and a margin with the supplied character string as the
    * centered text. Adds a MouseListener that is a {@link DiacriticPanel.TiblEditMouseAdapter}. It is called by the {@link #init} method.
    *
    * @param ch - A string variable that is the character associated with this label/button.
    *
    * @return JLabel - the label created.
    */
    public JLabel getDiaLabel(String ch)
    {
        JLabel lab = new JLabel(ch);
        lab.setFont(DEFAULT_FONT);
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        lab.setBorder(new CompoundBorder(new LineBorder(Color.black),BorderFactory.createEmptyBorder(2,2,2,2)));
        if(isGeneral) {
            lab.addMouseListener(new GenMouseAdapter());
        } else {
            lab.addMouseListener(new TiblEditMouseAdapter());
        }
        return lab;

    }
    public void setChosenChar(char aChar)
    {
        chosenChar = aChar;
        hasPicked = true;
    }

    public char getChosenChar()
    {
        char rValue = chosenChar;
        chosenChar = ' ';
        hasPicked = false;
        return rValue;
    }

    public void setController(TiblEdit te)
    {
        controller = te;
    }

    public TiblEdit getController()
    {
        return controller;
    }

    public void setFrame(TibFrame tf)
    {
        frame = tf;
    }

    public JFrame getFrame()
    {
        return frame;
    }

    public void setJTC(JTextComponent jtextc)
    {
        jtc = jtextc;
    }

    public JTextComponent getJTC()
    {
        return jtc;
    }

    // Constructors

    public DiacriticPanel(boolean isGen, Container cont)
    {
        super();
        isGeneral = isGen;
        if(cont instanceof TibFrame) {
            setFrame((TibFrame)cont);
        } else if(cont instanceof JTextComponent) {
            setJTC((JTextComponent)cont);
        }

        init();
        hasPicked = false;
    }

    public DiacriticPanel(TibFrame tf)
    {
        this(false,tf);
        setFrame(tf);
        setController(tf.getController());
    }

    public DiacriticPanel(JTextComponent jtc)
    {
        this(true,jtc);
    }


    // Inner Classes
    //  The Mouse Adapter
    /**
    * <p>
    * This inner class is a MouseAdapter that implements "mousePressed". It is added to a label with a diacritic
    * so that when that label is clicked, the corresponding Unicode diacritic character is inserted in the
    * open document, a {@link TextPane}, at the cursor.
    *</p>
    */

    private class TiblEditMouseAdapter extends MouseAdapter
    {
        /**
        * <p>
        * This version of mousePressed. Takes the {@link TibFrame} supplied with the DiacriticPanel's constructor
        * and gets its {@link TextPane}. It then inserts the Unicode diacritic character from the source
        * label at the cursor of this text pane.
        * </p>
        */
        public void mousePressed(MouseEvent me)
        {
            TextPane tp = frame.getTextPane();
            if(tp.isEditable()) {
                JLabel source = (JLabel)me.getSource();
                String dia = source.getText();
                int pos = tp.getCaret().getDot();
                try {
                    tp.getDocument().insertString(pos,dia,tp.getCharacterAttributes());
                } catch (BadLocationException ble)
                {
                    System.out.println("Bad location exception while inserting diacritic ("
                            +pos+")! \n");
                }
            }
        }
    }

    private class GenMouseAdapter extends MouseAdapter
    {
        /**
        * <p>
        * This version of mousePressed. Takes the {@link TibFrame} supplied with the DiacriticPanel's constructor
        * and gets its {@link TextPane}. It then inserts the Unicode diacritic character from the source
        * label at the cursor of this text pane.
        * </p>
        */
        public void mousePressed(MouseEvent me)
        {
            JLabel source = (JLabel)me.getSource();
            String dia = source.getText();
            jtc.replaceSelection(dia);
        }
    }
    // Main Method for testing
    public static void main(String[] args)
    {/*
        TibFrame tf = new TibFrame("Diacritic Test");
        DiacriticPanel dp = new DiacriticPanel();
        JScrollPane jsp = new JScrollPane(dp,
                                          ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                          ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tf.setContentPane(jsp);
        tf.pack();
        tf.show();*/
    }
}
