#!/usr/bin/env bash

#stack_name="sample-aws-xray-rds"
#stack_name="sample-aws-xray-frontend-ecs-cluster"
#stack_name="sample-aws-xray-frontend-alb"
stack_name="mynavi-sample-microservice-dynamodb"
#stack_name="mynavi-sample-microservice-sg"
#stack_name="mynavi-sample-microservice-vpc"
#template_path="cloudformation/3-rds-cfn.yml"
#template_path="cloudformation/3-frontend-ecs-cluster-cfn.yml"
#template_path="cloudformation/3-frontend-alb-cfn.yml"
template_path="cloudformation/2-dynamodb-cfn.yml"
#template_path="cloudformation/2-sg-cfn.yml"
#template_path="cloudformation/1-vpc-cfn.yml"
parameters="EnvType=Dev"
#aws cloudformation create-stack --stack-name ${stack_name} --template-body file://${template_path} --capabilities CAPABILITY_IAM
# It is better cloudformation deploy option because command can execute even if stack existing(no need to delete existing stack).

if [ "$parameters" == "" ]; then
    aws cloudformation deploy --stack-name ${stack_name} --template-file ${template_path} --capabilities CAPABILITY_IAM
else
    aws cloudformation deploy --stack-name ${stack_name} --template-file ${template_path} --parameter-overrides ${parameters} --capabilities CAPABILITY_NAMED_IAM
fi