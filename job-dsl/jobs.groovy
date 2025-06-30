// This Groovy DSL script creates three Jenkins pipeline jobs.
// Each job uses a Jenkinsfile stored in the repository.

// Common defaults
def defaultRepo = 'https://github.com/youruser/ci-cd-assignment.git'  // Replace with your actual repo
def defaultBranch = '*/main'
def defaultTag = 'latest'

// List of job definitions
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

// Loop to define each pipeline job
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

        properties {
            pipelineTriggers {
                scm('H/5 * * * *') // optional: poll every 5 minutes
            }
        }

        disabled(false)
    }
}
