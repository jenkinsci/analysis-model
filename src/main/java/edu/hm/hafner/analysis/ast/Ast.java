package edu.hm.hafner.analysis.ast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.puppycrawl.tools.checkstyle.JavaParser;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.TokenUtil;

/**
 * Base class for the Abstract Syntax Tree (AST) of the Java files containing a warning.
 *
 * @author Christian Möstl
 */
public abstract class Ast {
    private static final Sha1ToLongConverter SHA_1_TO_LONG_CONVERTER = new Sha1ToLongConverter();
    private static final String HASH_ALGORITHM = "SHA-1";

    private final int lineNumber;
    private final DetailAST root;
    private final String fileName;

    private final List<DetailAST> elementsInSameLine = new ArrayList<>();

    private List<DetailAST> children = new ArrayList<>();

    private final List<DetailAST> allElements = new ArrayList<>();

    private static final String DELIMITER = " ";
    private static final String CHARSET = "UTF-8"; // FIXME: use user value

    /**
     * Creates a new instance of {@link Ast}.
     *
     * @param fileName
     *         the name of the Java file
     * @param lineNumber
     *         the line number that contains the warning
     */
    // FIXME: check if we need to provide the line range?
    public Ast(final String fileName, final int lineNumber) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;

        root = createAst(fileName);

