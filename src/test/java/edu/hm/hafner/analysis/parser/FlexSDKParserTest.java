/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import edu.hm.hafner.IssueAssert;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the class {@link FlexSDKParser}.
 */
public class FlexSDKParserTest extends ParserTester {
    private static final String TYPE = new FlexSDKParser().getId();
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Parses a file with two deprecation warnings.
     */
    @Test
    public void parseDeprecation() {
        Issues warnings = new FlexSDKParser().parse(openFile());

        assertThat(warnings.size()).isEqualTo(5);

        Iterator<Issue> iterator = warnings.iterator();

        Issue firstCompcAnnotation = iterator.next();
        IssueAssert.assertThat(firstCompcAnnotation).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(34)
                .hasLineEnd(34)
                .hasMessage("class 'FeedStructureHelper' will be scoped to the default namespace: com.company.flex.feed internal.  It will not be visible outside of this package.")
                .hasFileName("D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/uicomponents/ugv_component/src/main/com/company/flex/feed/FeedStructureHelper.as")
                .hasType(TYPE);

        Issue secondCompcAnnotation = iterator.next();
        IssueAssert.assertThat(secondCompcAnnotation).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(122)
                .hasLineEnd(122)
                .hasMessage("Duplicate variable definition.")
                .hasFileName("D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/uicomponents/ugv_component/src/main/com/company/flex/component/chart/lasso/DefaultLassoObjectsHandler.as")
                .hasType(TYPE);

        Issue firstASMxmlcAnnotation = iterator.next();
        IssueAssert.assertThat(firstASMxmlcAnnotation).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(115)
                .hasLineEnd(115)
                .hasMessage("return value for function 'cx' has no type declaration.")
                .hasFileName("D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/extensibility/wordpress/Tag.as")
                .hasType(TYPE);

        Issue firstMXMLMxmlcAnnotation = iterator.next();
        IssueAssert.assertThat(firstMXMLMxmlcAnnotation).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(157)
                .hasLineEnd(157)
                .hasMessage("var 'cacheList' will be scoped to the default namespace: HelloExtensibleWorld: internal.  It will not be visible outside of this package.")
                .hasFileName("D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/HelloExtensibleWorld.mxml")
                .hasType(TYPE);

        Issue secondMXMLMxmlcAnnotation = iterator.next();
        IssueAssert.assertThat(secondMXMLMxmlcAnnotation).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(148)
                .hasLineEnd(148)
                .hasMessage("The CSS type selector 'Book' was not processed, because the type was not used in the application.")
                .hasFileName("D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/HelloExtensibleWorld.mxml")
                .hasType(TYPE);

    }

    @Override
    protected String getWarningsFile() {
        return "flexsdk.txt";
    }
}