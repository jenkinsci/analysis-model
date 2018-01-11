This 'analysis-model' project is a library to read static analysis reports into a Java object model. 
Currently it is used only by [Jenkins' warnings plug-in](https://wiki.jenkins.io/display/JENKINS/Warnings+Plugin). 
Since this library has no dependencies to the Jenkins project it might be used by other static analysis visualization 
tools as well in the future.

This library consists basically of two separate parts:
1. A model to manage a set of issues of static code analysis runs. This includes the possibility to track issues in
 different source code versions using a fingerprinting algorithm. 
2. Parsers for numerous static analysis tools (including several compilers), see the 
 [parser source folder](src/main/java/edu/hm/hafner/analysis/parser) for a complete list of supported formats.

All source code is licensed under the MIT license. 

[![Jenkins](https://ci.jenkins.io/job/Plugins/job/analysis-model/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/analysis-model/job/master/)
[![Travis](https://img.shields.io/travis/jenkinsci/analysis-model.svg)](https://travis-ci.org/jenkinsci/analysis-model)
[![Codecov](https://img.shields.io/codecov/c/github/jenkinsci/analysis-model.svg)](https://codecov.io/gh/jenkinsci/analysis-model)
[![Coverity](https://img.shields.io/coverity/scan/14205.svg)](https://scan.coverity.com/projects/jenkinsci-analysis-model)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/jenkinsci/analysis-model.svg)](https://github.com/jenkinsci/analysis-model/pulls)
![JDK8](https://img.shields.io/badge/jdk-8-yellow.svg)
[![License: MIT](https://img.shields.io/badge/license-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
