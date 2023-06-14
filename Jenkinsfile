//git凭证ID
def git_auth = "cca20337-815b-4731-bad7-27bd18fcf61d"
//git的url地址
def git_url = "https://gitee.com/dxx121/ruoyi_parent.git"
//镜像的版本号
def tag = "latest"

node {
    stage('拉取代码') {
      checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
    }
	
	stage('编译，项目依赖工程') {
      sh "mvn clean install"
    }

    stage('编译，项目依赖工程') {
      sh "mvn -f common clean install"
    }

    stage('编译，api_gateway') {
      sh "mvn -f infrastructure/api_gateway clean package"
    }
	
    stage('编译，service_cms') {
      sh "mvn -f service/service_cms clean package"
    }
	
    stage('编译，service_edu') {
      sh "mvn -f service/service_edu clean package"
    }
	
    stage('编译，service_oss') {
      sh "mvn -f service/service_oss clean package"
    }
	
    stage('编译，service_sms') {
      sh "mvn -f service/service_sms clean package"
    }
	
    stage('编译，service_statistics') {
      sh "mvn -f service/service_statistics clean package"
    }
	
    stage('编译，service_order') {
      sh "mvn -f service/service_order clean package"
    }
	
    stage('编译，service_ucenter') {
      sh "mvn -f service/service_ucenter clean package"
    }
	
    stage('编译，service_vod') {
      sh "mvn -f service/service_vod clean package"
    }
	
	stage('编译，service_acl') {
      sh "mvn -f service/service_acl clean package"
    }
	
}
