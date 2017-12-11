/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.hm.hafner.analysis.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for Cadence Incisive Enterprise Simulator
 *
 * @author Andrew 'Necromant' Andrianov
 */
public class CadenceIncisiveParser extends RegexpLineParser {
    private static final String SLASH = "/";
    private static final String CADENCE_MESSAGE_PATTERN = "(" + "(^[a-zA-Z]+): \\*([a-zA-Z]),([a-zA-Z]+): (.*) " +
            "\\[File:(.*), Line:(.*)\\]." //ncelab vhdl warning
            + ")|(" + "(^[a-zA-Z]+): \\*([a-zA-Z]),([a-zA-Z]+) \\((.*),([0-9]+)\\|([0-9]+)\\): (.*)$" //Warning/error
            // with filename
            + ")|(" + "(^g?make\\[.*\\]: Entering directory)\\s*(['`]((.*))\\')" // make: entering directory
            + ")|(" + "(^[a-zA-Z]+): \\*([a-zA-Z]),([a-zA-Z]+): (.*)$" //Single generic warning
            + ")";
    private String directory = "";

    /**
     * Creates a new instance of {@link CadenceIncisiveParser}.
     */
    public CadenceIncisiveParser() {
        super(CADENCE_MESSAGE_PATTERN);
    }

    private Issue handleDirectory(final Matcher matcher, int offset) {
        directory = matcher.group(offset) + SLASH; //17
        return FALSE_POSITIVE;
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {

        String tool;
        String type;
        String category;
        String message;
        String fileName;

        int lineNumber = 0;
        int column = 0;

        List<String> arr = new ArrayList<>();
        int n = matcher.groupCount();

        for (int i = 0; i <= n; i++) {
            arr.add(i, matcher.group(i));
        }

        Priority priority = Priority.LOW;

        if (matcher.group(1) != null) {
            /* vhdl warning from ncelab */
            tool = matcher.group(2);
            type = matcher.group(3);
            category = matcher.group(4);
            fileName = matcher.group(6);
            lineNumber = parseInt(matcher.group(7));
            message = matcher.group(5);
            priority = Priority.NORMAL;
        }
        else if (matcher.group(16) != null) {
            /* Set current directory */
            return handleDirectory(matcher, 20);
        }
        else if (matcher.group(8) != null) {
            tool = matcher.group(9);
            type = matcher.group(10);
            category = matcher.group(11);
            fileName = matcher.group(12);
            lineNumber = parseInt(matcher.group(13));
            priority = Priority.NORMAL;
            /* column = matcher.group(14); */
            message = matcher.group(15);
        }
        else if (matcher.group(21) != null) {
            tool = matcher.group(22);
            type = matcher.group(23);
            category = matcher.group(24);
            message = matcher.group(25);
            fileName = "/NotFileRelated";
        }
        else {
            return FALSE_POSITIVE;
            /* Should never happen! */
        }

        if (category.equalsIgnoreCase("E")) {
            priority = Priority.HIGH;
            category = "Error (" + tool + "): " + category;
        }
        else {
            category = "Warning (" + tool + "): " + category;
        }

        /*  Filename should never be null here, unless someone updates the above 
         *  logic and breaks it. 
         */

        if (fileName == null) {
            return FALSE_POSITIVE;
        }

        if (fileName.startsWith(SLASH)) {
            return builder.setFileName(fileName).setLineStart(lineNumber).setCategory(category)
                          .setMessage(message).setPriority(priority).build();
        }
        else {
            return builder.setFileName(directory + fileName).setLineStart(lineNumber).setCategory(category)
                          .setMessage(message).setPriority(priority).build();
        }
    }

}
