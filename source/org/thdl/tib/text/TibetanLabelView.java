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

package org.thdl.tib.text;

import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.Segment;
import javax.swing.text.View;
import java.awt.Font;
import javax.swing.text.* ;

import org.thdl.tib.input.GlobalResourceHolder ;

/** A TibetanLabelView is a LabelView that has its own idea, informed
 *  by its knowledge of Tibetan, about where a good place to break
 *  text is.
 *
 *  <p>
 *
 *  If Character.isWhiteSpace() could be overridden, and if that only
 *  affected breaking (which is doubtful), we wouldn't need this--we'd
 *  just treat Tibetan punctuation there.  We might also like to
 *  override java.awt.font.GlyphMetrics idea of whitespace (though I'm
 *  not sure what consequences besides breaking that might have).  But
 *  we can't override either since they're final.  So we roll our own.
 *
 *  @author David Chandler */
class TibetanLabelView extends LabelView {
    private boolean logging;
    /** Creates a new TibetanLabelView. */
    public TibetanLabelView(Element e, boolean debugLog) {
        super(e);
        // FIXME: assert (e == this.getElement())

        logging = debugLog;
    }

    public int getBreakWeight(int axis, float pos, float len) {
        if (View.X_AXIS != axis) {
            // This doesn't impact line wrapping.
            return super.getBreakWeight(axis, pos, len);
        } else {
            int startPos = this.getElement().getStartOffset();

            int boundedPos = getPosNearTheEnd(startPos, pos, len);

            // boundedPos is short, and can be as short as startPos.
            // I don't know when to say something is good as opposed
            // to bad, but calling everything bad didn't work so well.
            // So let's call boundedPos <= startPos bad and everything
            // else without whitespace or tshegs et cetera good.
            if (boundedPos <= startPos)
                return View.BadBreakWeight;

            if (getGoodBreakingLocation(startPos, boundedPos) >= 0)
                return View.ExcellentBreakWeight;
            else
                return View.GoodBreakWeight;
        }
    }

    public View breakView(int axis, int p0, float pos, float len) {
        if (View.X_AXIS != axis) {
            // This doesn't impact line wrapping.
            return super.breakView(axis, p0, pos, len);
        } else {
            int boundedPos = getPosNearTheEnd(p0, pos, len);

            if (p0 == boundedPos) {
                // We can't call createFragment safely.  Return the
                // current view.
                return this;
            } else {
                int bloc = getGoodBreakingLocation(p0, boundedPos);
                int whereToBreak;
                if (bloc >= 0)
                    whereToBreak = bloc;
                else
                    whereToBreak = boundedPos;
                /* Return a new view, a fragment of the current one.
                 * If createFragment isn't smart, we could create
                 * infinitely many views of the same text if we don't
                 * check to see that this new view is actually
                 * different than the current view. */
                if (this.getStartOffset() != p0
                    || this.getEndOffset() != whereToBreak) {
                    return createFragment(p0, whereToBreak);
                } else
                    return this;
            }
        }
    }

