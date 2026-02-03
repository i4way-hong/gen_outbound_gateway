// ===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:
//
// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.
//
// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.
//
// THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
// ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
// DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
// WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
// NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
// EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
// CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.applicationblocks.com.runtime.CfgObjectActivator;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

import com.genesyslab.platform.commons.GEnum;


/**
 * This class represents a very special query type for configuration server objects read request.
 *
 * <p/><i><b>Disclaimer:</b> It is used for internal undocumented XPath requests support
 * {@link com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestReadObjects2
 * RequestReadObjects2}.<br/>
 * It must be used very carefully taking into account fact that this request is not officially supported
 * by configuration server.<br/><br/>
 *
 * Other important note is that Platform SDK is not able to properly validate such request parameters.<br/>
 * So, if given XPath expression is invalid or is inconsistent with the object type parameter,
 * it may lead to unexpected results like exception in data parsing logic or invalid objects.</i>
 */
public class CfgXPathBasedQuery<TT extends ICfgObject>
        extends CfgQuery<TT> implements ICfgQueryObjectClass<TT> {

    private int objectType;
    private String sXPathExpr;

    /**
     * Creates a new uninitialized instance of query.
     */
    public CfgXPathBasedQuery() {
    }

    /**
     * Creates a new instance of query.
     *
     * @param objType configuration server object type.
     * @param xpath configuration server specific XPath query expression.
     * @see #setObjectType(int)
     * @see #setXPath(java.lang.String)
     */
    public CfgXPathBasedQuery(
            final int objType,
            final String xpath) {
        objectType = objType;
        sXPathExpr = xpath;
    }

    /**
     * Creates a new instance of query.
     *
     * @param service reference to IConfService to be used for query execution.
     * @deprecated Use constructor without configuration service parameter.
     */
    @Deprecated
    public CfgXPathBasedQuery(
            final IConfService service) {
        super(service);
    }

    /**
     * Creates a new instance of query.
     *
     * @param service reference to IConfService to be used for query execution.
     * @param objType configuration server object type.
     * @param xpath configuration server specific XPath query expression.
     * @see #setObjectType(int)
     * @see #setXPath(java.lang.String)
     * @deprecated Use constructor without configuration service parameter.
     */
    @Deprecated
    public CfgXPathBasedQuery(
            final IConfService service,
            final int objType,
            final String xpath) {
        super(service);
        objectType = objType;
        sXPathExpr = xpath;
    }

    
    @SuppressWarnings("unchecked")
    public Class<TT> getQueryObjectClass() {
        CfgObjectType enumValue = (CfgObjectType) GEnum.getValue(CfgObjectType.class, objectType);
        if (enumValue != null) {
            return (Class<TT>) CfgObjectActivator.getType(enumValue.name());
        }
        return null;
    }
    

    /**
     * Sets configuration server object type parameter value for the query.
     *
     * <p/><i><b>Note:</b> This parameter is required for server response proper handling by Platform SDK.
     * And it must be consistent with given XPath expression ({@link #setXPath(java.lang.String)}).<br/>
     * Invalid or inconsistent values of the query parameters may lead to unexpected results
     * like exception in data parsing logic or invalid objects.</i>
     *
     * @param objType {@link CfgObjectType} ordinal value.
     */
    public void setObjectType(final int objType) {
        objectType = objType;
    }

    /**
     * Gets configuration server object type id.
     *
     * @return {@link CfgObjectType} ordinal value.
     */
    public int getObjectType() {
        return objectType;
    }

    /**
     * Gets configuration server object type.
     *
     * @return {@link CfgObjectType} value.
     */
    public CfgObjectType getCfgObjectType() {
        return (CfgObjectType) GEnum.getValue(CfgObjectType.class, objectType);
    }

    /**
     * Sets configuration server XPath query expression.
     *
     * <p/><i><b>Note:</b> This parameter is required.
     * And it must be consistent with given object type parameter ({@link #setObjectType(int)}).<br/>
     * Invalid or inconsistent values of the query parameters may lead to unexpected results
     * like exception in data parsing logic or invalid objects.</i>
     *
     * @param xpath configuration server specific XPath query expression.
     */
    public void setXPath(final String xpath) {
        sXPathExpr = xpath;
    }

    /**
     * Gets configuration server XPath query expression.
     *
     * @return configuration server specific XPath query expression.
     */
    public String getXPath() {
        return sXPathExpr;
    }

    @Override
    public String toString() {
        CfgObjectType type = getCfgObjectType();
        return "XPathQuery[ObjectType: " + objectType
                + (type != null ? ("(" + type.name() + ")") : "")
                + "; XPath: " + sXPathExpr + "]";
    }

    @Override
    public int hashCode() {
        int code = getClass().hashCode();
        code ^= 17*objectType;
        if (sXPathExpr!=null)
            code ^= 19*sXPathExpr.hashCode();
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        final CfgXPathBasedQuery<?> query = (CfgXPathBasedQuery<?>) obj;
        if (objectType != query.objectType) {
            return false;
        }
        if (sXPathExpr == null) {
            return query.sXPathExpr == null;
        }
        return sXPathExpr.equals(query.sXPathExpr);
    }
}
