/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.xml;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author ozolotarev
 *
 */
public class Form
{
    public Form(String xmlSource) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputSource is = new InputSource(new StringReader(xmlSource));

        Document document = builder.parse(is);
        // Access elements by tag name

        NodeList nodeList = document.getElementsByTagName("attributes");
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node node = nodeList.item(i);
            System.out.println("Element Content: " + node.getTextContent());
        }

    }

}
