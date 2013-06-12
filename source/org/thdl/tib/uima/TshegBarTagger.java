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

import org.thdl.tib.solr.util.TshegBarUtils;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.ResourceInitializationException;

public class TshegBarTagger extends CasAnnotator_ImplBase {
   public static final String TOKEN_ANNOTATION_NAME = "org.apache.uima.TokenAnnotation";
   public static final String TOKEN_TYPE_FEATURE_NAME = "tokenType";
   private Type tokenType;
   private CAS cas = null;
   private String[] sofaNames;
   public static final String MESSAGE_BUNDLE = "org.thdl.tib.uima.TshegBarTaggerMessages";

   /* (non-Javadoc)
    * @see org.apache.uima.analysis_component.CasAnnotator_ImplBase#process(org.apache.uima.cas.CAS)
    */
    public void process(CAS aCas) throws AnalysisEngineProcessException {

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
			 char[] c = this.cas.getDocumentText().toCharArray();
			 int start = 0;

			 for (int i=0; i<c.length-1; i++) {
			 	 if (TshegBarUtils.isTshegBarEdge(c[i]) || i == c.length) {
			 	 	 createAnnotation(this.tokenType, start, i+1);
			 	 	 start = i+1;
			 	 }
			 }
		  }
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
   }

   @Override
   public void initialize(UimaContext context)
         throws ResourceInitializationException {
      super.initialize(context);

      this.sofaNames = (String[]) getContext().getConfigParameterValue(
            "SofaNames");

   }
}
