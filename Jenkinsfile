pipeline {
    agent any

    tools {
        jdk 'Java 21.0.8'
        maven 'Maven 3.8.7'
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
                sh 'cp target/hotel-booking-chatbot-0.0.1-SNAPSHOT.jar target/hotel-booking-system.jar'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying using Docker...'
                sh '''
                docker build -t hotel-booking-app .
                docker stop hotel-booking-container || true
                docker rm hotel-booking-container || true
                docker run -d --name hotel-booking-container -p 8081:8081 hotel-booking-app
                '''
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
