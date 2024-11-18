pipeline {
    agent 'any'
    tools {
        maven 'mvn-default'
        jdk 'jdk-default'
    }
    stages {
        stage ('Build and Analysis') {
            steps {
                sh '${M2_HOME}/bin/mvn --batch-mode -V -U -e clean verify -Dsurefire.useFile=false -Dmaven.test.failure.ignore'
            }
        }
    }
    post {
        always {
            junit testResults: '**/target/surefire-reports/TEST-*.xml'

            recordIssues enabledForFailure: true, tool: mavenConsole()
            recordIssues enabledForFailure: true, tool: errorProne()
            recordIssues enabledForFailure: true, tool: java()
            recordIssues enabledForFailure: true, tool: checkStyle()
            recordIssues enabledForFailure: true, tool: spotBugs()
            recordIssues enabledForFailure: true, tool: cpd(pattern: '**/target/cpd.xml')
            recordIssues enabledForFailure: true, tool: pmdParser(pattern: '**/target/pmd.xml')
            recordIssues enabledForFailure: true, tool: taskScanner(includePattern:'**/*.java', highTags:'FIXME', normalTags:'TODO')
        }
    }
}
