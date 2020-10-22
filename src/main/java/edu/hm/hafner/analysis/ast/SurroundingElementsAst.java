package edu.hm.hafner.analysis.ast;

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * Extracts the abstract syntax tree of the surrounding elements. It takes a specific number of lines before and after
 * the actual warning.
 *
 * @author Christian Möstl
 */
public class SurroundingElementsAst extends Ast {
    /** Number of lines before and after current line to consider. */
    private static final int LINES_LOOK_AHEAD = 3;

    private final int surroundingLines;

    /**
     * Creates a new instance of {@link SurroundingElementsAst}. Uses the predefined number of lines ({@link
     * #LINES_LOOK_AHEAD} as look ahead.
     *
     * @param fileName
     *         the name of the Java file
     * @param lineNumber
     *         the line number that contains the warning
     */
    public SurroundingElementsAst(final String fileName, final int lineNumber) {
        this(fileName, lineNumber, LINES_LOOK_AHEAD);
    }

    /**
     * Creates a new instance of {@link SurroundingElementsAst}.
     *
     * @param fileName
     *         the name of the Java file
     * @param lineNumber
     *         the line number that contains the warning
     * @param surroundingLines
     *         the number of lines before and after the affected line
     */
    public SurroundingElementsAst(final String fileName, final int lineNumber, final int surroundingLines) {
        super(fileName, lineNumber);

        this.surroundingLines = surroundingLines;
    }

    @Override
    public List<DetailAST> chooseArea() {
        List<DetailAST> chosen = new ArrayList<>();
        chosen.addAll(getElementsInSameLine());
        chosen.addAll(getElementsBeforeAffectedLine());
        chosen.addAll(getElementsAfterAffectedLine());

        return chosen;
    }

    private List<DetailAST> getElementsBeforeAffectedLine() {
        return getSurroundingElements(true);
    }

    private List<DetailAST> getElementsAfterAffectedLine() {
        return getSurroundingElements(false);
    }

    private List<DetailAST> getSurroundingElements(final boolean isMovingUp) {
        DetailAST completeAst = getRoot();

        List<DetailAST> environment = new ArrayList<>();

        int limit;
        int startLine = getLineNumber();
        if (isMovingUp) {
            limit = startLine;
        }
        else {
            limit = getLastLineNumber() - startLine + 1;
        }

        int nextLine;
        int counter = 0;
        for (int i = 1; i < limit; i++) {
            if (counter < surroundingLines) {
                if (isMovingUp) {
                    nextLine = startLine - i;
                }
                else {
                    nextLine = startLine + i;
                }
                List<DetailAST> elementsInCurrentLine = new ArrayList<>();
                collectElementsOfLine(completeAst, nextLine, elementsInCurrentLine);
                if (!elementsInCurrentLine.isEmpty()) {
                    counter++;
                    environment.addAll(elementsInCurrentLine);
                }
            }
            else {
                break;
            }
        }

        return environment;
    }
}
