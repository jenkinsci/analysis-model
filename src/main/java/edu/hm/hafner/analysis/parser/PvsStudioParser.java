package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.parser.PvsStudioParser.PlogMessagesReader.PlogMessage;
import edu.hm.hafner.analysis.util.IntegerParser;

/**
 * A parser for the PVS-Studio static analyzer.
 *
 * @author PVS-Studio Team
 */
public class PvsStudioParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -7777775729854832128L;
    private static final String SEVERITY_HIGH = "1";
    private static final String SEVERITY_NORMAL = "2";
    private static final String SEVERITY_LOW = "3";

    private static Severity getSeverity(final String level) {
        if (SEVERITY_HIGH.equals(level)) {
            return Severity.WARNING_HIGH;
        }
        else if (SEVERITY_NORMAL.equals(level)) {
            return Severity.WARNING_NORMAL;
        }
        else if (SEVERITY_LOW.equals(level)) {
            return Severity.WARNING_LOW;
        }
        else {
            return Severity.ERROR;
        }
    }

    @Override
    public Report parseReport(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        try (var issueBuilder = new IssueBuilder()) {
            var report = new Report();
            var parser = new PlogMessagesReader();

            for (PlogMessage plogMessage : parser.getMessagesFromReport(readerFactory)) {
                issueBuilder.setFileName(plogMessage.getFilePath());

                issueBuilder.setSeverity(getSeverity(plogMessage.getLevel()));
                issueBuilder.setMessage(plogMessage.toString());
                issueBuilder.setCategory(plogMessage.getType());

                issueBuilder.setType(AnalyzerType.fromErrorCode(plogMessage.getType()).getMessage());

                issueBuilder.setLineStart(plogMessage.getLine());

                report.add(issueBuilder.buildAndClean());
            }

            return report;
        }
    }

    /**
     * A parser for PVS-Studio Plog/XML files.
     *
     * @author PVS-Studio Team
     */
    static class PlogMessagesReader {
        private int failWarningsCount;
        private int falseAlarmCount;

        /**
         * Getting list messages from the report.
         *
         * @param readerFactory
         *         factory containing report file reader
         *
         * @return list plog messages
         */
        List<PlogMessage> getMessagesFromReport(final ReaderFactory readerFactory) {
            var plogDoc = readerFactory.readDocument();
            plogDoc.getDocumentElement().normalize();

            var nList = plogDoc.getElementsByTagName("PVS-Studio_Analysis_Log");

            List<PlogMessage> plogMessages = new ArrayList<>();
            for (int nodeCount = 0; nodeCount < nList.getLength(); nodeCount++) {
                var nNode = nList.item(nodeCount);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    processNode(plogMessages, nNode);
                }
            }

            if ((plogMessages.size() + falseAlarmCount) == 0 && failWarningsCount > 0) {
                Logger.getLogger(PvsStudioParser.class.getName()).log(Level.SEVERE, "No messages were parsed!");
            }

            return plogMessages;
        }

        private void processNode(final List<PlogMessage> plogMessages, final Node node) {
            var eElement = (Element) node;

            var nodeFalseAlarm = eElement.getElementsByTagName("FalseAlarm");
            if (skipMessage(nodeFalseAlarm)) {
                ++falseAlarmCount;
                return;
            }

            var nodeFile = eElement.getElementsByTagName("File");

            var msg = new PlogMessage();

            if (nodeNotNull(nodeFile)) {
                msg.file = nodeFile.item(0).getTextContent().trim();
            }

            if (msg.file.isEmpty()) {
                ++failWarningsCount;
                return;
            }

            var nodeErrorCode = eElement.getElementsByTagName("ErrorCode");

            if (nodeNotNull(nodeErrorCode)) {
                msg.errorCode = nodeErrorCode.item(0).getTextContent().trim();
            }

            if (!errorCodeIsValid(msg.errorCode)) {
                ++failWarningsCount;
                return;
            }

            msg.message = "<a target=\"_blank\" href=\"https://pvs-studio.com/en/docs/warnings/"
                    + msg.errorCode.toLowerCase(Locale.ENGLISH) + "/\">"
                    + msg.errorCode + "</a> "
                    + eElement.getElementsByTagName("Message").item(0).getTextContent();

            msg.level = eElement.getElementsByTagName("Level").item(0).getTextContent();

            msg.lineNumber = IntegerParser.parseInt(eElement.getElementsByTagName("Line").item(0).getTextContent());
            if (msg.lineNumber <= 0) {
                ++failWarningsCount;
                return;
            }

            plogMessages.add(msg);
        }

        private boolean skipMessage(final NodeList elements) {
            return elements != null && elements.item(0) != null
                    && equalsIgnoreCase(elements.item(0).getTextContent(), "true");
        }

        private boolean nodeNotNull(final NodeList elements) {
            return elements != null && elements.item(0) != null && elements.item(0).getTextContent() != null;
        }

        private boolean errorCodeIsValid(final String errorCode) {
            return !(errorCode.isEmpty() || errorCode.charAt(0) != 'V');
        }

        static class PlogMessage {
            private String file = StringUtils.EMPTY;
            private int lineNumber;
            private String errorCode = StringUtils.EMPTY;
            private String message = StringUtils.EMPTY;
            private String level = StringUtils.EMPTY;

            public String getHash() {
                return errorCode + message + file + lineNumber;
            }

            @Override
            public String toString() {
                return message;
            }

            String getFilePath() {
                return file;
            }

            public int getLine() {
                return lineNumber;
            }

            public String getType() {
                return errorCode;
            }

            public String getLevel() {
                return level;
            }
        }
    }

    /**
     * The AnalyzerType for PVS-Studio static analyzer.
     *
     * @author PVS-Studio Team
     */
    static final class AnalyzerType {
        /**
         * Diagnosis of 64-bit errors (Viva64, C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/#64CPP">...</a>
         */
        private static final int VIVA64_CCPP_ERRORCODE_BEGIN = 100;
        /**
         * Diagnosis of 64-bit errors (Viva64, C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/#64CPP">...</a>
         */
        private static final int VIVA64_CCPP_ERRORCODE_END = 499;
        /**
         * General Analysis (C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/#GeneralAnalysisCPP">...</a>
         */
        private static final int GENERAL_CCPP_LOW_ERRORCODE_BEGIN = 500;
        /**
         * General Analysis (C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/#GeneralAnalysisCPP">...</a>
         */
        private static final int GENERAL_CCPP_LOW_ERRORCODE_END = 799;
        /**
         * Diagnosis of micro-optimizations (C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/#MicroOptimizationsCPP">...</a>
         */
        private static final int OPTIMIZATION_CCPP_ERRORCODE_BEGIN = 800;
        /**
         * Diagnosis of micro-optimizations (C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/#MicroOptimizationsCPP">...</a>
         */
        private static final int OPTIMIZATION_CCPP_ERRORCODE_END = 999;
        /**
         * General Analysis (C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/">...</a>
         */
        private static final int GENERAL_CCPP_HIGH_ERRORCODE_BEGIN = 1000;
        /**
         * General Analysis (C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/">...</a>
         */
        private static final int GENERAL_CCPP_HIGH_ERRORCODE_END = 1999;
        /**
         * Customers Specific Requests (C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/#CustomersSpecificRequestsCPP">...</a>
         */
        private static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN = 2000;
        /**
         * Customers Specific Requests (C++).
         * <a href="https://pvs-studio.com/en/docs/warnings/#CustomersSpecificRequestsCPP">...</a>
         */
        private static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_END = 2499;
        /**
         * MISRA errors.
         * <a href="https://pvs-studio.com/en/docs/warnings/#MISRA">...</a>
         */
        private static final int MISRA_CCPP_ERRORCODE_BEGIN = 2500;
        /**
         * MISRA errors.
         * <a href="https://pvs-studio.com/en/docs/warnings/#MISRA">...</a>
         */
        private static final int MISRA_CCPP_ERRORCODE_END = 2999;
        /**
         * General Analysis (C#).
         * <a href="https://pvs-studio.com/en/docs/warnings/#GeneralAnalysisCS">...</a>
         */
        private static final int GENERAL_CS_ERRORCODE_BEGIN = 3000;
        /**
         * General Analysis (C#).
         * <a href="https://pvs-studio.com/en/docs/warnings/#GeneralAnalysisCS">...</a>
         */
        private static final int GENERAL_CS_ERRORCODE_END = 3499;
        /**
         * General Analysis (Java).
         * <a href="https://pvs-studio.com/en/docs/warnings/#GeneralAnalysisJAVA">...</a>
         */
        private static final int GENERAL_JAVA_ERRORCODE_BEGIN = 6000;
        /**
         * General Analysis (Java).
         * <a href="https://pvs-studio.com/en/docs/warnings/#GeneralAnalysisJAVA">...</a>
         */
        private static final int GENERAL_JAVA_ERRORCODE_END = 6999;

        static final String VIVA_64_MESSAGE = "64-bit";
        static final String GENERAL_MESSAGE = "General Analysis";
        static final String OPTIMIZATION_MESSAGE = "Micro-optimization";
        static final String CUSTOMER_SPECIFIC_MESSAGE = "Specific Requests";
        static final String MISRA_MESSAGE = "MISRA";
        static final String UNKNOWN_MESSAGE = "Unknown";

        private AnalyzerType() {
            // prevents instantiation
        }

        private static final AnalysisType[] ANALYSIS_TYPES = {new Viva64(), new General(),
                new Optimization(), new CustomerSpecific(), new Misra()};

        static AnalysisType fromErrorCode(final String errorCodeStr) {
            if (equalsIgnoreCase(errorCodeStr, "External")) {
                return new General();
            }

            // errorCodeStr format is Vnnn.
            int errorCode = IntegerParser.parseInt(errorCodeStr.substring(1));

            return Arrays.stream(ANALYSIS_TYPES)
                    .map(type -> type.create(errorCode))
                    .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                    .findFirst()
                    .orElse(new Unknown());
        }

        /**
         * Viva64 AnalysisType.
         */
        static final class Viva64 implements AnalysisType {
            @Override
            public String getMessage() {
                return VIVA_64_MESSAGE;
            }

            @Override
            public Optional<AnalysisType> create(final int errorCode) {
                if (errorCode >= VIVA64_CCPP_ERRORCODE_BEGIN && errorCode <= VIVA64_CCPP_ERRORCODE_END) {
                    return Optional.of(new Viva64());
                }

                return Optional.empty();
            }
        }

        /**
         * GENERAL AnalysisType.
         */
        private static final class General implements AnalysisType {
            @Override
            public String getMessage() {
                return GENERAL_MESSAGE;
            }

            @Override
            public Optional<AnalysisType> create(final int errorCode) {
                if (errorCode >= GENERAL_CCPP_LOW_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_LOW_ERRORCODE_END) {
                    return Optional.of(new General());
                }
                else if (errorCode >= GENERAL_CCPP_HIGH_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_HIGH_ERRORCODE_END) {
                    return Optional.of(new General());
                }
                else if (errorCode >= GENERAL_CS_ERRORCODE_BEGIN && errorCode <= GENERAL_CS_ERRORCODE_END) {
                    return Optional.of(new General());
                }
                else if (errorCode >= GENERAL_JAVA_ERRORCODE_BEGIN && errorCode <= GENERAL_JAVA_ERRORCODE_END) {
                    return Optional.of(new General());
                }
                else {
                    return Optional.empty();
                }
            }
        }

        /**
         * OPTIMIZATION AnalysisType.
         */
        private static final class Optimization implements AnalysisType {
            @Override
            public String getMessage() {
                return OPTIMIZATION_MESSAGE;
            }

            @Override
            public Optional<AnalysisType> create(final int errorCode) {
                if (errorCode >= OPTIMIZATION_CCPP_ERRORCODE_BEGIN && errorCode <= OPTIMIZATION_CCPP_ERRORCODE_END) {
                    return Optional.of(new Optimization());
                }

                return Optional.empty();
            }
        }

        /**
         * CustomerSpecific AnalysisType.
         */
        private static final class CustomerSpecific implements AnalysisType {
            @Override
            public String getMessage() {
                return CUSTOMER_SPECIFIC_MESSAGE;
            }

            @Override
            public Optional<AnalysisType> create(final int errorCode) {
                if (errorCode >= CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN && errorCode <= CUSTOMERSPECIFIC_CCPP_ERRORCODE_END) {
                    return Optional.of(new CustomerSpecific());
                }

                return Optional.empty();
            }
        }

        /**
         * MISRA AnalysisType.
         */
        private static final class Misra implements AnalysisType {
            @Override
            public String getMessage() {
                return MISRA_MESSAGE;
            }

            @Override
            public Optional<AnalysisType> create(final int errorCode) {
                if (errorCode >= MISRA_CCPP_ERRORCODE_BEGIN && errorCode <= MISRA_CCPP_ERRORCODE_END) {
                    return Optional.of(new Misra());
                }

                return Optional.empty();
            }
        }

        /**
         * Unknown AnalysisType.
         */
        private static final class Unknown implements AnalysisType {
            @Override
            public String getMessage() {
                return UNKNOWN_MESSAGE;
            }

            @Override
            public Optional<AnalysisType> create(final int errorCode) {
                return Optional.empty();
            }
        }

        interface AnalysisType {
            String getMessage();

            Optional<AnalysisType> create(int errorCodeStr);
        }
    }
}
