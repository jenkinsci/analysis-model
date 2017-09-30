package edu.hm.hafner.analysis;

/**
 * Processes an input line right before it will be parsed by a {@link RegexpParser}.
 *
 * @author Ullrich Hafner
 */
public interface PostProcessor {
    String processLine(String input);
}
