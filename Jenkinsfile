pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh './gradlew clean check jar'
      }
    }
    stage('Results') {
      steps {
        findbugs pattern: 'build/reports/findbugs/main.xml'
        checkstyle pattern: 'build/reports/checkstyle/main.xml'
        pmd pattern: 'build/reports/pmd/main.xml'
        warnings consoleParsers: [[parserName: 'Java Compiler (javac)']]
      }
    }
    stage('Deploy') {
      when {
        expression { env.BRANCH_NAME == "master" }
      }
      steps {
        withCredentials([usernamePassword(credentialsId: 'bintray',  usernameVariable: 'BINTRAY_USER', passwordVariable: 'BINTRAY_KEY')]) {
          sh './gradlew bintrayUpload'
        }
      }
    }
  }
}
