package org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BusinessExceptionResponse.class, name = "businessExceptionResponse"),
        @JsonSubTypes.Type(value = ValidationErrorResponse.class, name = "validationExceptionResponse"),
        @JsonSubTypes.Type(value = SystemExceptionResponse.class, name = "systemExceptionResponse")
})
public interface ErrorResponse {
}
