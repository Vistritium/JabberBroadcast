pipeline {
    agent any

    environment {
        DOCKER_LOGIN_USERNAME = 'nowicki'
        DOCKER_LOGIN_PASSWORD = credentials('DOCKER_REGISTRY_PASSWORD')
        DOCKER_REPO = 'nowicki.azurecr.io'
        DOCKER_APP_NAME = 'jabber-broadcast'
    }

    stages {
        stage('Build, publish docker image'){
            steps {
                sh 'docker login -u ${DOCKER_LOGIN_USERNAME} -p ${DOCKER_LOGIN_PASSWORD} ${DOCKER_REPO}'
                sh 'for i in $(seq 1 3); do sbt -no-colors docker:publish && s=0 && break || s=$? && sleep 1; done; (exit $s)'
            }
        }
        stage('Restart service') {
            steps {
                sh 'docker pull nowicki.azurecr.io/nowicki/${DOCKER_APP_NAME}:latest'
                //sh 'docker service update --with-registry-auth --image nowicki.azurecr.io/nowicki/${DOCKER_APP_NAME}:latest czapli-stack_app'

            }
        }
    }

}