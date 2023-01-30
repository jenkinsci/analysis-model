<!--- DO NOT EDIT -- Generated at 2023-01-30T05:59:13.969365400 - Run the `main` method of `ParserRegistry` to regenerate after changing parsers -- DO NOT EDIT --->
# Supported Report Formats

The static analysis model supports the following report formats.

If your tool is not yet supported you can
1. export the issues of your tool to the native XML or JSON format (or any other format).
2. provide a [pull request](https://github.com/jenkinsci/analysis-model/pulls) with a new parser.

If your tool is supported, but some properties are missing (icon, URL, etc.), please file a
[pull request](https://github.com/jenkinsci/analysis-model/pulls).


<table>
    <thead>
        <tr>
            <th>
                ID
            </th>
            <th>
                Icons
            </th>
            <th>
                Name
            </th>
            <th>
                Default Pattern
            </th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>
                acu-cobol
            </td>
            <td>
                -
            </td>
            <td>
                AcuCobol
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                gnat
            </td>
            <td>
                -
            </td>
            <td>
                Ada Compiler (gnat)
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                android-lint
            </td>
            <td>
                -
            </td>
            <td>
                Android Lint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use the flag -p.
            </td>
        </tr>
        <tr>
            <td>
                ansiblelint
            </td>
            <td>
                <img src="https://raw.githubusercontent.com/benc-uk/icon-collection/master/logos/ansible.svg" alt="Ansible Lint" height="64" width="64">
            </td>
            <td>
                Ansible Lint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use the flag -p.
            </td>
        </tr>
        <tr>
            <td>
                scannercli
            </td>
            <td>
                <img src="https://github.com/aquasecurity/aqua-operator/raw/master/images/logo.svg" alt="Aqua Scanner" height="64" width="64">
            </td>
            <td>
                <a href="https://support.aquasec.com/support/solutions/articles/16000120206">
                    Aqua Scanner
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>scannercli scan &#x27;image&#x27; --jsonfile results.json</code>, see <a href="https://support.aquasec.com/support/solutions/articles/16000120206">Aqua Scanner CLI</a> for usage details.
            </td>
        </tr>
        <tr>
            <td>
                trivy
            </td>
            <td>
                <img src="https://github.com/aquasecurity/trivy/blob/main/docs/imgs/logo.png?raw=true" alt="Aquasec Trivy" height="64" width="64">
            </td>
            <td>
                <a href="https://github.com/aquasecurity/trivy">
                    Aquasec Trivy
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>trivy image -f json -o results.json &#x27;image&#x27;</code>, see <a href="https://github.com/aquasecurity/trivy">tivy on Github</a> for usage details.
            </td>
        </tr>
        <tr>
            <td>
                armcc
            </td>
            <td>
                -
            </td>
            <td>
                Armcc Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                aspectj
            </td>
            <td>
                -
            </td>
            <td>
                AspectJ
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                bluepearl
            </td>
            <td>
                -
            </td>
            <td>
                Blue Pearl Visual Verification Suite
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                brakeman
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://brakemanscanner.org">
                    Brakeman
                </a>
            </td>
            <td>
                **/brakeman-output.json
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Reads Brakeman JSON reports. Use commandline <code>brakeman -o brakeman-output.json</code> output.<br/>See <a href='https://brakemanscanner.org/docs/jenkins/'>Brakeman documentation</a> for usage details.
            </td>
        </tr>
        <tr>
            <td>
                buckminster
            </td>
            <td>
                -
            </td>
            <td>
                Buckminster
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                ccm
            </td>
            <td>
                -
            </td>
            <td>
                CCM
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                cmake
            </td>
            <td>
                -
            </td>
            <td>
                CMake
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                cpd
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://pmd.github.io/latest/pmd_userdocs_cpd.html">
                    CPD
                </a>
            </td>
            <td>
                **/cpd.xml
            </td>
        </tr>
        <tr>
            <td>
                cppcheck
            </td>
            <td>
                -
            </td>
            <td>
                CPPCheck
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use options --xml --xml-version=2
            </td>
        </tr>
        <tr>
            <td>
                csslint
            </td>
            <td>
                -
            </td>
            <td>
                CSS-Lint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                cadence
            </td>
            <td>
                -
            </td>
            <td>
                Cadence Incisive
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                cargo
            </td>
            <td>
                -
            </td>
            <td>
                Cargo Check
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>cargo check --message-format json</code>
            </td>
        </tr>
        <tr>
            <td>
                checkstyle
            </td>
            <td>
                <img src="https://github.com/checkstyle/checkstyle/blob/master/src/site/resources/images/checkstyle_logo_small_64.png" alt="CheckStyle" height="64" width="64">
            </td>
            <td>
                <a href="https://checkstyle.org">
                    CheckStyle
                </a>
            </td>
            <td>
                **/checkstyle-result.xml
            </td>
        </tr>
        <tr>
            <td>
                clair
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/arminc/clair-scanner">
                    Clair Scanner
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Reads Clair json data. Use commandline <code>clair-scanner --report="/target/clair.json"</code> output.<br/>See <a href='https://github.com/arminc/clair-scanner'>clair-scanner on Github</a> for usage details.
            </td>
        </tr>
        <tr>
            <td>
                clang
            </td>
            <td>
                -
            </td>
            <td>
                Clang
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                clang-analyzer
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://clang-analyzer.llvm.org">
                    Clang Analyzer
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use options --analyze --analyzer-output plist-multi-file
            </td>
        </tr>
        <tr>
            <td>
                clang-tidy
            </td>
            <td>
                -
            </td>
            <td>
                Clang-Tidy
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                code-analysis
            </td>
            <td>
                -
            </td>
            <td>
                Code Analysis
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                code-checker
            </td>
            <td>
                -
            </td>
            <td>
                CodeChecker
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                codenarc
            </td>
            <td>
                -
            </td>
            <td>
                CodeNarc
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                coolflux
            </td>
            <td>
                -
            </td>
            <td>
                Coolflux DSP Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                cpplint
            </td>
            <td>
                -
            </td>
            <td>
                Cpplint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: You need to use the Eclipse format with the option <code>--output=eclipse</code>
            </td>
        </tr>
        <tr>
            <td>
                dscanner
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/dlang-community/D-Scanner">
                    DScanner
                </a>
            </td>
            <td>
                **/dscanner-report.json
            </td>
        </tr>
        <tr>
            <td>
                dart
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://dart.dev/">
                    Dart Analyze
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                detekt
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://detekt.github.io/detekt">
                    Detekt
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use option --output-format xml.
            </td>
        </tr>
        <tr>
            <td>
                docfx
            </td>
            <td>
                -
            </td>
            <td>
                DocFX
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                dockerlint
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/projectatomic/dockerfile_lint">
                    Dockerfile Lint
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>dockerfile_lint -j</code> output.<br/>See <a href='https://github.com/projectatomic/dockerfile_lint'>dockerfile_lint on Github</a> for usage details.
            </td>
        </tr>
        <tr>
            <td>
                doxygen
            </td>
            <td>
                -
            </td>
            <td>
                Doxygen
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Execute doxygen:As <b>shell</b> command <code>( cat Doxyfile; echo WARN_FORMAT='$file:$line: $text' ) | doxygen -</code>As <b>batch</b> command <code>( type Doxyfile & echo WARN_FORMAT='$file:$line: $text' ) | doxygen -</code>
            </td>
        </tr>
        <tr>
            <td>
                dr-memory
            </td>
            <td>
                -
            </td>
            <td>
                Dr. Memory
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                eslint
            </td>
            <td>
                <img src="https://github.com/eslint/eslint/blob/main/docs/src/static/icon.svg" alt="ESLint" height="64" width="64">
            </td>
            <td>
                <a href="https://eslint.org">
                    ESLint
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use option --format checkstyle.
            </td>
        </tr>
        <tr>
            <td>
                eclipse
            </td>
            <td>
                -
            </td>
            <td>
                Eclipse ECJ
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: <p><p>Create an output file that contains Eclipse ECJ output, in either XML or text format.</p><p>To log in XML format, specify &quot;.xml&quot; as the file extension to the -log argument:</p><p><code>java -jar ecj.jar -log &lt;logfile&gt;.xml &lt;other arguments&gt;</code></p><p>To log in text format, specify any file extension except &quot;.xml&quot; to the -log argument:</p><p><code>java -jar ecj.jar -log &lt;logfile&gt;.log &lt;other arguments&gt;</code></p></p>
            </td>
        </tr>
        <tr>
            <td>
                erlc
            </td>
            <td>
                -
            </td>
            <td>
                Erlang Compiler (erlc)
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                error-prone
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://errorprone.info">
                    Error Prone
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                findbugs
            </td>
            <td>
                -
            </td>
            <td>
                FindBugs
            </td>
            <td>
                **/findbugsXml.xml
            </td>
        </tr>
        <tr>
            <td>
                flake8
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://flake8.pycqa.org/">
                    Flake8
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: <p>Run flake8 as <code>flake8 --format=pylint</code></p>
            </td>
        </tr>
        <tr>
            <td>
                flawfinder
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://dwheeler.com/flawfinder/">
                    FlawFinder
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>flawfinder -S</code>.
            </td>
        </tr>
        <tr>
            <td>
                flex
            </td>
            <td>
                -
            </td>
            <td>
                Flex SDK Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                flow
            </td>
            <td>
                <img src="https://raw.githubusercontent.com/facebook/flow/main/website/favicon.svg" alt="Flow" height="64" width="64">
            </td>
            <td>
                <a href="https://flow.org/">
                    Flow
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                foodcritic
            </td>
            <td>
                <img src="https://github.com/Foodcritic/foodcritic-site/blob/master/source/images/foodcritic.png" alt="Foodcritic" height="64" width="64">
            </td>
            <td>
                <a href="http://www.foodcritic.io/">
                    Foodcritic
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                fxcop
            </td>
            <td>
                -
            </td>
            <td>
                FxCop
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                ghs-multi
            </td>
            <td>
                -
            </td>
            <td>
                GHS Multi Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                gcc
            </td>
            <td>
                -
            </td>
            <td>
                GNU C Compiler (gcc)
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                gcc3
            </td>
            <td>
                -
            </td>
            <td>
                GNU C Compiler 3 (gcc)
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                fortran
            </td>
            <td>
                -
            </td>
            <td>
                GNU Fortran Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                gendarme
            </td>
            <td>
                -
            </td>
            <td>
                Gendarme
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                golint
            </td>
            <td>
                -
            </td>
            <td>
                Go Lint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                go-vet
            </td>
            <td>
                -
            </td>
            <td>
                Go Vet
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                hadolint
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/hadolint/hadolint">
                    HadoLint
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>hadolint --format json Dockerfile</code> output.<br/>See <a href='https://github.com/hadolint/hadolint'>hadolint on Github</a> for usage details.
            </td>
        </tr>
        <tr>
            <td>
                iar-cstat
            </td>
            <td>
                -
            </td>
            <td>
                IAR C-STAT
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: <p>The IAR C-STAT static analysis tool finds potential issues in code by doing an analysis on the source code level. Use the following icstat command to generate the output on stdout in the correct format: <pre><code>icstat --db a.db --checks checks.ch commands commands.txt</code></pre> where the commands.txt contains: <pre><code>analyze - iccxxxxcompiler_opts cstat1.c
analyze - iccxxxxcompiler_opts cstat2.c</pre></code>For details check the IAR C-STAT guide.</p>
            </td>
        </tr>
        <tr>
            <td>
                iar
            </td>
            <td>
                -
            </td>
            <td>
                IAR Compiler (C/C++)
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: The IAR compilers need to be started with option <strong>--no_wrap_diagnostics</strong>. Then the IAR compilers will create single-line warnings.
            </td>
        </tr>
        <tr>
            <td>
                xlc
            </td>
            <td>
                -
            </td>
            <td>
                IBM XLC Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                iblinter
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/IBDecodable/IBLinter">
                    IbLinter
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use configuration reporter: \”checkstyle\”.
            </td>
        </tr>
        <tr>
            <td>
                infer
            </td>
            <td>
                <img src="https://github.com/facebook/infer/blob/main/website/static/img/logo.png" alt="Infer" height="64" width="64">
            </td>
            <td>
                <a href="https://fbinfer.com">
                    Infer
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use option --pmd-xml.
            </td>
        </tr>
        <tr>
            <td>
                intel
            </td>
            <td>
                -
            </td>
            <td>
                Intel Compiler (C, Fortran)
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                idea
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://www.jetbrains.com/help/idea/code-inspection.html">
                    IntelliJ IDEA Inspections
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                jc-report
            </td>
            <td>
                -
            </td>
            <td>
                JCReport
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                jslint
            </td>
            <td>
                -
            </td>
            <td>
                JSLint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                junit
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://junit.org">
                    JUnit
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                java
            </td>
            <td>
                -
            </td>
            <td>
                Java Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                javadoc-warnings
            </td>
            <td>
                -
            </td>
            <td>
                JavaDoc
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                js-hint
            </td>
            <td>
                -
            </td>
            <td>
                JsHint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                klocwork
            </td>
            <td>
                -
            </td>
            <td>
                Klocwork
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                kotlin
            </td>
            <td>
                -
            </td>
            <td>
                Kotlin
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                ktlint
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://ktlint.github.io">
                    KtLint
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use option --reporter=checkstyle.
            </td>
        </tr>
        <tr>
            <td>
                msbuild
            </td>
            <td>
                -
            </td>
            <td>
                MSBuild
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                maven-warnings
            </td>
            <td>
                -
            </td>
            <td>
                Maven
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                taglist
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://www.mojohaus.org/taglist-maven-plugin">
                    Maven Taglist Plugin
                </a>
            </td>
            <td>
                **/taglist.xml
            </td>
        </tr>
        <tr>
            <td>
                modelsim
            </td>
            <td>
                -
            </td>
            <td>
                Mentor Graphics Modelsim/Questa Simulators
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                metrowerks
            </td>
            <td>
                -
            </td>
            <td>
                Metrowerks CodeWarrior Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: <p><p>Ensure that the output from the CodeWarrior build tools is in the expected format. If there are warnings present, but they are not found, then it is likely that the format is incorrect. The mwccarm compiler and mwldarm linker tools may support a configurable message style. This can be used to enforce the expected output format, which may be different from Metrowerks CodeWarrior (and thus require a different tool). For example the following could be appended to the build flags:</p><p><code>-msgstyle gcc -nowraplines</code></p></p>
            </td>
        </tr>
        <tr>
            <td>
                mypy
            </td>
            <td>
                -
            </td>
            <td>
                MyPy
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                nag-fortran
            </td>
            <td>
                -
            </td>
            <td>
                NAG Fortran Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                native
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/jenkinsci/warnings-ng-plugin/blob/master/doc/Documentation.md#export-your-issues-into-a-supported-format">
                    Native Analysis Model Format
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: <p>Create an output file that contains issues in the native analysis-model format, in either XML or JSON. The parser is even capable of reading individual lines of a log file that contains issues in JSON format.</p>
            </td>
        </tr>
        <tr>
            <td>
                ot-docker-linter
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/opstree/OT-Dockerlinter">
                    OT Docker Linter
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>ot-docker-linter audit --docker.file Dockerfile -o json</code> output.<br/>See <a href='https://github.com/opstree/OT-Dockerlinter'>ot-docker-linter on Github</a> for usage details.
            </td>
        </tr>
        <tr>
            <td>
                owasp-dependency-check
            </td>
            <td>
                <img src="https://raw.githubusercontent.com/jeremylong/DependencyCheck/main/src/site/resources/images/logo.svg" alt="OWASP Dependency Check" height="64" width="64">
            </td>
            <td>
                <a href="https://github.com/jeremylong/DependencyCheck">
                    OWASP Dependency Check
                </a>
            </td>
            <td>
                **/dependency-check-report.json
            </td>
        </tr>
        <tr>
            <td>
                invalids
            </td>
            <td>
                -
            </td>
            <td>
                Oracle Invalids
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                pclint
            </td>
            <td>
                -
            </td>
            <td>
                PC-Lint Tool
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: <p>Use the following PC-Lint properties to create an output file in the correct format: <pre><code>-v // turn off verbosity
-width(0) // don't insert line breaks (unlimited output width)
-"format=%f(%l): %t %n: %m"
-hs1 // The height of a message should be 1
</code></pre></p>
            </td>
        </tr>
        <tr>
            <td>
                pep8
            </td>
            <td>
                -
            </td>
            <td>
                PEP8
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                php
            </td>
            <td>
                -
            </td>
            <td>
                PHP Runtime
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                phpstan
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/phpstan/phpstan">
                    PHPStan
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use the options: --no-progress --error-format checkstyle
            </td>
        </tr>
        <tr>
            <td>
                php-code-sniffer
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/squizlabs/PHP_CodeSniffer">
                    PHP_CodeSniffer
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use option --report=checkstyle.
            </td>
        </tr>
        <tr>
            <td>
                pit
            </td>
            <td>
                <img src="https://pitest.org/images/pit-black-150x152.png" alt="PIT" height="64" width="64">
            </td>
            <td>
                <a href="https://pitest.org">
                    PIT
                </a>
            </td>
            <td>
                **/mutations.xml
            </td>
        </tr>
        <tr>
            <td>
                pmd
            </td>
            <td>
                <img src="https://github.com/pmd/pmd/blob/master/docs/images/logo/pmd_logo_small.png" alt="PMD" height="64" width="64">
            </td>
            <td>
                <a href="https://pmd.github.io">
                    PMD
                </a>
            </td>
            <td>
                **/pmd.xml
            </td>
        </tr>
        <tr>
            <td>
                prefast
            </td>
            <td>
                -
            </td>
            <td>
                PREfast
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                pvs-studio
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://pvs-studio.com/en/pvs-studio/">
                    PVS-Studio
                </a>
            </td>
            <td>
                **/*.plog
            </td>
        </tr>
        <tr>
            <td>
                perforce
            </td>
            <td>
                -
            </td>
            <td>
                Perforce Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                perl-critic
            </td>
            <td>
                -
            </td>
            <td>
                Perl::Critic
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                polyspace-parser
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://www.mathworks.com/products/polyspace.html">
                    Polyspace Tool
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Reads reports of Polyspace Static Analysis Tool by MathWorks. Used for <a href='https://www.mathworks.com/help/bugfinder/ref/polyspaceresultsexport.html?s_tid=srchtitle_polyspace-results-export_1'>BugFinder</a> and  <a href='https://www.mathworks.com/help/codeprover/ref/polyspaceresultsexport.html?s_tid=srchtitle_polyspace-results-export_2'>CodeProver</a> result files.<br/>Report can be generated with command: polyspace-results-export -format csv -results-dir <RESULTS> -output-name <CSVFILE> -key <KEY>
            </td>
        </tr>
        <tr>
            <td>
                protolint
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/yoheimuta/protolint">
                    ProtoLint
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                puppetlint
            </td>
            <td>
                -
            </td>
            <td>
                Puppet Lint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: You will need a recent enough version that supports <code>--log-format flag</code>. When running puppet-lint, make sure you use the log format <code>%{path}:%{line}:%{check}:%{KIND}:%{message}</code>. <br> Complete example: <br> <code>find. -iname *.pp -exec puppet-lint --log-format &quot;%{path}:%{line}:%{check}:%{KIND}:%{message}&quot; {} \;</code>
            </td>
        </tr>
        <tr>
            <td>
                pydocstyle
            </td>
            <td>
                -
            </td>
            <td>
                PyDocStyle
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                pylint
            </td>
            <td>
                -
            </td>
            <td>
                Pylint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: <p>Create a./pylintrc that contains:<p><code>msg-template={path}:{module}:{line}: [{msg_id}({symbol}), {obj}] {msg}</code></p></p><p>Start pylint using the command:<p><code>pylint --rcfile=./pylintrc CODE > pylint.log</code></p></p>
            </td>
        </tr>
        <tr>
            <td>
                qac
            </td>
            <td>
                -
            </td>
            <td>
                QA-C Sourcecode Analyser
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                qt-translation
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://www.qt.io">
                    Qt translations
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Reads translation files of Qt, which are created by "lupdate" or "Linguist".
            </td>
        </tr>
        <tr>
            <td>
                dupfinder
            </td>
            <td>
                -
            </td>
            <td>
                Resharper DupFinder
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                resharper
            </td>
            <td>
                -
            </td>
            <td>
                Resharper Inspections
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                revapi
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://revapi.org/revapi-site/main/index.html">
                    Revapi
                </a>
            </td>
            <td>
                **/target/revapi-result.json
            </td>
        </tr>
        <tr>
            <td>
                robocopy
            </td>
            <td>
                -
            </td>
            <td>
                Robocopy
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                rflint
            </td>
            <td>
                -
            </td>
            <td>
                Robot Framework Lint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                rubocop
            </td>
            <td>
                -
            </td>
            <td>
                Rubocop
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>rubocop --format progress</code>.
            </td>
        </tr>
        <tr>
            <td>
                sarif
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/oasis-tcs/sarif-spec">
                    SARIF
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                sunc
            </td>
            <td>
                -
            </td>
            <td>
                SUN C++ Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                scala
            </td>
            <td>
                -
            </td>
            <td>
                Scala Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                simian
            </td>
            <td>
                -
            </td>
            <td>
                Simian
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                simulink-check-parser
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://www.mathworks.com/products/simulink-check.html">
                    Simulink Check Tool
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Reads and Parses HTML reports of Simulink Check Tool by MathWorks. Report can be generated with command: <code>ModelAdvisor.summaryReport(ModelAdvisor.run(<SYSTEMS>, <CONFIG>, <FILENAME>, <ARGS>))</code>
            </td>
        </tr>
        <tr>
            <td>
                sonar
            </td>
            <td>
                -
            </td>
            <td>
                SonarQube Issues
            </td>
            <td>
                **/sonar-report.json
            </td>
        </tr>
        <tr>
            <td>
                sphinx
            </td>
            <td>
                -
            </td>
            <td>
                Sphinx Build
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                spotbugs
            </td>
            <td>
                <img src="https://github.com/spotbugs/spotbugs.github.io/blob/master/images/logos/spotbugs_icon_only_zoom_256px.png" alt="SpotBugs" height="64" width="64">
            </td>
            <td>
                <a href="https://spotbugs.github.io">
                    SpotBugs
                </a>
            </td>
            <td>
                **/spotbugsXml.xml
            </td>
        </tr>
        <tr>
            <td>
                stylecop
            </td>
            <td>
                -
            </td>
            <td>
                StyleCop
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                stylelint
            </td>
            <td>
                <img src="https://cdn.worldvectorlogo.com/logos/stylelint.svg" alt="Stylelint" height="64" width="64">
            </td>
            <td>
                <a href="https://stylelint.io/">
                    Stylelint
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Requires <a href='https://www.npmjs.com/package/stylelint-checkstyle-reporter'>stylelint-checkstyle-reporter</a>.<br/>Use <code>--custom-formatter node_modules/stylelint-checkstyle-reporter/index.js -o stylelint-warnings.xml</code>
            </td>
        </tr>
        <tr>
            <td>
                swiftlint
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/realm/SwiftLint">
                    SwiftLint
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use configuration reporter: \”checkstyle\”.
            </td>
        </tr>
        <tr>
            <td>
                tasking-vx
            </td>
            <td>
                -
            </td>
            <td>
                TASKING VX Compiler
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                tnsdl
            </td>
            <td>
                -
            </td>
            <td>
                TNSDL Translator
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                tslint
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://palantir.github.io/tslint/">
                    TSLint  
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use option --format checkstyle.
            </td>
        </tr>
        <tr>
            <td>
                code-composer
            </td>
            <td>
                -
            </td>
            <td>
                Texas Instruments Code Composer Studio
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                veracode-pipeline-scanner
            </td>
            <td>
                <img src="https://github.com/jenkinsci/veracode-scan-plugin/blob/master/src/main/webapp/icons/veracode-48x48.png" alt="Veracode Pipeline Scanner" height="64" width="64">
            </td>
            <td>
                <a href="https://docs.veracode.com/r/c_about_pipeline_scan">
                    Veracode Pipeline Scanner
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>java -jar pipeline-scan.jar --json_output=true --json_output_file=results.json</code>, see <a href="https://docs.veracode.com/r/c_about_pipeline_scan">Veracode Pipeline Scanner</a> for usage details.
            </td>
        </tr>
        <tr>
            <td>
                diabc
            </td>
            <td>
                -
            </td>
            <td>
                Wind River Diab Compiler (C/C++)
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                xmllint
            </td>
            <td>
                -
            </td>
            <td>
                XML-Lint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                yui
            </td>
            <td>
                -
            </td>
            <td>
                YUI Compressor
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                yamllint
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://yamllint.readthedocs.io/">
                    YamlLint
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use option -f parsable.
            </td>
        </tr>
        <tr>
            <td>
                zptlint
            </td>
            <td>
                -
            </td>
            <td>
                ZPT-Lint
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                oelint-adv
            </td>
            <td>
                -
            </td>
            <td>
                <a href="https://github.com/priv-kweihmann/oelint-adv">
                    oelint-adv
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td>
                pnpm-audit
            </td>
            <td>
                <img src="https://pnpm.io/assets/images/pnpm-standard-79c9dbb2e99b8525ae55174580061e1b.svg" alt="pnpm Audit" height="64" width="64">
            </td>
            <td>
                <a href="https://pnpm.io/cli/audit">
                    pnpm Audit
                </a>
            </td>
            <td>
                -
            </td>
        </tr>
        <tr>
            <td colspan="4">
                :bulb: Use commandline <code>pnpm audit --json &gt; pnpm-audit.json</code>, see <a href="https://pnpm.io/cli/audit">pnpm audit</a> for usage details.
            </td>
        </tr>
    </tbody>
</table>

