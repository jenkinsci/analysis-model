package edu.hm.hafner.analysis.parser.pvsstudio;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import edu.hm.hafner.util.IntegerParser;

class AnalyzerType {
    /**
     * Diagnosis of 64-bit errors (Viva64, C++).
     * https://www.viva64.com/en/w/#64CPP
     */
    private static final int VIVA64_CCPP_ERRORCODE_BEGIN = 100;
    /**
     * Diagnosis of 64-bit errors (Viva64, C++).
     * https://www.viva64.com/en/w/#64CPP
     */
    private static final int VIVA64_CCPP_ERRORCODE_END = 499;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/#GeneralAnalysisCPP
     */
    private static final int GENERAL_CCPP_LOW_ERRORCODE_BEGIN = 500;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/#GeneralAnalysisCPP
     */
    private static final int GENERAL_CCPP_LOW_ERRORCODE_END = 799;

    /**
     * Diagnosis of micro-optimizations (C++).
     * https://www.viva64.com/en/w/#MicroOptimizationsCPP
     */
    private static final int OPTIMIZATION_CCPP_ERRORCODE_BEGIN = 800;
    /**
     * Diagnosis of micro-optimizations (C++).
     * https://www.viva64.com/en/w/#MicroOptimizationsCPP
     */
    private static final int OPTIMIZATION_CCPP_ERRORCODE_END = 999;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/
     */
    private static final int GENERAL_CCPP_HIGH_ERRORCODE_BEGIN = 1000;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/
     */
    private static final int GENERAL_CCPP_HIGH_ERRORCODE_END = 1999;
    /**
     * Customers Specific Requests (C++).
     * https://www.viva64.com/en/w/#CustomersSpecificRequestsCPP
     */
    private static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN = 2000;
    /**
     * Customers Specific Requests (C++).
     * https://www.viva64.com/en/w/#CustomersSpecificRequestsCPP
     */
    private static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_END = 2499;
    /**
     * MISRA errors.
     * https://www.viva64.com/en/w/#MISRA
     */
    private static final int MISRA_CCPP_ERRORCODE_BEGIN = 2500;
    /**
     * MISRA errors.
     * https://www.viva64.com/en/w/#MISRA
     */
    private static final int MISRA_CCPP_ERRORCODE_END = 2999;
    /**
     * General Analysis (C#).
     * https://www.viva64.com/en/w/#GeneralAnalysisCS
     */
    private static final int GENERAL_CS_ERRORCODE_BEGIN = 3000;
    /**
     * General Analysis (C#).
     * https://www.viva64.com/en/w/#GeneralAnalysisCS
     */
    private static final int GENERAL_CS_ERRORCODE_END = 3499;
    /**
     * General Analysis (Java).
     * https://www.viva64.com/en/w/#GeneralAnalysisJAVA
     */
    private static final int GENERAL_JAVA_ERRORCODE_BEGIN = 6000;
    /**
     * General Analysis (Java).
     * https://www.viva64.com/en/w/#GeneralAnalysisJAVA
     */
    private static final int GENERAL_JAVA_ERRORCODE_END = 6999;

    private AnalyzerType() {}

    private final static ArrayList<AnalysisType> allTypes = new ArrayList<>();
    static {
        allTypes.add(new VIVA_64());
        allTypes.add(new GENERAL());
        allTypes.add(new OPTIMIZATION());
        allTypes.add(new CUSTOMER_SPECIFIC());
        allTypes.add(new MISRA());
    }
    /**
     * errorCodeStr format is Vnnn.
     */
    static AnalysisType fromErrorCode(final String errorCodeStr) {

        if("External".equalsIgnoreCase(errorCodeStr)) {
            return new GENERAL();
        }

        int errorCode = IntegerParser.parseInt(errorCodeStr.substring(1));

        return allTypes.stream().map(type -> type.create(errorCode)).flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .findFirst()
                .orElse(new UNKNOWN());
    }

    static class VIVA_64 implements AnalysisType {

        private VIVA_64() {}

        @Override
        public String getMessage() {
            return "64-bit";
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= VIVA64_CCPP_ERRORCODE_BEGIN && errorCode <= VIVA64_CCPP_ERRORCODE_END) {
                return Optional.of(new VIVA_64());
            }

            return Optional.empty();
        }
    }

    static class GENERAL implements AnalysisType {

        private GENERAL() {}

        @Override
        public String getMessage() {
            return "General Analysis";
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if ( (errorCode >= GENERAL_CCPP_LOW_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_LOW_ERRORCODE_END)
                    || (errorCode >= GENERAL_CCPP_HIGH_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_HIGH_ERRORCODE_END)
                    || (errorCode >= GENERAL_CS_ERRORCODE_BEGIN && errorCode <= GENERAL_CS_ERRORCODE_END)
                    || (errorCode >= GENERAL_JAVA_ERRORCODE_BEGIN && errorCode <= GENERAL_JAVA_ERRORCODE_END)) {

                return Optional.of(new GENERAL());
            }

            return Optional.empty();
        }
    }

    static class OPTIMIZATION implements AnalysisType {

        private OPTIMIZATION() {}

        @Override
        public String getMessage() {
            return "Micro-optimization";
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= OPTIMIZATION_CCPP_ERRORCODE_BEGIN && errorCode <= OPTIMIZATION_CCPP_ERRORCODE_END) {
                return Optional.of(new OPTIMIZATION());
            }

            return Optional.empty();
        }
    }

    static class CUSTOMER_SPECIFIC implements AnalysisType {

        private CUSTOMER_SPECIFIC() {}

        @Override
        public String getMessage() {
            return "Specific Requests";
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN && errorCode <= CUSTOMERSPECIFIC_CCPP_ERRORCODE_END) {
                return Optional.of(new CUSTOMER_SPECIFIC());
            }

            return Optional.empty();
        }
    }
    static class MISRA implements AnalysisType {

        private MISRA() {}

        @Override
        public String getMessage() {
            return "MISRA";
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {

            if (errorCode >= MISRA_CCPP_ERRORCODE_BEGIN && errorCode <= MISRA_CCPP_ERRORCODE_END) {
                return Optional.of(new MISRA());
            }

            return Optional.empty();
        }
    }

    static class UNKNOWN implements AnalysisType {

        private UNKNOWN() {}

        @Override
        public String getMessage() {
            return "Unknown";
        }

        @Override
        public Optional<AnalysisType> create(final int errorCode) {
            return Optional.of(new UNKNOWN());
        }
    }
}
