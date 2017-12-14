package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link DrMemoryParser}.
 */
public class DrMemoryParserTest extends AbstractParserTest {
    /**
     * Creates a new instance of {@link AbstractParserTest}.
     */
    protected DrMemoryParserTest() {
        super("drmemory.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(8);

        Iterator<Issue> iterator = issues.iterator();

        softly.assertThat(iterator.next())
                .hasLineEnd(7)
                .hasLineStart(7)
                .hasMessage("LEAK 150 direct bytes 0x005a2540-0x005a25d6 + 0 indirect bytes<br>"
                        + "# 0 replace_malloc                                     [/drmemory_package/common/alloc_replace.c:2576]<br>"
                        + "# 1 test_open_address_hashmap_initialize               [/open_address_hash/test_open_address_hash.c:84]<br>"
                        + "# 2 main                                               [/open_address_hash/run_open_address_hash.c:7]")
                .hasFileName("/open_address_hash/run_open_address_hash.c")
                .hasCategory("Leak")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(7)
                .hasLineStart(7)
                .hasMessage("POSSIBLE LEAK 150 direct bytes 0x005a25f8-0x005a268e + 0 indirect bytes<br>"
                        + "# 0 replace_malloc                                              [/drmemory_package/common/alloc_replace.c:2576]<br>"
                        + "# 1 test_open_address_hashmap_compute_simple_hash               [/open_address_hash/test_open_address_hash.c:107]<br>"
                        + "# 2 main                                                        [/open_address_hash/run_open_address_hash.c:7]")
                .hasFileName("/open_address_hash/run_open_address_hash.c")
                .hasCategory("Possible Leak")
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(0)
                .hasLineStart(0)
                .hasMessage("LEAK 150 direct bytes 0x005a25f8-0x005a268e + 0 indirect bytes<br>"
                        + "# 0 replace_malloc<br>"
                        + "# 1 test_open_address_hashmap_compute_complex_hash<br>"
                        + "# 2 main")
                .hasFileName("Unknown")
                .hasCategory("Leak")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(16)
                .hasLineStart(16)
                .hasMessage("UNINITIALIZED READ: reading 0xb741b004-0xb741b012 14 byte(s) within 0xb741b000-0xb741b012<br>"
                        + "# 0 system call write parameter #1<br>"
                        + "# 1 libc.so.6!__GI___libc_write                    [../sysdeps/unix/syscall-template.S:81]<br>"
                        + "# 2 libc.so.6!_IO_new_file_write                   [/build/eglibc-X4bnBz/eglibc-2.19/libio/fileops.c:1261]<br>"
                        + "# 3 libc.so.6!new_do_write                         [/build/eglibc-X4bnBz/eglibc-2.19/libio/fileops.c:538]<br>"
                        + "# 4 libc.so.6!_IO_new_do_write                     [/build/eglibc-X4bnBz/eglibc-2.19/libio/fileops.c:511]<br>"
                        + "# 5 libc.so.6!__GI__IO_file_sync                   [/build/eglibc-X4bnBz/eglibc-2.19/libio/fileops.c:892]<br>"
                        + "# 6 libc.so.6!__GI__IO_fflush                      [/build/eglibc-X4bnBz/eglibc-2.19/libio/iofflush.c:41]<br>"
                        + "# 7 fll_write_node                                 [/var/lib/jenkins/jobs/jobs_name/workspace/jenkins_config/iondb/src/tests/unit/dictionary/linear_hash/run_linear_hash.c:16]<br>"
                        + "# 8 fll_create                                     [/var/lib/jenkins/jobs/jobs_name/workspace/jenkins_config/iondb/src/tests/unit/dictionary/linear_hash/run_linear_hash.c:16]<br>"
                        + "# 9 create_test_linked_list                        [/var/lib/jenkins/jobs/jobs_name/workspace/jenkins_config/iondb/src/tests/unit/dictionary/linear_hash/test_file_linked_list.c:24]<br>"
                        + "#10 test_file_linked_list_initialize               [/var/lib/jenkins/jobs/jobs_name/workspace/jenkins_config/iondb/src/tests/unit/dictionary/linear_hash/test_file_linked_list.c:55]<br>"
                        + "#11 planck_unit_run_suite                          [/var/lib/jenkins/jobs/jobs_name/workspace/jenkins_config/iondb/src/tests/unit/dictionary/linear_hash/run_linear_hash.c:16]<br>"
                        + "#12 runalltests_file_linked_list                   [/var/lib/jenkins/jobs/jobs_name/workspace/jenkins_config/iondb/src/tests/unit/dictionary/linear_hash/test_file_linked_list.c:776]<br>"
                        + "#13 main                                           [/var/lib/jenkins/jobs/jobs_name/workspace/jenkins_config/iondb/src/tests/unit/dictionary/linear_hash/run_linear_hash.c:11]<br>"
                        + "Note: @0:00:01.247 in thread 7601")
                .hasFileName("/var/lib/jenkins/jobs/jobs_name/workspace/jenkins_config/iondb/src/tests/unit/dictionary/linear_hash/run_linear_hash.c")
                .hasCategory("Uninitialized Read")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(164)
                .hasLineStart(164)
                .hasMessage("INVALID HEAP ARGUMENT: free 0x00001234<br>"
                        + "Elapsed time = 0:00:00.180 in thread 21848<br>"
                        + "# 0 malloc!main                                     [/var/lib/jenkins/jobs/drmemory/workspace/git/src/tests/malloc.c:164]<br>"
                        + "# 1 libc.so.6!__libc_start_main                     [/build/buildd/eglibc-2.11.1/csu/libc-start.c:226]<br>"
                        + "# 2 malloc!_start")
                .hasFileName("/var/lib/jenkins/jobs/drmemory/workspace/git/src/tests/malloc.c")
                .hasCategory("Invalid Heap Argument")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(139)
                .hasLineStart(139)
                .hasMessage("INVALID HEAP ARGUMENT: allocated with operator new[], freed with operator delete<br>"
                        + "# 0 test_mismatch                   [cs2::bug.cpp:122]<br>"
                        + "# 1 main                            [cs2::bug.cpp:139]<br>"
                        + "Note: memory was allocated here:<br>"
                        + "Note: # 0 test_mismatch                   [cs2::bug.cpp:121]<br>"
                        + "Note: # 1 main                            [cs2::bug.cpp:139]")
                .hasFileName("cs2::bug.cpp")
                .hasCategory("Invalid Heap Argument")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(0)
                .hasLineStart(0)
                .hasMessage("UNADDRESSABLE ACCESS of freed memory: reading 0x001338a8-0x001338ac 4 byte(s)")
                .hasFileName("Nil")
                .hasCategory("Unaddressable Access")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(0)
                .hasLineStart(0)
                .hasMessage("INVALID HEAP ARGUMENT: allocated with operator new[], freed with operator delete<br>"
                        + "Note: memory was allocated here:<br>"
                        + "Note: # 0 test_mismatch                   [test/cs2::bug.cpp:121]<br>"
                        + "Note: # 1 main                            [test/cs2::bug.cpp:139]")
                .hasFileName("Nil")
                .hasCategory("Invalid Heap Argument")
                .hasPriority(Priority.HIGH);

        softly.assertAll();
    }

    @Override
    protected AbstractParser createParser() {
        return new DrMemoryParser();
    }
}

