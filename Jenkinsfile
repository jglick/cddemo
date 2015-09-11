#!groovy
stage 'Dev'
node {
    checkout scm
    sh "mvn clean package"
    dir('target') {stash name: 'war', includes: 'x.war'}
}

stage 'QA'

node {
    unstash 'war'
    sh "cp x.war /var/lib/jetty/webapps/test.war"
}    

parallel(
    smokeTests: {
        node {
            checkout scm
            sh "mvn test -f sometests -P smoke -Durl=http://localhost:8080/test"
        }
    }, 
    acceptanceTests: {
        node {
            checkout scm
            sh "mvn test -f sometests -P acceptance -Durl=http://localhost:8080/test"
        }
    }
)

stage name: 'Staging', concurrency: 1

node {
    unstash 'war'
    sh "cp x.war /var/lib/jetty/webapps/staging.war"
}    

input message: "Does http://localhost:8080/staging/ look good?"

checkpoint('Before production')

stage name: 'Production', concurrency: 1
node {
    unstash 'war'
    sh "cp x.war /var/lib/jetty/webapps/production.war"
}    
