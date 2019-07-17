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

    public static final int VIVA64_CCPP_ERRORCODE_BEGIN = 100;
    public static final int VIVA64_CCPP_ERRORCODE_END = 499;

    public static final int GENERAL_CCPP_LOW_ERRORCODE_BEGIN = 500;
    public static final int GENERAL_CCPP_LOW_ERRORCODE_END = 799;

    public static final int OPTIMIZATION_CCPP_ERRORCODE_BEGIN = 800;
    public static final int OPTIMIZATION_CCPP_ERRORCODE_END = 999;

    public static final int GENERAL_CCPP_HIGH_ERRORCODE_BEGIN = 1000;
    public static final int GENERAL_CCPP_HIGH_ERRORCODE_END = 1999;

    public static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN = 2000;
    public static final int CUSTOMERSPECIFIC_CCPP_ERRORCODE_END = 2499;

    public static final int MISRA_CCPP_ERRORCODE_BEGIN = 2500;
    public static final int MISRA_CCPP_ERRORCODE_END = 2999;

    public static final int GENERAL_CS_ERRORCODE_BEGIN = 3000;
    public static final int GENERAL_CS_ERRORCODE_END = 3499;

    public static final int GENERAL_JAVA_ERRORCODE_BEGIN = 6000;
    public static final int GENERAL_JAVA_ERRORCODE_END = 6999;

    // errorCode is Vnnn
    // from DataTableConsts.cs
    public static AnalyzerType getAnalyzerType(final String errorCodeStr)
    {
        if (errorCodeStr == null ||  errorCodeStr.length() <= 1)
        {
            return AnalyzerType.UNKNOWN;
        }

        try {
            String sub = errorCodeStr.substring(1);

            int errorCode = Integer.parseInt(sub);

            if (errorCode >= VIVA64_CCPP_ERRORCODE_BEGIN && errorCode <= VIVA64_CCPP_ERRORCODE_END)
            {
                return AnalyzerType.VIVA_64;
            }
            else if (errorCode >= GENERAL_CCPP_LOW_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_LOW_ERRORCODE_END)
            {
                return AnalyzerType.GENERAL;
            }
            else if (errorCode >= OPTIMIZATION_CCPP_ERRORCODE_BEGIN && errorCode <= OPTIMIZATION_CCPP_ERRORCODE_END)
            {
                return AnalyzerType.OPTIMIZATION;
            }
            else if (errorCode >= GENERAL_CCPP_HIGH_ERRORCODE_BEGIN && errorCode <= GENERAL_CCPP_HIGH_ERRORCODE_END)
            {
                return AnalyzerType.GENERAL;
            }
            else if (errorCode >= CUSTOMERSPECIFIC_CCPP_ERRORCODE_BEGIN && errorCode <= CUSTOMERSPECIFIC_CCPP_ERRORCODE_END)
            {
                return AnalyzerType.CUSTOMER_SPECIFIC;
            }
            else if (errorCode >= MISRA_CCPP_ERRORCODE_BEGIN && errorCode <= MISRA_CCPP_ERRORCODE_END)
            {
                return AnalyzerType.MISRA;
            }
            else if (errorCode >= GENERAL_CS_ERRORCODE_BEGIN && errorCode <= GENERAL_CS_ERRORCODE_END)
            {
                return AnalyzerType.GENERAL;
            }
            else if (errorCode >= GENERAL_JAVA_ERRORCODE_BEGIN && errorCode <= GENERAL_JAVA_ERRORCODE_END)
            {
                return AnalyzerType.GENERAL;
            }
        }
        catch (Exception ex)
        {
            if ("External".equalsIgnoreCase(errorCodeStr)) {
                return AnalyzerType.GENERAL;
            }
        }

        return AnalyzerType.UNKNOWN;
    }
}

