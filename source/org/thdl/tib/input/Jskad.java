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

package org.thdl.tib.input;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.thdl.tib.text.TibetanDocument;
import org.thdl.util.RTFFixerInputStream;
import org.thdl.util.SimpleFrame;
import org.thdl.util.StatusBar;
import org.thdl.util.ThdlActionListener;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlLazyException;
import org.thdl.util.ThdlOptions;
import org.thdl.util.ThdlVersion;

import calpa.html.CalHTMLPane;



/**
* A simple Tibetan text editor. Jskad editors lack most of the
* functionality of a word-processor, but they do provide
* multiple keyboard input methods, as well as 
* conversion routines to go back and forth between Extended
* Wylie and TibetanMachineWeb.
* <p>
* Jskad embeds {@link DuffPane DuffPane}, which is the editing
* window where the keyboard logic and most functionality is housed.
* <p>
* Depending on the object passed to the constructor,
* a Jskad object can be used in either an application or an
* applet.
* @author Edward Garrett, Tibetan and Himalayan Digital Library
* @version 1.0
*/
public class Jskad extends JPanel implements DocumentListener {
    /** Sets the focus to the DuffPane if possible. */
    private void focusToDuffPane() {
        if (null != dp && !dp.hasFocus()) dp.grabFocus();
    }

    /** the name of the property a developer should set to see
        low-level info on how keypresses in "Tibetan" input mode are
        being interpreted */
    private final static String enableKeypressStatusProp
        = "thdl.Jskad.enable.tibetan.mode.status";

    /** the middleman that keeps code regarding Tibetan keyboards
     *  clean */
    private final static JskadKeyboardManager keybdMgr;

    static {
        try {
            keybdMgr
                = new JskadKeyboardManager(JskadKeyboardFactory.getAllAvailableJskadKeyboards());
        } catch (Exception e) {
            throw new ThdlLazyException(e);
        }
    }

    // private JComboBox fontFamilies, fontSizes;
    private JFileChooser fileChooser;
    private javax.swing.filechooser.FileFilter rtfFilter;
    private javax.swing.filechooser.FileFilter txtFilter;
    // private int fontSize = 0;
    private Object parentObject = null;
    private static int numberOfTibsRTFOpen = 0;
    private static int x_size;
    private static int y_size;

/**
* The text editing window which this Jskad object embeds.
*/
    public DuffPane dp;
/**
* Has the document been saved since it was last changed?
*/
    public boolean hasChanged = false;

/**
* The filename, if any, associated with this instance of Jskad.
*/
    public String fileName = null;
    
    private PreferenceWindow prefWindow;

    /** the status bar for this frame */
    private StatusBar statusBar;

    /** Do not use this JPanel constructor. */
    private Jskad() { super(); }
    
    /** Do not use this JPanel constructor. */
    private Jskad(boolean isDB) { super(isDB); }
    
    /** Do not use this JPanel constructor. */
    private Jskad(LayoutManager lm) { super(lm); }
    
    /** Do not use this JPanel constructor. */
    private Jskad(LayoutManager lm, boolean isDB) { super(lm, isDB); }
    
    /** Invokes to window to customize fonts. */
    private void setPreferences()
    {
        if (prefWindow == null) prefWindow = new PreferenceWindow(this, dp);
        prefWindow.show();
    }    

