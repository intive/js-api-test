package org.intive.patronage.services.test.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intive.patronage.services.test.models.common.RequestData;
import org.intive.patronage.services.test.models.common.TestData;

import java.io.File;

public class TestDataReader extends TestCommons {

    private final String fileName;

    public TestDataReader(final String fileName) {
        this.fileName = fileName;
    }

    public Object[] getTestData() {
        return getTestDataFromFile(fileName);
    }

    public RequestData getRequestData() {
        return getRequestDataFromFile(fileName);
    }

    private Object[] getTestDataFromFile(final String fileName) {
        final File file = getFileFromResources(fileName);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, TestData[].class);
        } catch (final Exception e) {
            throw new RuntimeException("Failed to read data from file ", e);
        }
    }

    private RequestData getRequestDataFromFile(final String fileName) {
        final File file = new File(fileName);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, RequestData.class);
        } catch (final Exception e) {
            throw new RuntimeException("Failed to read data from file ", e);
        }
    }
}
