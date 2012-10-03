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
Library (THDL). Portions created by the THDL are Copyright 2001, 2004 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.util;

/**
 * @author David Chandler
 *
 * <p>A ThdlLazyException wraps a "real" java.lang.Exception.
 * ThdlLazyException, however, is unchecked.  Thus, if you want
 * to be a lazy Java programmer, you can use this idiom:</p>
 * <pre>
 * // Note that bleh does not throw any checked exceptions: 
 * public static void bleh() {
 *     try {
 *         Foo.bar();
 *     } catch (Throwable t) {
 *         // Nah, this'll never happen!
 *         throw new ThdlLazyException(e);
 *     }
 * }
 * </pre>
 * 
 * <p>Now your code appears to be well written, and no one but the user
 * will ever know the difference.</p>
 * 
 * <p>In Java 1.4 (1.3?), Sun introduced constructors for java.lang.Error that
 * allow much the same thing.  This code is Java 1 compatible, though, and
 * is also a red flag, equivalent to a // FIXME comment.</p>
 */
public final class ThdlLazyException extends Error {

    /**
     * the wrapped exception
     */
    private Throwable wrappedException = null;

    /**
     * Constructor for ThdlLazyException.
     */
    public ThdlLazyException() {
        super();
    }

    /**
     * Constructor for ThdlLazyException.
     * @param descrip description
     */
    public ThdlLazyException(String descrip) {
        super(descrip);
    }

    /**
     * Constructor for ThdlLazyException.
     * @param descrip description
     * @param realException the exception the user should actually care about
     */
    public ThdlLazyException(String descrip, Throwable realException) {
        super(descrip);
        wrappedException = realException;
    }

    /**
     * Constructor for ThdlLazyException.
     * @param realException the exception the user should actually care about
     */
    public ThdlLazyException(Throwable realException) {
        super();
        wrappedException = realException;
    }

    /**
     * Returns the wrapped exception, the one about which you should actually
     * be concerned.
     */
    public Throwable getRealException() {
        return wrappedException;
    }

    public String toString() {
        return "ThdlLazyException [" + super.toString() + "] wrapping " + ((getRealException() == null) ? "nothing" : getRealException().toString());
    }
    public String getMessage() {
        return "ThdlLazyException [" + super.getMessage() + "] wrapping " + ((getRealException() == null) ? "nothing" : getRealException().getMessage());
    }
}
