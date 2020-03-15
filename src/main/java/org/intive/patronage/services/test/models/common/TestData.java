package org.intive.patronage.services.test.models.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class TestData {

    @JsonProperty("testCaseName")
    private String testCaseName;
    @JsonProperty("testId")
    private String testId;
    @JsonProperty("resourceCorrelationId")
    private String resourceCorrelationId;
    @JsonProperty("jira")
    private String jira;
    @JsonProperty("requestData")
    private RequestData requestData;
    @JsonProperty("responseData")
    private ResponseData responseData;
}
