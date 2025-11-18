pipeline {
    agent any
    tools {
        jdk '21'
        maven '3.9.11'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
    }
}