package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.QtTranslationParser;

/**
 * A descriptor for translation files of Qt.
 *
 * @author Lorenz Munsch
 */
class QtTranslationDescriptor extends ParserDescriptor {
    private static final String ID = "qt-translation";
    private static final String NAME = "Qt translations";

    QtTranslationDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new QtTranslationParser();
    }

    @Override
    public String getHelp() {
        return "Reads translation files of Qt, which are created by \"lupdate\" or \"Linguist\".";
    }

    @Override
    public String getUrl() {
        return "https://www.qt.io";
    }
}
