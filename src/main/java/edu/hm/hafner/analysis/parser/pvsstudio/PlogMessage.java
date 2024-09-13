package edu.hm.hafner.analysis.parser.pvsstudio;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.util.IntegerParser;

import static edu.hm.hafner.analysis.IssueParser.*;

/**
 * A parser for PVS-Studio Plog/XML files.
 *
 * @author PVS-Studio Team
 */
class PlogMessage {
    private String file = StringUtils.EMPTY;
    private int lineNumber = 0;
    private String errorCode = StringUtils.EMPTY;
    private String message = StringUtils.EMPTY;
    private String level = StringUtils.EMPTY;

    public String getHash() {
        return errorCode + message + file + lineNumber;
    }

    @Override
    public String toString() {
        return message;
    }

    String getFilePath() {
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

    private static boolean skipMessage(final NodeList elements) {
        return elements != null && elements.item(0) != null
                && equalsIgnoreCase(elements.item(0).getTextContent(), "true");
    }

    private static boolean nodeNotNull(final NodeList elements) {
        return elements != null && elements.item(0) != null && elements.item(0).getTextContent() != null;
    }

    private static boolean errorCodeIsValid(final String errorCode) {
        return !(errorCode.isEmpty() || errorCode.charAt(0) != 'V');
    }

    private static void processNode(final List<PlogMessage> plogMessages, final Node node) {
        var eElement = (Element) node;

        var nodeFalseAlarm = eElement.getElementsByTagName("FalseAlarm");
        if (skipMessage(nodeFalseAlarm)) {
            ++falseAlarmCount;
            return;
        }

        var nodeFile = eElement.getElementsByTagName("File");

        var msg = new PlogMessage();

        if (nodeNotNull(nodeFile)) {
            msg.file = nodeFile.item(0).getTextContent().trim();
        }

        if (msg.file.isEmpty()) {
            ++failWarningsCount;
            return;
        }

        var nodeErrorCode = eElement.getElementsByTagName("ErrorCode");

        if (nodeNotNull(nodeErrorCode)) {
            msg.errorCode = nodeErrorCode.item(0).getTextContent().trim();
        }

        if (!errorCodeIsValid(msg.errorCode)) {
            ++failWarningsCount;
            return;
        }

        msg.message = "<a target=\"_blank\" href=\"https://pvs-studio.com/en/docs/warnings/"
                + msg.errorCode.toLowerCase(Locale.ENGLISH) + "/\">"
                + msg.errorCode + "</a> "
                + eElement.getElementsByTagName("Message").item(0).getTextContent();

        msg.level = eElement.getElementsByTagName("Level").item(0).getTextContent();

        msg.lineNumber = IntegerParser.parseInt(eElement.getElementsByTagName("Line").item(0).getTextContent());
        if (msg.lineNumber <= 0) {
            ++failWarningsCount;
            return;
        }

        plogMessages.add(msg);
    }

    private static int failWarningsCount = 0;
    private static int falseAlarmCount = 0;

    /**
     * Getting list messages from report.
     *
     * @param readerFactory
     *         factory containing report file reader
     *
     * @return list plog messages
     */
    static List<PlogMessage> getMessagesFromReport(final ReaderFactory readerFactory) {
        Document plogDoc = readerFactory.readDocument();
        plogDoc.getDocumentElement().normalize();

        NodeList nList = plogDoc.getElementsByTagName("PVS-Studio_Analysis_Log");

        falseAlarmCount = 0;
        failWarningsCount = 0;

        List<PlogMessage> plogMessages = new ArrayList<>();
        for (int nodeCount = 0; nodeCount < nList.getLength(); nodeCount++) {
            Node nNode = nList.item(nodeCount);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                processNode(plogMessages, nNode);
            }
        }

        if ((plogMessages.size() + falseAlarmCount) == 0 && failWarningsCount > 0) {
            Logger.getLogger(PVSStudioParser.class.getName()).log(Level.SEVERE, "No messages were parsed!");
        }

        return plogMessages;
    }
}
