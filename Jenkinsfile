pipeline {
    agent any
    
    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-credentials')
        DOCKERHUB_USERNAME = 'diegoraar'
        PROJECT_NAME = 'microservicios-tingeso'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Clonando repositorio...'
                checkout scm
            }
        }
        
        stage('Build Maven Projects') {
            steps {
                echo 'Compilando microservicios con Maven...'
                script {
                    def services = [
                        'config-service',
                        'eureka-service',
                        'gateway-service',
                        'client-service',
                        'kardex-service',
                        'loan-service',
                        'price-service',
                        'report-service',
                        'tool-service'
                    ]
                    
                    for (service in services) {
                        echo "Building ${service}..."
                        dir(service) {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                echo 'Construyendo imágenes Docker...'
                script {
                    def services = [
                        'config-service',
                        'eureka-service',
                        'gateway-service',
                        'client-service',
                        'kardex-service',
                        'loan-service',
                        'price-service',
                        'report-service',
                        'tool-service'
                    ]
                    
                    for (service in services) {
                        echo "Building Docker image for ${service}..."
                        dir(service) {
                            sh "docker build -t ${DOCKERHUB_USERNAME}/${service}:latest ."
                            sh "docker build -t ${DOCKERHUB_USERNAME}/${service}:${BUILD_NUMBER} ."
                        }
                    }
                }
            }
        }
        
        stage('Login to DockerHub') {
            steps {
                echo 'Iniciando sesión en DockerHub...'
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }
        
        stage('Push to DockerHub') {
            steps {
                echo 'Subiendo imágenes a DockerHub...'
                script {
                    def services = [
                        'config-service',
                        'eureka-service',
                        'gateway-service',
                        'client-service',
                        'kardex-service',
                        'loan-service',
                        'price-service',
                        'report-service',
                        'tool-service'
                    ]
                    
                    for (service in services) {
                        echo "Pushing ${service} to DockerHub..."
                        sh "docker push ${DOCKERHUB_USERNAME}/${service}:latest"
                        sh "docker push ${DOCKERHUB_USERNAME}/${service}:${BUILD_NUMBER}"
                    }
                }
            }
        }
        
        stage('Cleanup') {
            steps {
                echo 'Limpiando imágenes locales...'
                sh 'docker image prune -f'
            }
        }
    }
    
    post {
        always {
            echo 'Cerrando sesión de DockerHub...'
            sh 'docker logout'
        }
        success {
            echo '✅ Pipeline ejecutado exitosamente!'
        }
        failure {
            echo '❌ El pipeline ha fallado.'
        }
    }
}