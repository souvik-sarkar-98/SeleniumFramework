package framework.automation.selenium.core.helpers;
public interface TestActionListener {
	void failTestCase(String testCaseName,String message,Exception e);
	void passTestCase(String testCaseName, String message);
	void uploadEvidence(String fileName);
}