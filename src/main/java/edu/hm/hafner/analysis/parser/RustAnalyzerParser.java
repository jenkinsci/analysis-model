package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import edu.hm.hafner.analysis.ReaderFactory;

/**
 * A parser for rust-analyzer flycheck diagnostics in JSON format.
 *
 * <p>rust-analyzer emits rustc JSON diagnostics for flycheck and check-on-save, so this parser reuses the cargo
 * check JSON parser implementation.</p>
 *
 * @author Akash Manna
 * @see <a href="https://github.com/rust-lang/rust-analyzer">rust-analyzer on GitHub</a>
 */
public class RustAnalyzerParser extends CargoCheckParser {
    @Serial
    private static final long serialVersionUID = -1230763498204372941L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".json");
    }
}