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
Library (THDL). Portions created by the THDL are Copyright 2001-2005 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.text;

import java.util.StringTokenizer;

/** A class that can create HTML that uses the Tibetan Machine Web
 *  font, which is really ten font files.  This is intended to be used
 *  by Xalan XSLT to convert an XML document that uses Wylie into HTML
 *  that uses TMW.  Or something like that -- I, David Chandler,
 *  didn't write this class, Edward Garrett did.  But now that
 *  EWTS->TMW conversion is best done by org.thdl.tib.text.ttt, this
 *  class is deprecated.  Really, it'd be easy to undeprecate it --
 *  just stop calling the deprecated EWTS->TMW function that's being
 *  called in favor of the new, better function.
 *  @author Edward Garrett, Tibetan and Himalayan Digital Library
 *  @deprecated
 */
public class TibetanHTML {
    static String[] styleNames =
    {"tmw","tmw1","tmw2","tmw3","tmw4","tmw5","tmw6","tmw7","tmw8","tmw9"};

    /** Returns CSS needed if you use other functions in this class.
     *  @param fontSize the base-10 integral font size.  */
    public static String getStyles(String fontSize) {
        return ".tmw {font: "+fontSize+"pt TibetanMachineWeb}\n"+
            ".tmw1 {font: "+fontSize+"pt TibetanMachineWeb1}\n"+
            ".tmw2 {font: "+fontSize+"pt TibetanMachineWeb2}\n"+
            ".tmw3 {font: "+fontSize+"pt TibetanMachineWeb3}\n"+
            ".tmw4 {font: "+fontSize+"pt TibetanMachineWeb4}\n"+
            ".tmw5 {font: "+fontSize+"pt TibetanMachineWeb5}\n"+
            ".tmw6 {font: "+fontSize+"pt TibetanMachineWeb6}\n"+
            ".tmw7 {font: "+fontSize+"pt TibetanMachineWeb7}\n"+
            ".tmw8 {font: "+fontSize+"pt TibetanMachineWeb8}\n"+
            ".tmw9 {font: "+fontSize+"pt TibetanMachineWeb9}\n";
    }

