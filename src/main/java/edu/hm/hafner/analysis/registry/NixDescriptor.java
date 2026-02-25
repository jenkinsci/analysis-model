package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.NixParser;

/**
 * A descriptor for Nix build and flake check output.
 *
 * @author Akash Manna
 */
class NixDescriptor extends ParserDescriptor {
    private static final String ID = "nix";
    private static final String NAME = "Nix";

    NixDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new NixParser();
    }

    @Override
    public String getUrl() {
        return "https://nixos.org/";
    }

    @Override
    public String getIconUrl() {
        return "https://nixos.org/logo/nixos-logo-only-hires.png";
    }
}
