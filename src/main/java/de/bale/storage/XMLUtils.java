package de.bale.storage;

import de.bale.Utils;
import de.bale.logger.Logger;
import de.bale.messages.InitMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
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
            Logger.getInstance().post(new InitMessage("Saving XML: " + getSettingsDir() + "/" + filename));
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
        Logger.getInstance().post(new InitMessage("Loading XML: "+filepath));
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

    /**
     * Creates an empty Document
     * @return empty Document which can be used to build an XML
     */
    public static Document createDocument() {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            return documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**

     * @param document Document in which the Tag should be created
     * @param rootElement Element which the new Element should be part of
     * @param name Name of the Tag which will be created
     * @return The new Tag which was created
     */
    public static Element createTag(Document document, Element rootElement, String name) {
        Element newTag = document.createElement(name);
        rootElement.appendChild(newTag);
        return newTag;
    }

    /**
     * Creates a new XML Tag
     * @param document Document in which the Tag should be created
     * @param rootElement Element which the new Element should be part of
     * @param name  Name of the Tag which will be created
     * @param textContent TextContent of the new Element
     * @return The new Tag which was created
     */
    public static Element createTag(Document document, Element rootElement, String name, String textContent) {
        Element newTag = document.createElement(name);
        newTag.setTextContent(textContent);
        rootElement.appendChild(newTag);
        return newTag;
    }
}
