def configurations = [
  [ platform: "linux", jdk: "11" ],
  [ platform: "windows", jdk: "11" ]
]

buildPlugin(failFast: false, configurations: configurations,
    checkstyle: [qualityGates: [[threshold: 1, type: 'NEW', unstable: true]]],
    pmd: [qualityGates: [[threshold: 1, type: 'NEW', unstable: true]]] )
