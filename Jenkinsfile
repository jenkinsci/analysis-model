node {
    stage ('Checkout') {
        checkout scm
    }

    stage ('Build') {
        withMaven(maven: 'mvn-default', mavenLocalRepo: '/var/data/m2repository', mavenOpts: '-Xmx768m -Xms512m') {
            sh 'mvn -V -U -e clean verify -DskipTests'
        }

        recordIssues tools: [java(), javaDoc()], aggregatingResults: 'true', id: 'java', name: 'Java'
        recordIssues tool: errorProne()
    }

    stage ('Analysis') {
        withMaven(maven: 'mvn-default', mavenLocalRepo: '/var/data/m2repository', mavenOpts: '-Xmx768m -Xms512m') {
            sh "mvn -V -U -e verify"
        }

        junit testResults: '**/target/*-reports/TEST-*.xml'
        recordIssues tools: [checkStyle(),
            spotBugs(pattern: 'target/spotbugsXml.xml'),
            pmdParser(pattern: 'target/pmd.xml'),
            cpd(pattern: 'target/cpd.xml'),
            taskScanner(highTags:'FIXME', normalTags:'TODO', includePattern: '**/*.java', excludePattern: 'target/**/*')]
    }

    stage ('Coverage') {
        withMaven(maven: 'mvn-default', mavenLocalRepo: '/var/data/m2repository', mavenOpts: '-Xmx768m -Xms512m') {
            sh "mvn -V -U -e jacoco:prepare-agent test jacoco:report -Djenkins.test.timeout=240"
        }
        publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')]
    }

    stage ('Collect Maven Warnings') {
        recordIssues tool: mavenConsole()
    }
}
