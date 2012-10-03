<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:encoder="java:java.net.URLEncoder" 
    xmlns:thdl="java:org.thdl.tib.text.ttt.EwtsToUnicodeForXslt"
    exclude-result-prefixes="thdl encoder" >
    
    
    <xsl:import href="qdToUnicode.xsl"/>
    
	<xsl:output method="xml" encoding="UTF-8" indent="yes" name="unicode.transcript.with.metadata"/>
    
    <xsl:param name="transcript.location" select="''"/>
    <xsl:param name="transform.to.dir" select="''"/>
	
	<xsl:template match="/">
        <xsl:for-each select="//transcript">
            <xsl:variable name="filename" select="."/>
            <xsl:result-document href="{../@id}.xml" format="unicode.transcript.with.metadata">
                <xsl:element name="TITLE">
                    <xsl:attribute name="id">
                        <xsl:value-of select="../@id"/>
                    </xsl:attribute>
                    <METADATA>
                        <xsl:apply-templates select="../*"/>
                    </METADATA>
                    <xsl:variable name="transcript.content" select="document(concat($transcript.location,$filename))/TEXT"/>
                    <TEXT>
                        <xsl:apply-templates select="$transcript.content/S"/>.
                    </TEXT>
                </xsl:element>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>
        
</xsl:stylesheet>
