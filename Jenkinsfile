pipeline {
    agent any
    tools {
        jdk '21'
        maven '3.9.11'
    }
    stage('Build Docker Image') {
        steps {
            script {
                docker.build('carshare', '.')
            }
        }
    }
}