/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link FlexSdkParser}.
 */
class FlexSdkParserTest extends AbstractParserTest {
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Creates a new instance of {@link FlexSdkParserTest}.
     */
    protected FlexSdkParserTest() {
        super("flexsdk.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);

        Iterator<Issue> iterator = report.iterator();
        softly.assertThat(iterator.next()).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(34)
                .hasLineEnd(34)
                .hasMessage(
                        "class 'FeedStructureHelper' will be scoped to the default namespace: com.company.flex.feed internal.  It will not be visible outside of this package.")
                .hasFileName(
                        "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/uicomponents/ugv_component/src/main/com/company/flex/feed/FeedStructureHelper.as");
        softly.assertThat(iterator.next()).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(122)
                .hasLineEnd(122)
                .hasMessage("Duplicate variable definition.")
                .hasFileName(
                        "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/uicomponents/ugv_component/src/main/com/company/flex/component/chart/lasso/DefaultLassoObjectsHandler.as");
        softly.assertThat(iterator.next()).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(115)
                .hasLineEnd(115)
                .hasMessage("return value for function 'cx' has no type declaration.")
                .hasFileName(
                        "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/extensibility/wordpress/Tag.as");
        softly.assertThat(iterator.next()).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(157)
                .hasLineEnd(157)
                .hasMessage(
                        "var 'cacheList' will be scoped to the default namespace: HelloExtensibleWorld: internal.  It will not be visible outside of this package.")
                .hasFileName(
                        "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/HelloExtensibleWorld.mxml");
        softly.assertThat(iterator.next()).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(148)
                .hasLineEnd(148)
                .hasMessage(
                        "The CSS type selector 'Book' was not processed, because the type was not used in the application.")
                .hasFileName(
                        "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/HelloExtensibleWorld.mxml");
    }

    @Override
    protected FlexSdkParser createParser() {
        return new FlexSdkParser();
    }
}
