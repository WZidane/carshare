pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = "akizsmar"
        APP_IMAGE = "${DOCKER_REGISTRY}/carshare-app:latest"
        DB_IMAGE  = "${DOCKER_REGISTRY}/carshare-mysql:latest"
        SSH_KEY = "/var/lib/jenkins/.ssh/id_ed25519_jenkins"
        SSH_USER_A = "urca"
        SSH_USER_B = "urca"
        SSH_HOST_A = "10.11.19.2"
        SSH_HOST_B = "10.11.19.3"
    }

    tools {
        jdk '21'
        maven '3.9.11'
    }
    stages {
        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh 'docker login -u $USERNAME -p $PASSWORD'
                }
            }
        }
        stage('Build App') {
            steps {
                dir('app') {
                    sh 'mvn -B -DskipTests clean package'
                    sh "docker build -t ${APP_IMAGE} ."
                }
            }
        }
        stage('Build DB') {
            steps {
                dir('db') {
                    sh "docker build -t ${DB_IMAGE} ."
                }
            }
        }
        stage('Push Images') {
            steps {
                script {
                    sh "docker push ${APP_IMAGE}"
                    sh "docker push ${DB_IMAGE}"
                }
            }
        }
        stage('Deploy To Pre Prod') {
            steps {
                sh """
                    ssh -i ${SSH_KEY} ${SSH_USER}@${SSH_HOST} 'mkdir -p /opt/carshare'

                    scp -i ${SSH_KEY} docker-compose.yml ${SSH_USER_A}@${SSH_HOST_A}:/opt/carshare/docker-compose.yml
                    
                    ssh -i ${SSH_KEY} ${SSH_USER_A}@${SSH_HOST_A} '
                        cd /opt/carshare &&
                        docker-compose pull &&
                        docker-compose up -d
                    '
                """
            }
        }
    }
}