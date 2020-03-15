package org.intive.patronage.services.test;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.intive.patronage.services.test.commons.TestCommons;
import org.json.JSONException;
import org.intive.patronage.services.test.models.common.RequestData;
import org.intive.patronage.services.test.models.common.ResponseData;
import org.intive.patronage.services.test.models.common.TestData;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;

public class TestBase extends TestCommons {

    protected String testCaseName;

    protected String testId;
    protected String resourceCorrelationId;
    private String jira;
    private RequestData requestData;
    protected Object requestBody;
    protected ResponseData responseData;
    private Integer expectedHttpStatus;
    private Response response;
    protected Integer httpStatus;
    private Headers responseHeaders;
    protected String responseBody;
    protected String jsonSchemaFileName;
    protected String jsonValuesFileName;

    protected void setTestClassObject(final TestData testData) {
        this.testCaseName = testData.getTestCaseName();
        this.testId = testData.getTestId();
        this.resourceCorrelationId = testData.getResourceCorrelationId();
        this.jira = testData.getJira();
        this.requestData = testData.getRequestData();
        this.requestBody = requestData.getBody();
        this.responseData = testData.getResponseData();
        this.expectedHttpStatus = responseData.getHttpStatus();
        this.jsonSchemaFileName = responseData.getJsonSchemaFileName();
        this.jsonValuesFileName = responseData.getJsonValuesFileName();
    }

    private void sendHttpRequest(final Object... pathArguments) {
        this.response = sendHttpRequestMethod(requestData, requestBody, pathArguments);
        this.httpStatus = this.response.getStatusCode();
        this.responseHeaders = this.response.getHeaders();
        this.responseBody = this.response.getBody().asString();
    }

    protected void verifyResponseStatusCode() {
        Assert.assertEquals(httpStatus, expectedHttpStatus);
    }

    private void validateResponseBodySchema() {
        validateJsonSchema(jsonSchemaFileName, responseBody);
    }

    private void verifyResponseBodyValues() throws IOException, JSONException {
        verifyJsonValues(jsonValuesFileName, responseBody);
    }

    protected void verifyResponse() throws IOException, JSONException {
        if (expectedHttpStatus != null) {
            verifyResponseStatusCode();
        }
        if (jsonSchemaFileName != null) {
            validateResponseBodySchema();
        }
        if (jsonValuesFileName != null) {
            verifyResponseBodyValues();
        }
    }

    protected void performTest(final Object... pathArguments) throws IOException, JSONException {
        sendHttpRequest(pathArguments);
        logRequestToReporter();
        logResponseToReporter();
        verifyResponse();
    }

    protected void extractObjectToSuiteContext(final ITestContext context, final String name, final Object object) {
        setSuiteContextAttribute(context, (name + "_" + resourceCorrelationId), object);
    }

    protected Object getObjectFromSuiteContextByResourceCorrelationId(final ITestContext context, final String name) {
        final String fixedName = (name + "_" + resourceCorrelationId);
        return getSuiteContextAttribute(context, fixedName);
    }

    protected void verifyResponseHeaderValue(final String headerName, final String expectedValue) {
        Assert.assertEquals(responseHeaders.get(headerName).getValue(), expectedValue);
    }

    protected void logTestNameToReporter() {
        logTestNameToReporter(testCaseName, jira);
    }

    private void logRequestToReporter() {
        logRequestToReporter(testId, requestData, requestBody);
    }

    private void logResponseToReporter() {
        logResponseToReporter(testId, httpStatus, responseHeaders, response.getBody(), response.getTime());
    }



    @BeforeSuite()
    protected void beforeSuite() {
        setRestAssuredTimeout();
        System.setProperty("org.uncommons.reportng.escape-output", "false");
    }
}
