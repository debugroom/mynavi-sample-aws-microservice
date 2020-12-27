package org.debugroom.mynavi.sample.aws.microservice.lambda.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.debugroom.mynavi.sample.aws.microservice.lambda.app.model.CfnResponse;
import org.debugroom.mynavi.sample.aws.microservice.lambda.app.model.Status;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class CloudFormationResponseSender {

    public static <T> void send(Map<String, Object> event, Context context,
                                Status status, T responseData, String physicalResourceId,
                                boolean noEcho){
        Object responseURL = event.get("ResponseURL");

        ObjectMapper mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        RestOperations restOperations = new RestTemplateBuilder()
                .messageConverters(Arrays.asList(converter))
                .build();

        if(responseURL != null){
            log.info("ResponseURL : " + responseURL.toString());
            CfnResponse<T> cfnResponse = CfnResponse.<T>builder()
                    .Status(status)
                    .Reason("See the details in CloudWatch Log Stream: " + context.getLogStreamName())
                    .PhysicalResourceId(physicalResourceId == null ?
                            context.getLogStreamName(): physicalResourceId)
                    .StackId(event.get("StackId").toString())
                    .RequestId(event.get("RequestId").toString())
                    .LogicalResourceId(event.get("LogicalResourceId").toString())
                    .NoEcho(noEcho)
                    .Data(responseData)
                    .build();


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CfnResponse> requestEntity = new HttpEntity<>(cfnResponse, headers);
            restOperations.exchange(getUri(responseURL.toString()),
                    HttpMethod.PUT, requestEntity, Void.class);
        }else {
            throw new IllegalStateException("No ResponseURL to send.");
        }

    }

    protected static URI getUri(String responseURL){

        String url = responseURL.split("\\?")[0];
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        try {
            String[] urlParams = responseURL.split("\\?")[1].split("&");
            for(String param : urlParams){
                String key = param.split("=")[0];
                String value = URLDecoder.decode(param.split("=")[1], StandardCharsets.UTF_8.toString());
                params.put(key, Collections.singletonList(URLEncoder.encode(value, "UTF-8")));
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params)
                .build(true)
                .toUri();
    }

}