    public static String getHTMLX(String wylie) {
        try {
            StringBuffer buffer = new StringBuffer();
            for (StringTokenizer tokenizer = new StringTokenizer(wylie, " \t\n", true); tokenizer.hasMoreElements();) {
                String next = tokenizer.nextToken();
                if (next.equals("\t") || next.equals("\n")) {
                    buffer.append("<wbr/>");
                    buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS("_")));  // hard-coded EWTS
                    buffer.append("<wbr/>");
                }
                else
                    buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS(next)));
            }
            return buffer.toString();
        } catch (InvalidWylieException ive) {
            return "";
        }
    }

    public static String getHTMLX(DuffData[] duffData) {
        String[] styleNames =
            {"tmw","tmw1","tmw2","tmw3","tmw4","tmw5","tmw6","tmw7","tmw8","tmw9"};

        StringBuffer htmlBuffer = new StringBuffer();
        htmlBuffer.append("<nobr>");

        for (int i=0; i<duffData.length; i++) {
            char[] c = duffData[i].text.toCharArray();
            for (int k=0; k<c.length; k++) {
                htmlBuffer.append("<span class=\"");
                htmlBuffer.append(styleNames[duffData[i].font-1]);
                htmlBuffer.append("\">");

                if (c[k] > 32 && c[k] < 127) { //ie if it's not formatting
                    switch (c[k]) {
                        case '"':
                            htmlBuffer.append("&quot;");
                            break;
                        case '<':
                            htmlBuffer.append("&lt;");
                            break;
                        case '>':
                            htmlBuffer.append("&gt;");
                            break;
                        case '&':
                            htmlBuffer.append("&amp;");
                            break;
                        default:
                            htmlBuffer.append(c[k]);
                            break;
                    }
                    htmlBuffer.append("</span>");
                    String wylie = TibetanMachineWeb.getWylieForGlyph(duffData[i].font, c[k], TibTextUtils.weDoNotCareIfThereIsCorrespondingWylieOrNot);
                    if (TibetanMachineWeb.isWyliePunc(wylie))
                        htmlBuffer.append("<wbr/>");
                } else {
                    htmlBuffer.append("</span><br/>");
                }
            }
        }

        htmlBuffer.append("</nobr>");
        return htmlBuffer.toString();
    }

    public static String getIndentedHTML(String wylie) {
        return getHTML("_" + wylie);  // hard-coded EWTS
    }

    public static String getHTML(String wylie) {
        try {
            StringBuffer buffer = new StringBuffer();
            for (StringTokenizer tokenizer = new StringTokenizer(wylie, " \t\n", true); tokenizer.hasMoreElements();) {
                String next = tokenizer.nextToken();
                if (next.equals("\t") || next.equals("\n")) {
                    buffer.append("<wbr/>");
                    buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS("_")));  // hard-coded EWTS
                    buffer.append("<wbr/>");
                }
                else
                    buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS(next)));
            }
            return buffer.toString();
        } catch (InvalidWylieException ive) {
            return "";
        }
    }

    public static String getHTML(DuffData[] duffData) {
        String[] styleNames =
            {"tmw","tmw1","tmw2","tmw3","tmw4","tmw5","tmw6","tmw7","tmw8","tmw9"};

        StringBuffer htmlBuffer = new StringBuffer();
        htmlBuffer.append("<nobr>");

        for (int i=0; i<duffData.length; i++) {
            htmlBuffer.append("<span class=\"");
            htmlBuffer.append(styleNames[duffData[i].font-1]);
            htmlBuffer.append("\">");
            char[] c = duffData[i].text.toCharArray();
            for (int k=0; k<c.length; k++) {
                if (c[k] > 31 && c[k] < 127) { //ie if it's not formatting
                    switch (c[k]) {
                        case '"':
                            htmlBuffer.append("&quot;");
                            break;
                        case '<':
                            htmlBuffer.append("&lt;");
                            break;
                        case '>':
                            htmlBuffer.append("&gt;");
                            break;
                        case '&':
                            htmlBuffer.append("&amp;");
                            break;
                        default:
                            htmlBuffer.append(c[k]);
                            break;
                    }
                    String wylie = TibetanMachineWeb.getWylieForGlyph(duffData[i].font, c[k], TibTextUtils.weDoNotCareIfThereIsCorrespondingWylieOrNot);
                    if (TibetanMachineWeb.isWyliePunc(wylie))
                        htmlBuffer.append("<wbr/>");
                } else {
                    htmlBuffer.append("<br/>");
                }
            }
            htmlBuffer.append("</span>");
        }

        htmlBuffer.append("</nobr>");
        return htmlBuffer.toString();
    }

    public static String getHTMLforJava(String wylie) {
        //differences:
        //    as of 1.4.1, anyway, browser built into java does not accept <wbr/> and <br/>,
        //    only <wbr> and <br>

        try {
            StringBuffer buffer = new StringBuffer();
            for (StringTokenizer tokenizer = new StringTokenizer(wylie, " \t\n", true); tokenizer.hasMoreElements();) {
                String next = tokenizer.nextToken();
                if (next.equals("\t") || next.equals("\n")) {
                    buffer.append("<wbr>");
                    buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS("_")));
                    buffer.append("<wbr>");
                }
                else
                    buffer.append(getHTML(TibTextUtils.getTibetanMachineWebForEWTS(next)));
            }
            return buffer.toString();
        } catch (InvalidWylieException ive) {
            return "";
        }
    }

    public static String getHTMLforJava(DuffData[] duffData) {
        String[] fontNames = {
            "TibetanMachineWeb","TibetanMachineWeb1", "TibetanMachineWeb2",
            "TibetanMachineWeb3","TibetanMachineWeb4","TibetanMachineWeb5",
            "TibetanMachineWeb6","TibetanMachineWeb7","TibetanMachineWeb8",
            "TibetanMachineWeb9"};

        StringBuffer htmlBuffer = new StringBuffer();
        htmlBuffer.append("<nobr>");

        for (int i=0; i<duffData.length; i++) {
            htmlBuffer.append("<font size=\"36\" face=\"");
            htmlBuffer.append(fontNames[duffData[i].font-1]);
            htmlBuffer.append("\">");
            char[] c = duffData[i].text.toCharArray();
            for (int k=0; k<c.length; k++) {
                if (c[k] > 31 && c[k] < 127) { //ie if it's not formatting
                    switch (c[k]) {
                        case '"':
                            htmlBuffer.append("&quot;");
                            break;
                        case '<':
                            htmlBuffer.append("&lt;");
                            break;
                        case '>':
                            htmlBuffer.append("&gt;");
                            break;
                        case '&':
                            htmlBuffer.append("&amp;");
                            break;
                        default:
                            htmlBuffer.append(c[k]);
                            break;
                    }
                    String wylie = TibetanMachineWeb.getWylieForGlyph(duffData[i].font, c[k], TibTextUtils.weDoNotCareIfThereIsCorrespondingWylieOrNot);
                    if (TibetanMachineWeb.isWyliePunc(wylie))
                        htmlBuffer.append("<wbr>");
                } else {
                    htmlBuffer.append("<br>");
                }
            }
            htmlBuffer.append("</font>");
        }

        htmlBuffer.append("</nobr>");
        return htmlBuffer.toString();
    }
}
