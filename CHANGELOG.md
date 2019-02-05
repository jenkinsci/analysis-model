# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.1.0...master)

## [2.1.0](https://github.com/jenkinsci/analysis-model/compare/analysis-model-2.0.2...analysis-model-2.1.0)

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
- [API]: Added new base class [LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
that provides a lookahead of the next report line
- [JENKINS-55442](https://issues.jenkins-ci.org/browse/JENKINS-55442), 
[PR#78](https://github.com/jenkinsci/analysis-model/pull/78): Added include/exclude filters for issue messages. 

### Changed
- Improved maven console parser: use the maven goal that logs a warning as issue type. Ignore all warnings
from the maven-compiler-plugin since these are already picked up by the Java parser.
- [API]: Replaced `CheckForNull` annotations with `Nullable` in order to enable [NullAway](https://github.com/uber/NullAway) checker in build

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
