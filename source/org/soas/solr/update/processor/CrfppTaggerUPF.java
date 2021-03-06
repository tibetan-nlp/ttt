package org.soas.solr.update.processor;

import org.apache.solr.update.processor.UpdateRequestProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class CrfppTaggerUPF extends UpdateRequestProcessorFactory {
    
	protected SolrParams defaults;
	protected SolrParams appends;
	protected SolrParams invariants;

	/**
	 * The UpdateRequestProcessor may be initialized in solrconfig.xml similarly
     * to a RequestHandler, with defaults, appends and invariants.
     * @param args a NamedList with the configuration parameters 
     */

    @SuppressWarnings("rawtypes")
    public void init( NamedList args )
    {
        if (args != null) {
            Object o;
            o = args.get("defaults");
            if (o != null && o instanceof NamedList) {
                defaults = SolrParams.toSolrParams((NamedList) o);
            } else {
                defaults = SolrParams.toSolrParams(args);
            }
            o = args.get("appends");
            if (o != null && o instanceof NamedList) {
                appends = SolrParams.toSolrParams((NamedList) o);
            }
            o = args.get("invariants");
            if (o != null && o instanceof NamedList) {
                invariants = SolrParams.toSolrParams((NamedList) o);
            }
        }
    }

    @Override
    public UpdateRequestProcessor getInstance(SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) {
        // Process defaults, appends and invariants if we got a request
        //if(req != null) {
           // SolrPluginUtils.setDefaults(req, defaults, appends, invariants);
        //}
        SolrParams p = req.getParams();
        p = SolrParams.wrapDefaults(p, defaults);
        p = SolrParams.wrapAppended(p, appends);
        p = SolrParams.wrapDefaults(invariants, p);
        
        return new CrfppTaggerUpdateProcessor(p, next);
    }
  
    static {
        try {
            System.loadLibrary("CRFPP");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Cannot load the example native code.\nMake sure your LD_LIBRARY_PATH contains \'.\'\n" + e);
          
            //probably shouldn't do this, as for example it won't load tomcat, then
            System.exit(1);
        }
    }
    
}
