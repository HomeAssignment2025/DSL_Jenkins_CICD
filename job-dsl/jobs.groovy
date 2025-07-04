def defaultRepo = 'https://github.com/HomeAssignment2025/DSL_Jenkins_CICD.git'
def defaultBranch = '*/main'
def defaultTag = 'latest'

def pipelineJobs = [
    [
        name: 'build-python-api',
        description: 'Builds and pushes the Flask Docker image',
        scriptPath: 'flask-api/Jenkinsfile'
    ],
    [
        name: 'build-nginx-proxy',
        description: 'Builds and pushes the Nginx reverse proxy image',
        scriptPath: 'nginx-proxy/Jenkinsfile'
    ],
    [
        name: 'run-local-containers',
        description: 'Runs both containers and verifies proxy -> Flask communication',
        scriptPath: 'runner-job/Jenkinsfile'
    ]
]

pipelineJobs.each { jobDef ->
    pipelineJob(jobDef.name) {
        description(jobDef.description)

        parameters {
            stringParam('BRANCH_NAME', defaultBranch, 'Git branch to build')
            stringParam('DOCKER_IMAGE_TAG', defaultTag, 'Docker image tag')
        }

        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url(defaultRepo)
                        }
                        branches('$BRANCH_NAME')
                    }
                }
                scriptPath(jobDef.scriptPath)
            }
        }

        logRotator {
            daysToKeep(14)
            numToKeep(25)
        }

        // Removed properties { pipelineTriggers { scm(...) } } to avoid Job DSL error
        disabled(false)
    }
}
