package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static edu.hm.hafner.analysis.IssueAssert.assertThat;

class IssueBuilderTest {
    @Test
    public void createIssueWithDefaultValues() {
        final Issue want = new Issue(
                "",
                0,
                0,
                0,
                0,
                "",
                "",
                "",
                Priority.NORMAL,
                "",
                ""
        );

        final IssueBuilder sut = new IssueBuilder()
                .setLineStart(0)
                .setLineEnd(0)
                .setColumnStart(0)
                .setColumnEnd(0)
                .setFileName("")
                .setCategory("")
                .setType("")
                .setPackageName("")
                .setPriority(Priority.NORMAL)
                .setMessage("")
                .setDescription("");


        assertThat(sut.build()).isEqualTo(want);
    }

    @Test
    public void createIssueWithNullValues() {
        final Issue want = new Issue(
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                null,
                null
        );

        final IssueBuilder sut = new IssueBuilder();

        assertThat(sut.build()).isEqualTo(want);
    }

    @Test
    public void copyFromIssueEqualsTheSameIssue() {
        final Issue want = new Issue(
                "asdf",
                0,
                0,
                0,
                0,
                "qweqr",
                "qwer",
                "asdf",
                Priority.NORMAL,
                "jkl",
                "zuio"
        );

        final IssueBuilder sut = new IssueBuilder().copy(want);


        assertThat(sut.build()).isEqualTo(want);
    }

    @Test
    public void copyFromIssueNotEqualsOtherIssue() {
        final Issue want = new Issue(
                "asdf",
                0,
                0,
                0,
                0,
                "qweqr",
                "qwer",
                "asdf",
                Priority.NORMAL,
                "jkl",
                "zuio"
        );

        final IssueBuilder sut = new IssueBuilder().copy(want);


        assertThat(sut.build()).isNotEqualTo(new IssueBuilder().build());
    }

    @Test
    public void fillWithUnAllowedValues() {
        final IssueBuilder sut = new IssueBuilder();

        sut
                .setLineStart(-1)
                .setLineEnd(-1)
                .setColumnStart(-1)
                .setColumnEnd(-1)
                .setPriority(null);

        assertThat(sut.build())
                .hasFileName("-")
                .hasCategory("")
                .hasType("-")
                .hasPriority(Priority.NORMAL)
                .hasMessage("")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasFingerPrint("-");
    }

}