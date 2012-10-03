package org.thdl.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComponent;

public class ThdlI18n {
	private static Locale locale = null;
	private static ResourceBundle resources = null;

	private ThdlI18n() {}

	public static ResourceBundle getResourceBundle() {
		if (resources == null)
			resources = ResourceBundle.getBundle("MessageBundle", getLocale());
		return resources;
	}
	public static void setLocale(Locale l) {
		Locale[] locales = Locale.getAvailableLocales();
		for (int k=0; k<locales.length; k++)
			if (locales[k].equals(locale)) {
				JComponent.setDefaultLocale(l);
				locale = l;
				break;
			}
	}
	public static Locale getLocale() {
		if (locale == null)
			locale = Locale.getDefault();
		return locale;
	}
}