/*
 * Licensed to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.thdl.tib.uima;

import org.thdl.tib.scanner.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.net.URL;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class DictionaryScannerTokenizer extends CasAnnotator_ImplBase {
	private TibetanScanner scanner;
	
	public static final String wyliePunc = "/;|!:^@#$%=,<>(){}[]";

   public static final String TOKEN_ANNOTATION_NAME = "org.apache.uima.TokenAnnotation";
   public static final String SENTENCE_ANNOTATION_NAME = "org.apache.uima.SentenceAnnotation";
   public static final String TOKEN_TYPE_FEATURE_NAME = "tokenType";
   private Type tokenType;
   private CAS cas = null;
   private Logger logger;
   private String[] sofaNames;
   public static final String MESSAGE_BUNDLE = "org.thdl.tib.uima.DictionaryScannerTokenizerMessages";

   /* (non-Javadoc)
    * @see org.apache.uima.analysis_component.CasAnnotator_ImplBase#process(org.apache.uima.cas.CAS)
    */
    public void process(CAS aCas) throws AnalysisEngineProcessException {

      this.logger.logrb(Level.INFO, "DictionaryScannerTokenizer", "process",
            MESSAGE_BUNDLE, "dictionary_scanner_tokenizer_info_start_processing");

      try {
		  URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
		  File file = new File(location.getPath());
		  String prefix = file.getParent();
		  prefix = (prefix == null) ? "": prefix + File.separator;
		  scanner = new LocalTibetanScanner(prefix + "thl-dicts/thl");
		  scanner.getDictionaryDescriptions();
		  
		  ArrayList<CAS> casList = new ArrayList<CAS>();
		  // check if sofa names are available
		  if (this.sofaNames != null && this.sofaNames.length > 0) {
	
			 // get sofa names
			 for (int i = 0; i < this.sofaNames.length; i++) {
				Iterator it = aCas.getViewIterator(this.sofaNames[i]);
				while (it.hasNext()) {
				   // add sofas to the cas List to process
				   casList.add((CAS) it.next());
				}
			 }
		  } else {
			 // use default sofa for the processing
			 casList.add(aCas);
		  }
	
		  for (int x = 0; x < casList.size(); x++) {
			 this.cas = casList.get(x);
			 scanner.scanBody(this.cas.getDocumentText());
			 Token[] tokens = scanner.getTokenArray();
			 if (tokens.length > 0) {
			 	 int i=0;
				 for (; i<tokens.length-1; i++) {
				 	 String tokStr = tokens[i].getRawValue();
				 	 if (wyliePunc.indexOf(tokStr.charAt(tokStr.length()-1)) != -1) {
				 	 	 createAnnotation(this.tokenType, tokens[i].getOffset(), tokens[i].getOffset() + tokStr.length());
				 	 }
				 	 else 
				 	 {
				 	 	 createAnnotation(this.tokenType, tokens[i].getOffset(), tokens[i+1].getOffset());
					 }
				 }
				 createAnnotation(this.tokenType, tokens[i].getOffset(), tokens[i].getOffset() + tokens[i].getRawValue().length());
			 }
			 
			 scanner.clearTokens();
			 scanner.finishUp();
			 tokens = scanner.getTokenArray();
			 for (int i=0; i<tokens.length; i++) {
				int start = tokens[i].getOffset();
				int length = tokens[i].getRawValue().length();
				createAnnotation(this.tokenType, start, start + length);
			 }
			 scanner.clearTokens();
		  }
      } 
      catch (Exception e) {
      	e.printStackTrace();
      }
      this.logger.logrb(Level.INFO, "DictionaryScannerTokenizer", "process",
            MESSAGE_BUNDLE, "dictionary_scanner_tokenizer_info_stop_processing");
   }

   /**
    * create an annotation of the given type in the CAS using startPos and
    * endPos.
    * 
    * @param annotationType
    *           annotation type
    * @param startPos
    *           annotation start position
    * @param endPos
    *           annotation end position
    */
   private void createAnnotation(Type annotationType, int startPos, int endPos) {

      AnnotationFS annot = this.cas.createAnnotation(annotationType, startPos,
            endPos);
      this.cas.addFsToIndexes(annot);
   }

   @Override
   public void typeSystemInit(TypeSystem typeSystem)
         throws AnalysisEngineProcessException {

      super.typeSystemInit(typeSystem);
      // initialize cas token type
      this.tokenType = typeSystem.getType(TOKEN_ANNOTATION_NAME);

      this.logger.logrb(Level.INFO, "DictionaryScannerTokenizer", "typeSystemInit",
            MESSAGE_BUNDLE, "dictionary_scanner_tokenizer_info_typesystem_initialized");

   }

   @Override
   public void initialize(UimaContext context)
         throws ResourceInitializationException {
      super.initialize(context);

      this.sofaNames = (String[]) getContext().getConfigParameterValue(
            "SofaNames");

      this.logger = context.getLogger();

      this.logger.logrb(Level.INFO, "DictionaryScannerTokenizer", "initialize",
            MESSAGE_BUNDLE, "dictionary_scanner_tokenizer_info_initialized");

   }
}
