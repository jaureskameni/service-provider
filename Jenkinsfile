pipeline {
    agent any

    environment {
        PROJET_NAME    = "service-provider"
        PROJECT_FOLDER = "cm.klg.service-provider"
        IMAGE_NAME     = "${PROJET_NAME}"

        GITLAB_PROJECT_ID = "81753898"
        GITLAB_API_URL    = "https://gitlab.com/api/v4"

        GRADLE_DOCKER_IMAGE = "gradle:9.3.0-jdk25-alpine"

        NEXUS_DOCKER_URL = "localhost:8096"
    }

    stages {

        stage("INIT") {
            steps {
                // Notify GitLab that build has started
                withCredentials([string(
                        credentialsId: 'gitlab-status-token',
                        variable: 'GITLAB_TOKEN'
                )]) {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                        sh """
                            curl --fail --request POST \
                                 --header "PRIVATE-TOKEN: \${GITLAB_TOKEN}" \
                                 "${GITLAB_API_URL}/projects/${GITLAB_PROJECT_ID}/statuses/${GIT_COMMIT}" \
                                 --data "state=running&name=jenkins&target_url=${BUILD_URL}"
                        """
                    }
                }

                // Checkout submodule
                withCredentials([usernamePassword(
                        credentialsId: 'gitlab-credentials',
                        usernameVariable: 'GIT_USER',
                        passwordVariable: 'GIT_TOKEN'
                )]) {
                    sh """
                        git config --file=.gitmodules submodule."submodule/cm.klg.common.build".url \
                            "https://\${GIT_USER}:\${GIT_TOKEN}@gitlab.com/shared.modules/library/cm.klg.common.build.git"
                        git submodule sync
                        git submodule update --init --recursive
                        git config --file=.gitmodules submodule."submodule/cm.klg.common.build".url \
                            "https://gitlab.com/shared.modules/library/cm.klg.common.build.git"
                    """
                }
            }
        }

        stage("BUILD") {
            agent {
                docker {
                    image "${GRADLE_DOCKER_IMAGE}"
                    reuseNode true
                    args '-v /var/lib/jenkins/.gradle:/.gradle --network host'
                }
            }
            steps {
                dir("${PROJECT_FOLDER}") {
                    withCredentials([usernamePassword(
                            credentialsId: 'nexus-credentials',
                            usernameVariable: 'NEXUS_USERNAME',
                            passwordVariable: 'NEXUS_PASSWORD'
                    )]) {
                        script {
                            env.APP_VERSION = sh(
                                returnStdout: true,
                                script: "./gradlew properties -q -PnexusUsername=\${NEXUS_USERNAME} -PnexusPassword=\${NEXUS_PASSWORD} | grep \"^version:\" | awk '{print \$2}'"
                            ).trim()
                        }
                        sh """
                            ./gradlew clean build -x test \
                                -PnexusUsername=\${NEXUS_USERNAME} \
                                -PnexusPassword=\${NEXUS_PASSWORD}
                        """
                        stash name: 'jar', includes: 'build/libs/*.jar'
                    }
                }
            }
        }

        stage("TEST") {
            when {
                beforeAgent true
                expression { env.BRANCH_NAME != 'main' }
            }
            agent {
                docker {
                    image "${GRADLE_DOCKER_IMAGE}"
                    reuseNode true
                    args '-v /var/lib/jenkins/.gradle:/.gradle --network host'
                }
            }
            steps {
                dir("${PROJECT_FOLDER}") {
                    withCredentials([usernamePassword(
                            credentialsId: 'nexus-credentials',
                            usernameVariable: 'NEXUS_USERNAME',
                            passwordVariable: 'NEXUS_PASSWORD'
                    )]) {
                        sh """
                            ./gradlew test \
                                -PnexusUsername=\${NEXUS_USERNAME} \
                                -PnexusPassword=\${NEXUS_PASSWORD}
                        """
                    }
                }
            }
            post {
                always {
                    junit '**/build/test-results/**/TEST*.xml'
                }
            }
        }

        stage("PUBLISH SNAPSHOT") {
            when {
                beforeAgent true
                expression { env.BRANCH_NAME == 'develop' }
            }
            steps {
                dir("${PROJECT_FOLDER}") {
                    unstash 'jar'
                }
                withCredentials([usernamePassword(
                        credentialsId: 'nexus-credentials',
                        usernameVariable: 'NEXUS_USERNAME',
                        passwordVariable: 'NEXUS_PASSWORD'
                )]) {
                    sh """
                        echo "\$NEXUS_PASSWORD" | docker login "${NEXUS_DOCKER_URL}" \
                            -u "\$NEXUS_USERNAME" --password-stdin
                    """
                    sh """
                        docker build -t ${NEXUS_DOCKER_URL}/${IMAGE_NAME}:${env.APP_VERSION}-${env.BUILD_NUMBER} .
                        docker push ${NEXUS_DOCKER_URL}/${IMAGE_NAME}:${env.APP_VERSION}-${env.BUILD_NUMBER}
                    """
                }
            }
        }

        stage("PUBLISH RELEASE") {
            when {
                beforeAgent true
                expression { env.BRANCH_NAME == 'main' }
            }
            steps {
                dir("${PROJECT_FOLDER}") {
                    unstash 'jar'
                }
                withCredentials([
                        usernamePassword(
                                credentialsId: 'nexus-credentials',
                                usernameVariable: 'NEXUS_USERNAME',
                                passwordVariable: 'NEXUS_PASSWORD'
                        ),
                        usernamePassword(
                                credentialsId: 'gitlab-credentials',
                                usernameVariable: 'GIT_USER',
                                passwordVariable: 'GIT_TOKEN'
                        )
                ]) {
                    script {
                        def releaseVersion = env.APP_VERSION.replace("-SNAPSHOT", "")

                        dir("${PROJECT_FOLDER}") {
                            sh """
                                ./gradlew publish \
                                    -PnexusUsername=\${NEXUS_USERNAME} \
                                    -PnexusPassword=\${NEXUS_PASSWORD} \
                                    -PnexusSnapshotUrl=${NEXUS_SNAPSHOT_URL}
                            """
                        }

                        sh """
                            docker build -t ${NEXUS_DOCKER_URL}/${IMAGE_NAME}:${releaseVersion} .
                            docker push ${NEXUS_DOCKER_URL}/${IMAGE_NAME}:${releaseVersion}
                        """

                        sh """
                            git config user.email "jenkins@ci.local"
                            git config user.name  "Jenkins"
                            git tag ${releaseVersion} || true
                        """
                        sh """
                            REPO_URL=\$(git config --get remote.origin.url | sed 's#https://gitlab.com/##')
                            git remote set-url origin https://\${GIT_USER}:\${GIT_TOKEN}@gitlab.com/\${REPO_URL}
                            git push origin --tags
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                switch(currentBuild.currentResult) {
                    case 'SUCCESS':  env.GITLAB_STATE = 'success';  break
                    case 'FAILURE':  env.GITLAB_STATE = 'failed';   break
                    case 'ABORTED':  env.GITLAB_STATE = 'canceled'; break
                    case 'UNSTABLE': env.GITLAB_STATE = 'failed';   break
                    default:         env.GITLAB_STATE = 'failed'
                }

                withCredentials([string(
                        credentialsId: 'gitlab-status-token',
                        variable: 'GITLAB_TOKEN'
                )]) {
                    sh """
                        curl --fail --request POST \
                             --header "PRIVATE-TOKEN: \${GITLAB_TOKEN}" \
                             "${GITLAB_API_URL}/projects/${GITLAB_PROJECT_ID}/statuses/${GIT_COMMIT}" \
                             --data "state=${env.GITLAB_STATE}&name=jenkins&target_url=${BUILD_URL}"
                    """
                }
            }

            cleanWs()
            sh "docker system prune --force"
        }
    }
}