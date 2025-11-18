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
        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh 'docker login -u $USERNAME -p $PASSWORD'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'cp /home/urca/carshare/target/carshare-app.war .'

                    sh 'docker build -t akizsmar/carshare-app:latest .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    sh 'docker push akizsmar/carshare-app:latest'
                }
            }
        }
    }
}