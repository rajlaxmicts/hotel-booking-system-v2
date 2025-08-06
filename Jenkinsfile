pipeline {
    agent any

    tools {
        jdk 'Java 17'
        maven 'Maven 3.6'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/rajlaxmicts/hotel-booking-system-v2.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Build successful!'
        }
        failure {
            echo '❌ Build failed. Check logs.'
        }
    }
}
