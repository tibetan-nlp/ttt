/*
 The contents of this file are subject to the AMP Open Community License
 Version 1.0 (the "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License on the AMP web site 
 (http://www.tibet.iteso.mx/Guatemala/).
 
 Software distributed under the License is distributed on an "AS IS" basis, 
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
 License for the specific terms governing rights and limitations under the 
 License. 
 
 The Initial Developer of this software is Andres Montano Pellegrini. Portions
 created by Andres Montano Pellegrini are Copyright 2001 Andres Montano
 Pellegrini. All Rights Reserved. 
 
 Contributor(s): ______________________________________.
 */

package org.thdl.tib.scanner;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import org.thdl.tib.input.DuffPane;
import org.thdl.tib.text.TibetanDocument;
import org.thdl.tib.text.reverter.*;
import org.thdl.util.RTFFixerInputStream;
import org.thdl.util.ThdlDebug;
import org.thdl.util.ThdlOptions;

/** Identical to DuffPane except that it only supports Tibetan script in
 TibetanMachineWeb. No roman script can be inputted. If roman script is
 pasted, it is assumed that it is either ACIP or wylie and is converted
 accordingly to TibetanMachineWeb. If text in TibetanMachine is pasted,
 it is converted to TibetanMachineWeb. Any other font is assumed to be
 Roman script.
 */
public class StrictDuffPane extends DuffPane
{
	public StrictDuffPane()
	{
		super();
		disableRoman();
	}
	
	/** Smart paste! Automatically recognizes what is being pasted and converts
	 respectively. Currently it supports pasting from TibetanMachineWeb,
	 TibetanMachine, wylie, and ACIP.
	 */
	public void paste(int offset) 
	{
		boolean pasteAsString = false;
		
		// Respect setEditable(boolean):
		if (!this.isEditable())
			return;
		
		try 
		{
			Transferable contents = rtfBoard.getContents(this);
			
			if (contents.isDataFlavorSupported(rtfFlavor)){
				
				InputStream in = (InputStream)contents.getTransferData(rtfFlavor);
				int p1 = offset;
				
				//construct new document that contains only portion of text you want to paste
				TibetanDocument sd = new TibetanDocument();
				
				// I swear this happened once when I pasted in some
				// random junk just after Jskad started up.
				ThdlDebug.verify(null != in);
				
				boolean errorReading = false;
				
				try 
				{
					if (!ThdlOptions.getBooleanOption("thdl.do.not.fix.rtf.hex.escapes"))
						in = new RTFFixerInputStream(in);
					rtfEd.read(in, sd, 0);
				} catch (Exception e) {
					
					errorReading = true;
					
					/* If fonts weren't supported and we don't know what it is try to paste
					 ACIP or wylie.
					 */
					if (contents.isDataFlavorSupported(DataFlavor.stringFlavor))
					{
						pasteAsString = true;
					}
					// JOptionPane.showMessageDialog(this, "You cannot paste from the application from which you copied.\nIt uses an RTF format that is too advanced for the version\nof Java Jskad is running atop.");
				}
				
				if (!errorReading) 
				{
					/* If it is any font beside TibetanMachine and TibetanMachineWeb
					 assume it is wylie or Acip.
					 */
					if (!sd.getFont((sd.getCharacterElement(0).getAttributes())).getFamily().startsWith("TibetanMachine")
							&& contents.isDataFlavorSupported(DataFlavor.stringFlavor))
					{
						pasteAsString = true;
					}
					else
					{
						// If it's font is TibetanMachine, convert to TibetanMachineWeb first
						if (sd.getFont((sd.getCharacterElement(0).getAttributes())).getFamily().equals("TibetanMachine"))
						{
							StringBuffer errors = new StringBuffer();
							long numAttemptedReplacements[] = new long[] { 0 };
							sd.convertToTMW(0, -1, errors, numAttemptedReplacements);
						}
						
						for (int i=0; i<sd.getLength()-1; i++) { //getLength()-1 so that final newline is not included in paste
							try 
							{
								String s = sd.getText(i,1);
								AttributeSet as = sd.getCharacterElement(i).getAttributes();
								getTibDoc().insertString(p1+i, s, as);
							} catch (BadLocationException ble) 
							{
								ble.printStackTrace();
								ThdlDebug.noteIffyCode();
							}
						}
					}
				}
			}
			else if (contents.isDataFlavorSupported(DataFlavor.stringFlavor))
			{
				// if it is not in a font, assume it is wylie or ACIP.
				pasteAsString = true;
			}
			
			if (pasteAsString)
			{
				String data = (String)contents.getTransferData(DataFlavor.stringFlavor);
				if (Manipulate.guessIfUnicode(data)) data = BasicTibetanTranscriptionConverter.unicodeToWylie(data);	
				else if (Manipulate.guessIfAcip(data)) data = BasicTibetanTranscriptionConverter.acipToWylie(data);
				toTibetanMachineWeb(data, offset);            	
			}
			
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
			ThdlDebug.noteIffyCode();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			ThdlDebug.noteIffyCode();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			ThdlDebug.noteIffyCode();
		}
	}
}
