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
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh """
                    ssh-keyscan -H ${SSH_HOST_B} >> ~/.ssh/known_hosts

                    ssh -i ${SSH_KEY} ${SSH_USER_B}@${SSH_HOST_B} 'mkdir -p ${REMOTE_APP_DIR}'

                    scp -i ${SSH_KEY} docker-compose.yaml ${SSH_USER_B}@${SSH_HOST_B}:${REMOTE_APP_DIR}docker-compose.yaml
                    
                    ssh -i ${SSH_KEY} ${SSH_USER_B}@${SSH_HOST_B} '
                        cd ${REMOTE_APP_DIR} &&
                        docker login -u $USERNAME -p $PASSWORD &&
                        docker compose pull &&
                        docker compose up -d

                        if [ ! -d "${VENV_DIR}" ]; then
                            python3 -m venv ${VENV_DIR}
                        fi
                        
                        source ${VENV_DIR}/bin/activate &&
                        pip install --upgrade pip &&
                        pip install selenium pytest pytest-html
                    '
                """
                }
            }
        }

        stage('Run Selenium On Pre Prod') {
            steps {
                sh 'mkdir -p $WORKSPACE/reports'

                dir('tests') {
                    sh """
                    scp -i ${SSH_KEY} test_selenium.py ${SSH_USER_B}@${SSH_HOST_B}:${REMOTE_APP_DIR}

                    ssh -i ${SSH_KEY} ${SSH_USER_B}@${SSH_HOST_B} "
                        cd ${REMOTE_APP_DIR} &&
                        source ${VENV_DIR}/bin/activate &&
                        pytest test_selenium.py --junitxml=selenium_report.xml --html=selenium_report.html --self-contained-html
                    "
                    """
                }
            }
        }

        stage('Get Selenium Reports') {
            steps {
                sh """
                scp -i ${SSH_KEY} ${SSH_USER_B}@${SSH_HOST_B}:${REMOTE_APP_DIR}selenium_report.* $WORKSPACE/reports/
                """
            }
        }

        stage('Deploy To Prod') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh """
                    ssh-keyscan -H ${SSH_HOST_C} >> ~/.ssh/known_hosts

                    ssh -i ${SSH_KEY} ${SSH_USER_C}@${SSH_HOST_C} 'mkdir -p ${REMOTE_APP_DIR}'

                    scp -i ${SSH_KEY} docker-compose.yaml ${SSH_USER_C}@${SSH_HOST_C}:${REMOTE_APP_DIR}docker-compose.yaml
                    
                    ssh -i ${SSH_KEY} ${SSH_USER_C}@${SSH_HOST_C} '
                        cd ${REMOTE_APP_DIR} &&
                        docker login -u $USERNAME -p $PASSWORD &&
                        docker compose pull &&
                        docker compose up -d

                        if [ ! -d "${VENV_DIR}" ]; then
                            python3 -m venv ${VENV_DIR}
                        fi
                        
                        source ${VENV_DIR}/bin/activate &&
                        pip install --upgrade pip &&
                        pip install locust
                    '
                """
                }
            }
        }

        stage('Run Locust On Prod') {
            steps {
                dir('tests') {
                    sh """
                    scp -i ${SSH_KEY} locustfile.py ${SSH_USER_C}@${SSH_HOST_C}:${REMOTE_APP_DIR}locustfile.py

                    ssh -i ${SSH_KEY} ${SSH_USER_C}@${SSH_HOST_C} '
                        cd ${REMOTE_APP_DIR} &&
                        source ${VENV_DIR}/bin/activate &&
                        pip install locust &&
                        locust -f locustfile.py --headless -u 10 -r 2 --run-time 1m --html=locust_report.html
                    '
                    """
                }
            }
        }

        stage('Get Locust Reports') {
            steps {
                sh """
                scp -i ${SSH_KEY} ${SSH_USER_B}@${SSH_HOST_B}:${REMOTE_APP_DIR}locust_report.* $WORKSPACE/reports/
                """
            }
        }
    }

    post {
        always {
            junit 'reports/selenium_report.xml'

            publishHTML([
                allowMissing: false,            
                alwaysLinkToLastBuild: true,   
                keepAll: true,               
                reportDir: 'reports',
                reportFiles: 'selenium_report.html',
                reportName: 'Carshare Selenium Report'
            ])

            publishHTML([
                allowMissing: false,            
                alwaysLinkToLastBuild: true,   
                keepAll: true,            
                reportDir: 'reports',
                reportFiles: 'locust_report.html',
                reportName: 'Carshare Locust Report',
            ])
        }
    }
}