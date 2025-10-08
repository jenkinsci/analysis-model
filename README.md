# Static Analysis Model and Parsers Library

[![Join the chat at Gitter/Matrix](https://badges.gitter.im/jenkinsci/warnings-plugin.svg)](https://gitter.im/jenkinsci/warnings-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Jenkins](https://ci.jenkins.io/job/Plugins/job/analysis-model/job/main/badge/icon?subject=Jenkins%20CI)](https://ci.jenkins.io/job/Plugins/job/analysis-model/job/main/)
[![CI on all platforms](https://github.com/jenkinsci/analysis-model/workflows/GitHub%20CI/badge.svg)](https://github.com/jenkinsci/analysis-model/actions)
[![CodeQL](https://github.com/jenkinsci/analysis-model/workflows/CodeQL/badge.svg)](https://github.com/jenkinsci/analysis-model/actions/workflows/codeql.yml)
[![Line Coverage](https://raw.githubusercontent.com/jenkinsci/analysis-model/main/badges/line-coverage.svg)](https://github.com/uhafner/analysis-model/actions/workflows/quality-monitor-comment.yml)
[![Branch Coverage](https://raw.githubusercontent.com/jenkinsci/analysis-model/main/badges/branch-coverage.svg)](https://github.com/uhafner/analysis-model/actions/workflows/quality-monitor-comment.yml)

This library provides a Java API to read, aggregate, filter, and query static analysis reports. 
It is used by my [Jenkins' warnings plug-in](https://github.com/jenkinsci/warnings-ng-plugin) to visualize
the warnings of individual builds. 

![Jenkins Warnings Plug-in](doc/jenkins.png)   

Additionally, this library is used by my additional [Quality Monitor GitHub Action](https://github.com/uhafner/quality-monitor), that monitors the quality of projects based on a configurable set of metrics and gives feedback on pull requests (or single commits) in GitHub.
There are also two additional actions available, to autograde student software projects based
on these metrics: [GitHub Autograding action](https://github.com/uhafner/autograding-github-action) and [GitLab Autograding action](https://github.com/uhafner/autograding-gitlab-action).

![Quality Monitor GitHub Action](doc/quality-monitor.png)   

This library consists basically of three separate parts:

1. A model to manage a set of issues of static code analysis runs. This includes the possibility to track issues in
 different source code versions using a fingerprinting algorithm. 
2. Parsers for more than a hundred [report formats](SUPPORTED-FORMATS.md). Among the problems this library can detect:
   * messages from your build tool (Maven, Gradle, MSBuild, make, etc.)
   * errors from your compiler (C, C#, Java, etc.)
   * warnings from a static analysis tool (CheckStyle, StyleCop, SpotBugs, etc.)
   * duplications from a copy-and-paste detector (CPD, Simian, etc.)
   * vulnerabilities
   * open tasks in comments of your source files
3. Additional descriptions for a selected set of static analysis tools that provide details for individual violations 
   (including code samples, solutions, or quick fixes). 

All source code is licensed under the MIT license. 

Contributions to this library are welcome, please refer to the separate [CONTRIBUTING](CONTRIBUTING.md) document
for details on how to proceed! 
