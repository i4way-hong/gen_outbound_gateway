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
package com.genesyslab.platform.applicationblocks.com.capacityrules;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.objects.CfgScript;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.xmlfactory.XmlFactories;
import com.genesyslab.platform.configuration.protocol.types.CfgScriptType;

import org.w3c.dom.Document;

/**
 * Helper class to retrieve CapacityRule data from CfgScript object.
 * <br><br>
 * Example of using helper-class:<br>
 * <blockquote><pre>
 * ConfServerProtocol protocol = new ConfServerProtocol(new Endpoint(host, port));
 * protocol.setClientApplicationType(appType);
 * protocol.setClientName("default");
 * protocol.setUserName(userName);
 * protocol.setUserPassword(userPassword);
 * IConfService service = (IConfService)ConfServiceFactory.createConfService(protocol);
 * service.getProtocol().open();
 * CfgScriptQuery query = new CfgScriptQuery(service);
 * CfgScript script = (CfgScript)service.retrieveObject(query);
 * CapacityRuleHelper helper = CapacityRuleHelper.create(script);
 * Document doc = helper.getXMLPresentation();
 * 
 * // edit xml document here
 * 
 * helper.setXMLPresentation(doc);
 * helper.getCfgScript().save();
 * service.getProtocol().close();
 * ConfServiceFactory.releaseConfService(service);
 * </pre></blockquote>
 */
public class CapacityRuleHelper {
    static final String WizardKey = "_WIZARD_";
    static final String MediaRulesKey = "_CRWizardMediaCapacityList_";
    static final String GuiPresentationKey = "_CRWizardGUIPresentation_";
    static final String BinaryPresentationKey = "_CRWizardBinaryPresentation_";

    private Document document;
    private byte[] bytesData;
    private CfgScript script;
    private final Object lockObject = new Object();
    private boolean enabledValidation;
    private ValidationResult validationResult = ValidationResult.Unknown;

    
    private CapacityRuleHelper(CfgScript cfgScript, Document doc, byte[] binaryPresentation) {
		script = cfgScript;
		document = doc;
		bytesData = binaryPresentation;
	}
    
    
    

    /**
     * Gets flag indicates that script does not have any data of capacity rule.
     * @return flag indicates that script does not have any data of capacity rule.
     */
    public boolean isEmpty() {
    	synchronized (lockObject) {
    		return document == null || bytesData == null || bytesData.length == 0;
		}
	}


    /**
     * Gets flag that indicate if enabled or disabled internal validation of capacity rule.
     * <br><b>Note:</b>Enabled the flag reduce performance.
     * @return flag that indicate if enabled or disabled internal validation of capacity rule.
     */
	public boolean isEnabledValidation() {
		return enabledValidation;
	}


	/**
	 * Set to true before assign new XML-presentation to validate results.
	 * @param enabledValidation flag that indicate if enabled or disabled internal validation of capacity rule.
	 */
	public void setEnabledValidation(boolean enabledValidation) {
		this.enabledValidation = enabledValidation;
	}


	/**
     * Gets a result of internal validation of capacity rule. 
     * @return result of internal validation of capacity rule.
     */
    public ValidationResult getValidationResult() {
		return validationResult;
	}

    /**
     * Compares two capacity rules binary presentations for equivalence (ignoring item order)
     * according to their's truth tables.
     *
     * @param capacityRulesBinary1 a binary presentation of 1st capacity rule
     * @param capacityRulesBinary2 a binary presentation of 2nd capacity rule
     * @return true if both capacity rules is equivalent else return false. 
     * @throws CapacityRuleException if occurs some problem in the binaries parsing.
     * @throws IllegalArgumentException if any argument is null.
     */
    public static boolean matchBinaries(byte[] capacityRulesBinary1, byte[] capacityRulesBinary2) 
    	throws CapacityRuleException {
    	
    	if (capacityRulesBinary1 == null) {
    		throw new IllegalArgumentException("capacityRulesBinary1 is null");
    	}
    	if (capacityRulesBinary2 == null) {
    		throw new IllegalArgumentException("capacityRulesBinary2 is null");
    	}

    	CRHeader h1 = new CRHeader(capacityRulesBinary1); 
    	CRHeader h2 = new CRHeader(capacityRulesBinary2);
    	
    	return h1.match(h2);
	}
    
