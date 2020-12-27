package org.debugroom.mynavi.sample.aws.microservice.lambda.app.function;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.CloudFormationStackResolver;
import org.debugroom.mynavi.sample.aws.microservice.lambda.app.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
public class ChangeCognitoUserStatusFunction implements Function<Map<String, Object>, Message<String>> {

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
        String clientSecret = getParameterFromParameterStore(
                serviceProperties.getSystemsManagerParameterStore().getCognito().getAppClientSecret(), true);
        ListUsersRequest listUsersRequest = new ListUsersRequest().withUserPoolId(userPoolId);
        ListUsersResult listUsersResult = awsCognitoIdentityProvider.listUsers(
                listUsersRequest);

        listUsersResult.getUsers().stream()
                .filter(userType -> Objects.equals(userType.getUserStatus(), "FORCE_CHANGE_PASSWORD"))
                .forEach(userType -> {
                    AdminInitiateAuthRequest adminInitiateAuthRequest
                            = adminInitiateAuthRequest(userType.getUsername(), userPoolId, appClientId, clientSecret);
                    AdminInitiateAuthResult adminInitiateAuthResult =
                            awsCognitoIdentityProvider.adminInitiateAuth(adminInitiateAuthRequest);

                    if(Objects.equals(adminInitiateAuthResult.getChallengeName(), "NEW_PASSWORD_REQUIRED")){
                        Map<String, String> challengeResponses = new HashMap<>();
                        challengeResponses.put("USERNAME", userType.getUsername());
                        challengeResponses.put("NEW_PASSWORD", "test02");
                        challengeResponses.put("userAttributes.given_name",
                                userType.getAttributes().stream().filter(attributeType ->
                                    Objects.equals(attributeType.getName(), "given_name")).findFirst().get().getValue());
                        challengeResponses.put("SECRET_HASH", calculateSecretHash(appClientId, clientSecret, userType.getUsername()));

                        AdminRespondToAuthChallengeRequest adminRespondToAuthChallengeRequest =
                                new AdminRespondToAuthChallengeRequest()
                                .withChallengeName(adminInitiateAuthResult.getChallengeName())
                                .withUserPoolId(userPoolId)
                                .withClientId(appClientId)
                                .withSession(adminInitiateAuthResult.getSession())
                                .withChallengeResponses(challengeResponses);

                        AdminRespondToAuthChallengeResult adminRespondToAuthChallengeResult =
                                awsCognitoIdentityProvider.adminRespondToAuthChallenge(adminRespondToAuthChallengeRequest);
                    }

                });

        return MessageBuilder.withPayload("Complete!").build();
    }

    private AdminInitiateAuthRequest adminInitiateAuthRequest(
            String userName, String userPoolId, String appClientId, String clientSecret){
        AdminInitiateAuthRequest adminInitiateAuthRequest = new AdminInitiateAuthRequest();
        adminInitiateAuthRequest.setAuthFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH);
        adminInitiateAuthRequest.setUserPoolId(userPoolId);
        adminInitiateAuthRequest.setClientId(appClientId);
        Map<String, String> authParameters = new HashMap<>();
        authParameters.put("USERNAME", userName);
        authParameters.put("PASSWORD", "test01");
        authParameters.put("SECRET_HASH", calculateSecretHash(appClientId, clientSecret, userName));
        adminInitiateAuthRequest.setAuthParameters(authParameters);
        return adminInitiateAuthRequest;
    }

    private static String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        SecretKeySpec signingKey = new SecretKeySpec(
                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating ");
        }
    }

    private String getParameterFromParameterStore(String paramName, boolean isEncripted){
        GetParameterRequest request = new GetParameterRequest();
        request.setName(paramName);
        request.setWithDecryption(isEncripted);
        return awsSimpleSystemsManagement.getParameter(request).getParameter().getValue();
    }

}
