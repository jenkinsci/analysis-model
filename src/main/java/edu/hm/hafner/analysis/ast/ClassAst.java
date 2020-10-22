package edu.hm.hafner.analysis.ast;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Creates the abstract syntax tree for the whole class except inner types.
 *
 * @author Christian Möstl
 */
public class ClassAst extends Ast {
    private final int[] excludeTypes = {TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.ENUM_DEF,
            TokenTypes.ANNOTATION_DEF, TokenTypes.IMPORT, TokenTypes.PACKAGE_DEF};

    /**
     * Creates a new instance of {@link ClassAst}.
     *
     * @param fileName
     *         the name of the Java file
     * @param lineNumber
     *         the line number that contains the warning
     */
    public ClassAst(final String fileName, final int lineNumber) {
        super(fileName, lineNumber);
    }

    @Override
    public List<DetailAST> chooseArea() {
        List<DetailAST> line = getElementsNearAffectedLine();
        List<DetailAST> chosenArea = new ArrayList<>();
        DetailAST classAst = getRootOfClass(line.get(0));
        if (classAst != null) {
            chosenArea.add(classAst);
            chosenArea.addAll(getAllButNestedElements(classAst.getFirstChild()));
        }

        return chosenArea;
    }

    private List<DetailAST> getAllButNestedElements(final DetailAST start) {
        List<DetailAST> chosenArea = new ArrayList<>();
        if (!ArrayUtils.contains(excludeTypes, start.getType())) {
            chosenArea.add(start);
            DetailAST child = start.getFirstChild();
            if (child != null) {
                chosenArea.addAll(getAllButNestedElements(child));
            }
        }
        DetailAST sibling = start.getNextSibling();
        if (sibling != null) {
            chosenArea.addAll(getAllButNestedElements(sibling));
        }
        return chosenArea;
    }

    private DetailAST getRootOfClass(final DetailAST elementInCLass) {
        if (elementInCLass.getType() == TokenTypes.CLASS_DEF) {
            return elementInCLass;
        }
        else if (elementInCLass.getParent() != null) {
            return getRootOfClass(elementInCLass.getParent());
        }
        else {
            return null;
        }
    }
}
