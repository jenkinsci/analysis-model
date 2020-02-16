package edu.hm.hafner.analysis.ast;

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Depicts the elements from the abstract syntax tree which are instance variables (i.e., fields).
 *
 * @author Christian Möstl
 */
public class FieldsAst extends Ast {
    /**
     * Creates a new instance of {@link FieldsAst}.
     *
     * @param fileName
     *         the name of the Java file
     * @param lineNumber
     *         the line number that contains the warning
     */
    public FieldsAst(final String fileName, final int lineNumber) {
        super(fileName, lineNumber);
    }

    private final List<DetailAST> instanceVariables = new ArrayList<>();

    private void getInstanceVariables(final DetailAST element) {
        if (element != null) {
            if (element.getType() == TokenTypes.VARIABLE_DEF) {
                instanceVariables.add(element);
            }
            if (element.getNextSibling() != null) {
                getInstanceVariables(element.getNextSibling());
            }
        }
    }

    @Override
    public List<DetailAST> chooseArea() {
        List<DetailAST> elementsInSameLine = getElementsInSameLine();
        List<DetailAST> chosenArea = new ArrayList<>();

        if (!elementsInSameLine.isEmpty()) {
            DetailAST objBlock = getObjBlockAsParent(elementsInSameLine.get(0));
            getInstanceVariables(objBlock.getFirstChild());

            chosenArea.add(objBlock);
            for (DetailAST instanceVariable : instanceVariables) {
                clear();
                if (!isConstant(instanceVariable)) {
                    chosenArea.add(instanceVariable);
                    chosenArea.addAll(calcAllChildren(instanceVariable.getFirstChild()));
                }
            }
        }

        return chosenArea;
    }

    private DetailAST getObjBlockAsParent(final DetailAST ast) {
        if (ast.getType() == TokenTypes.OBJBLOCK) {
            return ast;
        }
        else {
            return getObjBlockAsParent(ast.getParent());
        }
    }
}
