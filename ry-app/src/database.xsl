<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/">
        <center>
            <h2>
                <xsl:value-of select="database/caption"/>
                <h3>
                    <xsl:element name="a">
						<xsl:attribute name="href"><xsl:value-of select="database/home"/></xsl:attribute>
                    </xsl:element>
                    返回上一层
                </h3>
            </h2>
        </center>
        <xsl:for-each select="database/include">
            <h4>
                <xsl:element name="a">
                    <xsl:attribute name="href"><xsl:value-of select="@filename"/></xsl:attribute>
                </xsl:element>
                <xsl:value-of select="name"/>
				(<xsl:value-of select="@filename"/>)
				</h4>
        </xsl:for-each>
        <table border="1" width="100%" bordercolorlight="#000000" bordercolordark="#FFFFFF" bordercolor="#000000" cellspacing="0" cellpadding="3">
            <tr bordercolorlight="#000000" bordercolordark="#FFFFFF" bgcolor="#66FF99">
                <td id="head" align="center"><b>数据表</b></td>
                <td align="center"><b>Database</b></td>
                <td align="center"><b>描述</b></td>
                <td align="center"><b>备注</b></td>
                <td align="center"><b>创建\修改</b></td>
                <td align="center"><b>最后检测日期</b></td>
                <td align="center"><b>需移除否</b></td>
            </tr>
            <xsl:for-each select="database/tables/table">
                <xsl:sort select="@delete" order="ascending" data-type="text"/>
                <xsl:sort select="@code" order="ascending" data-type="text"/>
                <tr>
                    <td align="left">
					<xsl:element name="a">
					<xsl:attribute name="href">#<xsl:value-of select="@code"/></xsl:attribute>
					</xsl:element>
					<xsl:value-of select="@code"/><br/></td>
                    <td><xsl:value-of select="name"/><br/></td>
                    <td><xsl:value-of select="@database"/><br/></td>
                    <td><xsl:value-of select="remark"/><br/></td>
                    <td><xsl:value-of select="@author"/><br/></td>
                    <td><xsl:value-of select="@qcdate"/><br/></td>
                    <td><xsl:value-of select="@delete"/><br/></td>
                </tr>
            </xsl:for-each>
        </table>
        <xsl:for-each select="database/tables/table">
            <h2>
                <xsl:element name="a">
                    <xsl:attribute name="name"><xsl:value-of select="@code"/></xsl:attribute>
                </xsl:element>
                <xsl:value-of select="name"/>
				(<xsl:value-of select="@code"/>)，按此<a href="#head">返回列表</a></h2>
					数据结构：
					<table border="1" width="100%" bordercolorlight="#000000" bordercolordark="#FFFFFF" bordercolor="#000000" cellspacing="0" cellpadding="3">
						<tr bordercolorlight="#000000" bordercolordark="#FFFFFF" bgcolor="#66FF99">
						<b>
							<th>字段代码</th>
							<th>字段名称</th>
							<th>数据类型</th>
							<th>显示宽度</th>
							<th>允许NULL</th>
							<th>默认值</th>
							<th>等级</th>
							<th>备注</th>
							<th>移除否</th></b>
						</tr>
						<xsl:for-each select="fields/field">
						<xsl:sort select="@delete" order="ascending" data-type="text"/>
						<tr>
							<th><xsl:value-of select="@code"/><br/></th>
							<th><xsl:value-of select="name"/><br/></th>
							<th><xsl:value-of select="@type"/><br/></th>
							<th><xsl:value-of select="@width"/><br/></th>
							<th><xsl:value-of select="@null"/><br/></th>
							<th><xsl:value-of select="@default"/><br/></th>
							<th><xsl:value-of select="level"/><br/></th>
							<th><xsl:value-of select="remark"/><br/></th>
							<th><xsl:value-of select="@delete"/><br/></th>
						</tr>
						</xsl:for-each>
					</table>
					<h3>索引列表：</h3>
					<table border="1" width="100%" bordercolorlight="#000000" bordercolordark="#FFFFFF" bordercolor="#000000" cellspacing="0" cellpadding="3">
						<tr bordercolorlight="#000000" bordercolordark="#FFFFFF" bgcolor="#66FF99">
							<th>索引</th>
							<th>类别</th>
							<th>簇索引</th>
							<th>索引字段</th>
							<th>排序方式</th>
							<th>违规提示</th>
						</tr>
						<xsl:for-each select="indexs/index">
						<xsl:for-each select="field">
							<xsl:variable name="count" select="last()"/>
							<tr>
								<xsl:if test="position()=1">
									<th rowspan="{$count}" align="center" valign="middle">
										<span><xsl:value-of select="../@code"/></span>
									</th>
									<th rowspan="{$count}" align="center" valign="middle">
										<span><xsl:value-of select="../@type"/></span>
									</th>
									<th rowspan="{$count}" align="center" valign="middle">
										<span><xsl:value-of select="../@clustered"/><br/></span>
									</th>
								</xsl:if>
								<th><xsl:value-of select="@code"/></th>
								<th><xsl:value-of select="@sort"/></th>
								<xsl:if test="position()=1">
									<th rowspan="{$count}" align="center" valign="middle">
										<span><xsl:value-of select="../error"/><br/></span>
									</th>
								</xsl:if>
							</tr>
							</xsl:for-each>
						</xsl:for-each>
					</table>
			</xsl:for-each>
        <h4>存储过程列表：</h4>
        <table border="1" width="100%" bordercolorlight="#000000" bordercolordark="#FFFFFF" bordercolor="#000000" cellspacing="0" cellpadding="3">
            <tr bordercolorlight="#000000" bordercolordark="#FFFFFF" bgcolor="#66FF99">
                <td>过程名</td>
                <td>描述</td>
                <td>文件位置</td>
                <td>检测时间</td>
                <td>删除否</td>
            </tr>
            <xsl:for-each select="database/script/procedure">
                <xsl:sort select="@delete" order="ascending" data-type="text"/>
                <xsl:sort select="@code" order="ascending" data-type="text"/>
                <tr>
                    <td><xsl:value-of select="@code"/></td>
                    <td><xsl:value-of select="name"/></td>
                    <td><xsl:value-of select="@filename"/></td>
                    <td><xsl:value-of select="@qcdate"/></td>
                    <td><xsl:value-of select="@delete"/></td>
                </tr>
            </xsl:for-each>
        </table>
        <h4>存储函数列表：</h4>
        <table border="1" width="100%" bordercolorlight="#000000" bordercolordark="#FFFFFF" bordercolor="#000000" cellspacing="0" cellpadding="3">
            <tr bordercolorlight="#000000" bordercolordark="#FFFFFF" bgcolor="#66FF99">
                <td>函数名</td>
                <td>描述</td>
                <td>文件位置</td>
                <td>检测时间</td>
                <td>删除否</td>
            </tr>
            <xsl:for-each select="database/script/function">
                <xsl:sort select="@delete" order="ascending" data-type="text"/>
                <xsl:sort select="@code" order="ascending" data-type="text"/>
                <tr>
                    <td><xsl:value-of select="@code"/></td>
                    <td><xsl:value-of select="name"/></td>
                    <td><xsl:value-of select="@filename"/></td>
                    <td><xsl:value-of select="@qcdate"/></td>
                    <td><xsl:value-of select="@delete"/></td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>
