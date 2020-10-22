package edu.hm.hafner.analysis.moss;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import edu.hm.hafner.analysis.ast.MethodAst;

/**
 * Computes the text of the method for the given warning.
 */
public class WarningText {
    private static final int BUFFER_SIZE = 1000;
    String [] keywords = {
            "abstract", "continue",     "for",          "new",          "switch",
            "assert",   "default",      "if",           "package",      "synchronized",
            "boolean",  "do",           "goto",         "private",      "this",
            "break",    "double",       "implements",   "protected",    "throw",
            "byte",     "else",         "import",       "public",       "throws",
            "case",     "enum",         "instanceof",   "return",       "transient",
            "catch",    "extends",      "int",          "short",        "try",
            "char",     "final",        "interface",    "static",       "void",
            "class",    "finally",      "long",         "strictfp",     "volatile",
            "const",    "float",        "native",       "super",        "while",
            // literals
            "null",     "true",         "false"
    };

    public String create(final String fileName, final int line) throws IOException {
        if (fileName.contains(".java")) { // FIXME if the file is no java-file, it`s not possible to build an AST
            List<String> lineIterator = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            StringBuilder context = new StringBuilder(BUFFER_SIZE);

            //get text with MethodAST
            MethodAst a = new MethodAst(fileName, line);
            DetailAST dA = a.findMethodStart();
            //get first and last line number of the method
            int firstLine = dA.getLineNo();
            int lastLine;
            if (dA.getNextSibling() != null) {
                lastLine = dA.getNextSibling().getLineNo() - 1;
            }
            else {
                lastLine = a.getLastLineNumber() - 1;
            }

            int i = 0;
            for (String currentLine : lineIterator) {
                //get the last five lines of Javadoc by scanning above the method start for lines with a "*".
                if (i >= firstLine - 5 && i < firstLine && currentLine.contains("*")) {
                    context.append(currentLine);
                }
                //get Method-text but not the Javadoc of the following method.
                if (i >= firstLine - 1 && i <= lastLine) {
                    if (!currentLine.contains("*") && !currentLine.contains("@")) {
                        context.append(currentLine);
                    }
                }
                if (i > lastLine) {
                    break;
                }
                i++;
            }

            //remove java-specific keywords from the text
            String text = context.toString();
            for (String keyword : keywords) {
                text = text.replaceAll(keyword, "");
            }

            return text;
        }
        else {
            return "empty";
        }
    }
}
