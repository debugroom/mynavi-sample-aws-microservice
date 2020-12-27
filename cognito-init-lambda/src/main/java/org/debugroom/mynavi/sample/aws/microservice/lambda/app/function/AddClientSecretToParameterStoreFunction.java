package org.debugroom.mynavi.sample.aws.microservice.lambda.app.function;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.DescribeUserPoolClientRequest;
import com.amazonaws.services.cognitoidp.model.DescribeUserPoolClientResult;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.PutParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.PutParameterResult;
import lombok.extern.slf4j.Slf4j;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.CloudFormationStackResolver;
import org.debugroom.mynavi.sample.aws.microservice.lambda.app.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
public class AddClientSecretToParameterStoreFunction implements Function<Map<String, Object>, Message<String>> {

    @Autowired
    ServiceProperties serviceProperties;

    @Autowired
    CloudFormationStackResolver cloudFormationStackResolver;

    @Autowired
    AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    @Autowired
    AWSSimpleSystemsManagement awsSimpleSystemsManagement;

    @Override
    public Message<String> apply(Map<String, Object> stringObjectMap) {
        log.info(this.getClass().getName() +  "  has started!");
        String userPoolId = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getCognito().getUserPoolId());
        String appClientId = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getCognito().getAppClientId());
        String clientSecretParamName = serviceProperties.getSystemsManagerParameterStore().getCognito().getAppClientSecret();
        String clientSecret = getParameterFromParameterStore(clientSecretParamName, true);
        DescribeUserPoolClientRequest describeUserPoolClientRequest = new DescribeUserPoolClientRequest()
                .withUserPoolId(userPoolId)
                .withClientId(appClientId);
        DescribeUserPoolClientResult describeUserPoolClientResult = awsCognitoIdentityProvider
                .describeUserPoolClient(describeUserPoolClientRequest);
        String newClientSecret = describeUserPoolClientResult.getUserPoolClient().getClientSecret();

        if(Objects.nonNull(clientSecret) && !Objects.equals(clientSecret, newClientSecret)){
            PutParameterRequest putParameterRequest = new PutParameterRequest()
                    .withName(clientSecretParamName)
                    .withValue(newClientSecret)
                    .withType("SecureString")
                    .withOverwrite(true);
            PutParameterResult putParameterResult = awsSimpleSystemsManagement
                    .putParameter(putParameterRequest);
        }

        return MessageBuilder.withPayload("Complete!").build();
    }

    private String getParameterFromParameterStore(String paramName, boolean isEncripted){
        GetParameterRequest request = new GetParameterRequest();
        request.setName(paramName);
        request.setWithDecryption(isEncripted);
        GetParameterResult getParameterResult = awsSimpleSystemsManagement.getParameter(request);
        return getParameterResult.getParameter().getValue();
    }

}
