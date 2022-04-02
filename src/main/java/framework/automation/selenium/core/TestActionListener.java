package framework.automation.selenium.core;
public interface TestActionListener {
	default void failTestCase(String testCaseName,String message,Exception e) {}
	default void passTestCase(String testCaseName, String message) {}
	void failTestCase(String testCaseName,String message,Exception e,String screenshotPath);
	void passTestCase(String testCaseName, String message,String screenshotPath);
	void uploadEvidence(String fileName);
}