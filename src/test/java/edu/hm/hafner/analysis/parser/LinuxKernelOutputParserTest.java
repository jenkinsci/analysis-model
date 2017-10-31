package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Assertions.IssueSoftAssertion;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.Assertions.IssuesAssert.*;

/**
 * Tests the class {@link LinuxKernelOutputParser}.
 *
 * @author Benedikt Spranger
 */
public class LinuxKernelOutputParserTest extends ParserTester {
    private static final String TYPE = new LinuxKernelOutputParser().getId();

    /**
     * Parse a kernel log file.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new LinuxKernelOutputParser().parse(openFile());

        assertThat(warnings).hasSize(26);

        Iterator<Issue> iterator = warnings.iterator();

        Issue firstAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(firstAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: RSDP 0x00000000000F68D0 000014 (v00 BOCHS )")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue secondAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(secondAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: RSDT 0x0000000007FE18DC 000030 (v01 BOCHS  BXPCRSDT 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue thirdAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(thirdAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: FACP 0x0000000007FE17B8 000074 (v01 BOCHS  BXPCFACP 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue fourthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(fourthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: DSDT 0x0000000007FE0040 001778 (v01 BOCHS  BXPCDSDT 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue fifthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(fifthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: FACS 0x0000000007FE0000 000040")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue sixthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(sixthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: APIC 0x0000000007FE182C 000078 (v01 BOCHS  BXPCAPIC 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue seventhAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(seventhAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: HPET 0x0000000007FE18A4 000038 (v01 BOCHS  BXPCHPET 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue eightAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(eightAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: 1 ACPI AML tables successfully acquired and loaded")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue ninthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(ninthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("kworker/u2:0 (32) used greatest stack depth: 14256 bytes left")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue tenthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(tenthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("kworker/u2:0 (26) used greatest stack depth: 13920 bytes left")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue eleventhAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(eleventhAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("acpi PNP0A03:00: fail to add MMCONFIG information, can't access extended PCI configuration space under this bridge.")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue twelfthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(twelfthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: Enabled 3 GPEs in block 00 to 0F")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue thirteenthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(thirteenthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: PCI Interrupt Link [LNKC] enabled at IRQ 11")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue fourteenthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(fourteenthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("mdev (949) used greatest stack depth: 13888 bytes left")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue fifteenthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(fifteenthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: WARNING")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue sixteenthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(sixteenthAnnotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("WARNING")
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("WARNING in kaboom_init()")
                    .hasFileName("/home/bene/work/rtl/test-description/tmp/linux-stable-rt/drivers/misc/kaboom.c")
                    .hasType(TYPE);


        });

        Issue seventeenthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(seventeenthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: ERR")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue eighteenthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(eighteenthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: CRIT")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue nineteenthAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(nineteenthAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: ALERT")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue twentiethAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(twentiethAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: EMERG")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue twentyFirstAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(twentyFirstAnnotation)
                    .hasPriority(Priority.HIGH)
                    .hasCategory("BUG")
                    .hasLineStart(39)
                    .hasLineEnd(39)
                    .hasMessage("BUG in ()")
                    .hasFileName("/home/bene/work/rtl/test-description/tmp/linux-stable-rt/drivers/misc/kaboom.c")
                    .hasType(TYPE);


        });

        Issue twentySecondAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(twentySecondAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("sysrq: SysRq : Emergency Sync")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

        Issue twentyThirdAnnotation = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly -> {

            softly.assertThat(twentyThirdAnnotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("sysrq: SysRq : Emergency Remount R/O")
                    .hasFileName("Nil")
                    .hasType(TYPE);


        });

    }

    @Override
    protected String getWarningsFile() {
        return "kernel.log";
    }
}
