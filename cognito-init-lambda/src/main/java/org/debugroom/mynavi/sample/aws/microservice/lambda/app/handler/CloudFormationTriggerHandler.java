package org.debugroom.mynavi.sample.aws.microservice.lambda.app.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.function.adapter.aws.FunctionInvoker;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

import org.debugroom.mynavi.sample.aws.microservice.lambda.app.CloudFormationResponseSender;
import org.debugroom.mynavi.sample.aws.microservice.lambda.app.model.Status;

@Slf4j
public class CloudFormationTriggerHandler extends FunctionInvoker {

    public CloudFormationTriggerHandler(){
        super();
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        final byte[] payload = StreamUtils.copyToByteArray(input);
        Object request = objectMapper.readValue(payload, Object.class);
        log.info(request.toString());
        if(request instanceof Map){
            Map<String, Object> inputMap = (Map<String, Object>) request;
            for(String key : inputMap.keySet()){
                log.info("[Key]" + key + " [Value]" + inputMap.get(key).toString());
            }
            Object requestType = inputMap.get("RequestType");
            if(requestType != null && Objects.equals(requestType.toString(), "Delete")){
                CloudFormationResponseSender.send(inputMap, context, Status.SUCCESS,
                    inputMap.get("ResourceProperties"), inputMap.get("PhysicalResourceId").toString(), false);
            }
            input.reset();
            super.handleRequest(input, output, context);
            if(requestType != null && !Objects.equals("Test", requestType)){
                CloudFormationResponseSender.send(inputMap, context, Status.SUCCESS,
                    inputMap.get("ResourceProperties"), null, false);
            }
        }
    }

}
