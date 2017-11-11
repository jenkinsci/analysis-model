package edu.hm.hafner.analysis.parser;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.SoftAssertions;

import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;

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


        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: RSDP 0x00000000000F68D0 000014 (v00 BOCHS )")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: RSDT 0x0000000007FE18DC 000030 (v01 BOCHS  BXPCRSDT 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: FACP 0x0000000007FE17B8 000074 (v01 BOCHS  BXPCFACP 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(3))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: DSDT 0x0000000007FE0040 001778 (v01 BOCHS  BXPCDSDT 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(4))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: FACS 0x0000000007FE0000 000040")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(5))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: APIC 0x0000000007FE182C 000078 (v01 BOCHS  BXPCAPIC 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(6))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: HPET 0x0000000007FE18A4 000038 (v01 BOCHS  BXPCHPET 00000001 BXPC 00000001)")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(7))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: 1 ACPI AML tables successfully acquired and loaded")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(8))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("kworker/u2:0 (32) used greatest stack depth: 14256 bytes left")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(9))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("kworker/u2:0 (26) used greatest stack depth: 13920 bytes left")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(10))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("acpi PNP0A03:00: fail to add MMCONFIG information, can't access extended PCI configuration space under this bridge.")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(11))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: Enabled 3 GPEs in block 00 to 0F")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(12))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("ACPI: PCI Interrupt Link [LNKC] enabled at IRQ 11")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(13))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("mdev (949) used greatest stack depth: 13888 bytes left")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(14))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: WARNING")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(15))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("WARNING")
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("WARNING in kaboom_init()")
                    .hasFileName("/home/bene/work/rtl/test-description/tmp/linux-stable-rt/drivers/misc/kaboom.c")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(16))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: ERR")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(17))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: CRIT")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(18))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: ALERT")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(19))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("KABOOM: kaboom_init: EMERG")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(20))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("BUG")
                    .hasLineStart(39)
                    .hasLineEnd(39)
                    .hasMessage("BUG in ()")
                    .hasFileName("/home/bene/work/rtl/test-description/tmp/linux-stable-rt/drivers/misc/kaboom.c")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(21))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("sysrq: SysRq : Emergency Sync")
                    .hasFileName("Nil")
                    .hasType(TYPE);

            softly.assertThat(warnings.get(22))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Kernel Output")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("sysrq: SysRq : Emergency Remount R/O")
                    .hasFileName("Nil");


        });


    }

    @Override
    protected String getWarningsFile() {
        return "kernel.log";
    }
}
