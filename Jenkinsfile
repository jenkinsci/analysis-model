def configurations = [
  [ platform: "linux", jdk: "17" ],
  [ platform: "windows", jdk: "17" ]
]

buildPlugin(failFast: false, configurations: configurations,
    checkstyle: [qualityGates: [[threshold: 1, type: 'NEW', unstable: true]]],
    pmd: [qualityGates: [[threshold: 1, type: 'NEW', unstable: true]]] )
