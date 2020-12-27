package org.debugroom.mynavi.sample.aws.microservice.lambda.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.debugroom.mynavi.sample.aws.microservice.lambda.app.handler.CloudFormationTriggerHandler;
import org.debugroom.mynavi.sample.aws.microservice.lambda.config.App;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CloudFormationTriggerHanlderTest {

    ObjectMapper mapper = new ObjectMapper();

    String cloudFormationRequest = "{\n" +
            "    \"RequestType\": \"Test\",\n" +
            "    \"ServiceToken\": \"arn:aws:lambda:ap-northeast-1:123456789012:function:mynavi-microservice-cfn-cognito-user-status-change-function\",\n" +
            "    \"StackId\": \"arn:aws:cloudformation:ap-northeast-1:123456789012:stack/mynavi-sample-microservice-lambda-trigger/9c15a480-4501-11eb-84b9-0a6a4edc26e4\",\n" +
            "    \"RequestId\": \"592898cf-8254-4571-8298-1c2d6c9255e3\",\n" +
            "    \"LogicalResourceId\": \"LambdaTrigger\",\n" +
            "    \"ResourceType\": \"Custom::LambdaTrigger\",\n" +
           "    \"ResponseURL\": \"https://cloudformation-custom-resource-response-apnortheast1.s3-ap-northeast-1.amazonaws.com/arn%3Aaws%3Acloudformation%3Aap-northeast-1%3A576249913131%3Astack/mynavi-sample-microservice-lambda-trigger/9c15a480-4501-11eb-84b9-0a6a4edc26e4%7CLambdaTrigger%7C592898cf-8254-4571-8298-1c2d6c9255e3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20201223T093128Z&X-Amz-SignedHeaders=host&X-Amz-Expires=7200&X-Amz-Credential=AKIAXXXXXXXXXXXXXX%2F20201223%2Fap-northeast-1%2Fs3%2Faws4_request&X-Amz-Signature=1234567890\",\n" +
            "    \"ResourceProperties\": {\n" +
            "        \"ServiceToken\": \"arn:aws:lambda:ap-northeast-1:123456789012:function:mynavi-microservice-cfn-cognito-user-status-change-function\",\n" +
            "        \"Region\": \"ap-northeast-1\"\n" +
           "    }\n" +
            "}";

//    @Test
    public void testCloudFormationChangeCognitoUserStatusFunction() throws Exception {
        System.setProperty("MAIN_CLASS", App.class.getName());
        System.setProperty("spring.cloud.function.definition", "changeCognitoUserStatusFunction");
        CloudFormationTriggerHandler invoker = new CloudFormationTriggerHandler();

        InputStream targetStream = new ByteArrayInputStream(cloudFormationRequest.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        invoker.handleRequest(targetStream, outputStream, null);
    }

//    @Test
    public void testCloudFormationAddCognitoUserFunction() throws Exception {
        System.setProperty("MAIN_CLASS", App.class.getName());
        System.setProperty("spring.cloud.function.definition", "addCognitoUserFunction");
        CloudFormationTriggerHandler invoker = new CloudFormationTriggerHandler();

        InputStream targetStream = new ByteArrayInputStream(cloudFormationRequest.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        invoker.handleRequest(targetStream, outputStream, null);
    }

//    @Test
    public void testCloudFormationAddClientSecretToParameterStoreFunction() throws Exception {
        System.setProperty("MAIN_CLASS", App.class.getName());
        System.setProperty("spring.cloud.function.definition", "addClientSecretToParameterStoreFunction");
        CloudFormationTriggerHandler invoker = new CloudFormationTriggerHandler();

        InputStream targetStream = new ByteArrayInputStream(cloudFormationRequest.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        invoker.handleRequest(targetStream, outputStream, null);
    }


}

