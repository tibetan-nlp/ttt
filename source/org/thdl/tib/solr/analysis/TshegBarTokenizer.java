package org.thdl.tib.solr.analysis;

import java.io.IOException;
import java.io.Reader;

import org.thdl.tib.solr.util.TshegBarUtils;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.analysis.util.CharacterUtils;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.util.CharacterUtils.CharacterBuffer;

/**
 * Takes stream of Unicode Tibetan text and tokenizes it
 * into "syllables" or tsheg bars. Note that this is not
 * equivalent to tokenizing into "words" since words frequently
 * consist of more than one tsheg bar.
 * <p>
 * Non-Tibetan text and Tibetan punctuation is ignored by this
 * class.
 *
 * @author Edward Garrett
 */
public class TshegBarTokenizer extends Tokenizer {
  
  /**
   * Creates a new {@link TshegBarTokenizer} instance
   * 
   * @param matchVersion
   *          Lucene version to match See {@link <a href="#version">above</a>}
   * @param input
   *          the input to split up into tokens
   */
  public TshegBarTokenizer(Version matchVersion, Reader input) {
    super(input);
    charUtils = CharacterUtils.getInstance(matchVersion);
  }
  
  /**
   * Creates a new {@link TshegBarTokenizer} instance
   * 
   * @param matchVersion
   *          Lucene version to match See {@link <a href="#version">above</a>}
   * @param source
   *          the attribute source to use for this {@link Tokenizer}
   * @param input
   *          the input to split up into tokens
   */
  public TshegBarTokenizer(Version matchVersion, AttributeSource source,
      Reader input) {
    super(source, input);
    charUtils = CharacterUtils.getInstance(matchVersion);
  }
  
  /**
   * Creates a new {@link TshegBarTokenizer} instance
   * 
   * @param matchVersion
   *          Lucene version to match See {@link <a href="#version">above</a>}
   * @param factory
   *          the attribute factory to use for this {@link Tokenizer}
   * @param input
   *          the input to split up into tokens
   */
  public TshegBarTokenizer(Version matchVersion, AttributeFactory factory,
      Reader input) {
    super(factory, input);
    charUtils = CharacterUtils.getInstance(matchVersion);
  }
  
  // note: bufferIndex is -1 here to best-effort AIOOBE consumers that don't call reset()
  private int offset = 0, bufferIndex = -1, dataLen = 0, finalOffset = 0;
  private static final int MAX_WORD_LEN = 255;
  private static final int IO_BUFFER_SIZE = 4096;
  
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
  
  private final CharacterUtils charUtils;
  private final CharacterBuffer ioBuffer = CharacterUtils.newCharacterBuffer(IO_BUFFER_SIZE);
  
   public boolean isTokenChar(int c) {
   	   return TshegBarUtils.isTshegBarInternal(c) || TshegBarUtils.isTshegBarEdge(c);
   }
   
   public boolean isTokenEdge(int c) {
   	   return TshegBarUtils.isTshegBarEdge(c);
   }

  /**
   * Called on each token character to normalize it before it is added to the
   * token. The default implementation does nothing. Subclasses may use this to,
   * e.g., lowercase tokens.
   */
  protected int normalize(int c) {
    return c;
  }

  @Override
  public final boolean incrementToken() throws IOException {
    clearAttributes();
    int length = 0;
    int start = -1; // this variable is always initialized
    int end = -1;
    char[] buffer = termAtt.buffer();
    while (true) {
      if (bufferIndex >= dataLen) {
        offset += dataLen;
        if(!charUtils.fill(ioBuffer, input)) { // read supplementary char aware with CharacterUtils
          dataLen = 0; // so next offset += dataLen won't decrement offset
          if (length > 0) {
            break;
          } else {
            finalOffset = correctOffset(offset);
            return false;
          }
        }
        dataLen = ioBuffer.getLength();
        bufferIndex = 0;
      }
      // use CharacterUtils here to support < 3.1 UTF-16 code unit behavior if the char based methods are gone
      final int c = charUtils.codePointAt(ioBuffer.getBuffer(), bufferIndex);
      final int charCount = Character.charCount(c);
      bufferIndex += charCount;

      if (isTokenChar(c)) { 
			if (length == 0) {   
			  assert start == -1;
			  start = offset + bufferIndex - charCount;
			  end = start;
			} else if (length >= buffer.length-1) { // check if a supplementary could run out of bounds
			  buffer = termAtt.resizeBuffer(2+length); // make sure a supplementary fits in the buffer
			}
			end += charCount;
			length += Character.toChars(normalize(c), buffer, length); // buffer it, normalized
			if (length >= MAX_WORD_LEN) // buffer overflow! make sure to check for >= surrogate pair could break == test
				break;
			
			if (isTokenEdge(c))
				break;
      } else if (length > 0)             // at non-Letter w/ chars
        	break;        	                 // return 'em
    }

    termAtt.setLength(length);
    assert start != -1;
    offsetAtt.setOffset(correctOffset(start), finalOffset = correctOffset(end));
    return true;
    
  }
  
  @Override
  public final void end() {
    // set final offset
    offsetAtt.setOffset(finalOffset, finalOffset);
  }

  @Override
  public void reset() throws IOException {
    bufferIndex = 0;
    offset = 0;
    dataLen = 0;
    finalOffset = 0;
    ioBuffer.reset(); // make sure to reset the IO buffer!!
  }
}
