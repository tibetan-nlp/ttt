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
Library (THDL). Portions created by the THDL are Copyright 2001 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides a clean interface to the multi-tiered system of user
 * preferences (also known as options).
 *
 * <p>The {@link java.util.Properties java.util.Properties} class
 * makes it easy for us to provide a hierarchical preferences (that
 * is, options) structure.  One option a user might wish to set is the
 * font size for Tibetan, for example. The highest precedence goes to
 * a pref that the user set on the Java command line using something
 * like <code>java -Dpref=value -jar jskad_or_whatever.jar</code>.  If
 * the user went to the trouble of doing this, we definitely want to
 * respect his or her wish.  The next highest precedence goes to an
 * individual user's preferences file (a file readable and writable by
 * {@link java.util.Properties java.util.Properties}, but also
 * hand-editable), if one exists.  Next is the system-wide preferences
 * file, if one exists.  Finally, we fall back on the preferences file
 * shipped with the application inside the JAR.</p>
 *
 * <p>ThdlOptions is not instantiable.  It contains only static
 * methods for answering questions about the user's preferences.</p>
 * 
 * <p>There are three kinds of preferences: boolean-valued preferences
 * ("true" or "false"), integer-valued preferences, and string-valued
 * preferences.  Boolean-valued preferences should, by convention, be
 * false by default.  If you want to enable feature foo by default,
 * but give a preference for disabling it, then call the preference
 * "DisableFeatureFoo".  If you want to disable feature foo by
 * default, call it "EnableFeatureFoo".  This makes the users' lives
 * easier.</p>
 * 
 * @author David Chandler
 */
public class ThdlOptions {
	/**
	 * So that you're not tempted to instantiate this class, the
	 * constructor is private: */
	private ThdlOptions() {
        // don't instantiate this class.
	}

	/**
	 * Returns the value of a boolean-valued option, or false if that
	 * option is set nowhere in the hierarchy of properties files.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment)
     */
	public static boolean getBooleanOption(String optionName)
    {
        init();

        // Look to the System first.
        String answer = getSystemValue(optionName);
        if (answer == null) {
            if (null != userProperties) {
                answer = userProperties.getProperty(optionName, "false");
            } else {
                answer = "false";
            }
        }
        return answer.equalsIgnoreCase("true");
    }

    /** Returns the value of the system property optionName, or null
        if that value is not set. */
    private static String getSystemValue(String optionName) {
        // Look to the System first.
        String answer = null;
        try {
            answer = System.getProperty(optionName);
        } catch (SecurityException e) {
            if (!suppressErrs())
                throw e;
        }
        return answer;
    }

	/**
	 * Returns the value of a string-valued option, or null if that
	 * option is set nowhere in the hierarchy of properties files.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment) */
	public static String getStringOption(String optionName)
    {
        init();
        // Look to the System first.
        String answer = getSystemValue(optionName);
        if (answer == null) {
            if (null != userProperties) {
                answer = userProperties.getProperty(optionName, null);
            }
        }
        return answer;
    }

	/**
	 * Returns the value of a string-valued option, or defaultValue if that
	 * option is set nowhere in the hierarchy of properties files.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment)
     * @param defaultValue the default value */
	public static String getStringOption(String optionName,
                                         String defaultValue)
    {
        String x = getStringOption(optionName);
        if (null == x)
            return defaultValue;
        else
            return x;
    }

	/**
	 * Returns the value of an integer-valued option, or defaultValue
	 * if that option is set nowhere in the hierarchy of properties
	 * files or is set to something that cannot be decoded to an
	 * integer.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment)
     * @param defaultValue the default value */
	public static int getIntegerOption(String optionName, int defaultValue)
    {
        Integer x = getIntegerOption(optionName);
        if (null == x)
            return defaultValue;
        else
            return x.intValue();
    }

