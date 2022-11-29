package de.bale;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class XMLUtils {

    public static Document readXML(String filepath) {
        Document domDoc;
        try {
            domDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filepath);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return domDoc;
    }
}
