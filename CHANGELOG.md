# Changelog (1.0.0 - 5.2.0)

All notable changes of version 1.0.0 - 5.2.0 are documented in this file. All future changes will be automatically 
logged by release drafter in [GitHub releases](https://github.com/jenkinsci/analysis-model/releases). 

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [5.2.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-5.1.1...analysis-model-5.2.0) - 2019-7-2

### Added

- [JENKINS-57098](https://issues.jenkins.io/browse/JENKINS-55051):
Added DScanner parser

## [5.1.1](https://github.com/jenkinsci/analysis-model/compare/analysis-model-5.1.0...analysis-model-5.1.1) - 2019-6-3

### Fixed

- Apply additional custom line mappers before removing ANSI color codes. 
Fixes problems if the log contains color codes and Jenkins console notes.

## [5.1.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-5.0.2...analysis-model-5.1.0) - 2019-5-27

### Added

- [JENKINS-57098](https://issues.jenkins.io/browse/JENKINS-57098),
[PR#177](https://github.com/jenkinsci/analysis-model/pull/177),
[PR#168](https://github.com/jenkinsci/analysis-model/pull/168): 
Added a generic JSON parser that reads all properties of the internal `Issue` object.

### Changed internal API

- Use SpotBugs library to read FindBugs and SpotBugs files. Hide ASM and BCEL libraries that are used by Jen

## [5.0.2](https://github.com/jenkinsci/analysis-model/compare/analysis-model-5.0.1...analysis-model-5.0.2) - 2019-5-14

### Fixed
- [JENKINS-57365](https://issues.jenkins.io/browse/JENKINS-57365),
[PR#174](https://github.com/jenkinsci/analysis-model/pull/174): 
Fixes false positives for MsBuild if e.g. a build project name contains the string info/note/warning.

## [5.0.1](https://github.com/jenkinsci/analysis-model/compare/analysis-model-5.0.0...analysis-model-5.0.1) - 2019-5-12

### Fixed
- [JENKINS-57379](https://issues.jenkins.io/browse/JENKINS-57379),
[PR#171](https://github.com/jenkinsci/analysis-model/pull/171): 
EclipseParser fails to extract full message when message includes array brackets.

## [5.0.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-4.0.0...analysis-model-5.0.0) - 2019-5-7

### Fixed

### Added
- [JENKINS-56510](https://issues.jenkins.io/browse/JENKINS-56510),
[PR#154](https://github.com/jenkinsci/analysis-model/pull/154): 
Added a generic XML parser that reads all properties of the internal `Issue` object.
- [PR#132](https://github.com/jenkinsci/analysis-model/pull/132): 
Added a parser for CMake warnings.
- [PR#137](https://github.com/jenkinsci/analysis-model/pull/137):
Added a parser for JSON output from Cargo.
- [PR#160](https://github.com/jenkinsci/analysis-model/pull/160):
Added a parser for MentorGraphcis Modelsim/Questa.

### Fixed
- [JENKINS-54736](https://issues.jenkins.io/browse/JENKINS-54736)
PMD parser: Added support for errors.
- [PR#118](https://github.com/jenkinsci/analysis-model/pull/118):
Fixed parsing of log files that contain ANSI color codes (or escape sequences in general).
- [JENKINS-38685](https://issues.jenkins.io/browse/JENKINS-38685)
MsBuild parser: Allow MSBuild errors without category.
- [JENKINS-56333](https://issues.jenkins.io/browse/JENKINS-56333),
[PR#129](https://github.com/jenkinsci/analysis-model/pull/129): 
MsBuild Parser: Treat errors as errors and not warning (high).
- [JENKINS-56450](https://issues.jenkins.io/browse/JENKINS-56450): 
[PR#158](https://github.com/jenkinsci/analysis-model/pull/158): 
MSBuild Parser: Fix error when compiling with /MP.
- [JENKINS-48647](https://issues.jenkins.io/browse/JENKINS-48647),
[PR#145](https://github.com/jenkinsci/analysis-model/pull/145): 
MsBuild Parser: Update regular expression to detect logging prefixes.
- [JENKINS-56737](https://issues.jenkins.io/browse/JENKINS-56737),
[PR#136](https://github.com/jenkinsci/analysis-model/pull/136)
Javac parser: Add the ability to parse warnings with preceding timestamps.
- [JENKINS-56214](https://issues.jenkins.io/browse/JENKINS-56214),
[PR#142](https://github.com/jenkinsci/analysis-model/pull/142)
CheckStyle parser: Map errors to severity ERROR (rather than WARNING_HIGH)
- [JENKINS-52477](https://issues.jenkins.io/browse/JENKINS-52477),
[PR#146](https://github.com/jenkinsci/analysis-model/pull/146):
FileReaderFactory: Detect charset from XML-header when not specified. 
- [JENKINS-52462](https://issues.jenkins.io/browse/JENKINS-52462),
[PR#155](https://github.com/jenkinsci/analysis-model/pull/155):
SonarQubeParser: Use `textRange` when computing affected source code line.

### Changed
- Filters now work on a substring of the property, you don't need to create a regular
expression that matches the whole property value anymore. 

### Changed API

- [JENKINS-56698](https://issues.jenkins.io/browse/JENKINS-56698),
[PR#156](https://github.com/jenkinsci/analysis-model/pull/156) 
`NagFortranParser`: now uses uses new base class 
[LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
- [JENKINS-56702](https://issues.jenkins.io/browse/JENKINS-56702),
  [PR#150](https://github.com/jenkinsci/analysis-model/pull/150) `YuiCompressorParser`: now uses new base class 
[LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
that provides a lookahead of the next report line instead of using multi line parsing.
- [JENKINS-56700](https://issues.jenkins.io/browse/JENKINS-56700),
[PR#153](https://github.com/jenkinsci/analysis-model/pull/153):
`GhsMultiParser`: now uses new base class 
[LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
- [JENKINS-56701](https://issues.jenkins.io/browse/JENKINS-56701),
[PR#153](https://github.com/jenkinsci/analysis-model/pull/153):
`GnuFortranParser`: now uses new base class 
[LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
- `DrMemoryParser`: now uses new base class 
[LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 

## [4.0.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-3.0.0...analysis-model-4.0.0) - 2019-3-20

### Fixed
- [JENKINS-56612](https://issues.jenkins.io/browse/JENKINS-56612): 
Fixed filtering of multiline messages.
- [JENKINS-55345](https://issues.jenkins.io/browse/JENKINS-55345): 
DocFX Parser: Ignore Info messages and do not treat them as warnings.
- [JENKINS-42823](https://issues.jenkins.io/browse/JENKINS-42823): 
MsBuild Parser: Fix false positive with parser.
- [JENKINS-56526](https://issues.jenkins.io/browse/JENKINS-56526): 
Filters: let the message filter scan for texts in message **and** description.

### Removed
- \[API\]: Deprecated and unused classes `DoxygenParser` and `DotNetAssembly` have been removed.

## [3.0.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.1.2...analysis-model-3.0.0) - 2019-3-15

### Added
- Gcc4: Show multi-line warnings messages. Map all errors to severity error. 
- [PR#103](https://github.com/jenkinsci/analysis-model/pull/103): New parser for IAR CSTAT warnings.
- [PR#100](https://github.com/jenkinsci/analysis-model/pull/100): Add package and module support for Pylint.

### Fixed
- [JENKINS-56393](https://issues.jenkins.io/browse/JENKINS-56393),
[PR#117](https://github.com/jenkinsci/analysis-model/pull/117): Improved RfLintParser - Set category and type.
- [JENKINS-56394](https://issues.jenkins.io/browse/JENKINS-56394),
[PR#117](https://github.com/jenkinsci/analysis-model/pull/117): Improved RfLintParser - add directory as the package name.
- [JENKINS-55805](https://issues.jenkins.io/browse/JENKINS-55805): 
JavaDoc Parser: Improved performance (skip overly long lines).
- [JENKINS-54506](https://issues.jenkins.io/browse/JENKINS-54506):
JavaDoc Parser: Java errors were detected as JavaDoc warnings.
- [JENKINS-55750](https://issues.jenkins.io/browse/JENKINS-55750),
[PR#102](https://github.com/jenkinsci/analysis-model/pull/102): 
IarParser: Added support for absolute Windows paths.
- [JENKINS-55733](https://issues.jenkins.io/browse/JENKINS-55733): 
CppCheck: Added support of multiple locations per warning.
- [JENKINS-55840](https://issues.jenkins.io/browse/JENKINS-55840), 
[JENKINS-27973](https://issues.jenkins.io/browse/JENKINS-27973),
[JENKINS-7178](https://issues.jenkins.io/browse/JENKINS-7178): 
Refactored Gcc4 parser so that it is also capable of parsing Doxygen warnings.
- [JENKINS-56020](https://issues.jenkins.io/browse/JENKINS-56020), 
[JENKINS-56193](https://issues.jenkins.io/browse/JENKINS-56193): 
Fixed absolute paths if Ninja build system is used.
- [JENKINS-56235](https://issues.jenkins.io/browse/JENKINS-56235): Fixed IDEA inspection file paths
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
- [JENKINS-56001](https://issues.jenkins.io/browse/JENKINS-56001): 
Improved absolute path detection: make detection independent of running OS.
- [PR#84](https://github.com/jenkinsci/analysis-model/pull/84): Regex tweak to restore support for older versions of IAR

## [2.1.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.0.2...analysis-model-2.1.0) - 2019-2-5

### Added
- [JENKINS-51267](https://issues.jenkins.io/browse/JENKINS-51267), 
[JENKINS-51438](https://issues.jenkins.io/browse/JENKINS-51438),
[JENKINS-55730](https://issues.jenkins.io/browse/JENKINS-55730),
[JENKINS-55775](https://issues.jenkins.io/browse/JENKINS-55775),
[JENKINS-55839](https://issues.jenkins.io/browse/JENKINS-55839),
[JENKINS-51439](https://issues.jenkins.io/browse/JENKINS-51439): Added folder of affected file as property.
- [JENKINS-55846](https://issues.jenkins.io/browse/JENKINS-55846): 
ErrorProne parser: Added support for Gradle reports.
- [JENKINS-55873](https://issues.jenkins.io/browse/JENKINS-55873): 
MavenConsole parser: Added support for maven-enforcer-plugin error messages.

### Fixed
- [JENKINS-55805](https://issues.jenkins.io/browse/JENKINS-55805): 
Java Ant Parser: Improved performance of ant task detection.
- [PR#82](https://github.com/jenkinsci/analysis-model/pull/82): CppLint: Fixed parser for messages with colon.
- [JENKINS-55715](https://issues.jenkins.io/browse/JENKINS-55715), 
[PR#81](https://github.com/jenkinsci/analysis-model/pull/81): 
Taglist Parser: Convert class name into a file name and a package.
- [PR#80](https://github.com/jenkinsci/analysis-model/pull/80): Keep PathUtil inside of sym-linked directories.

## [2.0.2](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.0.1...analysis-model-2.0.2) - 2019-1-21

### Fixed
- [PR#79](https://github.com/jenkinsci/analysis-model/pull/79): Correctly detect categories for Ansible Lint 4.x.

## [2.0.1](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.0.0...analysis-model-2.0.1) - 2019-1-16

### Fixed
- [JENKINS-55368](https://issues.jenkins.io/browse/JENKINS-55368): Fixed Eclipse parser for maven builds.

## [2.0.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-1.1.0...analysis-model-2.0.0) - 2019-1-15

### Added
- Added support for [ErrorProne](http://errorprone.info) in maven builds. Parser now reports description with link to external documentation.
- \[API\]: Added new base class [LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
that provides a lookahead of the next report line
- [JENKINS-55442](https://issues.jenkins.io/browse/JENKINS-55442), 
[PR#78](https://github.com/jenkinsci/analysis-model/pull/78): Added include/exclude filters for issue messages. 

### Changed
- Improved maven console parser: use the maven goal that logs a warning as issue type. Ignore all warnings
from the maven-compiler-plugin since these are already picked up by the Java parser.
- \[API\]: Replaced `CheckForNull` annotations with `Nullable` in order to enable [NullAway](https://github.com/uber/NullAway) checker in build

### Fixed
- [PR#74](https://github.com/jenkinsci/analysis-model/pull/74): IntelParser: Check for project number in regex.
- [JENKINS-25278](https://issues.jenkins.io/browse/JENKINS-25278): Improved performance of Maven console parser. 
- [JENKINS-55328](https://issues.jenkins.io/browse/JENKINS-55328): Show error message if symbol 'pmd' is used
- [JENKINS-55340](https://issues.jenkins.io/browse/JENKINS-55340), [PR#73](https://github.com/jenkinsci/analysis-model/pull/73): 
: Fixed PyLint parser: detect human readable categories. 
- [JENKINS-55358](https://issues.jenkins.io/browse/JENKINS-55358): Improved parser to support ECJ reports of ant. 
- [JENKINS-55368](https://issues.jenkins.io/browse/JENKINS-55368): Fixed parser to remove console notes. 

## [1.1.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-1.0.0...analysis-model-1.1.0) - 2018-12-20

### Added
- Added ModuleResolver from Jenkins warnings plugin.

## 1.0.0 - 2018-12-20

First public release.
