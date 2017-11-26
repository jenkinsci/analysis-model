package edu.hm.hafner.analysis;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.*;
import static java.util.regex.Pattern.*;

/**
 * Filter for {@link Issues issues}.
 * <p>
 * The filtering can be performed based on a combination of the following properties of an {@link Issue issue}: <ul>
 * <li>{@link Issue#getFileName file name}</li> <li>{@link Issue#getPackageName package name}</li> <li>{@link
 * Issue#getModuleName module name}</li> <li>{@link Issue#getCategory category}</li> <li>{@link Issue#getType type}</li>
 * </ul>
 * <p>
 * A list of include and exclude regex can be defined for each property.
 * <p>
 * <strong>An {@link Issue issue} is included in the output if at least one include filter matches and no exclude filter
 * matches.</strong>
 *
 * @author Marcel Binder
 */
public class IssueFilter {
    private final List<IssuePropertyFilter> includeFilters;
    private final List<IssuePropertyFilter> excludeFilters;

    private IssueFilter(final List<IssuePropertyFilter> includeFilters, final List<IssuePropertyFilter> excludeFilters) {
        this.includeFilters = includeFilters;
        this.excludeFilters = excludeFilters;
    }

    public Issues filter(final Issues issues) {
        return new Issues(
                issues.all()
                        .stream()
                        .filter(issue -> includeFilters.isEmpty() || includeFilters
                                .stream()
                                .anyMatch(filter -> filter.filter(issue)))
                        .filter(issue -> excludeFilters
                                .stream()
                                .noneMatch(filter -> filter.filter(issue)))
                        .collect(Collectors.toList())
        );
    }

    @FunctionalInterface
    private interface IssuePropertyExtractor {
        String extract(final Issue issue);
    }

    @FunctionalInterface
    private interface IssuePropertyFilter {
        boolean filter(final Issue issue);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<IssuePropertyFilter> includeFilters;
        private List<IssuePropertyFilter> excludeFilters;

        private Builder() {
            this.includeFilters = newArrayList();
            this.excludeFilters = newArrayList();
        }

        private IssuePropertyFilter filter(final IssuePropertyExtractor propertyExtractor, final String regex) {
            Pattern pattern = compile(regex);
            return issue -> pattern.matcher(propertyExtractor.extract(issue)).matches();
        }

        private List<IssuePropertyFilter> filters(final IssuePropertyExtractor propertyExtractor, final List<String> regex) {
            return regex.stream()
                    .map(r -> filter(propertyExtractor, r))
                    .collect(Collectors.toList());
        }

        private Builder include(final IssuePropertyExtractor propertyExtractor, final List<String> regex) {
            includeFilters.addAll(filters(propertyExtractor, regex));
            return this;
        }

        private Builder exclude(final IssuePropertyExtractor propertyExtractor, final List<String> regex) {
            excludeFilters.addAll(filters(propertyExtractor, regex));
            return this;
        }

        public Builder includeFileNames(final List<String> fileNames) {
            return include(Issue::getFileName, fileNames);
        }

        public Builder excludeFileNames(final List<String> fileNames) {
            return exclude(Issue::getFileName, fileNames);
        }

        public Builder includePackageNames(final List<String> packageNames) {
            return include(Issue::getPackageName, packageNames);
        }

        public Builder excludePackageNames(final List<String> packageNames) {
            return exclude(Issue::getPackageName, packageNames);
        }

        public Builder includeModuleNames(final List<String> moduleNames) {
            return include(Issue::getModuleName, moduleNames);
        }

        public Builder excludeModuleNames(final List<String> moduleNames) {
            return exclude(Issue::getModuleName, moduleNames);
        }

        public Builder includeCategories(final List<String> categories) {
            return include(Issue::getCategory, categories);
        }

        public Builder excludeCategories(final List<String> categories) {
            return exclude(Issue::getCategory, categories);
        }

        public Builder includeTypes(final List<String> types) {
            return include(Issue::getType, types);
        }

        public Builder excludeTypes(final List<String> types) {
            return exclude(Issue::getType, types);
        }

        public IssueFilter build() {
            return new IssueFilter(includeFilters, excludeFilters);
        }
    }
}
