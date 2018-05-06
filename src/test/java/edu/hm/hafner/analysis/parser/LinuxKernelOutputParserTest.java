package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;



/**
 * Tests the class {@link LinuxKernelOutputParser}.
 *
 * @author Benedikt Spranger
 */
class LinuxKernelOutputParserTest extends AbstractIssueParserTest {
    protected LinuxKernelOutputParserTest() {
        super("kernel.log");
    }


    @SuppressWarnings("methodlength")
    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(26);

        softly.assertThat(report.get(0))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: RSDP 0x00000000000F68D0 000014 (v00 BOCHS )")
                .hasFileName("Nil");

        softly.assertThat(report.get(1))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: RSDT 0x0000000007FE18DC 000030 (v01 BOCHS  BXPCRSDT 00000001 BXPC 00000001)")
                .hasFileName("Nil");

        softly.assertThat(report.get(2))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: FACP 0x0000000007FE17B8 000074 (v01 BOCHS  BXPCFACP 00000001 BXPC 00000001)")
                .hasFileName("Nil");

        softly.assertThat(report.get(3))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: DSDT 0x0000000007FE0040 001778 (v01 BOCHS  BXPCDSDT 00000001 BXPC 00000001)")
                .hasFileName("Nil");

        softly.assertThat(report.get(4))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: FACS 0x0000000007FE0000 000040")
                .hasFileName("Nil");

        softly.assertThat(report.get(5))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: APIC 0x0000000007FE182C 000078 (v01 BOCHS  BXPCAPIC 00000001 BXPC 00000001)")
                .hasFileName("Nil");

        softly.assertThat(report.get(6))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: HPET 0x0000000007FE18A4 000038 (v01 BOCHS  BXPCHPET 00000001 BXPC 00000001)")
                .hasFileName("Nil");

        softly.assertThat(report.get(7))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: 1 ACPI AML tables successfully acquired and loaded")
                .hasFileName("Nil");

        softly.assertThat(report.get(8))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("kworker/u2:0 (32) used greatest stack depth: 14256 bytes left")
                .hasFileName("Nil");

        softly.assertThat(report.get(9))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("kworker/u2:0 (26) used greatest stack depth: 13920 bytes left")
                .hasFileName("Nil");

        softly.assertThat(report.get(10))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("acpi PNP0A03:00: fail to add MMCONFIG information, can't access extended PCI configuration space under this bridge.")
                .hasFileName("Nil");

        softly.assertThat(report.get(11))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: Enabled 3 GPEs in block 00 to 0F")
                .hasFileName("Nil");

        softly.assertThat(report.get(12))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("ACPI: PCI Interrupt Link [LNKC] enabled at IRQ 11")
                .hasFileName("Nil");

        softly.assertThat(report.get(13))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("mdev (949) used greatest stack depth: 13888 bytes left")
                .hasFileName("Nil");

        softly.assertThat(report.get(14))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("KABOOM: kaboom_init: WARNING")
                .hasFileName("Nil");

        softly.assertThat(report.get(15))
                .hasPriority(Priority.NORMAL)
                .hasCategory("WARNING")
                .hasLineStart(26)
                .hasLineEnd(26)
                .hasMessage("WARNING in kaboom_init()")
                .hasFileName("/home/bene/work/rtl/test-description/tmp/linux-stable-rt/drivers/misc/kaboom.c");

        softly.assertThat(report.get(16))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("KABOOM: kaboom_init: ERR")
                .hasFileName("Nil");

        softly.assertThat(report.get(17))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("KABOOM: kaboom_init: CRIT")
                .hasFileName("Nil");

        softly.assertThat(report.get(18))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("KABOOM: kaboom_init: ALERT")
                .hasFileName("Nil");

        softly.assertThat(report.get(19))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("KABOOM: kaboom_init: EMERG")
                .hasFileName("Nil");

        softly.assertThat(report.get(20))
                .hasPriority(Priority.HIGH)
                .hasCategory("BUG")
                .hasLineStart(39)
                .hasLineEnd(39)
                .hasMessage("BUG in ()")
                .hasFileName("/home/bene/work/rtl/test-description/tmp/linux-stable-rt/drivers/misc/kaboom.c");

        softly.assertThat(report.get(21))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("sysrq: SysRq : Emergency Sync")
                .hasFileName("Nil");

        softly.assertThat(report.get(22))
                .hasPriority(Priority.LOW)
                .hasCategory("Kernel Output")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("sysrq: SysRq : Emergency Remount R/O")
                .hasFileName("Nil");
    }

    @Override
    protected LinuxKernelOutputParser createParser() {
        return new LinuxKernelOutputParser();
    }
}
