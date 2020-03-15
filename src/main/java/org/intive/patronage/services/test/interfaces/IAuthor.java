package org.intive.patronage.services.test.interfaces;

import org.intive.patronage.services.test.models.responses.authors.CreateAuthorResponseModel;
import org.intive.patronage.services.test.models.responses.newEmployee.CreateEmployeeResponseModel;
import org.testng.SkipException;

public interface IAuthor {
    default String getAuthorIdFromModel(Object contextObject, String testId) {
        CreateAuthorResponseModel createAuthorResponseModel = (CreateAuthorResponseModel) contextObject;

        String authorId;

        try {
            authorId = Integer.toString(createAuthorResponseModel.getData().getId());
        } catch (NullPointerException e) {
            throw new SkipException("No authorId for test in suite context - skipping test with testId: " + testId + ".");
        }

        return authorId;
    }
}
