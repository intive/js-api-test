package org.intive.patronage.services.test.commons;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.intive.patronage.services.test.models.common.RequestData;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.*;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

public abstract class TestCommons {

    protected void setRestAssuredTimeout() {
        RestAssured.config = RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().
                setParam("http.connection.timeout", 5000).
                setParam("http.socket.timeout", 120000).
                setParam("http.connection-manager.timeout", 5000));
    }

    protected void validateJsonSchema(final String jsonSchemaFileName, final String json) throws AssertionError {
        final String fileName = setJsonFileName(jsonSchemaFileName, jsonReadMode.schema);
        final File file = getFileFromResources(fileName);
        assertThat(json, JsonSchemaValidator.matchesJsonSchema(file));
    }

    protected void verifyJsonValues(final String jsonValuesFileName, final String json) throws IOException, JSONException {
        final String fileName = setJsonFileName(jsonValuesFileName, jsonReadMode.values);
        final File file = getFileFromResources(fileName);
        final String expectedJson = FileUtils.readFileToString(file, "UTF-8");
        JSONAssert.assertEquals(expectedJson, json, false);
    }

    private String buildStringWithInterpolation(final String pattern, final Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    private RequestSpecification createRequestSpecification(final String baseUri, @Nullable final Map<String, String> headers, @Nullable final Object body) {
        RestAssured.baseURI = baseUri;
        final RequestSpecification httpRequest = RestAssured.given();
        httpRequest.relaxedHTTPSValidation();
        httpRequest.log().all();
        httpRequest.filter(new ResponseLoggingFilter());

        if (headers != null) {
            httpRequest.headers(headers);
        }

        if (body != null) {
            httpRequest.body(body);
        }

        return httpRequest;
    }

    protected Response sendHttpRequestMethod(final RequestData requestData, final Object requestBody, final Object... pathArguments) {
        final String httpMethod = requestData.getHttpMethod();
        final String baseUri = requestData.getBaseUri();
        requestData.setPath(buildStringWithInterpolation(requestData.getPath(), pathArguments));
        final String path = requestData.getPath();
        final Map<String, String> headers = requestData.getHeaders();
        final Object body = requestBody;

        //given
        final RequestSpecification httpRequest = createRequestSpecification(baseUri, headers, body);

        //when
        Response response = null;

        switch (httpMethod) {
            case "POST":
                response = httpRequest.when().post(path);
                break;
            case "GET":
                response = httpRequest.when().get(path);
                break;
            case "PUT":
                response = httpRequest.when().put(path);
                break;
            case "PATCH":
                response = httpRequest.when().patch(path);
                break;
            case "DELETE":
                response = httpRequest.when().delete(path);
                break;
        }

        return response;

    }

    private String setJsonFileName(final String fileName, final jsonReadMode mode) {
        final String packageName = this.getClass().getPackage().getName().replace(".", "/");
        switch (mode) {
            case schema:
                return (packageName + "/responseBodySchema/" + fileName);
            case values:
                return (packageName + "/responseBodyValues/" + fileName);

        }
        return null;
    }

    public File getFileFromResources(final String fileName) {

        final ClassLoader classLoader = getClass().getClassLoader();

        final URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file " + fileName + " not found!");
        } else {
            return new File(resource.getFile());
        }
    }

    protected void logTestNameToReporter(final String testCaseName, final String jira) {
        if (!(testCaseName == null)) {
            Reporter.log("<p style='color: black; font-size: 13px;font-family:Lucida Sans Unicode, Lucida Grande, sans-serif'>" + testCaseName + "</p>");
        }
        if (!(jira == null)) {
            Reporter.log("<b><a target=\"_blank\" href=\"" + jira + "\">" + jira + "</a></b><br>");
        }
    }

    protected void logRequestToReporter(final String testId, final RequestData requestData, final Object requestBody) {
        final String method = requestData.getHttpMethod();
        final String url = requestData.getBaseUri() + requestData.getPath();
        String headers = "";
        String body = "";
        if (requestData.getHeaders() != null) {
            for (final Map.Entry<String, String> entry : requestData.getHeaders().entrySet()) {
                headers = headers + entry.getKey() + ": " + entry.getValue() + "<br>";
            }
        }

        if (requestBody != null) {
            body = requestBody.toString();
        }

        Reporter.log("<a href=\"javascript:toggleElement('" + testId + "Request', 'block')\">" +
                "<b>Request:</b>" +
                "</a>" +
                "<div id=\"" + testId + "Request\" style=\"display: none; width: 1105px; word-wrap: break-word;\">" +
                "<b>HTTP method: </b>" + method +
                "<br>" +
                "<b>HTTP headers: </b><br>" + headers +
                "<br>" +
                "<b>URL: </b>" + url +
                "<br>" +
                "<b>Request body: </b><br>" + body +
                "<br>" +
                "</div>" +
                "<br>");
    }

    protected void logResponseToReporter(final String testId, final Integer httpStatus, final Headers responseHeaders, final ResponseBody responseBody, final Long time) {
        final List<Header> headersList = responseHeaders.asList();
        String headers = "";
        for (final Header header : headersList) {
            headers = headers + header + "<br>";
        }


            Reporter.log("<br><a href=\"javascript:toggleElement('" + testId + "Response', 'block')\">" +
                    "<b>Response:</b>" +
                    "</a>" +
                    "<div id=\"" + testId + "Response\" style=\"display: none; width: 1105px; word-wrap: break-word;\">" +
                    "<b>HTTP status: </b>" + httpStatus +
                    "<br>" +
                    "<b>Response headers: </b><br>" + headers +
                    "<br>" +
                    "<b>Response body: </b><br>" + responseBody.prettyPrint() +
                    "<br>" +
                    "<b>Response time: </b><br>" + time +
                    "<br>" +
                    "</div>" +
                    "<br>" +
                    "<br>");
    }

    protected void setSuiteContextAttribute(final ITestContext context, final String name, final Object value) {
        context.getSuite().setAttribute(name, value);
    }

    protected Object getSuiteContextAttribute(final ITestContext context, final String name) {
        return context.getSuite().getAttribute(name);
    }

    private static String setTestDataFileName(final Class testClass, final String fileName) {
        final String packageName = testClass.getPackage().getName().replace(".", "/");
        return packageName + "/testData/" + fileName;
    }

    protected static Object[] provideTestData(final Class testClass, final ITestContext context) {
        final String testDataFileSimpleName = context.getCurrentXmlTest().getParameter(testClass.getSimpleName());
        final String testDataFileName = setTestDataFileName(testClass, testDataFileSimpleName);
        final TestDataReader testDataReader = new TestDataReader(testDataFileName);
        return testDataReader.getTestData();
    }

    protected static String getParameterFromXml(final String paramName, final ITestContext context) {
        return context.getCurrentXmlTest().getParameter(paramName);
    }

    protected void runTestClassesLocally(ITestContext context, Class[] testClassess) {
        TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
        final XmlSuite xmlSuite = new XmlSuite();

        final Set<String> globalContextAttributesNames = context.getSuite().getAttributeNames();
        Map<String, Object> globalContextAttributes = new HashMap<>();
        for (final String globalContextAttributeName : globalContextAttributesNames) {
            globalContextAttributes.put(globalContextAttributeName, context.getSuite().getAttribute(globalContextAttributeName));
        }

        LocalTestListener localTestListener = new LocalTestListener(globalContextAttributes);

        final XmlTest xmlTest = new XmlTest(xmlSuite);
        Map<String, String> xmlParams = context.getCurrentXmlTest().getAllParameters();
        xmlParams.forEach((key, value) -> xmlTest.addParameter(key, value));
        final List<XmlClass> xmlClassesList = new ArrayList<>();

        for (Class testClass : testClassess) {
            final XmlClass xmlClass = new XmlClass(testClass);
            xmlClassesList.add(xmlClass);
        }

        xmlTest.setXmlClasses(xmlClassesList);

        final List<XmlSuite> xmlSuitesList = new ArrayList<>();
        xmlSuitesList.add(xmlSuite);

        final TestNG testNg = new TestNG();
        testNg.addListener((ITestNGListener) testListenerAdapter);
        testNg.addListener((ITestNGListener) localTestListener);

        testNg.setXmlSuites(xmlSuitesList);
        testNg.setVerbose(2);
        testNg.run();
        final List<ITestContext> contextList = testListenerAdapter.getTestContexts();
        final ITestContext localContext = contextList.get(0);
        final Set<String> localContextAttributesNames = localContext.getSuite().getAttributeNames();

        for (final String localContextAttributeName : localContextAttributesNames) {
            context.getSuite().setAttribute(localContextAttributeName, localContext.getSuite().getAttribute(localContextAttributeName));
        }
    }

    private enum jsonReadMode {
        schema,
        values
    }
}