package org.debugroom.mynavi.sample.aws.microservice.lambda.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CfnResponse<T> {

    private Status Status;
    private String Reason;
    private String PhysicalResourceId;
    private String StackId;
    private String RequestId;
    private String LogicalResourceId;
    private boolean NoEcho;
    private T Data;

}
