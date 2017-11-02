/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link FlexSDKParser}.
 */
public class FlexSDKParserTest extends ParserTester {
    private static final String TYPE = new FlexSDKParser().getId();
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Issues warnings = new FlexSDKParser().parse(openFile());

        assertEquals(5, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        Issue firstCompcAnnotation = iterator.next();
        checkWarning(firstCompcAnnotation,
                34,
                "class 'FeedStructureHelper' will be scoped to the default namespace: com.company.flex.feed internal.  It will not be visible outside of this package.",
                "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/uicomponents/ugv_component/src/main/com/company/flex/feed/FeedStructureHelper.as",
                TYPE, CATEGORY, Priority.NORMAL);

        Issue secondCompcAnnotation = iterator.next();
        checkWarning(secondCompcAnnotation,
                122,
                "Duplicate variable definition.",
                "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/uicomponents/ugv_component/src/main/com/company/flex/component/chart/lasso/DefaultLassoObjectsHandler.as",
                TYPE, CATEGORY, Priority.NORMAL);

        Issue firstASMxmlcAnnotation = iterator.next();
        checkWarning(firstASMxmlcAnnotation,
                115,
                "return value for function 'cx' has no type declaration.",
                "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/extensibility/wordpress/Tag.as",
                TYPE, CATEGORY, Priority.NORMAL);

        Issue firstMXMLMxmlcAnnotation = iterator.next();
        checkWarning(firstMXMLMxmlcAnnotation,
                157,
                "var 'cacheList' will be scoped to the default namespace: HelloExtensibleWorld: internal.  It will not be visible outside of this package.",
                "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/HelloExtensibleWorld.mxml",
                TYPE, CATEGORY, Priority.NORMAL);

        Issue secondMXMLMxmlcAnnotation = iterator.next();
        checkWarning(secondMXMLMxmlcAnnotation,
                148,
                "The CSS type selector 'Book' was not processed, because the type was not used in the application.",
                "D:/workspaces/flexcompo_trunkdev_nightly/src/flexcompo/samples/ugv_helloExtensibility_flex/src/main/HelloExtensibleWorld.mxml",
                TYPE, CATEGORY, Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "flexsdk.txt";
    }
}