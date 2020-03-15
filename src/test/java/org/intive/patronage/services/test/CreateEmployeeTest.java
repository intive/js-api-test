package org.intive.patronage.services.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intive.patronage.services.test.models.common.TestData;
import org.intive.patronage.services.test.models.responses.newEmployee.CreateEmployeeResponseModel;
import org.json.JSONException;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.io.IOException;

public class CreateEmployeeTest extends TestBase{
    @DataProvider(name = "testData")
    public static Object[] provideTestData(final ITestContext context) {
        return provideTestData(CreateEmployeeTest.class, context);
    }

    @Factory(dataProvider = "testData")
    public CreateEmployeeTest(final TestData testData) {
        setTestClassObject(testData);
    }

    @Test()
    public void createEmployeeTest() throws IOException, JSONException {
        logTestNameToReporter();
        performTest();
    }

    @AfterMethod
    public void extractCreateEmployeeResponseDataModel(final ITestContext context) throws IOException {
        if (httpStatus == 200) {
            final ObjectMapper mapper = new ObjectMapper();
            final CreateEmployeeResponseModel createEmployeeResponseModel = mapper.readValue(responseBody, CreateEmployeeResponseModel.class);
            extractObjectToSuiteContext(context, "createEmployeeResponseModel", createEmployeeResponseModel);
        }
    }
}
