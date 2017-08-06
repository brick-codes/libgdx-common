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
        findbugs pattern: 'core/build/reports/findbugs/main.xml,desktop/build/reports/findbugs/main.xml'
        checkstyle pattern: 'core/build/reports/checkstyle/main.xml,desktop/build/reports/checkstyle/main.xml'
        pmd pattern: 'core/build/reports/pmd/main.xml,desktop/build/reports/pmd/main.xml'
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