    /**
     * Compares current capacity rule binary presentation with another binaries presentation for equivalence (ignoring item order).
     * according to their's truth tables.
     * @param capacityRulesBinary a binary presentation of other capacity rule
     * @return true if this capacity rule is equivalent to another binary presentation of capacity rule. 
     * @throws CapacityRuleException if occurs some problem in the binary parsing.
     * @throws IllegalArgumentException if the argument is null.
     * @throws IllegalStateException if the current binary presentation is is null.
     */
    public boolean matchBinaries(byte[] capacityRulesBinary) 
    	throws CapacityRuleException {

    	if (bytesData == null) {
    		throw new IllegalStateException("internal binary capacity rule presentation is null");
    	}
    	if (capacityRulesBinary == null) {
    		throw new IllegalArgumentException("capacityRulesBinary is null");
    	}

    	CRHeader h1 = new CRHeader(bytesData); 
    	CRHeader h2 = new CRHeader(capacityRulesBinary);
    	
    	return h1.match(h2);
	}    
    
    /**
     * Compares two capacity rules binary presentations for equality (ignoring item order).
     *
     * @param capacityRulesBinary1 a binary presentation of 1st capacity rule
     * @param capacityRulesBinary2 a binary presentation of 2nd capacity rule
     * @return true if both capacity rules is equals else return false. 
     * @throws CapacityRuleException if occurs some problem in the binaries parsing.
     * @throws IllegalArgumentException if any argument is null.
     */
    public static boolean equalsBinaries(byte[] capacityRulesBinary1, byte[] capacityRulesBinary2) 
    	throws CapacityRuleException {
    	
    	if (capacityRulesBinary1 == null) {
    		throw new IllegalArgumentException("capacityRulesBinary1 is null");
    	}
    	if (capacityRulesBinary2 == null) {
    		throw new IllegalArgumentException("capacityRulesBinary2 is null");
    	}

    	CRHeader h1 = new CRHeader(capacityRulesBinary1); 
    	CRHeader h2 = new CRHeader(capacityRulesBinary2);
    	
    	return h1.equals(h2);
	}
    
    /**
     * Compares current capacity rule binary presentation with another binaries presentation for equality (ignoring item order).
     * @param capacityRulesBinary a binary presentation of other capacity rule
     * @return true if this capacity rule is equals to another binary presentation of capacity rule. 
     * @throws CapacityRuleException if occurs some problem in the binary parsing.
     * @throws IllegalArgumentException if the argument is null.
     * @throws IllegalStateException if the current binary presentation is is null.
     */
    public boolean equalsBinaries(byte[] capacityRulesBinary) 
    	throws CapacityRuleException {

    	if (bytesData == null) {
    		throw new IllegalStateException("internal binary capacity rule presentation is null");
    	}
    	if (capacityRulesBinary == null) {
    		throw new IllegalArgumentException("capacityRulesBinary is null");
    	}

    	CRHeader h1 = new CRHeader(bytesData); 
    	CRHeader h2 = new CRHeader(capacityRulesBinary);
    	
    	return h1.equals(h2);
	}

	/**
     * Gets optimized capacity rule binary representation according to passed xml presentation of capacity rule. 
     * @param doc document presentation of capacity rule.
     * @return binary binary representation of optimized capacity rule. 
     * @throws CapacityRuleException when occurs some problem in XML parsing.
     */
    public static byte[] getBinaryFromXML(Document doc) throws CapacityRuleException {
    	CapacityRule cr = CapacityRule.createFromXML(doc);
    	return cr.getBinaryData();
    }