    /** Saves user preferences to disk if possible. */
    private void savePreferencesAction() {
        if (!clearedPrefs) {
            try {
                RecentlyOpenedFilesDatabase.storeRecentlyOpenedFilePreferences();

                if (!ThdlOptions.saveUserPreferences()) {
                    JOptionPane.showMessageDialog(Jskad.this,
                                                  "You previously cleared preferences,\nso you cannot now save them.",
                                                  "Cannot Save User Preferences",
                                                  JOptionPane.PLAIN_MESSAGE);
                }
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(Jskad.this,
                                              "Could not save to your preferences file!",
                                              "Error Saving Preferences",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static boolean clearedPrefs = false;
    /** Clears user preferences by deleting the preferences file on
        disk.  Prompts the user to quit and reopen Jskad. */
    private void clearPreferencesAction() {
        clearedPrefs = true;
        try {
            ThdlOptions.clearUserPreferences();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(Jskad.this,
                                          "Could not delete your preferences file!",
                                          "Error Clearing Preferences",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (JOptionPane.YES_OPTION
            == JOptionPane.showConfirmDialog(Jskad.this,
                                             "You must exit and restart before default preferences\nwill take effect.\n\nExit now?",
                                             "Clearing Preferences",
                                             JOptionPane.YES_NO_OPTION)) {
            exitAction();
        }
    }

    /** the File menu */
    private JMenu fileMenu = null;

    /** Updates state information now that we know that fileChosen is
        the most recently opened file. */
    private static void noteMostRecentlyOpenedFile(File fileChosen) {
        RecentlyOpenedFilesDatabase.setMostRecentlyOpenedFile(fileChosen);

        int i, sz = jskads.size();
        for (i = 0; i < sz; i++) {
            ((Jskad)jskads.elementAt(i)).updateRecentlyOpenedFilesMenuItems();
        }
    }

    private int numItemsOnFileMenuBeforeRecentlyOpened = 0;
    private void updateRecentlyOpenedFilesMenuItems() {
        int menu_loc = numItemsOnFileMenuBeforeRecentlyOpened;
        while (fileMenu.getItemCount() > menu_loc + 2)
            fileMenu.remove(menu_loc + 1);
        int N = RecentlyOpenedFilesDatabase.getNumberOfFilesToShow();
        // Avoid adding duplicate entries:
        boolean addedSeparator = false;
        for (int i = 0; i < N; i++) {
            final File recentlyOpenedFile
                = RecentlyOpenedFilesDatabase.getNthRecentlyOpenedFile(N-i-1);
            if (null != recentlyOpenedFile) {
                if (!addedSeparator) {
                    fileMenu.insertSeparator(menu_loc);
                    addedSeparator = true;
                }
                JMenuItem item = new JMenuItem((N-i) + " "
                                               + RecentlyOpenedFilesDatabase.getLabel(recentlyOpenedFile));
                item.addActionListener(new ThdlActionListener() {
                        public void theRealActionPerformed(ActionEvent e) {
                            openFile(recentlyOpenedFile);
                        }
                    });
                fileMenu.add(item, menu_loc + 1);
            }
        }
    }

/**
* @param parent the object that embeds this instance of Jskad.
* Supported objects include JFrames and JApplets. If the parent
* is a JApplet then the File menu is omitted from the menu bar.
*/
    public Jskad(final Object parent) {
        super();

        if (ThdlOptions.getBooleanOption("thdl.Jskad.disable.status.bar")) {
            statusBar = null;
        } else {
            statusBar
                = new StatusBar(ThdlOptions.getStringOption("thdl.Jskad.initial.status.message",
                                                            "Welcome to Jskad!"));
        }
        parentObject = parent;
        numberOfTibsRTFOpen++;
        JMenuBar menuBar = new JMenuBar();

        if (parentObject instanceof JFrame || parentObject instanceof JInternalFrame) {
            String whereToStart
                = ThdlOptions.getStringOption("thdl.Jskad.working.directory",
                                              null);
            try {
                fileChooser
                    = new JFileChooser((whereToStart == null)
                                       ? null
                                       : (whereToStart.equals("")
                                          ? null
                                          : whereToStart));
            } catch (NullPointerException e) {
                // This weirdness happens to me when I edit .java
                // files in the midst of an 'ant clean jskad-run':
                System.err.println("Jskad was not cleanly compiled; please rebuild!");
                System.err.println("Messy details:");
                System.err.println("");
                e.printStackTrace(System.err);
                System.exit(1);
            }
            rtfFilter = new RTFFilter();
            txtFilter = new TXTFilter();
            fileChooser.addChoosableFileFilter(rtfFilter);

            fileMenu = new JMenu("File");
            fileMenu.setMnemonic ( 'F' ) ;
            numItemsOnFileMenuBeforeRecentlyOpened = 0;


            JMenuItem newItem = new JMenuItem("New...");
            newItem.setMnemonic ( 'N' ) ;
            newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 
                  java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); 
            newItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    newFile();
                }
            });
            ++numItemsOnFileMenuBeforeRecentlyOpened;
            fileMenu.add(newItem);

            JMenuItem openItem = new JMenuItem("Open...");
            openItem.setMnemonic ( 'o' ) ;
            openItem.setAccelerator(KeyStroke.getKeyStroke (KeyEvent.VK_O, java.awt.Toolkit
                        .getDefaultToolkit().getMenuShortcutKeyMask())); 
            openItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    openFile();
                }
            });
            ++numItemsOnFileMenuBeforeRecentlyOpened;
            fileMenu.add(openItem);

            if (parentObject instanceof JFrame) {
                JMenuItem closeItem = new JMenuItem("Close");    
                closeItem.setMnemonic ( 'c' ) ;
                closeItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        if (numberOfTibsRTFOpen == 1) {
                            JOptionPane.showMessageDialog(Jskad.this,
                                                          "You cannot close the last Jskad window.\nUse File/Exit if you intend to exit.",
                                                          "Cannot close last Jskad window",
                                                          JOptionPane.ERROR_MESSAGE);                     
                        } else {
                            if (!hasChanged || hasChanged && checkSave(JOptionPane.YES_NO_CANCEL_OPTION)) {
                                Jskad.this.realCloseAction(true);
                            }
                        }
                    }
                });
                ++numItemsOnFileMenuBeforeRecentlyOpened;
                fileMenu.add(closeItem);
            }
            JMenuItem saveItem = new JMenuItem("Save");
            saveItem.setMnemonic ( 's' ) ;
            saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); //Ctrl-s
            saveItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    if (fileName == null)
                        saveAsFile("legacy rtf");
                    else
                        saveFile();
                }

            });
            ++numItemsOnFileMenuBeforeRecentlyOpened;
            fileMenu.addSeparator();
            ++numItemsOnFileMenuBeforeRecentlyOpened;
            fileMenu.add(saveItem);

            JMenuItem saveAsItem = new JMenuItem("Save as...");
            saveAsItem.setMnemonic ( 'a' ) ;
            saveAsItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    saveAsFile("legacy rtf");
                }
            });
            ++numItemsOnFileMenuBeforeRecentlyOpened;
            fileMenu.add(saveAsItem);

            JMenuItem saveAsUnicodeUtf8Item = new JMenuItem("Save as Unicode UTF-8 text...");
            saveAsUnicodeUtf8Item.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    saveAsFile("unicode utf8 text");
                }
            });
            ++numItemsOnFileMenuBeforeRecentlyOpened;
            fileMenu.add(saveAsUnicodeUtf8Item);

            JMenuItem saveAsUnicodeRtfItem = new JMenuItem("Save as Unicode RTF...");
            saveAsUnicodeRtfItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    saveAsFile("unicode rtf");
                }
            });
            ++numItemsOnFileMenuBeforeRecentlyOpened;
            fileMenu.add(saveAsUnicodeRtfItem);

            if (parentObject instanceof JFrame) {
                JMenuItem exitItem = new JMenuItem("Exit");    
                exitItem.setMnemonic ( 'x' ) ;
                exitItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        exitAction();
                    }
                });
                fileMenu.addSeparator();
                fileMenu.add(exitItem);
            }

            updateRecentlyOpenedFilesMenuItems();

            menuBar.add(fileMenu);
        }

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic ( 'E' ) ;

        if (parentObject instanceof JFrame || parentObject instanceof JInternalFrame) {
            JMenuItem cutItem = new JMenuItem("Cut");
            cutItem.setMnemonic ( 'u' ) ;
            cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                  java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            cutItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    cutSelection();                    
                }
            });
            editMenu.add(cutItem);

            JMenuItem copyItem = new JMenuItem("Copy");
            copyItem.setMnemonic ( 'C' ) ;
            copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                  java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            copyItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    copySelection();
                }
            });
            editMenu.add(copyItem);

            JMenuItem copyAsUnicodeItem = new JMenuItem("Copy as Unicode");
            copyAsUnicodeItem.setMnemonic ( 'i' ) ;
            copyAsUnicodeItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    copyAsUnicodeSelection();
                }
            });
            editMenu.add(copyAsUnicodeItem);

            JMenuItem pasteItem = new JMenuItem("Paste");
            pasteItem.setMnemonic ( 'P' ) ;
            pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                  java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            pasteItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    pasteSelection();
                }
            });
            editMenu.add(pasteItem);
            editMenu.addSeparator();

            JMenuItem selectallItem = new JMenuItem("Select All");
            selectallItem.setMnemonic ( 'A' ) ;
            selectallItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                  java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            selectallItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    dp.setSelectionStart(0);
                    dp.setSelectionEnd(dp.getDocument().getLength());
                }
            });
            editMenu.add(selectallItem);
        }

        {
            JMenuItem preferencesItem = new JMenuItem("Preferences");
            preferencesItem.setMnemonic ( 'P' ) ;
            preferencesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                  java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            preferencesItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        setPreferences();
                    }
                });
            editMenu.addSeparator();
            editMenu.add(preferencesItem);
        }

        {
            JMenuItem preferencesItem = new JMenuItem("Save preferences to " + ThdlOptions.getUserPreferencesPath());
            preferencesItem.setMnemonic ( 'S' ) ;
            preferencesItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        savePreferencesAction();
                    }
                });
            editMenu.add(preferencesItem);
        }

        {
            JMenuItem preferencesItem = new JMenuItem("Clear preferences");
            preferencesItem.setMnemonic ( 'C' ) ;
            preferencesItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        clearPreferencesAction();
                    }
                });
            editMenu.addSeparator();
            editMenu.add(preferencesItem);
        }

        menuBar.add(editMenu);

        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic ( 'T' ) ;

        JMenu convertSelectionMenu = new JMenu("Convert Selection");
        convertSelectionMenu.setMnemonic ( 'o' ) ;
        JMenuItem TMWWylieItem = new JMenuItem("Convert Tibetan Machine Web (non-Unicode) to Wylie");
        TMWWylieItem.setMnemonic ( 'T' ) ;
        TMWWylieItem.addActionListener(new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                toTranslit(true);
            }
        });
        convertSelectionMenu.add(TMWWylieItem);
        toolsMenu.add(convertSelectionMenu);

        JMenuItem TMWACIPItem = new JMenuItem("Convert Tibetan Machine Web (non-Unicode) to ACIP");
        TMWACIPItem.setMnemonic ( 'W' ) ; 
        TMWACIPItem.addActionListener(new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                toTranslit(false);
            }
        });
        convertSelectionMenu.add(TMWACIPItem);

        JMenuItem wylieTMWItem = new JMenuItem("Convert Wylie to Tibetan Machine Web (non-Unicode) (no warnings)");
        wylieTMWItem.setMnemonic ( 'M' ) ;
        wylieTMWItem.addActionListener(new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                toTibetan(false, false);
            }
        });
        convertSelectionMenu.add(wylieTMWItem);

        JMenuItem wylieTMWWarningsItem = new JMenuItem("Convert Wylie to Tibetan Machine Web (non-Unicode) (pedantic warnings)");
        wylieTMWWarningsItem.addActionListener(new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                toTibetan(false, true);
            }
        });
        convertSelectionMenu.add(wylieTMWWarningsItem);

        JMenuItem ACIPTMWItem = new JMenuItem("Convert ACIP to Tibetan Machine Web (non-Unicode) (no warnings)");
        ACIPTMWItem.addActionListener(new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                toTibetan(true, false);
            }
        });
        convertSelectionMenu.add(ACIPTMWItem);

        JMenuItem ACIPTMWWarnItem = new JMenuItem("Convert ACIP to Tibetan Machine Web (non-Unicode) (pedantic warnings)");
        ACIPTMWWarnItem.addActionListener(new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                toTibetan(true, true);
            }
        });
        convertSelectionMenu.add(ACIPTMWWarnItem);

        JMenu convertAllMenu = new JMenu("Convert All");

        JMenuItem toTMItem = new JMenuItem("Convert Tibetan Machine Web (non-Unicode) to TM"); // DLC FIXME: do it just in the selection?
        toTMItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    StringBuffer errors = new StringBuffer();
                    long numAttemptedReplacements[] = new long[] { 0 };
                    boolean errorReturn
                        = ((TibetanDocument)dp.getDocument()).convertToTM(0, -1, errors,
                                                                          numAttemptedReplacements); // entire document
                    if (errorReturn) {
                        JOptionPane.showMessageDialog(Jskad.this,
                                                      "At least one error occurred while converting Tibetan Machine Web\nto Tibetan Machine.  Your document is mostly converted,\nexcept for the following glyphs, which you should replace manually\nbefore retrying:\n"
                                                      + errors.toString(),
                                                      "TMW to TM Errors",
                                                      JOptionPane.PLAIN_MESSAGE);
                    } else {
                        if (numAttemptedReplacements[0] > 0) {
                            JOptionPane.showMessageDialog(Jskad.this,
                                                          "Converting Tibetan Machine Web to Tibetan Machine met with perfect success.",
                                                          "Success",
                                                          JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(Jskad.this,
                                                          "No Tibetan Machine Web was found, so nothing was converted.",
                                                          "Nothing to do",
                                                          JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        JMenuItem toTMWItem = new JMenuItem("Convert TM to Tibetan Machine Web (non-Unicode)"); // DLC FIXME: do it just in the selection?
        toTMWItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    StringBuffer errors = new StringBuffer();
                    long numAttemptedReplacements[] = new long[] { 0 };
                    boolean errorReturn
                        = ((TibetanDocument)dp.getDocument()).convertToTMW(0, -1, errors,
                                                                           numAttemptedReplacements); // entire document
                    if (errorReturn) {
                        JOptionPane.showMessageDialog(Jskad.this,
                                                      "At least one error occurred while converting Tibetan Machine\nto Tibetan Machine Web.  Your document is mostly converted,\nexcept for the following glyphs, which you should replace manually\nbefore retrying:\n"
                                                      + errors.toString(),
                                                      "TM to TMW Errors", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        if (numAttemptedReplacements[0] > 0) {
                            JOptionPane.showMessageDialog(Jskad.this,
                                                          "Converting Tibetan Machine to Tibetan Machine Web met with perfect success.",
                                                          "Success",
                                                          JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(Jskad.this,
                                                          "No Tibetan Machine was found, so nothing was converted.",
                                                          "Nothing to do",
                                                          JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        JMenuItem toUnicodeItem = new JMenuItem("Convert Tibetan Machine Web (non-Unicode) to Unicode"); // DLC FIXME: do it just in the selection?
        toUnicodeItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    StringBuffer errors = new StringBuffer();
                    long numAttemptedReplacements[] = new long[] { 0 };
                    boolean errorReturn
                        = ((TibetanDocument)dp.getDocument()).convertToUnicode(0, -1, errors,
                                                                               ThdlOptions.getStringOption("thdl.tmw.to.unicode.font").intern(),
                                                                               numAttemptedReplacements); // entire document
                    if (errorReturn) {
                        JOptionPane.showMessageDialog(Jskad.this,
                                                      "At least one error occurred while converting Tibetan Machine Web\nto Unicode.  Your document is mostly converted,\nexcept for the following glyphs, which you should replace manually\nbefore retrying:\n"
                                                      + errors.toString(),
                                                      "TMW to Unicode Errors", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        if (numAttemptedReplacements[0] > 0) {
                            JOptionPane.showMessageDialog(Jskad.this,
                                                          "Converting Tibetan Machine Web to Unicode met with perfect success.",
                                                          "Success",
                                                          JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(Jskad.this,
                                                          "No Tibetan Machine Web was found, so nothing was converted.",
                                                          "Nothing to do",
                                                          JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        convertAllMenu.add(toTMItem);
        convertAllMenu.add(toTMWItem);
        convertAllMenu.add(toUnicodeItem);
        toolsMenu.add(convertAllMenu);

        if (parentObject instanceof JFrame || parentObject instanceof JInternalFrame) {
            JMenuItem converterItem = new JMenuItem("Launch Converter...");
            converterItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        // ignore the return value:
                        ConverterGUI.realMain(new String[] { },
                                              System.out,
                                              ((parentObject instanceof Frame)
                                               ? (Frame)parentObject
                                               : null));
                    }
                });
            toolsMenu.add(converterItem);

            JMenuItem openWithItem = new JMenuItem("Open With External Viewer...");
            openWithItem.addActionListener(new ThdlActionListener() {
                public void theRealActionPerformed(ActionEvent e) {
                    openWithExternalViewer();
                }
            });
            toolsMenu.addSeparator();
            toolsMenu.add(openWithItem);

        }


        if (ThdlOptions.getBooleanOption("thdl.add.developer.options.to.menu")) {
            toolsMenu.addSeparator();
            JMenuItem DevelItem = new JMenuItem("Toggle read-only");
            DevelItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        dp.setEditable(!dp.isEditable());
                    }
                });
            toolsMenu.add(DevelItem);
        }

// I used this when I validated the TM<->TMW mappings; I made
// dp.romanAttributeSet public to do so:
//
//            if (ThdlOptions.getBooleanOption("thdl.add.developer.options.to.menu")) {
//                toolsMenu.addSeparator();
//                JMenuItem DevelItem = new JMenuItem("Insert All TM Glyphs in Doc");
//                DevelItem.addActionListener(new ThdlActionListener() {
//                        public void theRealActionPerformed(ActionEvent e) {
//                            ((TibetanDocument)dp.getDocument()).insertAllTMGlyphs2();
//                        }
//                    });
//                toolsMenu.add(DevelItem);
//            }

        if (ThdlOptions.getBooleanOption("thdl.add.developer.options.to.menu")) {
            toolsMenu.addSeparator();
            JMenuItem DevelItem = new JMenuItem("Debug dump to standard output");
            DevelItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        StringBuffer buf = new StringBuffer();
                        ((TibetanDocument)dp.getDocument()).getTextRepresentation(0, -1, buf);
                        System.out.println("The text representation of the document, for debugging, is this:\n" + buf);
                    }
                });
            toolsMenu.add(DevelItem);
        }

        if (ThdlOptions.getBooleanOption("thdl.add.developer.options.to.menu")) {
            toolsMenu.addSeparator();
            JMenuItem DevelItem = new JMenuItem("Check for non-TMW characters"); // FIXME: do it just in the selection?
            DevelItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        ((TibetanDocument)dp.getDocument()).findSomeNonTMWCharacters(0, -1); // entire document.
                    }
                });
            toolsMenu.add(DevelItem);
        }

        menuBar.add(toolsMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic ( 'H' ) ;

        {
            JMenuItem helpItem = new JMenuItem("Help...");
            helpItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        CalHTMLPane helpPane = new CalHTMLPane();
                        try {
                            URL helpDocumentURL
                                = Jskad.class.getResource("/org/thdl/tib/input/jskad_doc.html");

                            helpPane.showHTMLDocument(helpDocumentURL);
                        } catch (Exception ex) {
                            ex.printStackTrace(System.err);
                            throw new ThdlLazyException(ex);
                        }
                        new SimpleFrame("Help for Jskad", helpPane);
                    }
                });
            helpMenu.add(helpItem);
        }

        {
            JMenuItem helpItem = new JMenuItem("Jskad on the Web...");
            helpItem.setMnemonic ( 'J' ) ;
            helpItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        CalHTMLPane onlineHelpPane = new CalHTMLPane();
                        try {
                            onlineHelpPane.showHTMLDocument(new URL("http://orion.lib.virginia.edu/thdl/tools/jskad.html"));
                        } catch (Exception ex) {
                            ex.printStackTrace(System.err);
                            throw new ThdlLazyException(ex);
                        }
                        new SimpleFrame("Jskad on the Web", onlineHelpPane);
                    }
                });
            helpMenu.add(helpItem);
            helpMenu.addSeparator();
        }

        for (int i = 0; i < keybdMgr.size(); i++) {
            final JskadKeyboard kbd = keybdMgr.elementAt(i);
            if (kbd.hasQuickRefFile()) {
                JMenuItem keybdItem = new JMenuItem(kbd.getIdentifyingString() + "...");
                keybdItem.addActionListener(new ThdlActionListener() {
                        public void theRealActionPerformed(ActionEvent e) {
                            new SimpleFrame(kbd.getIdentifyingString(),
                                            kbd.getQuickRefPane());
                            /* DLC FIXME -- pressing the "Extended
                               Wylie" menu item (for example) twice
                               causes the first pane to become dead.
                               We should check to see if the first
                               pane exists and raise it rather than
                               creating a second pane. */
                        }
                    });
                helpMenu.add(keybdItem);
            }
        }

        helpMenu.addSeparator();

        {
            JMenuItem aboutItem = new JMenuItem("About...");
            aboutItem.setMnemonic ( 'A' ) ;
            aboutItem.addActionListener(new ThdlActionListener() {
                    public void theRealActionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(Jskad.this,
                                                      "Copyright 2001-2003 Tibetan and Himalayan Digital Library\n\n"+
                                                      "Jskad is protected by the THDL Open Community License.\n\n"+ /* FIXME UPDATE THE YEAR REGULARLY */
            
                                                      "For more information, or to download the source code\n"+
                                                      "for Jskad, visit our web site:\n"+
                                                      "     http://thdl.org/\n" +
                                                      "\n" +
                                                      "Portions copyright Andrew Moulden.  Thanks to him for\n" + // CalHTMLPane's license requires this.
                                                      "allowing us to display Help documents.\n" +
                                                      "\n" +
                                                      "When submitting bug reports, please indicate that the\n" +
                                                      "time of compilation is "
                                                      + ThdlVersion.getTimeOfCompilation() + "\n",
                                                      "About Jskad", JOptionPane.PLAIN_MESSAGE);
                    }
                });
            helpMenu.add(aboutItem);
        }

        menuBar.add(helpMenu);

        /* Initialize dp before calling
           JskadKeyboard.activate(DuffPane) or dp.toggleLanguage(). */
        if (ThdlOptions.getBooleanOption(Jskad.enableKeypressStatusProp)) {
            dp = new DuffPane(parentObject, statusBar);
        } else {
            dp = new DuffPane(parentObject);
        }


        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(null);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Input mode:"));
        toolBar.addSeparator();

        String[] input_modes = {"Tibetan","Roman"};
        final JComboBox inputmethods = new JComboBox(input_modes);

        int initialInputMethod
            = ThdlOptions.getIntegerOption("thdl.Jskad.input.method", 0);
        if (!dp.isRomanEnabled() && 1 == initialInputMethod) {
            initialInputMethod = 0;
            System.out.println("Hey yo!  Roman input mode is not enabled, but your preference is for Roman mode at startup.  Sorry!");
            ThdlDebug.noteIffyCode();
        }
        try {
            inputmethods.setSelectedIndex(initialInputMethod);
        } catch (IllegalArgumentException e) {
            initialInputMethod = 0; // Tibetan is the default.
            inputmethods.setSelectedIndex(initialInputMethod);
        }
        // Because we start in Tibetan mode, we must toggle initially
        // if the user wants it that way:
        if (1 == initialInputMethod && dp.isRomanEnabled())
            dp.toggleLanguage();

        inputmethods.addActionListener(new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                int si = inputmethods.getSelectedIndex();
                ThdlOptions.setUserPreference("thdl.Jskad.input.method", si);
                switch (si) {
                    case 0: //Tibetan
                        if (dp.isRomanMode())
                            dp.toggleLanguage();
                        statusBar.replaceStatus("Now inputting Tibetan script");
                        break;

                    case 1: //Roman
                        if (!dp.isRomanMode() && dp.isRomanEnabled())
                            dp.toggleLanguage();
                        statusBar.replaceStatus("Now inputting Roman script");
                        break;
                }
            }
        });


        toolBar.add(inputmethods);
        toolBar.add(Box.createHorizontalGlue());

        toolBar.add(new JLabel("Keyboard:"));
        toolBar.addSeparator();

        final JComboBox keyboards
            = new JComboBox(keybdMgr.getIdentifyingStrings());
        int initialKeyboard
            = ThdlOptions.getIntegerOption("thdl.default.tibetan.keyboard", 0);
        try {
            keyboards.setSelectedIndex(initialKeyboard);
        } catch (IllegalArgumentException e) {
            initialKeyboard = 0; // good ol' Wylie
            keyboards.setSelectedIndex(initialKeyboard);
        }
        keybdMgr.elementAt(initialKeyboard).activate(dp);
        keyboards.addActionListener(new ThdlActionListener() {
            public void theRealActionPerformed(ActionEvent e) {
                int ki = keyboards.getSelectedIndex();
                keybdMgr.elementAt(ki).activate(dp);
                ThdlOptions.setUserPreference("thdl.default.tibetan.keyboard", ki);
            }
        });
        toolBar.add(keyboards);
        toolBar.add(Box.createHorizontalGlue());

        JScrollPane scrollingDuffPane
            = new JScrollPane(dp,
                              JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                              JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dp.getDocument().addDocumentListener(this);

        if (parentObject instanceof JFrame) {
            final JFrame parentFrame = (JFrame)parentObject;
            parentFrame.setJMenuBar(menuBar);
            parentFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            parentFrame.addWindowListener(new WindowAdapter () {
                    // We want the focus to be, at the start of the
                    // program and whenever Jskad gets the focus, on
                    // the DuffPane.
                    public void windowActivated (WindowEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    focusToDuffPane();
                                }
                            });
                    }
                    public void windowClosing (WindowEvent e) {
                        if (!hasChanged || hasChanged && checkSave(JOptionPane.YES_NO_CANCEL_OPTION)) {
                            numberOfTibsRTFOpen--;
                            if (numberOfTibsRTFOpen == 0)
                                System.exit(0); // calls our shutdown hook
                            else
                                parentFrame.dispose();
                        }
                    }
                });
        } else if (parentObject instanceof JInternalFrame) {
            final JInternalFrame parentFrame = (JInternalFrame)parentObject;
            parentFrame.setJMenuBar(menuBar);
        } else if (parentObject instanceof JApplet) {
            JApplet parentApplet = (JApplet)parentObject;
            parentApplet.setJMenuBar(menuBar);
            // TODO(dchandler): disable cut and paste in the applet
            // (we once did this...)
        }

        setLayout(new BorderLayout());
        add("North", toolBar);
        add("Center", scrollingDuffPane);
        if (statusBar != null)
            add("South", statusBar);

	dp.postInitialize ( this ) ;
    }

    private void newFile() {
        if (dp.getDocument().getLength()>0 && parentObject instanceof JFrame) {
            JFrame parentFrame = (JFrame)parentObject;
            JFrame newFrame = new JFrame("Jskad");
            Point point = parentFrame.getLocationOnScreen();
            newFrame.setSize(parentFrame.getSize().width, parentFrame.getSize().height);
            newFrame.setLocation(point.x+50, point.y+50);
            Jskad jskad = new Jskad(newFrame);
            jskads.add(jskad);
            newFrame.getContentPane().add(jskad);
            newFrame.setVisible(true);
        }
        else {
            if (parentObject instanceof JFrame) {
                JFrame parentFrame = (JFrame)parentObject;
                parentFrame.setTitle("Jskad");
            }
            else if (parentObject instanceof JInternalFrame) {
                JInternalFrame parentFrame = (JInternalFrame)parentObject;
                parentFrame.setTitle("Jskad");
            }
            dp.newDocument();
            dp.getDocument().addDocumentListener(Jskad.this);
            hasChanged = false;
        }
    }

    private void openFile() {
        String whereToStart
            = ThdlOptions.getStringOption("thdl.Jskad.working.directory",
                                          null);
        fileChooser
            = new JFileChooser((whereToStart == null)
                               ? null
                               : (whereToStart.equals("")
                                  ? null
                                  : whereToStart));
        fileChooser.addChoosableFileFilter(rtfFilter);

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        openFile(fileChooser.getSelectedFile());
    }

    /** Opens fileChosen for viewing and modifies the recently opened
        files list. */
    private void openFile(File fileChosen) {
        final String f_name = fileChosen.getAbsolutePath();

        try {
            if (dp.getDocument().getLength()>0 && parentObject instanceof JFrame) {
                JFrame parentFrame = (JFrame)parentObject;
                InputStream in = new FileInputStream(fileChosen);
                if (!ThdlOptions.getBooleanOption("thdl.do.not.fix.rtf.hex.escapes"))
                    in = new RTFFixerInputStream(in);
                ThdlOptions.setUserPreference("thdl.Jskad.working.directory",
                                              fileChosen.getParentFile().getAbsolutePath());
                JFrame newFrame = new JFrame(f_name);
                Point point = parentFrame.getLocationOnScreen();
                newFrame.setSize(x_size, y_size);
                newFrame.setLocation(point.x+50, point.y+50);
                Jskad newRTF = new Jskad(newFrame);
                jskads.add(newRTF);
                newFrame.getContentPane().add(newRTF);
                boolean error = false;
                try {
                    newRTF.dp.rtfEd.read(in, newRTF.dp.getDocument(), 0);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(newFrame,
                                                  TibetanConverter.rtfErrorMessage);
                    error = true;
                }
                in.close();
                if (error) {
                    newFrame.dispose();
                    numberOfTibsRTFOpen--;
                } else {
                    noteMostRecentlyOpenedFile(fileChosen);
                    if (!ThdlOptions.getBooleanOption("thdl.Jskad.do.not.fix.curly.braces.in.rtf")) {
                        ((TibetanDocument)newRTF.dp.getDocument()).replaceTahomaCurlyBracesAndBackslashes(0, -1);
                    }
                    newRTF.dp.getDocument().addDocumentListener(newRTF);
                    newFrame.setTitle("Jskad: " + f_name);
                    newRTF.fileName = new String(f_name);
                    newRTF.hasChanged = false;
                    newRTF.dp.getCaret().setDot(0);
                    newFrame.setVisible(true);
                }
            } else {
                InputStream in = new FileInputStream(fileChosen);
                if (!ThdlOptions.getBooleanOption("thdl.do.not.fix.rtf.hex.escapes"))
                    in = new RTFFixerInputStream(in);
                ThdlOptions.setUserPreference("thdl.Jskad.working.directory",
                                              fileChosen.getParentFile().getAbsolutePath());
                dp.newDocument();
                boolean error = false;
                try {
                    dp.rtfEd.read(in, dp.getDocument(), 0);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                                                  TibetanConverter.rtfErrorMessage);
                    error = true;
                }

                in.close();
                if (!error) {
                    noteMostRecentlyOpenedFile(fileChosen);
                    if (!ThdlOptions.getBooleanOption("thdl.Jskad.do.not.fix.curly.braces.in.rtf")) {
                        ((TibetanDocument)dp.getDocument()).replaceTahomaCurlyBracesAndBackslashes(0, -1);
                    }
                    dp.getCaret().setDot(0);
                    dp.getDocument().addDocumentListener(Jskad.this);
                    hasChanged = false;
                    fileName = new String(f_name);
                    if (parentObject instanceof JFrame) {
                        JFrame parentFrame = (JFrame)parentObject;
                        parentFrame.setTitle("Jskad: " + fileName);
                    }
                    else if (parentObject instanceof JInternalFrame) {
                        JInternalFrame parentFrame = (JInternalFrame)parentObject;
                        parentFrame.setTitle("Jskad: " + fileName);
                    }
                }
            }
        }
        catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(Jskad.this,
                                          "No such file exists.",
                                          "Open File Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ioe) {
            JOptionPane.showMessageDialog(Jskad.this,
                                          "Error reading file.",
                                          "Open File Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /** Returns true iff the save was successful. */
    private boolean saveFile() {
        String s = getSave("legacy rtf", fileName);
        if (null != s) {
            if (parentObject instanceof JFrame) {
                JFrame parentFrame = (JFrame)parentObject;
                parentFrame.setTitle("Jskad: " + s);
                fileName = new String(s);
            }
            else if (parentObject instanceof JInternalFrame) {
                JInternalFrame parentFrame = (JInternalFrame)parentObject;
                parentFrame.setTitle("Jskad: " + s);
                fileName = new String(s);
            }
            return true;
        } else {
            return false;
        }
    }

    /** Returns true iff the save was successful.
     *  @param fileType either "legacy rtf", "unicode utf8 text", or
     *  "unicode rtf" */
    private boolean saveAsFile(String fileType) {
        String s = getSaveAs(fileType);
        if (null != s) {
            if (parentObject instanceof JFrame) {
                JFrame parentFrame = (JFrame)parentObject;
                parentFrame.setTitle("Jskad: " + s);
                fileName = new String(s);
            }
            else if (parentObject instanceof JInternalFrame) {
                JInternalFrame parentFrame = (JInternalFrame)parentObject;
                parentFrame.setTitle("Jskad: " + s);
                fileName = new String(s);
            }
            return true;
        }
        return false;
    }

    /** Returns true iff the user says (possibly after a successful
        save) that it is OK to destroy this Jskad widget.
        @param dialogType either JOptionPane.YES_NO_CANCEL_OPTION or
        JOptionPane.YES_NO_OPTION, depending on whether canceling is
        different than saying no.
    */
    private boolean checkSave(int dialogType) {
        if (ThdlOptions.getBooleanOption("thdl.Jskad.do.not.confirm.quit")) {
            return true;
        }

        int saveFirst
            = JOptionPane.showConfirmDialog(this,
                                            "Do you want to save your changes?",
                                            "Please select",
                                            dialogType);

        switch (saveFirst) {
            case JOptionPane.NO_OPTION: //don't save but do continue
                return true;

            case JOptionPane.YES_OPTION: //save and continue
                if (fileName == null)
                    return saveAsFile("legacy rtf");
                else
                    return saveFile();

            default:
                return false;
        }
    }

    private String getSave(String fileType, String f_name) {
        File fileChosen = new File(f_name);

        try {
            if (fileType == "legacy rtf") {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileChosen));
                dp.rtfEd.write(out, dp.getDocument(), 0, dp.getDocument().getLength());
                out.flush();
                out.close();
            } else if (fileType == "unicode utf8 text") {
                if (!dp.saveAsUnicode(true,
                                      new BufferedOutputStream(new FileOutputStream(fileChosen)))) {
                    JOptionPane.showMessageDialog(Jskad.this,
                                                  "Saving as UTF-8 Unicode text did not go perfectly.  Try using the standalone converter if you want a perfect document.",
                                                  "Save As UTF-8 Warning",
                                                  JOptionPane.WARNING_MESSAGE);
                }
            } else if (fileType == "unicode rtf") {
                if (!dp.saveAsUnicode(false,
                                      new BufferedOutputStream(new FileOutputStream(fileChosen)))) {
                    JOptionPane.showMessageDialog(Jskad.this,
                                                  "Saving as Unicode RTF did not go perfectly.  Try using the standalone converter if you want a perfect document.",
                                                  "Save As Unicode RTF Warning",
                                                  JOptionPane.WARNING_MESSAGE);
                }
            } else {
                throw new IllegalArgumentException("fileType " + fileType
                                                   + " is not supported");
            }
            hasChanged = false;
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(Jskad.this,
                                          "Cannot save to that file.",
                                          "Save As Error",
                                          JOptionPane.ERROR_MESSAGE);

            return null;
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            ThdlDebug.noteIffyCode();
        }
        return f_name;
    }

    private String getSaveAs(String fileType) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        if (fileType == "unicode utf8 text") {
            fileChooser.removeChoosableFileFilter(rtfFilter);
            fileChooser.addChoosableFileFilter(txtFilter);
        }
        if (fileName == null)
            fileChooser.setSelectedFile(null);
        else
            fileChooser.setSelectedFile(new File(fileName));

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (fileType == "unicode utf8 text") {
                fileChooser.removeChoosableFileFilter(txtFilter);
                fileChooser.addChoosableFileFilter(rtfFilter);
            }
            return null;
        }

        File fileChosen = fileChooser.getSelectedFile();
        ThdlOptions.setUserPreference("thdl.Jskad.working.directory",
                                      fileChosen.getParentFile().getAbsolutePath());
        String fileName = fileChosen.getAbsolutePath();
        int i = fileName.lastIndexOf('.');

        String ext = ((fileType == "unicode utf8 text") ? ".txt" : ".rtf");

        if (i < 0)
            fileName += ext;
        else if (!fileName.regionMatches(true, i, ext, 0, ext.length())
                 || fileName.length() != i + ext.length()) {
            JOptionPane.showMessageDialog(Jskad.this,
                                          "Filename chosen does not have the extension '"
                                          + ext + "'",
                                          "Save As Error",
                                          JOptionPane.ERROR_MESSAGE);

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return null;
        }

        getSave(fileType, fileName);

        fileChooser.rescanCurrentDirectory();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return fileName;
    }

    private void cutSelection() {
        dp.cut();
    }

    private void copySelection() {
        dp.copy();
    }

    private void copyAsUnicodeSelection() {
        dp.copyAsUnicode();
    }

    private void pasteSelection() {
        dp.paste(dp.getCaret().getDot());
    }

    private void toTibetan(boolean fromACIP, boolean withWarnings) {
        Jskad.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        dp.toTibetanMachineWeb(fromACIP, withWarnings);
        Jskad.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void toTranslit(boolean toWylieNotACIP) {
        Jskad.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (!(toWylieNotACIP
              ? ((TibetanDocument)dp.getDocument()).toWylie(dp.getSelectionStart(),
                                                            dp.getSelectionEnd(),
                                                            new long[] { 0 })
              : ((TibetanDocument)dp.getDocument()).toACIP(dp.getSelectionStart(),
                                                           dp.getSelectionEnd(),
                                                           new long[] { 0 }))) {
            JOptionPane.showMessageDialog(Jskad.this,
                                          "Though some "
                                          + (toWylieNotACIP
                                             ? "Extended Wylie"
                                             : "ACIP")
                                          + " has been produced, it\ncontains ugly error messages"
                                          + (toWylieNotACIP
                                             ? " like\n\"<<[[JSKAD_TMW_TO_WYLIE_ERROR_NO_SUCH_WYLIE:\n    Cannot convert DuffCode...\"."
                                             : ".")
                                          + "\nPlease edit the output by hand to replace all such\ncreatures with the correct transliteration.",
                                          "Attention Required",
                                          JOptionPane.ERROR_MESSAGE);
        }
        Jskad.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void importWylie() {
        fileChooser.removeChoosableFileFilter(rtfFilter);
        fileChooser.addChoosableFileFilter(txtFilter);

        if (fileChooser.showDialog(Jskad.this, "Import Wylie") != JFileChooser.APPROVE_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            fileChooser.removeChoosableFileFilter(txtFilter);
            fileChooser.addChoosableFileFilter(rtfFilter);
            return;
        }

        File txt_fileChosen = fileChooser.getSelectedFile();
        fileChooser.rescanCurrentDirectory();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        fileChooser.removeChoosableFileFilter(txtFilter);
        fileChooser.addChoosableFileFilter(rtfFilter);

        if (fileChooser.showDialog(Jskad.this, "Save as Tibetan Machine Web (non-Unicode)") != JFileChooser.APPROVE_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        File rtf_fileChosen = fileChooser.getSelectedFile();
        fileChooser.rescanCurrentDirectory();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        String rtf_fileName = rtf_fileChosen.getAbsolutePath();
        int i = rtf_fileName.lastIndexOf('.');

        if (i < 0)
            rtf_fileName += ".rtf";
        else
            rtf_fileName = rtf_fileName.substring(0, i) + ".rtf";

        try {
            BufferedReader in = new BufferedReader(new FileReader(txt_fileChosen));
            // FIXME: why do we need a whole DuffPane to do this?
            DuffPane dp2 = new DuffPane( parentObject );

            try {
                String val = in.readLine();

                while (val != null) {
                    dp2.toTibetanMachineWeb(val + "\n", dp2.getCaret().getDot());
                    val = in.readLine();
                }

                TibetanDocument t_doc = (TibetanDocument)dp2.getDocument();
                t_doc.writeRTFOutputStream(new FileOutputStream(new File(rtf_fileName)));
            } catch (IOException ioe) {
                ThdlDebug.noteIffyCode();
                System.out.println("problem reading or writing file");
            }
        } catch (FileNotFoundException fnfe) {
            ThdlDebug.noteIffyCode();
            System.out.println("problem reading file");
        }
    }

/**
* Allows use of Jskad as dependent JFrame.
* Once you've called this method, users will
* be able to close Jskad without shutting
* down your superordinate application.
*/
    public void makeDependent() {
        numberOfTibsRTFOpen++;
    }

/**
* Fills the editing pane with content. If the
* editing pane already has content, this method does nothing.
*
* @param wylie the string of wylie you want to
* put in the editing pane
*/
    public void setContent(String wylie) {
        if (dp.getDocument().getLength() > 0)
            return;

        dp.newDocument();
        dp.toTibetanMachineWeb(wylie, 0);
    }
    
/**
* Enables typing of Roman (non-Tibetan) text along
* with Tibetan.
*/
    public void enableRoman() {
        dp.enableRoman();
    }

/**
* Disables typing of Roman (non-Tibetan) text.
*/
    public void disableRoman() {
        dp.disableRoman();
    }

/*
    private void showAttributes(int pos) {
        dp.skipUpdate = true;

        AttributeSet attr = dp.getDocument().getCharacterElement(pos).getAttributes();
        String name = StyleConstants.getFontFamily(attr);

        if (!fontName.equals(name)) {
            fontName = name;
            fontFamilies.setSelectedItem(name);
        }
        int size = StyleConstants.getFontSize(attr);
        if (fontSize != size) {
            fontSize = size;
            fontSizes.setSelectedItem(Integer.toString(fontSize));
        }

        dp.skipUpdate = false;
    }
*/

/**
* Required for implementations of DocumentListener.
* Does nothing.
*/
    public void changedUpdate(DocumentEvent de) {
    }

/**
* Required for implementations of DocumentListener.
* Informs the object that a change in the document
* has occurred.
*
* @param de a DocumentEvent
*/
    public void insertUpdate(DocumentEvent de) {
        hasChanged = true;
    }

/**
* Required for implementations of DocumentListener.
* Informs the object that a change in the document
* has occurred.
*
* @param de a DocumentEvent
*/
    public void removeUpdate(DocumentEvent de) {
        hasChanged = true;
    }

    private class RTFFilter extends javax.swing.filechooser.FileFilter {
        // Accept all directories and all RTF files.

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String fName = f.getName();
            int i = fName.lastIndexOf('.');

            if (i < 0)
                return false;

            else {
                String ext = fName.substring(i+1).toLowerCase();

                if (ext.equals("rtf"))
                    return true;
                else
                    return false;
            }
        }
    
        //the description of this filter
        public String getDescription() {
            return "Rich Text Format (.rtf)";
        }
    }

    private class TXTFilter extends javax.swing.filechooser.FileFilter {
        // Accept all directories and all TXT files.

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String fName = f.getName();
            int i = fName.lastIndexOf('.');

            if (i < 0)
                return false;

            else {
                String ext = fName.substring(i+1).toLowerCase();

                if (ext.equals("txt"))
                    return true;
                else
                    return false;
            }
        }
    
        //the description of this filter
        public String getDescription() {
            return "Text file (.txt)";
        }
    }

    /** Closes this Jskad session without saving.  If deleteFromList
        is true, the jskads vector is updated so that it no longer
        contains this instance.  If this is the last instance open,
        the program exits. */
    private void realCloseAction(boolean deleteFromList) {
        numberOfTibsRTFOpen--;

        // Delete this Jskad from jskads.
        if (deleteFromList) {
            int i, sz = jskads.size();
            for (i = 0; i < sz; i++) {
                if (jskads.elementAt(i) == this) {
                    jskads.removeElementAt(i);
                    break;
                }
            }
            if (i == sz)
                throw new Error("Couldn't find the Jskad session being closed in our list of open Jskad sessions! This is a bug.");
        }

        if (numberOfTibsRTFOpen == 0)
            System.exit(0);
        else {
            final JFrame parentFrame = (JFrame)parentObject;
            parentFrame.dispose();
        }
    }

    /** Stores all open Jskad sessions. */
    private static Vector jskads = new Vector();

    /** Closes all Jskad windows if user confirms.  After asking "Do
        you want to save?"  for the modified windows, this closes them
        all.  If you don't cancel for any unsaved session, then all
        sessions are closed.  If you do cancel for any one, even the
        last one, then all sessions remain open. */
    private static void exitAction() {
        int sz = jskads.size();
        for (int i = 0; i < sz; i++) {
            Jskad j = (Jskad)jskads.elementAt(i);
            if (j.hasChanged && !j.checkSave(JOptionPane.YES_NO_CANCEL_OPTION)) {
                return;
            }
        }
        for (int i = 0; i < sz; i++) {
            Jskad j = (Jskad)jskads.elementAt(i);
            j.realCloseAction(false);
        }
    }


    /** Checks if the current buffer has been modified or is not yet
        saved.  If so, the user is prompted to save.  After that, the
        current buffer is opened with a user-selected external viewer,
        which defaults to Microsoft Word if we can find it with our
        naive, "I bet it lives on everybody's machine where it lives
        on mine" mechanism. */
    private void openWithExternalViewer() {
        if ((hasChanged && !checkSave(JOptionPane.YES_NO_OPTION)) || null == fileName) {
            if (null == fileName) {
                JOptionPane.showMessageDialog(Jskad.this,
                                              "You must save the file before opening it with an external viewer.",
                                              "Cannot View Unsaved File",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                if (JOptionPane.YES_OPTION
                    != JOptionPane.showConfirmDialog(Jskad.this,
                                                     "Do you want to view the file as it exists on disk anyway?",
                                                     "Proceed Anyway?",
                                                     JOptionPane.YES_NO_OPTION))
                    return;
            }
        }
        ConvertDialog.openWithExternalViewer(this, fileName);
    }

/**
* Runs Jskad.  System output, including errors, is redirected to
* jskad.log in addition to appearing on the console as per usual.  If
* you discover a bug, please send us an email, making sure to include
* the jskad.log file as an attachment.  */
    public static void main(String[] args) {
        try {
            ThdlDebug.attemptToSetUpLogFile("jskad", ".log");

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e) {
                ThdlDebug.noteIffyCode();
            }

            JFrame f = new JFrame("Jskad");
            Dimension d = f.getToolkit().getScreenSize();
            x_size = d.width/4*3;
            y_size = d.height/4*3;
            f.setSize(x_size, y_size);
            f.setLocation(d.width/8, d.height/8);
            final Jskad jskad = new Jskad(f);
            jskads.add(jskad);
            f.getContentPane().add(jskad);
            f.setVisible(true);

            /* Make it so that any time the user exits Jskad by
             * (almost) any means, the user's preferences are saved if
             * the SecurityManager allows it and the path is
             * correct. */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        jskad.savePreferencesAction();
                    }
                }
                                                );

        } catch (ThdlLazyException e) {
            // FIXME: tell the users how to submit bug reports.
            System.err.println("Jskad has a BUG:");
            e.getRealException().printStackTrace(System.err);
            System.exit(1);
        }
    }
}