	/**
	 * Returns the value of an integer-valued option, or null if that
	 * option is set nowhere in the hierarchy of properties files or
	 * is set to something that cannot be decoded to an integer.
	 * 
	 * @param optionName the option whose value you wish to retrieve
	 * (note the naming conventions detailed in the class comment) */
	public static Integer getIntegerOption(String optionName)
    {
        init();

        // Look to the System first.
        String answer = getSystemValue(optionName);
        if (answer == null) {
            if (null != userProperties) {
                answer = userProperties.getProperty(optionName, null);
            }
        }
        if (null == answer) {
            return null;
        } else {
            try {
                return Integer.decode(answer);
            } catch (NumberFormatException e) {
                if (getBooleanOption("thdl.debug"))
                    throw new ThdlLazyException(e);
                else
                    return null;
            }
        }
    }

    private static boolean suppressErrs() {
        return false;
        /* FIXME--make THIS configurable.  It's not a simple thing,
         * though, since you can't use the usual prefences mechanism
         * because it helps to implement that mechanism. */
    }
    private static void init() {
        try {
            initialize(ThdlOptions.class, "/options.txt",
                       suppressErrs());
        } catch (FileNotFoundException e) {
            if (!suppressErrs())
                throw new ThdlLazyException(e);
        }
    }

    /** the properties object that is chained to the system-wide
        properties which is chained to the built-in properties which
        is chained to the Java System properties */
    private static Properties userProperties = null;

    /** to avoid initializing twice */
    private static boolean isInitialized = false;

    /** Call this when you're testing some code that uses the
     *  preferences mechanism provided by this class, and you don't
     *  want to use the system preferences or the user preferences
     *  with that code.  (By "system or user preferences", think
     *  options.txt and my_thdl_preferences.txt.)  You'll be relying
     *  on the defaults encoded in the calls to getBooleanOption etc.
     *  If you call this twice, it will wipe out preferences stored
     *  programmatically on each call. */
    public static void forTestingOnlyInitializeWithoutDefaultOptionsFile() {
        userProperties = new Properties(); // empty
        isInitialized = true;
    }

    /** Sets userProperties so that it represents the entire, chained
        preferences hierarchy.

        @param resourceHolder the class associated with the builtin
        defaults properties file resource
        @param resourceName the name of the builtin defaults
        properties file resource
        @param suppressErrors true if the show must go on, false if
        you want unchecked exceptions thrown when bad things happen

        @throws FileNotFoundException if !suppressErrors and if the
        user gave the location of the system-wide or user preferences
        files, but gave it incorrectly. */
    private static void initialize(Class resourceHolder,
                                   String resourceName,
                                   boolean suppressErrors)
        throws FileNotFoundException
    {
        if (isInitialized)
            return;
        isInitialized = true;
        try {

            // Get the application's built-in, default properties.
            Properties defaultProperties
                = getPropertiesFromResource(resourceHolder,
                                            resourceName,
                                            suppressErrors,
                                            new Properties() /* empty */);

            // Get the system's properties, if the system administrator
            // has created any:
            Properties systemWideProperties
                = tryToGetPropsFromFile("thdl.system.wide.options.file",
                                        // FIXME this default is
                                        // system-dependent:
                                        "C:\\thdl_opt.txt",
                                        defaultProperties,
                                        suppressErrors);

            // Get the user's properties, if they've set any:
            userProperties
                = tryToGetPropsFromFile("thdl.user.options.directory",
                                        getUserPreferencesPath(),
                                        systemWideProperties,
                                        suppressErrors);
        } catch (SecurityException e) {
            if (suppressErrors) {
                if (userProperties == null) {
                    userProperties = new Properties(); // empty
                } // else leave it as is.
            } else {
                throw new ThdlLazyException(e);
            }
        }
    }

