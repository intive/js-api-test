package org.intive.patronage.services.test.models.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@SuppressWarnings("unused")
public class RequestData {

    @JsonProperty("httpMethod")
    private String httpMethod;
    @JsonProperty("baseUri")
    private String baseUri;
    @JsonProperty("path")
    private String path;
    @JsonProperty("body")
    private Object body;
    @JsonProperty("headers")
    private Map<String, String> headers;
}
