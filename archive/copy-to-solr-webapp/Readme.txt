The files in this directory must be copied to your solr webapp
before you can use lucene-thdl-build.xml to post, commit, or 
delete documents from your solr server.

First, run the task solr-prepare-for-copy-to-solr-webapp.

Then, copy files to SOLR as follows:

*.xml: 
    Copy to your solr/conf directory, replacing any
    existing files..
    
*.xsl
    Copy to the solr/conf/xslt directory.

*.jar
    Copy to your solr/lib directory. If solr/lib does
    not exist, then create it.


