package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PortalInformation implements Serializable {

    UserResource userResource;

}
