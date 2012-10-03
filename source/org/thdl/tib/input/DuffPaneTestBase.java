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
Library (THDL). Portions created by the THDL are Copyright 2002-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.input;

import junit.framework.TestCase;

import org.thdl.util.ThdlOptions;

/**
 * @author David Chandler
 *
 * A base class whose subclasses tests {@link
 * org.thdl.tib.input.Duffpane} at the unit level.  */
public abstract class DuffPaneTestBase extends TestCase {
    /** A DuffPane that uses THDL's extended Wylie keyboard: */
    private DuffPane dp;

    /** Sets us up a DuffPane. */
    protected void setUp() {
        // We don't want to use options.txt:
        ThdlOptions.forTestingOnlyInitializeWithoutDefaultOptionsFile();

        // We don't want to load the TM or TMW font files ourselves:
        ThdlOptions.setUserPreference("thdl.rely.on.system.tmw.fonts", true);
        ThdlOptions.setUserPreference("thdl.rely.on.system.tm.fonts", true);
        ThdlOptions.setUserPreference("thdl.debug", true);

        dp = new DuffPane();
        dp.enableCaretManaging();
        dp.registerKeyboard();
    }

    /** Tears us down a DuffPane. */
    protected void tearDown() {
        // let GC do its worst with dp.  We're all set.
        dp = null;
    }

    /**
     * Plain vanilla constructor for DuffPaneTestBase.
     * @param arg0
     */
    public DuffPaneTestBase(String arg0) {
        super(arg0);
    }

    /** After ensuring that the caret is at the very end of the
     *  DuffPane's text, and that nothing is selected, this tells the
     *  DuffPane that the user has pressed key. */
    private void fireKeypressWithoutModifiers(char key) {
        dp.performKeyStroke(0, new String(new char[] { key }));
    }

    private void fireKeysWithoutModifiers(String x) {
        for (int i = 0; i < x.length(); i++) {
            fireKeypressWithoutModifiers(x.charAt(i));
        }
    }

    protected void ensureKeysGiveCorrectWylie(String wylie) {
        ensureKeysGiveCorrectWylie(wylie, wylie);
    }

    protected void e(String wylie) {
        ensureKeysGiveCorrectWylie(wylie);
    }

    protected void e(String in, String out) {
        ensureKeysGiveCorrectWylie(in, out);
    }

    protected void noExceptions(String keys) {
        dp.newDocument(); // initialize to a blank canvas.
        fireKeysWithoutModifiers(keys);
        // no assertion -- if an exception happens, then and only then
        // the test fails.
    }

    protected void ensureKeysGiveCorrectWylie(String keys, String wylie) {
        dp.newDocument(); // initialize to a blank canvas.
        fireKeysWithoutModifiers(keys);
        boolean passes = wylie.equals(dp.getWylie(new boolean[] { false }));
        if (!passes) {
            System.out.println("Congrats! These keys, \"" + keys
                               + "\", give this wylie, \"" + dp.getWylie(new boolean[] { false })
                               + "\", not the expected \"" + wylie + "\"");
        }
        assertTrue(passes);
    }

    protected void enableEWTSKeyboard() {
        new JskadKeyboard("EWTS for DuffPaneTest",
                          null,
                          null).activate(dp);
    }
        
    protected void enableACIPKeyboard() {
        new JskadKeyboard("Asian Classics Input Project (ACIP) FOR DuffPaneTest",
                          "acip_keyboard.ini",
                          null).activate(dp);
    }

    protected void enableSambhotaKeyboard() {
        new JskadKeyboard("Sambhota Keymap One FOR DuffPaneTest",
                          "sambhota_keyboard_1.ini",
                          null).activate(dp);
    }
}
// FIXME: EWTS needs a list of "native" stacks in it.

