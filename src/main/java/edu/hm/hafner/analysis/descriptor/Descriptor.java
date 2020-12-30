package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.IssueParser;

public interface Descriptor {

    String getName();

    IssueParser createParser();

    String getPattern();

    String getUrl();

}
