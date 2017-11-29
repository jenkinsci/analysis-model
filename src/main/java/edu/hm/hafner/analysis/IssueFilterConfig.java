package edu.hm.hafner.analysis;

import java.util.List;
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
     * @param builder
     *         contains the filter-parameters.
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

    public Pattern getIncludeFileName() {
        return includeFileName;
    }

    public Pattern getIncludePackageName() {
        return includePackageName;
    }

    public Pattern getIncludeModuleName() {
        return includeModuleName;
    }

    public Pattern getIncludeCategory() {
        return includeCategory;
    }

    public Pattern getIncludeType() {
        return includeType;
    }

    public Pattern getExcludeFileName() {
        return excludeFileName;
    }

    public Pattern getExcludePackageName() {
        return excludePackageName;
    }

    public Pattern getExcludeModuleName() {
        return excludeModuleName;
    }

    public Pattern getExcludeCategory() {
        return excludeCategory;
    }

    public Pattern getExcludeType() {
        return excludeType;
    }

    /**
     * A Builder for IssueFilterConfig. It initializes the includes and excludes with "matches nothing".
     */
    public static class IssueFilterConfigBuilder {
        // is set to true, whenever a include pattern is set.
        private boolean isIncludePatternSet = false;

        // Patterns for include. Initialized with a Regex that matches nothing.
        private Pattern includeFileName = Pattern.compile("(?!)");
        private Pattern includePackageName = Pattern.compile("(?!)");
        private Pattern includeModuleName = Pattern.compile("(?!)");
        private Pattern includeCategory = Pattern.compile("(?!)");
        private Pattern includeType = Pattern.compile("(?!)");

        // Patterns for exlude. Initialized with a Regex that matches nothing.
        private Pattern excludeFileName = Pattern.compile("(?!)");
        private Pattern excludePackageName = Pattern.compile("(?!)");
        private Pattern excludeModuleName = Pattern.compile("(?!)");
        private Pattern excludeCategory = Pattern.compile("(?!)");
        private Pattern excludeType = Pattern.compile("(?!)");

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
         * @param includeFileName
         *         List of Regexes. They are concatenated by 'OR'
         *
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
         * @param includePackageName
         *         List of Regexes. They are concatenated by 'OR'
         *
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
         * @param includeModuleName
         *         List of Regexes. They are concatenated by 'OR'
         *
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
         * @param includeCategory
         *         List of Regexes. They are concatenated by 'OR'
         *
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
         * @param includeType
         *         List of Regexes. They are concatenated by 'OR'
         *
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
         * @param excludeFileName
         *         List of Regexes. They are concatenated by 'OR'
         *
         * @return the builder
         */
        public IssueFilterConfigBuilder excludeFileName(final List<String> excludeFileName) {
            this.excludeFileName = convertStringsToPattern(excludeFileName);
            return this;
        }

        /**
         * Sets a PackageName-Regex, which issues to exclude.
         *
         * @param excludePackageName
         *         List of Regexes. They are concatenated by 'OR'
         *
         * @return the builder
         */
        public IssueFilterConfigBuilder excludePackageName(final List<String> excludePackageName) {
            this.excludePackageName = convertStringsToPattern(excludePackageName);
            return this;
        }

        /**
         * Sets a ModuleName-Regex, which issues to exclude.
         *
         * @param excludeModuleName
         *         List of Regexes. They are concatenated by 'OR'
         *
         * @return the builder
         */
        public IssueFilterConfigBuilder excludeModuleName(final List<String> excludeModuleName) {
            this.excludeModuleName = convertStringsToPattern(excludeModuleName);
            return this;
        }

        /**
         * Sets a Category-Regex, which issues to exclude.
         *
         * @param excludeCategory
         *         List of Regexes. They are concatenated by 'OR'
         *
         * @return the builder
         */
        public IssueFilterConfigBuilder excludeCategory(final List<String> excludeCategory) {
            this.excludeCategory = convertStringsToPattern(excludeCategory);
            return this;
        }

        /**
         * Sets a Type-Regex, which issues to exclude.
         *
         * @param excludeType
         *         List of Regexes. They are concatenated by 'OR'
         *
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
     * @param regexes
     *         a list of regexes
     *
     * @return a pattern to be used as a predicate
     */
    private static Pattern convertStringsToPattern(final List<String> regexes) {
        String completeRegex = "";
        boolean isNotFirst = false;
        for (int i = 0; i < regexes.size(); ++i) {
            if (isNotFirst) {
                completeRegex += "|";
            }
            else {
                isNotFirst = true;
            }
            completeRegex += regexes.get(i);
        }
        return Pattern.compile(completeRegex);
    }

}
