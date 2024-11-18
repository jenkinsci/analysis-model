package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.util.XmlElementUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Parser for Taglist Maven Plugin output. During parse, class names are converted into assumed file system names, so
 * {@code package.name.class} becomes {@code package/name/class.java}.
 *
 * @author Jason Faust
 * @see <a href= "https://www.mojohaus.org/taglist-maven-plugin/">https://www.mojohaus.org/taglist-maven-plugin/</a>
 */
public class TaglistParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            var xPathFactory = XPathFactory.newInstance();
            var xPath = xPathFactory.newXPath();

            var report = new Report();

            var document = readerFactory.readDocument();
            var tags = (NodeList) xPath.evaluate("/report/tags/tag", document, XPathConstants.NODESET);
            for (Element tag : XmlElementUtil.nodeListToList(tags)) {
                String category = xPath.evaluate("@name", tag);
                issueBuilder.setCategory(category);

                var files = (NodeList) xPath.evaluate("files/file", tag, XPathConstants.NODESET);
                for (Element file : XmlElementUtil.nodeListToList(files)) {
                    String clazz = xPath.evaluate("@name", file);
                    if (clazz != null) {
                        issueBuilder.setFileName(class2file(clazz));
                        issueBuilder.setPackageName(class2package(clazz));
                        issueBuilder.setAdditionalProperties(clazz);
                    }

                    var comments = (NodeList) xPath.evaluate("comments/comment", file, XPathConstants.NODESET);
                    for (Element comment : XmlElementUtil.nodeListToList(comments)) {
                        issueBuilder.setLineStart(xPath.evaluate("lineNumber", comment));
                        issueBuilder.setMessage(xPath.evaluate("comment", comment));

                        report.add(issueBuilder.build());
                    }
                }
            }

            return report;
        }
        catch (XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

    private String class2file(final String clazz) {
        return clazz.replace('.', '/').concat(".java");
    }

    @CheckForNull
    private String class2package(final String clazz) {
        int idx = clazz.lastIndexOf('.');
        return idx > 0 ? clazz.substring(0, idx) : null;
    }
}
