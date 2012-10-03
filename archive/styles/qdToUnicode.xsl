<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
       xmlns:thdl="java:org.thdl.tib.text.ttt.EwtsToUnicodeForXslt"
       exclude-result-prefixes="thdl"
	version="2.0">
      
    
        <xsl:template match="/">
                <xsl:apply-templates/>
        </xsl:template>
  
     <!--   <xsl:template match="SOUNDFILE">
                <SOUNDFILE href="{$mediaref}"/>
        </xsl:template> -->
        
        <xsl:template match="TEXT">
            <TEXT>
                <xsl:apply-templates/>
            </TEXT>
        </xsl:template>
        
        <xsl:template match="S">
          <xsl:element name="S">
            <xsl:apply-templates select="@*[not(. = 'id')]"/>
            <xsl:attribute name="id">
                <xsl:value-of select="generate-id(.)"/>
            </xsl:attribute>
            <xsl:apply-templates select="*"/>
          </xsl:element>  
        </xsl:template>

        <xsl:template match="FORM">
                <xsl:variable name="wylie" select="string(.)"/>
                <xsl:variable name="converted" select="thdl:convertEwtsTo($wylie)"/>
                <FORM xml:lang="bo">
                        <xsl:for-each select="tokenize($converted, '[\[\]]')">
                               <xsl:choose>
                                        <xsl:when test="starts-with(., '#ERROR')">࿐</xsl:when>
                                        
                                             <!--   <ERROR number="{substring(., 8, 3)}" offense="{substring-before(substring-after(., '{'), '}')}">
                                                        <MSG><xsl:value-of select="."/></MSG>
                                                        <SRC><xsl:value-of select="$wylie"/></SRC>
                                                </ERROR>
                                        </xsl:when>-->
                                        <xsl:otherwise>
                                                <xsl:value-of select="."/>
                                        </xsl:otherwise>
                                </xsl:choose>
                        </xsl:for-each>
                </FORM>
                <FORM xml:lang="bo-Latn">
                    <xsl:value-of select="$wylie"/>
                </FORM>
        </xsl:template>
        
        <xsl:template match="TRANSL">
            <TRANSL xml:lang="en">
                <xsl:apply-templates/>
            </TRANSL>
        </xsl:template>
        
        <xsl:template match="TRANSL_ZH">
            <TRANSL xml:lang="zh">
                <xsl:apply-templates/>
            </TRANSL>
        </xsl:template>
        
        <xsl:template match="SPEAKER">
                <xsl:variable name="wylie" select="string(.)"/>
                <xsl:variable name="converted" select="thdl:convertEwtsTo($wylie)"/>
                <SPEAKER xml:lang="bo" personId="{@personId}" wylie="{$wylie}">
                        <xsl:for-each select="tokenize($converted, '[\[\]]')">
                                <xsl:choose>
                                        <xsl:when test="starts-with(., '#ERROR')">࿐</xsl:when>
                                        <!--
                                                <ERROR number="{substring(., 8, 3)}" offense="{substring-before(substring-after(., '{'), '}')}">
                                                        <MSG><xsl:value-of select="."/></MSG>
                                                        <SRC><xsl:value-of select="$wylie"/></SRC>
                                                </ERROR>
                                        </xsl:when> -->
                                        <xsl:otherwise>
                                                <xsl:value-of select="."/>
                                        </xsl:otherwise>
                                </xsl:choose>
                        </xsl:for-each>
                </SPEAKER>
        </xsl:template>

        <xsl:template match="node()|@*">
          <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
          </xsl:copy>
        </xsl:template>

      <!--  <xsl:template match="*">
                <xsl:element name="{name(.)}">
                    <xsl:for-each select="@*">
                            <xsl:attribute name="{name(.)}">
                                    <xsl:value-of select="."/>
                            </xsl:attribute>
                    </xsl:for-each>
                    <xsl:apply-templates/>
                </xsl:element>
        </xsl:template> -->

</xsl:stylesheet>
