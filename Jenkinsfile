pipeline {
#  agent { docker 'maven:3.3.3' }
  wrappers {
    buildInDocker {
        image('centos:7')
        volume('/dev/urandom', '/dev/random')
        verbose()
    }
 }
  stages {
    stage('build') {
      steps {
        sh 'mvn --version'
        sh 'mvn install'
      }
    }
  }
}
