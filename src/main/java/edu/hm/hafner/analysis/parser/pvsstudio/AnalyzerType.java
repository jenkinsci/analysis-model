package edu.hm.hafner.analysis.parser.pvsstudio;

/**
 * The AnalyzerType for PVS-Studio static analyzer.
 *
 * @author PVS-Studio Team
 */
public enum AnalyzerType {
    GENERAL,
    OPTIMIZATION,
    CUSTOMER_SPECIFIC,
    VIVA_64,
    MISRA,
    UNKNOWN;

    /**
     * Diagnosis of 64-bit errors (Viva64, C++).
     * https://www.viva64.com/en/w/#64CPP
     */
    public static final int VIVA64_CCPP_ERRORCODE_BEGIN = 100;
    /**
     * Diagnosis of 64-bit errors (Viva64, C++).
     * https://www.viva64.com/en/w/#64CPP
     */
    public static final int VIVA64_CCPP_ERRORCODE_END = 499;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/#GeneralAnalysisCPP
     */
    public static final int GENERAL_CCPP_LOW_ERRORCODE_BEGIN = 500;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/#GeneralAnalysisCPP
     */
    public static final int GENERAL_CCPP_LOW_ERRORCODE_END = 799;

    /**
     * Diagnosis of micro-optimizations (C++).
     * https://www.viva64.com/en/w/#MicroOptimizationsCPP
     */
    public static final int OPTIMIZATION_CCPP_ERRORCODE_BEGIN = 800;
    /**
     * Diagnosis of micro-optimizations (C++).
     * https://www.viva64.com/en/w/#MicroOptimizationsCPP
     */
    public static final int OPTIMIZATION_CCPP_ERRORCODE_END = 999;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/
     */
    public static final int GENERAL_CCPP_HIGH_ERRORCODE_BEGIN = 1000;
    /**
     * General Analysis (C++).
     * https://www.viva64.com/en/w/
     */
    public static final int GENERAL_CCPP_HIGH_ERRORCODE_END = 1999;
    /**
     * Customers Specific Requests (C++).
     * https://www.viva64.com/en/w/#CustomersSpecificRequestsCPP
     */
    public static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN = 2000;
    /**
     * Customers Specific Requests (C++).
     * https://www.viva64.com/en/w/#CustomersSpecificRequestsCPP
     */
    public static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_END = 2499;
    /**
     * MISRA errors.
     * https://www.viva64.com/en/w/#MISRA
     */
    public static final int MISRA_CCPP_ERRORCODE_BEGIN = 2500;
    /**
     * MISRA errors.
     * https://www.viva64.com/en/w/#MISRA
     */
    public static final int MISRA_CCPP_ERRORCODE_END = 2999;
    /**
     * General Analysis (C#).
     * https://www.viva64.com/en/w/#GeneralAnalysisCS
     */
    public static final int GENERAL_CS_ERRORCODE_BEGIN = 3000;
    /**
     * General Analysis (C#).
     * https://www.viva64.com/en/w/#GeneralAnalysisCS
     */
    public static final int GENERAL_CS_ERRORCODE_END = 3499;

    /**
     * General Analysis (Java).
     * https://www.viva64.com/en/w/#GeneralAnalysisJAVA
     */
    public static final int GENERAL_JAVA_ERRORCODE_BEGIN = 6000;
    /**
     * General Analysis (Java).
     * https://www.viva64.com/en/w/#GeneralAnalysisJAVA
     */
    public static final int GENERAL_JAVA_ERRORCODE_END = 6999;

    // errorCode is Vnnn
    // from DataTableConsts.cs
    /**
     * Get analayzer by error code
     */
    @SuppressWarnings("PMD")
    public static AnalyzerType getAnalyzerType(final String errorCodeStr)
    {
        if (errorCodeStr == null ||  errorCodeStr.length() <= 1)
        {
            return AnalyzerType.UNKNOWN;
        }

        try {
            String sub = errorCodeStr.substring(1);

            int errorCode = Integer.parseInt(sub);

            if (errorCode >= VIVA64_CCPP_ERRORCODE_BEGIN && errorCode <= VIVA64_CCPP_ERRORCODE_END) {
                return AnalyzerType.VIVA_64;
            }
            else if (errorCode >= GENERAL_CCPP_LOW_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_LOW_ERRORCODE_END) {
                return AnalyzerType.GENERAL;
            }
            else if (errorCode >= OPTIMIZATION_CCPP_ERRORCODE_BEGIN && errorCode <= OPTIMIZATION_CCPP_ERRORCODE_END) {
                return AnalyzerType.OPTIMIZATION;
            }
            else if (errorCode >= GENERAL_CCPP_HIGH_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_HIGH_ERRORCODE_END) {
                return AnalyzerType.GENERAL;
            }
            else if (errorCode >= CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN && errorCode <= CUSTOMERSPECIFIC_CCPP_ERRORCODE_END) {
                return AnalyzerType.CUSTOMER_SPECIFIC;
            }
            else if (errorCode >= MISRA_CCPP_ERRORCODE_BEGIN && errorCode <= MISRA_CCPP_ERRORCODE_END) {
                return AnalyzerType.MISRA;
            }
            else if (errorCode >= GENERAL_CS_ERRORCODE_BEGIN && errorCode <= GENERAL_CS_ERRORCODE_END) {
                return AnalyzerType.GENERAL;
            }
            else if (errorCode >= GENERAL_JAVA_ERRORCODE_BEGIN && errorCode <= GENERAL_JAVA_ERRORCODE_END) {
                return AnalyzerType.GENERAL;
            }
        }
        catch (StringIndexOutOfBoundsException | NumberFormatException ex) {
            if ("External".equalsIgnoreCase(errorCodeStr)) {
                return AnalyzerType.GENERAL;
            }
        }

        return AnalyzerType.UNKNOWN;
    }
}

