pipeline {
    agent any

    stages {
        stage("Checkout") {
            steps {
                checkout scm
            }
        }

        stage("Run tests") {
            steps {
                sh "chmod +x run-tests.sh"
                sh "./run-tests.sh"
            }
        }
    }

    post {
        always {
            allure results: [[path: "allure-results"]]
        }
    }
}