    /** Returns a new, nonnull Properties object if (1) a preferences
        file is found at the location specified by the value of the
        System property pName, if it is set, or at defaultLoc, if
        pName is not set, and (2) if that file is successfully read
        in.  Otherwise, this returns defaultProps.

        @param pName the name of the System property that overrides
        this application's default idea of where to look for the file
        @param defaultLoc the default preferences file name
        @param suppressErrors true iff you want to proceed without
        throwing exceptions whenever possible
        
        @throws FileNotFoundException if !suppressErrors and if the
        user gave the location of the system-wide or user preferences
        files, but gave it incorrectly.  @throws SecurityException if
        playing with files or system properties is not OK */
    private static Properties tryToGetPropsFromFile(String pName,
                                                    String defaultLoc,
                                                    Properties defaultProps,
                                                    boolean suppressErrors)
        throws FileNotFoundException, SecurityException
    {
        String systemPropFileName = System.getProperty(pName, defaultLoc);
        
        /* The empty string means "use the default location".  See
         * options.txt. */
        if ("".equals(systemPropFileName))
            systemPropFileName = defaultLoc;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(systemPropFileName);
        } catch (FileNotFoundException e) {
            if (null != System.getProperty(pName)) {
                // the user explicitly set this, but not
                // correctly.
                if (!suppressErrors)
                    throw e;
            } else {
                // definitely suppress this.  On a Mac, I think
                // this'll happen every time at present.  (FIXME)
            }
        }

