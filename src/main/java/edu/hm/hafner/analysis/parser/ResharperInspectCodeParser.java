package edu.hm.hafner.analysis.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.XmlElementUtil;

/**
 * A parser for the Resharper InspectCode compiler warnings.
 *
 * @author Rafal Jasica
 */
public class ResharperInspectCodeParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 526872513348892L;
    private static final String WARNING_TYPE = "ResharperInspectCode";
    private static final String WARNING_PATTERN = "\\<Issue.*?TypeId=\"(.*?)\".*?File=\"(.*?)\".*?Line=\"(.*?)\"" +
            ".*?Message=\"(.*?)\"";

    private final Map<String, Priority> priorityByTypeId = new HashMap<>();

    /**
     * Creates a new instance of {@link ResharperInspectCodeParser}.
     */
    public ResharperInspectCodeParser() {
        super(WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(2)).setLineStart(parseInt(matcher.group(3)))
                             .setCategory(matcher.group(1)).setType(WARNING_TYPE).setMessage(matcher.group(4))
                             .setPriority(GetPriority(matcher.group(1))).build();
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        // TODO: remove this check
        if (line.contains("<IssueType Id=")) {
            try {
                // This is a quick workaround to get the IssueType parsing
                // to work for this parser (which is a RegexpLineParser)
                // It should probably be entirely xml-based instead
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder;
                docBuilder = docBuilderFactory.newDocumentBuilder();

                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader("<IssueTypes>" + line + "</IssueTypes>"));
                Document doc = docBuilder.parse(is);

                NodeList mainNode = doc.getElementsByTagName("IssueTypes");
                Element issueTypesElement = (Element)mainNode.item(0);
                parseIssueTypes(XmlElementUtil.getNamedChildElements(issueTypesElement, "IssueType"));
            }
            catch (ParserConfigurationException | SAXException ignored) {
            }
            catch (IOException ex) {
            }
            return false;
        }
        return line.contains("<Issue");
    }

    private void parseIssueTypes(final List<Element> issueTypeElements) {
        for (Element issueTypeElement : issueTypeElements) {
            String id = issueTypeElement.getAttribute("Id");
            if (!"".equals(id)) {
                String severity = issueTypeElement.getAttribute("Severity");
                if ("ERROR".equals(severity)) {
                    priorityByTypeId.put(id, Priority.HIGH);
                }
                else if ("WARNING".equals(severity)) {
                    priorityByTypeId.put(id, Priority.NORMAL);
                }
                else if ("SUGGESTION".equals(severity)) {
                    priorityByTypeId.put(id, Priority.LOW);
                }
            }
        }
    }

    private Priority GetPriority(final String typeId) {
        if (priorityByTypeId.containsKey(typeId)) {
            return priorityByTypeId.get(typeId);
        }
        else {
            return Priority.NORMAL;
        }
    }
}
