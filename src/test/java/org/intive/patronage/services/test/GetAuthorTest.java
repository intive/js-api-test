package org.intive.patronage.services.test;

import org.intive.patronage.services.test.interfaces.IAuthor;
import org.intive.patronage.services.test.models.common.TestData;
import org.json.JSONException;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.io.IOException;

public class GetAuthorTest extends TestBase implements IAuthor {
    private String authorId;

    @DataProvider(name = "testData")
    public static Object[] provideTestData(final ITestContext context) {
        return provideTestData(GetAuthorTest.class, context);
    }

    @Factory(dataProvider = "testData")
    public GetAuthorTest(final TestData testData) {
        setTestClassObject(testData);
    }


    @Test()
    public void getAuthorTest() throws IOException, JSONException {
        logTestNameToReporter();
        performTest();
    }
}
