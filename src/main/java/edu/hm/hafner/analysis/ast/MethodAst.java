package edu.hm.hafner.analysis.ast;

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Extracts the abstract syntax tree of the method that contains the warning.
 *
 * @author Christian Möstl
 */
public class MethodAst extends Ast {
    /**
     * Creates a new instance of {@link MethodAst}.
     *
     * @param fileName
     *         the name of the Java file
     * @param lineNumber
     *         the line number that contains the warning
     */
    public MethodAst(final String fileName, final int lineNumber) {
        super(fileName, lineNumber);
    }

    @Override
    public List<DetailAST> chooseArea() {
        List<DetailAST> chosenArea = new ArrayList<>(getElementsInSameLine());
        DetailAST methodStart = findMethodStart();
        chosenArea.add(methodStart);
        chosenArea.addAll(calcAllChildren(methodStart.getFirstChild()));
        return chosenArea;
    }

    private DetailAST findMethodStart() {
        List<DetailAST> elements = getElementsNearAffectedLine();

        DetailAST root = null;
        if (!elements.isEmpty()) {
            root = getRootOfMethod(elements.get(0));
            if (root == null) {
                root = findNextElements(getLineNumber()).get(0);
            }
        }
        if (root == null) {
            return getRoot();
        }
        else {
            return root;
        }
    }

    private DetailAST getRootOfMethod(final DetailAST elementInMethod) {
        if (elementInMethod.getType() == TokenTypes.METHOD_DEF || elementInMethod.getType() == TokenTypes.CTOR_DEF) {
            return elementInMethod;
        }
        else if (elementInMethod.getParent() != null) {
            return getRootOfMethod(elementInMethod.getParent());
        }
        else {
            return null;
        }
    }
}