        Properties props = defaultProps;
        if (fis != null) {
            props = getPropertiesFromStream(fis,
                                            suppressErrors,
                                            defaultProps);
            try {
                fis.close();
            } catch (IOException e) {
                // suppress this error.
            }
        }
        return props;
    }


    /** The resource named resourceName is find and read in using
     *  resourceHolder for guidance.  Default properties are provided
     *  by defaults if it is non-null.  If suppressErrors is true, no
     *  exceptions will be thrown in normal operation.  Otherwise, an
     *  unchecked exception may be thrown upon error. */
    public static Properties getPropertiesFromResource(Class resourceHolder,
                                                        String resourceName,
                                                        boolean suppressErrors,
                                                        Properties defaults)
    {
        InputStream in = resourceHolder.getResourceAsStream(resourceName);
        return getPropertiesFromStream(in, suppressErrors, defaults);
    }

    /** Returns a Properties object whose default values come from
        defaults if defaults is non-null.  The properties specified in
        the properties file from which in is open for reading override
        the default property values.  If suppressErrors is true, then
        this routine tries to always avoid failure. */
    private static Properties getPropertiesFromStream(InputStream in,
                                                      boolean suppressErrors,
                                                      Properties defaults)
    {
        Properties options;
        if (defaults == null)
            options = new Properties(); // empty properties list
        else
            options = new Properties(defaults);
        try {
            // Load props from the resource:
            options.load(in);
            return options;
        } catch (Exception e) {
            // e is an IOException or a SecurityException or, if the
            // resource was not found, a NullPointerException.
            if (suppressErrors) {
                return options;
            } else {
                throw new ThdlLazyException(e);
            }
        }
    }

    /** Deletes the user's preferences file, a file whose path is the
     *  value of {@link #getUserPreferencesPath()}.  You must restart
     *  the application before this will take effect.
     *
     *  @throws IOException if an IO or security exception occurs
     *  while deleting from disk. */
    public static void clearUserPreferences() throws IOException {
        // Avoid saving the preferences to disk at program exit.  Do
        // this first in case an exception is thrown during deletion.
        userProperties = null;

        File pf = new File(getUserPreferencesPath());

        try {
            if (pf.exists() && !pf.delete()) {
                throw new IOException("Could not delete the preferences file "
                                      + pf.getAbsolutePath());
            }
        } catch (SecurityException e) {
            throw new IOException("Could not delete the preferences file because a SecurityManager is in place and your security policy does not allow deleting "
                                  + pf.getAbsolutePath());
        }
    }

    /** Saves the user's preferences to a file whose path is the value
     *  of {@link #getUserPreferencesPath()}.  You must call
     *  <code>setUserPreference(..)</code> for this to be effective.
     *
     *  @return true on success, false if no preferences exist to be
     *  saved, such as after calling clearUserPreferences()
     *
     *  @throws IOException if an IO exception occurs while writing to
     *  the disk. */
    public static boolean saveUserPreferences() throws IOException {
        String lineSep = System.getProperty("line.separator");
        if (null == lineSep || "".equals(lineSep)) lineSep = "\n";
        if (null != userProperties) {
            userProperties.store(new FileOutputStream(getUserPreferencesPath()),
                                 " This file was automatically created by a THDL tool." + lineSep
                                 + "# You may edit this file, but it will be recreated," + lineSep
                                 + "# so your comments will be lost.  Moreover, you must" + lineSep
                                 + "# edit this file only after exiting all THDL tools." + lineSep
                                 + "# " + lineSep
                                 + "# To understand this file's contents, please see" + lineSep
                                 + "# options.txt in the JAR file." + lineSep
                                 + "# " + lineSep
                                 + "# Note that this is the user-specific preferences file." + lineSep
                                 + "# This tool also supports a system-specific preferences" + lineSep
                                 + "# file, which the user-specific preferences override." + lineSep
                                 + "# " + lineSep
                                 + "# Note also that you can set a JVM preference at run-time." + lineSep
                                 + "# Doing so will override both system- and user-specific" + lineSep
                                 + "# preferences.  On many systems, you do this like so:" + lineSep
                                 + "# 'java -Dthdl.default.tibetan.font.size=36 -jar Jskad.jar'" + lineSep
                                 + "# " + lineSep
                                 + "# There is, unfortunately, no further documentation on the" + lineSep
                                 + "# preferences mechanism at this time.  Yell for it!" + lineSep
                                 + "# " + lineSep
                                 + "# Created at:"); // DLC FIXME: document the preferences mechanism.
            return true;
        }
        return false;
    }

    /** This returns the location of the user's preferences file.
     *  This value may be overridden, by, you guessed it, a JVM,
     *  built-in, or system-wide preference
     *  <code>thdl.user.options.directory</code>
     */
    public static String getUserPreferencesPath() {
        String defaultUserDir;
        switch (OperatingSystemUtils.getOSType()) {
        case OperatingSystemUtils.MAC:
            // where?  DLC FIXME
            defaultUserDir = "/tmp";
            break;
        case OperatingSystemUtils.WIN32:
            defaultUserDir = "C:\\";
            break;
        default:
            //put linux etc. here
            defaultUserDir = "/tmp";
            break;
        }

        String defaultLoc = System.getProperty("user.home", defaultUserDir);
        String systemsOverridingValue
            = System.getProperty("thdl.user.options.directory", defaultLoc);

        return (new File(systemsOverridingValue,
                         "my_thdl_preferences.txt")).getPath();
    }

    /** In order to save preferences, this class must know that the
     *  user (explicitly or implicitly) has changed a preference,
     *  either through selecting something in a ComboBox, going
     *  through a Preferences GUI, or the like.  Calling this method
     *  indicates that the user has changed an integer-valued
     *  preference pref to value.
     *  @param pref the preference the user is setting
     *  @param value the user's new preference
     */
    public static void setUserPreference(String pref, int value) {
        if (userProperties == null) {
            userProperties = new Properties(); // empty
        } // else leave it as is.
        userProperties.setProperty(pref, String.valueOf(value));
    }

    /** In order to save preferences, this class must know that the
     *  user (explicitly or implicitly) has changed a preference,
     *  either through selecting something in a ComboBox, going
     *  through a Preferences GUI, or the like.  Calling this method
     *  indicates that the user has changed a boolean-valued
     *  preference pref to value.
     *  @param pref the preference the user is setting
     *  @param value the user's new preference
     */
    public static void setUserPreference(String pref, boolean value) {
        if (userProperties == null) {
            userProperties = new Properties(); // empty
        } // else leave it as is.
        userProperties.setProperty(pref, String.valueOf(value));
    }

    /** In order to save preferences, this class must know that the
     *  user (explicitly or implicitly) has changed a preference,
     *  either through selecting something in a ComboBox, going
     *  through a Preferences GUI, or the like.  Calling this method
     *  indicates that the user has changed a String-valued preference
     *  pref to value.
     *  @param pref the preference the user is setting
     *  @param value the user's new preference
     */
    public static void setUserPreference(String pref, String value) {
        if (userProperties == null) {
            userProperties = new Properties(); // empty
        } // else leave it as is.
        userProperties.setProperty(pref, value);
    }
}
