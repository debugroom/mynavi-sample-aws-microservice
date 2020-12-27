package org.debugroom.mynavi.sample.aws.microservice.lambda.app.function;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.CloudFormationStackResolver;
import org.debugroom.mynavi.sample.aws.microservice.lambda.app.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class AddCognitoUserFunction implements Function<Map<String, Object>, Message<String>> {

    @Autowired
    ServiceProperties serviceProperties;

    @Autowired
    CloudFormationStackResolver cloudFormationStackResolver;

    @Autowired
    AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    @Override
    public Message<String> apply(Map<String, Object> stringObjectMap) {
        log.info(this.getClass().getName() +  "  has started!");
        String userPoolId = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getCognito().getUserPoolId());
        ListUsersRequest listUsersRequest = new ListUsersRequest().withUserPoolId(userPoolId);
        ListUsersResult listUsersResult = awsCognitoIdentityProvider.listUsers(
                listUsersRequest);

        int numberOfCognitoUser = listUsersResult.getUsers().size();
        List<AttributeType> attributeTypes = new ArrayList<>();
        AdminCreateUserRequest adminCreateUserRequest = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withTemporaryPassword("test01");
        attributeTypes.add(new AttributeType().withName("family_name").withValue("mynavi"));
        attributeTypes.add(new AttributeType().withName("given_name").withValue("taro"));
        attributeTypes.add(new AttributeType().withName("custom:isAdmin").withValue("1"));
        if(numberOfCognitoUser != 0){
            adminCreateUserRequest.withUsername("taro.mynavi" + Integer.toString(numberOfCognitoUser));
            attributeTypes.add(new AttributeType()
                    .withName("custom:loginId").withValue("taro.mynavi" + Integer.toString(numberOfCognitoUser)));
        }else {
            adminCreateUserRequest.withUsername("taro.mynavi");
            attributeTypes.add(new AttributeType().withName("custom:loginId").withValue("taro.mynavi"));
        }
        adminCreateUserRequest.withUserAttributes(attributeTypes);
        AdminCreateUserResult adminCreateUserResult = awsCognitoIdentityProvider.adminCreateUser(
                adminCreateUserRequest);
        return MessageBuilder.withPayload("Complete!").build();
    }

}
