# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased](https://github.com/jenkinsci/analysis-model/compare/analysis-model-4.0.0...master)

### Added
- [PR#132](https://github.com/jenkinsci/analysis-model/pull/132): 
Added a parser for CMake warnings.
- [PR#137](https://github.com/jenkinsci/analysis-model/pull/137):
Added a parser for JSON output from Cargo.

### Fixed
- [JENKINS-56333](https://issues.jenkins-ci.org/browse/JENKINS-56333): 
MsBuild Parser: Treat errors as errors and not warning (high).
- [JENKINS-56737](https://issues.jenkins-ci.org/browse/JENKINS-56737),
[PR#136](https://github.com/jenkinsci/analysis-model/pull/136)
Javac parser: Add the ability to parse warnings with preceding timestamps.
- [JENKINS-56214](https://issues.jenkins-ci.org/browse/JENKINS-56214),
[PR#142](https://github.com/jenkinsci/analysis-model/pull/142)
ChackStyle parser: Map errors to severity ERROR (rather than WARNING_HIGH)

### Changed
- Filters now work on a substring of the property, you don't need to create a regular
expression that matches the whole property value anymore. 

## [4.0.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-3.0.0...analysis-model-4.0.0) - 2019-3-20

### Fixed
- [JENKINS-56612](https://issues.jenkins-ci.org/browse/JENKINS-56612): 
Fixed filtering of multiline messages.
- [JENKINS-55345](https://issues.jenkins-ci.org/browse/JENKINS-55345): 
DocFX Parser: Ignore Info messages and do not treat them as warnings.
- [JENKINS-42823](https://issues.jenkins-ci.org/browse/JENKINS-42823): 
MsBuild Parser: Fix false positive with parser.
- [JENKINS-56526](https://issues.jenkins-ci.org/browse/JENKINS-56526): 
Filters: let the message filter scan for texts in message **and** description.

### Removed
- \[API\]: Deprecated and unused classes `DoxygenParser` and `DotNetAssembly` have been removed.

## [3.0.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.1.2...analysis-model-3.0.0) - 2019-3-15

### Added
- Gcc4: Show multi-line warnings messages. Map all errors to severity error. 
- [PR#103](https://github.com/jenkinsci/analysis-model/pull/103): New parser for IAR CSTAT warnings.
- [PR#100](https://github.com/jenkinsci/analysis-model/pull/100): Add package and module support for Pylint.

### Fixed
- [JENKINS-56393](https://issues.jenkins-ci.org/browse/JENKINS-56393),
[PR#117](https://github.com/jenkinsci/analysis-model/pull/117): Improved RfLintParser - Set category and type.
- [JENKINS-56394](https://issues.jenkins-ci.org/browse/JENKINS-56394),
[PR#117](https://github.com/jenkinsci/analysis-model/pull/117): Improved RfLintParser - add directory as the package name.
- [JENKINS-55805](https://issues.jenkins-ci.org/browse/JENKINS-55805): 
JavaDoc Parser: Improved performance (skip overly long lines).
- [JENKINS-54506](https://issues.jenkins-ci.org/browse/JENKINS-54506):
JavaDoc Parser: Java errors were detected as JavaDoc warnings.
- [JENKINS-55750](https://issues.jenkins-ci.org/browse/JENKINS-55750),
[PR#102](https://github.com/jenkinsci/analysis-model/pull/102): 
IarParser: Added support for absolute Windows paths.
- [JENKINS-55733](https://issues.jenkins-ci.org/browse/JENKINS-55733): 
CppCheck: Added support of multiple locations per warning.
- [JENKINS-55840](https://issues.jenkins-ci.org/browse/JENKINS-55840), 
[JENKINS-27973](https://issues.jenkins-ci.org/browse/JENKINS-27973),
[JENKINS-7178](https://issues.jenkins-ci.org/browse/JENKINS-7178): 
Refactored Gcc4 parser so that it is also capable of parsing Doxygen warnings.
- [JENKINS-56020](https://issues.jenkins-ci.org/browse/JENKINS-56020), 
[JENKINS-56193](https://issues.jenkins-ci.org/browse/JENKINS-56193): 
Fixed absolute paths if Ninja build system is used.
- [JENKINS-56235](https://issues.jenkins-ci.org/browse/JENKINS-56235): Fixed IDEA inspection file paths
- [PR#104](https://github.com/jenkinsci/analysis-model/pull/104): Pylint: Add types, make categories types and categories, categories.

### Changed
- \[API\]: `Gcc4CompilerParser` uses new base class 
[LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
that provides a lookahead of the next report line

## [2.1.2](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.1.1...analysis-model-2.1.2) - 2019-2-10

### Fixed
- [violations-lib#58](https://github.com/tomasbjerre/violations-lib/issues/58): 
Flake8: Fixed broken file name if columns has more than one digit.
- [PR#85](https://github.com/jenkinsci/analysis-model/pull/85): 
Intel Parser: Look for diagnostics with the category "message".

## [2.1.1](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.1.0...analysis-model-2.1.1) - 2019-2-7

### Fixed
- [JENKINS-56001](https://issues.jenkins-ci.org/browse/JENKINS-56001): 
Improved absolute path detection: make detection independent of running OS.
- [PR#84](https://github.com/jenkinsci/analysis-model/pull/84): Regex tweak to restore support for older versions of IAR

## [2.1.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.0.2...analysis-model-2.1.0) - 2019-2-5

### Added
- [JENKINS-51267](https://issues.jenkins-ci.org/browse/JENKINS-51267), 
[JENKINS-51438](https://issues.jenkins-ci.org/browse/JENKINS-51438),
[JENKINS-55730](https://issues.jenkins-ci.org/browse/JENKINS-55730),
[JENKINS-55775](https://issues.jenkins-ci.org/browse/JENKINS-55775),
[JENKINS-55839](https://issues.jenkins-ci.org/browse/JENKINS-55839),
[JENKINS-51439](https://issues.jenkins-ci.org/browse/JENKINS-51439): Added folder of affected file as property.
- [JENKINS-55846](https://issues.jenkins-ci.org/browse/JENKINS-55846): 
ErrorProne parser: Added support for Gradle reports.
- [JENKINS-55873](https://issues.jenkins-ci.org/browse/JENKINS-55873): 
MavenConsole parser: Added support for maven-enforcer-plugin error messages.

### Fixed
- [JENKINS-55805](https://issues.jenkins-ci.org/browse/JENKINS-55805): 
Java Ant Parser: Improved performance of ant task detection.
- [PR#82](https://github.com/jenkinsci/analysis-model/pull/82): CppLint: Fixed parser for messages with colon.
- [JENKINS-55715](https://issues.jenkins-ci.org/browse/JENKINS-55715), 
[PR#81](https://github.com/jenkinsci/analysis-model/pull/81): 
Taglist Parser: Convert class name into a file name and a package.
- [PR#80](https://github.com/jenkinsci/analysis-model/pull/80): Keep PathUtil inside of sym-linked directories.

## [2.0.2](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.0.1...analysis-model-2.0.2) - 2019-1-21

### Fixed
- [PR#79](https://github.com/jenkinsci/analysis-model/pull/79): Correctly detect categories for Ansible Lint 4.x.

## [2.0.1](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.0.0...analysis-model-2.0.1) - 2019-1-16

### Fixed
- [JENKINS-55368](https://issues.jenkins-ci.org/browse/JENKINS-55368): Fixed Eclipse parser for maven builds.

## [2.0.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-1.1.0...analysis-model-2.0.0) - 2019-1-15

### Added
- Added support for [ErrorProne](http://errorprone.info) in maven builds. Parser now reports description with link to external documentation.
- \[API\]: Added new base class [LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
that provides a lookahead of the next report line
- [JENKINS-55442](https://issues.jenkins-ci.org/browse/JENKINS-55442), 
[PR#78](https://github.com/jenkinsci/analysis-model/pull/78): Added include/exclude filters for issue messages. 

### Changed
- Improved maven console parser: use the maven goal that logs a warning as issue type. Ignore all warnings
from the maven-compiler-plugin since these are already picked up by the Java parser.
- \[API\]: Replaced `CheckForNull` annotations with `Nullable` in order to enable [NullAway](https://github.com/uber/NullAway) checker in build

### Fixed
- [PR#74](https://github.com/jenkinsci/analysis-model/pull/74): IntelParser: Check for project number in regex.
- [JENKINS-25278](https://issues.jenkins-ci.org/browse/JENKINS-25278): Improved performance of Maven console parser. 
- [JENKINS-55328](https://issues.jenkins-ci.org/browse/JENKINS-55328): Show error message if symbol 'pmd' is used
- [JENKINS-55340](https://issues.jenkins-ci.org/browse/JENKINS-55340), [PR#73](https://github.com/jenkinsci/analysis-model/pull/73): 
: Fixed PyLint parser: detect human readable categories. 
- [JENKINS-55358](https://issues.jenkins-ci.org/browse/JENKINS-55358): Improved parser to support ECJ reports of ant. 
- [JENKINS-55368](https://issues.jenkins-ci.org/browse/JENKINS-55368): Fixed parser to remove console notes. 
### Deprecated
- [edu.hm.hafner.analysis.FastRegexpLineParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/FastRegexpLineParser.java)
- [edu.hm.hafner.analysis.RegexpDocumentParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/RegexpDocumentParser.java)

## [1.1.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-1.0.0...analysis-model-1.1.0) - 2018-12-20

### Added
- Added ModuleResolver from Jenkins warnings plugin.

## 1.0.0 - 2018-12-20

First public release.

<!---
## 1.0.0 - year-month-day
### Added
- One 
- Two 

### Changed
- One 
- Two 

### Deprecated
- One 
- Two 

### Removed
- One 
- Two 

### Fixed
- One 
- Two 

### Security
- One 
- Two 


-->
