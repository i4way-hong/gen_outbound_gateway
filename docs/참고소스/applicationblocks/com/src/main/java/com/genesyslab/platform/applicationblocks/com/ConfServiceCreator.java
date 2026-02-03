//===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:

// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.

// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.

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

// Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.commons.protocol.Protocol;


/**
 * This interface is created to allow user to create and
 * integrate custom ConfService.
 * Usage sample steps are following:
 * <ul>
 *   <li>1. Create custom service class<code><pre>
 *       public class CustomConfService extends ConfService {
 *           public CustomConfService(final Protocol protocol) {
 *               super(protocol);
 *           }
 *           // do override some methods here...
 *       }
 *     </pre></code></li>
 *   <li>2. Initialize ConfServiceFactory with new service creator<code><pre>
 *       ConfServiceFactory.setConfServiceCreator(
 *           new ConfServiceCreator() {
 *               public ConfService createInstance(
 *                       final Protocol protocol) {
 *                   return new CustomConfService(protocol);
 *               }
 *           }
 *       );
 *     </pre></code></li>
 * </ul>
 * After these operations ConfServiceFactory will create
 * all configuration services as customized by CustomConfService.
 *
 * @see ConfServiceFactory#setConfServiceCreator(ConfServiceCreator)
 */
public interface ConfServiceCreator {

    ConfService createInstance(Protocol protocol);
}
