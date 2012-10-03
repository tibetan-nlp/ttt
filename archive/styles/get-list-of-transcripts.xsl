<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:java="java" >
	
    
	<xsl:output method="text"/>
	
	<xsl:template match="/">
		<xsl:apply-templates select="//transcript"/>
	</xsl:template>
	
	<xsl:template match="transcript">
        <xsl:value-of select="java:net.URLEncoder.encode(.,'UTF-8')"/><xsl:text> </xsl:text>
	</xsl:template>
</xsl:stylesheet>
