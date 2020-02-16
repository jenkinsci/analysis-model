package edu.hm.hafner.analysis.ast;

import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Creates the abstract syntax tree (AST).
 *
 * @author Christian Möstl
 */
public class NameMethodAst extends Ast {
    private DetailAST ident;

    /**
     * Creates a new instance of {@link NameMethodAst}.
     *
     * @param fileName
     *         the name of the Java file
     * @param lineNumber
     *         the line number that contains the warning
     */
    public NameMethodAst(final String fileName, final int lineNumber) {
        super(fileName, lineNumber);
    }

    @Override
    public List<DetailAST> chooseArea() {
        Ast ast = new MethodAst(getFileName(), getLineNumber());
        List<DetailAST> chosenArea = ast.chooseArea();
        DetailAST first = chosenArea.get(0).getFirstChild();

        calcMethodName(first, 0);

        ast.setName(ident.getText());

        return chosenArea;
    }

    private void calcMethodName(final DetailAST method, final int start) {
        if (method != null) {
            int counter = start;
            if (method.getType() == TokenTypes.IDENT && counter == 0) {
                ident = method;
                counter++;
            }
            if (method.getFirstChild() != null) {
                calcMethodName(method.getFirstChild(), counter);
            }
            if (method.getNextSibling() != null) {
                calcMethodName(method.getNextSibling(), counter);
            }
        }
    }
}
