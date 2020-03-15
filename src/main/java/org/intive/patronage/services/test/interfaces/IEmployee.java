package org.intive.patronage.services.test.interfaces;

import org.intive.patronage.services.test.models.responses.newEmployee.CreateEmployeeResponseModel;
import org.testng.SkipException;

public interface IEmployee {

    default String getEmployeeIdFromModel(Object contextObject, String testId) {
        CreateEmployeeResponseModel createEmployeeResponseModel = (CreateEmployeeResponseModel) contextObject;

        String employeeId;

        try {
            employeeId = Integer.toString(createEmployeeResponseModel.getData().getId());
        } catch (NullPointerException e) {
            throw new SkipException("No employeeId for test in suite context - skipping test with testId: " + testId + ".");
        }

        return employeeId;
    }
}