        collectElementsOfLine(root, lineNumber, elementsInSameLine);
        calcConstants(root);
    }

    @Override
    public String toString() {
        return chosenAreaAsString(' ');
    }

    /**
     * Returns the primary line number of the warning.
     *
     * @return the line number of the warning
     */
    protected int getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns the root of the AST.
     *
     * @return the root of the AST
     */
    public DetailAST getRoot() {
        return root;
    }

    /**
     * Returns the filename.
     *
     * @return the filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the elementsInSameLine.
     *
     * @return the elementsInSameLine
     */
    public List<DetailAST> getElementsInSameLine() {
        return elementsInSameLine;
    }

    /**
     * Returns the allElements.
     *
     * @return the allElements
     */
    public List<DetailAST> getAllElements() {
        return allElements;
    }

    /**
     * Calculates all children of the given AST-element.
     *
     * @param start
     *         the root of the ast
     *
     * @return all children of the given AST-element
     */
    public List<DetailAST> calcAllChildren(final DetailAST start) {
        if (start != null) {
            children.add(start);
            if (start.getFirstChild() != null) {
                calcAllChildren(start.getFirstChild());
            }
            if (start.getNextSibling() != null) {
                calcAllChildren(start.getNextSibling());
            }
        }
        return children;
    }

    /**
     * Clears the children.
     */
    public void clear() {
        children.clear();
    }

    /**
     * Returns the children.
     *
     * @return the children
     */
    public List<DetailAST> getChildren() {
        return children;
    }

    /**
     * Creates the DetailAST of the specified Java-source-file.
     *
     * @param file
     *         the filename
     *
     * @return the DetailAST
     */
    private DetailAST createAst(final String file) {
        try {
            return JavaParser.parse(new FileContents(new FileText(new File(file), CHARSET)));
        }
        catch (IOException | CheckstyleException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    /**
     * Runs through the AST and collects all elements that are part of the selected line.
     *
     * @param node
     *         the node of the AST to start with
     * @param line
     *         the line where the elements are picked from
     * @param elements
     *         the elements of the selected line
     */
    protected void collectElementsOfLine(final DetailAST node, final int line, final List<DetailAST> elements) {
        if (node != null) {
            if (node.getLineNo() == line) {
                elements.add(node);
            }
            if (node.getFirstChild() != null) {
                collectElementsOfLine(node.getFirstChild(), line, elements);
            }
            if (node.getNextSibling() != null) {
                collectElementsOfLine(node.getNextSibling(), line, elements);
            }
        }
    }

    /**
     * Runs entirely through the AST.
     *
     * @param node
     *         Expects the root of the AST which is run through
     */
    public void runThroughAST(final DetailAST node) {
        if (node != null) {
            allElements.add(node);
            isConstant(node);

            if (node.getFirstChild() != null) {
                runThroughAST(node.getFirstChild());
            }
            if (node.getNextSibling() != null) {
                runThroughAST(node.getNextSibling());
            }
        }
    }

    /**
     * Runs entirely through the AST.
     *
     * @param node
     *         Expects the root of the AST which is run through
     */
    public void calcConstants(final DetailAST node) {
        if (node != null) {
            isConstant(node);

            if (node.getFirstChild() != null) {
                calcConstants(node.getFirstChild());
            }
            if (node.getNextSibling() != null) {
                calcConstants(node.getNextSibling());
            }
        }
    }

    /**
     * Choose the Area around the warning.
     *
     * @return the Area
     */
    public abstract List<DetailAST> chooseArea();

    /**
     * Depicts the result of chooseArea() as a string.
     *
     * @param delimiter
     *         the delimiter between the ast-elements. If delimiter is equal z, then no delimiter would be inserted.
     *
     * @return the result in string-format
     */
    public String chosenAreaAsString(final char delimiter) {
        StringBuilder stringBuilder = new StringBuilder();

        for (DetailAST detailAST : chooseArea()) {
            stringBuilder.append(TokenUtil.getTokenName(detailAST.getType()));
            stringBuilder.append(delimiter);
        }

        return stringBuilder.toString();
    }

    /** Necessary for ASTs with name. */
    private String name = "";

    /**
     * Sets the name to the specified value.
     *
     * @param name
     *         the value to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the hash code of the selected part of the AST. The hash code is derived from the SHA1 digest.
     *
     * @return hash code of the AST
     */
    public long getContextHashCode() {
        return SHA_1_TO_LONG_CONVERTER.toLong(getDigest());
    }

    /**
     * Returns a digest of the selected part of the AST.
     *
     * @return the digest of the AST, represented as SHA1 hash code
     */
    public String getDigest() {
        List<DetailAST> elements = chooseArea();
        StringBuilder astElements = new StringBuilder();
        children.clear();
        for (DetailAST astElement : elements) {
            int type = astElement.getType();

            if (type == TokenTypes.TYPE) {
                children = calcAllChildren(astElement.getFirstChild());
                for (DetailAST child : children) {
                    astElements.append(child.getText());
                    astElements.append(DELIMITER);
                }
                children.clear();
            }

            boolean lockedNextElement = false;
            if (!constants.isEmpty()) {
                for (DetailAST ast : constants.keySet()) {
                    if (ast.getType() == TokenTypes.IDENT && ast.getText().equals(astElement.getText())) {
                        astElements.append(TokenUtil.getTokenName(constants.get(ast).getType()));
                        astElements.append(DELIMITER);
                        lockedNextElement = true;
                    }
                }
            }
            if (type != TokenTypes.TYPE && !lockedNextElement) {
                astElements.append(TokenUtil.getTokenName(type));
                astElements.append(DELIMITER);
            }
        }

        if (getName() != null) {
            astElements.append(name);
        }

        return createHashCodeFromAst(astElements);
    }

    private String createHashCodeFromAst(final StringBuilder astElements) {
        MessageDigest messageDigest = createMessageDigest();
        byte[] digest = messageDigest.digest(astElements.toString().getBytes(Charset.forName(CHARSET)));

        StringBuilder result = new StringBuilder();
        for (byte b : digest) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance(HASH_ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Requested algorithm not found: " + HASH_ALGORITHM);
        }
    }

    private DetailAST objBlock;

    /**
     * Returns the objBlock of the abstract syntax tree.
     *
     * @param topRoot
     *         the highest root of the ast
     *
     * @return the objblock
     */
    protected DetailAST getObjBlock(final DetailAST topRoot) {
        calcObjBlock(topRoot, 0);

        return objBlock;
    }

    /**
     * Calculates the OBJBLOCK of the ast.
     *
     * @param topRoot
     *         the highest root of the ast
     * @param start
     *         which OBJBLOCK should be found? For example if you want have the first OBJBLOCK, you have to set the
     *         counter to 0.
     */
    protected void calcObjBlock(final DetailAST topRoot, final int start) {
        if (topRoot != null) {
            int counter = start;
            if (topRoot.getType() == TokenTypes.OBJBLOCK && counter == 0) {
                objBlock = topRoot;
                counter++;
            }
            if (topRoot.getFirstChild() != null) {
                calcObjBlock(topRoot.getFirstChild(), counter);
            }
            if (topRoot.getNextSibling() != null) {
                calcObjBlock(topRoot.getNextSibling(), counter);
            }
        }
    }

    /**
     * Calculates the last sibling of the given ast-element.
     *
     * @param element
     *         the current element in the ast
     *
     * @return the last element.
     */
    protected DetailAST getLastSibling(final DetailAST element) {
        if (element.getNextSibling() != null) {
            return getLastSibling(element.getNextSibling());
        }
        return element;
    }

    /**
     * Calculates the last line number of the abstract syntax tree.
     *
     * @return Returns the last linenumber of the ast.
     */
    public int getLastLineNumber() {
        return getLastSibling(getObjBlock(root).getFirstChild()).getLineNo();
    }

    // Map<nameOfConstant, value>
    private final Map<DetailAST, DetailAST> constants = new HashMap<>();

    /**
     * Returns {@code true}, if the element is a constant, otherwise returns {@code false}.
     *
     * @param element
     *         the element
     *
     * @return {@code true}, if the element is a constant, otherwise returns {@code false}.
     */
    protected boolean isConstant(final DetailAST element) {
        if (element.getType() != TokenTypes.VARIABLE_DEF) {
            return false;
        }
        else if (element.getChildCount() != 5) {
            return false;
        }

        DetailAST firstChild = element.getFirstChild();
        if (firstChild.getType() != TokenTypes.MODIFIERS || firstChild.getChildCount() != 3) {
            return false;
        }
        DetailAST type = firstChild.getNextSibling();
        if (type.getType() != TokenTypes.TYPE) {
            return false;
        }
        DetailAST ident = type.getNextSibling();
        if (ident.getType() != TokenTypes.IDENT) {
            return false;
        }
        DetailAST assign = ident.getNextSibling();
        if (assign.getType() != TokenTypes.ASSIGN) {
            return false;
        }
        DetailAST semi = assign.getNextSibling();
        if (semi.getType() != TokenTypes.SEMI) {
            return false;
        }

        constants.put(ident, assign.getFirstChild().getFirstChild());

        return true;
    }

    /**
     * Returns the constants.
     *
     * @return the constants
     */
    protected Map<DetailAST, DetailAST> getConstants() {
        return constants;
    }

    /**
     * Returns the abstract syntax tree of the elements in a non blank line that is located right before the specified
     * line.
     *
     * @param maxLineNumber
     *         the maximum line number
     *
     * @return the elements of the previous non-blank line
     */
    protected List<DetailAST> findPreviousElements(final int maxLineNumber) {
        List<DetailAST> elements = new ArrayList<>();
        int currentLine = maxLineNumber - 1;
        while (elements.isEmpty() && currentLine >= 0) {
            collectElementsOfLine(getRoot(), currentLine, elements);

            currentLine--;
        }
        return elements;
    }

    /**
     * Returns the abstract syntax tree of the elements in a non blank line that is located right after the specified
     * line.
     *
     * @param maxLineNumber
     *         the maximum line number
     *
     * @return the elements of the next non-blank line
     */
    protected List<DetailAST> findNextElements(final int maxLineNumber) {
        List<DetailAST> elements = new ArrayList<>();
        int currentLine = maxLineNumber + 1;
        while (elements.isEmpty() && currentLine <= getLastLineNumber()) {
            collectElementsOfLine(getRoot(), currentLine, elements);

            currentLine++;
        }
        return elements;
    }

    /**
     * Returns the AST elements in the affected line (or if this line does not contain statements), above or before that
     * line.
     *
     * @return the AST elements in the affected line
     */
    protected List<DetailAST> getElementsNearAffectedLine() {
        List<DetailAST> elements = getElementsInSameLine();
        if (elements.isEmpty()) {
            elements = findNextElements(getLineNumber());
        }
        if (elements.isEmpty()) {
            elements = findPreviousElements(getLineNumber());
        }
        return elements;
    }
}
