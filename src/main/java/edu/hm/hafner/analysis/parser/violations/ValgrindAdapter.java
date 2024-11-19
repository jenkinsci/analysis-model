package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import j2html.tags.ContainerTag;
import j2html.tags.DomContentJoiner;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.ValgrindParser;

import static j2html.TagCreator.*;

/**
 * Parses Valgrind XML report files.
 *
 * @author Tony Ciavarella
 */
public class ValgrindAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = -6117336551972081612L;
    private static final int NUMBERED_STACK_THRESHOLD = 2;
    private static final int NO_LINE = -1;

    @Override
    ValgrindParser createParser() {
        return new ValgrindParser();
    }

    @Override
    Report convertToReport(final Set<Violation> violations) {
        try (var issueBuilder = new IssueBuilder()) {
            var report = new Report();

            for (Violation violation: violations) {
                updateIssueBuilder(violation, issueBuilder);
                issueBuilder.setCategory("valgrind:" + violation.getReporter());
                issueBuilder.setDescription(generateDescriptionHtml(violation));
                report.add(issueBuilder.buildAndClean());
            }

            return report;
        }
    }

    private String generateDescriptionHtml(final Violation violation) {
        var specifics = violation.getSpecifics();
        var auxWhats = getAuxWhatsArray(specifics);

        return DomContentJoiner.join(
                        "",
                        false,
                        generateGeneralTableHtml(violation.getSource(), violation.getGroup(), specifics.get("tid"), specifics.get("threadname"), auxWhats),
                        maybeGenerateStackTracesHtml(specifics.get("stacks"), violation.getMessage(), auxWhats),
                        maybeGenerateSuppressionHtml(specifics.get("suppression"))
                ).render();
    }

    private ContainerTag generateGeneralTableHtml(final String executable, final String uniqueId, @CheckForNull final String threadId, @CheckForNull final String threadName, @CheckForNull final JSONArray auxWhats) {
        var generalTable = table(
                attrs(".table.table-striped"),
                maybeGenerateTableRowHtml("Executable", executable),
                maybeGenerateTableRowHtml("Unique Id", uniqueId),
                maybeGenerateTableRowHtml("Thread Id", threadId),
                maybeGenerateTableRowHtml("Thread Name", threadName)
        );

        if (auxWhats != null && !auxWhats.isEmpty()) {
            for (int auxwhatIndex = 0; auxwhatIndex < auxWhats.length(); auxwhatIndex++) {
                generalTable.with(maybeGenerateTableRowHtml("Auxiliary", auxWhats.getString(auxwhatIndex)));
            }
        }

        return generalTable;
    }

    @CheckForNull
    private ContainerTag maybeGenerateStackTracesHtml(@CheckForNull final String stacksJson, final String message, @CheckForNull final JSONArray auxWhats) {
        if (StringUtils.isBlank(stacksJson)) {
            return null;
        }

        var stacks = new JSONArray(new JSONTokener(stacksJson));

        if (!stacks.isEmpty()) {
            var stackTraces = div();

            stackTraces.with(generateStackTraceHtml("Primary Stack Trace", message, stacks.getJSONArray(0)));

            for (int stackIndex = 1; stackIndex < stacks.length(); stackIndex++) {
                String msg = null;

                if (auxWhats != null && auxWhats.length() >= stackIndex) {
                    msg = auxWhats.getString(stackIndex - 1);
                }

                var title = "Auxiliary Stack Trace";

                if (stacks.length() > NUMBERED_STACK_THRESHOLD) {
                    title += " #" + stackIndex;
                }

                stackTraces.with(generateStackTraceHtml(title, msg, stacks.getJSONArray(stackIndex)));
            }

            return stackTraces;
        }

        return null;
    }

    private ContainerTag generateStackTraceHtml(final String title, @CheckForNull final String message, final JSONArray frames) {
        var stackTraceContainer =
                div(
                        br(),
                        h4(title),
                        iff(StringUtils.isNotBlank(message), p(message))
                );

        for (int frameIndex = 0; frameIndex < frames.length(); frameIndex++) {
            var frame = frames.getJSONObject(frameIndex);

            if (frameIndex > 0) {
                stackTraceContainer.with(br());
            }

            stackTraceContainer.with(generateStackFrameHtml(frame));
        }

        return stackTraceContainer;
    }

    private ContainerTag generateStackFrameHtml(final JSONObject frame) {
        return
                table(
                        maybeGenerateTableRowHtml("Object", frame.optString("obj")),
                        maybeGenerateTableRowHtml("Function", frame.optString("fn")),
                        maybeGenerateStackFrameFileTableRowHtml(frame)
                );
    }

    private ContainerTag maybeGenerateSuppressionHtml(@CheckForNull final String suppression) {
        return
                iff(
                        StringUtils.isNotBlank(suppression),
                        div(br(), h4("Suppression"), table(tr(td(pre(suppression)))))
                );
    }

    private ContainerTag maybeGenerateTableRowHtml(final String name, @CheckForNull final String value) {
        return iff(StringUtils.isNotBlank(value), tr(td(text(name), td(text(value)))));
    }

    @CheckForNull
    private ContainerTag maybeGenerateStackFrameFileTableRowHtml(final JSONObject frame) throws JSONException {
        var file = frame.optString("file");

        if (StringUtils.isNotBlank(file)) {
            var dir = frame.optString("dir");
            int line = frame.optInt("line", NO_LINE);
            var fileBuilder = new StringBuilder(256);

            if (StringUtils.isNotBlank(dir)) {
                fileBuilder.append(dir).append('/');
            }

            fileBuilder.append(file);

            if (line != NO_LINE) {
                fileBuilder.append(':').append(line);
            }

            return maybeGenerateTableRowHtml("File", fileBuilder.toString());
        }

        return null;
    }

    @CheckForNull
    private JSONArray getAuxWhatsArray(final Map<String, String> specifics) {
        var auxWhatsJson = specifics.get("auxwhats");
        return StringUtils.isNotBlank(auxWhatsJson) ? new JSONArray(new JSONTokener(auxWhatsJson)) : null;
    }
}
