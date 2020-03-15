package org.intive.patronage.services.test.models.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class ResponseData {
    @JsonProperty("httpStatus")
    private Integer httpStatus;
    @JsonProperty("jsonSchemaFileName")
    private String jsonSchemaFileName;
    @JsonProperty("jsonValuesFileName")
    private String jsonValuesFileName;
}
