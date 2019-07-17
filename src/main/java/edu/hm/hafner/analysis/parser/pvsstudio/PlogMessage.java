package edu.hm.hafner.analysis.parser.pvsstudio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;

/**
 * A parser for PVS-Studio Plog/XML files.
 *
 * @author PVS-Studio Team
 */
public class PlogMessage {
    private String file = "";
    private int lineNumber = 0;
    private String errorCode = "";
    private String message = "";
    private String level = "";

    public String getHash() {
        return errorCode + message + file + lineNumber;
    }

    @Override
    public String toString() {
        return message;
    }

    public String getFilePath() {
        return file;
    }

    public int getLine() {
        return lineNumber;
    }

    public String getType() {
        return errorCode;
    }

    public String getLevel() {
        return level;
    }

    @SuppressWarnings("PMD")
    public static List<PlogMessage> getMessagesFromReport(final File report) {
        List<PlogMessage> plogMessages = new ArrayList<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        long failWarningsCount = 0;
        long falseAlarmCount = 0;
        
        try {

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document plogDoc = dBuilder.parse(report);
            plogDoc.getDocumentElement().normalize();

            NodeList nList = plogDoc.getElementsByTagName("PVS-Studio_Analysis_Log");

            for (int nodeCount = 0; nodeCount < nList.getLength(); nodeCount++) {
                Node nNode = nList.item(nodeCount);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    PlogMessage msg = new PlogMessage();

                    NodeList nodeFalseAlarm = eElement.getElementsByTagName("FalseAlarm");
                    if (nodeFalseAlarm != null && nodeFalseAlarm.item(0) != null && nodeFalseAlarm.item(0).getTextContent().equalsIgnoreCase("true")) {
                        ++falseAlarmCount;
                        continue;
                    }

                    msg.file = "";
                    NodeList nodeFile = eElement.getElementsByTagName("File");

                    if (nodeFile != null && nodeFile.item(0) != null && nodeFile.item(0).getTextContent() != null) {
                        msg.file = nodeFile.item(0).getTextContent().trim();
                    }

                    if (msg.file.isEmpty()) {
                        ++failWarningsCount;
                        continue;
                    }

                    msg.errorCode = "";

                    NodeList nodeErrorCode = eElement.getElementsByTagName("ErrorCode");

                    if (nodeErrorCode != null && nodeErrorCode.item(0) != null && nodeErrorCode.item(0).getTextContent() != null) {
                        msg.errorCode = nodeErrorCode.item(0).getTextContent().trim();
                    }

                    if (msg.errorCode.isEmpty() || msg.errorCode.charAt(0) != 'V') {
                        ++failWarningsCount;
                        continue;
                    }

                    msg.message = "<a target=\"_blank\" href=\"https://www.viva64.com/en/w/"
                            + msg.errorCode.toLowerCase(Locale.ENGLISH) + "/\">"
                            + msg.errorCode + "</a> " + eElement.getElementsByTagName("Message").item(0).getTextContent();

                    msg.level = eElement.getElementsByTagName("Level").item(0).getTextContent();

                    try {
                        msg.lineNumber = Integer.parseInt(eElement.getElementsByTagName("Line").item(0).getTextContent());
                        if (msg.lineNumber <= 0) {

                            ++failWarningsCount;
                            continue;
                        }
                    }
                    catch (NumberFormatException e) {

                        ++failWarningsCount;
                        continue;
                    }

                    plogMessages.add(msg);
                }
            }
        }
        catch (ParserConfigurationException e) {
            throw new ParsingCanceledException(e);
        }
        catch (SAXException e) {
            throw new ParsingException(e);
        }
        catch (IOException e) {
            throw new ParsingException(e);
        }

        if ((plogMessages.size() + falseAlarmCount) == 0 && failWarningsCount > 0) {
            throw new IllegalStateException("No messages were parsed!");
        }

        return plogMessages;
    }
}
