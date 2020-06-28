# Static Analysis Model and Parsers Library

[![Join the chat at https://gitter.im/jenkinsci/warnings-plugin](https://badges.gitter.im/jenkinsci/warnings-plugin.svg)](https://gitter.im/jenkinsci/warnings-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Jenkins Plugin](https://img.shields.io/github/release/jenkinsci/analysis-model.svg?label=changelog)](https://github.com/jenkinsci/analysis-model/releases/latest)
[![Jenkins](https://ci.jenkins.io/job/Plugins/job/analysis-model/job/master/badge/icon?subject=Jenkins%20CI)](https://ci.jenkins.io/job/Plugins/job/analysis-model/job/master/)
[![CI on all platforms](https://github.com/jenkinsci/analysis-model/workflows/GitHub%20CI/badge.svg?branch=master)](https://github.com/jenkinsci/analysis-model/actions)
[![Codacy](https://api.codacy.com/project/badge/Grade/1b96405c72db49eeb0d67486f77f8f75)](https://app.codacy.com/app/uhafner/analysis-model?utm_source=github.com&utm_medium=referral&utm_content=jenkinsci/analysis-model&utm_campaign=Badge_Grade_Dashboard)
[![codecov](https://codecov.io/gh/jenkinsci/analysis-model/branch/master/graph/badge.svg)](https://codecov.io/gh/jenkinsci/analysis-model)

This project is a library to read static analysis reports into a Java object model. 
Currently, it is used only by [Jenkins' warnings next generation plug-in](https://github.com/jenkinsci/warnings-ng-plugin). 
Since this library has no dependencies to the Jenkins project it might be used by other static analysis visualization 
tools as well in the future.

This library consists basically of two separate parts:
1. A model to manage a set of issues of static code analysis runs. This includes the possibility to track issues in
 different source code versions using a fingerprinting algorithm. 
2. Parsers for numerous static analysis tools (including several compilers), see the 
 [parser source folder](src/main/java/edu/hm/hafner/analysis/parser) for a complete list of supported formats.

All source code is licensed under the MIT license. 

