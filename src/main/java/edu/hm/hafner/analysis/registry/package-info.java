/**
 * Provides a registry for all {@link edu.hm.hafner.analysis.IssueParser parsers}. Consumers of the analysis model
 * library should never depend directly on one of the parser classes. Rather use the {@link
 * edu.hm.hafner.analysis.registry.ParserRegistry registry} to obtain a generic {@link
 * edu.hm.hafner.analysis.registry.ParserDescriptor descrptior} for the desired parser. You can use this descriptor to
 * create the actual parser and to query additional properties, like a help text or the default file name pattern.
 */
@DefaultAnnotation(NonNull.class)
package edu.hm.hafner.analysis.registry;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
