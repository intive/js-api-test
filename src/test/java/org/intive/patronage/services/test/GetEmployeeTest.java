package org.intive.patronage.services.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intive.patronage.services.test.interfaces.IEmployee;
import org.intive.patronage.services.test.models.common.TestData;
import org.intive.patronage.services.test.models.responses.newEmployee.CreateEmployeeResponseModel;
import org.json.JSONException;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.IOException;

public class GetEmployeeTest extends TestBase implements IEmployee {

    private String employeeId;

    @DataProvider(name = "testData")
    public static Object[] provideTestData(final ITestContext context) {
        return provideTestData(GetEmployeeTest.class, context);
    }

    @Factory(dataProvider = "testData")
    public GetEmployeeTest(final TestData testData) {
        setTestClassObject(testData);
    }

    @BeforeClass
    public void setEmployeeId(final ITestContext context) {
        employeeId = getEmployeeIdFromModel(getObjectFromSuiteContextByResourceCorrelationId(context, "createEmployeeResponseModel"), testId);
    }

    @Test()
    public void getEmployeeTest() throws IOException, JSONException {
        logTestNameToReporter();
        performTest(employeeId);
    }
}
