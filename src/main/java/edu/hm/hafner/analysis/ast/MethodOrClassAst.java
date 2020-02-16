package edu.hm.hafner.analysis.ast;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Extracts the abstract syntax tree either of the affected method or whole class. If the warning is within a method
 * body, then the AST is computed by an instance of {@link MethodAst}. Otherwise the AST of the whole class is used (see
 * {@link ClassAst}.
 *
 * @author Christian Möstl
 */
public class MethodOrClassAst extends Ast {
    private static final int[] NO_METHOD_TYPES
            = {TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.ENUM_DEF, TokenTypes.ANNOTATION_DEF};

    /**
     * Creates a new instance of {@link MethodOrClassAst}.
     *
     * @param fileName
     *         the name of the Java file
     * @param lineNumber
     *         the line number that contains the warning
     */
    public MethodOrClassAst(final String fileName, final int lineNumber) {
        super(fileName, lineNumber);
    }

    @Override
    public List<DetailAST> chooseArea() {
        List<DetailAST> elementsInCurrentLine = getElementsInSameLine();
        if (elementsInCurrentLine.isEmpty()) {
            elementsInCurrentLine = findPreviousElements(getLineNumber());
        }
        if (!elementsInCurrentLine.isEmpty() && isLevelOfMethod(elementsInCurrentLine.get(0))) {
            return new MethodAst(getFileName(), getLineNumber()).chooseArea();
        }
        else {
            return new ClassAst(getFileName(), getLineNumber()).chooseArea();
        }
    }

    private boolean isLevelOfMethod(final DetailAST element) {
        if (element != null) {
            if (element.getType() == TokenTypes.METHOD_DEF || element.getType() == TokenTypes.CTOR_DEF) {
                return true;
            }
            else if (ArrayUtils.contains(NO_METHOD_TYPES, element.getType())) {
                return false;
            }
            else {
                return isLevelOfMethod(element.getParent());
            }
        }
        return false;
    }
}
