#!groovy?
BUILD_URL = "URL"
BUILD_TYPE = "Debug" // e.g. "Debug", "Release"
FLAVOUR = "Develop"
BUILD_NUMBER = "13"
EMAIL = "unai@baturamobile.com"
PORT_EMU_1 = "${env.PORT_EMU_1}"
PORT_EMU_2 = "${env.PORT_EMU_2}"

REPORT_PATH_UNIT_TESTS = 'app/build/reports/tests/testDebugUnitTest/'
REPORT_PATH_INSTRUMENTED_TESTS = 'app/build/reports/tests/testDebugUnitTest/'
REPORT_LINT_APP = 'app/build/reports/'
REPORT_LINT_BLUETOOTH = 'designlibrary-android/bluetooth/build/reports/'
REPORT_LINT_DESIGN = 'designlibrary-android/design/build/reports/'
REPORT_LINT_MVP = 'designlibrary-android/mvp/build/reports/'
REPORT_LINT_UTILS = 'designlibrary-android/utils/build/reports/'
TEAM_DOMAIN = 'baturamobile'

SLACK_CHANNEL = "#Jenkins"


node {
    sendJenkinsNotification("#FFFF00",SLACK_CHANNEL,"STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    mirroring();
    grantPermissionsGradlew()
    sendJenkinsNotification("#36ff33",SLACK_CHANNEL,"Finished SuccessFully: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

}

void checkout() {
    stage('Checkout') {
        checkout scm
    }
}

void mirroring() {
    stage('Mirroring to gitHub') {
         try {
            sh 'git clone --mirror git@bitbucket.org:baturamobile/designlibrary-android.git'
            sh 'cd designlibrary-android.git'
            sh 'git push --mirror https://github.com/BaturaMobile/android-libraries.git'
            slackSend color: 'good', channel: "#jenkins", message: "Finished Successfully: iOS Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})", botUser:true
         } catch (Exception e) {
            slackSend color: 'danger', channel: "#jenkins", message: "Job Failed: iOS Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})", botUser:true
         }
    }
}

void assemble() {
    stage('Assemble') {
        // Remove reports from previous build
        sh "rm -rf ../htmlreports/"
        sh "./gradlew clean assemble$FLAVOUR$BUILD_TYPE"
    }
}

void runUnitTests() {
    stage('Unit Tests') {
        Throwable error = null
        try {
            sh "./gradlew --stacktrace testDebug"
            publishUnitTestReport()
        } catch (e) {
            sendErrorNotification(SLACK_CHANNEL)
            error = e
        }
    }
}


void sendJenkinsNotification(color,channel,message){
    stage('sendSlack'){
        try {
          //  slackSend color: '#FFFF00',channel: "#ibil",message: "STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})", botUser:true
           slackSend color: color,channel: channel,message: message, botUser:true
        } catch (error) {
            throw error
        }
    }
}

void sendErrorNotification(channel){
     slackSend color: '#ff0000',channel: channel,message: "Job failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})", botUser:true
}

void sendEmailSuccess() {
    emailext body: BUILD_URL, subject: "Build $BUILD_NUMBER succeeded", to: EMAIL
}

void sendEmailFail() {
    if(currentBuild.result != 'FAILURE') {
        currentBuild.result = 'FAILURE'
        step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: EMAIL, sendToIndividuals: true])
    }
}