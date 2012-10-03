<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    
    <xsl:import href="solarizeConstantsForImport.xsl"/>
    
    <xsl:template match="/">
        <xsl:apply-templates select="TITLE"/>
    </xsl:template>
    
    <xsl:template match="TITLE">
        <add>
            <xsl:variable name="title.id" select="@id"/>
            <xsl:apply-templates select="METADATA">
                <xsl:with-param name="title.id" select="$title.id"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="TEXT/S">
                <xsl:with-param name="title.id" select="$title.id"/>
                <xsl:with-param name="belongs.to" select="METADATA/belongsTo"/>
            </xsl:apply-templates>
        </add>
    </xsl:template>
    
    <!-- should we also include transcript and video ids? -->
    <xsl:template match="METADATA">
        <xsl:param name="title.id" select="''"/>
        <doc>
            <field name="id"><xsl:value-of select="$title.id"/></field> <!-- has 't' prefix -->
            <field name="numericRef"><xsl:value-of select="substring($title.id,2)"/></field> <!-- strips prefix for searching -->
            <field name="thdlType_s"><xsl:value-of select="$TITLE_TYPE"/></field>
            <field name="belongsTo_idlist"><xsl:value-of select="concat($title.id,' ',belongsTo)"/></field>
            <field name="speechType_s"><xsl:value-of select="speechType"/></field>
            <field name="language_lang"><xsl:value-of select="language"/></field>
            <field name="administrativeLocation_s"><xsl:value-of select="administrativeLocation"/></field>
            <field name="culturalRegion_s"><xsl:value-of select="culturalRegion"/></field>
            <field name="title_en"><xsl:value-of select="name"/></field>
            <field name="caption_en"><xsl:value-of select="caption"/></field>
            <field name="transcript_filename"><xsl:value-of select="transcript"/></field>
            <xsl:variable name="video.ids">
                <xsl:call-template name="getVideoList">
                    <xsl:with-param name="metadata" select="."/>
                </xsl:call-template>
            </xsl:variable>
            <xsl:if test="normalize-space($video.ids)">
                <field name="videos_idlist"><xsl:value-of select="normalize-space($video.ids)"/></field>
            </xsl:if>
        </doc>
        <xsl:for-each select="video">
            <doc>
                <field name="id"><xsl:value-of select="@id"/></field> <!-- has 'v' prefix -->
                <field name="numericRef"><xsl:value-of select="substring(@id,2)"/></field> <!-- strips prefix for searching -->
                <field name="title_idref"><xsl:value-of select="$title.id"/></field>
                <field name="thdlType_s"><xsl:value-of select="$VIDEO_TYPE"/></field>
                <field name="mediaType_s"><xsl:value-of select="mediaDescription"/></field>
                <field name="connSpeed_s"><xsl:value-of select="connectionSpeed"/></field>
                <field name="quality_s"><xsl:value-of select="quality"/></field>
                <field name="size_i"><xsl:value-of select="size"/></field>
                <field name="duration_dt"><xsl:value-of select="concat($DURATION_PREFIX,duration,$DURATION_SUFFIX)"/></field>
                <field name="media_filename"><xsl:value-of select="name"/></field>
            </doc>
        </xsl:for-each>
    </xsl:template>
         
    <xsl:template name="getVideoList">
        <xsl:param name="metadata" select="''"/>
        <xsl:for-each select="$metadata/video">
            <xsl:value-of select="@id"/><xsl:text> </xsl:text>
        </xsl:for-each>
    </xsl:template>
    
<!-- Here's what a chunk of metadata looks like:

      <speechType>Conversation</speechType>
      <language>Tibetan</language>
      <culturalRegion>dbus</culturalRegion>
      <name>A Lucky Dream: &lt;i&gt;Three's Company #01&lt;/i&gt;</name>
      <caption>CAPTION HEAD</caption>
      <transcript id="2502">00008_01-a-lucky-dream_09.xml</transcript>
      <video id="6613">			
         <mediaDescription>Audio</mediaDescription>			
         <connectionSpeed>medium</connectionSpeed>			
         <size>3225818</size>			
         <duration>00:02:14</duration>			
         <name>00008_lucky-dream.mp3</name>		
      </video>
      <video id="6612">			
         <mediaDescription>Video</mediaDescription>			
         <connectionSpeed>medium</connectionSpeed>			
         <size>5009798</size>			
         <duration>00:11:11</duration>			
         <name>00008_lucky-dream.mp4</name>		
      </video>
      <video id="6611">			
         <mediaDescription>Video</mediaDescription>			
         <connectionSpeed>fast</connectionSpeed>			
         <size>13556712</size>			
         <duration>00:11:11</duration>			
         <name>00008_lucky-dream.mp4</name>		
      </video>
-->    

    <xsl:template match="S">
        <xsl:param name="title.id" select="''"/>
        <xsl:param name="belongs.to" select="''"/>
        <xsl:variable name="s.id" select="concat($title.id, '_', @id)"/>
        <doc>
            <field name="id"><xsl:value-of select="$s.id"/></field> <!-- has 't' prefix -->
            <field name="numericRef"><xsl:value-of select="substring($s.id,2)"/></field> <!-- strips prefix for searching -->
            <!-- <field name="title_idref"><xsl:value-of select="$title.id"/></field> -->
            <field name="thdlType_s"><xsl:value-of select="$TRANSCRIPT_FRAGMENT_TYPE"/></field>
            <field name="belongsTo_idlist"><xsl:value-of select="concat($title.id,' ',$belongs.to)"/></field>
            <field name="form_bo"><xsl:value-of select="FORM[@xml:lang='bo']"/></field>
            <field name="form_bo-Latn"><xsl:value-of select="FORM[@xml:lang='bo-Latn']"/></field>
            <xsl:if test="TRANSL[@xml:lang='en']">
                <field name="transl_en"><xsl:value-of select="TRANSL[@xml:lang='en']"/></field>
            </xsl:if>
            <xsl:if test="TRANSL[@xml:lang='zh']">
                <field name="transl_zh"><xsl:value-of select="TRANSL[@xml:lang='zh']"/></field>
            </xsl:if>
            <xsl:if test="string(AUDIO/@start)">
                <field name="start_f"><xsl:value-of select="AUDIO/@start"/></field>
                <xsl:if test="string(AUDIO/@end)">
                    <field name="end_f"><xsl:value-of select="AUDIO/@end"/></field>
                    <field name="duration_f"><xsl:value-of select="AUDIO/@end - AUDIO/@start"/></field>
                </xsl:if>
            </xsl:if>
        </doc>
    </xsl:template>
    
<!-- Here's what a chunk of transcript looks like:

      <S who="N400005" id="d148e29">
         <FORM xml:lang="bo">དེ་རིང་གནམ་ཡག་པོ་ར་ཅིག་༿འདྲ་ཅིག༾མི་འདུག་གས།</FORM>
         <FORM xml:lang="bo-Latn">de ring gnam yag po ra cig {'dra cig}mi 'dug gas/</FORM>
         <TRANSL xml:lang="en">Isn't it a nice day today?</TRANSL>
         <TRANSL xml:lang="zh">今天的天气多好啊, 是吧!</TRANSL>
         <AUDIO end="8.925999997392298" start="7.63"/>
      </S>
-->
    
</xsl:stylesheet>
