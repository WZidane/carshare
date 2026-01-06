pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = "akizsmar"
        REMOTE_APP_DIR = "~/carshare/"
        VENV_DIR = "~/carshare/venv"
        APP_IMAGE = "${DOCKER_REGISTRY}/carshare-app:latest"
        DB_IMAGE  = "${DOCKER_REGISTRY}/carshare-mysql:latest"
        SSH_KEY = "~/.ssh/id_ed25519_jenkins"
        SSH_USER_A = "urca"
        SSH_USER_B = "urca"
        SSH_USER_C = "urca"
        SSH_HOST_A = "10.11.19.1"
        SSH_HOST_B = "10.11.19.2"
        SSH_HOST_C = "10.11.19.3"
    }

    tools {
        jdk '21'
        maven '3.9.11'
    }
    stages {
        stage('Deploy To Pre Prod') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh """
                    ssh-keyscan -H ${SSH_HOST_B} >> ~/.ssh/known_hosts

                    ssh -i ${SSH_KEY} ${SSH_USER_B}@${SSH_HOST_B} 'mkdir -p ~/carshare'

                    scp -i ${SSH_KEY} docker-compose.yaml ${SSH_USER_B}@${SSH_HOST_B}:~/carshare/docker-compose.yml
                    
                    ssh -i ${SSH_KEY} ${SSH_USER_B}@${SSH_HOST_B} '
                        cd ~/carshare &&
                        docker login -u $USERNAME -p $PASSWORD &&
                        docker compose pull &&
                        docker compose up -d

                        if [ ! -d "${VENV_DIR}" ]; then
                            python3 -m venv ${VENV_DIR}
                        fi
                        
                        source ${VENV_DIR}/bin/activate &&
                        pip install --upgrade pip &&
                        pip install selenium locust pytest pytest-html webdriver-manager
                    '
                """
                }
            }
        }

        stage('Create Reports Directory') {
            steps {
                sh 'mkdir -p $WORKSPACE/reports'
            }
        }

        stage('Run Selenium') {
            steps {
                dir('tests') {
                    sh """
                    scp -i ${SSH_KEY} test_selenium.py ${SSH_USER_B}@${SSH_HOST_B}:~/carshare/test_selenium.py

                    ssh -i ${SSH_KEY} ${SSH_USER_B}@${SSH_HOST_B} '
                        cd ${REMOTE_APP_DIR} &&
                        source ${VENV_DIR}/bin/activate &&
                        pytest test_selenium.py --junitxml=selenium_report.xml --html=selenium_report.html --self-contained-html
                    '
                    """
                }
            }
        }
        stage('Get Reports Selenium') {
            steps {

                sh """scp ${SSH_USER_B}@${SSH_HOST_B}:~/carshare/selenium_report.* $WORKSPACE/reports"""
            }
        }
    }
    post {
        always {
            junit 'reports/selenium_report.xml'
            publishHTML([
                reportDir: 'reports',
                reportFiles: 'selenium_report.html',
                reportName: 'Carshare Selenium Report'
            ])
        }
    }
}