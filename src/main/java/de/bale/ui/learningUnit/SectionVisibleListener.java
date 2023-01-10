package de.bale.ui.learningUnit;

import de.bale.logger.Logger;
import de.bale.messages.ErrorMessage;
import de.bale.messages.InformationMessage;
import de.bale.ui.learningUnit.interfaces.ILearningUnitModel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SectionVisibleListener {

    ILearningUnitModel model;
    Element lastChapter;

    public SectionVisibleListener(ILearningUnitModel model) {
        this.model = model;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            lastChapter = documentBuilder.newDocument().createElement("img");
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Notifies the Listener to check if there is a onVisible-Event Attribute in the section
     */
    public void notifyMyself() {
        Element section = model.getContainer()[model.getContainerIndicator()];
        try {
            String[] onVisible = section.getAttribute("onVisible").split(";");
            switch (onVisible[0]) {
                case "disableNextButton" -> {
                    model.setNextButtonDisabled(true);
                    Logger.getInstance().post(new InformationMessage("Disabling Next Button"));
                }
                case "renameCloseButton" -> model.setCloseButtonText(onVisible[1]);
                default -> {
                    Logger.getInstance().post(new ErrorMessage("Unknown Value:" + onVisible[0]));
                }
            }
        } catch (NullPointerException ignored) {
        }
        //Check Chapter
        Element currentChapter = getSectionChapter(section);
        if (!LearningUnitUtils.getID(lastChapter).equals(LearningUnitUtils.getID(currentChapter))) {
            Logger.getInstance().post(new InformationMessage("Displaying Chapter " + model.getChapterIndicator()));
            lastChapter = currentChapter;
            model.setChapterIndicator(model.getChapterIndicator() + 1);
        }
    }

    private Element getSectionChapter(Element element) {
        Node parentNode = element.getParentNode();
        Node highestParentNode = parentNode;
        while (parentNode != null && !LearningUnitUtils.getClasses(parentNode).contains("chapter") && !parentNode.getNodeName().equals("BODY")) {
            if (parentNode.getParentNode() != null) {
                highestParentNode = parentNode.getParentNode();
            }
            parentNode = parentNode.getParentNode();
        }
        if (highestParentNode.getNodeName().equals("BODY")) {
            return lastChapter;
        }
        return (Element) highestParentNode;
    }
}
