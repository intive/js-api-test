package org.intive.patronage.services.test.commons;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Map;

public class LocalTestListener implements ITestListener {
    private Map<String, Object> attributes;

    public LocalTestListener(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(final ITestContext context) {
        attributes.forEach((key, value) -> context.getSuite().setAttribute(key, value));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }
}
