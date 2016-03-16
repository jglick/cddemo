#!groovy
stage 'Dev'
node {
    checkout scm
    mvn "clean package"
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
            mvn "test -f sometests -P smoke -Durl=http://webserver:8080/test"
        }
    }, 
    acceptanceTests: {
        node {
            checkout scm
            mvn "test -f sometests -P acceptance -Durl=http://webserver:8080/test"
        }
    }
)

stage name: 'Staging', concurrency: 1

node {
    unstash 'war'
    sh "cp x.war /var/lib/jetty/webapps/staging.war"
}    

input message: "Does http://webserver:8080/staging/ look good?"

checkpoint('Before production')

stage name: 'Production', concurrency: 1
node {
    unstash 'war'
    sh "cp x.war /var/lib/jetty/webapps/production.war"
}    


// my custom utility methods

def mvn(args) {
    sh "${tool 'maven (latest)'}/bin/mvn ${args}"
}

