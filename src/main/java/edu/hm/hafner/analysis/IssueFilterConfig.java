package edu.hm.hafner.analysis;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * A config for filtering issues. Filter-options are FileName, PackageName, ModuleName, Category and Type. For each of
 * them there is a possibility to include and exclude issues. The filters are regexes. Instantiation only with
 * IssueFilterConfigBuilder.
 */
public class IssueFilterConfig {
    private final boolean isIncludeSet;

    private final Pattern includeFileName;
    private final Pattern includePackageName;
    private final Pattern includeModuleName;
    private final Pattern includeCategory;
    private final Pattern includeType;

    private final Pattern excludeFileName;
    private final Pattern excludePackageName;
    private final Pattern excludeModuleName;
    private final Pattern excludeCategory;
    private final Pattern excludeType;

    /**
     * Ctor for Builder.
     *
     * @param builder contains the filter-parameters.
     */
    private IssueFilterConfig(IssueFilterConfigBuilder builder) {
        this.isIncludeSet = builder.isIncludePatternSet;
        this.includeFileName = builder.includeFileName;
        this.includePackageName = builder.includePackageName;
        this.includeModuleName = builder.includeModuleName;
        this.includeCategory = builder.includeCategory;
        this.includeType = builder.includeType;
        this.excludeFileName = builder.excludeFileName;
        this.excludePackageName = builder.excludePackageName;
        this.excludeModuleName = builder.excludeModuleName;
        this.excludeCategory = builder.excludeCategory;
        this.excludeType = builder.excludeType;
    }

    public boolean isIncludePatternSet() {
        return isIncludeSet;
    }

    public Predicate<String> getIncludeFileName() {
        return includeFileName.asPredicate();
    }

    public Predicate<String> getIncludePackageName() {
        return includePackageName.asPredicate();
    }

    public Predicate<String> getIncludeModuleName() {
        return includeModuleName.asPredicate();
    }

    public Predicate<String> getIncludeCategory() {
        return includeCategory.asPredicate();
    }

    public Predicate<String> getIncludeType() {
        return includeType.asPredicate();
    }

    public Predicate<String> getExcludeFileName() {
        return excludeFileName.asPredicate();
    }

    public Predicate<String> getExcludePackageName() {
        return excludePackageName.asPredicate();
    }

    public Predicate<String> getExcludeModuleName() {
        return excludeModuleName.asPredicate();
    }

    public Predicate<String> getExcludeCategory() {
        return excludeCategory.asPredicate();
    }

    public Predicate<String> getExcludeType() {
        return excludeType.asPredicate();
    }

    /**
     * A Builder for IssueFilterConfig. It initializes the includes and excludes with "matches nothing".
     */
    public static class IssueFilterConfigBuilder {
        private static final Pattern MATCHES_NOTHING = Pattern.compile("(?!)");

        // is set to true, whenever a include pattern is set.
        private boolean isIncludePatternSet = false;

        // Patterns for include. Initialized with a Regex that matches nothing.
        private Pattern includeFileName = MATCHES_NOTHING;
        private Pattern includePackageName = MATCHES_NOTHING;
        private Pattern includeModuleName = MATCHES_NOTHING;
        private Pattern includeCategory = MATCHES_NOTHING;
        private Pattern includeType = MATCHES_NOTHING;

        // Patterns for exlude. Initialized with a Regex that matches nothing.
        private Pattern excludeFileName = MATCHES_NOTHING;
        private Pattern excludePackageName = MATCHES_NOTHING;
        private Pattern excludeModuleName = MATCHES_NOTHING;
        private Pattern excludeCategory = MATCHES_NOTHING;
        private Pattern excludeType = MATCHES_NOTHING;

        /**
         * Build an immutable IssueFilterConfig.
         *
         * @return An instance of IssueBuilderConfig
         */
        public IssueFilterConfig build() {
            return new IssueFilterConfig(this);
        }

        /**
         * Sets a FIleName-Regex, which issues to include.
         *
         * @param includeFileName List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder includeFileName(final List<String> includeFileName) {
            this.includeFileName = convertStringsToPattern(includeFileName);
            isIncludePatternSet = true;
            return this;
        }

        /**
         * Sets a PackageName-Regex, which issues to include.
         *
         * @param includePackageName List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder includePackageName(final List<String> includePackageName) {
            this.includePackageName = convertStringsToPattern(includePackageName);
            isIncludePatternSet = true;
            return this;
        }

        /**
         * Sets a ModuleName-Regex, which issues to include.
         *
         * @param includeModuleName List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder includeModuleName(final List<String> includeModuleName) {
            this.includeModuleName = convertStringsToPattern(includeModuleName);
            isIncludePatternSet = true;
            return this;
        }

        /**
         * Sets a Category-Regex, which issues to include.
         *
         * @param includeCategory List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder includeCategory(final List<String> includeCategory) {
            this.includeCategory = convertStringsToPattern(includeCategory);
            isIncludePatternSet = true;
            return this;
        }

        /**
         * Sets a Type-Regex, which issues to include.
         *
         * @param includeType List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder includeType(final List<String> includeType) {
            this.includeType = convertStringsToPattern(includeType);
            isIncludePatternSet = true;
            return this;
        }

        /**
         * Sets a FileName-Regex, which issues to exclude.
         *
         * @param excludeFileName List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder excludeFileName(final List<String> excludeFileName) {
            this.excludeFileName = convertStringsToPattern(excludeFileName);
            return this;
        }

        /**
         * Sets a PackageName-Regex, which issues to exclude.
         *
         * @param excludePackageName List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder excludePackageName(final List<String> excludePackageName) {
            this.excludePackageName = convertStringsToPattern(excludePackageName);
            return this;
        }

        /**
         * Sets a ModuleName-Regex, which issues to exclude.
         *
         * @param excludeModuleName List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder excludeModuleName(final List<String> excludeModuleName) {
            this.excludeModuleName = convertStringsToPattern(excludeModuleName);
            return this;
        }

        /**
         * Sets a Category-Regex, which issues to exclude.
         *
         * @param excludeCategory List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder excludeCategory(final List<String> excludeCategory) {
            this.excludeCategory = convertStringsToPattern(excludeCategory);
            return this;
        }

        /**
         * Sets a Type-Regex, which issues to exclude.
         *
         * @param excludeType List of Regexes. They are concatenated by 'OR'
         * @return the builder
         */
        public IssueFilterConfigBuilder excludeType(final List<String> excludeType) {
            this.excludeType = convertStringsToPattern(excludeType);
            return this;
        }
    }

    /**
     * Builds a Pattern from a List of Regex-Strings. The list-entries are concatenated with 'OR'.
     *
     * @param regexes a list of regexes
     * @return a pattern to be used as a predicate
     */
    private static Pattern convertStringsToPattern(final List<String> regexes) {
        String completeRegex = "";
        boolean isNotFirst = false;
        for (int i = 0; i < regexes.size(); ++i) {
            if (isNotFirst) {
                completeRegex += "|(";
            }
            else {
                isNotFirst = true;
                completeRegex += "(";
            }
            completeRegex += regexes.get(i) + ")";
        }
        return Pattern.compile(completeRegex);
    }

}
