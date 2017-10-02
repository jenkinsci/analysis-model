package edu.hm.hafner.analysis;

import com.google.common.base.Function;

/**
 * Processes an input line right before it will be parsed by a {@link AbstractParser}.
 *
 * @author Ullrich Hafner
 */
public interface PostProcessor extends Function<String, String> {
}
