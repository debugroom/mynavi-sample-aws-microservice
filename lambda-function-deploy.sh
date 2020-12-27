#!/usr/bin/env bash

export JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto.x86_64
bucket_name="debugroom-mynavi-microservice-cfn-lambda-bucket"
stack_name="mynavi-microservice-s3-lambda"
template_path="cloudformation/2-s3-for-lambda-deploy-cfn.yml"
s3_objectkey="cognito-init-lambda-0.0.1-SNAPSHOT-aws.jar"

if [ "" == "`aws s3 ls | grep $bucket_name`" ]; then
    aws cloudformation deploy --stack-name ${stack_name} --template-file ${template_path} --capabilities CAPABILITY_IAM
fi

cd cognito-init-lambda
./mvnw clean package
aws s3 cp target/${s3_objectkey} s3://${bucket_name}/