    /** Returns a nonnegative offset if we find a character after
     *  which breaking would be good.  Returns negative otherwise. */
    private int getGoodBreakingLocation(int startOffset, int endOffset) {

        // Grab the underlying characters:
        Segment seggy = this.getText(startOffset, endOffset);

        // Now look for whitespace.  Going from the back is what you
        // want--otherwise, your 2nd line of text will be fuller than
        // your first.
        char currentChar = seggy.last();
        for (; currentChar != Segment.DONE; currentChar = seggy.previous()) {
            // FIXME: eeek!  How do we know when we're dealing with
            // Tibetan and when we're not?  This is styled text, so
            // where are the attributes etc.?  We should find the font
            // and decide about breaking based on that.  Well, we
            // should if we want a clean solution and don't mind a
            // little performance hit.
            //
            // This question only needs to be answered if you want a
            // clean solution, I think, because the code below should
            // work exactly the same.  Here's what's up: Even though
            // we aren't testing to see if we're typing Roman or
            // Tibetan, a character that's good for a line break in
            // one is also good in the other.  Methinks Tony Duff was
            // smart like that.
            //
            // To be explicit, the test below seems to work perfectly
            // for both Tibetan and Roman text.  (Maybe Roman text
            // will break after hyphens more quickly, but hey.)
            //
            // That said, this is still a FIXME.  But note that the
            // obvious fix will slow things down.

            if (Character.isWhitespace(currentChar) // FIXME: is this OK for Tibetan text?  Tony Duff may have made it so, but maybe not.  Test!
                || TibetanMachineWeb.isTMWFontCharBreakable(currentChar))
                {
                    // The '+ 1' is because you want to break after a
                    // tsheg or what not rather than before it.
                    int goodPlace = (startOffset + seggy.getIndex()
                                     - seggy.getBeginIndex() + 1);
                    if (logging) {
                        String s = new String(seggy.array, seggy.offset, seggy.count);
                        if (!"\n".equals(s)) {
                            System.out.println("TibetanLabelView: found a good break in \""
                                               + new String(seggy.array, seggy.offset, seggy.count)
                                                   + "\"; we should break after character "
                                               + (seggy.getIndex() - seggy.getBeginIndex() + 1)
                                               + " (counting begins at one)");
                        }
                    }
                    return goodPlace;
                }
        }

        // There is no good place.  Return a negative number.
        if (logging) {
            String s = new String(seggy.array, seggy.offset, seggy.count);
            if (!"\n".equals(s)) {
                System.out.println("TibetanLabelView: found NO good break in \""
                                   + new String(seggy.array, seggy.offset, seggy.count)
                                       + "\"");
            }
        }
        return -1;
    }

    /** Returns a position just before or at the position specified by
     *  the three arguments.  viewToModel seems like the thing to use,
     *  but we don't have the parameters to pass to it.  We can call
     *  GlyphView.GlyphPainter.getBoundedPosition(..)  instead, and
     *  its comment mentions viewToModel, so maybe this is actually
     *  better.
     */
    private int getPosNearTheEnd(int startPos, float pos, float len) {
        // this is provided, and it appears that we'd better use it:
        checkPainter();

        return this.getGlyphPainter().getBoundedPosition(this, startPos,
                                                         pos, len);
    }

	/** When an element contains only diacritic chars, the default
	 *  implementation will return 0.0 (since there is no base char to
	 *  attach the diacritic(s) to. But the painting scheme will ignore
	 *  all zero sized items. The point of the workaround below is just to 
	 *  return any non-zero value so that the item doesn't get ignored.
	 *  (Try typint oM in exteded-Wylie kbd mode to see the bug). */
	public float getPreferredSpan ( int axis )
	{
		float span = super.getPreferredSpan ( axis ) ;

		//
		// the condition should be safe enough, we want an element that has at least one char
		// but whose default span is zero.
		//
		if ( View.X_AXIS == axis && 0.0f == span && 
			 ( getElement ().getEndOffset () - getElement ().getStartOffset () + 1 ) > 0 )
		{
			span = 1.0f ;
		}

		return span ;
	}

    /**
     * getFont - we override the method so we can use local fonts
     */
	public Font getFont ()
	{
		String fontFamily = (String)getElement ().getAttributes ()
                                  .getAttribute ( StyleConstants.FontFamily ) ;
		int fontSize = ((Integer)(getElement ().getAttributes ()
                                  .getAttribute ( StyleConstants.FontSize ))).intValue () ;

		Font font = null ;

		try
		{
			font = GlobalResourceHolder.getFont ( fontFamily, fontSize ) ;

			if ( null == font )
				font = super.getFont () ;
		}
		catch ( Exception e )
		{
			System.err.println ( "No font !" ) ;
			font = super.getFont () ;
		}

		return font ;
	}
}
