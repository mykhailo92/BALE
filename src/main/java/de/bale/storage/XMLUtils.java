package de.bale.storage;

import de.bale.Utils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLUtils extends Utils {


    public static void writeXML(Document doc, String filename) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            FileOutputStream fileOutputStream = new FileOutputStream(getSettingsDir() + "/" + filename);
            StreamResult result = new StreamResult(fileOutputStream);
            transformer.transform(source, result);
        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads an XML File to return a changable DOM Document
     *
     * @param filepath Complete Filepath, e.g. (...).class.getResource(ResourceBundle("learningUnitTable.xml")
     * @return w3c DOM Document
     */
    public static Document readXML(String filepath) {
        Document domDoc;
        try {
            domDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getSettingsDir() + "/" + filepath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + getSettingsDir() + "/" + filepath);
            return null;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return domDoc;
    }
}
