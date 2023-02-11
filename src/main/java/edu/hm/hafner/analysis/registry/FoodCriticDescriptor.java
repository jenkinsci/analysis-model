package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.FoodcriticParser;

/**
 * A descriptor for Foodcritic.
 *
 * @author Lorenz Munsch
 */
class FoodCriticDescriptor extends ParserDescriptor {
    private static final String ID = "foodcritic";
    private static final String NAME = "Foodcritic";

    FoodCriticDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new FoodcriticParser();
    }

    @Override
    public String getUrl() {
        return "http://www.foodcritic.io/";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/Foodcritic/foodcritic-site/master/source/images/foodcritic.png";
    }
}
