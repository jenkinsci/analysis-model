# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased](https://github.com/jenkinsci/analysis-model/compare/analysis-model-1.1.0...master)

### Added
- [API]: Added new base class [LookaheadParser](https://github.com/jenkinsci/analysis-model/blob/master/src/main/java/edu/hm/hafner/analysis/LookaheadParser.java) 
that provides a lookahead of the next report line

### Changed
- [API]: Replaced `CheckForNull` annotations with `Nullable` in order to enable [NullAway](https://github.com/uber/NullAway) checker in build

### Fixed
- [PR#74](https://github.com/jenkinsci/analysis-model/pull/74): IntelParser: Check for project number in regex.
- Maven Parser: Disabled post processing on agent since there are no source files involved.
 
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