    /**
     * Gets optimized capacity rule binary representation according to passed xml presentation of capacity rule. 
     * @param xml XML presentation of capacity rule.
     * @return binary binary representation of optimized capacity rule. 
     * @throws CapacityRuleException when occurs some problem in XML parsing.
     */
    public static byte[] getBinaryFromXML(String xml) throws CapacityRuleException {
    	Document doc;
		try {
			doc = XmlFactories.newDocumentBuilderNS().parse( new ByteArrayInputStream(xml.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new CapacityRuleException("Problem parsing xml", e);
		}
		return getBinaryFromXML(doc);
    }


	/**
     * Creates instance of CapacityRuleHelper class. Verifies script object and retrieves data.
     * @param script instance of CfgScript object.
     * @return instance of this helper class.
	 * @throws ConfigException if script has invalid format.
     * @throws IllegalArgumentException if script is null or script type isn't CFGCapacityRule.
     */
    public static CapacityRuleHelper create(CfgScript script) throws ConfigException
    {
    	if (script == null) {
    		throw new IllegalArgumentException("null argument");
    	}
    	CfgScriptType type = script.getType();
		if (type != CfgScriptType.CFGCapacityRule) {
    		throw new IllegalArgumentException("invalid script type: " + (type == null ? "null" : type.name()));
    	}

    	Document doc = null;
      	byte[] binaryPresentation = null;
    	
      	KeyValueCollection userProperties = script.getUserProperties();
		if (userProperties != null) {
	      	KeyValueCollection wizardData = userProperties.getList(WizardKey);
	      	if (wizardData != null) {
		      	binaryPresentation = wizardData.getBinary(BinaryPresentationKey);
		      	
		      	byte[] guiPresentation = wizardData.getBinary(GuiPresentationKey);
		      	if (guiPresentation != null) {
			      	String xmlData;
					try {
						xmlData = new String(guiPresentation, 0, guiPresentation.length-1, "UTF-8"); // ignore trailing zero
					} catch (UnsupportedEncodingException e) {
						throw new ConfigException("CfgScript conatins " + GuiPresentationKey + " not in UTF-8 format", e);
					}
			    	DocumentBuilder db;
					try {
						db = XmlFactories.newDocumentBuilderNS();
					} catch (ParserConfigurationException e) {
						throw new RuntimeException(e);
					}

					try {
						doc = db.parse( new ByteArrayInputStream(xmlData.getBytes("UTF-8")));
					} catch (Exception e) {
						throw new ConfigException("CfgScript contains broken " + GuiPresentationKey, e);
					} 
		      	}
	      	}
		}
      	
      	return new CapacityRuleHelper(script, doc, binaryPresentation);
    }
    

	/**
	* Gets XML presentation of CapacityRule in CfgScript object.
	* @return document presentation of CapacityRule.
	*/
	public Document getXMLPresentation() {
		synchronized (lockObject) {
			return document == null ? null  : (Document) document.cloneNode(true);
		}
	}

	/**
	 * Sets XML presentation of CapacityRule in CfgScript object.
	 * During setting new XML the new binary data will be created.
	 * @param doc Document presentation of CapacityRule. Set null value to delete the rule data.
	 * @throws CapacityRuleException See exception message to identify of problem.
	 */
	public void setXMLPresentation(Document doc) throws CapacityRuleException {
		synchronized (lockObject) {
			if (doc == null) {
				bytesData = null;
				document = null;
				validationResult = ValidationResult.Unknown;
				
				KeyValueCollection userProperties = script.getUserProperties();
				if (userProperties != null) {
					userProperties.remove(MediaRulesKey);
					KeyValueCollection wizardData = userProperties.getList(WizardKey);
					if (wizardData != null) {
						wizardData.remove(GuiPresentationKey);
						wizardData.remove(BinaryPresentationKey);
					}
				}
			}
			else {
				CapacityRule rule = CapacityRule.createFromXML(doc);
				validationResult = enabledValidation ? 
						rule.validate() ? ValidationResult.Success : ValidationResult.Failed 
						: ValidationResult.Unknown;
				bytesData = rule.getBinaryData();
				document = doc;

				
				KeyValueCollection userProperties = script.getUserProperties();
				if (userProperties == null) {
					userProperties = new KeyValueCollection();
					script.setUserProperties(userProperties);
				}
				KeyValueCollection wizardData = userProperties.getList(WizardKey);
				if (wizardData == null) {
					wizardData = new KeyValueCollection();
					userProperties.remove(WizardKey);
					userProperties.addList(WizardKey, wizardData);
				}
				wizardData.remove(GuiPresentationKey);
				wizardData.remove(BinaryPresentationKey);
				
				if (!isEmpty()) {
					String xml = getXMLStringData() + "\0";
					try {
						wizardData.addBinary(GuiPresentationKey, xml.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
					
					if (bytesData != null)
						wizardData.addBinary(BinaryPresentationKey, bytesData);

					HashMap<String, Integer> map = rule.getSimpleMediaMap();
					if (map != null) {
						KeyValueCollection mapValues = new KeyValueCollection();
						for( Map.Entry<String,Integer> entry : map.entrySet() ) {
							mapValues.addInt(entry.getKey(), entry.getValue());
						}
						userProperties.remove(MediaRulesKey);
						userProperties.addList(MediaRulesKey, mapValues);
					}
				}
			}
        }
    }

	/**
	 * Gets XML data as string.
	 * @return XML data as string
	 */
	public String getXMLStringData() {
		
		try {
			if (document == null) {
				return null;
			}
			
			StringWriter sw = new StringWriter();
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        synchronized(lockObject) {
				if (document == null) {
					return null;
				}
		        transformer.transform(new DOMSource(document), new StreamResult(sw));
			}
	        
	        return sw.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets binary presentation of CapacityRule in CfgScript object.
	 * @return binary presentation of CapacityRule in CfgScript object.
	 */
	public byte[] getBinaryPresentation() {
        synchronized (lockObject)
        {
          return bytesData;
        }
    }

	/**
	 * Gets COM AB CfgScript object instance. 
	 * @return COM AB CfgScript object instance.
	 */
	public CfgScript getCfgScript() {
		return script;
	}

}
