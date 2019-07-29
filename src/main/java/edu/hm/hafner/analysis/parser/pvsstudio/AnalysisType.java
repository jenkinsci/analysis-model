package edu.hm.hafner.analysis.parser.pvsstudio;

import java.util.Optional;

interface AnalysisType {
    String getMessage();

    Optional<AnalysisType> create(int errorCodeStr);
}
