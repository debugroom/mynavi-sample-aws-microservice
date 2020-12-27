#!/usr/bin/env bash

#stack_name="sample-aws-xray-rds"
#stack_name="sample-aws-xray-frontend-ecs-cluster"
#stack_name="sample-aws-xray-frontend-alb"
#stack_name="sample-aws-xray-dynamodb"
#stack_name="mynavi-sample-microservice-lambda-trigger"
stack_name="mynavi-sample-microservice-cognito-init-lambda"
#stack_name="mynavi-sample-microservice-cognito"
#stack_name="mynavi-sample-microservice-sg"
#stack_name="mynavi-sample-microservice-vpc"

aws cloudformation delete-stack --stack-name ${stack_name}