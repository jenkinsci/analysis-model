package edu.hm.hafner.analysis;

import static edu.hm.hafner.analysis.assertj.SoftAssertions.assertSoftly;

import java.util.Optional;
import java.util.regex.Matcher;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.util.LookaheadStream;

class LookaheadParserTest extends AbstractParserTest {

    private final String pattern = "hello";
    private boolean isWindows = false;
    
    protected LookaheadParserTest() {
        super("msysmake.txt");
    }

    @Override
    protected IssueParser createParser() {
        return new LookaheadparserImplForTest(pattern, isWindows);
    }

    @Test
    void msysPathsAreFixedOnWindows() {
        isWindows = true;
        
        Report report = parse("msysmake.txt");
        
        assertSoftly(softly -> {
            softly.assertThat(report).hasSize(1);
            softly.assertThat(report.get(0))
                    .hasFileName("c:/dir1/filename.java");
        });
    }

    @Test
    void apparentMsysPathsAreNotChangedOnNonWindows() {
        isWindows = false;
        
        Report report = parse("msysmake.txt");
        
        assertSoftly(softly -> {
            softly.assertThat(report).hasSize(1);
            softly.assertThat(report.get(0))
                    .hasFileName("/c/dir1/filename.java");
        });
    }


    @Override
    protected void assertThatIssuesArePresent(Report report, SoftAssertions softly) {        
    }
    
    
    
    protected static class LookaheadparserImplForTest extends LookaheadParser {

        private static final long serialVersionUID = 611665599082721916L;
        private final boolean isWindows;
        
        protected LookaheadparserImplForTest(String pattern, boolean isWindows) {
            super(pattern);
            this.isWindows = isWindows;
        }
        
        @Override
        protected boolean isWindows() {
            return this.isWindows;
        }

        @Override
        protected Optional<Issue> createIssue(Matcher matcher, LookaheadStream lookahead, IssueBuilder builder)
                throws ParsingException {
            return builder.setFileName("filename.java").buildOptional();
        }      
    }
}

