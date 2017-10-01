node {
    stage ('Checkout') {
        checkout scm
    }

    stage ('Build') {
        def mvnHome = tool 'mvn-default'

        sh "${mvnHome}/bin/mvn --batch-mode -V -U -e clean verify -Dsurefire.useFile=false"

        junit testResults: '**/target/surefire-reports/TEST-*.xml'
        warnings consoleParsers: [[parserName: 'Java Compiler (javac)']]
    }

    stage ('Analysis') {
        def mvnHome = tool 'mvn-default'

        sh "${mvnHome}/bin/mvn -batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd findbugs:findbugs"

        checkstyle()
        pmd()
        findbugs()
        dry()
        openTasks high: 'FIXME', normal: 'TODO'
    }

    stage ('Coverage') {
        def mvnHome = tool 'mvn-default'

        sh "${mvnHome}/bin/mvn -batch-mode -V -U -e clean jacoco:prepare-agent test jacoco:report"

        jacoco()
    }
}