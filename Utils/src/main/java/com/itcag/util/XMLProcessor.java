/*
 *
 * Copyright 2020 IT Consulting AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.itcag.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 * <p>Provides basic methods to manipulate XML.</p>
 */
public final class XMLProcessor {
    
    /**
     * Generates an empty XML document with the root element name specified as argument.
     * @param name Name of the XML root element (optional).
     * @return An empty XML DOM document.
     * @throws UtilException if for any reason document cannot be created.
     */
    public final static Document getDocument(String name) throws UtilException {
        Document retVal = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            document.setXmlVersion("1.0");
            Element rootElement;
            if (name == null || name.isEmpty()) {
                rootElement = document.createElement("document");
            } else {
                rootElement = document.createElement(name);
            }
            document.appendChild(rootElement);
            retVal = document;
        } catch(ParserConfigurationException | DOMException ex) {
            String location = Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
            throw new UtilException("ERROR in " + location + ". For more information see log.");
        }
        return retVal;
    }

    /**
     * Clones an XML document.
     * @param document XML DOM document to be cloned.
     * @return XML DOM clone copy of the original document.
     * @throws UtilException 
     */
    public final static Document cloneDocument(Document document) throws UtilException {
        Document retVal = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document newDocument = documentBuilder.newDocument();
            newDocument.setXmlVersion("1.0");
            Node copiedRoot = newDocument.importNode(document.getDocumentElement(), true);
            newDocument.appendChild(copiedRoot);
            retVal = newDocument;
        } catch(ParserConfigurationException | DOMException ex) {
            String location = Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
            throw new UtilException("ERROR in " + location + ". For more information see log.");
        }
        return retVal;
    }
    
    /**
     * Reads a file containing XML and transforms into a DOM Document object.
     * @param filePath Path to a local file.
     * @return XML DOM document.
     * @throws UtilException if for any reason document cannot be opened. 
     */
    public final static Document getDocumentFromLocalFile(String filePath) throws UtilException {
        Document retVal = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            factory.setFeature("http://xml.org/sax/features/namespaces", false);
            factory.setFeature("http://xml.org/sax/features/validation", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            InputSource inputSource = new InputSource(inputStreamReader);
            inputSource.setEncoding("UTF-8");
            retVal = builder.parse(inputSource);
        } catch(IOException | ParserConfigurationException | SAXException ex) {
            String location = Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
            throw new UtilException("ERROR in " + location + ". For more information see log.");
        }        
        return retVal;
    }

    /**
     * Saves a DOM document as an XML-formatted file.
     * @param document XML DOM document to be saved.
     * @param filePath Path where file is to be saved locally.
     * @throws UtilException if for any reason file cannot be saved. 
     */
    public final static void writeDocumentToFile(Document document, String filePath) throws UtilException {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");            
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch(IllegalArgumentException | TransformerException ex) {
            String location = Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
            throw new UtilException("ERROR in " + location + ". For more information see log.");
        }
    }

    /**
     * Converts a string containing a serialized XML document into DOM object. This method is intentionally designed not to resolve XML entities, and not to download referenced DTD and schema files.
     * @param xmlString String holding serialized XML.
     * @return XML DOM document.
     * @throws UtilException if for any reason string cannot be converted. 
     */
    public final static Document convertStringToDocument(String xmlString) throws UtilException {
        Document retVal = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            /** Ensure that DTD and schema are not downloaded. */
            factory.setSchema(null);
            factory.setExpandEntityReferences(false);
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            factory.setFeature("http://xml.org/sax/features/namespaces", false);
            factory.setFeature("http://xml.org/sax/features/validation", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            /** Parse the string. */
            StringReader stringReader = new StringReader(xmlString);
            InputSource inputSource = new InputSource(stringReader);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            retVal = documentBuilder.parse(inputSource);
        } catch(IOException | ParserConfigurationException | SAXException ex) {
            String location = Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
            throw new UtilException("ERROR in " + location + ". For more information see log.");
        }
        return retVal;
    }

    /**
     * Serializes an XML DOM document into a string.
     * @param document XML DOM document to be serialized.
     * @return string containing serialized XML.
     * @throws UtilException if for any reason document cannot be serialized. 
     */
    public final static String convertDocumentToString(Document document) throws UtilException {
        String resultString = "";
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-16");
            DOMSource source = new DOMSource(document);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Result streamResult = new StreamResult(byteArrayOutputStream);
            // using tramsformer for transforamtion of source to output stream
            transformer.transform(source, streamResult);
            resultString = byteArrayOutputStream.toString("UTF-16");
        } catch(UnsupportedEncodingException | IllegalArgumentException | TransformerException ex) {
            String location = Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
            throw new UtilException("ERROR in " + location + ". For more information see log.");
        }
        return resultString;
    }

}
