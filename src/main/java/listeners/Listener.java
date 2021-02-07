package listeners;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import resources.Base;
import utilities.ExtentReporter;

public class Listener extends Base implements ITestListener{

	public WebDriver driver;
	ExtentReports extentReport = ExtentReporter.getExtentReport();
	ExtentTest extentTest;
	
	
	ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<ExtentTest>();
	
	@Override
	public void onTestStart(ITestResult result) {
		
		String testName = result.getName();
		
		//extentTest = extentReport.createTest(result.getName()); This was used when we weren't executing the tests in parallel
		extentTest = extentReport.createTest(testName);
		extentTestThread.set(extentTest);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		
		String testName = result.getName();
		
		//extentTest.log(Status.PASS, "Test Passed"); This was used when we weren't executing the tests in parallel
		extentTestThread.get().log(Status.PASS, testName+ "Test Passed"); 
		
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		
		String testName = result.getName();
		//extentTest.fail(result.getThrowable());
		extentTestThread.get().fail(result.getThrowable());
		
		 
		try {
			
			driver=(WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		try {
			
			String screenShotFilePath = takeScreenshot(testName, driver);
			extentTestThread.get().addScreenCaptureFromPath(screenShotFilePath, testName);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		 
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		 
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		 
	}

	@Override
	public void onStart(ITestContext context) {
		 
	}

	@Override
	public void onFinish(ITestContext context) {
		
		extentReport.flush();
		 
	}

}
