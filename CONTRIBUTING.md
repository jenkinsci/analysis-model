# Contributing to the Static Analysis Model and Parsers Library

This document provides information about contributing code to the Static Analysis Model and Parsers Library. 

## Beginner Topics

If you don't have a specific problem or task in mind, i.e.,  you simply want to participate in this open source project 
I would suggest to have a look at the 
[open issues in our issues tracker](https://issues.jenkins-ci.org/issues/?filter=-1&jql=resolution%20%3D%20Unresolved%20AND%20component%20%3D%20analysis-model%20AND%20labels%20in%20(help-wanted%2C%20newbie-friendly)%20order%20by%20updated%20DESC). 
I marked several newbie friendly issues with the labels `newbie-friendly` and `help-wanted`. These are a good starting
point to get in touch with this library.

## Getting started

Setup your development environment as described in 
[Development environment for Jenkins' Warnings Next Generation Plugin](https://github.com/uhafner/warnings-ng-plugin-devenv).

## Coding Guidelines

Start reading the code and you'll get the hang of it. A complete description of the 
coding guidelines is part of a [separate GitHub repository](https://github.com/uhafner/codingstyle), which 
is only available in German. 

For [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/) users: 
the coding style is stored in configuration files within this project. If you import this project into 
IntelliJ or Eclipse this style will used automatically. 

Moreover (since this project is about static code analysis :wink:) a configuration for the following static code
analysis tools is defined in the POM and the `etc` and `.idea` folders:
- [Checkstyle](http://checkstyle.sourceforge.net/)
- [PMD](https://pmd.github.io/)
- [SpotBugs](https://spotbugs.github.io)
- [Error Prone](http://errorprone.info)
- [IntelliJ](https://www.jetbrains.com/help/idea/code-inspection.html)

This configuration will be picked up automatically if you build the project using maven. If you install the CheckStyle 
plugin of IntelliJ then the correct set of CheckStyle rules will used automatically. Moreover, the code formatter and 
the inspection rules will be automatically picked up by IntelliJ.

## Proposing Changes

All proposed changes are submitted and code reviewed using the 
[GitHub Pull Request](https://help.github.com/articles/about-pull-requests/) process.

To submit a pull request:

1. Commit changes and push them to your fork on GitHub.
It is a good practice is to create branches instead of pushing to master.
2. In GitHub Web UI click the **New Pull Request** button.
3. Select `analysis-model` as **base fork** and `master` as **base**, then click **Create Pull Request**.
4. Fill in the Pull Request description. It should reflect the changes, the reason behind the changes, and if available a
reference to the Jenkins ticket in our [issue tracker](https://issues.jenkins-ci.org/).
5. Click **Create Pull Request**.
6. Wait for CI results, reviews. 
7. Process the feedback (see previous step). If there are changes required, commit them in your local branch and push them
again to GitHub. Your pull request will be updated automatically. Review comments for changed lines will become outdated.

Once your Pull Request is ready to be merged, the repository maintainer will integrate it.
There is no additional action required from pull request authors at this point.

## Copyright

The Static Analysis Model and Parsers Library  is licensed under [MIT license](./LICENSE). 
We consider all contributions as MIT unless it's explicitly stated otherwise. 
MIT-incompatible code contributions will be rejected.
Contributions under MIT-compatible licenses may be also rejected if they are not ultimately necessary.

## Continuous Integration

The Jenkins project has a Continuous Integration server... powered by Jenkins, of course.
The CI job for this project is located at [ci.jenkins.io](https://ci.jenkins.io/job/Plugins/job/analysis-model/).

